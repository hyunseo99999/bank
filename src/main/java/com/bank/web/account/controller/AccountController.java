package com.bank.web.account.controller;

import com.bank.core.auth.LoginUser;
import com.bank.web.account.dto.AccountReqDto;
import com.bank.web.account.dto.AccountReqDto.AccountDepositReqDto;
import com.bank.web.account.dto.AccountReqDto.AccountSaveReqDto;
import com.bank.web.account.dto.AccountReqDto.AccountTransferReqDto;
import com.bank.web.account.dto.AccountReqDto.AccountWithdrawReqDto;
import com.bank.web.account.dto.AccountRespDto;
import com.bank.web.account.service.AccountService;
import com.bank.web.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.bank.web.account.dto.AccountRespDto.*;
import static com.bank.web.account.service.AccountService.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/s/account")
    public ResponseEntity<?> saveAccount(@RequestBody @Valid AccountSaveReqDto accountSaveReqDto, BindingResult bindingResult, @AuthenticationPrincipal LoginUser loginUser) {
        AccountSaveRespDto accountSaveRespDto = accountService.save(accountSaveReqDto, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌등록 성공", accountSaveRespDto), HttpStatus.CREATED);
    }

    @GetMapping("/s/account/login-user")
    public ResponseEntity<?> findAccountByUser(@AuthenticationPrincipal LoginUser user) {
        AccountListRespDto findAccountByUser = accountService.findAccountByUser(user.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌번호 조회", findAccountByUser), HttpStatus.OK);
   }

    /**
     * Long number
     * Long userId
     * @param number
     * @return
     */
    @DeleteMapping("/s/account/{number}")
    public ResponseEntity<?> deleteAccountByUser(@PathVariable Long number, @AuthenticationPrincipal LoginUser loginUser) {
        accountService.deleteByAccountAndUser(number, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌번호 삭제 성공", null), HttpStatus.OK);
    }

    @PostMapping("/account/deposit")
    public ResponseEntity<?> depositAccount(@RequestBody @Valid AccountDepositReqDto accountDepositReqDto,
                                            BindingResult bindingResult) {
        AccountDepositRespDto accountDepositRespDto = accountService.insertAccount(accountDepositReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 입금 완료", accountDepositRespDto), HttpStatus.CREATED);
    }

    /**
     * 계좌 출금
     */
    @PostMapping("/s/account/withdraw")
    public ResponseEntity<?> accountWithDraw(@RequestBody @Valid AccountWithdrawReqDto accountWithdrawReqDto, BindingResult bindingResult
                                                , @AuthenticationPrincipal LoginUser login ) {
        AccountWithdrawRespDto accountWithdrawRespDto = accountService.accountWithDraw(accountWithdrawReqDto, login.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 출금 완료", accountWithdrawRespDto), HttpStatus.OK);
    }

    /**
     * 계좌이체
     */
    @PostMapping("/s/account/transfer")
    public ResponseEntity<?> transferAccount(@RequestBody @Valid AccountTransferReqDto accountTransferReqDto
                                            , BindingResult bindingResult
                                            , @AuthenticationPrincipal LoginUser login
    ) {
        AccountTransferRespDto accountTransferRespDto = accountService.계좌이체(accountTransferReqDto, login.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 이체 완료", accountTransferRespDto), HttpStatus.OK);
    }

}
