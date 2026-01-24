package com.robintegg.copilot.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatSession {

  private final ChatClient chatClient;
  private final List<PromptConfigurer> promptConfigurers;

  public ChatSession(ChatClient.Builder chatClientBuilder, List<PromptConfigurer> promptConfigurers) {
    this.chatClient = chatClientBuilder.build();
    this.promptConfigurers = promptConfigurers;
  }

  public String sendMessage(String message) {

    ChatClient.ChatClientRequestSpec prompt = this.chatClient.prompt();

    List<Message> messages = new ArrayList<>();

    for (PromptConfigurer promptConfigurer : this.promptConfigurers) {

      messages.addAll( promptConfigurer.messages() );

    }

    messages.add(new UserMessage(message));

    ChatClient.ChatClientRequestSpec user = prompt
      .messages(messages);

    return user
      .call()
      .content();

  }

  public void close() {

  }
}
