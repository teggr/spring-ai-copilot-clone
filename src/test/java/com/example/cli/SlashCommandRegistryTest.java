package com.example.cli;

import com.example.cli.command.SlashCommand;
import com.example.cli.command.SlashCommandRegistry;
import com.example.cli.context.CommandContext;
import org.jline.reader.Candidate;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SlashCommandRegistryTest {
    
    static class TestCommand implements SlashCommand {
        private final String name;
        
        public TestCommand(String name) {
            this.name = name;
        }
        
        @Override
        public String name() {
            return name;
        }
        
        @Override
        public String description() {
            return "Test command: " + name;
        }
        
        @Override
        public List<Candidate> complete(CommandContext ctx, String buffer) {
            return new ArrayList<>();
        }
        
        @Override
        public void execute(CommandContext ctx, String rawInput) {
            // Test implementation
        }
    }
    
    @Test
    void testCommandDiscovery() {
        List<SlashCommand> commands = List.of(
            new TestCommand("test1"),
            new TestCommand("test2"),
            new TestCommand("test3")
        );
        
        SlashCommandRegistry registry = new SlashCommandRegistry(commands);
        
        assertEquals(3, registry.getAllCommands().size());
        assertTrue(registry.getCommandNames().contains("test1"));
        assertTrue(registry.getCommandNames().contains("test2"));
        assertTrue(registry.getCommandNames().contains("test3"));
    }
    
    @Test
    void testFindCommand() {
        List<SlashCommand> commands = List.of(
            new TestCommand("test1"),
            new TestCommand("test2")
        );
        
        SlashCommandRegistry registry = new SlashCommandRegistry(commands);
        
        Optional<SlashCommand> found = registry.findCommand("test1");
        assertTrue(found.isPresent());
        assertEquals("test1", found.get().name());
        
        Optional<SlashCommand> notFound = registry.findCommand("nonexistent");
        assertFalse(notFound.isPresent());
    }
}
