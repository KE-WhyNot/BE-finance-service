package com.youthfi.finance.domain.stock.application.dto.response;

import com.youthfi.finance.domain.stock.domain.entity.Stock;
import lombok.Builder;
import lombok.Getter;

/**
 * 종목 목록 응답 DTO
 */
@Getter
@Builder
public class StockListResponse {
    
    private String stockId;        // 종목코드
    private String stockName;      // 종목명
    private String sectorName;     // 섹터명
    private String stockImage;     // 종목 이미지
    
    public static StockListResponse from(Stock stock) {
        return StockListResponse.builder()
                .stockId(stock.getStockId())
                .stockName(stock.getStockName())
                .sectorName(stock.getSector().getSectorName())
                .stockImage(stock.getStockImage())
                .build();
    }
}
