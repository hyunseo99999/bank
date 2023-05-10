package com.bank.web.account.service;

import com.bank.domain.account.Account;
import com.bank.domain.transaction.Transaction;
import com.bank.domain.user.User;
import com.bank.exception.handler.ex.CustomApiException;
import com.bank.util.DateUtil;
import com.bank.web.account.dto.AccountReqDto.AccountSaveReqDto;
import com.bank.web.account.dto.AccountRespDto;
import com.bank.web.account.dto.AccountRespDto.AccountListRespDto;
import com.bank.web.account.dto.AccountRespDto.AccountSaveRespDto;
import com.bank.web.account.repository.AccountRepository;
import com.bank.web.transaction.repository.TransactionRepository;
import com.bank.web.user.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    @Transactional
    public AccountSaveRespDto save(AccountSaveReqDto accountSaveReqDto, Long userId) {
        // 회원 정보 체크
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다"));
        //해당 계좌가 DB에 존재하는지 체크
        Optional<Account> findAccount = accountRepository.findByNumber(accountSaveReqDto.getNumber());
        if (findAccount.isPresent()) {
            throw new CustomApiException("해당 계좌가 이미 존재합니다");
        }

        Account account = accountRepository.save(accountSaveReqDto.toEntity(user));
        return new AccountSaveRespDto(account);
    }

    public AccountListRespDto findAccountByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다"));

        List<Account> accountList = accountRepository.findByUserId(userId);
        return new AccountListRespDto(user, accountList);
    }

    @Transactional
    public void deleteByAccountAndUser(Long number, Long userId) {
        Account findAccount = accountRepository.findByNumber(number).orElseThrow(
                () -> new CustomApiException("계좌를 찾을 수 없습니다.")
        );

        findAccount.checkOwner(userId);

        accountRepository.deleteById(findAccount.getId());
    }

    /**
     * 계좌 입금
     */
    @Transactional
    public void insertAccount(AccountDepositReqDto accountDepositReqDto) {
        // 0원 이상만 입금 가능
        if (accountDepositReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이하는 입금할 수 없습니다.");
        }

        // 계좌가 있는지 확인
        Account findAccount = accountRepository.findByNumber(accountDepositReqDto.getNumber()).orElseThrow(
                () -> new CustomApiException("계좌를 찾을 수 없습니다.")
        );

        // 입금 (해당 계좌 balance 조정 - update문 - 더티체크)
        findAccount.depoist(accountDepositReqDto.getAmount());

        Transaction transaction = Transaction
                .builder()
                .depositAccount(findAccount)
                .withdrawAccount(null)
                .depositAccountBalance(findAccount.getBalance())
                .withdrawAccountBalance(null)
                .amount(accountDepositReqDto.getAmount())
                .sender("ATM")
                .receiver(findAccount.getNumber() + "")
                .tel(accountDepositReqDto.getTel())
                .build();

        Transaction resultTransaction = transactionRepository.save(transaction);




    }

    @Getter @Setter
    public static class AccountDepositRespDto {
        private Long id;
        private Long number;
        private TransactionDto transaction;

        public AccountDepositRespDto(Account account, Transaction transaction) {
          this.id = account.getId();
          this.number = account.getNumber();
          this.transaction = new TransactionDto(transaction);
      }
        @Getter
        @Setter
        public class TransactionDto {
             private Long id;
             private String gubun; // 입금
             private String sender; // ATM
             private String receiver;
             private Long amount;
             @JsonIgnore
             private Long depositAccountBalance;
             private String tel;
             private String createdAt;

             public TransactionDto(Transaction transaction) {
                 this.id = transaction.getId();
                 this.gubun = transaction.getGubun().getValue();
                 this.sender = transaction.getSender();
                 this.receiver = transaction.getReceiver();
                 this.amount = transaction.getAmount();
                 this.depositAccountBalance = transaction.getDepositAccountBalance();
                 this.tel = transaction.getTel();
                 this.createdAt = DateUtil.toStringFormat(transaction.getCreatedAt());
             }
         }
    }

    @Getter @Setter
    public static class AccountDepositReqDto {
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long number;

        @NotNull
        private Long amount;

        @NotEmpty
        @Pattern(regexp = "^(DEPOSIT)$")
        private String gubun;

        @NotEmpty
        @Pattern(regexp = "^[0-9]{3}[0-9]{4}[0-9]{4}")
        private String tel;
    }




}
