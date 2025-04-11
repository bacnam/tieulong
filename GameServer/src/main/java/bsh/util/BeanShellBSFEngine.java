package bsh.util;

import bsh.*;
import org.apache.bsf.BSFDeclaredBean;
import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.apache.bsf.util.BSFEngineImpl;

import java.util.Vector;

public class BeanShellBSFEngine extends BSFEngineImpl {
    static final String bsfApplyMethod = "_bsfApply( _bsfNames, _bsfArgs, _bsfText ) {for(i=0;i<_bsfNames.length;i++)this.namespace.setVariable(_bsfNames[i], _bsfArgs[i],false);return this.interpreter.eval(_bsfText, this.namespace);}";
    Interpreter interpreter;
    boolean installedApplyMethod;

    public void initialize(BSFManager mgr, String lang, Vector<BSFDeclaredBean> declaredBeans) throws BSFException {
        super.initialize(mgr, lang, declaredBeans);

        this.interpreter = new Interpreter();

        try {
            this.interpreter.set("bsf", mgr);
        } catch (EvalError e) {
            throw new BSFException("bsh internal error: " + e.toString());
        }

        for (int i = 0; i < declaredBeans.size(); i++) {

            BSFDeclaredBean bean = declaredBeans.get(i);
            declareBean(bean);
        }
    }

    public void setDebug(boolean debug) {
        Interpreter.DEBUG = debug;
    }

    public Object call(Object object, String name, Object[] args) throws BSFException {
        if (object == null) {
            try {
                object = this.interpreter.get("global");
            } catch (EvalError e) {
                throw new BSFException("bsh internal error: " + e.toString());
            }
        }
        if (object instanceof This) {

            try {
                Object value = ((This) object).invokeMethod(name, args);
                return Primitive.unwrap(value);
            } catch (InterpreterError e) {

                throw new BSFException("BeanShell interpreter internal error: " + e);
            } catch (TargetError e2) {

                throw new BSFException("The application script threw an exception: " + e2.getTarget());

            } catch (EvalError e3) {

                throw new BSFException("BeanShell script error: " + e3);
            }
        }
        throw new BSFException(
                "Cannot invoke method: " + name + ". Object: " + object + " is not a BeanShell scripted object.");
    }

    public Object apply(String source, int lineNo, int columnNo, Object funcBody, Vector namesVec, Vector argsVec)
            throws BSFException {
        if (namesVec.size() != argsVec.size())
            throw new BSFException("number of params/names mismatch");
        if (!(funcBody instanceof String)) {
            throw new BSFException("apply: functino body must be a string");
        }
        String[] names = new String[namesVec.size()];
        namesVec.copyInto((Object[]) names);
        Object[] args = new Object[argsVec.size()];
        argsVec.copyInto(args);

        try {
            if (!this.installedApplyMethod) {

                this.interpreter.eval(
                        "_bsfApply( _bsfNames, _bsfArgs, _bsfText ) {for(i=0;i<_bsfNames.length;i++)this.namespace.setVariable(_bsfNames[i], _bsfArgs[i],false);return this.interpreter.eval(_bsfText, this.namespace);}");
                this.installedApplyMethod = true;
            }

            This global = (This) this.interpreter.get("global");
            Object value = global.invokeMethod("_bsfApply", new Object[]{names, args, funcBody});

            return Primitive.unwrap(value);
        } catch (InterpreterError e) {

            throw new BSFException("BeanShell interpreter internal error: " + e + sourceInfo(source, lineNo, columnNo));

        } catch (TargetError e2) {

            throw new BSFException("The application script threw an exception: " + e2.getTarget()
                    + sourceInfo(source, lineNo, columnNo));

        } catch (EvalError e3) {

            throw new BSFException("BeanShell script error: " + e3 + sourceInfo(source, lineNo, columnNo));
        }
    }

    public Object eval(String source, int lineNo, int columnNo, Object expr) throws BSFException {
        if (!(expr instanceof String)) {
            throw new BSFException("BeanShell expression must be a string");
        }
        try {
            return this.interpreter.eval((String) expr);
        } catch (InterpreterError e) {

            throw new BSFException("BeanShell interpreter internal error: " + e + sourceInfo(source, lineNo, columnNo));

        } catch (TargetError e2) {

            throw new BSFException("The application script threw an exception: " + e2.getTarget()
                    + sourceInfo(source, lineNo, columnNo));

        } catch (EvalError e3) {

            throw new BSFException("BeanShell script error: " + e3 + sourceInfo(source, lineNo, columnNo));
        }
    }

    public void exec(String source, int lineNo, int columnNo, Object script) throws BSFException {
        eval(source, lineNo, columnNo, script);
    }

    public void declareBean(BSFDeclaredBean bean) throws BSFException {
        try {
            this.interpreter.set(bean.name, bean.bean);
        } catch (EvalError e) {
            throw new BSFException("error declaring bean: " + bean.name + " : " + e.toString());
        }
    }

    public void undeclareBean(BSFDeclaredBean bean) throws BSFException {
        try {
            this.interpreter.unset(bean.name);
        } catch (EvalError e) {
            throw new BSFException("bsh internal error: " + e.toString());
        }
    }

    public void terminate() {
    }

    private String sourceInfo(String source, int lineNo, int columnNo) {
        return " BSF info: " + source + " at line: " + lineNo + " column: columnNo";
    }
}
