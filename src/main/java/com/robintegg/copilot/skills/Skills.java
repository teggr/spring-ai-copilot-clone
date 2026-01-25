package com.robintegg.copilot.skills;

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
public class Skills {

  private static final Logger logger = LoggerFactory.getLogger(Skills.class);
  private static final String SKILLS_DIR = ".examples/.github/skills";
  private static final Pattern FRONTMATTER_PATTERN = Pattern.compile("^---\\s*\\n(.*?)\\n---\\s*\\n(.*)$", Pattern.DOTALL);
  private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("^description:\\s*(.+)$", Pattern.MULTILINE);

  private final Map<String, Skill> skillsMap;

  public Skills() {
    this.skillsMap = new HashMap<>();
    loadSkills();
  }

  private void loadSkills() {
    File skillsDirectory = new File(SKILLS_DIR);

    if (!skillsDirectory.exists() || !skillsDirectory.isDirectory()) {
      throw new RuntimeException("Skills directory does not exist: " + SKILLS_DIR);
    }

    File[] subdirectories = skillsDirectory.listFiles(File::isDirectory);

    if (subdirectories == null || subdirectories.length == 0) {
      throw new RuntimeException("No skill directories found in: " + SKILLS_DIR);
    }

    for (File skillDir : subdirectories) {
      File skillFile = new File(skillDir, "SKILL.md");

      if (!skillFile.exists()) {
        throw new RuntimeException("SKILL.md not found in directory: " + skillDir.getAbsolutePath());
      }

      try {
        String fileContent = IOUtils.toString(new FileInputStream(skillFile));
        Skill skill = parseSkillFile(fileContent, skillDir.getName());
        skillsMap.put(skill.name(), skill);
        logger.info("Loaded skill: {} - {}", skill.name(), skill.description());
      } catch (IOException e) {
        throw new RuntimeException("Failed to read skill file: " + skillFile.getAbsolutePath(), e);
      }
    }

    logger.info("Successfully loaded {} skill(s)", skillsMap.size());
  }

  private Skill parseSkillFile(String fileContent, String directoryName) {
    Matcher frontmatterMatcher = FRONTMATTER_PATTERN.matcher(fileContent);

    if (!frontmatterMatcher.matches()) {
      throw new RuntimeException("Invalid SKILL.md format - missing or malformed frontmatter in directory: " + directoryName);
    }

    String frontmatter = frontmatterMatcher.group(1);
    String content = frontmatterMatcher.group(2).trim();

    Matcher descriptionMatcher = DESCRIPTION_PATTERN.matcher(frontmatter);

    if (!descriptionMatcher.find()) {
      throw new RuntimeException("Missing 'description' field in frontmatter for directory: " + directoryName);
    }

    String description = descriptionMatcher.group(1).trim();

    if (description.isEmpty()) {
      throw new RuntimeException("Empty 'description' field in frontmatter for directory: " + directoryName);
    }

    return new Skill(directoryName, description, content);
  }

  public Collection<Skill> getAll() {
    return Collections.unmodifiableCollection(skillsMap.values());
  }

  public Optional<Skill> getByName(String name) {
    return Optional.ofNullable(skillsMap.get(name));
  }

  public record Skill(String name, String description, String content) {
  }

}
