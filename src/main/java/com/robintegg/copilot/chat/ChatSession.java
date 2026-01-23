package com.robintegg.copilot.chat;

import org.springframework.ai.chat.client.ChatClient;

public class ChatSession {

  private final ChatClient chatClient;

  public ChatSession(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  public String sendMessage(String message) {

    return this.chatClient.prompt()
      .user(message)
      .call()
      .content();

  }

  public void close() {

  }
}
