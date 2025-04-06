/*     */ package com.mchange.v2.util;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Hashtable;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CollectionUtils
/*     */ {
/*  49 */   public static final SortedSet EMPTY_SORTED_SET = Collections.unmodifiableSortedSet(new TreeSet());
/*     */   
/*  51 */   static final Class[] EMPTY_ARG_CLASSES = new Class[0];
/*  52 */   static final Object[] EMPTY_ARGS = new Object[0];
/*     */   
/*  54 */   static final Class[] COMPARATOR_ARG_CLASSES = new Class[] { Comparator.class };
/*  55 */   static final Class[] COLLECTION_ARG_CLASSES = new Class[] { Collection.class };
/*  56 */   static final Class[] SORTED_SET_ARG_CLASSES = new Class[] { SortedSet.class };
/*  57 */   static final Class[] MAP_ARG_CLASSES = new Class[] { Map.class };
/*  58 */   static final Class[] SORTED_MAP_ARG_CLASSES = new Class[] { SortedMap.class };
/*     */   
/*     */   static final Class STD_UNMODIFIABLE_COLLECTION_CL;
/*     */   
/*     */   static final Class STD_UNMODIFIABLE_SET_CL;
/*     */   
/*     */   static final Class STD_UNMODIFIABLE_LIST_CL;
/*     */   static final Class STD_UNMODIFIABLE_RA_LIST_CL;
/*     */   static final Class STD_UNMODIFIABLE_SORTED_SET_CL;
/*     */   static final Class STD_UNMODIFIABLE_MAP_CL;
/*     */   static final Class STD_UNMODIFIABLE_SORTED_MAP_CL;
/*     */   static final Class STD_SYNCHRONIZED_COLLECTION_CL;
/*     */   static final Class STD_SYNCHRONIZED_SET_CL;
/*     */   static final Class STD_SYNCHRONIZED_LIST_CL;
/*     */   static final Class STD_SYNCHRONIZED_RA_LIST_CL;
/*     */   static final Class STD_SYNCHRONIZED_SORTED_SET_CL;
/*     */   static final Class STD_SYNCHRONIZED_MAP_CL;
/*     */   static final Class STD_SYNCHRONIZED_SORTED_MAP_CL;
/*     */   static final Set UNMODIFIABLE_WRAPPERS;
/*     */   static final Set SYNCHRONIZED_WRAPPERS;
/*     */   static final Set ALL_COLLECTIONS_WRAPPERS;
/*     */   
/*     */   static {
/*  81 */     HashSet<?> hashSet = new HashSet();
/*  82 */     TreeSet<?> treeSet = new TreeSet();
/*  83 */     LinkedList<?> linkedList = new LinkedList();
/*  84 */     ArrayList<?> arrayList = new ArrayList();
/*  85 */     HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
/*  86 */     TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
/*     */     
/*  88 */     HashSet<Class<?>> hashSet1 = new HashSet();
/*  89 */     HashSet<Class<?>> hashSet2 = new HashSet();
/*     */     
/*  91 */     hashSet1.add(STD_UNMODIFIABLE_COLLECTION_CL = Collections.unmodifiableCollection(arrayList).getClass());
/*  92 */     hashSet1.add(STD_UNMODIFIABLE_SET_CL = Collections.unmodifiableSet(hashSet).getClass());
/*  93 */     hashSet1.add(STD_UNMODIFIABLE_LIST_CL = Collections.unmodifiableList(linkedList).getClass());
/*  94 */     hashSet1.add(STD_UNMODIFIABLE_RA_LIST_CL = Collections.unmodifiableList(arrayList).getClass());
/*  95 */     hashSet1.add(STD_UNMODIFIABLE_SORTED_SET_CL = Collections.unmodifiableSortedSet(treeSet).getClass());
/*  96 */     hashSet1.add(STD_UNMODIFIABLE_MAP_CL = Collections.unmodifiableMap(hashMap).getClass());
/*  97 */     hashSet1.add(STD_UNMODIFIABLE_SORTED_MAP_CL = Collections.unmodifiableSortedMap(treeMap).getClass());
/*     */     
/*  99 */     hashSet2.add(STD_SYNCHRONIZED_COLLECTION_CL = Collections.synchronizedCollection(arrayList).getClass());
/* 100 */     hashSet2.add(STD_SYNCHRONIZED_SET_CL = Collections.synchronizedSet(hashSet).getClass());
/* 101 */     hashSet2.add(STD_SYNCHRONIZED_LIST_CL = Collections.synchronizedList(linkedList).getClass());
/* 102 */     hashSet2.add(STD_SYNCHRONIZED_RA_LIST_CL = Collections.synchronizedList(arrayList).getClass());
/* 103 */     hashSet2.add(STD_SYNCHRONIZED_SORTED_SET_CL = Collections.synchronizedSortedSet(treeSet).getClass());
/* 104 */     hashSet2.add(STD_SYNCHRONIZED_MAP_CL = Collections.synchronizedMap(hashMap).getClass());
/* 105 */     hashSet2.add(STD_SYNCHRONIZED_SORTED_MAP_CL = Collections.synchronizedMap(treeMap).getClass());
/*     */     
/* 107 */     UNMODIFIABLE_WRAPPERS = Collections.unmodifiableSet(hashSet1);
/*     */     
/* 109 */     SYNCHRONIZED_WRAPPERS = Collections.unmodifiableSet(hashSet2);
/*     */     
/* 111 */     HashSet<Class<?>> hashSet3 = new HashSet<Class<?>>(hashSet1);
/* 112 */     hashSet3.addAll(hashSet2);
/* 113 */     ALL_COLLECTIONS_WRAPPERS = Collections.unmodifiableSet(hashSet3);
/*     */   }
/*     */   
/*     */   public static boolean isCollectionsWrapper(Class paramClass) {
/* 117 */     return ALL_COLLECTIONS_WRAPPERS.contains(paramClass);
/*     */   }
/*     */   public static boolean isCollectionsWrapper(Collection paramCollection) {
/* 120 */     return isCollectionsWrapper(paramCollection.getClass());
/*     */   }
/*     */   public static boolean isCollectionsWrapper(Map paramMap) {
/* 123 */     return isCollectionsWrapper(paramMap.getClass());
/*     */   }
/*     */   public static boolean isSynchronizedWrapper(Class paramClass) {
/* 126 */     return SYNCHRONIZED_WRAPPERS.contains(paramClass);
/*     */   }
/*     */   public static boolean isSynchronizedWrapper(Collection paramCollection) {
/* 129 */     return isSynchronizedWrapper(paramCollection.getClass());
/*     */   }
/*     */   public static boolean isSynchronizedWrapper(Map paramMap) {
/* 132 */     return isSynchronizedWrapper(paramMap.getClass());
/*     */   }
/*     */   public static boolean isUnmodifiableWrapper(Class paramClass) {
/* 135 */     return UNMODIFIABLE_WRAPPERS.contains(paramClass);
/*     */   }
/*     */   public static boolean isUnmodifiableWrapper(Collection paramCollection) {
/* 138 */     return isUnmodifiableWrapper(paramCollection.getClass());
/*     */   }
/*     */   public static boolean isUnmodifiableWrapper(Map paramMap) {
/* 141 */     return isUnmodifiableWrapper(paramMap.getClass());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Collection narrowUnmodifiableCollection(Collection<?> paramCollection) {
/* 149 */     if (paramCollection instanceof SortedSet)
/* 150 */       return Collections.unmodifiableSortedSet((SortedSet)paramCollection); 
/* 151 */     if (paramCollection instanceof Set)
/* 152 */       return Collections.unmodifiableSet((Set)paramCollection); 
/* 153 */     if (paramCollection instanceof List) {
/* 154 */       return Collections.unmodifiableList((List)paramCollection);
/*     */     }
/* 156 */     return Collections.unmodifiableCollection(paramCollection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Collection narrowSynchronizedCollection(Collection<?> paramCollection) {
/* 165 */     if (paramCollection instanceof SortedSet)
/* 166 */       return Collections.synchronizedSortedSet((SortedSet)paramCollection); 
/* 167 */     if (paramCollection instanceof Set)
/* 168 */       return Collections.synchronizedSet((Set)paramCollection); 
/* 169 */     if (paramCollection instanceof List) {
/* 170 */       return Collections.synchronizedList((List)paramCollection);
/*     */     }
/* 172 */     return Collections.synchronizedCollection(paramCollection);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Map narrowUnmodifiableMap(Map<?, ?> paramMap) {
/* 177 */     if (paramMap instanceof SortedMap) {
/* 178 */       return Collections.unmodifiableSortedMap((SortedMap<?, ?>)paramMap);
/*     */     }
/* 180 */     return Collections.unmodifiableMap(paramMap);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Map narrowSynchronizedMap(Map<?, ?> paramMap) {
/* 185 */     if (paramMap instanceof SortedMap) {
/* 186 */       return Collections.synchronizedSortedMap((SortedMap<?, ?>)paramMap);
/*     */     }
/* 188 */     return Collections.synchronizedMap(paramMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Collection attemptClone(Collection paramCollection) throws NoSuchMethodException {
/* 197 */     if (paramCollection instanceof Vector) return (Collection)((Vector)paramCollection).clone(); 
/* 198 */     if (paramCollection instanceof ArrayList) return (Collection)((ArrayList)paramCollection).clone(); 
/* 199 */     if (paramCollection instanceof LinkedList) return (Collection)((LinkedList)paramCollection).clone(); 
/* 200 */     if (paramCollection instanceof HashSet) return (Collection)((HashSet)paramCollection).clone(); 
/* 201 */     if (paramCollection instanceof TreeSet) return (Collection)((TreeSet)paramCollection).clone();
/*     */ 
/*     */     
/* 204 */     Collection collection = null;
/* 205 */     Class<?> clazz = paramCollection.getClass();
/*     */     
/*     */     try {
/* 208 */       Method method = clazz.getMethod("clone", EMPTY_ARG_CLASSES);
/* 209 */       collection = (Collection)method.invoke(paramCollection, EMPTY_ARGS);
/*     */     }
/* 211 */     catch (Exception exception) {
/*     */ 
/*     */ 
/*     */       
/* 215 */       exception.printStackTrace();
/*     */     } 
/*     */     
/* 218 */     if (collection == null) {
/*     */       
/*     */       try {
/*     */         
/* 222 */         Constructor<?> constructor = clazz.getConstructor((paramCollection instanceof SortedSet) ? SORTED_SET_ARG_CLASSES : COLLECTION_ARG_CLASSES);
/* 223 */         collection = (Collection)constructor.newInstance(new Object[] { paramCollection });
/*     */       }
/* 225 */       catch (Exception exception) {
/*     */ 
/*     */ 
/*     */         
/* 229 */         exception.printStackTrace();
/*     */       } 
/*     */     }
/*     */     
/* 233 */     if (collection == null) {
/*     */       
/*     */       try {
/*     */         
/* 237 */         Constructor<?> constructor = clazz.getConstructor(new Class[] { clazz });
/* 238 */         collection = (Collection)constructor.newInstance(new Object[] { paramCollection });
/*     */       }
/* 240 */       catch (Exception exception) {
/*     */ 
/*     */ 
/*     */         
/* 244 */         exception.printStackTrace();
/*     */       } 
/*     */     }
/*     */     
/* 248 */     if (collection == null) {
/* 249 */       throw new NoSuchMethodException("No accessible clone() method or reasonable copy constructor could be called on Collection " + paramCollection);
/*     */     }
/* 251 */     return collection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map attemptClone(Map paramMap) throws NoSuchMethodException {
/* 261 */     if (paramMap instanceof Properties) return (Map)((Properties)paramMap).clone(); 
/* 262 */     if (paramMap instanceof Hashtable) return (Map)((Hashtable)paramMap).clone(); 
/* 263 */     if (paramMap instanceof HashMap) return (Map)((HashMap)paramMap).clone(); 
/* 264 */     if (paramMap instanceof TreeMap) return (Map)((TreeMap)paramMap).clone();
/*     */ 
/*     */     
/* 267 */     Map map = null;
/* 268 */     Class<?> clazz = paramMap.getClass();
/*     */     
/*     */     try {
/* 271 */       Method method = clazz.getMethod("clone", EMPTY_ARG_CLASSES);
/* 272 */       map = (Map)method.invoke(paramMap, EMPTY_ARGS);
/*     */     }
/* 274 */     catch (Exception exception) {
/*     */ 
/*     */ 
/*     */       
/* 278 */       exception.printStackTrace();
/*     */     } 
/*     */     
/* 281 */     if (map == null) {
/*     */       
/*     */       try {
/*     */         
/* 285 */         Constructor<?> constructor = clazz.getConstructor((paramMap instanceof SortedMap) ? SORTED_MAP_ARG_CLASSES : MAP_ARG_CLASSES);
/* 286 */         map = (Map)constructor.newInstance(new Object[] { paramMap });
/*     */       }
/* 288 */       catch (Exception exception) {
/*     */ 
/*     */ 
/*     */         
/* 292 */         exception.printStackTrace();
/*     */       } 
/*     */     }
/*     */     
/* 296 */     if (map == null) {
/*     */       
/*     */       try {
/*     */         
/* 300 */         Constructor<?> constructor = clazz.getConstructor(new Class[] { clazz });
/* 301 */         map = (Map)constructor.newInstance(new Object[] { paramMap });
/*     */       }
/* 303 */       catch (Exception exception) {
/*     */ 
/*     */ 
/*     */         
/* 307 */         exception.printStackTrace();
/*     */       } 
/*     */     }
/*     */     
/* 311 */     if (map == null) {
/* 312 */       throw new NoSuchMethodException("No accessible clone() method or reasonable copy constructor could be called on Map " + paramMap);
/*     */     }
/* 314 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void add(Collection<Object> paramCollection, Object paramObject) {
/* 325 */     paramCollection.add(paramObject);
/*     */   }
/*     */   public static void remove(Collection paramCollection, Object paramObject) {
/* 328 */     paramCollection.remove(paramObject);
/*     */   }
/*     */   
/*     */   public static int size(Object paramObject) {
/* 332 */     if (paramObject instanceof Collection)
/* 333 */       return ((Collection)paramObject).size(); 
/* 334 */     if (paramObject instanceof Map)
/* 335 */       return ((Map)paramObject).size(); 
/* 336 */     if (paramObject instanceof Object[])
/* 337 */       return ((Object[])paramObject).length; 
/* 338 */     if (paramObject instanceof boolean[])
/* 339 */       return ((boolean[])paramObject).length; 
/* 340 */     if (paramObject instanceof byte[])
/* 341 */       return ((byte[])paramObject).length; 
/* 342 */     if (paramObject instanceof char[])
/* 343 */       return ((char[])paramObject).length; 
/* 344 */     if (paramObject instanceof short[])
/* 345 */       return ((short[])paramObject).length; 
/* 346 */     if (paramObject instanceof int[])
/* 347 */       return ((int[])paramObject).length; 
/* 348 */     if (paramObject instanceof long[])
/* 349 */       return ((long[])paramObject).length; 
/* 350 */     if (paramObject instanceof float[])
/* 351 */       return ((float[])paramObject).length; 
/* 352 */     if (paramObject instanceof double[]) {
/* 353 */       return ((double[])paramObject).length;
/*     */     }
/* 355 */     throw new IllegalArgumentException(paramObject + " must be a Collection, Map, or array!");
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/util/CollectionUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */