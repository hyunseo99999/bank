package com.bank.dummy;

import com.bank.domain.account.Account;
import com.bank.domain.user.User;
import com.bank.web.account.repository.AccountRepository;
import com.bank.web.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Transactional
public class DummyDevInit extends DummyObject {

    @Bean
    @Profile("dev")
    CommandLineRunner init(UserRepository userRepository, AccountRepository accountRepository) {
        return (args -> {
            // 서버 실행시에 무조건 실행된다.
            User ssar = userRepository.save(newUser("ssar", "쌀"));
            User cos = userRepository.save(newUser("cos", "코스,"));
            User love = userRepository.save(newUser("love", "러브"));
            User admin = userRepository.save(newUser("admin", "관리자"));

            Account ssarAccount1 = accountRepository.save(newAccount(1111L, ssar));
            Account cosAccount = accountRepository.save(newAccount(2222L, cos));
            Account loveAccount = accountRepository.save(newAccount(3333L, love));
            Account ssarAccount2 = accountRepository.save(newAccount(4444L, admin));
        });
    }

}
