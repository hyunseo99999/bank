package com.bank.web.user.service;

import com.bank.domain.user.User;
import com.bank.dummy.DummyObject;
import com.bank.web.user.dto.UserReqDto.JoinReqDto;
import com.bank.web.user.dto.UserRespDto;
import com.bank.web.user.dto.UserRespDto.JoinRespDto;
import com.bank.web.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest extends DummyObject {

    @InjectMocks // 목 객체를 injection받을 객체
    private UserService userService;

    @Mock // 목 객체로 만들 객체
    private UserRepository userRepository;

    @Spy
    private BCryptPasswordEncoder passwordEncoder;


    @BeforeEach
    void initSetting() {
        dataSetting();
    }

    private void dataSetting() {
      userRepository.save(newUser("ssar", "쌀"));
    }

    @Test
    @DisplayName("회원가입 이름 중복 테스트")
    void signup_usernameDupli_test() {

        // given
        String username = "ssar";

        // when
        Optional<User> userOP = userRepository.findByUsername(username);

        // then
        assertTrue(userOP.isPresent());
        assertThat(userOP.get().getUsername()).isEqualTo("ssar");
    }

    @Test
    @DisplayName("회원가입 테스트")
    void signup_test() {

        // given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("ssar");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("ssar@nate.com");
        joinReqDto.setFullname("쌀");

        // stub1(가설)
        // when(userRepository.findByUsername(any())).thenReturn(Optional.empty()); // 중복 체크를 pass를 위해서
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User())); // 중복 체크를 pass를 위해서


        // stub2
        User saar = newMockUser(1L, "ssar", "쌀");
        when(userRepository.save(any())).thenReturn(saar);
        // when
        JoinRespDto joinRespDto = userService.signUp(joinReqDto);
        System.out.println("테스트 =>" + joinRespDto);
        // then
    }
}