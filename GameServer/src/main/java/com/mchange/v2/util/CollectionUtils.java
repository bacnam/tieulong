package com.mchange.v2.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

public final class CollectionUtils
{
public static final SortedSet EMPTY_SORTED_SET = Collections.unmodifiableSortedSet(new TreeSet());

static final Class[] EMPTY_ARG_CLASSES = new Class[0];
static final Object[] EMPTY_ARGS = new Object[0];

static final Class[] COMPARATOR_ARG_CLASSES = new Class[] { Comparator.class };
static final Class[] COLLECTION_ARG_CLASSES = new Class[] { Collection.class };
static final Class[] SORTED_SET_ARG_CLASSES = new Class[] { SortedSet.class };
static final Class[] MAP_ARG_CLASSES = new Class[] { Map.class };
static final Class[] SORTED_MAP_ARG_CLASSES = new Class[] { SortedMap.class };

static final Class STD_UNMODIFIABLE_COLLECTION_CL;

static final Class STD_UNMODIFIABLE_SET_CL;

static final Class STD_UNMODIFIABLE_LIST_CL;
static final Class STD_UNMODIFIABLE_RA_LIST_CL;
static final Class STD_UNMODIFIABLE_SORTED_SET_CL;
static final Class STD_UNMODIFIABLE_MAP_CL;
static final Class STD_UNMODIFIABLE_SORTED_MAP_CL;
static final Class STD_SYNCHRONIZED_COLLECTION_CL;
static final Class STD_SYNCHRONIZED_SET_CL;
static final Class STD_SYNCHRONIZED_LIST_CL;
static final Class STD_SYNCHRONIZED_RA_LIST_CL;
static final Class STD_SYNCHRONIZED_SORTED_SET_CL;
static final Class STD_SYNCHRONIZED_MAP_CL;
static final Class STD_SYNCHRONIZED_SORTED_MAP_CL;
static final Set UNMODIFIABLE_WRAPPERS;
static final Set SYNCHRONIZED_WRAPPERS;
static final Set ALL_COLLECTIONS_WRAPPERS;

static {
HashSet<?> hashSet = new HashSet();
TreeSet<?> treeSet = new TreeSet();
LinkedList<?> linkedList = new LinkedList();
ArrayList<?> arrayList = new ArrayList();
HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();

HashSet<Class<?>> hashSet1 = new HashSet();
HashSet<Class<?>> hashSet2 = new HashSet();

hashSet1.add(STD_UNMODIFIABLE_COLLECTION_CL = Collections.unmodifiableCollection(arrayList).getClass());
hashSet1.add(STD_UNMODIFIABLE_SET_CL = Collections.unmodifiableSet(hashSet).getClass());
hashSet1.add(STD_UNMODIFIABLE_LIST_CL = Collections.unmodifiableList(linkedList).getClass());
hashSet1.add(STD_UNMODIFIABLE_RA_LIST_CL = Collections.unmodifiableList(arrayList).getClass());
hashSet1.add(STD_UNMODIFIABLE_SORTED_SET_CL = Collections.unmodifiableSortedSet(treeSet).getClass());
hashSet1.add(STD_UNMODIFIABLE_MAP_CL = Collections.unmodifiableMap(hashMap).getClass());
hashSet1.add(STD_UNMODIFIABLE_SORTED_MAP_CL = Collections.unmodifiableSortedMap(treeMap).getClass());

hashSet2.add(STD_SYNCHRONIZED_COLLECTION_CL = Collections.synchronizedCollection(arrayList).getClass());
hashSet2.add(STD_SYNCHRONIZED_SET_CL = Collections.synchronizedSet(hashSet).getClass());
hashSet2.add(STD_SYNCHRONIZED_LIST_CL = Collections.synchronizedList(linkedList).getClass());
hashSet2.add(STD_SYNCHRONIZED_RA_LIST_CL = Collections.synchronizedList(arrayList).getClass());
hashSet2.add(STD_SYNCHRONIZED_SORTED_SET_CL = Collections.synchronizedSortedSet(treeSet).getClass());
hashSet2.add(STD_SYNCHRONIZED_MAP_CL = Collections.synchronizedMap(hashMap).getClass());
hashSet2.add(STD_SYNCHRONIZED_SORTED_MAP_CL = Collections.synchronizedMap(treeMap).getClass());

UNMODIFIABLE_WRAPPERS = Collections.unmodifiableSet(hashSet1);

SYNCHRONIZED_WRAPPERS = Collections.unmodifiableSet(hashSet2);

HashSet<Class<?>> hashSet3 = new HashSet<Class<?>>(hashSet1);
hashSet3.addAll(hashSet2);
ALL_COLLECTIONS_WRAPPERS = Collections.unmodifiableSet(hashSet3);
}

public static boolean isCollectionsWrapper(Class paramClass) {
return ALL_COLLECTIONS_WRAPPERS.contains(paramClass);
}
public static boolean isCollectionsWrapper(Collection paramCollection) {
return isCollectionsWrapper(paramCollection.getClass());
}
public static boolean isCollectionsWrapper(Map paramMap) {
return isCollectionsWrapper(paramMap.getClass());
}
public static boolean isSynchronizedWrapper(Class paramClass) {
return SYNCHRONIZED_WRAPPERS.contains(paramClass);
}
public static boolean isSynchronizedWrapper(Collection paramCollection) {
return isSynchronizedWrapper(paramCollection.getClass());
}
public static boolean isSynchronizedWrapper(Map paramMap) {
return isSynchronizedWrapper(paramMap.getClass());
}
public static boolean isUnmodifiableWrapper(Class paramClass) {
return UNMODIFIABLE_WRAPPERS.contains(paramClass);
}
public static boolean isUnmodifiableWrapper(Collection paramCollection) {
return isUnmodifiableWrapper(paramCollection.getClass());
}
public static boolean isUnmodifiableWrapper(Map paramMap) {
return isUnmodifiableWrapper(paramMap.getClass());
}

public static Collection narrowUnmodifiableCollection(Collection<?> paramCollection) {
if (paramCollection instanceof SortedSet)
return Collections.unmodifiableSortedSet((SortedSet)paramCollection); 
if (paramCollection instanceof Set)
return Collections.unmodifiableSet((Set)paramCollection); 
if (paramCollection instanceof List) {
return Collections.unmodifiableList((List)paramCollection);
}
return Collections.unmodifiableCollection(paramCollection);
}

public static Collection narrowSynchronizedCollection(Collection<?> paramCollection) {
if (paramCollection instanceof SortedSet)
return Collections.synchronizedSortedSet((SortedSet)paramCollection); 
if (paramCollection instanceof Set)
return Collections.synchronizedSet((Set)paramCollection); 
if (paramCollection instanceof List) {
return Collections.synchronizedList((List)paramCollection);
}
return Collections.synchronizedCollection(paramCollection);
}

public static Map narrowUnmodifiableMap(Map<?, ?> paramMap) {
if (paramMap instanceof SortedMap) {
return Collections.unmodifiableSortedMap((SortedMap<?, ?>)paramMap);
}
return Collections.unmodifiableMap(paramMap);
}

public static Map narrowSynchronizedMap(Map<?, ?> paramMap) {
if (paramMap instanceof SortedMap) {
return Collections.synchronizedSortedMap((SortedMap<?, ?>)paramMap);
}
return Collections.synchronizedMap(paramMap);
}

public static Collection attemptClone(Collection paramCollection) throws NoSuchMethodException {
if (paramCollection instanceof Vector) return (Collection)((Vector)paramCollection).clone(); 
if (paramCollection instanceof ArrayList) return (Collection)((ArrayList)paramCollection).clone(); 
if (paramCollection instanceof LinkedList) return (Collection)((LinkedList)paramCollection).clone(); 
if (paramCollection instanceof HashSet) return (Collection)((HashSet)paramCollection).clone(); 
if (paramCollection instanceof TreeSet) return (Collection)((TreeSet)paramCollection).clone();

Collection collection = null;
Class<?> clazz = paramCollection.getClass();

try {
Method method = clazz.getMethod("clone", EMPTY_ARG_CLASSES);
collection = (Collection)method.invoke(paramCollection, EMPTY_ARGS);
}
catch (Exception exception) {

exception.printStackTrace();
} 

if (collection == null) {

try {

Constructor<?> constructor = clazz.getConstructor((paramCollection instanceof SortedSet) ? SORTED_SET_ARG_CLASSES : COLLECTION_ARG_CLASSES);
collection = (Collection)constructor.newInstance(new Object[] { paramCollection });
}
catch (Exception exception) {

exception.printStackTrace();
} 
}

if (collection == null) {

try {

Constructor<?> constructor = clazz.getConstructor(new Class[] { clazz });
collection = (Collection)constructor.newInstance(new Object[] { paramCollection });
}
catch (Exception exception) {

exception.printStackTrace();
} 
}

if (collection == null) {
throw new NoSuchMethodException("No accessible clone() method or reasonable copy constructor could be called on Collection " + paramCollection);
}
return collection;
}

public static Map attemptClone(Map paramMap) throws NoSuchMethodException {
if (paramMap instanceof Properties) return (Map)((Properties)paramMap).clone(); 
if (paramMap instanceof Hashtable) return (Map)((Hashtable)paramMap).clone(); 
if (paramMap instanceof HashMap) return (Map)((HashMap)paramMap).clone(); 
if (paramMap instanceof TreeMap) return (Map)((TreeMap)paramMap).clone();

Map map = null;
Class<?> clazz = paramMap.getClass();

try {
Method method = clazz.getMethod("clone", EMPTY_ARG_CLASSES);
map = (Map)method.invoke(paramMap, EMPTY_ARGS);
}
catch (Exception exception) {

exception.printStackTrace();
} 

if (map == null) {

try {

Constructor<?> constructor = clazz.getConstructor((paramMap instanceof SortedMap) ? SORTED_MAP_ARG_CLASSES : MAP_ARG_CLASSES);
map = (Map)constructor.newInstance(new Object[] { paramMap });
}
catch (Exception exception) {

exception.printStackTrace();
} 
}

if (map == null) {

try {

Constructor<?> constructor = clazz.getConstructor(new Class[] { clazz });
map = (Map)constructor.newInstance(new Object[] { paramMap });
}
catch (Exception exception) {

exception.printStackTrace();
} 
}

if (map == null) {
throw new NoSuchMethodException("No accessible clone() method or reasonable copy constructor could be called on Map " + paramMap);
}
return map;
}

public static void add(Collection<Object> paramCollection, Object paramObject) {
paramCollection.add(paramObject);
}
public static void remove(Collection paramCollection, Object paramObject) {
paramCollection.remove(paramObject);
}

public static int size(Object paramObject) {
if (paramObject instanceof Collection)
return ((Collection)paramObject).size(); 
if (paramObject instanceof Map)
return ((Map)paramObject).size(); 
if (paramObject instanceof Object[])
return ((Object[])paramObject).length; 
if (paramObject instanceof boolean[])
return ((boolean[])paramObject).length; 
if (paramObject instanceof byte[])
return ((byte[])paramObject).length; 
if (paramObject instanceof char[])
return ((char[])paramObject).length; 
if (paramObject instanceof short[])
return ((short[])paramObject).length; 
if (paramObject instanceof int[])
return ((int[])paramObject).length; 
if (paramObject instanceof long[])
return ((long[])paramObject).length; 
if (paramObject instanceof float[])
return ((float[])paramObject).length; 
if (paramObject instanceof double[]) {
return ((double[])paramObject).length;
}
throw new IllegalArgumentException(paramObject + " must be a Collection, Map, or array!");
}
}

