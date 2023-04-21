package com.bank.web.account.service;

import com.bank.web.account.repository.AccountRepository;
import com.bank.web.user.repository.UserRepository;
import com.bank.web.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public void save() {
        // 회원 정보 체크

        //해당 계좌가 DB에 존재하는지 체크
    }

}
