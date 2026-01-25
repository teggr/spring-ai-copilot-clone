package com.robintegg.copilot.chat;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class ChatSessionConfiguration {


  @Bean
  public ChatSessionConfigurer loggingConfigurer() {
    return builder -> builder
      .defaultAdvisors(new SimpleLoggerAdvisor(
        ChatSessionConfiguration::logChatClientRequest,
        ModelOptionsUtils::toJsonStringPrettyPrinter,
        100
      ));
  }

  static String logChatClientRequest(ChatClientRequest request) {

    Map<String, Object> context = request.context();

    StringBuilder sb = new StringBuilder();

    sb.append("\n# Context\n\n");

    for (String key : context.keySet()) {
      sb.append(key).append(": ").append(context.get(key)).append("\n");
    }

    sb.append("\n# Request\n\n");

    Prompt prompt = request.prompt();

    sb.append("\n## Instructions\n\n");

    List<Message> instructions = prompt.getInstructions();

    for (Message message : instructions) {

      sb.append("\n### Message\n\n");

      sb.append(message).append("\n");

    }

    sb.append("\n## Options\n\n");

    ChatOptions options = prompt.getOptions();

    sb.append(ModelOptionsUtils.toJsonStringPrettyPrinter(options)).append("\n");

    if( options instanceof OpenAiChatOptions ai ) {
      sb.append("\n### OpenAiChatOptions\n\n");

      List<ToolCallback> toolCallbacks = ai.getToolCallbacks();

      for (ToolCallback toolCallback : toolCallbacks) {
        sb.append("\n### Tool\n\n");
        sb.append("Definition: ").append(toolCallback.getToolDefinition()).append("\n");
        sb.append("Metadata: ").append(toolCallback.getToolMetadata()).append("\n");
      }

    }

    return sb.toString();

  }

}
