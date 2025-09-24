package com.youthfi.finance.controller;

import com.youthfi.finance.service.KisTokenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KisTokenTestController {
    private final KisTokenService kisTokenService;

    public KisTokenTestController(KisTokenService kisTokenService) {
        this.kisTokenService = kisTokenService;
    }

    @GetMapping("/api/kis/token")
    public String getKisToken() {
        return kisTokenService.getValidToken();
    }
}