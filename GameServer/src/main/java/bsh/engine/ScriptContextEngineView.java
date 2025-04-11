package bsh.engine;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.script.ScriptContext;

public class ScriptContextEngineView
        implements Map<String, Object> {
    ScriptContext context;

    public ScriptContextEngineView(ScriptContext context) {
        this.context = context;
    }

    public int size() {
        return totalKeySet().size();
    }

    public boolean isEmpty() {
        return (totalKeySet().size() == 0);
    }

    public boolean containsKey(Object key) {
        return (this.context.getAttribute((String) key) != null);
    }

    public boolean containsValue(Object value) {
        Set values = totalValueSet();
        return values.contains(value);
    }

    public Object get(Object key) {
        return this.context.getAttribute((String) key);
    }

    public Object put(String key, Object value) {
        Object oldValue = this.context.getAttribute(key, 100);

        this.context.setAttribute(key, value, 100);
        return oldValue;
    }

    public void putAll(Map<? extends String, ? extends Object> t) {
        this.context.getBindings(100).putAll(t);
    }

    public Object remove(Object okey) {
        String key = (String) okey;
        Object oldValue = this.context.getAttribute(key, 100);

        this.context.removeAttribute(key, 100);
        return oldValue;
    }

    public void clear() {
        this.context.getBindings(100).clear();
    }

    public Set keySet() {
        return totalKeySet();
    }

    public Collection values() {
        return totalValueSet();
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        throw new Error("unimplemented");
    }

    private Set totalKeySet() {
        Set<String> keys = new HashSet();
        List<Integer> scopes = this.context.getScopes();
        for (Iterator<Integer> i$ = scopes.iterator(); i$.hasNext();) {
            int i = ((Integer) i$.next()).intValue();
            keys.addAll(this.context.getBindings(i).keySet());
        }

        return Collections.unmodifiableSet(keys);
    }

    private Set<Object> totalValueSet() {
        Set<Object> values = new HashSet<>();
        List<Integer> scopes = this.context.getScopes();
        for (Iterator<Integer> i$ = scopes.iterator(); i$.hasNext();) {
            int i = i$.next();
            values.addAll(this.context.getBindings(i).values());
        }
    
        return Collections.unmodifiableSet(values);
    }    
}
