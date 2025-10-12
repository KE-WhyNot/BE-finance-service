package com.youthfi.finance.domain.stock.domain.entity;

import com.youthfi.finance.global.common.BaseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Stock 엔티티 단위 테스트")
class StockTest {

    @Test
    @DisplayName("Stock 생성 성공")
    void createStock_Success() {
        // given
        Sector sector = Sector.builder()
                .sectorName("전기·전자")
                .build();
        
        String stockId = "005930";
        String stockName = "삼성전자";
        String stockImage = "https://example.com/samsung.png";

        // when
        Stock stock = Stock.builder()
                .stockId(stockId)
                .sector(sector)
                .stockName(stockName)
                .stockImage(stockImage)
                .build();

        // then
        assertThat(stock.getStockId()).isEqualTo(stockId);
        assertThat(stock.getSector()).isEqualTo(sector);
        assertThat(stock.getStockName()).isEqualTo(stockName);
        assertThat(stock.getStockImage()).isEqualTo(stockImage);
        assertThat(stock.getPortfolioStocks()).isEmpty();
        assertThat(stock.getUserStocks()).isEmpty();
        assertThat(stock.getExecutions()).isEmpty();
        assertThat(stock.getInterestStocks()).isEmpty();
    }

    @Test
    @DisplayName("Stock 생성 - 필수 필드만")
    void createStock_RequiredFieldsOnly() {
        // given
        Sector sector = Sector.builder()
                .sectorName("화학")
                .build();
        
        String stockId = "000660";
        String stockName = "SK하이닉스";

        // when
        Stock stock = Stock.builder()
                .stockId(stockId)
                .sector(sector)
                .stockName(stockName)
                .build();

        // then
        assertThat(stock.getStockId()).isEqualTo(stockId);
        assertThat(stock.getSector()).isEqualTo(sector);
        assertThat(stock.getStockName()).isEqualTo(stockName);
        assertThat(stock.getStockImage()).isNull();
    }

    @Test
    @DisplayName("Stock 생성 - null 값 처리")
    void createStock_NullValues() {
        // given
        Sector sector = Sector.builder()
                .sectorName("IT 서비스")
                .build();
        
        String stockId = "035420";
        String stockName = "NAVER";

        // when
        Stock stock = Stock.builder()
                .stockId(stockId)
                .sector(sector)
                .stockName(stockName)
                .stockImage(null)
                .build();

        // then
        assertThat(stock.getStockId()).isEqualTo(stockId);
        assertThat(stock.getSector()).isEqualTo(sector);
        assertThat(stock.getStockName()).isEqualTo(stockName);
        assertThat(stock.getStockImage()).isNull();
    }

    @Test
    @DisplayName("Stock 생성 - 빈 문자열 처리")
    void createStock_EmptyString() {
        // given
        Sector sector = Sector.builder()
                .sectorName("기계·장비")
                .build();
        
        String stockId = "009310";
        String stockName = "참엔지니어링";
        String stockImage = "";

        // when
        Stock stock = Stock.builder()
                .stockId(stockId)
                .sector(sector)
                .stockName(stockName)
                .stockImage(stockImage)
                .build();

        // then
        assertThat(stock.getStockId()).isEqualTo(stockId);
        assertThat(stock.getSector()).isEqualTo(sector);
        assertThat(stock.getStockName()).isEqualTo(stockName);
        assertThat(stock.getStockImage()).isEqualTo("");
    }

    @Test
    @DisplayName("Stock 생성 - 긴 종목명")
    void createStock_LongStockName() {
        // given
        Sector sector = Sector.builder()
                .sectorName("제약")
                .build();
        
        String stockId = "207940";
        String stockName = "삼성바이오로직스";
        String stockImage = "https://example.com/samsung-bio.png";

        // when
        Stock stock = Stock.builder()
                .stockId(stockId)
                .sector(sector)
                .stockName(stockName)
                .stockImage(stockImage)
                .build();

        // then
        assertThat(stock.getStockId()).isEqualTo(stockId);
        assertThat(stock.getSector()).isEqualTo(sector);
        assertThat(stock.getStockName()).isEqualTo(stockName);
        assertThat(stock.getStockImage()).isEqualTo(stockImage);
    }

