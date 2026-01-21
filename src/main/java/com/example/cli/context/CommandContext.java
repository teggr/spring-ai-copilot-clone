package com.example.cli.context;

import com.example.cli.session.CliSession;
import org.jline.reader.LineReader;

import java.util.function.Consumer;

public class CommandContext {
    
    private final CliSession session;
    private final LineReader lineReader;
    private final Consumer<String> output;
    
    public CommandContext(CliSession session, LineReader lineReader, Consumer<String> output) {
        this.session = session;
        this.lineReader = lineReader;
        this.output = output;
    }
    
    public CliSession getSession() {
        return session;
    }
    
    public LineReader getLineReader() {
        return lineReader;
    }
    
    public Consumer<String> getOutput() {
        return output;
    }
}
