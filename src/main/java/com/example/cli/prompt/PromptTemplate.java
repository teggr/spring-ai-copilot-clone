package com.example.cli.prompt;

public class PromptTemplate {
    
    private final String name;
    private final String description;
    private final String body;
    
    public PromptTemplate(String name, String description, String body) {
        this.name = name;
        this.description = description;
        this.body = body;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getBody() {
        return body;
    }
}
