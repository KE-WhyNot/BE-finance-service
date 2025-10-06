package com.youthfi.finance.domain.stock.application.usecase;

import com.youthfi.finance.domain.stock.application.dto.request.InterestStockRequest;
import com.youthfi.finance.domain.stock.application.dto.response.InterestStockResponse;
import com.youthfi.finance.domain.stock.application.mapper.StockMapper;
import com.youthfi.finance.domain.stock.domain.entity.InterestStock;
import com.youthfi.finance.domain.stock.domain.service.InterestStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class InterestStockUseCase {

    private final InterestStockService interestStockService;
    private final StockMapper stockMapper;

    /**
     * 관심종목 추가
     */
    @Transactional
    public InterestStockResponse addInterestStock(String userId, InterestStockRequest request) {

        log.info("관심종목 추가 요청 - 사용자: {}, 종목: {}", userId, request.getStockId());
        InterestStock interestStock = interestStockService.addInterestStock(userId, request.getStockId());
        InterestStockResponse response = stockMapper.toInterestStockResponse(interestStock);
        log.info("관심종목 추가 완료 - 사용자: {}, 종목: {}, 관심종목ID: {}", 
                userId, request.getStockId(), interestStock.getId());
        return response;
    }

    /**
     * 관심종목 제거
     */
    @Transactional
    public void removeInterestStock(String userId, InterestStockRequest request) {

        log.info("관심종목 제거 요청 - 사용자: {}, 종목: {}", userId, request.getStockId());
        interestStockService.removeInterestStock(userId, request.getStockId());
        log.info("관심종목 제거 완료 - 사용자: {}, 종목: {}", userId, request.getStockId());
    }

    /**
     * 관심종목 토글 (추가/제거)
     */
    @Transactional
    public InterestStockResponse toggleInterestStock(String userId, InterestStockRequest request) {

        log.info("관심종목 토글 요청 - 사용자: {}, 종목: {}", userId, request.getStockId());
        InterestStock interestStock = interestStockService.toggleInterestStock(userId, request.getStockId());    
        InterestStockResponse response = stockMapper.toInterestStockResponse(interestStock);      
        log.info("관심종목 토글 완료 - 사용자: {}, 종목: {}, 관심종목ID: {}", 
                userId, request.getStockId(), interestStock.getId());
        return response;
    }

    /**
     * 내 관심종목 목록 조회 
     */
    public List<InterestStockResponse> getMyInterestStocks(String userId) {

        log.info("관심종목 목록 조회 요청 - 사용자: {}", userId);        
        List<InterestStock> interestStocks = interestStockService.getInterestStocksByUserId(userId);
        List<InterestStockResponse> responses = interestStocks.stream()
                .map(stockMapper::toInterestStockResponse)
                .collect(Collectors.toList());       
        log.info("관심종목 목록 조회 완료 - 사용자: {}, 건수: {}", userId, responses.size());
        return responses;
    }

    /**
     * 특정 종목 관심 여부 확인 
     */
    public boolean isMyInterestStock(String userId, String stockId) {

        log.info("관심 여부 확인 요청 - 사용자: {}, 종목: {}", userId, stockId);       
        boolean isInterest = interestStockService.isInterestStock(userId, stockId);      
        log.info("관심 여부 확인 완료 - 사용자: {}, 종목: {}, 관심여부: {}", userId, stockId, isInterest);
        return isInterest;
    }

    /**
     * 특정 섹터의 관심종목 조회 
     */
    public List<InterestStockResponse> getMyInterestStocksBySector(String userId, Long sectorId) {

        log.info("섹터별 관심종목 조회 요청 - 사용자: {}, 섹터: {}", userId, sectorId);      
        List<InterestStock> interestStocks = interestStockService.getInterestStocksByUserIdAndSectorId(userId, sectorId);
        List<InterestStockResponse> responses = interestStocks.stream()
                .map(stockMapper::toInterestStockResponse)
                .collect(Collectors.toList());       
        log.info("섹터별 관심종목 조회 완료 - 사용자: {}, 섹터: {}, 건수: {}", userId, sectorId, responses.size());
        return responses;
    }

    /**
     * 내 관심종목 개수 조회 
     */
    public long getMyInterestStockCount(String userId) {

        log.info("관심종목 개수 조회 요청 - 사용자: {}", userId);      
        long count = interestStockService.getInterestStockCountByUserId(userId);       
        log.info("관심종목 개수 조회 완료 - 사용자: {}, 개수: {}", userId, count);
        return count;
    }

    /**
     * 관심종목 존재 여부 확인 
     */
    public boolean existsMyInterestStock(String userId, String stockId) {

        log.info("관심종목 존재 여부 확인 요청 - 사용자: {}, 종목: {}", userId, stockId);
        boolean exists = interestStockService.existsInterestStock(userId, stockId);      
        log.info("관심종목 존재 여부 확인 완료 - 사용자: {}, 종목: {}, 존재여부: {}", userId, stockId, exists);
        return exists;
    }
}
