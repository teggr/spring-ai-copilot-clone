package com.robintegg.copilot.repl;

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
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ReplConfiguration {

  @Bean
  public CommandLineRunner commandLineRunner(List<SlashCommand> comamnds) {

    return args -> {
      System.out.println("REPL CommandLineRunner initialized.");
      // Additional REPL setup can be done here

      try {
        // Create a terminal
        Terminal terminal = TerminalBuilder.builder().system(true).build();

        List<Completer> allCompleters = new ArrayList<>();
        allCompleters.add(new StringsCompleter("/quit", "/exit"));
        allCompleters.addAll( comamnds.stream().map( SlashCommand::completer ).toList() );

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
          terminal.writer().println("You entered: " + line);
          terminal.flush();
        }

        terminal.writer().println("Goodbye!");
        terminal.close();

      } catch (IOException e) {
        System.err.println("Error creating terminal: " + e.getMessage());
      }

    };

  }

}
