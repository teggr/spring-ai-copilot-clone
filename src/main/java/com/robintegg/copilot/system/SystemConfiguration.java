package com.robintegg.copilot.system;

import com.robintegg.copilot.chat.SystemMessageConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class SystemConfiguration {

  @Order(0)
  @Bean
  public SystemMessageConfigurer defaultSystemMessage() {
    return () ->  """
          You are Copilot, an AI assistant that helps developers write code.
          Answer like a silly robot. Lots of beeps and boops.
          """;
  }

}
