package com.youthfi.finance.domain.stock.ui;

import com.youthfi.finance.domain.stock.application.usecase.InterestStockUseCase;
import com.youthfi.finance.domain.stock.domain.entity.InterestStock;
import com.youthfi.finance.global.common.BaseResponse;
import com.youthfi.finance.global.swagger.BaseApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public BaseResponse<InterestStock> add(@RequestParam Long userId, @RequestParam String stockId) {
        InterestStock result = interestStockUseCase.addInterestStock(userId, stockId);
        return BaseResponse.onSuccess(result);
    }

    @Operation(summary = "관심종목 제거", description = "사용자의 관심종목 목록에서 종목을 제거합니다.")
    @DeleteMapping("")
    public BaseResponse<Void> remove(@RequestParam Long userId, @RequestParam String stockId) {
        interestStockUseCase.removeInterestStock(userId, stockId);
        return BaseResponse.onSuccess();
    }

    @Operation(summary = "관심종목 토글", description = "해당 종목의 관심 여부를 토글합니다.")
    @PostMapping("/toggle")
    public BaseResponse<InterestStock> toggle(@RequestParam Long userId, @RequestParam String stockId) {
        InterestStock result = interestStockUseCase.toggleInterestStock(userId, stockId);
        return BaseResponse.onSuccess(result);
    }

    @Operation(summary = "관심종목 목록 조회", description = "사용자의 관심종목 목록을 반환합니다.")
    @GetMapping("")
    public BaseResponse<List<InterestStock>> list(@RequestParam Long userId) {
        List<InterestStock> list = interestStockUseCase.getMyInterestStocks(userId);
        return BaseResponse.onSuccess(list);
    }

    @Operation(summary = "관심 여부 확인", description = "해당 종목이 관심종목인지 여부를 반환합니다.")
    @GetMapping("/check")
    public BaseResponse<Boolean> check(@RequestParam Long userId, @RequestParam String stockId) {
        boolean flag = interestStockUseCase.isMyInterestStock(userId, stockId);
        return BaseResponse.onSuccess(flag);
    }
}


