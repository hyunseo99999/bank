package com.bank.web.account.dto;

import com.bank.domain.account.Account;
import com.bank.domain.transaction.Transaction;
import com.bank.domain.user.User;
import com.bank.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountRespDto {

    @Getter @Setter
    public static class AccountSaveRespDto {
        private Long id;
        private Long number;
        private Long balance;

        public AccountSaveRespDto(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
        }
    }

    @Getter @Setter
    public static class AccountListRespDto {
        private String fullName;
        private List<AccountDto> accounts = new ArrayList<>();

        public AccountListRespDto(User user, List<Account> accounts) {
            this.fullName = user.getFullName();
            this.accounts = accounts.stream().map(AccountDto::new).collect(Collectors.toList());
        }

        @Getter @Setter
        public class AccountDto {
            private Long id;
            private Long number;
            private Long balance;

            public AccountDto(Account account) {
                this.id = account.getId();
                this.number = account.getNumber();
                this.balance = account.getBalance();
            }
        }
    }

    @Getter @Setter
    public static class AccountDepositRespDto {
        private Long id;
        private Long number;
        private TransactionDto transaction;

        public AccountDepositRespDto(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.transaction = new TransactionDto(transaction);
        }
        @Getter
        @Setter
        public class TransactionDto {
            private Long id;
            private String gubun; // 입금
            private String sender; // ATM
            private String receiver;
            private Long amount;
            @JsonIgnore
            private Long depositAccountBalance;
            private String tel;
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.depositAccountBalance = transaction.getDepositAccountBalance();
                this.tel = transaction.getTel();
                this.createdAt = DateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

    @Getter @Setter
    public static class AccountWithdrawRespDto {
        private Long id;
        private Long number;
        private Long balance;
        private TransactionDto transaction;

        public AccountWithdrawRespDto(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transaction = new TransactionDto(transaction);
        }

        @Getter
        @Setter
        public class TransactionDto {
            private Long id;
            private String gubun; // 입금
            private String sender; // ATM
            private String receiver;
            private Long amount;

            private String createdAt;
            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.createdAt = DateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

    @Getter @Setter
    public static class AccountTransferRespDto {
        private Long id;
        private Long number;
        private Long balance;
        private TransactionDto transaction;

        public AccountTransferRespDto(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transaction = new TransactionDto(transaction);
        }

        @Getter
        @Setter
        public class TransactionDto {
            private Long id;
            private String gubun; // 입금
            private String sender; // ATM
            private String receiver;
            private Long amount;
            @JsonIgnore
            private Long depositAccountBalance;
            private String createdAt;
            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.depositAccountBalance = transaction.getDepositAccountBalance();
                this.createdAt = DateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }
}
