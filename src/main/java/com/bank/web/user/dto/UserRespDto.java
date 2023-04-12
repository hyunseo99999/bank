package com.bank.web.user.dto;

import com.bank.domain.user.User;
import com.bank.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class UserRespDto {

    @Getter @Setter
    public static class LoginRespDto {
        private Long id;
        private String username;
        private String createAt;

        public LoginRespDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.createAt = DateUtil.toStringFormat(user.getCreateAt());
        }
    }

    @Setter
    @Getter
    @ToString
    public static class JoinRespDto {
        private Long id;
        private String username;
        private String fullname;

        public JoinRespDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.fullname = user.getFullName();
        }
    }
}
