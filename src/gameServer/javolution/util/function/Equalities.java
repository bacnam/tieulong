package javolution.util.function;

import javolution.lang.Parallelizable;
import javolution.lang.Realtime;
import javolution.util.internal.comparator.ArrayComparatorImpl;
import javolution.util.internal.comparator.IdentityComparatorImpl;
import javolution.util.internal.comparator.LexicalCaseInsensitiveComparatorImpl;
import javolution.util.internal.comparator.LexicalComparatorImpl;
import javolution.util.internal.comparator.LexicalFastComparatorImpl;
import javolution.util.internal.comparator.StandardComparatorImpl;

public class Equalities
{
@Parallelizable
@Realtime(limit = Realtime.Limit.UNKNOWN)
public static final Equality<Object> STANDARD = (Equality<Object>)new StandardComparatorImpl();

@Parallelizable
@Realtime(limit = Realtime.Limit.CONSTANT)
public static final Equality<Object> IDENTITY = (Equality<Object>)new IdentityComparatorImpl();

@Parallelizable
@Realtime(limit = Realtime.Limit.LINEAR)
public static final Equality<Object> ARRAY = (Equality<Object>)new ArrayComparatorImpl();

@Parallelizable
@Realtime(limit = Realtime.Limit.LINEAR)
public static final Equality<CharSequence> LEXICAL = (Equality<CharSequence>)new LexicalComparatorImpl();

@Parallelizable
@Realtime(limit = Realtime.Limit.LINEAR)
public static final Equality<CharSequence> LEXICAL_CASE_INSENSITIVE = (Equality<CharSequence>)new LexicalCaseInsensitiveComparatorImpl();

@Parallelizable
@Realtime(limit = Realtime.Limit.LINEAR)
public static final Equality<CharSequence> LEXICAL_FAST = (Equality<CharSequence>)new LexicalFastComparatorImpl();
}

