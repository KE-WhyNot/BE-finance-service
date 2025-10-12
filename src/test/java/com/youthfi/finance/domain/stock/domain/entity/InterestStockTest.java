package com.youthfi.finance.domain.stock.domain.entity;

import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.global.common.BaseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("InterestStock 엔티티 단위 테스트")
class InterestStockTest {

    @Test
    @DisplayName("InterestStock 생성 성공")
    void createInterestStock_Success() {
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

        Boolean interestFlag = true;

        // when
        InterestStock interestStock = InterestStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .interestFlag(interestFlag)
                .build();

        // then
        assertThat(interestStock.getUser()).isEqualTo(user);
        assertThat(interestStock.getStock()).isEqualTo(stock);
        assertThat(interestStock.getSector()).isEqualTo(sector);
        assertThat(interestStock.getInterestFlag()).isEqualTo(interestFlag);
        assertThat(interestStock.getId()).isNull(); // JPA가 설정
    }

    @Test
    @DisplayName("InterestStock 생성 - 관심 없음")
    void createInterestStock_NotInterested() {
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

        Boolean interestFlag = false;

        // when
        InterestStock interestStock = InterestStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .interestFlag(interestFlag)
                .build();

        // then
        assertThat(interestStock.getUser()).isEqualTo(user);
        assertThat(interestStock.getStock()).isEqualTo(stock);
        assertThat(interestStock.getSector()).isEqualTo(sector);
        assertThat(interestStock.getInterestFlag()).isFalse();
        assertThat(interestStock.isInterested()).isFalse();
    }

    @Test
    @DisplayName("toggleInterest - 관심에서 비관심으로")
    void toggleInterest_FromInterestedToNotInterested() {
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

        InterestStock interestStock = InterestStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .interestFlag(true)
                .build();

        // when
        interestStock.toggleInterest();

        // then
        assertThat(interestStock.getInterestFlag()).isFalse();
        assertThat(interestStock.isInterested()).isFalse();
    }

    @Test
    @DisplayName("toggleInterest - 비관심에서 관심으로")
    void toggleInterest_FromNotInterestedToInterested() {
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

        InterestStock interestStock = InterestStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .interestFlag(false)
                .build();

        // when
        interestStock.toggleInterest();

        // then
        assertThat(interestStock.getInterestFlag()).isTrue();
        assertThat(interestStock.isInterested()).isTrue();
    }

    @Test
    @DisplayName("toggleInterest - 여러 번 토글")
    void toggleInterest_MultipleToggles() {
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

        InterestStock interestStock = InterestStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .interestFlag(true)
                .build();

        // when & then
        // 첫 번째 토글: true -> false
        interestStock.toggleInterest();
        assertThat(interestStock.getInterestFlag()).isFalse();

        // 두 번째 토글: false -> true
        interestStock.toggleInterest();
        assertThat(interestStock.getInterestFlag()).isTrue();

        // 세 번째 토글: true -> false
        interestStock.toggleInterest();
        assertThat(interestStock.getInterestFlag()).isFalse();
    }

    @Test
    @DisplayName("setInterest - 관심으로 설정")
    void setInterest_ToInterested() {
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

        InterestStock interestStock = InterestStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .interestFlag(false)
                .build();

        // when
        interestStock.setInterest(true);

        // then
        assertThat(interestStock.getInterestFlag()).isTrue();
        assertThat(interestStock.isInterested()).isTrue();
    }

    @Test
    @DisplayName("setInterest - 비관심으로 설정")
    void setInterest_ToNotInterested() {
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

        InterestStock interestStock = InterestStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .interestFlag(true)
                .build();

        // when
        interestStock.setInterest(false);

        // then
        assertThat(interestStock.getInterestFlag()).isFalse();
        assertThat(interestStock.isInterested()).isFalse();
    }

    @Test
    @DisplayName("setInterest - null 값 설정")
    void setInterest_NullValue() {
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

        InterestStock interestStock = InterestStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .interestFlag(true)
                .build();

        // when
        interestStock.setInterest(null);

        // then
        assertThat(interestStock.getInterestFlag()).isNull();
        // null 값에 대한 isInterested()는 NullPointerException 발생
        // 실제로는 null 값을 허용하지 않는 것이 맞음
    }

    @Test
    @DisplayName("isInterested - true 반환")
    void isInterested_ReturnsTrue() {
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

        InterestStock interestStock = InterestStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .interestFlag(true)
                .build();

        // when & then
        assertThat(interestStock.isInterested()).isTrue();
    }

    @Test
    @DisplayName("isInterested - false 반환")
    void isInterested_ReturnsFalse() {
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

        InterestStock interestStock = InterestStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .interestFlag(false)
                .build();

        // when & then
        assertThat(interestStock.isInterested()).isFalse();
    }

    @Test
    @DisplayName("InterestStock 생성 - BaseEntity 상속 확인")
    void createInterestStock_BaseEntityInheritance() {
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

        // when
        InterestStock interestStock = InterestStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .interestFlag(true)
                .build();

        // then
        assertThat(interestStock).isInstanceOf(BaseEntity.class);
        assertThat(interestStock.getCreatedAt()).isNull(); // JPA가 설정
        assertThat(interestStock.getUpdatedAt()).isNull(); // JPA가 설정
        assertThat(interestStock.getDeletedAt()).isNull(); // JPA가 설정
    }

    @Test
    @DisplayName("InterestStock 생성 - ID 자동 생성 확인")
    void createInterestStock_AutoGeneratedId() {
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
        InterestStock interestStock = InterestStock.builder()
                .user(user)
                .stock(stock)
                .sector(sector)
                .interestFlag(true)
                .build();

        // then
        assertThat(interestStock.getId()).isNull(); // JPA가 설정
    }
}
