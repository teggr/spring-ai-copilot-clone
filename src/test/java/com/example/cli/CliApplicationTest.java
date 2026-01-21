package com.example.cli;

import com.example.cli.application.CliApplication;
import com.example.cli.command.SlashCommandRegistry;
import com.example.cli.repl.ReplLoop;
import com.example.cli.session.CliSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = CliApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class CliApplicationTest {

    @MockBean
    private ReplLoop replLoop; // Mock to prevent REPL from starting
    
    @Autowired
    private SlashCommandRegistry registry;
    
    @Autowired
    private CliSession session;

    @Test
    void contextLoads() {
        // This test verifies that the Spring context loads successfully
        assertNotNull(registry, "SlashCommandRegistry should be loaded");
        assertNotNull(session, "CliSession should be loaded");
    }
}
