package com.youthfi.finance.domain.stock.ui;

import com.youthfi.finance.domain.stock.application.dto.request.DividendScheduleRequest;
import com.youthfi.finance.domain.stock.application.dto.request.StockCurrentPriceRequest;
import com.youthfi.finance.domain.stock.application.usecase.StockApiUseCase;
import com.youthfi.finance.global.common.BaseResponse;
import com.youthfi.finance.global.swagger.BaseApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
@Tag(name = "Stock", description = "주식 API")
public class StockApiController implements BaseApi {
    
    private final StockApiUseCase stockApiUseCase;
    
    @Operation(summary = "주식현재가 시세 조회", description = "KIS API를 통해 주식현재가 시세를 조회합니다.")
    @PostMapping("/current-price")
    public BaseResponse<Map<String, Object>> getStockCurrentPrice(@Valid @RequestBody StockCurrentPriceRequest request) {
        Map<String, Object> result = stockApiUseCase.getStockCurrentPrice(request);
        return BaseResponse.onSuccess(result);
    }
    
    @Operation(summary = "배당일정 조회", description = "예탁원정보를 통해 배당일정을 조회합니다.")
    @PostMapping("/dividend-schedule")
    public BaseResponse<Map<String, Object>> getDividendSchedule(@Valid @RequestBody DividendScheduleRequest request) {
        Map<String, Object> result = stockApiUseCase.getDividendSchedule(request);
        return BaseResponse.onSuccess(result);
    }
    
    @Operation(summary = "KIS API 토큰 상태 조회", description = "KIS API 토큰의 상태를 조회합니다.")
    @GetMapping("/token-status")
    public BaseResponse<Map<String, Map<String, Object>>> getTokenStatus() {
        Map<String, Map<String, Object>> result = stockApiUseCase.getTokenStatus();
        return BaseResponse.onSuccess(result);
    }
}
