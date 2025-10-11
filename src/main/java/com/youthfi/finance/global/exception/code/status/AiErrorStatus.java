package com.youthfi.finance.global.exception.code.status;

import org.springframework.http.HttpStatus;

import com.youthfi.finance.global.exception.code.BaseCode;
import com.youthfi.finance.global.exception.code.BaseCodeInterface;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AiErrorStatus implements BaseCodeInterface {

    // AI 서비스 관련 에러
    AI_CHATBOT_CONNECTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI001", "AI 챗봇 서비스 연결에 실패했습니다."),
    AI_PORTFOLIO_CONNECTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI002", "AI 포트폴리오 서비스 연결에 실패했습니다."),
    AI_GCP_AUTHENTICATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI003", "GCP 인증에 실패했습니다."),
    AI_INVALID_CHAT_REQUEST(HttpStatus.BAD_REQUEST, "AI004", "유효하지 않은 채팅 요청입니다."),
    AI_INVALID_PORTFOLIO_REQUEST(HttpStatus.BAD_REQUEST, "AI005", "유효하지 않은 포트폴리오 요청입니다."),
    AI_SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "AI006", "세션을 찾을 수 없습니다."),
    AI_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "AI007", "AI 서비스를 사용할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public BaseCode getCode() {
        return BaseCode.builder()
                .httpStatus(this.httpStatus)
                .code(this.code)
                .message(this.message)
                .build();
    }
}
