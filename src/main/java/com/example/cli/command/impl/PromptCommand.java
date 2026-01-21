package com.example.cli.command.impl;

import com.example.cli.command.SlashCommand;
import com.example.cli.context.CommandContext;
import com.example.cli.prompt.PromptRegistry;
import com.example.cli.prompt.PromptTemplate;
import org.jline.reader.Candidate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PromptCommand implements SlashCommand {
    
    private final PromptRegistry promptRegistry;
    
    public PromptCommand(PromptRegistry promptRegistry) {
        this.promptRegistry = promptRegistry;
    }
    
    @Override
    public String name() {
        return "prompt";
    }
    
    @Override
    public String description() {
        return "Use a prompt template: /prompt <prompt-name> <optional trailing text>";
    }
    
    @Override
    public List<Candidate> complete(CommandContext ctx, String buffer) {
        List<Candidate> candidates = new ArrayList<>();
        
        // Parse the buffer to get the part after /prompt
        String afterCommand = buffer.substring("/prompt".length()).trim();
        
        if (afterCommand.isEmpty()) {
            // Show all prompts
            for (PromptTemplate prompt : promptRegistry.getAllPrompts()) {
                String fullLine = "/prompt " + prompt.getName();
                candidates.add(new Candidate(
                    fullLine,
                    prompt.getName(),
                    null,
                    prompt.getDescription(),
                    null,
                    null,
                    true
                ));
            }
        } else {
            // Filter prompts by prefix
            String[] parts = afterCommand.split("\\s+", 2);
            String prefix = parts[0];
            
            for (PromptTemplate prompt : promptRegistry.findPromptsWithPrefix(prefix)) {
                String fullLine = "/prompt " + prompt.getName();
                if (parts.length > 1) {
                    fullLine += " " + parts[1];
                }
                candidates.add(new Candidate(
                    fullLine,
                    prompt.getName(),
                    null,
                    prompt.getDescription(),
                    null,
                    null,
                    true
                ));
            }
        }
        
        return candidates;
    }
    
    @Override
    public void execute(CommandContext ctx, String rawInput) throws Exception {
        // Parse: /prompt <prompt-name> <optional trailing text>
        String afterCommand = rawInput.substring("/prompt".length()).trim();
        
        if (afterCommand.isEmpty()) {
            ctx.getOutput().accept("Error: Please specify a prompt name.\nAvailable prompts: " + 
                String.join(", ", promptRegistry.getPromptNames()));
            return;
        }
        
        String[] parts = afterCommand.split("\\s+", 2);
        String promptName = parts[0];
        String trailingText = parts.length > 1 ? parts[1] : "";
        
        Optional<PromptTemplate> promptOpt = promptRegistry.findPrompt(promptName);
        
        if (promptOpt.isEmpty()) {
            ctx.getOutput().accept("Error: Unknown prompt '" + promptName + "'.\nAvailable prompts: " + 
                String.join(", ", promptRegistry.getPromptNames()));
            return;
        }
        
        PromptTemplate prompt = promptOpt.get();
        String output = prompt.getBody() + trailingText;
        ctx.getOutput().accept(output);
    }
}
