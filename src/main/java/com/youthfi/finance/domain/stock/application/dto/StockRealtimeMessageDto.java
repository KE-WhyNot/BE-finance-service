package com.youthfi.finance.domain.stock.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "실시간 주식 WebSocket 메시지 포맷")
public class StockRealtimeMessageDto {
    @Schema(description = "종목코드")
    private String symbol;
    @Schema(description = "매도호가 1~4")
    private List<Integer> askPrices;
    @Schema(description = "매수호가 1~4")
    private List<Integer> bidPrices;
    @Schema(description = "매도호가 잔량 1~4")
    private List<Integer> askQtys;
    @Schema(description = "매수호가 잔량 1~4")
    private List<Integer> bidQtys;
    @Schema(description = "현재가")
    private Integer stckPrpr;
    @Schema(description = "전일대비")
    private Integer prdyVrss;
    @Schema(description = "전일대비율")
    private Double prdyCtrt;
    @Schema(description = "최저가")
    private Integer stckLwpr;
    @Schema(description = "최고가")
    private Integer stckHgpr;
    @Schema(description = "타임스탬프")
    private String timestamp;

    // getter/setter
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public List<Integer> getAskPrices() { return askPrices; }
    public void setAskPrices(List<Integer> askPrices) { this.askPrices = askPrices; }
    public List<Integer> getBidPrices() { return bidPrices; }
    public void setBidPrices(List<Integer> bidPrices) { this.bidPrices = bidPrices; }
    public List<Integer> getAskQtys() { return askQtys; }
    public void setAskQtys(List<Integer> askQtys) { this.askQtys = askQtys; }
    public List<Integer> getBidQtys() { return bidQtys; }
    public void setBidQtys(List<Integer> bidQtys) { this.bidQtys = bidQtys; }
    public Integer getStckPrpr() { return stckPrpr; }
    public void setStckPrpr(Integer stckPrpr) { this.stckPrpr = stckPrpr; }
    public Integer getPrdyVrss() { return prdyVrss; }
    public void setPrdyVrss(Integer prdyVrss) { this.prdyVrss = prdyVrss; }
    public Double getPrdyCtrt() { return prdyCtrt; }
    public void setPrdyCtrt(Double prdyCtrt) { this.prdyCtrt = prdyCtrt; }
    public Integer getStckLwpr() { return stckLwpr; }
    public void setStckLwpr(Integer stckLwpr) { this.stckLwpr = stckLwpr; }
    public Integer getStckHgpr() { return stckHgpr; }
    public void setStckHgpr(Integer stckHgpr) { this.stckHgpr = stckHgpr; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
