package com.levelup.levelupgamer;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    @Test
    public void generatePassword() {
        System.out.println("HASH_START");
        System.out.println(new BCryptPasswordEncoder().encode("1234"));
        System.out.println("HASH_END");
    }
}
