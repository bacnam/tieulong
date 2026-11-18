package core.server;

import ConsoleTask._AConsoleTaskRunner;
import bsh.Interpreter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BshRunner extends _AConsoleTaskRunner {
    private Interpreter it = new Interpreter();

    @Override
    public void run(String statements) {
        this.eval(statements);
    }

    public Object eval(String statements) {
        try {
            String filterdCmd = statements.trim().toLowerCase();
            int foundExit = filterdCmd.indexOf("exit");
            if (foundExit >= 0) {
                Pattern pName = Pattern.compile("exit[\\s]?([\\s]?)[\\s]?");
                Matcher matcherName = pName.matcher(filterdCmd);
                if (matcherName.find()) {
                    return "exit() not allowed!\n";
                }
            }

            return this.it.eval(statements);
        } catch (Exception var7) {
            this.it.getErr().println(var7);
            return var7;
        }
    }
}
