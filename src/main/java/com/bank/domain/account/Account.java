package com.bank.domain.account;

import com.bank.domain.user.User;
import com.bank.exception.handler.ex.CustomApiException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private Long number; // 계좌번호

    @Column(nullable = false, length = 4)
    private Long password; // 계좌비밀번호

    @Column(nullable = false)
    private Long balance; // 잔액 (디폴트 값 1000원)

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Account(Long id, Long number, Long password, Long balance, User user,
            LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.id = id;
        this.number = number;
        this.password = password;
        this.balance = balance;
        this.user = user;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public void checkOwner(Long userId) {
        if (user.getId() != userId) {
            throw new CustomApiException("계좌 소유자가 아닙니다.");
        }
    }

    public void deposit(Long amount) {
        balance = balance + amount;
    }

    public void checkSamePassword(Long password) {
        if (this.password.longValue() != password.longValue()) {
            throw new CustomApiException("계좌 비밀번호 검증에 실패했습니다.");
        }
    }

    public void checkBalance(Long amount) {
        if (this.balance.longValue() < amount.longValue()) {
            throw new CustomApiException("계좌 잔액이 부족합니다.");
        }
    }

    public void withdraw(Long amount) {
        balance = balance - amount;
    }
}