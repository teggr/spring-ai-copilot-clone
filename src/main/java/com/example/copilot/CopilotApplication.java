package com.example.copilot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
public class CopilotApplication implements CommandLineRunner {

    private final ApplicationContext applicationContext;
    private final ChatSession chatSession;
    private final CopilotCommands copilotCommands;

    public CopilotApplication(ApplicationContext applicationContext, 
                             ChatSession chatSession,
                             CopilotCommands copilotCommands) {
        this.applicationContext = applicationContext;
        this.chatSession = chatSession;
        this.copilotCommands = copilotCommands;
    }

    public static void main(String[] args) {
        SpringApplication.run(CopilotApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== Spring AI Copilot Clone ===");
        System.out.println("Type your message to chat with AI");
        System.out.println("Commands start with '/' (e.g., /help, /exit)");
        System.out.println("Shell commands start with '!' (e.g., !ls, !pwd)");
        System.out.println();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        CommandLine commandLine = new CommandLine(copilotCommands);

        while (true) {
            System.out.print("> ");
            String input = reader.readLine();

            if (input == null) {
                // EOF reached (Ctrl+D)
                break;
            }

            input = input.trim();

            if (input.isEmpty()) {
                continue;
            }

            try {
                if (input.startsWith("/")) {
                    // Handle Picocli command
                    String commandInput = input.substring(1);
                    
                    if (commandInput.equals("exit")) {
                        System.out.println("Goodbye!");
                        break;
                    }
                    
                    // Parse and execute the command directly
                    String[] commandArgs = parseCommandLine(commandInput);
                    if (commandArgs.length == 0) {
                        continue;
                    }
                    
                    String commandName = commandArgs[0];
                    String result = executeCommand(commandName, commandArgs);
                    
                    if (result != null && !result.isEmpty()) {
                        System.out.println(result);
                    }
                    
                } else if (input.startsWith("!")) {
                    // Handle shell command
                    String shellCommand = input.substring(1).trim();
                    if (shellCommand.isEmpty()) {
                        System.out.println("Error: No shell command provided.");
                        continue;
                    }
                    executeShellCommand(shellCommand);
                    
                } else {
                    // Regular message - add to chat session
                    String response = chatSession.addMessage(input);
                    System.out.println(response);
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        
        System.exit(0);
    }

    private String[] parseCommandLine(String commandLine) {
        // Simple command line parser that handles quoted strings
        java.util.List<String> args = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < commandLine.length(); i++) {
            char c = commandLine.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ' ' && !inQuotes) {
                if (current.length() > 0) {
                    args.add(current.toString());
                    current = new StringBuilder();
                }
            } else {
                current.append(c);
            }
        }
        
        if (current.length() > 0) {
            args.add(current.toString());
        }
        
        return args.toArray(new String[0]);
    }

    private String executeCommand(String commandName, String[] args) {
        try {
            switch (commandName) {
                case "ask":
                    if (args.length < 2) {
                        return "Error: Please provide a question.";
                    }
                    // Join all arguments after 'ask' as the question
                    String question = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
                    return copilotCommands.ask(question);
                    
                case "clear":
                    return copilotCommands.clear();
                    
                case "history":
                    return copilotCommands.history();
                    
                case "help":
                    return copilotCommands.help();
                    
                default:
                    return "Unknown command: " + commandName + ". Type /help for available commands.";
            }
        } catch (Exception e) {
            return "Error executing command: " + e.getMessage();
        }
    }

    private void executeShellCommand(String command) {
        try {
            ProcessBuilder processBuilder;
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("win")) {
                processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
            } else {
                processBuilder = new ProcessBuilder("sh", "-c", command);
            }
            
            processBuilder.inheritIO(); // This allows interactive commands
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            
            if (exitCode != 0) {
                System.out.println("Command exited with code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error executing shell command: " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
