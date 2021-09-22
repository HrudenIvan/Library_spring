package com.final_ptoject.library_spring.util;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderGeneratorTest {

    @Test
    @Ignore
    void hash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String pas = "123";
        for (int i = 0; i < 10; i++) {
            System.out.println(encoder.encode(pas));
        }
        assertTrue(true,"for testing purposes");
    }
}
