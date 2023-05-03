package com.bank.web.account.service;

import com.bank.domain.account.Account;
import com.bank.domain.user.User;
import com.bank.dummy.DummyObject;
import com.bank.exception.handler.ex.CustomApiException;
import com.bank.web.account.dto.AccountReqDto.AccountSaveReqDto;
import com.bank.web.account.dto.AccountRespDto.AccountSaveRespDto;
import com.bank.web.account.repository.AccountRepository;
import com.bank.web.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = MOCK)
class AccountServiceTest extends DummyObject {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Spy
    private ObjectMapper mapper;

    @Spy
    private EntityManager em;

    @Test
    @DisplayName("계좌 등록")
    void account_save_test() throws JsonProcessingException {
        Long userId = 1L;

        AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
        accountSaveReqDto.setNumber(1000L);
        accountSaveReqDto.setPassword(1234L);

        // stub1
        User ssar = newMockUser(userId, "ssar", "쌀");
        when(userRepository.findById(any())).thenReturn(Optional.of(ssar));

        // stub2
        when(accountRepository.findByNumber(any())).thenReturn(Optional.empty());

        // stub3
        Account ssarAcount = newMockAccount(1L, 1111L, 1000L, ssar);
        when(accountRepository.save(any())).thenReturn(ssarAcount);

        AccountSaveRespDto accountSaveRespDto = accountService.save(accountSaveReqDto, userId);
        String responseBody = mapper.writeValueAsString(accountSaveRespDto);
        System.out.println("responseBody --> " + responseBody);


        assertThat(ssarAcount.getNumber()).isEqualTo(1111L);
    }

    @Test
    @DisplayName("계좌 삭제")
    void account_delete_test() {
        Long accountNumber = 1111L;
        Long userId = 2L;

        // stub
        User ssar = newMockUser(1L, "ssar", "쌀");
        Account ssarAccount = newMockAccount(1L, 1111L, 1000L, ssar);
        when(accountRepository.findByNumber(accountNumber)).thenReturn(Optional.of(ssarAccount));

      // when then
        assertThrows(CustomApiException.class, () -> accountService.deleteByAccountAndUser(accountNumber, userId));

    }

    @Test
    public void account_delete_test2() throws Exception {
       // given
       Long accountNumber = 1111L;
       Long userId = 2L;

       // stub
       User ssar = newMockUser(1L, "ssar", "쌀");
       Account ssarAccount = newMockAccount(1L, 1111L, 1000L, ssar);
       when(accountRepository.findByNumber(accountNumber)).thenReturn(Optional.of(ssarAccount));

       // when
       try {
           accountService.deleteByAccountAndUser(accountNumber, userId);
       } catch (Exception e) {
           return;
       }

       // then
       fail("예외 발생 안함");
    }
}