package com.robintegg.copilot.repl;

import org.jline.reader.Completer;

public interface SlashCommand {

  String name();

  Completer completer();

}
