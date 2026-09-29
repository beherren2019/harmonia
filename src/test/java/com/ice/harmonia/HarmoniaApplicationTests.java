package com.ice.harmonia;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // <-- ADD THIS TO FIX THE BOOT FAILURE
class HarmoniaApplicationTests {

    @Test
    void contextLoads() {
    }

}
