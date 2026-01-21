package com.example.copilot;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChatSession {

    private final ChatClient chatClient;
    private final List<Message> conversationHistory;

    public ChatSession(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
        this.conversationHistory = new ArrayList<>();
    }

    public String addMessage(String userInput) {
        if (userInput == null || userInput.trim().isEmpty()) {
            return "Error: Message cannot be empty.";
        }

        try {
            // Add user message to history
            conversationHistory.add(new UserMessage(userInput));

            // Get response from AI with conversation history
            String response = chatClient.prompt()
                    .messages(conversationHistory)
                    .call()
                    .content();

            // Add assistant response to history
            conversationHistory.add(new AssistantMessage(response));

            return response;
        } catch (Exception e) {
            return "Error: Unable to get response from AI service. " +
                   "Please check your API key and network connection. Details: " + e.getMessage();
        }
    }

    public void clearHistory() {
        conversationHistory.clear();
    }

    public int getMessageCount() {
        return conversationHistory.size();
    }

    public List<Message> getConversationHistory() {
        return new ArrayList<>(conversationHistory);
    }
}
