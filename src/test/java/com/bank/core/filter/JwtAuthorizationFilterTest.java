package com.bank.core.filter;

import com.bank.core.auth.LoginUser;
import com.bank.core.jwt.JwtProcess;
import com.bank.core.jwt.JwtVO;
import com.bank.domain.user.User;
import com.bank.domain.user.UserEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthorizationFilterTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void sucess_test() throws Exception {
        User user = User
                .builder()
                .username("ssar")
                .password("1234")
                .fullName("쌀")
                .role(UserEnum.CUSTOMER)
                .build();

        LoginUser loginUser = new LoginUser(user);
        String jwtToken = JwtProcess.create(loginUser);

        ResultActions perform = mvc.perform(get("/api/s/hello").header(JwtVO.HEADER, jwtToken).contentType(MediaType.APPLICATION_JSON));
        perform.andExpect(status().isOk());
    }

    @Test
    void forbidden_test() throws Exception {
       User user = User
               .builder()
               .username("ssar")
               .password("1234")
               .fullName("쌀")
               .role(UserEnum.CUSTOMER)
               .build();

       LoginUser loginUser = new LoginUser(user);
       String jwtToken = JwtProcess.create(loginUser);

       ResultActions perform = mvc.perform(get("/api/admin/hello").header(JwtVO.HEADER, jwtToken).contentType(MediaType.APPLICATION_JSON));
       perform.andExpect(status().isForbidden());
    }
}