package com.youthfi.finance.domain.portfolio.domain.service;

import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfile;
import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfileSector;
import com.youthfi.finance.domain.stock.domain.entity.Sector;
import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.domain.portfolio.domain.repository.InvestmentProfileRepository;
import com.youthfi.finance.domain.portfolio.domain.repository.InvestmentProfileSectorRepository;
import com.youthfi.finance.domain.stock.domain.repository.SectorRepository;
import com.youthfi.finance.domain.user.domain.repository.UserRepository;
import com.youthfi.finance.global.exception.PortfolioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvestmentProfileService {

    private final InvestmentProfileRepository investmentProfileRepository;
    private final InvestmentProfileSectorRepository investmentProfileSectorRepository;
    private final SectorRepository sectorRepository;
    private final UserRepository userRepository;

    
    // 투자성향 프로필 생성
    public InvestmentProfile createInvestmentProfile(String userId,
                                                   InvestmentProfile.InvestmentProfileType investmentProfile,
                                                   BigDecimal availableAssets, 
                                                   InvestmentProfile.InvestmentGoal investmentGoal,
                                                   List<String> interestedSectorNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> PortfolioException.userNotFound(userId));

        PortfolioException.validateAvailableAssets(availableAssets);
        
        InvestmentProfile profile = InvestmentProfile.builder()
                .user(user)
                .investmentProfile(investmentProfile)
                .availableAssets(availableAssets)
                .investmentGoal(investmentGoal)
                .build();

        InvestmentProfile savedProfile = investmentProfileRepository.save(profile);
        
        // 관심섹터는 이렇게 따로 추가.
        if (interestedSectorNames != null && !interestedSectorNames.isEmpty()) {
            addInterestedSectors(savedProfile, interestedSectorNames);
        }
        
        return savedProfile;
    }


    // 사용자별 투자성향 프로필 조회
    public Optional<InvestmentProfile> getInvestmentProfileByUserId(String userId) {
        return investmentProfileRepository.findByUserUserId(userId);
    }
    
    // 투자성향 프로필 수정
    public InvestmentProfile updateInvestmentProfile(Long profileId, 
                                                   InvestmentProfile.InvestmentProfileType investmentProfile,
                                                   BigDecimal availableAssets, 
                                                   InvestmentProfile.InvestmentGoal investmentGoal,
                                                   List<String> interestedSectorNames) {
        InvestmentProfile profile = investmentProfileRepository.findById(profileId)
                .orElseThrow(() -> PortfolioException.investmentProfileNotFound());

        PortfolioException.validateAvailableAssets(availableAssets);

        profile.updateProfile(investmentProfile, availableAssets, investmentGoal);
        InvestmentProfile savedProfile = investmentProfileRepository.save(profile);
        
        // 관심섹터 처리
        if (interestedSectorNames != null && !interestedSectorNames.isEmpty()) {
            addInterestedSectors(savedProfile, interestedSectorNames);
        }
        
        return savedProfile;
    }

   

    // 사용자별 투자성향 프로필 존재여부 조회
    public boolean existsInvestmentProfile(String userId) {
        return investmentProfileRepository.findByUserUserId(userId).isPresent();
    }


    
    public void addInterestedSectors(InvestmentProfile investmentProfile, List<String> sectorNames) {
        if (sectorNames == null || sectorNames.isEmpty()) {
            return;
        }

        // 기존 관심섹터 삭제
        List<InvestmentProfileSector> existingSectors = investmentProfileSectorRepository.findByInvestmentProfile(investmentProfile);
        investmentProfileSectorRepository.deleteAll(existingSectors);

        // 새로운 관심섹터 추가
        for (String sectorName : sectorNames) {
            Sector sector = sectorRepository.findBySectorName(sectorName)
                    .orElseThrow(() -> PortfolioException.sectorNotFound(sectorName));

            InvestmentProfileSector investmentProfileSector = InvestmentProfileSector.builder()
                    .investmentProfile(investmentProfile)
                    .sector(sector)
                    .build();

            investmentProfileSectorRepository.save(investmentProfileSector);
        }
    }

}


