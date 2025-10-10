package com.youthfi.finance.domain.stock.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 캔들 데이터 (날짜, 시가, 종가, 거래량)
 */
@Schema(description = "캔들 데이터 응답")
public record CandleDataResponse(
    @Schema(description = "날짜 (yyyy-MM-dd)", example = "2025-01-01")
    String date,

    @Schema(description = "시간 (HH:mm)", example = "09:30")
    String time,

    @Schema(description = "시가", example = "70000")
    Long open,

    @Schema(description = "고가", example = "71500")
    Long high,

    @Schema(description = "저가", example = "69500")
    Long low,

    @Schema(description = "종가", example = "71000")
    Long close,

    @Schema(description = "거래량", example = "12345678")
    Long volume
) {
    
    /**
     * 캔들 데이터 생성 (KIS API 응답에서 변환용) - 분봉용
     */
    public static CandleDataResponse of(String dateStr, String timeStr, String open, String high, String low, String close, String volume) {
        String formattedDate = safeFormatDate(dateStr);
        String formattedTime = safeFormatTime(timeStr);
        Long openVal = safeParseLong(open);
        Long highVal = safeParseLong(high);
        Long lowVal = safeParseLong(low);
        Long closeVal = safeParseLong(close);
        Long volumeVal = safeParseLong(volume);

        return new CandleDataResponse(
            formattedDate,
            formattedTime,
            openVal,
            highVal,
            lowVal,
            closeVal,
            volumeVal
        );
    }

    /**
     * 캔들 데이터 생성 (KIS API 응답에서 변환용) - 일/월/연봉용 (시간 없음)
     */
    public static CandleDataResponse of(String dateStr, String open, String high, String low, String close, String volume) {
        String formattedDate = safeFormatDate(dateStr);
        Long openVal = safeParseLong(open);
        Long highVal = safeParseLong(high);
        Long lowVal = safeParseLong(low);
        Long closeVal = safeParseLong(close);
        Long volumeVal = safeParseLong(volume);

        return new CandleDataResponse(
            formattedDate,
            "", // 시간 없음
            openVal,
            highVal,
            lowVal,
            closeVal,
            volumeVal
        );
    }

    private static String safeFormatDate(String yyyymmdd) {
        if (yyyymmdd == null || yyyymmdd.length() < 8) {
            return ""; // 비정상 값은 빈 문자열로 반환
        }
        return yyyymmdd.substring(0, 4) + "-" + yyyymmdd.substring(4, 6) + "-" + yyyymmdd.substring(6, 8);
    }

    private static String safeFormatTime(String hhmm) {
        if (hhmm == null || hhmm.length() < 4) {
            return ""; // 비정상 값은 빈 문자열로 반환
        }
        return hhmm.substring(0, 2) + ":" + hhmm.substring(2, 4);
    }

    private static Long safeParseLong(String value) {
        if (value == null || value.isEmpty()) {
            return 0L;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}
