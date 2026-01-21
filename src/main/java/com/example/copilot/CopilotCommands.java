package com.example.copilot;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class CopilotCommands {

    private final ChatClient chatClient;

    public CopilotCommands(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @ShellMethod(key = "ask", value = "Ask a question to the AI assistant")
    public String ask(@ShellOption(help = "Your question") String question) {
        if (question == null || question.trim().isEmpty()) {
            return "Error: Please provide a question.";
        }
        
        try {
            return chatClient.prompt()
                    .user(question)
                    .call()
                    .content();
        } catch (Exception e) {
            return "Error: Unable to get response from AI service. " + 
                   "Please check your API key and network connection. Details: " + e.getMessage();
        }
    }

}
