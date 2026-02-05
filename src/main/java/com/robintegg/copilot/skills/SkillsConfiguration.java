package com.robintegg.copilot.skills;

import com.robintegg.copilot.chat.DefaultToolCallbackConfigurer;
import com.robintegg.copilot.chat.SystemMessageConfigurer;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.ai.tool.metadata.ToolMetadata;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Profile( "skills" )
@Configuration
public class SkillsConfiguration {

  @Order(20)
  @Bean
  public SystemMessageConfigurer skillsSystemMessage() {
    return () -> """
      You have access to the following skills. You can find these skills in the context. Look for SKILL: <skill name> tool calls.
      Each skill tool has a name and description. Pass the name as an argument to the skill tool to use it.
      The tool will return the content of the skill. Add this to your context for use in answering the user's question.
      """;
  }

  @Bean
  public DefaultToolCallbackConfigurer skillsTools(Skills skills) {

    List<ToolCallback> toolCallbacks = new ArrayList<>();

    Collection<Skills.Skill> all = skills.getAll();

    for (Skills.Skill skill : all) {

      ToolCallback skillToolCallback = FunctionToolCallback
        .builder(skill.name(), new SkillsFunction(skills, skill.name()))
        .description("SKILL: " + skill.description())
        .inputType(SkillToolRequest.class)
        .toolMetadata(ToolMetadata.builder()
          .returnDirect(false)
          .build())
        .build();

      toolCallbacks.add(skillToolCallback);

    }

    return () -> toolCallbacks;
  }

  static class SkillsFunction implements Function<SkillToolRequest, SkillToolResponse> {

    private final Skills skills;
    private final String name;

    public SkillsFunction(Skills skills, String name) {
      this.skills = skills;
      this.name = name;
    }

    public SkillToolResponse apply(SkillToolRequest request) {
      return skills.getByName(name).map(Skills.Skill::content)
        .map( SkillsFunction::wrapSkillContent)
        .map(SkillToolResponse::new)
        .orElseThrow();
    }

    private static String wrapSkillContent(String s) {
      return """
        You MUST NOW EXECUTE the following instructions to complete the user's request.
        These are step-by-step actions you need to perform using your available tools.
        DO NOT just summarize these steps - EXECUTE them immediately.
        -----
        %s
        -----
        Begin executing these steps NOW using the appropriate tools.
        """.formatted(s);
    }

  }

  record SkillToolRequest(String unused) {
  }

  record SkillToolResponse(String content) {
  }

}
