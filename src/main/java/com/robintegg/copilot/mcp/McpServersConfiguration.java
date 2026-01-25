package com.robintegg.copilot.mcp;

import com.robintegg.copilot.chat.ChatSessionConfigurer;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServersConfiguration {

  @Bean
  public ChatSessionConfigurer mcpServers(ToolCallbackProvider toolCallbackProvider) {
    return builder -> builder
      .defaultToolCallbacks(toolCallbackProvider);
  }

}
