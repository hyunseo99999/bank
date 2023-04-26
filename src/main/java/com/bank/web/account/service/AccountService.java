package com.bank.web.account.service;

import com.bank.domain.account.Account;
import com.bank.domain.user.User;
import com.bank.exception.handler.ex.CustomApiException;
import com.bank.web.account.dto.AccountReqDto.AccountSaveReqDto;
import com.bank.web.account.dto.AccountRespDto.AccountSaveRespDto;
import com.bank.web.account.repository.AccountRepository;
import com.bank.web.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Getter @Setter
    public static class AccountListRespDto {
        private String fullName;
        private List<AccountDto> accounts = new ArrayList<>();

        public AccountListRespDto(User user, List<Account> accounts) {
            this.fullName = user.getFullName();
            this.accounts = accounts.stream().map(AccountDto::new).collect(Collectors.toList());
        }

        @Getter @Setter
        public class AccountDto {
            private Long id;
            private Long number;
            private Long balance;

            public AccountDto(Account account) {
                this.id = account.getId();
                this.number = account.getNumber();
                this.balance = account.getBalance();
            }
        }

    }

}
