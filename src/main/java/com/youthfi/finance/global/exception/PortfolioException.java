package com.youthfi.finance.global.exception;

import com.youthfi.finance.global.exception.code.BaseCode;
import com.youthfi.finance.global.exception.code.BaseCodeInterface;
import com.youthfi.finance.global.exception.code.status.PortfolioErrorStatus;
import lombok.Getter;

@Getter
public class PortfolioException extends RestApiException {

    public PortfolioException(PortfolioErrorStatus errorStatus) {
        super(errorStatus);
    }

    public PortfolioException(PortfolioErrorStatus errorStatus, String message) {
        super(new BaseCodeInterface() {
            @Override
            public BaseCode getCode() {
                return errorStatus.getCode().toBuilder().message(message).build();
            }
        });
    }

    public PortfolioException(PortfolioErrorStatus errorStatus, String message, Throwable cause) {
        super(new BaseCodeInterface() {
            @Override
            public BaseCode getCode() {
                return errorStatus.getCode().toBuilder().message(message).build();
            }
        }, cause);
    }

    // 편의 메서드들
    public static PortfolioException userNotFound(String userId) {
        return new PortfolioException(PortfolioErrorStatus.USER_NOT_FOUND, 
            "사용자를 찾을 수 없습니다: " + userId);
    }

    public static PortfolioException investmentProfileNotFound() {
        return new PortfolioException(PortfolioErrorStatus.INVESTMENT_PROFILE_NOT_FOUND);
    }

    public static PortfolioException portfolioNotFound(Long portfolioId) {
        return new PortfolioException(PortfolioErrorStatus.PORTFOLIO_NOT_FOUND, 
            "포트폴리오를 찾을 수 없습니다: " + portfolioId);
    }

    public static PortfolioException stockNotFound(String stockId) {
        return new PortfolioException(PortfolioErrorStatus.STOCK_NOT_FOUND, 
            "종목을 찾을 수 없습니다: " + stockId);
    }

    public static PortfolioException sectorNotFound(String sectorName) {
        return new PortfolioException(PortfolioErrorStatus.SECTOR_NOT_FOUND, 
            "섹터를 찾을 수 없습니다: " + sectorName);
    }

    public static PortfolioException invalidAllocationPercentage() {
        return new PortfolioException(PortfolioErrorStatus.INVALID_ALLOCATION_PERCENTAGE);
    }

    public static PortfolioException invalidAvailableAssets() {
        return new PortfolioException(PortfolioErrorStatus.INVALID_AVAILABLE_ASSETS);
    }

    public static PortfolioException llmApiConnectionFailed(Throwable cause) {
        return new PortfolioException(PortfolioErrorStatus.LLM_API_CONNECTION_FAILED, 
            "LLM 서비스와의 통신에 실패했습니다.", cause);
    }

    public static PortfolioException riskAnalysisFailed(Throwable cause) {
        return new PortfolioException(PortfolioErrorStatus.RISK_ANALYSIS_FAILED, 
            "포트폴리오 리스크 분석에 실패했습니다.", cause);
    }


    /**
     * 포트폴리오 존재 여부를 확인하고 없으면 예외 발생
     */
    public static void validatePortfolioExists(Object portfolio, Long portfolioId) {
        if (portfolio == null) {
            throw portfolioNotFound(portfolioId);
        }
    }

    /**
     * 투자가능자산 유효성 검증
     */
    public static void validateAvailableAssets(java.math.BigDecimal availableAssets) {
        if (availableAssets == null || availableAssets.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw invalidAvailableAssets();
        }
    }

    /**
     * 배분 비율 유효성 검증
     */
    public static void validateAllocationPercentage(java.math.BigDecimal allocationPct) {
        if (allocationPct == null || 
            allocationPct.compareTo(java.math.BigDecimal.ZERO) <= 0 || 
            allocationPct.compareTo(java.math.BigDecimal.valueOf(100)) > 0) {
            throw invalidAllocationPercentage();
        }
    }

}
