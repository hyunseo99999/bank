package com.bank.web.account.service;

import com.bank.domain.account.Account;
import com.bank.domain.user.User;
import com.bank.exception.handler.ex.CustomApiException;
import com.bank.web.account.dto.AccountReqDto;
import com.bank.web.account.dto.AccountReqDto.AccountSaveReqDto;
import com.bank.web.account.dto.AccountRespDto;
import com.bank.web.account.dto.AccountRespDto.AccountSaveRespDto;
import com.bank.web.account.repository.AccountRepository;
import com.bank.web.user.repository.UserRepository;
import com.bank.web.user.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

}
