package com.youthfi.finance.domain.portfolio.application.mapper;

import com.youthfi.finance.domain.portfolio.application.dto.response.InvestmentProfileResponse;
import com.youthfi.finance.domain.portfolio.application.dto.response.PortfolioResponse;
import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfile;
import com.youthfi.finance.domain.portfolio.domain.entity.Portfolio;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PortfolioMapper {

    /**
     * Portfolio 엔터티를 PortfolioResponse DTO로 변환
     */
    public PortfolioResponse toPortfolioResponse(Portfolio portfolio) {
        if (portfolio == null) {
            return null;
        }
        
        return new PortfolioResponse(
                portfolio.getPortfolioId(),
                portfolio.getUser().getUserId(),
                List.of(),
                portfolio.getCreatedAt(),
                portfolio.getUpdatedAt()
        );
    }

    /**
     * InvestmentProfile 엔터티를 InvestmentProfileResponse DTO로 변환
     */
    public InvestmentProfileResponse toInvestmentProfileResponse(InvestmentProfile investmentProfile) {
        if (investmentProfile == null) {
            return null;
        }
        
        // 관심섹터 정보 변환
        List<String> interestedSectors = Optional.ofNullable(investmentProfile.getInvestmentProfileSectors())
                .orElse(Collections.emptyList())
                .stream()
                .map(ips -> ips.getSector().getSectorName())
                .collect(Collectors.toList());
        
        return new InvestmentProfileResponse(
                investmentProfile.getProfileId(),
                investmentProfile.getUser().getUserId(),
                investmentProfile.getInvestmentProfile(),
                investmentProfile.getAvailableAssets(),
                investmentProfile.getInvestmentGoal(),
                interestedSectors,
                investmentProfile.getCreatedAt(),
                investmentProfile.getUpdatedAt()
        );
    }
}
