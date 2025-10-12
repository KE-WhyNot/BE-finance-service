package com.youthfi.finance.domain.stock.domain.entity;

import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.global.common.BaseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Execution 엔티티 단위 테스트")
class ExecutionTest {

    @Test
    @DisplayName("Execution 생성 성공 - 매수")
    void createExecution_Success_Buy() {
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

        LocalDateTime executedAt = LocalDateTime.now();
        Execution.ExecutionType executionType = Execution.ExecutionType.BUY;
        Long quantity = 100L;
        BigDecimal price = new BigDecimal("70000");
        BigDecimal totalPrice = new BigDecimal("7000000");

        // when
        Execution execution = Execution.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .executedAt(executedAt)
                .executionType(executionType)
                .quantity(quantity)
                .price(price)
                .totalPrice(totalPrice)
                .build();

        // then
        assertThat(execution.getUser()).isEqualTo(user);
        assertThat(execution.getStock()).isEqualTo(stock);
        assertThat(execution.getSector()).isEqualTo(sector);
        assertThat(execution.getExecutedAt()).isEqualTo(executedAt);
        assertThat(execution.getExecutionType()).isEqualTo(executionType);
        assertThat(execution.getQuantity()).isEqualTo(quantity);
        assertThat(execution.getPrice()).isEqualTo(price);
        assertThat(execution.getTotalPrice()).isEqualTo(totalPrice);
        assertThat(execution.getExecutionId()).isNull(); // JPA가 설정
    }

    @Test
    @DisplayName("Execution 생성 성공 - 매도")
    void createExecution_Success_Sell() {
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

        LocalDateTime executedAt = LocalDateTime.of(2024, 1, 15, 14, 30, 0);
        Execution.ExecutionType executionType = Execution.ExecutionType.SELL;
        Long quantity = 50L;
        BigDecimal price = new BigDecimal("80000");
        BigDecimal totalPrice = new BigDecimal("4000000");

        // when
        Execution execution = Execution.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .executedAt(executedAt)
                .executionType(executionType)
                .quantity(quantity)
                .price(price)
                .totalPrice(totalPrice)
                .build();

        // then
        assertThat(execution.getUser()).isEqualTo(user);
        assertThat(execution.getStock()).isEqualTo(stock);
        assertThat(execution.getSector()).isEqualTo(sector);
        assertThat(execution.getExecutedAt()).isEqualTo(executedAt);
        assertThat(execution.getExecutionType()).isEqualTo(executionType);
        assertThat(execution.getQuantity()).isEqualTo(quantity);
        assertThat(execution.getPrice()).isEqualTo(price);
        assertThat(execution.getTotalPrice()).isEqualTo(totalPrice);
    }

    @Test
    @DisplayName("Execution 생성 - 다양한 거래 수량")
    void createExecution_VariousQuantities() {
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

        LocalDateTime executedAt = LocalDateTime.now();

        // when & then
        Long[] quantities = {1L, 10L, 100L, 1000L, 10000L};
        
        for (Long quantity : quantities) {
            Execution execution = Execution.builder()
                    .user(user)
                    .stock(stock)
                    .sector(sector)
                    .executedAt(executedAt)
                    .executionType(Execution.ExecutionType.BUY)
                    .quantity(quantity)
                    .price(new BigDecimal("200000"))
                    .totalPrice(new BigDecimal("200000").multiply(BigDecimal.valueOf(quantity)))
                    .build();

            assertThat(execution.getQuantity()).isEqualTo(quantity);
        }
    }

    @Test
    @DisplayName("Execution 생성 - 다양한 가격")
    void createExecution_VariousPrices() {
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

        LocalDateTime executedAt = LocalDateTime.now();

        // when & then
        BigDecimal[] prices = {
                new BigDecimal("1000"),
                new BigDecimal("10000"),
                new BigDecimal("100000"),
                new BigDecimal("1000000"),
                new BigDecimal("10000000")
        };
        
        for (BigDecimal price : prices) {
            Execution execution = Execution.builder()
                    .user(user)
                    .stock(stock)
                    .sector(sector)
                    .executedAt(executedAt)
                    .executionType(Execution.ExecutionType.BUY)
                    .quantity(10L)
                    .price(price)
                    .totalPrice(price.multiply(BigDecimal.valueOf(10)))
                    .build();

            assertThat(execution.getPrice()).isEqualTo(price);
        }
    }

