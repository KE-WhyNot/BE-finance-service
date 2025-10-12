package com.youthfi.finance.domain.stock.domain.entity;

import com.youthfi.finance.domain.stock.domain.repository.ExecutionRepository;
import com.youthfi.finance.domain.stock.domain.repository.InterestStockRepository;
import com.youthfi.finance.domain.stock.domain.repository.SectorRepository;
import com.youthfi.finance.domain.stock.domain.repository.StockRepository;
import com.youthfi.finance.domain.stock.domain.repository.UserStockRepository;
import com.youthfi.finance.domain.stock.domain.service.InterestStockService;
import com.youthfi.finance.domain.stock.application.usecase.StockApiUseCase;
import com.youthfi.finance.domain.stock.domain.service.UserStockService;
import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.domain.user.domain.repository.UserRepository;
import com.youthfi.finance.domain.user.domain.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
@DisplayName("사용자 주식 거래 통합 테스트") // 현재가로 매수,매도 테스트는 postman으로 테스트, 확인됨.
class UserStockIntegrationTest {

    @Autowired
    private UserService userService;
    
    @MockBean
    private StockApiUseCase stockApiUseCase;
    
    @Autowired
    private InterestStockService interestStockService;
    
    @Autowired
    private UserStockService userStockService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StockRepository stockRepository;
    
    @Autowired
    private SectorRepository sectorRepository;
    
    @Autowired
    private ExecutionRepository executionRepository;
    
    @Autowired
    private InterestStockRepository interestStockRepository;
    
    @Autowired
    private UserStockRepository userStockRepository;

