package com.youthfi.finance.global.exception;

import com.youthfi.finance.global.exception.code.BaseCode;
import com.youthfi.finance.global.exception.code.BaseCodeInterface;
import com.youthfi.finance.global.exception.code.status.StockErrorStatus;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class StockException extends RestApiException {

    public StockException(StockErrorStatus errorStatus) {
        super(errorStatus);
    }

    public StockException(StockErrorStatus errorStatus, String message) {
        super(new BaseCodeInterface() {
            @Override
            public BaseCode getCode() {
                return errorStatus.getCode().toBuilder().message(message).build();
            }
        });
    }

    public StockException(StockErrorStatus errorStatus, String message, Throwable cause) {
        super(new BaseCodeInterface() {
            @Override
            public BaseCode getCode() {
                return errorStatus.getCode().toBuilder().message(message).build();
            }
        }, cause);
    }


    // 사용자 관련 편의 메서드

    public static StockException userNotFound(String userId) {
        return new StockException(StockErrorStatus.USER_NOT_FOUND, 
            "사용자를 찾을 수 없습니다: " + userId);
    }


    // 종목 관련 편의 메서드

    public static StockException stockNotFound(String stockCode) {
        return new StockException(StockErrorStatus.STOCK_NOT_FOUND, 
            "종목을 찾을 수 없습니다: " + stockCode);
    }


    // 관심종목 관련 편의 메서드

    public static StockException interestStockNotFound() {
        return new StockException(StockErrorStatus.INTEREST_STOCK_NOT_FOUND);
    }


    // 보유종목 관련 편의 메서드

    public static StockException userStockNotFound() {
        return new StockException(StockErrorStatus.USER_STOCK_NOT_FOUND);
    }


    public static StockException invalidQuantity(Long quantity) {
        return new StockException(StockErrorStatus.INVALID_QUANTITY, 
            "유효하지 않은 수량입니다: " + quantity);
    }

    public static StockException invalidPrice(long price) {
        return new StockException(StockErrorStatus.INVALID_PRICE, 
            "유효하지 않은 가격입니다: " + price);
    }


    // 거래 관련 편의 메서드



    public static StockException insufficientBalance(long required, long available) {
        return new StockException(StockErrorStatus.INSUFFICIENT_BALANCE, 
            String.format("잔고가 부족합니다. 필요: %d, 보유: %d", required, available));
    }

    public static StockException insufficientStockQuantity(String stockCode, Long required, Long available) {
        return new StockException(StockErrorStatus.INSUFFICIENT_STOCK_QUANTITY, 
            String.format("보유 수량이 부족합니다. 종목: %s, 필요: %d, 보유: %d", stockCode, required, available));
    }



    // 차트 관련 편의 메서드


    public static StockException invalidChartPeriod(String period) {
        return new StockException(StockErrorStatus.INVALID_CHART_PERIOD, 
            "유효하지 않은 차트 기간입니다: " + period);
    }



    // KIS API 관련 편의 메서드

    public static StockException kisApiConnectionFailed(Throwable cause) {
        return new StockException(StockErrorStatus.KIS_API_CONNECTION_FAILED, 
            "KIS API와의 통신에 실패했습니다.", cause);
    }

    public static StockException kisApiResponseError(Throwable cause) {
        return new StockException(StockErrorStatus.KIS_API_RESPONSE_ERROR, 
            "KIS API 응답 처리 중 오류가 발생했습니다.", cause);
    }

    public static StockException currentPriceNotAvailable(String stockId) {
        return new StockException(StockErrorStatus.CURRENT_PRICE_NOT_AVAILABLE, 
            "현재가 정보를 가져올 수 없습니다: " + stockId);
    }

    public static StockException currentPriceFetchFailed(String stockId, Throwable cause) {
        return new StockException(StockErrorStatus.CURRENT_PRICE_FETCH_FAILED, 
            "현재가 조회 중 오류가 발생했습니다: " + stockId, cause);
    }


    // WebSocket 관련 편의 메서드

    public static StockException websocketConnectionFailed(Throwable cause) {
        return new StockException(StockErrorStatus.WEBSOCKET_CONNECTION_FAILED, 
            "WebSocket 연결에 실패했습니다.", cause);
    }

    // 유틸리티 메서드들

    /**
     * 관심종목 존재 여부를 확인하고 없으면 예외 발생
     */
    public static void validateInterestStockExists(Object interestStock) {
        if (interestStock == null) {
            throw interestStockNotFound();
        }
    }



    /**
     * 거래내역 존재 여부를 확인하고 없으면 예외 발생
     */
    public static void validateExecutionExists(Object execution) {
        if (execution == null) {
            throw new StockException(StockErrorStatus.EXECUTION_NOT_FOUND, "거래내역을 찾을 수 없습니다.");
        }
    }



    /**
     * 수량 유효성 검증
     */
    public static void validateQuantity(Long quantity) {
        if (quantity == null || quantity <= 0) {
            throw invalidQuantity(quantity);
        }
    }

    /**
     * 가격 유효성 검증
     */
    public static void validatePrice(Long price) {
        if (price == null || price <= 0) {
            throw invalidPrice(price);
        }
    }

    /**
     * 잔고 충분성 검증
     */
    public static void validateSufficientBalance(BigDecimal currentBalance, BigDecimal requiredAmount) {
        if (currentBalance.compareTo(requiredAmount) < 0) {
            throw insufficientBalance(requiredAmount.longValue(), currentBalance.longValue());
        }
    }

}
