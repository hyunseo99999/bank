package com.bank.password;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordTest {

    @DisplayName("비밀번호 성공")
    @Test
    void password_success_test() {
       String value = "1q2w3e4r!@";
       // /(\w)\1\1\1/
       boolean matches = Pattern.matches("/(\\w)\\1\\1\\1/", value);
       assertThat(matches).isTrue();

       value = "1q2w3e4r!@r3";
       // /(\w)\1\1\1/
       matches = Pattern.matches("/(\\w)\\1\\1\\1/", value);
       assertThat(matches).isTrue();

    }

    @DisplayName("비밀번호 4자 이상 연속 또는 반복되는 숫자 및 숫자를 사용하실 수 없습니다.")
    @Test
    void password_fail_test() {
        String value = "11test";
        // /(\w)\1\1\1/
        boolean matches = Pattern.matches("/(\\w)\\1\\1\\1/", value);
        assertThat(matches).isFalse();

       value = "1w2w3w3w";
       // /(\w)\1\1\1/
       matches = Pattern.matches("/(\\w)\\1\\1\\1/", value);
       assertThat(matches).isFalse();
    }

    @DisplayName("비밀번호는 영문, 숫자, 특수문자의 조합의 8~12자리로 입력해주세요.")
    @Test
    void password_fail2_test() {
        String value = "1q2w3e4r";
        // /(\w)\1\1\1/
        boolean matches = Pattern.matches("`~!@#$%^&*()_-+=|\\{}[];':,./<>?", value);
        assertThat(matches).isFalse();

        value = "1234";
       // /(\w)\1\1\1/
        matches = Pattern.matches("`~!@#$%^&*()_-+=|\\{}[];':,./<>?", value);
        assertThat(matches).isFalse();
    }

}
