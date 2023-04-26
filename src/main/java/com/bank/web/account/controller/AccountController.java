package com.bank.web.account.controller;

import com.bank.core.auth.LoginUser;
import com.bank.web.account.dto.AccountReqDto;
import com.bank.web.account.dto.AccountReqDto.AccountSaveReqDto;
import com.bank.web.account.dto.AccountRespDto;
import com.bank.web.account.service.AccountService;
import com.bank.web.common.ResponseDto;
import lombok.RequiredArgsConstructor;
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

}
