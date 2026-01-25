package com.robintegg.copilot.subagents;

import com.robintegg.copilot.chat.DefaultToolsConfigurer;
import com.robintegg.copilot.chat.SystemMessageConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class SubAgentConfugration {

//  @Order(40)
//  @Bean
//  public SystemMessageConfigurer subagentsSystemMessageConfigurer() {
//    return () -> """
//        You have access to several specialized sub-agents, each designed to handle specific types of tasks.
//        When a user request is received, analyze the request to determine if it falls within the domain of any sub-agent.
//        If it does, delegate the task to the appropriate sub-agent for processing.
//        Ensure that you provide clear instructions to the sub-agent about what needs to be done.
//        After the sub-agent completes its task, collect the results and present them back to the user in a coherent manner.
//        """;
//  }

}
