package com.youthfi.finance.domain.stock.domain.entity;

import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.global.common.BaseEntity;
import com.youthfi.finance.global.exception.StockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("UserStock 엔티티 단위 테스트")
class UserStockTest {

    @Test
    @DisplayName("UserStock 생성 성공")
    void createUserStock_Success() {
        // given
        User user = User.builder()
                .userId("testuser123")
                .balance(new BigDecimal("10000000"))
                .build();

        Sector sector = Sector.builder()
                .sectorName("전기·전자")
                .build();

        Stock stock = Stock.builder()
                .stockId("005930")
                .sector(sector)
                .stockName("삼성전자")
                .build();

        Long holdingQuantity = 100L;
        BigDecimal avgPrice = new BigDecimal("70000");
        BigDecimal totalValue = new BigDecimal("7000000");

        // when
        UserStock userStock = UserStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .holdingQuantity(holdingQuantity)
                .avgPrice(avgPrice)
                .totalValue(totalValue)
                .build();

        // then
        assertThat(userStock.getUser()).isEqualTo(user);
        assertThat(userStock.getStock()).isEqualTo(stock);
        assertThat(userStock.getSector()).isEqualTo(sector);
        assertThat(userStock.getHoldingQuantity()).isEqualTo(holdingQuantity);
        assertThat(userStock.getAvgPrice()).isEqualTo(avgPrice);
        assertThat(userStock.getTotalValue()).isEqualTo(totalValue);
        assertThat(userStock.getUserStockId()).isNull(); // JPA가 설정
    }

    @Test
    @DisplayName("updateHoldingQuantity - 수량 업데이트")
    void updateHoldingQuantity_Success() {
        // given
        User user = User.builder()
                .userId("testuser456")
                .balance(new BigDecimal("5000000"))
                .build();

        Sector sector = Sector.builder()
                .sectorName("화학")
                .build();

        Stock stock = Stock.builder()
                .stockId("000660")
                .sector(sector)
                .stockName("SK하이닉스")
                .build();

        UserStock userStock = UserStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .holdingQuantity(50L)
                .avgPrice(new BigDecimal("80000"))
                .totalValue(new BigDecimal("4000000"))
                .build();

        Long newQuantity = 75L;

        // when
        userStock.updateHoldingQuantity(newQuantity);

        // then
        assertThat(userStock.getHoldingQuantity()).isEqualTo(newQuantity);
    }

    @Test
    @DisplayName("updateAvgPrice - 평균가 업데이트")
    void updateAvgPrice_Success() {
        // given
        User user = User.builder()
                .userId("testuser789")
                .balance(new BigDecimal("20000000"))
                .build();

        Sector sector = Sector.builder()
                .sectorName("IT 서비스")
                .build();

        Stock stock = Stock.builder()
                .stockId("035420")
                .sector(sector)
                .stockName("NAVER")
                .build();

        UserStock userStock = UserStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .holdingQuantity(30L)
                .avgPrice(new BigDecimal("200000"))
                .totalValue(new BigDecimal("6000000"))
                .build();

        BigDecimal newAvgPrice = new BigDecimal("250000");

        // when
        userStock.updateAvgPrice(newAvgPrice);

        // then
        assertThat(userStock.getAvgPrice()).isEqualTo(newAvgPrice);
    }

    @Test
    @DisplayName("updateTotalValue - 총 평가금액 업데이트")
    void updateTotalValue_Success() {
        // given
        User user = User.builder()
                .userId("testuser101")
                .balance(new BigDecimal("15000000"))
                .build();

        Sector sector = Sector.builder()
                .sectorName("기계·장비")
                .build();

        Stock stock = Stock.builder()
                .stockId("009310")
                .sector(sector)
                .stockName("참엔지니어링")
                .build();

        UserStock userStock = UserStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .holdingQuantity(20L)
                .avgPrice(new BigDecimal("50000"))
                .totalValue(new BigDecimal("1000000"))
                .build();

        BigDecimal newTotalValue = new BigDecimal("1200000");

        // when
        userStock.updateTotalValue(newTotalValue);

        // then
        assertThat(userStock.getTotalValue()).isEqualTo(newTotalValue);
    }

