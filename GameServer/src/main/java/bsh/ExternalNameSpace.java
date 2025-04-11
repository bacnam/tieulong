package bsh;

import java.util.*;

public class ExternalNameSpace
        extends NameSpace {
    private Map externalMap;

    public ExternalNameSpace() {
        this(null, "External Map Namespace", null);
    }

    public ExternalNameSpace(NameSpace parent, String name, Map<Object, Object> externalMap) {
        super(parent, name);

        if (externalMap == null) {
            externalMap = new HashMap<Object, Object>();
        }
        this.externalMap = externalMap;
    }

    public Map getMap() {
        return this.externalMap;
    }

    public void setMap(Map map) {
        this.externalMap = null;
        clear();
        this.externalMap = map;
    }

    public void unsetVariable(String name) {
        super.unsetVariable(name);
        this.externalMap.remove(name);
    }

    public String[] getVariableNames() {
        Set nameSet = new HashSet();
        String[] nsNames = super.getVariableNames();
        nameSet.addAll(Arrays.asList(nsNames));
        nameSet.addAll(this.externalMap.keySet());
        return (String[]) nameSet.toArray((Object[]) new String[0]);
    }

    protected Variable getVariableImpl(String name, boolean recurse) throws UtilEvalError {
        Variable var;
        Object value = this.externalMap.get(name);

        if (value == null) {

            super.unsetVariable(name);

            var = super.getVariableImpl(name, recurse);

        } else {

            Variable localVar = super.getVariableImpl(name, false);

            if (localVar == null) {
                var = createVariable(name, null, value, null);
            } else {
                var = localVar;
            }
        }
        return var;
    }

    public Variable createVariable(String name, Class type, Object value, Modifiers mods) {
        LHS lhs = new LHS(this.externalMap, name);

        try {
            lhs.assign(value, false);
        } catch (UtilEvalError e) {
            throw new InterpreterError(e.toString());
        }
        return new Variable(name, type, lhs);
    }

    public void clear() {
        super.clear();
        this.externalMap.clear();
    }
}

