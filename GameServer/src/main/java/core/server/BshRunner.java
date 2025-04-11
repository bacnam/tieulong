package core.server;

import ConsoleTask._AConsoleTaskRunner;
import bsh.Interpreter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BshRunner
        extends _AConsoleTaskRunner {
    private Interpreter it = new Interpreter();

    public void run(String statements) {
        eval(statements);
    }

    public Object eval(String statements) {
        try {
            String filterdCmd = statements.trim().toLowerCase();
            int foundExit = filterdCmd.indexOf("exit");
            if (foundExit >= 0) {
                Pattern pName = Pattern.compile("exit[\\s]?([\\s]?)[\\s]?");
                Matcher matcherName = pName.matcher(filterdCmd);
                if (matcherName.find()) {
                    String res = "exit() not allowed!\n";
                    return res;
                }
            }

            return this.it.eval(statements);
        } catch (Exception ex) {
            this.it.getErr().println(ex);
            return ex;
        }
    }
}

