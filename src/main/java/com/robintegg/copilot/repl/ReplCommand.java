package com.robintegg.copilot.repl;

import org.jline.reader.Completer;
import org.jline.terminal.Terminal;

public interface ReplCommand {

   Completer completer();

   void dispatch(String line, Terminal terminal);

   boolean canHandle(String line);

}
