package com.example.cli.command.impl;

import com.example.cli.command.SlashCommand;
import com.example.cli.context.CommandContext;
import org.jline.reader.Candidate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExitCommand implements SlashCommand {
    
    @Override
    public String name() {
        return "exit";
    }
    
    @Override
    public String description() {
        return "Exit the REPL cleanly";
    }
    
    @Override
    public List<Candidate> complete(CommandContext ctx, String buffer) {
        return new ArrayList<>();
    }
    
    @Override
    public void execute(CommandContext ctx, String rawInput) {
        ctx.getOutput().accept("Goodbye!");
        System.exit(0);
    }
}
