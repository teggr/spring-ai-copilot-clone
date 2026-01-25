package com.robintegg.copilot.mcp;

import com.robintegg.copilot.chat.DefaultToolCallbackConfigurer;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class McpServersConfiguration {

  @Bean
  public DefaultToolCallbackConfigurer mcpServers(ToolCallbackProvider toolCallbackProvider) {
    return () -> List.of(toolCallbackProvider.getToolCallbacks());
  }

}
