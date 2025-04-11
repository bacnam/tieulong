package com.zhonglian.server.common.utils;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.NameSpace;

import java.io.*;
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

    public static void redirectOutputToFile(String filename) {
        Interpreter.redirectOutputToFile(filename);
    }

    public void run() {
        try {
            this.it.run();
        } catch (Throwable ex) {
            this.it.getErr().println(ex);
        }
    }

    public Object source(String filename, NameSpace nameSpace) {
        try {
            return this.it.source(filename, nameSpace);
        } catch (FileNotFoundException ex) {
            this.it.getErr().println(ex);
        } catch (EvalError | IOException ex) {
            this.it.getErr().println(ex);
        }
        return null;
    }

    public Object source(String filename) {
        try {
            return this.it.source(filename);
        } catch (FileNotFoundException ex) {
            this.it.getErr().println(ex);
        } catch (EvalError | IOException ex) {
            this.it.getErr().println(ex);
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
                    String res = "exit() not allowed!\n";
                    return res;
                }
            }

            return this.it.eval(statements);
        } catch (LinkageError | EvalError ex) {
            this.it.getErr().println(ex);
            return ex;
        }
    }

    public Object eval(String statements, NameSpace nameSpace) {
        try {
            return this.it.eval(statements, nameSpace);
        } catch (LinkageError | EvalError ex) {
            this.it.getErr().println(ex);

            return null;
        }
    }

    public Reader getIn() {
        return this.it.getIn();
    }

    public PrintStream getOut() {
        return this.it.getOut();
    }

    public void setOut(PrintStream out) {
        this.it.setOut(out);
    }

    public PrintStream getErr() {
        return this.it.getErr();
    }

    public void setErr(PrintStream err) {
        this.it.setErr(err);
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
        } catch (LinkageError | EvalError ex) {
            this.it.getErr().println(ex);

            return null;
        }
    }

    public void set(String name, Object value) {
        try {
            this.it.set(name, value);
        } catch (LinkageError | EvalError ex) {
            this.it.getErr().println(ex);
        }
    }

    public void set(String name, long value) throws EvalError {
        try {
            this.it.set(name, value);
        } catch (LinkageError | EvalError ex) {
            this.it.getErr().println(ex);
        }
    }

    public void set(String name, int value) throws EvalError {
        try {
            this.it.set(name, value);
        } catch (LinkageError | EvalError ex) {
            this.it.getErr().println(ex);
        }
    }

    public void set(String name, double value) throws EvalError {
        try {
            this.it.set(name, value);
        } catch (LinkageError | EvalError ex) {
            this.it.getErr().println(ex);
        }
    }

    public void set(String name, float value) throws EvalError {
        try {
            this.it.set(name, value);
        } catch (LinkageError | EvalError ex) {
            this.it.getErr().println(ex);
        }
    }

    public void set(String name, boolean value) throws EvalError {
        try {
            this.it.set(name, value);
        } catch (LinkageError | EvalError ex) {
            this.it.getErr().println(ex);
        }
    }

    public void unset(String name) throws EvalError {
        try {
            this.it.unset(name);
        } catch (LinkageError | EvalError ex) {
            this.it.getErr().println(ex);
        }
    }

    public boolean getStrictJava() {
        return this.it.getStrictJava();
    }

    public void setStrictJava(boolean b) {
        this.it.setStrictJava(b);
    }

    public String getSourceFileInfo() {
        return this.it.getSourceFileInfo();
    }

    public boolean getShowResults() {
        return this.it.getShowResults();
    }

    public void setShowResults(boolean showResults) {
        this.it.setShowResults(showResults);
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
            loadCommands(this.scriptPath);
        } catch (LinkageError | EvalError ex) {
            this.it.getErr().print("bsh: import defaults error:" + ex.toString() + "\r\n");
        }
    }

    public void start() {
        stop();

        reloadCommands();

        this.workThread = new CommCmdStringThread((Runnable) this.it);
        this.workThread.start();
    }

    public int loadCommands(String path) {
        try {
            File dirs = new File(path);
            if (dirs.exists()) {
                File[] files = dirs.listFiles(new FileFilter() {
                    public boolean accept(File pathname) {
                        return pathname.getName().endsWith(".bsh");
                    }
                });
                byte b;
                int i;
                File[] arrayOfFile1;
                for (i = (arrayOfFile1 = files).length, b = 0; b < i; ) {
                    File f = arrayOfFile1[b];
                    try {
                        this.it.source(f.getPath());
                    } catch (LinkageError | EvalError e) {
                        this.it.getErr().println(e);
                    }
                    b++;
                }

                return files.length;
            }
        } catch (IOException e) {
            this.it.getErr().println(e);
        }
        return 0;
    }

    public void stop() {
        if (this.workThread != null) {
            try {
                this.it.getIn().close();
            } catch (IOException ex) {
                this.it.getErr().println(ex);
            }
            this.workThread = null;
        }
    }

    class CommCmdStringThread
            extends Thread {
        private Runnable target = null;

        public CommCmdStringThread(Runnable target) {
            super(target, "CommCmdString:bsh");
            this.target = target;
        }

        public CommCmdStringThread(Runnable target, String name) {
            super(target, name);
            this.target = target;
        }

        public void run() {
            while (true) {
                try {
                    if (this.target != null) {
                        this.target.run();
                    }

                    break;
                } catch (LinkageError ex) {
                    CommCmdString.this.it.getErr().println(ex);
                } catch (Throwable ex) {
                    CommCmdString.this.it.getErr().println(ex);
                    throw ex;
                }
            }
        }
    }
}

