package org.apache.mina.filter.executor;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoEvent;
import org.apache.mina.core.write.WriteRequest;

public class DefaultIoEventSizeEstimator
implements IoEventSizeEstimator
{
private final ConcurrentMap<Class<?>, Integer> class2size = new ConcurrentHashMap<Class<?>, Integer>();

public DefaultIoEventSizeEstimator() {
this.class2size.put(boolean.class, Integer.valueOf(4));
this.class2size.put(byte.class, Integer.valueOf(1));
this.class2size.put(char.class, Integer.valueOf(2));
this.class2size.put(int.class, Integer.valueOf(4));
this.class2size.put(short.class, Integer.valueOf(2));
this.class2size.put(long.class, Integer.valueOf(8));
this.class2size.put(float.class, Integer.valueOf(4));
this.class2size.put(double.class, Integer.valueOf(8));
this.class2size.put(void.class, Integer.valueOf(0));
}

public int estimateSize(IoEvent event) {
return estimateSize(event) + estimateSize(event.getParameter());
}

public int estimateSize(Object message) {
if (message == null) {
return 8;
}

int answer = 8 + estimateSize(message.getClass(), null);

if (message instanceof IoBuffer) {
answer += ((IoBuffer)message).remaining();
} else if (message instanceof WriteRequest) {
answer += estimateSize(((WriteRequest)message).getMessage());
} else if (message instanceof CharSequence) {
answer += ((CharSequence)message).length() << 1;
} else if (message instanceof Iterable) {
for (Object m : message) {
answer += estimateSize(m);
}
} 

return align(answer);
}

private int estimateSize(Class<?> clazz, Set<Class<?>> visitedClasses) {
Integer objectSize = this.class2size.get(clazz);

if (objectSize != null) {
return objectSize.intValue();
}

if (visitedClasses != null) {
if (visitedClasses.contains(clazz)) {
return 0;
}
} else {
visitedClasses = new HashSet<Class<?>>();
} 

visitedClasses.add(clazz);

int answer = 8;

for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
Field[] fields = c.getDeclaredFields();

for (Field f : fields) {
if ((f.getModifiers() & 0x8) == 0)
{

answer += estimateSize(f.getType(), visitedClasses);
}
} 
} 
visitedClasses.remove(clazz);

answer = align(answer);

Integer tmpAnswer = this.class2size.putIfAbsent(clazz, Integer.valueOf(answer));

if (tmpAnswer != null) {
answer = tmpAnswer.intValue();
}

return answer;
}

private static int align(int size) {
if (size % 8 != 0) {
size /= 8;
size++;
size *= 8;
} 

return size;
}
}

