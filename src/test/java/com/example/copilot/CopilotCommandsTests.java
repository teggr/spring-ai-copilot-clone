package com.example.copilot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CopilotCommandsTests {

    private ChatSession mockChatSession;
    private CopilotCommands commands;

    @BeforeEach
    void setUp() {
        mockChatSession = mock(ChatSession.class);
        commands = new CopilotCommands(mockChatSession);
    }

    @Test
    void askWithEmptyQuestionReturnsError() {
        String result = commands.ask("");
        
        assertTrue(result.contains("Error"));
        assertTrue(result.contains("provide a question"));
        verify(mockChatSession, never()).addMessage(any());
    }

    @Test
    void askWithNullQuestionReturnsError() {
        String result = commands.ask(null);
        
        assertTrue(result.contains("Error"));
        assertTrue(result.contains("provide a question"));
        verify(mockChatSession, never()).addMessage(any());
    }

    @Test
    void askWithValidQuestionClearsHistoryAndAddsMessage() {
        String question = "What is Spring Boot?";
        String expectedResponse = "Spring Boot is a framework...";
        when(mockChatSession.addMessage(question)).thenReturn(expectedResponse);
        
        String result = commands.ask(question);
        
        assertEquals(expectedResponse, result);
        verify(mockChatSession).clearHistory();
        verify(mockChatSession).addMessage(question);
    }

    @Test
    void clearCommandClearsHistory() {
        String result = commands.clear();
        
        assertTrue(result.contains("cleared"));
        verify(mockChatSession).clearHistory();
    }

    @Test
    void historyCommandReturnsMessageCount() {
        when(mockChatSession.getMessageCount()).thenReturn(0);
        
        String result = commands.history();
        
        assertTrue(result.contains("No conversation"));
        verify(mockChatSession).getMessageCount();
    }

    @Test
    void historyCommandWithMessagesReturnsCount() {
        when(mockChatSession.getMessageCount()).thenReturn(4);
        
        String result = commands.history();
        
        assertTrue(result.contains("4"));
        assertTrue(result.contains("messages"));
        verify(mockChatSession).getMessageCount();
    }

    @Test
    void helpCommandReturnsHelpText() {
        String result = commands.help();
        
        assertNotNull(result);
        assertTrue(result.contains("Available commands"));
        assertTrue(result.contains("/ask"));
        assertTrue(result.contains("/clear"));
        assertTrue(result.contains("/help"));
    }
}
