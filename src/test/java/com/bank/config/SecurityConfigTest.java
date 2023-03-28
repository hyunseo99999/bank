package com.bank.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class SecurityConfigTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void authentication_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/api/s/hello"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        int status = resultActions.andReturn().getResponse().getStatus();


        System.out.println("responseBody --> " + responseBody);
        System.out.println("status --> " + status);
        assertThat(status).isEqualTo(401);
        // then
    }
}