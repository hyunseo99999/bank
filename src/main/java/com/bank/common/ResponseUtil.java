package com.bank.common;

import com.bank.web.common.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Getter @Setter
public class ResponseUtil<T> {
    private int code; // 200 성공

    private String message; // 메시지

    private T data;

    @Builder
    public ResponseUtil(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static void fail(HttpServletResponse response, String message, HttpStatus httpStatus) {
        try {
            response.setContentType("application/json;");
            response.setCharacterEncoding("utf-8");
            response.setStatus(httpStatus.value());

            ResponseUtil responseUtil = ResponseUtil.builder()
                                                .code(httpStatus.value())
                                                .message(message)
                                                .build();

            ObjectMapper mapper = new ObjectMapper();
            String responseBody = mapper.writeValueAsString(responseUtil);
            response.getWriter().println(responseBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void success(HttpServletResponse response, Object dto) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(1, "로그인 성공", dto);
            String responseBody = mapper.writeValueAsString(responseDto);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(200);
            response.getWriter().println(responseBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
