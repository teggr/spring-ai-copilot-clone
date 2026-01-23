package com.robintegg.copilot.chat;

import org.springframework.ai.chat.client.ChatClient;


public interface ChatSessionConfigurer {

  ChatClient.Builder configure(ChatClient.Builder builder);

}
