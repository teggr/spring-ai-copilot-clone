package com.robintegg.copilot.chat;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Component;

@Component
public class ChatSessions {

  private final OpenAiChatModel chatModel;

  public ChatSessions(OpenAiChatModel chatModel) {
    this.chatModel = chatModel;
  }

  public ChatSession createSession() {
    return new ChatSession(chatModel);
  }

}