    private User testUser;
    private Stock samsungStock;
    private Stock skHynixStock;
    private Sector electronicsSector;
    private Sector chemicalSector;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
        setupTestData();
    }

    private void setupTestData() {
        // 섹터 생성
        electronicsSector = Sector.builder()
                .sectorName("전기·전자")
                .build();
        sectorRepository.save(electronicsSector);

        chemicalSector = Sector.builder()
                .sectorName("화학")
                .build();
        sectorRepository.save(chemicalSector);

        // 주식 생성
        samsungStock = Stock.builder()
                .stockId("005930")
                .stockName("삼성전자")
                .sector(electronicsSector)
                .build();
        stockRepository.save(samsungStock);

        skHynixStock = Stock.builder()
                .stockId("000660")
                .stockName("SK하이닉스")
                .sector(chemicalSector)
                .build();
        stockRepository.save(skHynixStock);

        // 사용자 생성 (테스트용으로 잔고를 더 많이 설정)
        testUser = userService.createUser("integration_test_user");
        // 테스트를 위해 잔고를 20,000,000원으로 설정
        testUser.addBalance(new BigDecimal("10000000"));
        userRepository.save(testUser);
    }

    @Test
    @DisplayName("사용자 주식 거래 전체 시나리오 - 삼성전자 매수/매도")
    void userStockTradingScenario_Samsung() {
        // given
        String userId = testUser.getUserId();
        String stockId = samsungStock.getStockId();
        Long quantity = 100L;
        BigDecimal buyPrice = new BigDecimal("70000");
        BigDecimal sellPrice = new BigDecimal("75000");

        // when & then
        // 1. 초기 잔고 확인
        assertThat(testUser.getBalance()).isEqualTo(new BigDecimal("20000000"));

        // 2. 삼성전자 매수 (직접 처리)
        Execution buyExecution = executeBuyWithMockPrice(stockId, quantity, buyPrice);
        
        // 3. 매수 후 잔고 확인
        User updatedUser = userRepository.findById(userId).orElseThrow();
        BigDecimal expectedBalanceAfterBuy = new BigDecimal("20000000").subtract(buyPrice.multiply(BigDecimal.valueOf(quantity)));
        assertThat(updatedUser.getBalance()).isEqualTo(expectedBalanceAfterBuy);

        // 4. 보유주식 확인
        Optional<UserStock> userStockOpt = userStockService.getUserStockByUserIdAndStockId(userId, stockId);
        assertThat(userStockOpt).isPresent();
        UserStock userStock = userStockOpt.get();
        assertThat(userStock.getHoldingQuantity()).isEqualTo(quantity);
        assertThat(userStock.getAvgPrice()).isEqualByComparingTo(buyPrice);
        assertThat(userStock.getTotalValue()).isEqualByComparingTo(buyPrice.multiply(BigDecimal.valueOf(quantity)));

        // 5. 거래 내역 확인
        List<Execution> executions = executionRepository.findByUserUserIdOrderByExecutedAtDesc(userId);
        assertThat(executions).hasSize(1);
        assertThat(executions.get(0).getExecutionType()).isEqualTo(Execution.ExecutionType.BUY);
        assertThat(executions.get(0).getQuantity()).isEqualTo(quantity);
        assertThat(executions.get(0).getPrice()).isEqualTo(buyPrice);

        // 6. 삼성전자 매도
        Execution sellExecution = executeSellWithMockPrice(stockId, 50L, sellPrice);

        // 7. 매도 후 잔고 확인
        User userAfterSell = userRepository.findById(userId).orElseThrow();
        BigDecimal expectedBalanceAfterSell = expectedBalanceAfterBuy.add(sellPrice.multiply(BigDecimal.valueOf(50L)));
        assertThat(userAfterSell.getBalance()).isEqualTo(expectedBalanceAfterSell);

        // 8. 보유주식 수량 확인
        UserStock userStockAfterSell = userStockService.getUserStockByUserIdAndStockId(userId, stockId).orElseThrow();
        assertThat(userStockAfterSell.getHoldingQuantity()).isEqualTo(50L);
        assertThat(userStockAfterSell.getAvgPrice()).isEqualByComparingTo(buyPrice); // 평균 매입가는 유지

        // 9. 최종 거래 내역 확인
        List<Execution> allExecutions = executionRepository.findByUserUserIdOrderByExecutedAtDesc(userId);
        assertThat(allExecutions).hasSize(2);
        
        // 매수 내역
        Execution buyExec = allExecutions.stream()
                .filter(e -> e.getExecutionType() == Execution.ExecutionType.BUY)
                .findFirst().orElseThrow();
        assertThat(buyExec.getQuantity()).isEqualTo(100L);
        
        // 매도 내역
        Execution sellExec = allExecutions.stream()
                .filter(e -> e.getExecutionType() == Execution.ExecutionType.SELL)
                .findFirst().orElseThrow();
        assertThat(sellExec.getQuantity()).isEqualTo(50L);
    }

    @Test
    @DisplayName("관심종목 관리 시나리오")
    void interestStockManagementScenario() {
        // given
        String userId = testUser.getUserId();
        String samsungStockId = samsungStock.getStockId();
        String skHynixStockId = skHynixStock.getStockId();

        // when & then
        // 1. 초기 관심종목 개수 확인
        long initialCount = interestStockService.getInterestStockCountByUserId(userId);
        assertThat(initialCount).isZero();

        // 2. 삼성전자 관심종목 추가
        InterestStock samsungInterest = interestStockService.addInterestStock(userId, samsungStockId);
        assertThat(samsungInterest.getInterestFlag()).isTrue();
        assertThat(samsungInterest.getStock().getStockId()).isEqualTo(samsungStockId);

        // 3. SK하이닉스 관심종목 추가
        InterestStock skHynixInterest = interestStockService.addInterestStock(userId, skHynixStockId);
        assertThat(skHynixInterest.getInterestFlag()).isTrue();

        // 4. 관심종목 목록 확인
        List<InterestStock> interestStocks = interestStockService.getInterestStocksByUserId(userId);
        assertThat(interestStocks).hasSize(2);
        assertThat(interestStocks).extracting(InterestStock::getStock)
                .extracting(Stock::getStockId)
                .containsExactlyInAnyOrder(samsungStockId, skHynixStockId);

        // 5. 관심종목 개수 확인
        long countAfterAdd = interestStockService.getInterestStockCountByUserId(userId);
        assertThat(countAfterAdd).isEqualTo(2);

        // 6. 특정 종목 관심 여부 확인
        assertThat(interestStockService.isInterestStock(userId, samsungStockId)).isTrue();
        assertThat(interestStockService.isInterestStock(userId, skHynixStockId)).isTrue();

        // 7. 관심종목 토글 (삼성전자)
        InterestStock toggledSamsung = interestStockService.toggleInterestStock(userId, samsungStockId);
        assertThat(toggledSamsung.getInterestFlag()).isFalse();

        // 8. 토글 후 관심종목 개수 확인
        long countAfterToggle = interestStockService.getInterestStockCountByUserId(userId);
        assertThat(countAfterToggle).isEqualTo(1);

        // 9. 관심종목 제거 (SK하이닉스)
        interestStockService.removeInterestStock(userId, skHynixStockId);
        
        // 10. 제거 후 관심종목 개수 확인
        long countAfterRemove = interestStockService.getInterestStockCountByUserId(userId);
        assertThat(countAfterRemove).isZero();
    }

    @Test
    @DisplayName("복합 시나리오 - 거래 + 관심종목 + 보유주식 관리")
    void complexTradingScenario() {
        // given
        String userId = testUser.getUserId();
        String samsungStockId = samsungStock.getStockId();
        String skHynixStockId = skHynixStock.getStockId();

        // when & then
        // 1. 관심종목 추가
        interestStockService.addInterestStock(userId, samsungStockId);
        interestStockService.addInterestStock(userId, skHynixStockId);

        // 2. 삼성전자 매수 (100주)
        BigDecimal samsungPrice = new BigDecimal("70000");
        executeBuyWithMockPrice(samsungStockId, 100L, samsungPrice);

        // 3. SK하이닉스 매수 (50주)
        BigDecimal skHynixPrice = new BigDecimal("80000");
        executeBuyWithMockPrice(skHynixStockId, 50L, skHynixPrice);

        // 4. 보유주식 목록 확인
        List<UserStock> userStocks = userStockService.getUserStocksByUserId(userId);
        assertThat(userStocks).hasSize(2);
        
        // 5. 각 종목별 보유주식 확인
        UserStock samsungUserStock = userStocks.stream()
                .filter(us -> us.getStock().getStockId().equals(samsungStockId))
                .findFirst().orElseThrow();
        assertThat(samsungUserStock.getHoldingQuantity()).isEqualTo(100L);
        assertThat(samsungUserStock.getAvgPrice()).isEqualByComparingTo(samsungPrice);

        UserStock skHynixUserStock = userStocks.stream()
                .filter(us -> us.getStock().getStockId().equals(skHynixStockId))
                .findFirst().orElseThrow();
        assertThat(skHynixUserStock.getHoldingQuantity()).isEqualTo(50L);
        assertThat(skHynixUserStock.getAvgPrice()).isEqualByComparingTo(skHynixPrice);

        // 6. 추가 매수 (평균 매입가 계산 확인)
        BigDecimal additionalSamsungPrice = new BigDecimal("75000");
        executeBuyWithMockPrice(samsungStockId, 50L, additionalSamsungPrice);

        // 7. 평균 매입가 재계산 확인
        UserStock updatedSamsungStock = userStockService.getUserStockByUserIdAndStockId(userId, samsungStockId).orElseThrow();
        assertThat(updatedSamsungStock.getHoldingQuantity()).isEqualTo(150L);
        
        // 평균 매입가 = (70000 * 100 + 75000 * 50) / 150 = 71666.67
        BigDecimal expectedAvgPrice = new BigDecimal("71666.67");
        assertThat(updatedSamsungStock.getAvgPrice()).isEqualByComparingTo(expectedAvgPrice);

        // 8. 부분 매도
        executeSellWithMockPrice(samsungStockId, 30L, new BigDecimal("80000"));

        // 9. 매도 후 보유수량 확인
        UserStock finalSamsungStock = userStockService.getUserStockByUserIdAndStockId(userId, samsungStockId).orElseThrow();
        assertThat(finalSamsungStock.getHoldingQuantity()).isEqualTo(120L);

        // 10. 최종 거래 내역 확인
        List<Execution> allExecutions = executionRepository.findByUserUserIdOrderByExecutedAtDesc(userId);
        assertThat(allExecutions).hasSize(4); // 삼성 2번, SK하이닉스 1번, 삼성 매도 1번

        // 11. 매수/매도별 거래 내역 확인
        List<Execution> buyExecutions = allExecutions.stream()
                .filter(e -> e.getExecutionType() == Execution.ExecutionType.BUY)
                .toList();
        assertThat(buyExecutions).hasSize(3);

        List<Execution> sellExecutions = allExecutions.stream()
                .filter(e -> e.getExecutionType() == Execution.ExecutionType.SELL)
                .toList();
        assertThat(sellExecutions).hasSize(1);

        // 12. 최종 잔고 확인
        User finalUser = userRepository.findById(userId).orElseThrow();
        BigDecimal totalSpent = samsungPrice.multiply(BigDecimal.valueOf(100L))
                .add(skHynixPrice.multiply(BigDecimal.valueOf(50L)))
                .add(additionalSamsungPrice.multiply(BigDecimal.valueOf(50L)));
        BigDecimal totalEarned = new BigDecimal("80000").multiply(BigDecimal.valueOf(30L));
        BigDecimal expectedFinalBalance = new BigDecimal("20000000").subtract(totalSpent).add(totalEarned);
        
        assertThat(finalUser.getBalance()).isEqualByComparingTo(expectedFinalBalance);
    }

    @Test
    @DisplayName("에러 시나리오 - 잔고 부족으로 매수 실패")
    void insufficientBalanceScenario() {
        // given
        String userId = testUser.getUserId();
        String stockId = samsungStock.getStockId();
        BigDecimal veryHighPrice = new BigDecimal("300000"); // 3억원 (잔고 2억원보다 많음)
        Long quantity = 100L;

        // when & then
        // 잔고 부족으로 매수 실패해야 함
        assertThatThrownBy(() -> executeBuyWithMockPrice(stockId, quantity, veryHighPrice))
                .isInstanceOf(Exception.class); // StockException이 발생해야 함

        // 잔고는 변경되지 않아야 함
        User user = userRepository.findById(userId).orElseThrow();
        assertThat(user.getBalance()).isEqualTo(new BigDecimal("20000000"));
    }

    @Test
    @DisplayName("에러 시나리오 - 보유수량 부족으로 매도 실패")
    void insufficientStockQuantityScenario() {
        // given
        String userId = testUser.getUserId();
        String stockId = samsungStock.getStockId();
        BigDecimal price = new BigDecimal("70000");
        Long buyQuantity = 50L;
        Long sellQuantity = 100L; // 보유수량보다 많음

        // when
        // 1. 먼저 매수
        executeBuyWithMockPrice(stockId, buyQuantity, price);

        // 2. 보유수량보다 많은 수량으로 매도 시도
        assertThatThrownBy(() -> executeSellWithMockPrice(stockId, sellQuantity, price))
                .isInstanceOf(Exception.class); // StockException이 발생해야 함

        // 3. 보유수량은 변경되지 않아야 함
        UserStock userStock = userStockService.getUserStockByUserIdAndStockId(userId, stockId).orElseThrow();
        assertThat(userStock.getHoldingQuantity()).isEqualTo(buyQuantity);
    }

    // Mock 현재가를 사용한 매수/매도 헬퍼 메서드
    private Execution executeBuyWithMockPrice(String stockId, Long quantity, BigDecimal mockPrice) {
        // 실제 TradingService의 executeBuyAtCurrentPrice는 외부 API를 호출하므로
        // 테스트용으로 직접 Execution을 생성하고 UserStock을 업데이트
        User user = userRepository.findById(testUser.getUserId()).orElseThrow();
        Stock stock = stockRepository.findById(stockId).orElseThrow();
        
        BigDecimal totalPrice = mockPrice.multiply(BigDecimal.valueOf(quantity));
        user.subtractBalance(totalPrice);
        userRepository.save(user);

        Execution execution = Execution.builder()
                .user(user)
                .stock(stock)
                .sector(stock.getSector())
                .executedAt(java.time.LocalDateTime.now())
                .executionType(Execution.ExecutionType.BUY)
                .quantity(quantity)
                .price(mockPrice)
                .totalPrice(totalPrice)
                .build();
        Execution savedExecution = executionRepository.save(execution);

        // UserStock 업데이트
        UserStock userStock = userStockRepository.findByUserUserIdAndStockStockId(user.getUserId(), stockId)
                .orElseGet(() -> userStockRepository.save(
                        UserStock.builder()
                                .user(user)
                                .stock(stock)
                                .sector(stock.getSector())
                                .holdingQuantity(0L)
                                .avgPrice(BigDecimal.ZERO)
                                .totalValue(BigDecimal.ZERO)
                                .build()
                ));
        userStock.addQuantity(quantity, mockPrice);
        userStockRepository.save(userStock);

        return savedExecution;
    }

    private Execution executeSellWithMockPrice(String stockId, Long quantity, BigDecimal mockPrice) {
        User user = userRepository.findById(testUser.getUserId()).orElseThrow();
        Stock stock = stockRepository.findById(stockId).orElseThrow();
        
        BigDecimal totalPrice = mockPrice.multiply(BigDecimal.valueOf(quantity));
        user.addBalance(totalPrice);
        userRepository.save(user);

        Execution execution = Execution.builder()
                .user(user)
                .stock(stock)
                .sector(stock.getSector())
                .executedAt(java.time.LocalDateTime.now())
                .executionType(Execution.ExecutionType.SELL)
                .quantity(quantity)
                .price(mockPrice)
                .totalPrice(totalPrice)
                .build();
        Execution savedExecution = executionRepository.save(execution);

        // UserStock 업데이트
        UserStock userStock = userStockRepository.findByUserUserIdAndStockStockId(user.getUserId(), stockId)
                .orElseThrow(() -> new RuntimeException("보유주식이 없습니다"));
        userStock.subtractQuantity(quantity);
        userStockRepository.save(userStock);

        return savedExecution;
    }
}
