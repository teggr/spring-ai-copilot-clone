package com.robintegg.copilot.prompts;

import com.robintegg.copilot.agents.MainAgent;
import com.robintegg.copilot.repl.ReplCommand;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomPromptsConfiguration implements BeanDefinitionRegistryPostProcessor {

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
    // This will be called after all beans are defined but before instantiation
    // We'll register prompt command beans here
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    Prompts prompts = beanFactory.getBean(Prompts.class);
    MainAgent mainAgent = beanFactory.getBean(MainAgent.class);

    for (Prompts.Prompt prompt : prompts.getAll()) {
      String commandName = "/" + prompt.name();
      
      ReplCommand command = new ReplCommand() {

        @Override
        public Completer completer() {
          return new StringsCompleter(commandName);
        }

        @Override
        public void dispatch(String line, Terminal terminal) {
          String response = mainAgent.send(prompt.content());
          terminal.writer().println(response);
          terminal.writer().flush();
        }

        @Override
        public boolean canHandle(String line) {
          return line.trim().startsWith(commandName);
        }

      };
      
      beanFactory.registerSingleton("promptCommand_" + prompt.name(), command);
    }
  }

}
