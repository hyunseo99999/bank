package com.bank;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;


public class RegexTest {

    @Test
    public void 한글만_test() throws Exception {
        String value = "한글";
        boolean matches = Pattern.matches("^[가-힣]+$", value);
        assertThat(matches).isTrue();
    }

    @Test
    public void 한글은안된다_test() throws Exception {
        String value = "asfdsafㄱ";
        boolean matches = Pattern.matches("^[^ㄱ-ㅎ가-힣]+$", value);
        assertThat(matches).isFalse();
    }

    @Test
    public void 영어만된다_test() throws Exception {
        String value = "ssar";
        boolean matches = Pattern.matches("^[a-zA-Z]+$", value);
        assertThat(matches).isTrue();
    }

    @Test
    public void 영어는안된다_test() throws Exception {
        String value = "ssar";
        boolean matches = Pattern.matches("^[^a-zA-Z]+$", value);
        assertThat(matches).isFalse();
    }

    @Test
    public void 영어와숫자만된다_test() throws Exception {
        String value = "ddd0000=";
        boolean matches = Pattern.matches("^[^a-zA-Z0-9]+$", value);
        assertThat(matches).isFalse();
    }

    @Test
    public void 영어만되고_길이는최소2에서4개만된다_test() throws Exception {
        String value = "ssar";
        boolean matches = Pattern.matches("^[a-zA-Z]{2,4}$", value);
        assertThat(matches).isTrue();
    }
}
