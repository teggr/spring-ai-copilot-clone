package com.robintegg.copilot.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class ChatSessions {

  private final ChatClient.Builder clientBuilder;

  public ChatSessions(ChatClient.Builder clientBuilder) {
    this.clientBuilder = clientBuilder;
  }

  public ChatSession createSession() {
    return new ChatSession(clientBuilder.clone());
  }

}
