package com.youthfi.finance.domain.stock.application.usecase;

import com.youthfi.finance.domain.stock.application.dto.response.UserHoldingResponse;
import com.youthfi.finance.domain.stock.domain.entity.UserStock;
import com.youthfi.finance.domain.stock.domain.service.UserStockService;
import com.youthfi.finance.global.exception.StockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserStockUseCase {

    private final UserStockService userStockService;

    /**
     * 내 보유 주식 목록 조회 
     */
    public List<UserHoldingResponse> getMyHoldings(String userId) {
        log.info("보유 주식 목록 조회 요청 - 사용자: {}", userId);

        List<UserStock> userStocks = userStockService.getUserStocksByUserId(userId);
        List<UserHoldingResponse> responses = userStocks.stream()
                .map(us -> new UserHoldingResponse(
                        us.getUserStockId(),
                        us.getStock().getStockId(),
                        us.getStock().getStockName(),
                        us.getHoldingQuantity(),
                        us.getAvgPrice(),
                        us.getCreatedAt()
                ))
                .collect(Collectors.toList());

        log.info("보유 주식 목록 조회 완료 - 사용자: {}, 건수: {}", userId, responses.size());
        return responses;
    }

    /**
     * 특정 종목 보유 정보 조회     
     */
    public UserHoldingResponse getMyHoldingByStockId(String userId, String stockId) {
        log.info("특정 종목 보유 정보 조회 요청 - 사용자: {}, 종목: {}", userId, stockId);

        UserStock us = userStockService.getUserStockByUserIdAndStockId(userId, stockId)
                .orElseThrow(() -> StockException.userStockNotFound());

        UserHoldingResponse response = new UserHoldingResponse(
                us.getUserStockId(),
                us.getStock().getStockId(),
                us.getStock().getStockName(),
                us.getHoldingQuantity(),
                us.getAvgPrice(),
                us.getCreatedAt()
        );

        log.info("특정 종목 보유 정보 조회 완료 - 사용자: {}, 종목: {}", userId, stockId);
        return response;
    }
}
