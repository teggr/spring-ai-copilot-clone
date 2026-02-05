package com.robintegg.copilot.memory;

import com.robintegg.copilot.chat.ChatSessionConfigurer;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile( "memory" )
@Configuration
public class ChatMemoryConfiguration {


  @Bean
  public ChatSessionConfigurer messageChatMemory() {
    InMemoryChatMemoryRepository inMemoryChatMemoryRepository = new InMemoryChatMemoryRepository();
    MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
      .chatMemoryRepository(inMemoryChatMemoryRepository)
      .maxMessages(10)
      .build();
    return builder -> builder
      .defaultAdvisors(PromptChatMemoryAdvisor.builder(chatMemory).build());
  }

}
