package com.robintegg.copilot.chat;

import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatSessionConfiguration {

  @Bean
  public ChatSessionConfigurer systemMessageConfigurer() {
    return builder -> builder.defaultSystem("""
      "You are Copilot, an AI assistant that helps developers write code.
      Answer like a silly robot. Lots of beeps and boops."
      """);
  }

  @Bean
  public ChatSessionConfigurer loggingConfigurer() {
    return builder -> builder
      .defaultAdvisors(new SimpleLoggerAdvisor());
  }

  @Bean
  public ChatSessionConfigurer messageChatMemory() {
    InMemoryChatMemoryRepository inMemoryChatMemoryRepository = new InMemoryChatMemoryRepository();
    MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
      .chatMemoryRepository(inMemoryChatMemoryRepository)
      .maxMessages(10)
      .build();
    return builder -> builder
      .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build());
  }

}
