package com.example.cli.repl;

import com.example.cli.command.SlashCommand;
import com.example.cli.command.SlashCommandRegistry;
import com.example.cli.completion.ContextAwareCompleter;
import com.example.cli.context.CommandContext;
import com.example.cli.session.CliSession;
import org.jline.reader.*;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class ReplLoop {
    
    private final SlashCommandRegistry registry;
    private final CliSession session;
    
    public ReplLoop(SlashCommandRegistry registry, CliSession session) {
        this.registry = registry;
        this.session = session;
    }
    
    public void start() {
        try {
            Terminal terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();
            
            // Create command context
            CommandContext context = new CommandContext(
                session,
                null, // will be set after LineReader creation
                System.out::println
            );
            
            LineReader lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(new ContextAwareCompleter(registry, context))
                    .build();
            
            // Update context with lineReader
            CommandContext updatedContext = new CommandContext(
                session,
                lineReader,
                System.out::println
            );
            
            System.out.println("=== Interactive Java CLI with Slash Commands ===");
            System.out.println("Type /help for available commands");
            System.out.println("Press Ctrl+C to cancel input, Ctrl+D to exit");
            System.out.println();
            
            while (true) {
                try {
                    String input = lineReader.readLine("> ");
                    
                    if (input == null || input.trim().isEmpty()) {
                        continue;
                    }
                    
                    input = input.trim();
                    
                    if (input.startsWith("/")) {
                        handleSlashCommand(updatedContext, input);
                    } else {
                        System.out.println("Input does not start with /. Only slash commands are supported.");
                    }
                    
                } catch (UserInterruptException e) {
                    // Ctrl+C - cancel current input and continue
                    System.out.println("^C");
                } catch (EndOfFileException e) {
                    // Ctrl+D - exit
                    System.out.println("\nGoodbye!");
                    break;
                }
            }
            
            terminal.close();
            
        } catch (IOException e) {
            System.err.println("Error initializing terminal: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleSlashCommand(CommandContext context, String input) {
        // Extract command name
        String afterSlash = input.substring(1);
        if (afterSlash.isEmpty()) {
            System.out.println("Error: Empty command. Type /help for available commands.");
            return;
        }
        
        String[] parts = afterSlash.split("\\s+", 2);
        String commandName = parts[0];
        
        Optional<SlashCommand> commandOpt = registry.findCommand(commandName);
        
        if (commandOpt.isEmpty()) {
            System.out.println("Error: Unknown command '/" + commandName + "'");
            System.out.println("Available commands: " + 
                String.join(", ", registry.getCommandNames().stream()
                    .map(name -> "/" + name)
                    .toList()));
            return;
        }
        
        try {
            commandOpt.get().execute(context, input);
        } catch (Exception e) {
            System.err.println("Error executing command: " + e.getMessage());
        }
    }
}
