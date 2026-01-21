package com.example.copilot;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CopilotApplicationTests {

    @Test
    void applicationClassExists() {
        assertNotNull(CopilotApplication.class);
    }

    @Test
    void commandsClassExists() {
        assertNotNull(CopilotCommands.class);
    }

}
