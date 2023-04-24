package com.bank.dummy;

import com.bank.domain.user.User;
import com.bank.web.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DummyDevInit extends DummyObject {

    @Profile("dev")
    @Bean
    CommandLineRunner init(UserRepository userRepository) {
        return (args -> {
            User ssar = userRepository.save(newUser("ssar", "쌀"));
        });
    }

}