    @Test
    @DisplayName("addQuantity - 수량 추가 성공")
    void addQuantity_Success() {
        // given
        User user = User.builder()
                .userId("testuser202")
                .balance(new BigDecimal("8000000"))
                .build();

        Sector sector = Sector.builder()
                .sectorName("제약")
                .build();

        Stock stock = Stock.builder()
                .stockId("207940")
                .sector(sector)
                .stockName("삼성바이오로직스")
                .build();

        UserStock userStock = UserStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .holdingQuantity(10L)
                .avgPrice(new BigDecimal("100000"))
                .totalValue(new BigDecimal("1000000"))
                .build();

        Long addQuantity = 5L;
        BigDecimal addPrice = new BigDecimal("120000");

        // when
        userStock.addQuantity(addQuantity, addPrice);

        // then
        assertThat(userStock.getHoldingQuantity()).isEqualTo(15L);
        // 평균가 계산: (10 * 100000 + 5 * 120000) / 15 = 106666.67
        assertThat(userStock.getAvgPrice()).isEqualByComparingTo(new BigDecimal("106666.67"));
        // 총 평가금액은 평균가 * 수량으로 계산됨
        assertThat(userStock.getTotalValue()).isEqualByComparingTo(userStock.getAvgPrice().multiply(BigDecimal.valueOf(userStock.getHoldingQuantity())));
    }

    @Test
    @DisplayName("addQuantity - 동일한 가격으로 추가")
    void addQuantity_SamePrice() {
        // given
        User user = User.builder()
                .userId("testuser303")
                .balance(new BigDecimal("12000000"))
                .build();

        Sector sector = Sector.builder()
                .sectorName("운송장비·부품")
                .build();

        Stock stock = Stock.builder()
                .stockId("005380")
                .sector(sector)
                .stockName("현대차")
                .build();

        UserStock userStock = UserStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .holdingQuantity(20L)
                .avgPrice(new BigDecimal("50000"))
                .totalValue(new BigDecimal("1000000"))
                .build();

        Long addQuantity = 10L;
        BigDecimal addPrice = new BigDecimal("50000"); // 동일한 가격

        // when
        userStock.addQuantity(addQuantity, addPrice);

        // then
        assertThat(userStock.getHoldingQuantity()).isEqualTo(30L);
        assertThat(userStock.getAvgPrice()).isEqualByComparingTo(new BigDecimal("50000"));
        assertThat(userStock.getTotalValue()).isEqualByComparingTo(new BigDecimal("1500000"));
    }

    @Test
    @DisplayName("subtractQuantity - 수량 차감 성공")
    void subtractQuantity_Success() {
        // given
        User user = User.builder()
                .userId("testuser404")
                .balance(new BigDecimal("9000000"))
                .build();

        Sector sector = Sector.builder()
                .sectorName("금속")
                .build();

        Stock stock = Stock.builder()
                .stockId("005490")
                .sector(sector)
                .stockName("POSCO 홀딩스")
                .build();

        UserStock userStock = UserStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .holdingQuantity(100L)
                .avgPrice(new BigDecimal("30000"))
                .totalValue(new BigDecimal("3000000"))
                .build();

        Long subtractQuantity = 20L;

        // when
        userStock.subtractQuantity(subtractQuantity);

        // then
        assertThat(userStock.getHoldingQuantity()).isEqualTo(80L);
        assertThat(userStock.getAvgPrice()).isEqualByComparingTo(new BigDecimal("30000"));
        assertThat(userStock.getTotalValue()).isEqualByComparingTo(new BigDecimal("2400000"));
    }

    @Test
    @DisplayName("subtractQuantity - 보유 수량 부족으로 실패")
    void subtractQuantity_Fail_InsufficientQuantity() {
        // given
        User user = User.builder()
                .userId("testuser505")
                .balance(new BigDecimal("7000000"))
                .build();

        Sector sector = Sector.builder()
                .sectorName("건설")
                .build();

        Stock stock = Stock.builder()
                .stockId("000725")
                .sector(sector)
                .stockName("현대건설")
                .build();

        UserStock userStock = UserStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .holdingQuantity(50L)
                .avgPrice(new BigDecimal("40000"))
                .totalValue(new BigDecimal("2000000"))
                .build();

        Long subtractQuantity = 60L; // 보유 수량보다 많음

        // when & then
        assertThatThrownBy(() -> userStock.subtractQuantity(subtractQuantity))
                .isInstanceOf(StockException.class)
                .hasMessageContaining("보유 수량이 부족합니다");
    }

