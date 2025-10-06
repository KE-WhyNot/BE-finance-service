package com.youthfi.finance.domain.stock.application.usecase;

import com.youthfi.finance.domain.stock.application.dto.response.UserHoldingResponse;
import com.youthfi.finance.domain.stock.domain.entity.UserStock;
import com.youthfi.finance.domain.stock.domain.service.UserStockService;
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
                .map(us -> UserHoldingResponse.builder()
                        .userStockId(us.getUserStockId())
                        .stockId(us.getStock().getStockId())
                        .stockName(us.getStock().getStockName())
                        .holdingQuantity(us.getHoldingQuantity())
                        .avgPrice(us.getAvgPrice())
                        .createdAt(us.getCreatedAt())
                        .build())
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
                .orElseThrow(() -> new RuntimeException("보유하지 않은 종목입니다: " + stockId));

        UserHoldingResponse response = UserHoldingResponse.builder()
                .userStockId(us.getUserStockId())
                .stockId(us.getStock().getStockId())
                .stockName(us.getStock().getStockName())
                .holdingQuantity(us.getHoldingQuantity())
                .avgPrice(us.getAvgPrice())
                .createdAt(us.getCreatedAt())
                .build();

        log.info("특정 종목 보유 정보 조회 완료 - 사용자: {}, 종목: {}", userId, stockId);
        return response;
    }
}
