# spring-ai-copilot-clone

A Github copilot CLI clone using Spring AI, implementing an interactive console with AI chat capabilities.

## Features

- Interactive console interface with command support
- Continuous conversation with AI maintaining chat history
- Integration with OpenAI's GPT models via Spring AI
- Command-line based question and answer interface
- Shell command execution support

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- OpenAI API key

## Setup

1. Set your OpenAI API key as an environment variable:

```bash
export OPENAI_API_KEY=your-api-key-here
```

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

### Interactive Mode

- **Plain text input**: Add messages to the current chat session (maintains conversation history)
  ```
  > What is Spring Boot?
  ```

- **Commands (prefix with `/`)**: Execute application commands
  ```
  > /ask "What is Spring Boot?"
  > /clear
  > /history
  > /help
  > /exit
  ```

- **Shell commands (prefix with `!`)**: Execute system shell commands
  ```
  > !ls
  > !pwd
  > !echo "Hello World"
  ```

## Commands

- `/ask <question>` - Ask a single question (clears conversation history first)
- `/clear` - Clear the conversation history
- `/history` - Show information about the conversation history
- `/help` - Display help information
- `/exit` - Exit the application

## Technologies Used

- Spring Boot 3.5.9
- Picocli 4.7.5 (for command parsing)
- Spring AI 1.1.2
- OpenAI GPT-4o-mini

## Examples

```
> Hello, how are you?
I'm doing well, thank you for asking! How can I help you today?

> What is Java?
Java is a high-level, class-based, object-oriented programming language...

> /clear
Conversation history cleared.

> /ask "What is Python?"
Python is an interpreted, high-level programming language...

> !date
Tue Jan 21 16:30:00 UTC 2026

> /exit
Goodbye!
```

