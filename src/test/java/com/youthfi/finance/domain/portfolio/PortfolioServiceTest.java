package com.youthfi.finance.domain.portfolio;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfile;
import com.youthfi.finance.domain.portfolio.domain.entity.Portfolio;
import com.youthfi.finance.domain.portfolio.domain.service.InvestmentProfileService;
import com.youthfi.finance.domain.portfolio.domain.service.PortfolioService;
import com.youthfi.finance.domain.stock.domain.entity.Sector;
import com.youthfi.finance.domain.stock.domain.entity.Stock;
import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.global.exception.PortfolioException;

@DataJpaTest
@Import({PortfolioService.class, InvestmentProfileService.class})
@ActiveProfiles("test")
@DisplayName("Portfolio 서비스 테스트")
class PortfolioServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private InvestmentProfileService investmentProfileService;

    private User testUser;
    private InvestmentProfile testProfile;
    private Sector testSector;

    @BeforeEach
    void setUp() {
        // 테스트 사용자 생성
        testUser = User.builder()
                .userId("test-user-123")
                .balance(new BigDecimal("10000000"))
                .build();
        entityManager.persistAndFlush(testUser);

        // 테스트 섹터 생성
        testSector = Sector.builder()
                .sectorName("전기·전자")
                .build();
        entityManager.persistAndFlush(testSector);

        // 테스트 투자성향 프로필 생성
        testProfile = InvestmentProfile.builder()
                .user(testUser)
                .investmentProfile(InvestmentProfile.InvestmentProfileType.CONSERVATIVE)
                .availableAssets(new BigDecimal("10000000"))
                .investmentGoal(InvestmentProfile.InvestmentGoal.ASSET_GROWTH)
                .lossTolerance(InvestmentProfile.LossTolerance.TEN_PERCENT)
                .financialKnowledge(InvestmentProfile.FinancialKnowledge.MEDIUM)
                .expectedProfit(InvestmentProfile.ExpectedProfit.TWO_HUNDRED_PERCENT)
                .build();
        entityManager.persistAndFlush(testProfile);
    }

    @Test
    @DisplayName("포트폴리오 생성 - 성공")
    void createPortfolio_Success() {
        // Given
        String portfolioName = "테스트 포트폴리오";
        BigDecimal highestValue = new BigDecimal("12000000");
        BigDecimal lowestValue = new BigDecimal("8000000");

        // When
        Portfolio portfolio = portfolioService.createPortfolio(
                testUser.getUserId(),
                portfolioName,
                highestValue,
                lowestValue
        );

        // Then
        assertThat(portfolio).isNotNull();
        assertThat(portfolio.getPortfolioName()).isEqualTo(portfolioName);
        assertThat(portfolio.getHighestValue()).isEqualTo(highestValue);
        assertThat(portfolio.getLowestValue()).isEqualTo(lowestValue);
        assertThat(portfolio.getUser().getUserId()).isEqualTo(testUser.getUserId());
    }

    @Test
    @DisplayName("포트폴리오 조회 - 성공")
    void getPortfolio_Success() {
        // Given
        Portfolio portfolio = portfolioService.createPortfolio(
                testUser.getUserId(),
                "조회 테스트 포트폴리오",
                new BigDecimal("10000000"),
                new BigDecimal("9000000")
        );

        // When
        List<Portfolio> portfolios = portfolioService.findPortfoliosByUserId(testUser.getUserId());

        // Then
        assertThat(portfolios).hasSize(1);
        assertThat(portfolios.get(0).getPortfolioId()).isEqualTo(portfolio.getPortfolioId());
        assertThat(portfolios.get(0).getPortfolioName()).isEqualTo("조회 테스트 포트폴리오");
    }

    @Test
    @DisplayName("포트폴리오 수정 - 성공")
    void updatePortfolio_Success() {
        // Given
        Portfolio portfolio = portfolioService.createPortfolio(
                testUser.getUserId(),
                "수정 전 포트폴리오",
                new BigDecimal("10000000"),
                new BigDecimal("9000000")
        );

        // When
        Portfolio updatedPortfolio = portfolioService.updatePortfolio(
                portfolio.getPortfolioId(),
                "수정 후 포트폴리오",
                new BigDecimal("15000000"),
                new BigDecimal("12000000")
        );

        // Then
        assertThat(updatedPortfolio.getPortfolioName()).isEqualTo("수정 후 포트폴리오");
        assertThat(updatedPortfolio.getHighestValue()).isEqualTo(new BigDecimal("15000000"));
        assertThat(updatedPortfolio.getLowestValue()).isEqualTo(new BigDecimal("12000000"));
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 포트폴리오 생성 - 실패")
    void createPortfolio_UserNotFound_Failure() {
        // When & Then
        assertThatThrownBy(() -> 
                portfolioService.createPortfolio(
                        "non-existent-user",
                        "테스트 포트폴리오",
                        new BigDecimal("10000000"),
                        new BigDecimal("9000000")
                ))
                .isInstanceOf(PortfolioException.class);
    }

    @Test
    @DisplayName("존재하지 않는 포트폴리오 수정 - 실패")
    void updatePortfolio_NotFound_Failure() {
        // When & Then
        assertThatThrownBy(() -> 
                portfolioService.updatePortfolio(
                        999L,
                        "수정 테스트",
                        new BigDecimal("10000000"),
                        new BigDecimal("9000000")
                ))
                .isInstanceOf(PortfolioException.class);
    }

    @Test
    @DisplayName("투자성향 프로필 생성 - 성공")
    void createInvestmentProfile_Success() {
        // Given
        User newUser = User.builder()
                .userId("test-user-456")
                .balance(new BigDecimal("15000000"))
                .build();
        entityManager.persistAndFlush(newUser);

        // When
        InvestmentProfile profile = investmentProfileService.createInvestmentProfile(
                newUser.getUserId(),
                InvestmentProfile.InvestmentProfileType.AGGRESSIVE,
                new BigDecimal("15000000"),
                InvestmentProfile.InvestmentGoal.HOUSE_PURCHASE,
                InvestmentProfile.LossTolerance.THIRTY_PERCENT,
                InvestmentProfile.FinancialKnowledge.HIGH,
                InvestmentProfile.ExpectedProfit.TWO_FIFTY_PERCENT,
                List.of("전기·전자") // 존재하는 섹터만 사용
        );

        // Then
        assertThat(profile).isNotNull();
        assertThat(profile.getInvestmentProfile()).isEqualTo(InvestmentProfile.InvestmentProfileType.AGGRESSIVE);
        assertThat(profile.getAvailableAssets()).isEqualTo(new BigDecimal("15000000"));
        assertThat(profile.getInvestmentGoal()).isEqualTo(InvestmentProfile.InvestmentGoal.HOUSE_PURCHASE);
    }

    @Test
    @DisplayName("투자성향 프로필 조회 - 성공")
    void getInvestmentProfile_Success() {
        // When
        InvestmentProfile profile = investmentProfileService.getInvestmentProfileByUserId(testUser.getUserId())
                .orElse(null);

        // Then
        assertThat(profile).isNotNull();
        assertThat(profile.getInvestmentProfile()).isEqualTo(InvestmentProfile.InvestmentProfileType.CONSERVATIVE);
        assertThat(profile.getAvailableAssets()).isEqualTo(new BigDecimal("10000000"));
        assertThat(profile.getUser().getUserId()).isEqualTo(testUser.getUserId());
    }

    @Test
    @DisplayName("투자성향 프로필 수정 - 성공")
    void updateInvestmentProfile_Success() {
        // When
        InvestmentProfile updatedProfile = investmentProfileService.updateInvestmentProfile(
                testProfile.getProfileId(),
                InvestmentProfile.InvestmentProfileType.RISK_NEUTRAL,
                new BigDecimal("20000000"),
                InvestmentProfile.InvestmentGoal.EDUCATION,
                InvestmentProfile.LossTolerance.FIFTY_PERCENT,
                InvestmentProfile.FinancialKnowledge.VERY_HIGH,
                InvestmentProfile.ExpectedProfit.THREE_HUNDRED_PERCENT_PLUS,
                List.of("전기·전자") // 존재하는 섹터만 사용
        );

        // Then
        assertThat(updatedProfile).isNotNull();
        assertThat(updatedProfile.getInvestmentProfile()).isEqualTo(InvestmentProfile.InvestmentProfileType.RISK_NEUTRAL);
        assertThat(updatedProfile.getAvailableAssets()).isEqualTo(new BigDecimal("20000000"));
        assertThat(updatedProfile.getInvestmentGoal()).isEqualTo(InvestmentProfile.InvestmentGoal.EDUCATION);
    }

    @Test
    @DisplayName("투자성향 프로필 존재 여부 확인 - 성공")
    void existsInvestmentProfile_Success() {
        // When
        boolean exists = investmentProfileService.existsInvestmentProfile(testUser.getUserId());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("투자성향 프로필 존재 여부 확인 - 실패")
    void existsInvestmentProfile_NotFound_Failure() {
        // Given - 새로운 사용자 (투자성향 프로필 없음)
        User newUser = User.builder()
                .userId("new-user-999")
                .balance(new BigDecimal("5000000"))
                .build();
        entityManager.persistAndFlush(newUser);

        // When
        boolean exists = investmentProfileService.existsInvestmentProfile(newUser.getUserId());

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("잘못된 투자성향 데이터로 프로필 생성 - 실패")
    void createInvestmentProfile_InvalidData_Failure() {
        // Given - 새로운 사용자
        User newUser = User.builder()
                .userId("test-user-789")
                .balance(new BigDecimal("5000000"))
                .build();
        entityManager.persistAndFlush(newUser);

        // When & Then - 음수 자산으로 프로필 생성 시도
        assertThatThrownBy(() -> 
                investmentProfileService.createInvestmentProfile(
                        newUser.getUserId(),
                        InvestmentProfile.InvestmentProfileType.CONSERVATIVE,
                        new BigDecimal("-1000000"), // 음수 자산
                        InvestmentProfile.InvestmentGoal.ASSET_GROWTH,
                        InvestmentProfile.LossTolerance.TEN_PERCENT,
                        InvestmentProfile.FinancialKnowledge.MEDIUM,
                        InvestmentProfile.ExpectedProfit.TWO_HUNDRED_PERCENT,
                        List.of("전기·전자")
                ))
                .isInstanceOf(PortfolioException.class);
    }

    @Test
    @DisplayName("포트폴리오가 없을 때 조회 - 빈 리스트")
    void getPortfolio_EmptyList_Success() {
        // Given - 새로운 사용자 (포트폴리오 없음)
        User newUser = User.builder()
                .userId("new-user-789")
                .balance(new BigDecimal("5000000"))
                .build();
        entityManager.persistAndFlush(newUser);

        // When
        List<Portfolio> portfolios = portfolioService.findPortfoliosByUserId(newUser.getUserId());

        // Then
        assertThat(portfolios).isEmpty();
    }

    @Test
    @DisplayName("여러 포트폴리오 조회 - 성공")
    void getMultiplePortfolios_Success() throws InterruptedException {
        // Given - 여러 포트폴리오 생성
        Portfolio firstPortfolio = portfolioService.createPortfolio(testUser.getUserId(), "첫 번째 포트폴리오", 
                new BigDecimal("10000000"), new BigDecimal("9000000"));
        
        // 잠시 대기하여 생성 시간 차이 만들기
        Thread.sleep(100);
        
        Portfolio secondPortfolio = portfolioService.createPortfolio(testUser.getUserId(), "두 번째 포트폴리오", 
                new BigDecimal("12000000"), new BigDecimal("10000000"));

        // When
        List<Portfolio> portfolios = portfolioService.findPortfoliosByUserId(testUser.getUserId());

        // Then
        assertThat(portfolios).hasSize(2);
        // 생성된 포트폴리오들이 포함되어 있는지 확인
        assertThat(portfolios).extracting("portfolioName")
                .containsExactlyInAnyOrder("첫 번째 포트폴리오", "두 번째 포트폴리오");
        assertThat(portfolios).extracting("portfolioId")
                .containsExactlyInAnyOrder(firstPortfolio.getPortfolioId(), secondPortfolio.getPortfolioId());
    }
}
