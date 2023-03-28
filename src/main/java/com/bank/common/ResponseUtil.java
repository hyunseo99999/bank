package com.bank.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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

    public static void unAuthentication(HttpServletResponse response, String message) {
        try {
            response.setContentType("application/json;");
            response.setCharacterEncoding("utf-8");
            response.setStatus(401);

            ResponseUtil responseUtil = ResponseUtil.builder()
                                                .code(401)
                                                .message(message)
                                                .build();

            ObjectMapper mapper = new ObjectMapper();
            String responseBody = mapper.writeValueAsString(responseUtil);
            response.getWriter().println(responseBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
