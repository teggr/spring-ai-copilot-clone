package com.example.cli.command;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SlashCommandRegistry {
    
    private final Map<String, SlashCommand> commands = new HashMap<>();
    
    public SlashCommandRegistry(List<SlashCommand> commandBeans) {
        for (SlashCommand command : commandBeans) {
            commands.put(command.name(), command);
        }
    }
    
    public Optional<SlashCommand> findCommand(String name) {
        return Optional.ofNullable(commands.get(name));
    }
    
    public Collection<SlashCommand> getAllCommands() {
        return commands.values();
    }
    
    public Set<String> getCommandNames() {
        return commands.keySet();
    }
}
