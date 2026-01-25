package com.robintegg.copilot.chat;

import org.springframework.ai.tool.ToolCallback;

import java.util.List;

public interface DefaultToolCallbackConfigurer {
  List<ToolCallback> toolCallbacks();
}
