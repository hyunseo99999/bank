package com.bank.web.user.service;

import com.bank.domain.user.UserEnum;
import com.bank.web.user.dto.UserReqDto;
import com.bank.web.user.dto.UserReqDto.JoinReqDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("회원가입 테스트")
    @Rollback(value = false)
    void signup_test() {
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("test");
        joinReqDto.setFullname("fullTest");
        joinReqDto.setPassword("1234");
        userService.signUp(joinReqDto);
    }
}