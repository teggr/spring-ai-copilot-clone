package com.robintegg.copilot.tools;

import com.robintegg.copilot.chat.ToolsConfigurer;
import io.micrometer.core.instrument.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class DefaultToolsConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(DefaultToolsConfiguration.class);

  @Bean
  public ToolsConfigurer defaultTools() {

    return DefaultToolDefinitions::new;

  }

  static class DefaultToolDefinitions {

    @Tool(description = "Reads the contents of a specified file and returns it as a string.")
    public String readFile(@ToolParam(description = "The path to the file to read") String filePath) throws IOException {
      logger.info("TOOL INVOKED - Reading file: " + filePath);
      return IOUtils.toString(new FileInputStream(new File(filePath)));
    }

    @Tool(description = "Executes a system command and returns its output as a string.")
    public String executeCommand(@ToolParam(description = "The system command to execute") String command) throws IOException {
      logger.info("TOOL INVOKED - Executing command: " + command);

      // For Windows, wrap the command in cmd.exe
      String[] cmd;
      if (System.getProperty("os.name").toLowerCase().contains("windows")) {
        cmd = new String[]{"cmd.exe", "/c", command};
      } else {
        cmd = new String[]{"/bin/sh", "-c", command};
      }

      Process process = Runtime.getRuntime().exec(cmd);
      return IOUtils.toString(process.getInputStream());
    }

  }

}
