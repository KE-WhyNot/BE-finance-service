package com.youthfi.finance.domain.stock.ui;

import com.youthfi.finance.domain.stock.application.dto.response.UserHoldingResponse;
import com.youthfi.finance.domain.stock.application.usecase.UserStockUseCase;
import com.youthfi.finance.global.common.BaseResponse;
import com.youthfi.finance.global.security.SecurityUtils;
import com.youthfi.finance.global.swagger.BaseApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/holdings")
@RequiredArgsConstructor
@Tag(name = "UserStock", description = "보유 주식 관리 API")
public class UserStockController implements BaseApi {

    private final UserStockUseCase userStockUseCase;

    @Operation(summary = "내 보유 주식 목록 조회", description = "사용자의 보유 주식 목록을 조회합니다. 실시간 가격 계산은 프론트에서 수행합니다.")
    @GetMapping("")
    public BaseResponse<List<UserHoldingResponse>> getMyHoldings() {

        String userId = SecurityUtils.getCurrentUserId();
        List<UserHoldingResponse> holdings = userStockUseCase.getMyHoldings(userId);
        return BaseResponse.onSuccess(holdings);
    }

    @Operation(summary = "특정 종목 보유 정보 조회", description = "특정 종목의 보유 정보를 조회합니다. 실시간 가격 계산은 프론트에서 수행합니다.")
    @GetMapping("/{stockId}")
    public BaseResponse<UserHoldingResponse> getMyHoldingByStockId(@PathVariable String stockId) {

        String userId = SecurityUtils.getCurrentUserId();
        UserHoldingResponse holding = userStockUseCase.getMyHoldingByStockId(userId, stockId);
        return BaseResponse.onSuccess(holding);
    }
}
