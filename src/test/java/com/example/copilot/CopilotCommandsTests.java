package com.example.copilot;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CopilotCommandsTests {

    @Test
    void askWithEmptyQuestionReturnsError() {
        ChatClient.Builder mockBuilder = mock(ChatClient.Builder.class);
        when(mockBuilder.build()).thenReturn(mock(ChatClient.class));
        
        CopilotCommands commands = new CopilotCommands(mockBuilder);
        String result = commands.ask("");
        
        assertTrue(result.contains("Error"));
        assertTrue(result.contains("provide a question"));
    }

    @Test
    void askWithNullQuestionReturnsError() {
        ChatClient.Builder mockBuilder = mock(ChatClient.Builder.class);
        when(mockBuilder.build()).thenReturn(mock(ChatClient.class));
        
        CopilotCommands commands = new CopilotCommands(mockBuilder);
        String result = commands.ask(null);
        
        assertTrue(result.contains("Error"));
        assertTrue(result.contains("provide a question"));
    }

}
