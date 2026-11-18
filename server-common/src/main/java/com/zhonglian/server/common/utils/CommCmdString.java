package com.zhonglian.server.common.utils;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.NameSpace;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommCmdString {
    private final Interpreter it;
    private Thread workThread;
    private String defaultImports = "import com.mg.server.common.conf.*; import com.mg.server.common.utils.*; import com.mg.server.common.mgr.*; import com.mg.server.common.db.*; import com.mg.server.common.db.game.*; import com.mg.server.common.db.game.bo.*; import com.mg.server.common.db.game.create.*; import com.mg.server.common.db.log.*; import com.mg.server.common.db.log.create.*; import com.mg.server.common.data.ref.*; import com.mg.server.utils.*; import com.mg.server.utils.collections.*; import com.mg.server.common.async.*;import com.mg.server.framework.*; import com.mg.server.framework.telnet.*; import com.mg.server.framework.session.*; import com.mg.server.framework.protocol.*;";
    private String scriptPath = "";

    public CommCmdString(boolean interactive) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        this.it = new Interpreter(br, System.out, System.err, interactive);
    }

    public CommCmdString(Reader in, PrintStream out, PrintStream err, boolean interactive, NameSpace namespace) {
        this.it = new Interpreter(in, out, err, interactive, namespace);
    }

    public void run() {
        try {
            this.it.run();
        } catch (Throwable var2) {
            this.it.getErr().println(var2);
        }
    }

    public Object source(String filename, NameSpace nameSpace) {
        try {
            return this.it.source(filename, nameSpace);
        } catch (FileNotFoundException var4) {
            this.it.getErr().println(var4);
        } catch (IOException | EvalError var5) {
            this.it.getErr().println(var5);
        }

        return null;
    }

    public Object source(String filename) {
        try {
            return this.it.source(filename);
        } catch (FileNotFoundException var3) {
            this.it.getErr().println(var3);
        } catch (IOException | EvalError var4) {
            this.it.getErr().println(var4);
        }

        return null;
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
        } catch (EvalError | LinkageError var7) {
            this.it.getErr().println(var7);
            return var7;
        }
    }

    public Object eval(String statements, NameSpace nameSpace) {
        try {
            return this.it.eval(statements, nameSpace);
        } catch (EvalError | LinkageError var4) {
            this.it.getErr().println(var4);
            return null;
        }
    }

    public Reader getIn() {
        return this.it.getIn();
    }

    public PrintStream getOut() {
        return this.it.getOut();
    }

    public PrintStream getErr() {
        return this.it.getErr();
    }

    public final void println(Object o) {
        this.it.println(o);
    }

    public final void print(Object o) {
        this.it.print(o);
    }

    public Object get(String name) {
        try {
            return this.it.get(name);
        } catch (EvalError | LinkageError var3) {
            this.it.getErr().println(var3);
            return null;
        }
    }

    public void set(String name, Object value) {
        try {
            this.it.set(name, value);
        } catch (EvalError | LinkageError var4) {
            this.it.getErr().println(var4);
        }
    }

    public void set(String name, long value) throws EvalError {
        try {
            this.it.set(name, value);
        } catch (EvalError | LinkageError var5) {
            this.it.getErr().println(var5);
        }
    }

    public void set(String name, int value) throws EvalError {
        try {
            this.it.set(name, value);
        } catch (EvalError | LinkageError var4) {
            this.it.getErr().println(var4);
        }
    }

    public void set(String name, double value) throws EvalError {
        try {
            this.it.set(name, value);
        } catch (EvalError | LinkageError var5) {
            this.it.getErr().println(var5);
        }
    }

    public void set(String name, float value) throws EvalError {
        try {
            this.it.set(name, value);
        } catch (EvalError | LinkageError var4) {
            this.it.getErr().println(var4);
        }
    }

    public void set(String name, boolean value) throws EvalError {
        try {
            this.it.set(name, value);
        } catch (EvalError | LinkageError var4) {
            this.it.getErr().println(var4);
        }
    }

    public void unset(String name) throws EvalError {
        try {
            this.it.unset(name);
        } catch (EvalError | LinkageError var3) {
            this.it.getErr().println(var3);
        }
    }

    public static void redirectOutputToFile(String filename) {
        Interpreter.redirectOutputToFile(filename);
    }

    public void setStrictJava(boolean b) {
        this.it.setStrictJava(b);
    }

    public boolean getStrictJava() {
        return this.it.getStrictJava();
    }

    public String getSourceFileInfo() {
        return this.it.getSourceFileInfo();
    }

    public void setOut(PrintStream out) {
        this.it.setOut(out);
    }

    public void setErr(PrintStream err) {
        this.it.setErr(err);
    }

    public void setShowResults(boolean showResults) {
        this.it.setShowResults(showResults);
    }

    public boolean getShowResults() {
        return this.it.getShowResults();
    }

    public String getDefaultImports() {
        return this.defaultImports;
    }

    public void setDefaultImports(String defaultImports) {
        this.defaultImports = defaultImports;
    }

    public void setScriptPath(String scripts) {
        this.scriptPath = scripts;
    }

    public void reloadCommands() {
        try {
            if (this.defaultImports != null && !this.defaultImports.isEmpty()) {
                this.it.getOut().print("bsh: " + this.defaultImports + "\r\n");
                this.it.eval(this.defaultImports);
                this.it.set("cmdline", this);
            }

            this.loadCommands(this.scriptPath);
        } catch (EvalError | LinkageError var2) {
            this.it.getErr().print("bsh: import defaults error:" + var2.toString() + "\r\n");
        }
    }

    public void start() {
        this.stop();
        this.reloadCommands();
        this.workThread = new CommCmdString.CommCmdStringThread(this.it);
        this.workThread.start();
    }

    public int loadCommands(String path) {
        try {
            File dirs = new File(path);
            if (dirs.exists()) {
                File[] files = dirs.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getName().endsWith(".bsh");
                    }
                });

                for (File f : files) {
                    try {
                        this.it.source(f.getPath());
                    } catch (EvalError | LinkageError var9) {
                        this.it.getErr().println(var9);
                    }
                }

                return files.length;
            }
        } catch (IOException var10) {
            this.it.getErr().println(var10);
        }

        return 0;
    }

    public void stop() {
        if (this.workThread != null) {
            try {
                this.it.getIn().close();
            } catch (IOException var2) {
                this.it.getErr().println(var2);
            }

            this.workThread = null;
        }
    }

    class CommCmdStringThread extends Thread {
        private Runnable target = null;

        public CommCmdStringThread(Runnable target) {
            super(target, "CommCmdString:bsh");
            this.target = target;
        }

        public CommCmdStringThread(Runnable target, String name) {
            super(target, name);
            this.target = target;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    if (this.target != null) {
                        this.target.run();
                    }

                    return;
                } catch (LinkageError var2) {
                    CommCmdString.this.it.getErr().println(var2);
                } catch (Throwable var3) {
                    CommCmdString.this.it.getErr().println(var3);
                    throw var3;
                }
            }
        }
    }
}
