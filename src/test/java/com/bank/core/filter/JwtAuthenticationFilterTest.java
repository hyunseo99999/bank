package com.bank.core.filter;

import com.bank.core.jwt.JwtVO;
import com.bank.dummy.DummyObject;
import com.bank.web.user.dto.UserReqDto.LoginReqDto;
import com.bank.web.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthenticationFilterTest extends DummyObject {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void dataSetting() {
        userRepository.save(newUser("ssar", "쌀"));
    }

    @Test
    @DisplayName("로그인 성공")
    void successfulAuthentication_test() throws Exception {
        LoginReqDto loginReqDto = new LoginReqDto();
        loginReqDto.setUsername("ssar");
        loginReqDto.setPassword("1234");

        String requestBody = mapper.writeValueAsString(loginReqDto);
        ResultActions resultActions = mvc.perform(post("/api/login").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtVO.HEADER);

        System.out.println("ResponseBody =>" + responseBody);
        System.out.println("jwtToken =>" + jwtToken);

        resultActions.andExpect(status().isOk());
        assertNotNull(jwtToken);
        assertTrue(jwtToken.startsWith(JwtVO.TOKEN_PREFIX));
        resultActions.andExpect(jsonPath("$.data.username").value("ssar"));
    }

    @Test
    @DisplayName("로그인 실패")
    void unsuccessfulAuthentication_test() throws Exception {
        LoginReqDto loginReqDto = new LoginReqDto();
        loginReqDto.setUsername("ssar1");
        loginReqDto.setPassword("1234");

        String requestBody = mapper.writeValueAsString(loginReqDto);
        ResultActions resultActions = mvc.perform(post("/api/login").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();

        System.out.println("ResponseBody =>" + responseBody);
        resultActions.andExpect(status().isUnauthorized());
    }

}