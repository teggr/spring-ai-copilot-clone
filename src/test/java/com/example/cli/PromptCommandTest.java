package com.example.cli;

import com.example.cli.command.impl.PromptCommand;
import com.example.cli.context.CommandContext;
import com.example.cli.prompt.PromptRegistry;
import com.example.cli.prompt.PromptTemplate;
import com.example.cli.session.CliSession;
import org.jline.reader.Candidate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PromptCommandTest {
    
    private PromptRegistry promptRegistry;
    private PromptCommand promptCommand;
    private List<String> outputLines;
    private CommandContext context;
    
    @BeforeEach
    void setUp() {
        promptRegistry = new PromptRegistry();
        promptCommand = new PromptCommand(promptRegistry);
        outputLines = new ArrayList<>();
        
        CliSession session = new CliSession();
        context = new CommandContext(session, null, outputLines::add);
    }
    
    @Test
    void testExecuteWithValidPrompt() throws Exception {
        promptCommand.execute(context, "/prompt daily-summary focus on auth module");
        
        assertEquals(1, outputLines.size());
        assertEquals("Summarize today's work:\nfocus on auth module", outputLines.get(0));
    }
    
    @Test
    void testExecuteWithValidPromptNoTrailingText() throws Exception {
        promptCommand.execute(context, "/prompt daily-summary");
        
        assertEquals(1, outputLines.size());
        assertEquals("Summarize today's work:\n", outputLines.get(0));
    }
    
    @Test
    void testExecuteWithInvalidPrompt() throws Exception {
        promptCommand.execute(context, "/prompt nonexistent");
        
        assertEquals(1, outputLines.size());
        assertTrue(outputLines.get(0).startsWith("Error: Unknown prompt 'nonexistent'"));
    }
    
    @Test
    void testExecuteWithNoPromptName() throws Exception {
        promptCommand.execute(context, "/prompt");
        
        assertEquals(1, outputLines.size());
        assertTrue(outputLines.get(0).startsWith("Error: Please specify a prompt name"));
    }
    
    @Test
    void testCompletionEmptyBuffer() {
        List<Candidate> candidates = promptCommand.complete(context, "/prompt ");
        
        assertTrue(candidates.size() >= 2); // At least daily-summary and code-review
        assertTrue(candidates.stream().anyMatch(c -> c.value().contains("daily-summary")));
        assertTrue(candidates.stream().anyMatch(c -> c.value().contains("code-review")));
    }
    
    @Test
    void testCompletionWithPrefix() {
        List<Candidate> candidates = promptCommand.complete(context, "/prompt daily");
        
        assertTrue(candidates.size() >= 1);
        assertTrue(candidates.stream().anyMatch(c -> c.value().contains("daily-summary")));
    }
    
    @Test
    void testCompletionWithPrefixAndTrailingText() {
        List<Candidate> candidates = promptCommand.complete(context, "/prompt daily some text");
        
        assertTrue(candidates.size() >= 1);
        assertTrue(candidates.stream().anyMatch(c -> c.value().contains("daily-summary")));
    }
}
