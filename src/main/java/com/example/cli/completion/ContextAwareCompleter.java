package com.example.cli.completion;

import com.example.cli.command.SlashCommand;
import com.example.cli.command.SlashCommandRegistry;
import com.example.cli.context.CommandContext;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.ArrayList;
import java.util.List;

public class ContextAwareCompleter implements Completer {
    
    private final SlashCommandRegistry registry;
    private final CommandContext context;
    
    public ContextAwareCompleter(SlashCommandRegistry registry, CommandContext context) {
        this.registry = registry;
        this.context = context;
    }
    
    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        String buffer = line.line();
        
        // No suggestions if input doesn't start with /
        if (!buffer.startsWith("/")) {
            return;
        }
        
        // Input is just "/" - suggest all slash commands
        if (buffer.equals("/")) {
            for (SlashCommand command : registry.getAllCommands()) {
                String fullLine = "/" + command.name();
                candidates.add(new Candidate(
                    fullLine,
                    command.name(),
                    null,
                    command.description(),
                    null,
                    null,
                    true
                ));
            }
            return;
        }
        
        // Check if it's a specific command with arguments
        String afterSlash = buffer.substring(1);
        String[] parts = afterSlash.split("\\s+", 2);
        String commandName = parts[0];
        
        // If we have a recognized command and there are arguments, delegate to command completion
        registry.findCommand(commandName).ifPresent(command -> {
            List<Candidate> commandCandidates = command.complete(context, buffer);
            candidates.addAll(commandCandidates);
        });
        
        // If no space yet, also suggest matching command names
        if (parts.length == 1) {
            for (SlashCommand command : registry.getAllCommands()) {
                if (command.name().startsWith(commandName)) {
                    String fullLine = "/" + command.name();
                    candidates.add(new Candidate(
                        fullLine,
                        command.name(),
                        null,
                        command.description(),
                        null,
                        null,
                        true
                    ));
                }
            }
        }
    }
}
