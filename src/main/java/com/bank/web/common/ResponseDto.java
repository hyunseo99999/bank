package com.bank.web.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class ResponseDto<T> {

    private int code;
    private String message;

    private T data;
}