    @Test
    @DisplayName("Execution 생성 - 다양한 거래일시")
    void createExecution_VariousExecutionTimes() {
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

        // when & then
        LocalDateTime[] executionTimes = {
                LocalDateTime.of(2024, 1, 1, 9, 0, 0),   // 장 시작
                LocalDateTime.of(2024, 1, 1, 12, 0, 0),  // 장중
                LocalDateTime.of(2024, 1, 1, 15, 30, 0), // 장 마감
                LocalDateTime.of(2024, 12, 31, 23, 59, 59) // 연말
        };
        
        for (LocalDateTime executedAt : executionTimes) {
            Execution execution = Execution.builder()
                    .user(user)
                    .stock(stock)
                    .sector(sector)
                    .executedAt(executedAt)
                    .executionType(Execution.ExecutionType.BUY)
                    .quantity(10L)
                    .price(new BigDecimal("100000"))
                    .totalPrice(new BigDecimal("1000000"))
                    .build();

            assertThat(execution.getExecutedAt()).isEqualTo(executedAt);
        }
    }

    @Test
    @DisplayName("ExecutionType 열거형 테스트")
    void executionType_EnumTest() {
        // when & then
        assertThat(Execution.ExecutionType.BUY).isNotNull();
        assertThat(Execution.ExecutionType.SELL).isNotNull();
        assertThat(Execution.ExecutionType.values()).hasSize(2);
        assertThat(Execution.ExecutionType.valueOf("BUY")).isEqualTo(Execution.ExecutionType.BUY);
        assertThat(Execution.ExecutionType.valueOf("SELL")).isEqualTo(Execution.ExecutionType.SELL);
    }

    @Test
    @DisplayName("Execution 생성 - 총 거래금액 계산")
    void createExecution_TotalPriceCalculation() {
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

        LocalDateTime executedAt = LocalDateTime.now();
        Long quantity = 25L;
        BigDecimal price = new BigDecimal("50000");
        BigDecimal expectedTotalPrice = price.multiply(BigDecimal.valueOf(quantity));

        // when
        Execution execution = Execution.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .executedAt(executedAt)
                .executionType(Execution.ExecutionType.BUY)
                .quantity(quantity)
                .price(price)
                .totalPrice(expectedTotalPrice)
                .build();

        // then
        assertThat(execution.getTotalPrice()).isEqualTo(expectedTotalPrice);
        assertThat(execution.getTotalPrice()).isEqualByComparingTo(new BigDecimal("1250000"));
    }

    @Test
    @DisplayName("Execution 생성 - 소수점 가격")
    void createExecution_DecimalPrice() {
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

        LocalDateTime executedAt = LocalDateTime.now();
        Long quantity = 100L;
        BigDecimal price = new BigDecimal("12345.67");
        BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(quantity));

        // when
        Execution execution = Execution.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .executedAt(executedAt)
                .executionType(Execution.ExecutionType.BUY)
                .quantity(quantity)
                .price(price)
                .totalPrice(totalPrice)
                .build();

        // then
        assertThat(execution.getPrice()).isEqualByComparingTo(price);
        assertThat(execution.getTotalPrice()).isEqualByComparingTo(totalPrice);
    }

    @Test
    @DisplayName("Execution 생성 - BaseEntity 상속 확인")
    void createExecution_BaseEntityInheritance() {
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

        // when
        Execution execution = Execution.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .executedAt(LocalDateTime.now())
                .executionType(Execution.ExecutionType.BUY)
                .quantity(50L)
                .price(new BigDecimal("40000"))
                .totalPrice(new BigDecimal("2000000"))
                .build();

        // then
        assertThat(execution).isInstanceOf(BaseEntity.class);
        assertThat(execution.getCreatedAt()).isNull(); // JPA가 설정
        assertThat(execution.getUpdatedAt()).isNull(); // JPA가 설정
        assertThat(execution.getDeletedAt()).isNull(); // JPA가 설정
    }

    @Test
    @DisplayName("Execution 생성 - ID 자동 생성 확인")
    void createExecution_AutoGeneratedId() {
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

        // when
        Execution execution = Execution.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .executedAt(LocalDateTime.now())
                .executionType(Execution.ExecutionType.SELL)
                .quantity(30L)
                .price(new BigDecimal("60000"))
                .totalPrice(new BigDecimal("1800000"))
                .build();

        // then
        assertThat(execution.getExecutionId()).isNull(); // JPA가 설정
    }
}
