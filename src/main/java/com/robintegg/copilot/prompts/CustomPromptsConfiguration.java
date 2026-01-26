package com.robintegg.copilot.prompts;

import com.robintegg.copilot.agents.MainAgent;
import com.robintegg.copilot.repl.ReplCommand;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CustomPromptsConfiguration {

  @Bean
  public List<ReplCommand> customPromptCommands(Prompts prompts, MainAgent mainAgent) {

    List<ReplCommand> commands = new ArrayList<>();

    for (Prompts.Prompt prompt : prompts.getAll()) {
      String commandName = "/" + prompt.name();
      
      ReplCommand command = new ReplCommand() {

        @Override
        public Completer completer() {
          return new StringsCompleter(commandName);
        }

        @Override
        public void dispatch(String line, Terminal terminal) {
          // Extract any additional text after the command
          String remainingText = "";
          if (line.length() > commandName.length()) {
            remainingText = line.substring(commandName.length()).trim();
          }
          
          // Build the complete prompt
          String completePrompt = prompt.content();
          if (!remainingText.isEmpty()) {
            completePrompt = completePrompt + "\n\n" + remainingText;
          }
          
          String response = mainAgent.send(completePrompt);
          terminal.writer().println(response);
          terminal.writer().flush();
        }

        @Override
        public boolean canHandle(String line) {
          return line.trim().startsWith(commandName);
        }

      };
      
      commands.add(command);
    }

    return commands;
  }

}
