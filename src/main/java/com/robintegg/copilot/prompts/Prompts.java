package com.robintegg.copilot.prompts;

import io.micrometer.core.instrument.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Component
public class Prompts {

  private static final Logger logger = LoggerFactory.getLogger(Prompts.class);
  private static final String PROMPTS_DIR = ".examples/.github/prompts";

  private final Map<String, Prompt> promptsMap;

  public Prompts() {
    this.promptsMap = new HashMap<>();
    loadPrompts();
  }

  private void loadPrompts() {
    File promptsDirectory = new File(PROMPTS_DIR);

    if (!promptsDirectory.exists() || !promptsDirectory.isDirectory()) {
      logger.warn("Prompts directory does not exist: " + PROMPTS_DIR);
      return;
    }

    File[] promptFiles = promptsDirectory.listFiles((dir, name) -> name.endsWith(".prompt.md"));

    if (promptFiles == null || promptFiles.length == 0) {
      logger.warn("No prompt files found in: " + PROMPTS_DIR);
      return;
    }

    for (File promptFile : promptFiles) {
      try {
        String fileContent = IOUtils.toString(new FileInputStream(promptFile));
        String fileName = promptFile.getName();
        String commandName = extractCommandName(fileName);
        Prompt prompt = new Prompt(commandName, fileContent, promptFile);
        promptsMap.put(commandName, prompt);
        logger.info("Loaded prompt: {} from {}", commandName, fileName);
      } catch (IOException e) {
        throw new RuntimeException("Failed to read prompt file: " + promptFile.getAbsolutePath(), e);
      }
    }

    logger.info("Successfully loaded {} prompt(s)", promptsMap.size());
  }

  private String extractCommandName(String fileName) {
    // Remove .prompt.md extension to get command name
    // e.g., "fun-fact.prompt.md" -> "fun-fact"
    if (fileName.endsWith(".prompt.md")) {
      return fileName.substring(0, fileName.length() - ".prompt.md".length());
    }
    return fileName;
  }

  public Collection<Prompt> getAll() {
    return Collections.unmodifiableCollection(promptsMap.values());
  }

  public Optional<Prompt> getByName(String name) {
    return Optional.ofNullable(promptsMap.get(name));
  }

  public record Prompt(String name, String content, File file) {
  }

}
