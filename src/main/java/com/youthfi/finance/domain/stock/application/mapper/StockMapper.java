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
        
        return new ExecutionResponse(
                execution.getExecutionId(),
                execution.getUser().getUserId(),
                execution.getStock().getStockId(),
                execution.getStock().getStockName(),
                execution.getExecutionType().name(),
                execution.getQuantity().intValue(),
                execution.getPrice(),
                execution.getTotalPrice(),
                execution.getExecutedAt()
        );
    }

    /**
     * InterestStock 엔터티를 InterestStockResponse DTO로 변환
     */
    public InterestStockResponse toInterestStockResponse(InterestStock interestStock) {
        if (interestStock == null) {
            return null;
        }
        
        return new InterestStockResponse(
                interestStock.getId(),
                interestStock.getUser().getUserId(),
                interestStock.getStock().getStockId(),
                interestStock.getStock().getStockName(),
                interestStock.getStock().getSector().getSectorName(),
                interestStock.getCreatedAt()
        );
    }

}
