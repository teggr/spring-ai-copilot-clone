package com.example.copilot;

import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Component
@Command(name = "", description = "Copilot CLI commands")
public class CopilotCommands {

    private final ChatSession chatSession;

    public CopilotCommands(ChatSession chatSession) {
        this.chatSession = chatSession;
    }

    @Command(name = "ask", description = "Ask a single question to the AI assistant")
    public String ask(@Parameters(description = "Your question") String question) {
        if (question == null || question.trim().isEmpty()) {
            return "Error: Please provide a question.";
        }
        
        // Clear history and ask a single question
        chatSession.clearHistory();
        return chatSession.addMessage(question);
    }

    @Command(name = "clear", description = "Clear the conversation history")
    public String clear() {
        chatSession.clearHistory();
        return "Conversation history cleared.";
    }

    @Command(name = "history", description = "Show the conversation history")
    public String history() {
        int count = chatSession.getMessageCount();
        if (count == 0) {
            return "No conversation history.";
        }
        return "Conversation has " + count + " messages.";
    }

    @Command(name = "exit", description = "Exit the application")
    public int exit() {
        System.out.println("Goodbye!");
        return 0;
    }

    @Command(name = "help", description = "Display help information")
    public String help() {
        return """
                Available commands:
                  /ask <question>  - Ask a single question (clears history)
                  /clear           - Clear conversation history
                  /history         - Show conversation history info
                  /help            - Display this help message
                  /exit            - Exit the application
                  
                Interactive mode:
                  <text>           - Add message to conversation (maintains history)
                  /<command>       - Execute a command
                  !<shell-command> - Execute a shell command
                """;
    }
}
