package com.youthfi.finance.domain.stock.application.dto.response;

import com.youthfi.finance.domain.stock.domain.entity.Stock;

/**
 * 종목 목록 응답 DTO
 */
public record StockListResponse(
    String stockId,        // 종목코드
    String stockName,      // 종목명
    String sectorName,     // 섹터명
    String stockImage      // 종목 이미지
) {
    
    public static StockListResponse from(Stock stock) {
        return new StockListResponse(
            stock.getStockId(),
            stock.getStockName(),
            stock.getSector().getSectorName(),
            stock.getStockImage()
        );
    }
}
