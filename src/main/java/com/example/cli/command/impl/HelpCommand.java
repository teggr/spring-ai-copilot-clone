package com.example.cli.command.impl;

import com.example.cli.command.SlashCommand;
import com.example.cli.command.SlashCommandRegistry;
import com.example.cli.context.CommandContext;
import org.jline.reader.Candidate;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HelpCommand implements SlashCommand {
    
    private final SlashCommandRegistry registry;
    
    public HelpCommand(@Lazy SlashCommandRegistry registry) {
        this.registry = registry;
    }
    
    @Override
    public String name() {
        return "help";
    }
    
    @Override
    public String description() {
        return "List all available commands with descriptions";
    }
    
    @Override
    public List<Candidate> complete(CommandContext ctx, String buffer) {
        return new ArrayList<>();
    }
    
    @Override
    public void execute(CommandContext ctx, String rawInput) {
        StringBuilder sb = new StringBuilder();
        sb.append("Available commands:\n");
        
        for (SlashCommand command : registry.getAllCommands()) {
            sb.append(String.format("  /%s - %s\n", command.name(), command.description()));
        }
        
        ctx.getOutput().accept(sb.toString());
    }
}
