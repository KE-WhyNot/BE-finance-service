package com.youthfi.finance.domain.stock.application.mapper;

import com.youthfi.finance.domain.stock.application.dto.response.ExecutionResponse;
import com.youthfi.finance.domain.stock.application.dto.response.InterestStockResponse;
import com.youthfi.finance.domain.stock.domain.entity.Execution;
import com.youthfi.finance.domain.stock.domain.entity.InterestStock;
import com.youthfi.finance.domain.stock.domain.entity.UserStock;
import org.springframework.stereotype.Component;

 

@Component
public class StockMapper {

    /**
     * Execution 엔터티를 ExecutionResponse DTO로 변환
     */
    public ExecutionResponse toExecutionResponse(Execution execution) {
        if (execution == null) {
            return null;
        }
        
        return ExecutionResponse.builder()
                .executionId(execution.getExecutionId())
                .userId(execution.getUser().getUserId())
                .stockId(execution.getStock().getStockId())
                .stockName(execution.getStock().getStockName())
                .executionType(execution.getExecutionType().name())
                .quantity(execution.getQuantity().intValue())
                .price(execution.getPrice())
                .totalAmount(execution.getTotalPrice())
                .executedAt(execution.getExecutedAt())
                .build();
    }

    /**
     * InterestStock 엔터티를 InterestStockResponse DTO로 변환
     */
    public InterestStockResponse toInterestStockResponse(InterestStock interestStock) {
        if (interestStock == null) {
            return null;
        }
        
        return InterestStockResponse.builder()
                .interestStockId(interestStock.getId())
                .userId(interestStock.getUser().getUserId())
                .stockId(interestStock.getStock().getStockId())
                .stockName(interestStock.getStock().getStockName())
                .sectorName(interestStock.getStock().getSector().getSectorName())
                .createdAt(interestStock.getCreatedAt())
                .build();
    }

}
