package com.bank.web.account.service;

import com.bank.domain.account.Account;
import com.bank.domain.transaction.Transaction;
import com.bank.domain.user.User;
import com.bank.dummy.DummyObject;
import com.bank.exception.handler.ex.CustomApiException;
import com.bank.web.account.dto.AccountReqDto;
import com.bank.web.account.dto.AccountReqDto.AccountSaveReqDto;
import com.bank.web.account.dto.AccountRespDto;
import com.bank.web.account.dto.AccountRespDto.AccountDepositRespDto;
import com.bank.web.account.dto.AccountRespDto.AccountSaveRespDto;
import com.bank.web.account.dto.AccountRespDto.AccountTransferRespDto;
import com.bank.web.account.repository.AccountRepository;
import com.bank.web.transaction.repository.TransactionRepository;
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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.bank.web.account.dto.AccountReqDto.*;
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

    @Mock
    private TransactionRepository transactionRepository;

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

    @Test
    @DisplayName("계좌 이체")
    void accountDeposit() {
        AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();

        Long number = 1111L;
        Long amount = 100L;

        accountDepositReqDto.setNumber(number);
        accountDepositReqDto.setAmount(amount);
        accountDepositReqDto.setGubun("DEPOSIT");
        accountDepositReqDto.setTel("01012345678");

        // stub 1L
        User ssar = newMockUser(1L, "ssar", "쌀"); // 실행됨
        Account ssarAccount1 = newMockAccount(1L, 1111L, 1000L, ssar); // 실행됨 - ssarAccount1 -> 1000원
        when(accountRepository.findByNumber(any())).thenReturn(Optional.of(ssarAccount1)); // 실행안됨 -> service호출후 실행됨 ->
        // 1100원
        // stub 2 (스텁이 진행될 때 마다 연관된 객체는 새로 만들어서 주입하기 - 타이밍 때문에 꼬인다)
        Account ssarAccount2 = newMockAccount(1L, 1111L, 1000L, ssar); // 실행됨 - ssarAccount1 -> 1000원
        Transaction transaction = newMockDepositTransaction(1L, ssarAccount2);
        when(transactionRepository.save(any())).thenReturn(transaction); // 실행안됨

        // when
        AccountDepositRespDto accountDepositRespDto = accountService.insertAccount(accountDepositReqDto);
        System.out.println("테스트 : 트랜잭션 입금계좌 잔액 : " + accountDepositRespDto.getTransaction().getDepositAccountBalance());
        System.out.println("테스트 : 계좌쪽 잔액 : " + ssarAccount1.getBalance());
        System.out.println("테스트 : 계좌쪽 잔액 : " + ssarAccount2.getBalance());

        // then
        assertThat(ssarAccount1.getBalance()).isEqualTo(1100L);
        assertThat(accountDepositRespDto.getTransaction().getDepositAccountBalance()).isEqualTo(1100L);
    }

    @Test
    public void 계좌이체_test() throws Exception {
        Long userId = 1L;
        AccountTransferReqDto accountTransferReqDto = new AccountTransferReqDto();
        accountTransferReqDto.setWithdrawNumber(1111L);
        accountTransferReqDto.setDepositNumber(2222L);
        accountTransferReqDto.setWithdrawPassword(1234L);
        accountTransferReqDto.setAmount(100L);
        accountTransferReqDto.setGubun("TRANSFER");

        User ssar = newMockUser(1L, "ssar", "쌀");
        User cost = newMockUser(1L, "cost", "코스트");
        Account withdrawAccount = newMockAccount(1L, 1111L, 1000L, ssar);
        Account depositAccount = newMockAccount(2L, 2222L, 1000L, cost);

        // 출금계좌와 입금계좌가 동일하면 안됨
        if (accountTransferReqDto.getWithdrawNumber().longValue() == accountTransferReqDto.getDepositNumber().longValue()) {
            throw new CustomApiException("입출금계좌가 동일할 수 없습니다.");
        }


        if (accountTransferReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이하는 금액을 출금할 수 없습니다.");
        }

        // 출금 소유자 확인(로그인한 사람과 동일한지)
        withdrawAccount.checkOwner(userId);

        // 출금계좌 비밀번호 확인
        withdrawAccount.checkSamePassword(accountTransferReqDto.getWithdrawPassword());

        // 출금계좌 잔액 확인
        withdrawAccount.checkBalance(accountTransferReqDto.getAmount());

        // 이체하기
        withdrawAccount.withdraw(accountTransferReqDto.getAmount());
        depositAccount.deposit(accountTransferReqDto.getAmount());

        assertThat(withdrawAccount.getBalance()).isEqualTo(900L);
        assertThat(depositAccount.getBalance()).isEqualTo(1100L);

    }
}