package com.youthfi.finance.domain.stock.ui;

import com.youthfi.finance.domain.stock.application.dto.request.ChartRequest;
import com.youthfi.finance.domain.stock.application.dto.response.ChartDataResponse;
import com.youthfi.finance.domain.stock.domain.service.ChartService;
import com.youthfi.finance.domain.stock.domain.service.ChartCacheService;
import com.youthfi.finance.domain.stock.infra.KisChartApiClient;
import com.youthfi.finance.global.common.BaseResponse;
import com.youthfi.finance.global.swagger.BaseApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 주식 차트 API 컨트롤러
 */
@RestController
@RequestMapping("/api/stock/chart")
@RequiredArgsConstructor
@Tag(name = "Stock Chart", description = "주식 차트 API")
public class ChartController implements BaseApi {

    private final ChartService chartService;
    private final KisChartApiClient kisChartApiClient;
    private final ChartCacheService chartCacheService;
    
    /**
     * 차트 데이터 조회 (통합)
     */
    @GetMapping("/{stockCode}")
    @Operation(summary = "차트 데이터 조회", description = "종목의 차트 데이터를 조회합니다.")
    public ResponseEntity<BaseResponse<ChartDataResponse>> getChart(
            @PathVariable String stockCode,
            String period,
            String range) {

        ChartRequest request = new ChartRequest(stockCode, period, range);
        ChartDataResponse chartData = chartService.getChartData(request);
        
        return ResponseEntity.ok(BaseResponse.onSuccess(chartData));
    }
    
    /**
     * 일봉 차트 조회 (60일)
     */
    @GetMapping("/{stockCode}/daily")
    @Operation(summary = "일봉 차트 조회", description = "60일간의 일봉 차트 데이터를 조회합니다.")
    public ResponseEntity<BaseResponse<ChartDataResponse>> getDailyChart(
            @PathVariable String stockCode) {

        ChartDataResponse chartData = chartService.getDailyChart(stockCode);
        
        return ResponseEntity.ok(BaseResponse.onSuccess(chartData));
    }
    
    /**
     * 월봉 차트 조회 (5년)
     */
    @GetMapping("/{stockCode}/monthly")
    @Operation(summary = "월봉 차트 조회", description = "5년간의 월봉 차트 데이터를 조회합니다.")
    public ResponseEntity<BaseResponse<ChartDataResponse>> getMonthlyChart(
            @PathVariable String stockCode) {

        ChartDataResponse chartData = chartService.getMonthlyChart(stockCode);
        
        return ResponseEntity.ok(BaseResponse.onSuccess(chartData));
    }
    
    /**
     * 연봉 차트 조회 (10년)
     */
    @GetMapping("/{stockCode}/yearly")
    @Operation(summary = "연봉 차트 조회", description = "10년간의 연봉 차트 데이터를 조회합니다.")
    public ResponseEntity<BaseResponse<ChartDataResponse>> getYearlyChart(
            @PathVariable String stockCode) {

        ChartDataResponse chartData = chartService.getYearlyChart(stockCode);
        
        return ResponseEntity.ok(BaseResponse.onSuccess(chartData));
    }
    
    /**
     * 분봉 차트 조회 (당일) - 실시간 전용
     */
    @GetMapping("/{stockCode}/minute")
    @Operation(summary = "분봉 차트 조회", description = "당일의 분봉 차트 데이터를 실시간으로 조회합니다.")
    public ResponseEntity<BaseResponse<ChartDataResponse>> getMinuteChart(
            @PathVariable String stockCode) {
        
        ChartDataResponse chartData = kisChartApiClient.getMinuteChart(stockCode);
        
        return ResponseEntity.ok(BaseResponse.onSuccess(chartData));
    }
    
    /**
     * 차트 캐시 삭제
     */
    @DeleteMapping("/{stockCode}/cache")
    @Operation(summary = "차트 캐시 삭제", description = "특정 종목의 차트 캐시를 삭제합니다.")
    public ResponseEntity<BaseResponse<String>> clearChartCache(
            @PathVariable String stockCode,
            @RequestParam(required = false) String period) {
        
        if (period != null) {
            chartCacheService.deleteChart(stockCode, period, "default");
        } else {
            // 모든 기간 캐시 삭제
            chartCacheService.deleteChart(stockCode, "1d", "60d");
            chartCacheService.deleteChart(stockCode, "1m", "5y");
            chartCacheService.deleteChart(stockCode, "1y", "10y");
        }
        
        return ResponseEntity.ok(BaseResponse.onSuccess("캐시 삭제 완료"));
    }
}
