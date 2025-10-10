package com.youthfi.finance.domain.stock.domain.service;

import com.youthfi.finance.domain.stock.domain.entity.InterestStock;
import com.youthfi.finance.domain.stock.domain.entity.Stock;
import com.youthfi.finance.domain.stock.domain.entity.Sector;
import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.domain.stock.domain.repository.InterestStockRepository;
import com.youthfi.finance.domain.stock.domain.repository.StockRepository;
import com.youthfi.finance.domain.stock.domain.repository.SectorRepository;
import com.youthfi.finance.domain.user.domain.repository.UserRepository;
import com.youthfi.finance.global.exception.StockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterestStockService {

    private final InterestStockRepository interestStockRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final SectorRepository sectorRepository;
    
    /**
     * 관심종목 추가        
     */
    public InterestStock addInterestStock(String userId, String stockId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> StockException.userNotFound(userId));
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> StockException.stockNotFound(stockId));
        Sector sector = stock.getSector();

        return interestStockRepository.findByUserUserIdAndStockStockId(userId, stockId)
                .map(existing -> { existing.setInterest(true); return interestStockRepository.save(existing); })
                .orElseGet(() -> interestStockRepository.save(InterestStock.builder()
                        .user(user)
                        .stock(stock)
                        .sector(sector)
                        .interestFlag(true)
                        .build()));
    }
    /**
     * 관심종목 제거
     */
    public void removeInterestStock(String userId, String stockId) {
        InterestStock interestStock = interestStockRepository.findByUserUserIdAndStockStockId(userId, stockId)
                .orElseThrow(() -> StockException.interestStockNotFound());
        interestStock.setInterest(false);
        interestStockRepository.save(interestStock);
    }

    /**
     * 관심 토글 
     */
    public InterestStock toggleInterestStock(String userId, String stockId) {
        InterestStock interestStock = interestStockRepository.findByUserUserIdAndStockStockId(userId, stockId)
                .orElseGet(() -> addInterestStock(userId, stockId));
        interestStock.toggleInterest();
        return interestStockRepository.save(interestStock);
    }

    /**
     * 사용자별 관심종목 조회 
     */
    public List<InterestStock> getInterestStocksByUserId(String userId) {
        return interestStockRepository.findByUserUserIdAndInterestFlagTrue(userId);
    }

    /**
     * 특정 종목 관심 여부 확인 
     */
    public boolean isInterestStock(String userId, String stockId) {
        Optional<InterestStock> interestStock = interestStockRepository
                .findByUserUserIdAndStockStockId(userId, stockId);
        return interestStock.map(InterestStock::isInterested).orElse(false);
    }

    /**
     * 특정 섹터의 관심종목 조회 
     */
    public List<InterestStock> getInterestStocksByUserIdAndSectorId(String userId, Long sectorId) {
        return interestStockRepository.findByUserUserIdAndSectorSectorIdAndInterestFlagTrue(userId, sectorId);
    }

    /**
     * 사용자별 관심종목 개수 조회 
     */
    public long getInterestStockCountByUserId(String userId) {
        return interestStockRepository.findByUserUserIdAndInterestFlagTrue(userId).size();
    }

    /**
     * 관심종목 존재 여부 확인 
     */
    public boolean existsInterestStock(String userId, String stockId) {
        return interestStockRepository.findByUserUserIdAndStockStockId(userId, stockId).isPresent();
    }
}
