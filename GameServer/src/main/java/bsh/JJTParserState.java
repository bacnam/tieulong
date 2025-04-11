package bsh;

import java.util.Stack;

class JJTParserState {
    private Stack<Node> nodes = new Stack<>();
    private Stack<Integer> marks = new Stack<>();

    private int sp = 0;
    private int mk = 0;

    private boolean node_created;

    boolean nodeCreated() {
        return this.node_created;
    }

    void reset() {
        this.nodes.removeAllElements();
        this.marks.removeAllElements();
        this.sp = 0;
        this.mk = 0;
    }

    Node rootNode() {
        return this.nodes.elementAt(0);
    }

    void pushNode(Node n) {
        this.nodes.push(n);
        this.sp++;
    }

    Node popNode() {
        if (--this.sp < this.mk) {
            this.mk = this.marks.pop();
        }
        return this.nodes.pop();
    }

    Node peekNode() {
        return this.nodes.peek();
    }

    int nodeArity() {
        return this.sp - this.mk;
    }

    void clearNodeScope(Node n) {
        while (this.sp > this.mk) {
            popNode();
        }
        this.mk = ((Integer) this.marks.pop()).intValue();
    }

    void openNodeScope(Node n) {
        this.marks.push(new Integer(this.mk));
        this.mk = this.sp;
        n.jjtOpen();
    }

    void closeNodeScope(Node n, int num) {
        this.mk = this.marks.pop();
        while (num-- > 0) {
            Node c = popNode();
            c.jjtSetParent(n);
            n.jjtAddChild(c, num);
        }
        n.jjtClose();
        pushNode(n);
        this.node_created = true;
    }

    void closeNodeScope(Node n, boolean condition) {
        if (condition) {
            int a = nodeArity();
            this.mk = this.marks.pop();
            while (a-- > 0) {
                Node c = popNode();
                c.jjtSetParent(n);
                n.jjtAddChild(c, a);
            }
            n.jjtClose();
            pushNode(n);
            this.node_created = true;
        } else {
            this.mk = this.marks.pop();
            this.node_created = false;
        }
    }
}
