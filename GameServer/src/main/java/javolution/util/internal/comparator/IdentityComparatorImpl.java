package javolution.util.internal.comparator;

import java.io.Serializable;
import javolution.util.function.Equality;

public class IdentityComparatorImpl<E>
implements Equality<E>, Serializable
{
private static final long serialVersionUID = 6576306094743751922L;

public int hashCodeOf(E obj) {
return System.identityHashCode(obj);
}

public boolean areEqual(E e1, E e2) {
return (e1 == e2);
}

public int compare(E left, E right) {
if (left == right)
return 0; 
if (left == null)
return -1; 
if (right == null) {
return 1;
}

return (hashCodeOf(left) < hashCodeOf(right)) ? -1 : 1;
}
}

