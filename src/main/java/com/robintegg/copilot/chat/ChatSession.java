package com.robintegg.copilot.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;

public class ChatSession {

  private final ChatClient chatClient;

  public ChatSession( OpenAiChatModel chatModel ) {
    this.chatClient =  ChatClient.builder( chatModel )
      .build();
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
