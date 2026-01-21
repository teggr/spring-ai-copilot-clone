package com.example.cli.command;

import com.example.cli.context.CommandContext;
import org.jline.reader.Candidate;

import java.util.List;

public interface SlashCommand {
    
    String name();
    
    String description();
    
    List<Candidate> complete(CommandContext ctx, String buffer);
    
    void execute(CommandContext ctx, String rawInput) throws Exception;
}
