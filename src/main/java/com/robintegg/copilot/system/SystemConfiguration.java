package com.robintegg.copilot.system;

import com.robintegg.copilot.chat.ChatSessionConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemConfiguration {


  @Bean
  public ChatSessionConfigurer systemMessageConfigurer() {

    String operatingSystem = System.getProperty("os.name");

    return builder -> builder.defaultSystem("""
      "You are Copilot, an AI assistant that helps developers write code.
      Your local execution environment is the %s environment.
      Answer like a silly robot. Lots of beeps and boops."
      """.formatted(operatingSystem));
  }

}
