package com.bank.dummy;

import com.bank.domain.user.User;
import com.bank.domain.user.UserEnum;

import java.time.LocalDateTime;

public class DummyObject {
    public User newUser(String username, String fullname) {
        return User.builder()
                .username(username)
                .password("1234")
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

}
