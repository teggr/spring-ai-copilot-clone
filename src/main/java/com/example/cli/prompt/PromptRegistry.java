package com.example.cli.prompt;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PromptRegistry {
    
    private final Map<String, PromptTemplate> prompts = new LinkedHashMap<>();
    
    public PromptRegistry() {
        // Preload example prompts
        addPrompt(new PromptTemplate(
            "daily-summary",
            "Summarize today's work",
            "Summarize today's work:\n"
        ));
        
        addPrompt(new PromptTemplate(
            "code-review",
            "Request a code review",
            "Please review the following code:\n"
        ));
    }
    
    public void addPrompt(PromptTemplate prompt) {
        prompts.put(prompt.getName(), prompt);
    }
    
    public Optional<PromptTemplate> findPrompt(String name) {
        return Optional.ofNullable(prompts.get(name));
    }
    
    public Collection<PromptTemplate> getAllPrompts() {
        return prompts.values();
    }
    
    public Set<String> getPromptNames() {
        return prompts.keySet();
    }
    
    public List<PromptTemplate> findPromptsWithPrefix(String prefix) {
        List<PromptTemplate> results = new ArrayList<>();
        for (PromptTemplate prompt : prompts.values()) {
            if (prompt.getName().startsWith(prefix)) {
                results.add(prompt);
            }
        }
        return results;
    }
}
