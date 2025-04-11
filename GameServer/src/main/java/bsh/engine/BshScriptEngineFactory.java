package bsh.engine;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import java.util.Arrays;
import java.util.List;

public class BshScriptEngineFactory implements ScriptEngineFactory {
    final List<String> extensions = Arrays.asList(new String[]{"bsh", "java"});

    final List<String> mimeTypes = Arrays.asList(new String[]{"application/x-beanshell", "application/x-bsh", "application/x-java-source"});

    final List<String> names = Arrays.asList(new String[]{"beanshell", "bsh", "java"});

    public String getEngineName() {
        return "BeanShell Engine";
    }

    public String getEngineVersion() {
        return "1.0";
    }

    public List<String> getExtensions() {
        return this.extensions;
    }

    public List<String> getMimeTypes() {
        return this.mimeTypes;
    }

    public List<String> getNames() {
        return this.names;
    }

    public String getLanguageName() {
        return "BeanShell";
    }

    public String getLanguageVersion() {
        return "2.0b5";
    }

    public Object getParameter(String param) {
        if (param.equals("javax.script.engine"))
            return getEngineName();
        if (param.equals("javax.script.engine_version"))
            return getEngineVersion();
        if (param.equals("javax.script.name"))
            return getEngineName();
        if (param.equals("javax.script.language"))
            return getLanguageName();
        if (param.equals("javax.script.language_version"))
            return getLanguageVersion();
        if (param.equals("THREADING")) {
            return "MULTITHREADED";
        }
        return null;
    }

    public String getMethodCallSyntax(String objectName, String methodName, String... args) {
        StringBuffer sb = new StringBuffer();
        if (objectName != null)
            sb.append(objectName + ".");
        sb.append(methodName + "(");
        if (args.length > 0)
            sb.append(" ");
        for (int i = 0; i < args.length; i++) {
            sb.append(((args[i] == null) ? "null" : args[i]) + ((i < args.length - 1) ? ", " : " "));
        }
        sb.append(")");
        return sb.toString();
    }

    public String getOutputStatement(String message) {
        return "print( \"" + message + "\" );";
    }

    public String getProgram(String... statements) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < statements.length; i++) {

            sb.append(statements[i]);
            if (!statements[i].endsWith(";"))
                sb.append(";");
            sb.append("\n");
        }
        return sb.toString();
    }

    public ScriptEngine getScriptEngine() {
        // return new BshScriptEngine();
        return null;
    }
}
