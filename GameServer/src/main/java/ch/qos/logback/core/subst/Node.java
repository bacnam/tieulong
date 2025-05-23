package ch.qos.logback.core.subst;

public class Node
{
Type type;
Object payload;
Object defaultPart;
Node next;

enum Type
{
LITERAL, VARIABLE;
}

public Node(Type type, Object payload) {
this.type = type;
this.payload = payload;
}

public Node(Type type, Object payload, Object defaultPart) {
this.type = type;
this.payload = payload;
this.defaultPart = defaultPart;
}

void append(Node newNode) {
if (newNode == null)
return; 
Node n = this;
while (true) {
if (n.next == null) {
n.next = newNode;
return;
} 
n = n.next;
} 
} public String toString() {
StringBuilder payloadBuf;
StringBuilder defaultPartBuf2;
String r;
switch (this.type) {
case LITERAL:
return "Node{type=" + this.type + ", payload='" + this.payload + "'}";

case VARIABLE:
payloadBuf = new StringBuilder();
defaultPartBuf2 = new StringBuilder();
if (this.defaultPart != null) {
recursive((Node)this.defaultPart, defaultPartBuf2);
}
recursive((Node)this.payload, payloadBuf);
r = "Node{type=" + this.type + ", payload='" + payloadBuf.toString() + "'";

if (this.defaultPart != null)
r = r + ", defaultPart=" + defaultPartBuf2.toString(); 
r = r + '}';
return r;
} 
return null;
}

public void dump() {
System.out.print(toString());
System.out.print(" -> ");
if (this.next != null) {
this.next.dump();
} else {
System.out.print(" null");
} 
}

void recursive(Node n, StringBuilder sb) {
Node c = n;
while (c != null) {
sb.append(c.toString()).append(" --> ");
c = c.next;
} 
sb.append("null ");
}

public void setNext(Node n) {
this.next = n;
}

public boolean equals(Object o) {
if (this == o) return true; 
if (o == null || getClass() != o.getClass()) return false;

Node node = (Node)o;

if (this.type != node.type) return false; 
if ((this.payload != null) ? !this.payload.equals(node.payload) : (node.payload != null)) return false; 
if ((this.defaultPart != null) ? !this.defaultPart.equals(node.defaultPart) : (node.defaultPart != null)) return false; 
if ((this.next != null) ? !this.next.equals(node.next) : (node.next != null)) return false;

return true;
}

public int hashCode() {
int result = (this.type != null) ? this.type.hashCode() : 0;
result = 31 * result + ((this.payload != null) ? this.payload.hashCode() : 0);
result = 31 * result + ((this.defaultPart != null) ? this.defaultPart.hashCode() : 0);
result = 31 * result + ((this.next != null) ? this.next.hashCode() : 0);
return result;
}
}

