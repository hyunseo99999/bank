package com.bank.core.jwt;

import com.auth0.jwt.JWT;
import com.bank.core.auth.LoginUser;
import com.bank.domain.user.User;
import com.bank.domain.user.UserEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JwtProcessTest {

    @Test
    @DisplayName("토큰 생성")
    void jwtCreate_test() {
        User user = User
                .builder()
                .id(1L)
                .role(UserEnum.CUSTOMER)
                .build();

        LoginUser loginUser = new LoginUser(user);
        String jwtToken = JwtProcess.create(loginUser);
        System.out.println("테스트 =>" + jwtToken);
        assertThat(jwtToken.startsWith(JwtVO.TOKEN_PREFIX)).isTrue();
    }

    @Test
    @DisplayName("토큰 검증")
    void jwtVerify_test() {
        String jwtToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYW5rIiwicm9sZSI6IkNVU1RPTUVSIiwiaWQiOjEsImV4cCI6MTY4MjMzNjgxOX0.LGkhXitgghy7pu6DPU0mmv9Pd2jS3lVGhzYNPY19Ik9dXwrcX80NRRVNqHcy_EYG47v2_vi3N4ttmGhDaMCukg";
        LoginUser loginUser = JwtProcess.verify(jwtToken);
        assertThat(loginUser.getUser().getId()).isEqualTo(1L);
        assertThat(loginUser.getUser().getRole()).isEqualTo(UserEnum.CUSTOMER);

    }


}