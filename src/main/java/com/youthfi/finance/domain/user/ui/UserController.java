package com.youthfi.finance.domain.user.ui;

import com.youthfi.finance.domain.user.application.usecase.UserUseCase;
import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.global.common.BaseResponse;
import com.youthfi.finance.global.swagger.BaseApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 관리 API")
public class UserController implements BaseApi {

    private final UserUseCase userUseCase;

    @Operation(summary = "내 프로필 조회", description = "사용자의 프로필 정보를 조회합니다.")
    @GetMapping("/profile")
    public BaseResponse<User> getMyProfile(@RequestParam Long userId) {
        User user = userUseCase.getMyProfile(userId);
        return BaseResponse.onSuccess(user);
    }

    @Operation(summary = "내 프로필 수정", description = "사용자의 이름과 프로필 이미지를 수정합니다.")
    @PutMapping("/profile")
    public BaseResponse<User> updateMyProfile(
            @RequestParam Long userId,
            @RequestParam String name,
            @RequestParam(required = false) String profileImage
    ) {
        User user = userUseCase.updateMyProfile(userId, name, profileImage);
        return BaseResponse.onSuccess(user);
    }

    @Operation(summary = "내 잔고 조회", description = "사용자의 현재 잔고를 조회합니다.")
    @GetMapping("/balance")
    public BaseResponse<BigDecimal> getMyBalance(@RequestParam Long userId) {
        BigDecimal balance = userUseCase.getMyBalance(userId);
        return BaseResponse.onSuccess(balance);
    }

    @Operation(summary = "내 시드머니 조회", description = "사용자의 초기 시드머니를 조회합니다.")
    @GetMapping("/seed-money")
    public BaseResponse<BigDecimal> getMySeedMoney(@RequestParam Long userId) {
        BigDecimal seedMoney = userUseCase.getMySeedMoney(userId);
        return BaseResponse.onSuccess(seedMoney);
    }

    @Operation(summary = "내 수익률 계산", description = "사용자의 현재 수익률을 계산합니다.")
    @GetMapping("/return-rate")
    public BaseResponse<BigDecimal> getMyReturnRate(@RequestParam Long userId) {
        BigDecimal returnRate = userUseCase.calculateMyReturnRate(userId);
        return BaseResponse.onSuccess(returnRate);
    }

    @Operation(summary = "내 수익금 계산", description = "사용자의 현재 수익금을 계산합니다.")
    @GetMapping("/profit")
    public BaseResponse<BigDecimal> getMyProfit(@RequestParam Long userId) {
        BigDecimal profit = userUseCase.calculateMyProfit(userId);
        return BaseResponse.onSuccess(profit);
    }

    @Operation(summary = "사용자 존재 여부 확인", description = "해당 사용자 ID가 존재하는지 확인합니다.")
    @GetMapping("/exists")
    public BaseResponse<Boolean> existsUser(@RequestParam Long userId) {
        boolean exists = userUseCase.existsUser(userId);
        return BaseResponse.onSuccess(exists);
    }
}
