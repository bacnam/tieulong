package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Comparator;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
final class GeneralRange<T>
implements Serializable
{
private final Comparator<? super T> comparator;
private final Optional<T> lowerEndpoint;
private final BoundType lowerBoundType;
private final Optional<T> upperEndpoint;
private final BoundType upperBoundType;
private transient GeneralRange<T> reverse;

static <T extends Comparable> GeneralRange<T> from(Range<T> range) {
Optional<T> lowerEndpoint = range.hasLowerBound() ? Optional.of(range.lowerEndpoint()) : Optional.absent();

BoundType lowerBoundType = range.hasLowerBound() ? range.lowerBoundType() : BoundType.OPEN;
Optional<T> upperEndpoint = range.hasUpperBound() ? Optional.of(range.upperEndpoint()) : Optional.absent();

BoundType upperBoundType = range.hasUpperBound() ? range.upperBoundType() : BoundType.OPEN;
return new GeneralRange<T>(Ordering.natural(), lowerEndpoint, lowerBoundType, upperEndpoint, upperBoundType);
}

static <T> GeneralRange<T> all(Comparator<? super T> comparator) {
return new GeneralRange<T>(comparator, Optional.absent(), BoundType.OPEN, Optional.absent(), BoundType.OPEN);
}

static <T> GeneralRange<T> downTo(Comparator<? super T> comparator, T endpoint, BoundType boundType) {
return new GeneralRange<T>(comparator, Optional.of(endpoint), boundType, Optional.absent(), BoundType.OPEN);
}

static <T> GeneralRange<T> upTo(Comparator<? super T> comparator, T endpoint, BoundType boundType) {
return new GeneralRange<T>(comparator, Optional.absent(), BoundType.OPEN, Optional.of(endpoint), boundType);
}

static <T> GeneralRange<T> range(Comparator<? super T> comparator, T lower, BoundType lowerType, T upper, BoundType upperType) {
return new GeneralRange<T>(comparator, Optional.of(lower), lowerType, Optional.of(upper), upperType);
}

private GeneralRange(Comparator<? super T> comparator, Optional<T> lowerEndpoint, BoundType lowerBoundType, Optional<T> upperEndpoint, BoundType upperBoundType) {
this.comparator = (Comparator<? super T>)Preconditions.checkNotNull(comparator);
this.lowerEndpoint = (Optional<T>)Preconditions.checkNotNull(lowerEndpoint);
this.lowerBoundType = (BoundType)Preconditions.checkNotNull(lowerBoundType);
this.upperEndpoint = (Optional<T>)Preconditions.checkNotNull(upperEndpoint);
this.upperBoundType = (BoundType)Preconditions.checkNotNull(upperBoundType);
if (lowerEndpoint.isPresent() && upperEndpoint.isPresent()) {
int cmp = comparator.compare((T)lowerEndpoint.get(), (T)upperEndpoint.get());

Preconditions.checkArgument((cmp <= 0), "lowerEndpoint (%s) > upperEndpoint (%s)", new Object[] { lowerEndpoint, upperEndpoint });

if (cmp == 0) {
Preconditions.checkArgument(((lowerBoundType != BoundType.OPEN)) | ((upperBoundType != BoundType.OPEN)));
}
} 
}

Comparator<? super T> comparator() {
return this.comparator;
}

boolean hasLowerBound() {
return this.lowerEndpoint.isPresent();
}

boolean hasUpperBound() {
return this.upperEndpoint.isPresent();
}

boolean isEmpty() {
return ((hasUpperBound() && tooLow((T)this.upperEndpoint.get())) || (hasLowerBound() && tooHigh((T)this.lowerEndpoint.get())));
}

boolean tooLow(T t) {
if (!hasLowerBound()) {
return false;
}
T lbound = (T)this.lowerEndpoint.get();
int cmp = this.comparator.compare(t, lbound);
return ((cmp < 0) ? 1 : 0) | ((cmp == 0)) & ((this.lowerBoundType == BoundType.OPEN));
}

boolean tooHigh(T t) {
if (!hasUpperBound()) {
return false;
}
T ubound = (T)this.upperEndpoint.get();
int cmp = this.comparator.compare(t, ubound);
return ((cmp > 0) ? 1 : 0) | ((cmp == 0)) & ((this.upperBoundType == BoundType.OPEN));
}

boolean contains(T t) {
Preconditions.checkNotNull(t);
return (!tooLow(t) && !tooHigh(t));
}

GeneralRange<T> intersect(GeneralRange<T> other) {
Preconditions.checkNotNull(other);
Preconditions.checkArgument(this.comparator.equals(other.comparator));

Optional<T> lowEnd = this.lowerEndpoint;
BoundType lowType = this.lowerBoundType;
if (!hasLowerBound()) {
lowEnd = other.lowerEndpoint;
lowType = other.lowerBoundType;
} else if (other.hasLowerBound()) {
int cmp = this.comparator.compare((T)this.lowerEndpoint.get(), (T)other.lowerEndpoint.get());
if (cmp < 0 || (cmp == 0 && other.lowerBoundType == BoundType.OPEN)) {
lowEnd = other.lowerEndpoint;
lowType = other.lowerBoundType;
} 
} 

Optional<T> upEnd = this.upperEndpoint;
BoundType upType = this.upperBoundType;
if (!hasUpperBound()) {
upEnd = other.upperEndpoint;
upType = other.upperBoundType;
} else if (other.hasUpperBound()) {
int cmp = this.comparator.compare((T)this.upperEndpoint.get(), (T)other.upperEndpoint.get());
if (cmp > 0 || (cmp == 0 && other.upperBoundType == BoundType.OPEN)) {
upEnd = other.upperEndpoint;
upType = other.upperBoundType;
} 
} 

if (lowEnd.isPresent() && upEnd.isPresent()) {
int cmp = this.comparator.compare((T)lowEnd.get(), (T)upEnd.get());
if (cmp > 0 || (cmp == 0 && lowType == BoundType.OPEN && upType == BoundType.OPEN)) {

lowEnd = upEnd;
lowType = BoundType.OPEN;
upType = BoundType.CLOSED;
} 
} 

return new GeneralRange(this.comparator, lowEnd, lowType, upEnd, upType);
}

public boolean equals(@Nullable Object obj) {
if (obj instanceof GeneralRange) {
GeneralRange<?> r = (GeneralRange)obj;
return (this.comparator.equals(r.comparator) && this.lowerEndpoint.equals(r.lowerEndpoint) && this.lowerBoundType.equals(r.lowerBoundType) && this.upperEndpoint.equals(r.upperEndpoint) && this.upperBoundType.equals(r.upperBoundType));
} 

return false;
}

public int hashCode() {
return Objects.hashCode(new Object[] { this.comparator, this.lowerEndpoint, this.lowerBoundType, this.upperEndpoint, this.upperBoundType });
}

public GeneralRange<T> reverse() {
GeneralRange<T> result = this.reverse;
if (result == null) {
result = new GeneralRange(Ordering.<T>from(this.comparator).reverse(), this.upperEndpoint, this.upperBoundType, this.lowerEndpoint, this.lowerBoundType);

result.reverse = this;
return this.reverse = result;
} 
return result;
}

public String toString() {
StringBuilder builder = new StringBuilder();
builder.append(this.comparator).append(":");
switch (this.lowerBoundType) {
case CLOSED:
builder.append('[');
break;
case OPEN:
builder.append('(');
break;
} 
if (hasLowerBound()) {
builder.append(this.lowerEndpoint.get());
} else {
builder.append("-∞");
} 
builder.append(',');
if (hasUpperBound()) {
builder.append(this.upperEndpoint.get());
} else {
builder.append("∞");
} 
switch (this.upperBoundType) {
case CLOSED:
builder.append(']');
break;
case OPEN:
builder.append(')');
break;
} 
return builder.toString();
}
}

