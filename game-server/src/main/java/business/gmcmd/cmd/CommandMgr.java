package business.gmcmd.cmd;

import BaseCommon.CommClass;
import BaseCommon.CommLog;
import business.player.Player;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CommandMgr {
    private Map<String, Commander> commanders = new HashMap<>();
    private static CommandMgr _instance = new CommandMgr();

    public static CommandMgr getInstance() {
        return _instance;
    }

    public void init() {
        Set<Class<?>> clazzs = CommClass.getClasses("business.gmcmd.cmds");
        Iterator var3 = clazzs.iterator();

        while (true) {
            Class<?> clazz;
            business.gmcmd.annotation.Commander aCommander;
            Object excuter;
            while (true) {
                if (!var3.hasNext()) {
                    return;
                }

                clazz = (Class<?>)var3.next();
                aCommander = clazz.getAnnotation(business.gmcmd.annotation.Commander.class);
                if (aCommander != null) {
                    excuter = null;

                    try {
                        excuter = clazz.newInstance();
                        break;
                    } catch (Exception var13) {
                    }
                }
            }

            Commander commander = new Commander(aCommander);

            Method[] var10;
            for (Method m : var10 = clazz.getMethods()) {
                business.gmcmd.annotation.Command aCommand = m.getAnnotation(business.gmcmd.annotation.Command.class);
                if (aCommand != null) {
                    Command command = new Command(aCommand, m, excuter);
                    commander.addCommand(command);
                }
            }

            this.commanders.put(commander.getName().toLowerCase(), commander);
        }
    }

    public String run(Player player, String cmdline) {
        if (cmdline == null) {
            return "null command";
        } else {
            cmdline = cmdline.trim();
            if (!cmdline.isEmpty() && !cmdline.equalsIgnoreCase("help")) {
                String[] args = cmdline.split("\\s+");
                Commander commander = this.commanders.get(args[0].toLowerCase());
                if (commander == null) {
                    return "commander[" + args[0] + "] not found";
                } else if (args.length < 2) {
                    return commander.getHelp();
                } else {
                    String cmd = args[1].toLowerCase();
                    if (cmd.equalsIgnoreCase("help")) {
                        return commander.getHelp();
                    } else {
                        try {
                            return commander.run(player, cmd, Arrays.copyOfRange(args, 2, args.length));
                        } catch (Exception var7) {
                            CommLog.warn("error occurs while running command:{}", cmdline, var7);
                            return "error occurs while running command";
                        }
                    }
                }
            } else {
                return this.getHelp();
            }
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
