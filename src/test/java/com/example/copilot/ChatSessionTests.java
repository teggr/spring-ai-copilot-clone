package com.example.copilot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChatSessionTests {

    private ChatClient mockChatClient;
    private ChatClient.Builder mockBuilder;
    private ChatSession chatSession;

    @BeforeEach
    void setUp() {
        mockChatClient = mock(ChatClient.class);
        mockBuilder = mock(ChatClient.Builder.class);
        when(mockBuilder.build()).thenReturn(mockChatClient);
        
        chatSession = new ChatSession(mockBuilder);
    }

    @Test
    void addMessageWithEmptyInputReturnsError() {
        String result = chatSession.addMessage("");
        
        assertTrue(result.contains("Error"));
        assertTrue(result.contains("empty"));
    }

    @Test
    void addMessageWithNullInputReturnsError() {
        String result = chatSession.addMessage(null);
        
        assertTrue(result.contains("Error"));
        assertTrue(result.contains("empty"));
    }

    @Test
    void clearHistoryRemovesAllMessages() {
        chatSession.clearHistory();
        
        assertEquals(0, chatSession.getMessageCount());
    }

    @Test
    void getMessageCountReturnsZeroInitially() {
        assertEquals(0, chatSession.getMessageCount());
    }

    @Test
    void getConversationHistoryReturnsEmptyListInitially() {
        assertTrue(chatSession.getConversationHistory().isEmpty());
    }
}
