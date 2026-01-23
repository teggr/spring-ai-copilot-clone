package com.robintegg.copilot.repl;

import com.robintegg.copilot.agents.MainAgent;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.*;

@Configuration
public class ReplConfiguration {

  @Bean
  public CommandLineRunner commandLineRunner(List<ReplCommand> replCommands, MainAgent mainAgent) {

    return args -> {
      System.out.println("Welcome to the Github Copilot clone.");

      try {
        // Create a terminal
        Terminal terminal = TerminalBuilder.builder().system(true).build();

        List<Completer> allCompleters = new ArrayList<>();
        allCompleters.add(new StringsCompleter("/quit", "/exit"));
        replCommands.stream().map(ReplCommand::completer).forEach(allCompleters::add);

        // Create a line reader
        LineReader reader = LineReaderBuilder.builder()
          .terminal(terminal)
          .completer(new AggregateCompleter(allCompleters))
          .build();

        // Read lines from the user
        while (true) {
          String line = reader.readLine("prompt> ");

          // Exit if requested
          if ("/exit".equalsIgnoreCase(line.trim()) || "/quit".equalsIgnoreCase(line.trim())) {
            break;
          }

          // Echo the line back to the user
          boolean handled = false;
          if (line.startsWith("/")) {
            Optional<ReplCommand> first = replCommands.stream().filter(r -> r.canHandle(line)).findFirst();
            if(first.isPresent()) {
              handled = true;
              first.get().dispatch(line, terminal);
            }
          }

          if (!handled) {
            terminal.writer().println(mainAgent.send(line));
            terminal.flush();
          }

        }

        terminal.writer().println("Goodbye!");
        terminal.close();

      } catch (IOException e) {
        System.err.println("Error creating terminal: " + e.getMessage());
      } finally {
        mainAgent.close();
      }

    };

  }

}
