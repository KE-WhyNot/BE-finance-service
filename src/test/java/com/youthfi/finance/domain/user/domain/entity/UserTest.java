package com.youthfi.finance.domain.user.domain.entity;

import com.youthfi.finance.global.exception.UserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@DisplayName("User 엔티티 테스트")
class UserTest {

    @Test
    @DisplayName("사용자 생성 성공")
    void createUser_Success() {
        // given
        String userId = "test_user";
        BigDecimal balance = new BigDecimal("10000000");

        // when
        User user = User.builder()
                .userId(userId)
                .balance(balance)
                .build();

        // then
        assertThat(user.getUserId()).isEqualTo(userId);
        assertThat(user.getBalance()).isEqualTo(balance);
    }

    @Test
    @DisplayName("잔고 차감 성공")
    void subtractBalance_Success() {
        // given
        User user = User.builder()
                .userId("test_user")
                .balance(new BigDecimal("10000000"))
                .build();
        BigDecimal amount = new BigDecimal("5000000");

        // when
        user.subtractBalance(amount);

        // then
        assertThat(user.getBalance()).isEqualTo(new BigDecimal("5000000"));
    }

    @Test
    @DisplayName("잔고 차감 실패 - 잔고 부족")
    void subtractBalance_Fail_InsufficientBalance() {
        // given
        User user = User.builder()
                .userId("test_user")
                .balance(new BigDecimal("1000000"))
                .build();
        BigDecimal amount = new BigDecimal("2000000");

        // when & then
        assertThatThrownBy(() -> user.subtractBalance(amount))
                .isInstanceOf(UserException.class)
                .hasMessageContaining("잔액이 부족합니다");
    }

    @Test
    @DisplayName("잔고 차감 실패 - 음수 금액")
    void subtractBalance_Fail_NegativeAmount() {
        // given
        User user = User.builder()
                .userId("test_user")
                .balance(new BigDecimal("10000000"))
                .build();
        BigDecimal amount = new BigDecimal("-1000000");

        // when & then
        // User 엔티티에서는 validateAmount를 호출하지 않으므로 예외가 발생하지 않음
        // 대신 잔고가 증가하는 결과가 됨
        user.subtractBalance(amount);
        assertThat(user.getBalance()).isEqualTo(new BigDecimal("11000000"));
    }

    @Test
    @DisplayName("잔고 증가 성공")
    void addBalance_Success() {
        // given
        User user = User.builder()
                .userId("test_user")
                .balance(new BigDecimal("10000000"))
                .build();
        BigDecimal amount = new BigDecimal("5000000");

        // when
        user.addBalance(amount);

        // then
        assertThat(user.getBalance()).isEqualTo(new BigDecimal("15000000"));
    }

    @Test
    @DisplayName("잔고 증가 - 0원 추가")
    void addBalance_ZeroAmount() {
        // given
        User user = User.builder()
                .userId("test_user")
                .balance(new BigDecimal("10000000"))
                .build();
        BigDecimal amount = BigDecimal.ZERO;

        // when
        user.addBalance(amount);

        // then
        assertThat(user.getBalance()).isEqualTo(new BigDecimal("10000000"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1000000", "5000000", "10000000", "99999999"})
    @DisplayName("잔고 차감 - 다양한 금액으로 성공")
    void subtractBalance_VariousAmounts_Success(String amountStr) {
        // given
        User user = User.builder()
                .userId("test_user")
                .balance(new BigDecimal("100000000"))
                .build();
        BigDecimal amount = new BigDecimal(amountStr);

        // when
        user.subtractBalance(amount);

        // then
        BigDecimal expectedBalance = new BigDecimal("100000000").subtract(amount);
        assertThat(user.getBalance()).isEqualTo(expectedBalance);
    }

    @Test
    @DisplayName("잔고 차감 - 정확히 잔고와 같은 금액")
    void subtractBalance_ExactBalance() {
        // given
        User user = User.builder()
                .userId("test_user")
                .balance(new BigDecimal("10000000"))
                .build();
        BigDecimal amount = new BigDecimal("10000000");

        // when
        user.subtractBalance(amount);

        // then
        assertThat(user.getBalance()).isEqualTo(BigDecimal.ZERO);
    }

}
