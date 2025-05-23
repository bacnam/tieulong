package ch.qos.logback.core.subst;

public class Token
{
public static final Token START_TOKEN = new Token(Type.START, null);
public static final Token CURLY_LEFT_TOKEN = new Token(Type.CURLY_LEFT, null);
public static final Token CURLY_RIGHT_TOKEN = new Token(Type.CURLY_RIGHT, null); Type type;
public static final Token DEFAULT_SEP_TOKEN = new Token(Type.DEFAULT, null); String payload;

public enum Type { LITERAL, START, CURLY_LEFT, CURLY_RIGHT, DEFAULT; }

public Token(Type type, String payload) {
this.type = type;
this.payload = payload;
}

public boolean equals(Object o) {
if (this == o) return true; 
if (o == null || getClass() != o.getClass()) return false;

Token token = (Token)o;

if (this.type != token.type) return false; 
if ((this.payload != null) ? !this.payload.equals(token.payload) : (token.payload != null)) return false;

return true;
}

public int hashCode() {
int result = (this.type != null) ? this.type.hashCode() : 0;
result = 31 * result + ((this.payload != null) ? this.payload.hashCode() : 0);
return result;
}

public String toString() {
String result = "Token{type=" + this.type;

if (this.payload != null) {
result = result + ", payload='" + this.payload + '\'';
}
result = result + '}';
return result;
}
}

