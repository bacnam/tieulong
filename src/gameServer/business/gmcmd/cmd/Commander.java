package business.gmcmd.cmd;

import business.player.Player;
import java.util.HashMap;
import java.util.Map;

public class Commander
{
private String name;
private String comment;
private Map<String, Command> commands;

public Commander(business.gmcmd.annotation.Commander annotation) {
this.name = annotation.name();
this.comment = annotation.comment();
this.commands = new HashMap<>();
}

public String getName() {
return this.name;
}

public String run(Player player, String cmdname, String[] args) throws Exception {
Command command = this.commands.get(cmdname);
if (command == null) {
return "command[" + cmdname + "] not found in commander[" + this.name + "]";
}
return command.run(player, args);
}

public String getHelp() {
StringBuilder sBuilder = new StringBuilder("");
for (Command command : this.commands.values()) {
sBuilder.append(command.toString()).append("\n");
}
return sBuilder.toString();
}

public String toString() {
return String.valueOf(this.name) + " : " + this.comment;
}

public void addCommand(Command command) {
this.commands.put(command.getName(), command);
}
}

