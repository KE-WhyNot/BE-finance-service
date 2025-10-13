package com.youthfi.finance.domain.user.domain.service;

import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.domain.user.domain.repository.UserRepository;
import com.youthfi.finance.global.exception.UserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 비즈니스 로직 테스트")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("사용자 생성 성공")
    void createUser_Success() {
        // given
        String userId = "test_user";

        // when
        User result = userService.createUser(userId);

        // then
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getBalance()).isEqualTo(new BigDecimal("10000000"));
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
        userService.subtractBalance(user, amount);

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
        assertThatThrownBy(() -> userService.subtractBalance(user, amount))
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
        assertThatThrownBy(() -> userService.subtractBalance(user, amount))
                .isInstanceOf(UserException.class)
                .hasMessageContaining("유효하지 않은 금액입니다");
    }

    @Test
    @DisplayName("잔고 차감 실패 - null 금액")
    void subtractBalance_Fail_NullAmount() {
        // given
        User user = User.builder()
                .userId("test_user")
                .balance(new BigDecimal("10000000"))
                .build();
        BigDecimal amount = null;

        // when & then
        assertThatThrownBy(() -> userService.subtractBalance(user, amount))
                .isInstanceOf(UserException.class)
                .hasMessageContaining("유효하지 않은 금액입니다");
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
        userService.addBalance(user, amount);

        // then
        assertThat(user.getBalance()).isEqualTo(new BigDecimal("15000000"));
    }

    @Test
    @DisplayName("잔고 증가 실패 - 음수 금액")
    void addBalance_Fail_NegativeAmount() {
        // given
        User user = User.builder()
                .userId("test_user")
                .balance(new BigDecimal("10000000"))
                .build();
        BigDecimal amount = new BigDecimal("-1000000");

        // when & then
        assertThatThrownBy(() -> userService.addBalance(user, amount))
                .isInstanceOf(UserException.class)
                .hasMessageContaining("유효하지 않은 금액입니다");
    }

    @Test
    @DisplayName("잔고 증가 실패 - null 금액")
    void addBalance_Fail_NullAmount() {
        // given
        User user = User.builder()
                .userId("test_user")
                .balance(new BigDecimal("10000000"))
                .build();
        BigDecimal amount = null;

        // when & then
        assertThatThrownBy(() -> userService.addBalance(user, amount))
                .isInstanceOf(UserException.class)
                .hasMessageContaining("유효하지 않은 금액입니다");
    }

    @Test
    @DisplayName("사용자 조회 성공")
    void getUserById_Success() {
        // given
        String userId = "test_user";
        User expectedUser = User.builder()
                .userId(userId)
                .balance(new BigDecimal("10000000"))
                .build();
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        // when
        Optional<User> result = userService.getUserById(userId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUserId()).isEqualTo(userId);
        assertThat(result.get().getBalance()).isEqualTo(new BigDecimal("10000000"));
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("사용자 조회 실패 - 사용자 없음")
    void getUserById_NotFound() {
        // given
        String userId = "nonexistent_user";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when
        Optional<User> result = userService.getUserById(userId);

        // then
        assertThat(result).isEmpty();
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("사용자 존재 여부 확인 - 존재함")
    void existsUser_Exists() {
        // given
        String userId = "test_user";
        when(userRepository.existsById(userId)).thenReturn(true);

        // when
        boolean result = userService.existsUser(userId);

        // then
        assertThat(result).isTrue();
        verify(userRepository).existsById(userId);
    }

    @Test
    @DisplayName("사용자 존재 여부 확인 - 존재하지 않음")
    void existsUser_NotExists() {
        // given
        String userId = "nonexistent_user";
        when(userRepository.existsById(userId)).thenReturn(false);

        // when
        boolean result = userService.existsUser(userId);

        // then
        assertThat(result).isFalse();
        verify(userRepository).existsById(userId);
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
        userService.subtractBalance(user, amount);

        // then
        assertThat(user.getBalance()).isEqualTo(BigDecimal.ZERO);
    }

}
