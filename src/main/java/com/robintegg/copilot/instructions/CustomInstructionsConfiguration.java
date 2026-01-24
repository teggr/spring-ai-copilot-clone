package com.robintegg.copilot.instructions;

import com.networknt.schema.OutputFormat;
import com.robintegg.copilot.chat.PromptConfigurer;
import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Configuration
public class CustomInstructionsConfiguration {

  @Bean
  public PromptConfigurer customInstructions() {

    File customInstructionFile = new File(".examples/.github/copilot-instructions.md");

    return () -> {

      if (customInstructionFile.exists()) {

        try {

          String customInstructionsFileContent = IOUtils.toString(new FileInputStream(customInstructionFile));

          return List.of( new UserMessage( customInstructionsFileContent ));

        } catch (IOException e) {

          throw new RuntimeException(e);

        }

      }

      return List.of();

    };

  }

}
