package com.bank.web.user.dto;

import com.bank.domain.user.User;
import com.bank.domain.user.UserEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class UserReqDto {
    @Setter
    @Getter
    public static class JoinReqDto {
       @NotEmpty(message = "username은 필수입니다")
       @Pattern(regexp = "", message = "영문/")
       private String username;

       @NotEmpty(message = "password는 필수입니다")
       private String password;

       @NotEmpty(message = "email은 필수입니다")
       private String email;

       @NotEmpty(message = "fullname은 필수입니다")
       private String fullname;

       public User toEntity() {
           return User.builder()
                   .username(username)
                   .password(password)
                   .email(email)
                   .fullName(fullname)
                   .role(UserEnum.CUSTOMER)
                   .build();
       }
    }


}
