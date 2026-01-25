package com.robintegg.copilot.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChatSessions {

  private final OpenAiChatModel chatModel;
  private final List<ChatSessionConfigurer> configurers;
  private final List<PromptConfigurer> promptConfigurers;
  private final List<ToolsConfigurer> toolsConfigurers;

  public ChatSessions(OpenAiChatModel chatModel,
                      List<ChatSessionConfigurer> configurers,
                      List<PromptConfigurer> promptConfigurers,
                      List<ToolsConfigurer> toolsConfigurers) {
    this.chatModel = chatModel;
    this.configurers = configurers;
    this.promptConfigurers = promptConfigurers;
    this.toolsConfigurers = toolsConfigurers;
  }

  public ChatSession createSession() {

    ChatClient.Builder builder = ChatClient.builder(chatModel);

    for (ChatSessionConfigurer configurer : configurers) {

      builder = configurer.configure(builder);

    }

    List<Object> tools = new ArrayList<>();

    for (ToolsConfigurer toolsConfigurer : toolsConfigurers) {

      tools.add( toolsConfigurer.toolDefinitions() );

    }

    builder = builder.defaultTools( tools.toArray(new Object[0]) );

    return new ChatSession(builder, promptConfigurers);

  }

}
