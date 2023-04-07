package com.bank.web.user.dto;

import com.bank.domain.user.User;
import com.bank.domain.user.UserEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserReqDto {
    @Setter
    @Getter
    public static class JoinReqDto {
       @NotEmpty(message = "username은 필수입니다")
       @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자 2~20자 이내로 작성해주세요.")
       private String username;

       @NotEmpty(message = "password는 필수입니다")
       @Size(min = 4, max = 20) // 패스워드 인코딩 때문에
       private String password;

       @NotEmpty(message = "email은 필수입니다")
       @Size(min = 9, max = 20)
       @Pattern(regexp = "^[a-zA-Z0-9.]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$", message = "이메일 형식이 맞지 않습니다.")
       private String email;

       @NotEmpty(message = "fullname은 필수입니다")
       @Pattern(regexp = "^[a-zA-Z가-힣]{1,20}$", message = "한글/영문 1~20자 이내로 작성해주세요.")
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
