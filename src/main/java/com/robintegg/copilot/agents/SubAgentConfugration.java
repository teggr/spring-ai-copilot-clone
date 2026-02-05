package com.robintegg.copilot.agents;

import com.robintegg.copilot.chat.ChatSessions;
import com.robintegg.copilot.chat.DefaultToolCallbackConfigurer;
import com.robintegg.copilot.chat.SystemMessageConfigurer;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.ai.tool.metadata.ToolMetadata;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Profile( "agents" )
@Configuration
public class SubAgentConfugration {

  @Order(40)
  @Bean
  public SystemMessageConfigurer subagentsSystemMessageConfigurer() {
    return () -> """
        You have access to several specialized sub-agents, each designed to handle specific types of tasks. Look for SUBAGENT: <subagent name> tool calls.
        When a user request is received, analyze the request to determine if it falls within the domain of any sub-agent.
        If it does, delegate the task to the appropriate sub-agent for processing.
        Ensure that you provide clear instructions to the sub-agent about what needs to be done.
        After the sub-agent completes its task, collect the results and present them back to the user in a coherent manner.
        """;
  }

  @Bean
  public DefaultToolCallbackConfigurer subAgentToolCallbackConfigurer( Agents agents, @Lazy ChatSessions chatSessions) {

    List<ToolCallback> toolCallbacks = new ArrayList<>();

    Collection<Agents.Agent> all = agents.getAll();

    for (Agents.Agent agent : all) {

      ToolCallback agentToolCallback = FunctionToolCallback
        .builder("subagent-" + agent.name(), new SubAgentFunction(agents, chatSessions, agent.name()))
          .description("SUBAGENT: " + agent.description())
        .inputType(SubAgentToolRequest.class)
          .toolMetadata(ToolMetadata.builder()
            .returnDirect(false)
            .build())
            .build();

      toolCallbacks.add(agentToolCallback);

    }

    return () -> toolCallbacks;

  }

  static class SubAgentFunction implements Function<SubAgentToolRequest, SubAgentTooResponse> {

    private final Agents agents;
    private final ChatSessions chatSessions;
    private final String name;

    public SubAgentFunction(Agents agents, ChatSessions chatSessions, String name) {
      this.agents = agents;
      this.chatSessions = chatSessions;
      this.name = name;
    }

    @Override
    public SubAgentTooResponse apply(SubAgentToolRequest subAgentToolRequest) {

      // spin up new agent instance to handle the request
      SubAgent subAgent = new SubAgent( chatSessions );
      subAgent.setAgent( agents.getByName(name).orElseThrow() );

      String response = subAgent.send(subAgentToolRequest.context());

      return new SubAgentTooResponse( wrapSubAgentResponse( response ) );

    }

    private static String wrapSubAgentResponse(String response) {
      return """
        Here is the response from the sub-agent:
        
        %s
        """.formatted(response);
    }

  }

  record SubAgentToolRequest(String context) {
  }

  record SubAgentTooResponse(String content) {
  }

}
