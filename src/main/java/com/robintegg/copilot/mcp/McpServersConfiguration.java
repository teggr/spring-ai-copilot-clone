package com.robintegg.copilot.mcp;

import com.robintegg.copilot.chat.DefaultToolCallbackConfigurer;
import com.robintegg.copilot.chat.SystemMessageConfigurer;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.util.List;

@Profile( "mcp" )
@Configuration
public class McpServersConfiguration {

  @Order(30)
  @Bean
  public SystemMessageConfigurer mcpToolsSystemMessage() {
    return () -> """
      When using the Playwright MCP server, you MUST explicitly close the browser before completing the task.
      
      The task is not considered complete until the browser has been closed via the appropriate Playwright MCP command.
      
      Always follow this sequence:
      
      Perform the required browser actions
      
      Confirm results
      
      Close all pages and the browser
      
      Only then provide the final answer
      
      If the browser is not closed, the task is incomplete.
      
      IMPORTANT: When extracting content from web pages, use browser_evaluate with JavaScript to extract ONLY the specific
      data needed (e.g., headline text, specific elements) rather than returning entire page HTML or screenshots with full content.
      This prevents token limit issues. The browser_evaluate tool requires a FUNCTION parameter, not just an expression.
      For example: `() => { return Array.from(document.querySelectorAll('selector')).map(el => el.textContent.trim()); }`
      """;
  }

  @Bean
  public DefaultToolCallbackConfigurer mcpServers(ToolCallbackProvider toolCallbackProvider) {
    return () -> List.of(toolCallbackProvider.getToolCallbacks());
  }

}
