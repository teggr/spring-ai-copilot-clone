package com.robintegg.copilot.agents;

import com.robintegg.copilot.repl.ReplCommand;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CustomAgentConfiguration {

  @Bean
  public ReplCommand customAgentCommand(Agents agents, MainAgent mainAgent) {

    List<String> agentNames =
      agents.getAll().stream()
        .map(Agents.Agent::name)
        .toList();

    return new ReplCommand() {

      @Override
      public boolean canHandle(String line) {
        return line.startsWith("/agent");
      }

      @Override
      public Completer completer() {

        return new AggregateCompleter(
          new StringsCompleter("/agent"),
          new ArgumentCompleter(new StringsCompleter(agentNames))
        );

      }

      @Override
      public void dispatch(String line, Terminal terminal) {

        // parse the agent name
        String[] parts = line.split(" ");
        if (parts.length < 2) {
          terminal.writer().println("Usage: /agent <agent-name>");
          terminal.flush();
        } else {
          mainAgent.setAgent(agents.getByName(parts[1]).orElseThrow());
          terminal.writer().println("Using agent: " + parts[1]);
          terminal.flush();
        }
      }

    };

  }

}
