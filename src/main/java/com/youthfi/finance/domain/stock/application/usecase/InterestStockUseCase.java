package com.youthfi.finance.domain.stock.application.usecase;

import com.youthfi.finance.domain.stock.domain.entity.InterestStock;
import com.youthfi.finance.domain.stock.domain.service.InterestStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterestStockUseCase {

    private final InterestStockService interestStockService;

    /**
     * 관심종목 추가
     */
    @Transactional
    public InterestStock addInterestStock(Long userId, String stockId) {
        return interestStockService.addInterestStock(userId, stockId);
    }

    /**
     * 관심종목 제거
     */
    @Transactional
    public void removeInterestStock(Long userId, String stockId) {
        interestStockService.removeInterestStock(userId, stockId);
    }

    /**
     * 관심종목 토글 (추가/제거)
     */
    @Transactional
    public InterestStock toggleInterestStock(Long userId, String stockId) {
        return interestStockService.toggleInterestStock(userId, stockId);
    }

    /**
     * 내 관심종목 목록 조회
     */
    public List<InterestStock> getMyInterestStocks(Long userId) {
        return interestStockService.getInterestStocksByUserId(userId);
    }

    /**
     * 특정 종목 관심 여부 확인
     */
    public boolean isMyInterestStock(Long userId, String stockId) {
        return interestStockService.isInterestStock(userId, stockId);
    }

    /**
     * 특정 섹터의 관심종목 조회
     */
    public List<InterestStock> getMyInterestStocksBySector(Long userId, Long sectorId) {
        return interestStockService.getInterestStocksByUserIdAndSectorId(userId, sectorId);
    }

    /**
     * 내 관심종목 개수 조회
     */
    public long getMyInterestStockCount(Long userId) {
        return interestStockService.getInterestStockCountByUserId(userId);
    }

    /**
     * 관심종목 존재 여부 확인
     */
    public boolean existsMyInterestStock(Long userId, String stockId) {
        return interestStockService.existsInterestStock(userId, stockId);
    }
}
