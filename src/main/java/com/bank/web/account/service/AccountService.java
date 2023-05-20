package com.bank.web.account.service;

import com.bank.domain.account.Account;
import com.bank.domain.transaction.Transaction;
import com.bank.domain.transaction.TransactionEnum;
import com.bank.domain.user.User;
import com.bank.exception.handler.ex.CustomApiException;
import com.bank.util.DateUtil;
import com.bank.web.account.dto.AccountReqDto;
import com.bank.web.account.dto.AccountReqDto.AccountDepositReqDto;
import com.bank.web.account.dto.AccountReqDto.AccountSaveReqDto;
import com.bank.web.account.dto.AccountReqDto.AccountTransferReqDto;
import com.bank.web.account.dto.AccountReqDto.AccountWithdrawReqDto;
import com.bank.web.account.dto.AccountRespDto;
import com.bank.web.account.dto.AccountRespDto.*;
import com.bank.web.account.repository.AccountRepository;
import com.bank.web.transaction.repository.TransactionRepository;
import com.bank.web.user.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.*;
import java.util.List;
import java.util.Optional;

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
     *
     * @return
     */
    @Transactional
    public AccountDepositRespDto insertAccount(AccountDepositReqDto accountDepositReqDto) {
        // 0원 이상만 입금 가능
        if (accountDepositReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이하는 입금할 수 없습니다.");
        }

        // 계좌가 있는지 확인
        Account findAccount = accountRepository.findByNumber(accountDepositReqDto.getNumber()).orElseThrow(
                () -> new CustomApiException("계좌를 찾을 수 없습니다.")
        );

        // 입금 (해당 계좌 balance 조정 - update문 - 더티체크)
        findAccount.deposit(accountDepositReqDto.getAmount());

        Transaction transaction = Transaction
                .builder()
                .depositAccount(findAccount)
                .withdrawAccount(null)
                .depositAccountBalance(findAccount.getBalance())
                .withdrawAccountBalance(null)
                .amount(accountDepositReqDto.getAmount())
                .gubun(TransactionEnum.DEPOSIT)
                .sender("ATM")
                .receiver(findAccount.getNumber() + "")
                .tel(accountDepositReqDto.getTel())
                .build();

        Transaction resultTransaction = transactionRepository.save(transaction);
        return new AccountDepositRespDto(findAccount, resultTransaction);
    }

    @Transactional
    public AccountWithdrawRespDto accountWithDraw(AccountWithdrawReqDto accountWithdrawReqDto, Long userId) {
        if (accountWithdrawReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이하는 금액을 출금할 수 없습니다.");
        }

        // 출금 계좌 확인
        Account withdrawAccountPS = accountRepository.findByNumber(accountWithdrawReqDto.getNumber())
                .orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다."));


        // 출금 소유자 확인(로그인한 사람과 동일한지)
        withdrawAccountPS.checkOwner(userId);

        // 출금계좌 비밀번호 확인
        withdrawAccountPS.checkSamePassword(accountWithdrawReqDto.getPassword());
        // 출금계좌 잔액 확인
        withdrawAccountPS.checkBalance(accountWithdrawReqDto.getAmount());
        // 출금하기
        withdrawAccountPS.withdraw(accountWithdrawReqDto.getAmount());
        // 거래내역 남기기
        Transaction transaction = Transaction.builder()
                .withdrawAccount(withdrawAccountPS)
                .depositAccount(null)
                .withdrawAccountBalance(withdrawAccountPS.getBalance())
                .depositAccountBalance(null)
                .amount(accountWithdrawReqDto.getAmount())
                .gubun(TransactionEnum.WITHDRAW)
                .sender(accountWithdrawReqDto.getNumber() + "")
                .receiver("ATM")
                .build();
        transactionRepository.save(transaction);
        // DTO 응답하기
        return new AccountWithdrawRespDto(withdrawAccountPS, transaction);
    }

    @Transactional
    public AccountTransferRespDto 계좌이체(AccountTransferReqDto accountTransferReqDto, Long userId) {

        // 출금계좌와 입금계좌가 동일하면 안됨
        if (accountTransferReqDto.getWithdrawNumber().longValue() == accountTransferReqDto.getDepositNumber().longValue()) {
            throw new CustomApiException("입출금계좌가 동일할 수 없습니다.");
        }


        if (accountTransferReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이하는 금액을 출금할 수 없습니다.");
        }

        // 출금 계좌 확인
        Account withdrawAccountPS = accountRepository.findByNumber(accountTransferReqDto.getWithdrawNumber())
                .orElseThrow(() -> new CustomApiException("출금계좌를 찾을 수 없습니다."));

        // 입금계좌 확인
        Account depositAccountPS = accountRepository.findByNumber(accountTransferReqDto.getDepositNumber())
                .orElseThrow(() -> new CustomApiException("입금계좌를 찾을 수 없습니다."));


        // 출금 소유자 확인(로그인한 사람과 동일한지)
        withdrawAccountPS.checkOwner(userId);

        // 출금계좌 비밀번호 확인
        withdrawAccountPS.checkSamePassword(accountTransferReqDto.getWithdrawPassword());

        // 출금계좌 잔액 확인
        withdrawAccountPS.checkBalance(accountTransferReqDto.getAmount());

        // 이체하기
        withdrawAccountPS.withdraw(accountTransferReqDto.getAmount());
        withdrawAccountPS.deposit(accountTransferReqDto.getAmount());

        // 거래내역 남기기
        Transaction transaction = Transaction.builder()
                .withdrawAccount(withdrawAccountPS)
                .depositAccount(depositAccountPS)
                .withdrawAccountBalance(withdrawAccountPS.getBalance())
                .depositAccountBalance(depositAccountPS.getBalance())
                .amount(accountTransferReqDto.getAmount())
                .gubun(TransactionEnum.TRANSFER)
                .sender(accountTransferReqDto.getWithdrawNumber() + "")
                .receiver(accountTransferReqDto.getDepositNumber() + "")
                .build();
        transactionRepository.save(transaction);
        // DTO 응답하기
        return new AccountTransferRespDto(withdrawAccountPS, transaction);
    }

}
