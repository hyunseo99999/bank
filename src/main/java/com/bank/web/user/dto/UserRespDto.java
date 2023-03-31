package com.bank.web.user.dto;

import com.bank.domain.user.User;
import lombok.Getter;
import lombok.Setter;

public class UserRespDto {
    @Setter
    @Getter
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
