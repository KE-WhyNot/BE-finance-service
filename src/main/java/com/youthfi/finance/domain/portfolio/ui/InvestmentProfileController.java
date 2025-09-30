package com.youthfi.finance.domain.portfolio.ui;

import com.youthfi.finance.domain.portfolio.application.usecase.InvestmentProfileUseCase;
import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfile;
import com.youthfi.finance.global.common.BaseResponse;
import com.youthfi.finance.global.swagger.BaseApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/investment-profile")
@RequiredArgsConstructor
@Tag(name = "InvestmentProfile", description = "투자성향 프로필 관리 API")
public class InvestmentProfileController implements BaseApi {

    private final InvestmentProfileUseCase investmentProfileUseCase;

    /**
     * 투자성향 설문을 완료하거나 새 프로필을 생성 및 저장합니다.
     *
     * @param userId 사용자 ID (필수)
     * @param investmentProfile 투자성향 유형 (필수)
     * @param availableAssets 투자가능 자산 (필수, 0 초과)
     * @param investmentGoal 투자 목표 (필수)
     * @return 생성 또는 수정된 InvestmentProfile을 포함하는 공통 응답 객체
     * @throws RuntimeException 사용자 미존재 시 예외 발생
     * @throws IllegalArgumentException 투자가능 자산이 0 이하일 경우 예외 발생
     */

    @Operation(summary = "투자성향 설문 완료", description = "투자성향 설문을 완료하거나 새 프로필을 생성 및 저장합니다.")
    @PostMapping("/complete")
    public BaseResponse<InvestmentProfile> completeInvestmentProfile(
            @RequestParam Long userId,
            @RequestParam InvestmentProfile.InvestmentProfileType investmentProfile,
            @RequestParam java.math.BigDecimal availableAssets,
            @RequestParam InvestmentProfile.InvestmentGoal investmentGoal) {
        InvestmentProfile profile = investmentProfileUseCase.completeInvestmentProfile(
            userId, investmentProfile, availableAssets, investmentGoal);
        return BaseResponse.onSuccess(profile);
    }

    /**
     * 특정 사용자의 투자성향 프로필 정보를 조회합니다.
     *
     * @param userId 사용자 ID (필수)
     * @return 조회된 InvestmentProfile을 포함하는 공통 응답 객체
     * @throws RuntimeException 투자성향 정보 없을 경우 예외 발생
     */

    @Operation(summary = "내 투자성향 조회", description = "사용자의 투자성향 프로필 정보를 조회합니다.")
    @GetMapping("/my")
    public BaseResponse<InvestmentProfile> getMyInvestmentProfile(@RequestParam Long userId) {
        InvestmentProfile profile = investmentProfileUseCase.getMyInvestmentProfile(userId);
        return BaseResponse.onSuccess(profile);
    }

    /**
     * 특정 사용자의 투자성향 프로필을 수정합니다.
     *
     * @param userId 사용자 ID (필수)
     * @param investmentProfile 수정할 투자성향 유형 (필수)
     * @param availableAssets 수정할 투자가능 자산 (필수, 0 초과)
     * @param investmentGoal 수정할 투자 목표 (필수)
     * @return 수정된 InvestmentProfile을 포함하는 공통 응답 객체
     * @throws RuntimeException 사용자 미존재 시 예외 발생
     * @throws IllegalArgumentException 투자가능 자산이 0 이하일 경우 예외 발생
     */

    @Operation(summary = "내 투자성향 수정", description = "사용자의 투자성향 프로필 정보를 수정합니다.")
    @PutMapping("/my")
    public BaseResponse<InvestmentProfile> updateMyInvestmentProfile(
            @RequestParam Long userId,
            @RequestParam InvestmentProfile.InvestmentProfileType investmentProfile,
            @RequestParam java.math.BigDecimal availableAssets,
            @RequestParam InvestmentProfile.InvestmentGoal investmentGoal) {
        InvestmentProfile profile = investmentProfileUseCase.updateMyInvestmentProfile(
            userId, investmentProfile, availableAssets, investmentGoal);
        return BaseResponse.onSuccess(profile);
    }

    /**
     * 특정 사용자의 투자성향 프로필을 삭제합니다.
     *
     * @param userId 사용자 ID (필수)
     * @return 성공 여부를 담은 공통 응답 객체
     * @throws RuntimeException 사용자 미존재 시 예외 발생
     */
    
    @Operation(summary = "내 투자성향 삭제", description = "사용자의 투자성향 프로필을 삭제합니다.")
    @DeleteMapping("/my")
    public BaseResponse<Void> deleteMyInvestmentProfile(@RequestParam Long userId) {
        investmentProfileUseCase.deleteMyInvestmentProfile(userId);
        return BaseResponse.onSuccess();
    }

    /**
     * 특정 투자성향 유형에 해당하는 사용자 리스트를 조회합니다.
     *
     * @param investmentProfile 투자성향 유형 (필수)
     * @return 투자성향 리스트를 포함하는 공통 응답 객체
     */

    @Operation(summary = "투자성향별 사용자 조회", description = "특정 투자성향 유형을 가진 사용자 목록을 조회합니다.")
    @GetMapping("/by-type")
    public BaseResponse<List<InvestmentProfile>> getUsersByInvestmentProfile(
            @RequestParam InvestmentProfile.InvestmentProfileType investmentProfile) {
        List<InvestmentProfile> profiles = investmentProfileUseCase.getUsersByInvestmentProfile(investmentProfile);
        return BaseResponse.onSuccess(profiles);
    }

    /**
     * 특정 투자 목표에 해당하는 사용자 리스트를 조회합니다.
     *
     * @param investmentGoal 투자 목표 (필수)
     * @return 투자성향 리스트를 포함하는 공통 응답 객체
     */

    @Operation(summary = "투자목표별 사용자 조회", description = "특정 투자 목표를 가진 사용자 목록을 조회합니다.")
    @GetMapping("/by-goal")
    public BaseResponse<List<InvestmentProfile>> getUsersByInvestmentGoal(
            @RequestParam InvestmentProfile.InvestmentGoal investmentGoal) {
        List<InvestmentProfile> profiles = investmentProfileUseCase.getUsersByInvestmentGoal(investmentGoal);
        return BaseResponse.onSuccess(profiles);
    }

    /**
     * 사용자가 투자성향 설문을 완료했는지 여부를 조회합니다.
     *
     * @param userId 사용자 ID (필수)
     * @return 완료 여부(boolean)를 포함하는 공통 응답 객체
     */
    
    @Operation(summary = "투자성향 완료 여부 확인", description = "사용자가 투자성향 설문을 완료했는지 확인합니다.")
    @GetMapping("/exists")
    public BaseResponse<Boolean> hasCompletedInvestmentProfile(@RequestParam Long userId) {
        boolean exists = investmentProfileUseCase.hasCompletedInvestmentProfile(userId);
        return BaseResponse.onSuccess(exists);
    }
}


