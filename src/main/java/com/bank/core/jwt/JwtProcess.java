package com.bank.core.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bank.core.auth.LoginUser;
import com.bank.domain.user.User;
import com.bank.domain.user.UserEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtProcess {

    public static String create(LoginUser loginUser) {
        String token = JWT.create()
                .withSubject("bank")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.EXPIRATION_TIME))
                .withClaim("id", loginUser.getUser().getId())
                .withClaim("role", loginUser.getUser().getRole() + "")
                .sign(Algorithm.HMAC512(JwtVO.SECRET));
        return JwtVO.TOKEN_PREFIX + token;
    }

    public static LoginUser verify(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtVO.SECRET)).build().verify(token);
        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();
        User user = User.builder()
                .id(id)
                .role(UserEnum.valueOf(role)).build();
        LoginUser loginUser = new LoginUser(user);
        return loginUser;
    }

}
