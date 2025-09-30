package com.youthfi.finance.domain.stock.ui;

import com.youthfi.finance.domain.stock.application.usecase.TradingUseCase;
import com.youthfi.finance.domain.stock.domain.entity.Execution;
import com.youthfi.finance.global.common.BaseResponse;
import com.youthfi.finance.global.swagger.BaseApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/user/trading")
@RequiredArgsConstructor
@Tag(name = "Trading", description = "주식 매수/매도 및 거래 내역 API")
public class TradingController implements BaseApi {

    private final TradingUseCase tradingUseCase;

    @Operation(summary = "주식 매수", description = "사용자가 특정 종목을 지정 수량과 가격으로 매수합니다.")
    @PostMapping("/buy")
    public BaseResponse<Execution> buy(
            @RequestParam Long userId,
            @RequestParam String stockId,
            @RequestParam Integer quantity,
            @RequestParam BigDecimal price
    ) {
        Execution execution = tradingUseCase.buyStock(userId, stockId, quantity, price);
        return BaseResponse.onSuccess(execution);
    }

    @Operation(summary = "주식 매도", description = "사용자가 보유한 특정 종목을 지정 수량과 가격으로 매도합니다.")
    @PostMapping("/sell")
    public BaseResponse<Execution> sell(
            @RequestParam Long userId,
            @RequestParam String stockId,
            @RequestParam Integer quantity,
            @RequestParam BigDecimal price
    ) {
        Execution execution = tradingUseCase.sellStock(userId, stockId, quantity, price);
        return BaseResponse.onSuccess(execution);
    }

    @Operation(summary = "내 거래 내역 조회", description = "사용자의 전체 거래 내역을 최근순으로 반환합니다.")
    @GetMapping("/history")
    public BaseResponse<List<Execution>> history(@RequestParam Long userId) {
        List<Execution> list = tradingUseCase.getMyTradingHistory(userId);
        return BaseResponse.onSuccess(list);
    }

    @Operation(summary = "종목별 거래 내역 조회", description = "사용자의 특정 종목 거래 내역을 최근순으로 반환합니다.")
    @GetMapping("/history/by-stock")
    public BaseResponse<List<Execution>> historyByStock(@RequestParam Long userId, @RequestParam String stockId) {
        List<Execution> list = tradingUseCase.getMyTradingHistoryByStock(userId, stockId);
        return BaseResponse.onSuccess(list);
    }

    @Operation(summary = "매수 내역 조회", description = "사용자의 매수 거래 내역만 최근순으로 반환합니다.")
    @GetMapping("/history/buys")
    public BaseResponse<List<Execution>> buyHistory(@RequestParam Long userId) {
        List<Execution> list = tradingUseCase.getMyBuyHistory(userId);
        return BaseResponse.onSuccess(list);
    }

    @Operation(summary = "매도 내역 조회", description = "사용자의 매도 거래 내역만 최근순으로 반환합니다.")
    @GetMapping("/history/sells")
    public BaseResponse<List<Execution>> sellHistory(@RequestParam Long userId) {
        List<Execution> list = tradingUseCase.getMySellHistory(userId);
        return BaseResponse.onSuccess(list);
    }

    @Operation(summary = "특정 날짜 거래 내역 조회", description = "지정한 날짜의 거래 내역만 최근순으로 반환합니다.")
    @GetMapping("/history/by-date")
    public BaseResponse<List<Execution>> historyByDate(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<Execution> list = tradingUseCase.getMyTradingHistoryByDate(userId, date);
        return BaseResponse.onSuccess(list);
    }
}


