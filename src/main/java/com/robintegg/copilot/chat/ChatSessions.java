package com.robintegg.copilot.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatSessions {

  private final OpenAiChatModel chatModel;
  private final List<ChatSessionConfigurer> configurers;

  public ChatSessions(OpenAiChatModel chatModel, List<ChatSessionConfigurer> configurers) {
    this.chatModel = chatModel;
    this.configurers = configurers;
  }

  public ChatSession createSession() {

    ChatClient.Builder builder = ChatClient.builder(chatModel);

    for (ChatSessionConfigurer configurer : configurers) {

      builder = configurer.configure(builder);

    }

    return new ChatSession(builder);

  }

}
