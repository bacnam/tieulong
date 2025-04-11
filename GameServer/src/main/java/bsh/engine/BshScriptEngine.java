package bsh.engine;

import bsh.*;

import javax.script.*;
import java.io.*;
import java.util.Map;

public class BshScriptEngine extends AbstractScriptEngine implements Compilable, Invocable {
    static final String engineNameSpaceKey = "org_beanshell_engine_namespace";
    private BshScriptEngineFactory factory;
    private Interpreter interpreter;

    public BshScriptEngine() {
        this(null);
    }

    public BshScriptEngine(BshScriptEngineFactory factory) {
        this.factory = factory;
        getInterpreter();
    }

    private static NameSpace getEngineNameSpace(ScriptContext scriptContext) {
        NameSpace ns = (NameSpace) scriptContext.getAttribute("org_beanshell_engine_namespace", 100);

        if (ns == null) {
            Map engineView = new ScriptContextEngineView(scriptContext);
            ns = new ExternalNameSpace(null, "javax_script_context", engineView);

            scriptContext.setAttribute("org_beanshell_engine_namespace", ns, 100);
        }

        return ns;
    }

    @Override
    public Object invokeMethod(Object thiz, String name, Object... args) throws ScriptException, NoSuchMethodException {
        // TODO: Implement logic to invoke method on script object
        throw new UnsupportedOperationException("invokeMethod is not implemented yet.");
    }

    @Override
    public Object invokeFunction(String name, Object... args) throws ScriptException, NoSuchMethodException {
        // TODO: Implement logic to invoke global function from script
        throw new UnsupportedOperationException("invokeFunction is not implemented yet.");
    }

    protected Interpreter getInterpreter() {
        if (this.interpreter == null) {
            this.interpreter = new Interpreter();
            this.interpreter.setNameSpace(null);
        }

        return this.interpreter;
    }

    public Object eval(String script, ScriptContext scriptContext) throws ScriptException {
        return evalSource(script, scriptContext);
    }

    public Object eval(Reader reader, ScriptContext scriptContext) throws ScriptException {
        return evalSource(reader, scriptContext);
    }

    private Object evalSource(Object source, ScriptContext scriptContext) throws ScriptException {
        NameSpace contextNameSpace = getEngineNameSpace(scriptContext);
        Interpreter bsh = getInterpreter();
        bsh.setNameSpace(contextNameSpace);

        bsh.setOut(new PrintStream(new WriterOutputStream(scriptContext.getWriter())));

        bsh.setErr(new PrintStream(new WriterOutputStream(scriptContext.getErrorWriter())));

        try {
            if (source instanceof Reader) {
                return bsh.eval((Reader) source);
            }
            return bsh.eval((String) source);
        } catch (ParseException e) {

            throw new ScriptException(e.toString(), e.getErrorSourceFile(), e.getErrorLineNumber());
        } catch (TargetError e) {

            ScriptException se = new ScriptException(e.toString(), e.getErrorSourceFile(), e.getErrorLineNumber());

            se.initCause(e.getTarget());
            throw se;
        } catch (EvalError e) {

            throw new ScriptException(e.toString(), e.getErrorSourceFile(), e.getErrorLineNumber());
        } catch (InterpreterError e) {

            throw new ScriptException(e.toString());
        }
    }

    public Bindings createBindings() {
        return new SimpleBindings();
    }

    public ScriptEngineFactory getFactory() {
        if (this.factory == null)
            this.factory = new BshScriptEngineFactory();
        return this.factory;
    }

    public CompiledScript compile(String script) throws ScriptException {
        return compile(new StringReader(script));
    }

    public CompiledScript compile(Reader script) throws ScriptException {
        throw new Error("unimplemented");
    }

    public Object invoke(Object thiz, String name, Object... args) throws ScriptException, NoSuchMethodException {
        if (!(thiz instanceof This)) {
            throw new ScriptException("Illegal objec type: " + thiz.getClass());
        }
        This bshObject = (This) thiz;

        try {
            return bshObject.invokeMethod(name, args);
        } catch (ParseException e) {

            throw new ScriptException(e.toString(), e.getErrorSourceFile(), e.getErrorLineNumber());
        } catch (TargetError e) {

            ScriptException se = new ScriptException(e.toString(), e.getErrorSourceFile(), e.getErrorLineNumber());

            se.initCause(e.getTarget());
            throw se;
        } catch (EvalError e) {

            throw new ScriptException(e.toString(), e.getErrorSourceFile(), e.getErrorLineNumber());
        } catch (InterpreterError e) {

            throw new ScriptException(e.toString());
        }
    }

    public Object invoke(String name, Object... args) throws ScriptException, NoSuchMethodException {
        return invoke(getGlobal(), name, args);
    }

    public <T> T getInterface(Class<T> clasz) {
        try {
            return (T) getGlobal().getInterface(clasz);
        } catch (UtilEvalError utilEvalError) {
            utilEvalError.printStackTrace();
            return null;
        }
    }

    public <T> T getInterface(Object thiz, Class<T> clasz) {
        if (!(thiz instanceof This)) {
            throw new IllegalArgumentException("invalid object type: " + thiz.getClass());
        }

        try {
            This bshThis = (This) thiz;
            return (T) bshThis.getInterface(clasz);
        } catch (UtilEvalError utilEvalError) {
            utilEvalError.printStackTrace(System.err);
            return null;
        }
    }

    private This getGlobal() {
        return getEngineNameSpace(getContext()).getThis(getInterpreter());
    }

    class WriterOutputStream extends OutputStream {
        Writer writer;

        WriterOutputStream(Writer writer) {
            this.writer = writer;
        }

        public void write(int b) throws IOException {
            this.writer.write(b);
        }

        public void flush() throws IOException {
            this.writer.flush();
        }

        public void close() throws IOException {
            this.writer.close();
        }
    }
}
