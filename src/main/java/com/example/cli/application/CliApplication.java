package com.example.cli.application;

import com.example.cli.repl.ReplLoop;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.cli")
public class CliApplication implements CommandLineRunner {
    
    private final ReplLoop replLoop;
    
    public CliApplication(ReplLoop replLoop) {
        this.replLoop = replLoop;
    }
    
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CliApplication.class);
        app.setWebApplicationType(org.springframework.boot.WebApplicationType.NONE);
        app.run(args);
    }
    
    @Override
    public void run(String... args) throws Exception {
        replLoop.start();
    }
}