    @Test
    @DisplayName("Stock 생성 - 긴 이미지 URL")
    void createStock_LongImageUrl() {
        // given
        Sector sector = Sector.builder()
                .sectorName("운송장비·부품")
                .build();
        
        String stockId = "005380";
        String stockName = "현대차";
        String stockImage = "https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-005380.png%3F20231215?width=96&height=96";

        // when
        Stock stock = Stock.builder()
                .stockId(stockId)
                .sector(sector)
                .stockName(stockName)
                .stockImage(stockImage)
                .build();

        // then
        assertThat(stock.getStockId()).isEqualTo(stockId);
        assertThat(stock.getSector()).isEqualTo(sector);
        assertThat(stock.getStockName()).isEqualTo(stockName);
        assertThat(stock.getStockImage()).isEqualTo(stockImage);
    }

    @Test
    @DisplayName("Stock 생성 - 특수문자 포함 종목명")
    void createStock_SpecialCharactersInName() {
        // given
        Sector sector = Sector.builder()
                .sectorName("금속")
                .build();
        
        String stockId = "005490";
        String stockName = "POSCO 홀딩스";
        String stockImage = "https://example.com/posco.png";

        // when
        Stock stock = Stock.builder()
                .stockId(stockId)
                .sector(sector)
                .stockName(stockName)
                .stockImage(stockImage)
                .build();

        // then
        assertThat(stock.getStockId()).isEqualTo(stockId);
        assertThat(stock.getSector()).isEqualTo(sector);
        assertThat(stock.getStockName()).isEqualTo(stockName);
        assertThat(stock.getStockImage()).isEqualTo(stockImage);
    }

    @Test
    @DisplayName("Stock 생성 - 숫자로만 구성된 종목코드")
    void createStock_NumericStockId() {
        // given
        Sector sector = Sector.builder()
                .sectorName("건설")
                .build();
        
        String stockId = "000725";
        String stockName = "현대건설";
        String stockImage = "https://example.com/hyundai-construction.png";

        // when
        Stock stock = Stock.builder()
                .stockId(stockId)
                .sector(sector)
                .stockName(stockName)
                .stockImage(stockImage)
                .build();

        // then
        assertThat(stock.getStockId()).isEqualTo(stockId);
        assertThat(stock.getSector()).isEqualTo(sector);
        assertThat(stock.getStockName()).isEqualTo(stockName);
        assertThat(stock.getStockImage()).isEqualTo(stockImage);
    }

    @Test
    @DisplayName("Stock 생성 - 영문자 포함 종목코드")
    void createStock_AlphanumericStockId() {
        // given
        Sector sector = Sector.builder()
                .sectorName("기타금융")
                .build();
        
        String stockId = "000155";
        String stockName = "두산우";
        String stockImage = "https://example.com/doosan.png";

        // when
        Stock stock = Stock.builder()
                .stockId(stockId)
                .sector(sector)
                .stockName(stockName)
                .stockImage(stockImage)
                .build();

        // then
        assertThat(stock.getStockId()).isEqualTo(stockId);
        assertThat(stock.getSector()).isEqualTo(sector);
        assertThat(stock.getStockName()).isEqualTo(stockName);
        assertThat(stock.getStockImage()).isEqualTo(stockImage);
    }

    @Test
    @DisplayName("Stock 생성 - 관계 엔티티 초기화 확인")
    void createStock_RelationshipInitialization() {
        // given
        Sector sector = Sector.builder()
                .sectorName("전기·전자")
                .build();
        
        String stockId = "005930";
        String stockName = "삼성전자";

        // when
        Stock stock = Stock.builder()
                .stockId(stockId)
                .sector(sector)
                .stockName(stockName)
                .build();

        // then
        assertThat(stock.getPortfolioStocks()).isNotNull().isEmpty();
        assertThat(stock.getUserStocks()).isNotNull().isEmpty();
        assertThat(stock.getExecutions()).isNotNull().isEmpty();
        assertThat(stock.getInterestStocks()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Stock 생성 - BaseEntity 상속 확인")
    void createStock_BaseEntityInheritance() {
        // given
        Sector sector = Sector.builder()
                .sectorName("IT 서비스")
                .build();
        
        String stockId = "035720";
        String stockName = "카카오";

        // when
        Stock stock = Stock.builder()
                .stockId(stockId)
                .sector(sector)
                .stockName(stockName)
                .build();

        // then
        assertThat(stock).isInstanceOf(BaseEntity.class);
        assertThat(stock.getCreatedAt()).isNull(); // JPA가 설정
        assertThat(stock.getUpdatedAt()).isNull(); // JPA가 설정
        assertThat(stock.getDeletedAt()).isNull(); // JPA가 설정
    }
}
