package com.youthfi.finance.global.exception;

import com.youthfi.finance.global.exception.code.status.AiErrorStatus;

public class AiException extends RestApiException {

    public AiException(AiErrorStatus errorStatus) {
        super(errorStatus);
    }

    public AiException(AiErrorStatus errorStatus, Throwable cause) {
        super(errorStatus, cause);
    }

    public static AiException chatbotApiConnectionFailed(Throwable cause) {
        return new AiException(AiErrorStatus.AI_CHATBOT_CONNECTION_FAILED, cause);
    }

    public static AiException portfolioApiConnectionFailed(Throwable cause) {
        return new AiException(AiErrorStatus.AI_PORTFOLIO_CONNECTION_FAILED, cause);
    }

    public static AiException gcpAuthenticationFailed(Throwable cause) {
        return new AiException(AiErrorStatus.AI_GCP_AUTHENTICATION_FAILED, cause);
    }

    public static AiException invalidChatRequest() {
        return new AiException(AiErrorStatus.AI_INVALID_CHAT_REQUEST);
    }

    public static AiException invalidPortfolioRequest() {
        return new AiException(AiErrorStatus.AI_INVALID_PORTFOLIO_REQUEST);
    }

    public static AiException sessionNotFound(String sessionId) {
        return new AiException(AiErrorStatus.AI_SESSION_NOT_FOUND);
    }

    public static AiException serviceUnavailable() {
        return new AiException(AiErrorStatus.AI_SERVICE_UNAVAILABLE);
    }
}
