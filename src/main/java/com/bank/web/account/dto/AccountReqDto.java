package com.bank.web.account.dto;

import com.bank.domain.account.Account;
import com.bank.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

public class AccountReqDto {

    @Getter @Setter
    public static class AccountSaveReqDto {

        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long number;

        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long password;

        public Account toEntity(User user) {
            return Account
                    .builder()
                    .number(number)
                    .password(password)
                    .balance(1000L)
                    .user(user)
                    .build();
        }
    }

    @Getter @Setter
    public static class AccountDepositReqDto {
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long number;

        @NotNull
        private Long amount;

        @NotEmpty
        @Pattern(regexp = "^(DEPOSIT)$")
        private String gubun;

        @NotEmpty
        @Pattern(regexp = "^[0-9]{3}[0-9]{4}[0-9]{4}")
        private String tel;
    }

    @Getter @Setter
    public static class AccountWithdrawReqDto {
       @NotNull
       @Digits(integer = 4, fraction = 4)
       private Long number;

       @NotNull
       @Digits(integer = 4, fraction = 4)
       private Long password;

       @NotNull
       private Long amount;

       @NotEmpty
       @Pattern(regexp = "^(DEPOSIT)$")
       private String gubun;
    }

    @Getter @Setter
    public static class AccountTransferReqDto {
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long withdrawNumber;

        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long depositNumber;

        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long withdrawPassword;

        @NotNull
        private Long amount;

        @NotEmpty
        @Pattern(regexp = "TRANSFER")
        private String gubun;
    }
}
