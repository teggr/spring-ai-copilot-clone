package com.robintegg.copilot.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChatSessions {

  private final OpenAiChatModel chatModel;
  private final List<SystemMessageConfigurer> systemMessageConfigurers;
  private final List<ChatSessionConfigurer> configurers;
  private final List<PromptConfigurer> promptConfigurers;
  private final List<DefaultToolsConfigurer> defaultToolsConfigurers;
  private final List<DefaultToolCallbackConfigurer> defaultToolCallbackConfigurers;

  public ChatSessions(OpenAiChatModel chatModel,
                      List<SystemMessageConfigurer> systemMessageConfigurers,
                      List<ChatSessionConfigurer> configurers,
                      List<PromptConfigurer> promptConfigurers,
                      List<DefaultToolsConfigurer> defaultToolsConfigurers,
                      List<DefaultToolCallbackConfigurer> defaultToolCallbackConfigurers) {
    this.chatModel = chatModel;
    this.systemMessageConfigurers = systemMessageConfigurers;
    this.configurers = configurers;
    this.promptConfigurers = promptConfigurers;
    this.defaultToolsConfigurers = defaultToolsConfigurers;
    this.defaultToolCallbackConfigurers = defaultToolCallbackConfigurers;
  }

  public ChatSession createSession() {

    ChatClient.Builder builder = ChatClient.builder(chatModel);

    // build system message
    StringBuilder systemMessageBuilder = new StringBuilder();
    for (SystemMessageConfigurer systemMessageConfigurer : systemMessageConfigurers) {
      systemMessageBuilder.append( systemMessageConfigurer.systemMessage() );
    }
    builder.defaultSystem(systemMessageBuilder.toString());

    // other configurations
    for (ChatSessionConfigurer configurer : configurers) {
      builder = configurer.configure(builder);
    }

    // build tools
    List<Object> tools = new ArrayList<>();
    for (DefaultToolsConfigurer defaultToolsConfigurer : defaultToolsConfigurers) {
      tools.addAll( defaultToolsConfigurer.toolDefinitions() );
    }
    builder = builder.defaultTools( tools.toArray(new Object[0]) );

    // build tool callbacks
    List<ToolCallback> toolCallbacks = new ArrayList<>();
    for( DefaultToolCallbackConfigurer toolcallBackConfigurerDefault : defaultToolCallbackConfigurers) {
      toolCallbacks.addAll( toolcallBackConfigurerDefault.toolCallbacks() );
    }
    builder = builder.defaultToolCallbacks( toolCallbacks.toArray(new ToolCallback[0]) );

    return new ChatSession(builder, promptConfigurers);

  }

}
