package com.example.cli;

import com.example.cli.prompt.PromptRegistry;
import com.example.cli.prompt.PromptTemplate;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PromptRegistryTest {
    
    @Test
    void testPreloadedPrompts() {
        PromptRegistry registry = new PromptRegistry();
        
        assertTrue(registry.getPromptNames().contains("daily-summary"));
        assertTrue(registry.getPromptNames().contains("code-review"));
        assertEquals(2, registry.getAllPrompts().size());
    }
    
    @Test
    void testFindPrompt() {
        PromptRegistry registry = new PromptRegistry();
        
        Optional<PromptTemplate> found = registry.findPrompt("daily-summary");
        assertTrue(found.isPresent());
        assertEquals("daily-summary", found.get().getName());
        assertEquals("Summarize today's work:\n", found.get().getBody());
        
        Optional<PromptTemplate> notFound = registry.findPrompt("nonexistent");
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testAddPrompt() {
        PromptRegistry registry = new PromptRegistry();
        
        PromptTemplate newPrompt = new PromptTemplate(
            "test-prompt",
            "Test prompt",
            "Test body:\n"
        );
        
        registry.addPrompt(newPrompt);
        
        assertTrue(registry.getPromptNames().contains("test-prompt"));
        Optional<PromptTemplate> found = registry.findPrompt("test-prompt");
        assertTrue(found.isPresent());
        assertEquals("test-prompt", found.get().getName());
    }
    
    @Test
    void testFindPromptsWithPrefix() {
        PromptRegistry registry = new PromptRegistry();
        
        registry.addPrompt(new PromptTemplate("code-format", "Format code", "Format:\n"));
        registry.addPrompt(new PromptTemplate("code-analyze", "Analyze code", "Analyze:\n"));
        
        List<PromptTemplate> codePrompts = registry.findPromptsWithPrefix("code");
        assertEquals(3, codePrompts.size()); // code-review, code-format, code-analyze
        
        List<PromptTemplate> dailyPrompts = registry.findPromptsWithPrefix("daily");
        assertEquals(1, dailyPrompts.size());
        assertEquals("daily-summary", dailyPrompts.get(0).getName());
        
        List<PromptTemplate> noMatch = registry.findPromptsWithPrefix("xyz");
        assertEquals(0, noMatch.size());
    }
}
