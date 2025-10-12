package com.youthfi.finance.global.exception.code.status;

import com.youthfi.finance.global.exception.code.BaseCode;
import com.youthfi.finance.global.exception.code.BaseCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StockErrorStatus implements BaseCodeInterface {

    // ===========================================
    // 사용자 관련 예외
    // ===========================================
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "STOCK4001", "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "STOCK4002", "이미 존재하는 사용자입니다."),

    // ===========================================
    // 종목 관련 예외
    // ===========================================
    STOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "STOCK5001", "종목을 찾을 수 없습니다."),
    STOCK_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "STOCK5002", "이미 존재하는 종목입니다."),
    INVALID_STOCK_CODE(HttpStatus.BAD_REQUEST, "STOCK5003", "유효하지 않은 종목코드입니다."),
    STOCK_DATA_NOT_AVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "STOCK5004", "종목 데이터를 사용할 수 없습니다."),

    // ===========================================
    // 관심종목 관련 예외
    // ===========================================
    INTEREST_STOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "STOCK6001", "관심종목을 찾을 수 없습니다."),
    INTEREST_STOCK_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "STOCK6002", "이미 관심종목으로 등록된 종목입니다."),
    INTEREST_STOCK_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "STOCK6003", "관심종목 등록 한도를 초과했습니다."),
    INTEREST_STOCK_NOT_REGISTERED(HttpStatus.BAD_REQUEST, "STOCK6004", "관심종목으로 등록되지 않은 종목입니다."),

    // ===========================================
    // 보유종목 관련 예외
    // ===========================================
    USER_STOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "STOCK7001", "보유종목을 찾을 수 없습니다."),
    INSUFFICIENT_STOCK_QUANTITY(HttpStatus.BAD_REQUEST, "STOCK7002", "보유 수량이 부족합니다."),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "STOCK7003", "유효하지 않은 수량입니다."),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "STOCK7004", "유효하지 않은 가격입니다."),

    // ===========================================
    // 거래 관련 예외
    // ===========================================
    TRADING_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "STOCK8001", "거래가 허용되지 않습니다."),
    INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "STOCK8002", "잔고가 부족합니다."),
    TRADING_AMOUNT_TOO_SMALL(HttpStatus.BAD_REQUEST, "STOCK8003", "거래 금액이 최소 금액보다 작습니다."),
    TRADING_AMOUNT_TOO_LARGE(HttpStatus.BAD_REQUEST, "STOCK8004", "거래 금액이 최대 금액을 초과합니다."),
    MARKET_CLOSED(HttpStatus.BAD_REQUEST, "STOCK8005", "장이 마감되어 거래할 수 없습니다."),
    INVALID_TRADING_TYPE(HttpStatus.BAD_REQUEST, "STOCK8006", "유효하지 않은 거래 유형입니다."),
    EXECUTION_NOT_FOUND(HttpStatus.NOT_FOUND, "STOCK8007", "거래내역을 찾을 수 없습니다."),

    // ===========================================
    // 차트 관련 예외
    // ===========================================
    CHART_DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "STOCK9001", "차트 데이터를 찾을 수 없습니다."),
    INVALID_CHART_PERIOD(HttpStatus.BAD_REQUEST, "STOCK9002", "유효하지 않은 차트 기간입니다."),
    CHART_DATA_FETCH_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "STOCK9003", "차트 데이터 조회에 실패했습니다."),
    CHART_CACHE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "STOCK9004", "차트 캐시 처리 중 오류가 발생했습니다."),

    // ===========================================
    // KIS API 관련 예외
    // ===========================================
    KIS_API_CONNECTION_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "STOCK10001", "KIS API와의 통신에 실패했습니다."),
    KIS_API_RESPONSE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "STOCK10002", "KIS API 응답 처리 중 오류가 발생했습니다."),
    KIS_API_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "STOCK10003", "KIS API 응답 시간이 초과되었습니다."),
    KIS_API_INVALID_RESPONSE(HttpStatus.BAD_GATEWAY, "STOCK10004", "KIS API로부터 유효하지 않은 응답을 받았습니다."),
    KIS_API_RATE_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "STOCK10005", "KIS API 요청 한도를 초과했습니다."),
    KIS_API_AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "STOCK10006", "KIS API 인증에 실패했습니다."),
    KIS_API_INSUFFICIENT_PERMISSION(HttpStatus.FORBIDDEN, "STOCK10007", "KIS API 권한이 부족합니다."),
    CURRENT_PRICE_NOT_AVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "STOCK10008", "현재가 정보를 가져올 수 없습니다."),
    CURRENT_PRICE_FETCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "STOCK10009", "현재가 조회 중 오류가 발생했습니다."),
    KIS_API_INVALID_RESPONSE_STRUCTURE(HttpStatus.BAD_GATEWAY, "STOCK10010", "KIS API 응답 구조가 올바르지 않습니다."),

    // ===========================================
    // WebSocket 관련 예외
    // ===========================================
    WEBSOCKET_CONNECTION_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "STOCK11001", "WebSocket 연결에 실패했습니다."),
    WEBSOCKET_AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "STOCK11002", "WebSocket 인증에 실패했습니다."),
    WEBSOCKET_SUBSCRIPTION_FAILED(HttpStatus.BAD_REQUEST, "STOCK11003", "WebSocket 구독에 실패했습니다."),
    WEBSOCKET_MESSAGE_PARSING_FAILED(HttpStatus.BAD_REQUEST, "STOCK11004", "WebSocket 메시지 파싱에 실패했습니다."),


    // ===========================================
    // 섹터 관련 예외
    // ===========================================
    SECTOR_NOT_FOUND(HttpStatus.NOT_FOUND, "STOCK13001", "섹터를 찾을 수 없습니다."),
    INVALID_SECTOR_NAME(HttpStatus.BAD_REQUEST, "STOCK13002", "유효하지 않은 섹터명입니다.");

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
