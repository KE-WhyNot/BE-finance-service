package com.youthfi.finance.domain.stock.ui;

import com.youthfi.finance.domain.stock.application.dto.request.InterestStockRequest;
import com.youthfi.finance.domain.stock.application.dto.response.InterestStockResponse;
import com.youthfi.finance.domain.stock.application.usecase.InterestStockUseCase;
import com.youthfi.finance.global.common.BaseResponse;
import com.youthfi.finance.global.security.SecurityUtils;
import com.youthfi.finance.global.swagger.BaseApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/interest-stocks")
@RequiredArgsConstructor
@Tag(name = "InterestStock", description = "관심종목 관리 API")
public class InterestStockController implements BaseApi {

    private final InterestStockUseCase interestStockUseCase;

    @Operation(summary = "관심종목 추가", description = "사용자의 관심종목 목록에 종목을 추가합니다.")
    @PostMapping("")
    public BaseResponse<InterestStockResponse> add(@Valid @RequestBody InterestStockRequest request) {

        String userId = SecurityUtils.getCurrentUserId();
        InterestStockResponse result = interestStockUseCase.addInterestStock(userId, request);
        return BaseResponse.onSuccess(result);
    }

    @Operation(summary = "관심종목 제거", description = "사용자의 관심종목 목록에서 종목을 제거합니다.")
    @DeleteMapping("")
    public BaseResponse<Void> remove(@Valid @RequestBody InterestStockRequest request) {

        String userId = SecurityUtils.getCurrentUserId();
        interestStockUseCase.removeInterestStock(userId, request);
        return BaseResponse.onSuccess();
    }

    @Operation(summary = "관심종목 토글", description = "해당 종목의 관심 여부를 토글합니다.")
    @PostMapping("/toggle")
    public BaseResponse<InterestStockResponse> toggle(@Valid @RequestBody InterestStockRequest request) {
  
        String userId = SecurityUtils.getCurrentUserId();
        InterestStockResponse result = interestStockUseCase.toggleInterestStock(userId, request);
        return BaseResponse.onSuccess(result);
    }

    @Operation(summary = "관심종목 목록 조회", description = "사용자의 관심종목 목록을 조회합니다.")
    @GetMapping("")
    public BaseResponse<List<InterestStockResponse>> list() {

        String userId = SecurityUtils.getCurrentUserId();
        List<InterestStockResponse> list = interestStockUseCase.getMyInterestStocks(userId);
        return BaseResponse.onSuccess(list);
    }

    @Operation(summary = "관심 여부 확인", description = "해당 종목이 관심종목인지 여부를 반환합니다.")
    @GetMapping("/check")
    public BaseResponse<Boolean> check(@RequestParam String stockId) {
       
        String userId = SecurityUtils.getCurrentUserId();
        boolean flag = interestStockUseCase.isMyInterestStock(userId, stockId);
        return BaseResponse.onSuccess(flag);
    }
}


