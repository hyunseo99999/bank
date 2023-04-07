package com.bank;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class RegexTest {

    @Test
    public void 한글만된다_test() throws Exception {
        String value = "";
        boolean result = Pattern.matches("^[가-힣]+$", value);
        log.debug("테스트 : " + result);
    }

    @Test
    public void 한글은안된다_test() throws Exception {
        String value = "$86..ssa";
        // String value = "$86..ssa";
        boolean result = Pattern.matches("^[^가-힣]+$", value);
        log.debug("테스트 : " + result);
    }

    @Test
    public void 영어만된다_test() throws Exception {
        String value = "ssar";
        // String value = "ssar2";
        boolean result = Pattern.matches("^[a-zA-Z]+$", value);
        log.debug("테스트 : " + result);
    }

    @Test
    public void 영어는안된다_test() throws Exception {
        String value = "1한글$%^";
        // String value = "ssar";
        boolean result = Pattern.matches("^[^a-zA-Z]+$", value);
        log.debug("테스트 : " + result);
    }

    @Test
    public void 영어와숫자만된다_test() throws Exception {
        String value = "ssar2";
        // String value = "ssar2&";
        // String value = "ssar한글";
        boolean result = Pattern.matches("^[a-zA-Z0-9]+$", value);
        log.debug("테스트 : " + result);
    }

    @Test
    public void 영어만되고_길이는최소2최대4이다_test() throws Exception {
        String value = "ssar";
        // String value = "ssarm";
        boolean result = Pattern.matches("^[a-zA-Z]{2,4}$", value);
        log.debug("테스트 : " + result);
    }

    @Test
    public void user_username_test() throws Exception {
        String username = "ssar";
        // String username = "ssa^";
        boolean result = Pattern.matches("^[a-zA-Z0-9]{2,20}$", username);
        System.out.println("테스트 : " + result);
    }

    @Test
    public void user_email_test() throws Exception {
        String email = "s...s@fGf.ccm";
        // String username = "@fGf.ccm"; // +를 *로 변경해보기
        boolean result = Pattern.matches("^[a-zA-Z0-9.]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$", email);
        log.debug("테스트 : " + result);
    }

    @Test
    public void user_fullname_test() throws Exception {
        String fullname = "코스";
        // String fullname = "코스ss1";
        boolean result = Pattern.matches("^[a-zA-Z가-힣]{1,20}$", fullname);
        log.debug("테스트 : " + result);
    }
}