    @Test
    @DisplayName("subtractQuantity - 음수 수량으로 실패")
    void subtractQuantity_Fail_NegativeQuantity() {
        // given
        User user = User.builder()
                .userId("testuser606")
                .balance(new BigDecimal("11000000"))
                .build();

        Sector sector = Sector.builder()
                .sectorName("기타금융")
                .build();

        Stock stock = Stock.builder()
                .stockId("105560")
                .sector(sector)
                .stockName("KB금융")
                .build();

        UserStock userStock = UserStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .holdingQuantity(30L)
                .avgPrice(new BigDecimal("60000"))
                .totalValue(new BigDecimal("1800000"))
                .build();

        Long subtractQuantity = -5L; // 음수

        // when & then
        assertThatThrownBy(() -> userStock.subtractQuantity(subtractQuantity))
                .isInstanceOf(StockException.class);
    }

    @Test
    @DisplayName("subtractQuantity - 0 수량으로 실패")
    void subtractQuantity_Fail_ZeroQuantity() {
        // given
        User user = User.builder()
                .userId("testuser707")
                .balance(new BigDecimal("6000000"))
                .build();

        Sector sector = Sector.builder()
                .sectorName("IT 서비스")
                .build();

        Stock stock = Stock.builder()
                .stockId("035720")
                .sector(sector)
                .stockName("카카오")
                .build();

        UserStock userStock = UserStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .holdingQuantity(25L)
                .avgPrice(new BigDecimal("80000"))
                .totalValue(new BigDecimal("2000000"))
                .build();

        Long subtractQuantity = 0L; // 0

        // when & then
        assertThatThrownBy(() -> userStock.subtractQuantity(subtractQuantity))
                .isInstanceOf(StockException.class);
    }

    @Test
    @DisplayName("addQuantity - 복잡한 평균가 계산")
    void addQuantity_ComplexAverageCalculation() {
        // given
        User user = User.builder()
                .userId("testuser808")
                .balance(new BigDecimal("13000000"))
                .build();

        Sector sector = Sector.builder()
                .sectorName("전기·전자")
                .build();

        Stock stock = Stock.builder()
                .stockId("005930")
                .sector(sector)
                .stockName("삼성전자")
                .build();

        UserStock userStock = UserStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .holdingQuantity(3L)
                .avgPrice(new BigDecimal("70000"))
                .totalValue(new BigDecimal("210000"))
                .build();

        // 첫 번째 추가
        userStock.addQuantity(2L, new BigDecimal("80000"));
        // 두 번째 추가
        userStock.addQuantity(1L, new BigDecimal("90000"));

        // when & then
        assertThat(userStock.getHoldingQuantity()).isEqualTo(6L);
        // 평균가: (3*70000 + 2*80000 + 1*90000) / 6 = 76666.67
        assertThat(userStock.getAvgPrice()).isEqualByComparingTo(new BigDecimal("76666.67"));
        // 총 평가금액은 평균가 * 수량으로 계산됨
        assertThat(userStock.getTotalValue()).isEqualByComparingTo(userStock.getAvgPrice().multiply(BigDecimal.valueOf(userStock.getHoldingQuantity())));
    }

    @Test
    @DisplayName("UserStock 생성 - BaseEntity 상속 확인")
    void createUserStock_BaseEntityInheritance() {
        // given
        User user = User.builder()
                .userId("testuser909")
                .balance(new BigDecimal("10000000"))
                .build();

        Sector sector = Sector.builder()
                .sectorName("화학")
                .build();

        Stock stock = Stock.builder()
                .stockId("000660")
                .sector(sector)
                .stockName("SK하이닉스")
                .build();

        // when
        UserStock userStock = UserStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .holdingQuantity(100L)
                .avgPrice(new BigDecimal("70000"))
                .totalValue(new BigDecimal("7000000"))
                .build();

        // then
        assertThat(userStock).isInstanceOf(BaseEntity.class);
        assertThat(userStock.getCreatedAt()).isNull(); // JPA가 설정
        assertThat(userStock.getUpdatedAt()).isNull(); // JPA가 설정
        assertThat(userStock.getDeletedAt()).isNull(); // JPA가 설정
    }
}
