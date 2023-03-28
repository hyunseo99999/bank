package com.bank.domain;

import com.bank.domain.user.User;
import com.bank.domain.user.UserEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
class UserTest {
    @Autowired
    EntityManager em;

    @Test
    @Transactional
    void save() {
        User user = User.builder()
                .username("admin")
                .password("1234")
                .fullName("fullUser")
                .role(UserEnum.ADMIN)
                .build();

        em.persist(user);

        em.clear();
        em.flush();

        Assertions.assertThat(user.getId()).isEqualTo(1);

    }
}