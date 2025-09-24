package com.youthfi.finance.controller;

import com.youthfi.finance.domain.stock.infra.StockWebSocketApprovalKeyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KisWebSocketTestController {
    private final StockWebSocketApprovalKeyService approvalKeyService;

    public KisWebSocketTestController(StockWebSocketApprovalKeyService approvalKeyService) {
        this.approvalKeyService = approvalKeyService;
    }

    @GetMapping("/api/kis/ws-approval-key")
    public String getWsApprovalKey() {
        return approvalKeyService.getApprovalKey();
    }
}