package com.robintegg.copilot.agents;

import com.robintegg.copilot.chat.ChatSession;
import com.robintegg.copilot.chat.ChatSessions;
import org.springframework.stereotype.Component;

@Component
public class MainAgent {

  private final ChatSessions chatSessions;

  private ChatSession mainSession;

  public MainAgent(ChatSessions chatSessions) {
    this.chatSessions = chatSessions;
    this.mainSession = chatSessions.createSession();
  }

  public String send(String line) {
    return mainSession.sendMessage(line);
  }

  public void close() {
    mainSession.close();
  }

}
