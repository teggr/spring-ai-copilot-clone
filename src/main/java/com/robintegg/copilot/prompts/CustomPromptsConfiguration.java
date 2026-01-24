package com.robintegg.copilot.prompts;

import com.robintegg.copilot.agents.MainAgent;
import com.robintegg.copilot.repl.ReplCommand;
import io.micrometer.core.instrument.util.IOUtils;
import org.jline.builtins.Completers;
import org.jline.reader.Completer;
import org.jline.terminal.Terminal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.jline.builtins.Completers.TreeCompleter.node;

@Configuration
public class CustomPromptsConfiguration {

  @Bean
  public ReplCommand customPromptCommand(MainAgent mainAgent) {

    File currentDir = new File(".examples/.github/prompts");

    return new ReplCommand() {

      @Override
      public Completer completer() {
        return new Completers.TreeCompleter(
          node(
            "/prompt",
            node(new Completers.FilesCompleter(currentDir, "*.prompt.md"))
          )
        );
      }

      @Override
      public void dispatch(String line, Terminal terminal) {

        String strippedLine = line.replaceFirst("/prompt", "").trim();

        File promptFile = new File(currentDir, strippedLine);

        try {

          String promptFileContents = IOUtils.toString(new FileInputStream(promptFile));

          // parse the command line
          String response = mainAgent.send(promptFileContents);

          terminal.writer().println(response);

        } catch ( IOException e) {
          terminal.writer().println("Could not read prompt file: " + promptFile.getAbsolutePath());
         }

        terminal.writer().flush();

      }

      @Override
      public boolean canHandle(String line) {
        return line.startsWith("/prompt");
      }

    };
  }

}
