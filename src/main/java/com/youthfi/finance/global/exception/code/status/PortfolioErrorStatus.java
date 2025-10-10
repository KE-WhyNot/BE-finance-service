package com.youthfi.finance.global.exception.code.status;

import com.youthfi.finance.global.exception.code.BaseCode;
import com.youthfi.finance.global.exception.code.BaseCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PortfolioErrorStatus implements BaseCodeInterface {

    // ===========================================
    // 사용자 관련 예외
    // ===========================================
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "PORTFOLIO4001", "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "PORTFOLIO4002", "이미 존재하는 사용자입니다."),

    // ===========================================
    // 투자성향 프로필 관련 예외
    // ===========================================
    INVESTMENT_PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "PORTFOLIO5001", "투자성향 정보가 없습니다. 먼저 설문을 완료해주세요."),
    INVESTMENT_PROFILE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "PORTFOLIO5002", "이미 투자성향 프로필이 존재합니다."),
    INVALID_AVAILABLE_ASSETS(HttpStatus.BAD_REQUEST, "PORTFOLIO5003", "투자가능자산은 0보다 커야 합니다."),
    INVALID_INVESTMENT_GOAL(HttpStatus.BAD_REQUEST, "PORTFOLIO5004", "유효하지 않은 투자목표입니다."),
    INVALID_INVESTMENT_PROFILE_TYPE(HttpStatus.BAD_REQUEST, "PORTFOLIO5005", "유효하지 않은 투자성향 유형입니다."),

    // ===========================================
    // 포트폴리오 관련 예외
    // ===========================================
    PORTFOLIO_NOT_FOUND(HttpStatus.NOT_FOUND, "PORTFOLIO6001", "포트폴리오를 찾을 수 없습니다."),
    PORTFOLIO_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "PORTFOLIO6002", "이미 존재하는 포트폴리오입니다."),
    PORTFOLIO_RECOMMENDATION_NOT_FOUND(HttpStatus.NOT_FOUND, "PORTFOLIO6003", "추천 포트폴리오가 없습니다. 먼저 포트폴리오를 생성해주세요."),
    PORTFOLIO_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PORTFOLIO6004", "포트폴리오 생성에 실패했습니다."),

    // ===========================================
    // 포트폴리오 종목 관련 예외
    // ===========================================
    STOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "PORTFOLIO7001", "종목을 찾을 수 없습니다."),
    INVALID_ALLOCATION_PERCENTAGE(HttpStatus.BAD_REQUEST, "PORTFOLIO7002", "배분 비율은 0% 초과 100% 이하여야 합니다."),
    ALLOCATION_PERCENTAGE_EXCEEDED(HttpStatus.BAD_REQUEST, "PORTFOLIO7003", "총 배분 비율이 100%를 초과할 수 없습니다."),
    STOCK_ALREADY_IN_PORTFOLIO(HttpStatus.BAD_REQUEST, "PORTFOLIO7004", "이미 포트폴리오에 포함된 종목입니다."),

    // ===========================================
    // 섹터 관련 예외
    // ===========================================
    SECTOR_NOT_FOUND(HttpStatus.NOT_FOUND, "PORTFOLIO8001", "섹터를 찾을 수 없습니다."),
    INVALID_SECTOR_NAME(HttpStatus.BAD_REQUEST, "PORTFOLIO8002", "유효하지 않은 섹터명입니다."),

    // ===========================================
    // LLM API 관련 예외
    // ===========================================
    LLM_API_CONNECTION_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "PORTFOLIO9001", "LLM 서비스와의 통신에 실패했습니다."),
    LLM_API_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "PORTFOLIO9002", "LLM 서비스 응답 시간이 초과되었습니다."),
    LLM_API_INVALID_RESPONSE(HttpStatus.BAD_GATEWAY, "PORTFOLIO9003", "LLM 서비스로부터 유효하지 않은 응답을 받았습니다."),
    LLM_API_RATE_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "PORTFOLIO9004", "LLM 서비스 요청 한도를 초과했습니다."),

    // ===========================================
    // 리스크 분석 관련 예외
    // ===========================================
    RISK_ANALYSIS_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PORTFOLIO10001", "포트폴리오 리스크 분석에 실패했습니다."),
    STOCK_PRICE_FETCH_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "PORTFOLIO10002", "주식 가격 정보 조회에 실패했습니다."),
    INVALID_RISK_PARAMETERS(HttpStatus.BAD_REQUEST, "PORTFOLIO10003", "유효하지 않은 리스크 분석 매개변수입니다.");

    private final HttpStatus httpStatus;
    private final boolean isSuccess = false;
    private final String code;
    private final String message;

    public BaseCode getCode() {
        return BaseCode.builder()
                .httpStatus(httpStatus)
                .isSuccess(isSuccess)
                .code(code)
                .message(message)
                .build();
    }
}
