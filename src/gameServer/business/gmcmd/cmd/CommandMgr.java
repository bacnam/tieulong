package business.gmcmd.cmd;

import BaseCommon.CommClass;
import BaseCommon.CommLog;
import business.gmcmd.annotation.Command;
import business.gmcmd.annotation.Commander;
import business.player.Player;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandMgr
{
private Map<String, Commander> commanders = new HashMap<>();

private static CommandMgr _instance = new CommandMgr();

public static CommandMgr getInstance() {
return _instance;
}

public void init() {
Set<Class<?>> clazzs = CommClass.getClasses("business.gmcmd.cmds");
for (Class<?> clazz : clazzs) {
Commander aCommander = clazz.<Commander>getAnnotation(Commander.class);
if (aCommander == null) {
continue;
}
Object excuter = null;
try {
excuter = clazz.newInstance();
} catch (Exception e) {
continue;
} 
Commander commander = new Commander(aCommander); byte b; int i; Method[] arrayOfMethod;
for (i = (arrayOfMethod = clazz.getMethods()).length, b = 0; b < i; ) { Method m = arrayOfMethod[b];
Command aCommand = m.<Command>getAnnotation(Command.class);
if (aCommand != null) {

Command command = new Command(aCommand, m, excuter);
commander.addCommand(command);
}  b++; }
this.commanders.put(commander.getName().toLowerCase(), commander);
} 
}

public String run(Player player, String cmdline) {
if (cmdline == null) {
return "null command";
}
cmdline = cmdline.trim();
if (cmdline.isEmpty() || cmdline.equalsIgnoreCase("help")) {
return getHelp();
}
String[] args = cmdline.split("\\s+");
Commander commander = this.commanders.get(args[0].toLowerCase());
if (commander == null) {
return "commander[" + args[0] + "] not found";
}
if (args.length < 2) {
return commander.getHelp();
}
String cmd = args[1].toLowerCase();
if (cmd.equalsIgnoreCase("help")) {
return commander.getHelp();
}
try {
return commander.run(player, cmd, Arrays.<String>copyOfRange(args, 2, args.length));
} catch (Exception e) {
CommLog.warn("error occurs while running command:{}", cmdline, e);
return "error occurs while running command";
} 
}

private String getHelp() {
StringBuilder sBuilder = new StringBuilder("");
for (Commander commander : this.commanders.values()) {
sBuilder.append(commander.toString()).append("\n");
}
return sBuilder.toString();
}
}

