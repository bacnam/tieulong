package business.gmcmd.cmd;

import java.util.HashMap;
import java.util.Map;

public class Commander {
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

    public String run(long player, String cmdname, String[] args) throws Exception {
        Command command = this.commands.get(cmdname);
        return command == null ? "command[" + cmdname + "] not found in commander[" + this.name + "]" : command.run(player, args);
    }

    public String getHelp() {
        StringBuilder sBuilder = new StringBuilder("");

        for (Command command : this.commands.values()) {
            sBuilder.append(command.toString()).append("\n");
        }

        return sBuilder.toString();
    }

    @Override
    public String toString() {
        return this.name + " : " + this.comment;
    }

    public void addCommand(Command command) {
        this.commands.put(command.getName(), command);
    }
}
