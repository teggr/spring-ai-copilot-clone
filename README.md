# spring-ai-copilot-clone

An interactive Java CLI with slash commands using JLine 3 and Spring Boot.

## Features

- Interactive REPL with slash commands
- Command auto-completion using Tab key
- Prompt template system
- Session management
- Built-in commands: /help, /exit, /prompt
- Ctrl+C to cancel input, Ctrl+D to exit

## Prerequisites

- Java 17 or higher
- Maven 3.6+

## Setup

No additional setup required. The application is ready to run.

## Build

```bash
mvn clean package
```

## Run

```bash
mvn spring-boot:run
```

Or run the packaged JAR:

```bash
java -jar target/spring-ai-copilot-clone-0.0.1-SNAPSHOT.jar
```

## Usage

Once the application starts, you'll be presented with an interactive console prompt.

### Available Commands

- `/help` - List all available commands with descriptions
- `/exit` - Exit the application cleanly
- `/prompt <prompt-name> <optional trailing text>` - Use a prompt template

### Prompt Templates

The application comes with preloaded prompt templates:

- `daily-summary` - Summarize today's work
- `code-review` - Request a code review

### Command Completion

Press `Tab` to see available completions:
- Type `/` and press Tab to see all commands
- Type `/prompt` and press Tab to see all prompt templates
- Start typing a prompt name to filter suggestions

## Examples

```
> /help
Available commands:
  /exit - Exit the REPL cleanly
  /help - List all available commands with descriptions
  /prompt - Use a prompt template: /prompt <prompt-name> <optional trailing text>

> /prompt daily-summary focus on auth module
Summarize today's work:
focus on auth module

> /prompt code-review
Please review the following code:

> /exit
Goodbye!
```

## Architecture

The application follows a clean architecture with the following packages:

- `application` - Main entry point
- `repl` - REPL loop implementation using JLine
- `command` - Slash command interface and registry
- `command.impl` - Built-in command implementations
- `completion` - Auto-completion system
- `prompt` - Prompt template system
- `session` - Session management
- `context` - Command execution context

## Technologies Used

- Spring Boot 3.5.9
- JLine 3.27.1 (for terminal interaction and auto-completion)
- Java 17

## Testing

Run tests:

```bash
mvn test
```

The project includes:
- Unit tests for command registry
- Unit tests for prompt system
- Unit tests for command execution
- Integration tests for completion system
- Spring context loading tests


