package com.youthfi.finance.domain.user.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youthfi.finance.domain.user.application.dto.response.UserResponse;
import com.youthfi.finance.domain.user.application.usecase.UserUseCase;
import com.youthfi.finance.global.common.BaseResponse;
import com.youthfi.finance.global.security.SecurityUtils;
import com.youthfi.finance.global.swagger.BaseApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 관리 API")
public class UserController implements BaseApi {

    private final UserUseCase userUseCase;

    @Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다.")
    @PostMapping
    public BaseResponse<UserResponse> createUser() {
        // 1. 인증된 사용자 ID 조회
        String userId = SecurityUtils.getCurrentUserId();
        
        // 2. UseCase 호출
        UserResponse user = userUseCase.createUser(userId);
        
        // 3. 응답 DTO 변환 및 반환
        return BaseResponse.onSuccess(user);
    }


    @Operation(summary = "내 잔고 조회", description = "사용자의 잔고 정보를 조회합니다.")
    @GetMapping("/balance")
    public BaseResponse<UserResponse> getMyBalance() {
        // 1. 인증된 사용자 ID 조회
        String userId = SecurityUtils.getCurrentUserId();
        
        // 2. UseCase 호출
        UserResponse balance = userUseCase.getMyBalance(userId);
        
        // 3. 응답 DTO 변환 및 반환
        return BaseResponse.onSuccess(balance);
    }



    @Operation(summary = "사용자 존재 여부 확인", description = "해당 사용자 ID가 존재하는지 확인합니다.")
    @GetMapping("/exists")
    public BaseResponse<Boolean> existsUser() {
        // 1. 인증된 사용자 ID 조회
        String userId = SecurityUtils.getCurrentUserId();
        
        // 2. UseCase 호출
        boolean exists = userUseCase.existsUser(userId);
        
        // 3. 응답 DTO 변환 및 반환
        return BaseResponse.onSuccess(exists);
    }

    @Operation(summary = "인증 테스트", description = "X-User-Id 헤더 기반 인증이 정상 작동하는지 테스트합니다.")
    @GetMapping("/auth-test")
    public BaseResponse<String> authTest() {
        String userId = SecurityUtils.getCurrentUserId();
        return BaseResponse.onSuccess("인증 성공! 사용자 ID: " + userId);
    }
}