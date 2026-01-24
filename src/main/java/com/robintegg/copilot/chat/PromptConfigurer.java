package com.robintegg.copilot.chat;

import org.springframework.ai.chat.messages.Message;

import java.util.List;

public interface PromptConfigurer {

  List<Message> messages();

}
