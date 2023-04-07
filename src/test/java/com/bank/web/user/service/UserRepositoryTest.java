package com.bank.web.user.service;

import com.bank.domain.user.User;
import com.bank.dummy.DummyObject;
import com.bank.web.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest // @Transactional 이 포함되어 있어서 메서드마다 자동 Rollback 됨.
public class UserRepositoryTest extends DummyObject {

    @Autowired
    private EntityManager em;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
       dataSetting();
    }
    private void dataSetting() {
        userRepository.save(newUser("ssar", "쌀"));
    }

    @Test
    public void findByUsername_test() throws Exception {
        // given
        String username = "ssar";

        // when
        Optional<User> userOP = userRepository.findByUsername(username);

        // then
        assertTrue(userOP.isPresent());
        assertThat(userOP.get().getUsername()).isEqualTo("ssar");
    }
}
