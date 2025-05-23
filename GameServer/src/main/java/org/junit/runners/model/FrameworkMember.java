package org.junit.runners.model;

import java.lang.reflect.Modifier;
import java.util.List;

public abstract class FrameworkMember<T extends FrameworkMember<T>>
implements Annotatable
{
abstract boolean isShadowedBy(T paramT);

boolean isShadowedBy(List<T> members) {
for (FrameworkMember frameworkMember : members) {
if (isShadowedBy((T)frameworkMember)) {
return true;
}
} 
return false;
}

protected abstract int getModifiers();

public boolean isStatic() {
return Modifier.isStatic(getModifiers());
}

public boolean isPublic() {
return Modifier.isPublic(getModifiers());
}

public abstract String getName();

public abstract Class<?> getType();

public abstract Class<?> getDeclaringClass();
}

