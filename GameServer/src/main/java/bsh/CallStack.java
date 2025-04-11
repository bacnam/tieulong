package bsh;

import java.util.Vector;

public class CallStack {
    private Vector stack = new Vector(2);

    public CallStack() {
    }

    public CallStack(NameSpace namespace) {
        push(namespace);
    }

    public void clear() {
        this.stack.removeAllElements();
    }

    public void push(NameSpace ns) {
        this.stack.insertElementAt(ns, 0);
    }

    public NameSpace top() {
        return get(0);
    }

    public NameSpace swap(NameSpace newTop) {
        NameSpace oldTop = (NameSpace) this.stack.elementAt(0);
        this.stack.setElementAt(newTop, 0);
        return oldTop;
    }
    
    public NameSpace get(int depth) {
        if (depth >= depth()) {
            return NameSpace.JAVACODE;
        }
        return (NameSpace) this.stack.elementAt(depth);
    }
    

    public void set(int depth, NameSpace ns) {
        this.stack.setElementAt(ns, depth);
    }

    public NameSpace pop() {
        if (depth() < 1)
            throw new InterpreterError("pop on empty CallStack");
        NameSpace top = top();
        this.stack.removeElementAt(0);
        return top;
    }

    public int depth() {
        return this.stack.size();
    }

    public NameSpace[] toArray() {
        NameSpace[] nsa = new NameSpace[depth()];
        this.stack.copyInto((Object[]) nsa);
        return nsa;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("CallStack:\n");
        NameSpace[] nsa = toArray();
        for (int i = 0; i < nsa.length; i++) {
            sb.append("\t" + nsa[i] + "\n");
        }
        return sb.toString();
    }

    public CallStack copy() {
        CallStack cs = new CallStack();
        cs.stack = (Vector) this.stack.clone();
        return cs;
    }
}
