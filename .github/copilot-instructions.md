# spring-ai-copilot-clone

We are building a github copilot clone using spring boot ai. It is intended to help developers learn about the inner workings of github copilot cli and understand how the features are implemented.

# Call out technologies

* Java 25
* Spring Boot 3.5
* Spring AI 1.1.2
* JLine 3.30.0

# HLD

Using Jline we have a REPL to allow the user to either submit text to the AI model or use commands to control the behavior of the application. The commands are prefixed with a `/` character.

