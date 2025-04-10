package javolution.util.internal.comparator;

import java.io.Serializable;
import javolution.util.function.Equality;

public class StandardComparatorImpl<E>
implements Equality<E>, Serializable
{
private static final long serialVersionUID = -615690677813206151L;

public int hashCodeOf(E e) {
return (e == null) ? 0 : e.hashCode();
}

public boolean areEqual(E e1, E e2) {
return (e1 == e2 || (e1 != null && e1.equals(e2)));
}

public int compare(E left, E right) {
if (left == right)
return 0; 
if (left == null)
return -1; 
if (right == null)
return 1; 
if (left instanceof Comparable) {
return ((Comparable<E>)left).compareTo(right);
}

if (left.equals(right))
return 0; 
return (left.hashCode() < right.hashCode()) ? -1 : 1;
}
}

