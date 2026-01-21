package com.example.cli;

import com.example.cli.command.SlashCommandRegistry;
import com.example.cli.completion.ContextAwareCompleter;
import com.example.cli.context.CommandContext;
import com.example.cli.session.CliSession;
import org.jline.reader.Candidate;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.StringsCompleter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = com.example.cli.application.CliApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class CompletionTest {

    @MockBean
    private com.example.cli.repl.ReplLoop replLoop;
    
    @Autowired
    private SlashCommandRegistry registry;
    
    @Autowired
    private CliSession session;
    
    private ContextAwareCompleter completer;
    private CommandContext context;
    
    @BeforeEach
    void setUp() {
        context = new CommandContext(session, null, System.out::println);
        completer = new ContextAwareCompleter(registry, context);
    }
    
    @Test
    void testCompletionForSlashOnly() {
        List<Candidate> candidates = new ArrayList<>();
        DefaultParser parser = new DefaultParser();
        
        completer.complete(null, parser.parse("/", 1), candidates);
        
        assertTrue(candidates.size() >= 3); // exit, help, prompt
        assertTrue(candidates.stream().anyMatch(c -> c.value().equals("/help")));
        assertTrue(candidates.stream().anyMatch(c -> c.value().equals("/exit")));
        assertTrue(candidates.stream().anyMatch(c -> c.value().equals("/prompt")));
    }
    
    @Test
    void testCompletionForPromptCommand() {
        List<Candidate> candidates = new ArrayList<>();
        DefaultParser parser = new DefaultParser();
        
        completer.complete(null, parser.parse("/prompt ", 8), candidates);
        
        assertTrue(candidates.size() >= 2); // daily-summary, code-review
        assertTrue(candidates.stream().anyMatch(c -> c.value().contains("daily-summary")));
        assertTrue(candidates.stream().anyMatch(c -> c.value().contains("code-review")));
    }
    
    @Test
    void testCompletionForPromptWithPrefix() {
        List<Candidate> candidates = new ArrayList<>();
        DefaultParser parser = new DefaultParser();
        
        completer.complete(null, parser.parse("/prompt dai", 11), candidates);
        
        assertTrue(candidates.size() >= 1);
        assertTrue(candidates.stream().anyMatch(c -> c.value().contains("daily-summary")));
    }
    
    @Test
    void testNoCompletionForNonSlashInput() {
        List<Candidate> candidates = new ArrayList<>();
        DefaultParser parser = new DefaultParser();
        
        completer.complete(null, parser.parse("regular text", 12), candidates);
        
        assertEquals(0, candidates.size());
    }
}
