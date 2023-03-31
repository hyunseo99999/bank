package com.bank.web.user.service;

import com.bank.domain.user.User;
import com.bank.exception.handler.ex.CustomApiException;
import com.bank.web.user.dto.UserReqDto.JoinReqDto;
import com.bank.web.user.dto.UserRespDto;
import com.bank.web.user.dto.UserRespDto.JoinRespDto;
import com.bank.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 회원가입
     *
     * @return
     */
    @Transactional
    public JoinRespDto signUp(JoinReqDto userReqDto) {
        // 1. 동일 유저 존재 검사
        Optional<User> userOP = userRepository.findByUsername(userReqDto.getUsername());
        if (userOP.isPresent()) {
            throw new CustomApiException("동일한 username이 존재합니다.");
        }

        // 패스워드 인코딩 + DB 저장

        userReqDto.setPassword(passwordEncoder.encode(userReqDto.getPassword()));
        User user = userRepository.save(userReqDto.toEntity());
        return new JoinRespDto(user);
    }
}
