package com.bank.dummy;

import com.bank.domain.account.Account;
import com.bank.domain.user.User;
import com.bank.domain.user.UserEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class DummyObject {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User newUser(String username, String fullname) {
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode("1234"))
                .email(fullname + "@nate.com")
                .fullName(fullname)
                .role(UserEnum.CUSTOMER)
                .build();

    }

    public User newMockUser(Long id, String username, String fullname) {
        return User.builder()
                .id(id)
                .username(username)
                .password("1234")
                .email(fullname + "@nate.com")
                .fullName(fullname)
                .role(UserEnum.CUSTOMER)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
    }

    public Account newAccount(Long number, User user) {
        return Account.builder()
                .number(number)
                .password(1234L)
                .balance(1000L)
                .user(user)
                .build();
    }

    public Account newMockAccount(Long id, Long number, Long balance, User user) {
        return Account.builder()
                .id(id)
                .number(number)
                .password(1234L)
                .balance(balance)
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

}
