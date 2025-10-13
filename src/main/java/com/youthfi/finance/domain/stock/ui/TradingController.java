package com.youthfi.finance.domain.stock.ui;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.youthfi.finance.domain.stock.application.dto.request.CurrentPriceTradingRequest;
import com.youthfi.finance.domain.stock.application.dto.response.ExecutionResponse;
import com.youthfi.finance.domain.stock.application.usecase.TradingUseCase;
import com.youthfi.finance.global.common.BaseResponse;
import com.youthfi.finance.global.security.SecurityUtils;
import com.youthfi.finance.global.swagger.BaseApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user/trading")
@RequiredArgsConstructor
@Tag(name = "Trading", description = "주식 매수/매도 및 거래 내역 API")
public class TradingController implements BaseApi {

    private final TradingUseCase tradingUseCase;

    @Operation(summary = "주식 매수", description = "실시간 현재가로 주식을 자동 매수합니다.")
    @PostMapping("/buy")
    public BaseResponse<ExecutionResponse> buy(@Valid @RequestBody CurrentPriceTradingRequest request) {
        String userId = SecurityUtils.getCurrentUserId();
        ExecutionResponse execution = tradingUseCase.buyStockAtCurrentPrice(userId, request);
        return BaseResponse.onSuccess(execution);
    }

    @Operation(summary = "주식 매도", description = "실시간 현재가로 주식을 자동 매도합니다.")
    @PostMapping("/sell")
    public BaseResponse<ExecutionResponse> sell(@Valid @RequestBody CurrentPriceTradingRequest request) {
        String userId = SecurityUtils.getCurrentUserId();
        ExecutionResponse execution = tradingUseCase.sellStockAtCurrentPrice(userId, request);
        return BaseResponse.onSuccess(execution);
    }

    @Operation(summary = "내 거래 내역 조회", description = "사용자의 전체 거래 내역을 최근순으로 반환합니다.")
    @GetMapping("/history")
    public BaseResponse<List<ExecutionResponse>> history() {

        String userId = SecurityUtils.getCurrentUserId();
        List<ExecutionResponse> list = tradingUseCase.getMyTradingHistory(userId);
        return BaseResponse.onSuccess(list);
    }

    @Operation(summary = "종목별 거래 내역 조회", description = "사용자의 특정 종목 거래 내역을 최근순으로 반환합니다.")
    @GetMapping("/history/by-stock")
    public BaseResponse<List<ExecutionResponse>> historyByStock(@RequestParam String stockId) {
     
        String userId = SecurityUtils.getCurrentUserId();
        List<ExecutionResponse> list = tradingUseCase.getMyTradingHistoryByStock(userId, stockId);
        return BaseResponse.onSuccess(list);
    }

    @Operation(summary = "매수 내역 조회", description = "사용자의 매수 거래 내역만 최근순으로 반환합니다.")
    @GetMapping("/history/buys")
    public BaseResponse<List<ExecutionResponse>> buyHistory() {
    
        String userId = SecurityUtils.getCurrentUserId();
        List<ExecutionResponse> list = tradingUseCase.getMyBuyHistory(userId);
        return BaseResponse.onSuccess(list);
    }

    @Operation(summary = "매도 내역 조회", description = "사용자의 매도 거래 내역만 최근순으로 반환합니다.")
    @GetMapping("/history/sells")
    public BaseResponse<List<ExecutionResponse>> sellHistory() {
    
        String userId = SecurityUtils.getCurrentUserId();
        List<ExecutionResponse> list = tradingUseCase.getMySellHistory(userId);
        return BaseResponse.onSuccess(list);
    }
}


