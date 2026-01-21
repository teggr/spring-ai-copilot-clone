# spring-ai-copilot-clone

A Github copilot CLI clone using Spring AI, implementing the features to better understand the logic and implementation of Github copilot.

## Features

- Interactive shell interface powered by Spring Shell
- Integration with OpenAI's GPT models via Spring AI
- Command-line based question and answer interface

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

Once the application starts, you'll be presented with an interactive shell prompt. Use the `ask` command to ask questions:

```
shell:>ask "What is Spring Boot?"
```

The AI will process your question and return a response.

## Commands

- `ask <question>` - Ask a question to the AI assistant
- `help` - Show available commands
- `exit` - Exit the application

## Technologies Used

- Spring Boot 3.4.1
- Spring Shell 3.3.3
- Spring AI 1.0.0-M5
- OpenAI GPT-4o-mini

