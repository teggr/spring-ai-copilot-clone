package com.robintegg.copilot.agents;

import io.micrometer.core.instrument.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Agents {

  private static final Logger logger = LoggerFactory.getLogger(Agents.class);
  private static final String AGENTS_DIR = ".examples/.github/agents";
  private static final Pattern FRONTMATTER_PATTERN = Pattern.compile("^---\\s*\\n(.*?)\\n---\\s*\\n(.*)$", Pattern.DOTALL);
  private static final Pattern NAME_PATTERN = Pattern.compile("^name:\\s*(.+)$", Pattern.MULTILINE);
  private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("^description:\\s*(.+)$", Pattern.MULTILINE);

  private final Map<String, Agent> agentsMap;

  public Agents() {
    this.agentsMap = new HashMap<>();
    loadAgents();
  }

  private void loadAgents() {
    File agentsDirectory = new File(AGENTS_DIR);

    if (!agentsDirectory.exists() || !agentsDirectory.isDirectory()) {
      logger.warn("Agents directory does not exist: " + AGENTS_DIR);
      return;
    }

    File[] agentFiles = agentsDirectory.listFiles((dir, name) -> name.endsWith(".agent.md"));

    if (agentFiles == null || agentFiles.length == 0) {
      logger.warn("No agent files found in: " + AGENTS_DIR);
      return;
    }

    for (File agentFile : agentFiles) {
      try {
        String fileContent = IOUtils.toString(new FileInputStream(agentFile));
        Agent agent = parseAgentFile(fileContent, agentFile.getName());
        agentsMap.put(agent.name(), agent);
        logger.info("Loaded agent: {} - {}", agent.name(), agent.description());
      } catch (IOException e) {
        throw new RuntimeException("Failed to read agent file: " + agentFile.getAbsolutePath(), e);
      }
    }

    logger.info("Successfully loaded {} agent(s)", agentsMap.size());
  }

  private Agent parseAgentFile(String fileContent, String fileName) {
    Matcher frontmatterMatcher = FRONTMATTER_PATTERN.matcher(fileContent);

    if (!frontmatterMatcher.matches()) {
      throw new RuntimeException("Invalid agent file format - missing or malformed frontmatter in file: " + fileName);
    }

    String frontmatter = frontmatterMatcher.group(1);
    String content = frontmatterMatcher.group(2).trim();

    Matcher nameMatcher = NAME_PATTERN.matcher(frontmatter);
    if (!nameMatcher.find()) {
      throw new RuntimeException("Missing 'name' field in frontmatter for file: " + fileName);
    }
    String name = nameMatcher.group(1).trim();

    if (name.isEmpty()) {
      throw new RuntimeException("Empty 'name' field in frontmatter for file: " + fileName);
    }

    Matcher descriptionMatcher = DESCRIPTION_PATTERN.matcher(frontmatter);
    if (!descriptionMatcher.find()) {
      throw new RuntimeException("Missing 'description' field in frontmatter for file: " + fileName);
    }
    String description = descriptionMatcher.group(1).trim();

    if (description.isEmpty()) {
      throw new RuntimeException("Empty 'description' field in frontmatter for file: " + fileName);
    }

    return new Agent(name, description, content);
  }

  public Collection<Agent> getAll() {
    return Collections.unmodifiableCollection(agentsMap.values());
  }

  public Optional<Agent> getByName(String name) {
    return Optional.ofNullable(agentsMap.get(name));
  }

  public record Agent(String name, String description, String content) {
  }

}
