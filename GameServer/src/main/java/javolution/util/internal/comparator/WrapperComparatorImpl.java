package javolution.util.internal.comparator;

import java.io.Serializable;
import java.util.Comparator;
import javolution.util.function.Equality;

public final class WrapperComparatorImpl<E>
implements Equality<E>, Serializable
{
private static final long serialVersionUID = 8775282553794347279L;
private final Comparator<? super E> comparator;

public WrapperComparatorImpl(Comparator<? super E> comparator) {
this.comparator = comparator;
}

public int hashCodeOf(E obj) {
throw new UnsupportedOperationException("Standard comparator (java.util.Comparator) cannot be used for hashcode calculations; please use a coherent equality comparator instead (e.g. javolution.util.function.Equality).");
}

public boolean areEqual(E e1, E e2) {
return (e1 == e2 || (e1 != null && this.comparator.compare(e1, e2) == 0));
}

public int compare(E left, E right) {
if (left == right)
return 0; 
if (left == null)
return -1; 
if (right == null)
return 1; 
return this.comparator.compare(left, right);
}
}

