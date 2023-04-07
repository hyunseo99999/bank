package com.bank.web.user.controller;

import com.bank.domain.user.User;
import com.bank.dummy.DummyObject;
import com.bank.web.user.dto.UserReqDto.JoinReqDto;
import com.bank.web.user.dto.UserRespDto.JoinRespDto;
import com.bank.web.user.repository.UserRepository;
import com.bank.web.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class UserControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;


    @BeforeEach
    void initSetting() {
        dataSetting();
    }

    private void dataSetting() {
      userRepository.save(newUser("ssar", "쌀"));
    }

    @Test
    @DisplayName("사용자 중복")
    void join_fail_test() throws Exception {
        // given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("ssar");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("ssar@nate.com");
        joinReqDto.setFullname("쌀");
        // when

        String requestBody = mapper.writeValueAsString(joinReqDto);

        ResultActions result = mvc.perform(post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));

        String responseBody = result.andReturn().getResponse().getContentAsString();
        int statusCode = result.andReturn().getResponse().getStatus();
        System.out.println("테스트 ==>" + responseBody);
        Assertions.assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST);
        // then
    }
}