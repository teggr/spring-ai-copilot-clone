package com.robintegg.copilot.agents;

import com.robintegg.copilot.chat.ChatSession;
import com.robintegg.copilot.chat.ChatSessionConfiguration;
import com.robintegg.copilot.chat.ChatSessions;
import org.springframework.stereotype.Component;

@Component
public class MainAgent {

  private final ChatSessions chatSessions;

  private ChatSession mainSession;
  private Agents.Agent agent;

  public MainAgent(ChatSessions chatSessions) {
    this.chatSessions = chatSessions;
    this.mainSession = chatSessions.createSession();
  }

  public void setAgent(Agents.Agent agent) {
    this.agent = agent;
  }

  public String send(String line) {
    return mainSession.sendMessage(agent, line);
  }

  public void close() {
    mainSession.close();
  }

}
