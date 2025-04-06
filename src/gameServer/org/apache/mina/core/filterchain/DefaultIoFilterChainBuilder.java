/*     */ package org.apache.mina.core.filterchain;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public class DefaultIoFilterChainBuilder
/*     */   implements IoFilterChainBuilder
/*     */ {
/*  64 */   private static final Logger LOGGER = LoggerFactory.getLogger(DefaultIoFilterChainBuilder.class);
/*     */ 
/*     */   
/*     */   private final List<IoFilterChain.Entry> entries;
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultIoFilterChainBuilder() {
/*  72 */     this.entries = new CopyOnWriteArrayList<IoFilterChain.Entry>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultIoFilterChainBuilder(DefaultIoFilterChainBuilder filterChain) {
/*  79 */     if (filterChain == null) {
/*  80 */       throw new IllegalArgumentException("filterChain");
/*     */     }
/*  82 */     this.entries = new CopyOnWriteArrayList<IoFilterChain.Entry>(filterChain.entries);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoFilterChain.Entry getEntry(String name) {
/*  89 */     for (IoFilterChain.Entry e : this.entries) {
/*  90 */       if (e.getName().equals(name)) {
/*  91 */         return e;
/*     */       }
/*     */     } 
/*     */     
/*  95 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoFilterChain.Entry getEntry(IoFilter filter) {
/* 102 */     for (IoFilterChain.Entry e : this.entries) {
/* 103 */       if (e.getFilter() == filter) {
/* 104 */         return e;
/*     */       }
/*     */     } 
/*     */     
/* 108 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoFilterChain.Entry getEntry(Class<? extends IoFilter> filterType) {
/* 115 */     for (IoFilterChain.Entry e : this.entries) {
/* 116 */       if (filterType.isAssignableFrom(e.getFilter().getClass())) {
/* 117 */         return e;
/*     */       }
/*     */     } 
/*     */     
/* 121 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoFilter get(String name) {
/* 128 */     IoFilterChain.Entry e = getEntry(name);
/* 129 */     if (e == null) {
/* 130 */       return null;
/*     */     }
/*     */     
/* 133 */     return e.getFilter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoFilter get(Class<? extends IoFilter> filterType) {
/* 140 */     IoFilterChain.Entry e = getEntry(filterType);
/* 141 */     if (e == null) {
/* 142 */       return null;
/*     */     }
/*     */     
/* 145 */     return e.getFilter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<IoFilterChain.Entry> getAll() {
/* 152 */     return new ArrayList<IoFilterChain.Entry>(this.entries);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<IoFilterChain.Entry> getAllReversed() {
/* 159 */     List<IoFilterChain.Entry> result = getAll();
/* 160 */     Collections.reverse(result);
/* 161 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(String name) {
/* 168 */     return (getEntry(name) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(IoFilter filter) {
/* 175 */     return (getEntry(filter) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Class<? extends IoFilter> filterType) {
/* 182 */     return (getEntry(filterType) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addFirst(String name, IoFilter filter) {
/* 189 */     register(0, new EntryImpl(name, filter));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addLast(String name, IoFilter filter) {
/* 196 */     register(this.entries.size(), new EntryImpl(name, filter));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addBefore(String baseName, String name, IoFilter filter) {
/* 203 */     checkBaseName(baseName);
/*     */     
/* 205 */     for (ListIterator<IoFilterChain.Entry> i = this.entries.listIterator(); i.hasNext(); ) {
/* 206 */       IoFilterChain.Entry base = i.next();
/* 207 */       if (base.getName().equals(baseName)) {
/* 208 */         register(i.previousIndex(), new EntryImpl(name, filter));
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addAfter(String baseName, String name, IoFilter filter) {
/* 218 */     checkBaseName(baseName);
/*     */     
/* 220 */     for (ListIterator<IoFilterChain.Entry> i = this.entries.listIterator(); i.hasNext(); ) {
/* 221 */       IoFilterChain.Entry base = i.next();
/* 222 */       if (base.getName().equals(baseName)) {
/* 223 */         register(i.nextIndex(), new EntryImpl(name, filter));
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized IoFilter remove(String name) {
/* 233 */     if (name == null) {
/* 234 */       throw new IllegalArgumentException("name");
/*     */     }
/*     */     
/* 237 */     for (ListIterator<IoFilterChain.Entry> i = this.entries.listIterator(); i.hasNext(); ) {
/* 238 */       IoFilterChain.Entry e = i.next();
/* 239 */       if (e.getName().equals(name)) {
/* 240 */         this.entries.remove(i.previousIndex());
/* 241 */         return e.getFilter();
/*     */       } 
/*     */     } 
/*     */     
/* 245 */     throw new IllegalArgumentException("Unknown filter name: " + name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized IoFilter remove(IoFilter filter) {
/* 252 */     if (filter == null) {
/* 253 */       throw new IllegalArgumentException("filter");
/*     */     }
/*     */     
/* 256 */     for (ListIterator<IoFilterChain.Entry> i = this.entries.listIterator(); i.hasNext(); ) {
/* 257 */       IoFilterChain.Entry e = i.next();
/* 258 */       if (e.getFilter() == filter) {
/* 259 */         this.entries.remove(i.previousIndex());
/* 260 */         return e.getFilter();
/*     */       } 
/*     */     } 
/*     */     
/* 264 */     throw new IllegalArgumentException("Filter not found: " + filter.getClass().getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized IoFilter remove(Class<? extends IoFilter> filterType) {
/* 271 */     if (filterType == null) {
/* 272 */       throw new IllegalArgumentException("filterType");
/*     */     }
/*     */     
/* 275 */     for (ListIterator<IoFilterChain.Entry> i = this.entries.listIterator(); i.hasNext(); ) {
/* 276 */       IoFilterChain.Entry e = i.next();
/* 277 */       if (filterType.isAssignableFrom(e.getFilter().getClass())) {
/* 278 */         this.entries.remove(i.previousIndex());
/* 279 */         return e.getFilter();
/*     */       } 
/*     */     } 
/*     */     
/* 283 */     throw new IllegalArgumentException("Filter not found: " + filterType.getName());
/*     */   }
/*     */   
/*     */   public synchronized IoFilter replace(String name, IoFilter newFilter) {
/* 287 */     checkBaseName(name);
/* 288 */     EntryImpl e = (EntryImpl)getEntry(name);
/* 289 */     IoFilter oldFilter = e.getFilter();
/* 290 */     e.setFilter(newFilter);
/* 291 */     return oldFilter;
/*     */   }
/*     */   
/*     */   public synchronized void replace(IoFilter oldFilter, IoFilter newFilter) {
/* 295 */     for (IoFilterChain.Entry e : this.entries) {
/* 296 */       if (e.getFilter() == oldFilter) {
/* 297 */         ((EntryImpl)e).setFilter(newFilter);
/*     */         return;
/*     */       } 
/*     */     } 
/* 301 */     throw new IllegalArgumentException("Filter not found: " + oldFilter.getClass().getName());
/*     */   }
/*     */   
/*     */   public synchronized void replace(Class<? extends IoFilter> oldFilterType, IoFilter newFilter) {
/* 305 */     for (IoFilterChain.Entry e : this.entries) {
/* 306 */       if (oldFilterType.isAssignableFrom(e.getFilter().getClass())) {
/* 307 */         ((EntryImpl)e).setFilter(newFilter);
/*     */         return;
/*     */       } 
/*     */     } 
/* 311 */     throw new IllegalArgumentException("Filter not found: " + oldFilterType.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void clear() {
/* 318 */     this.entries.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFilters(Map<String, ? extends IoFilter> filters) {
/* 329 */     if (filters == null) {
/* 330 */       throw new IllegalArgumentException("filters");
/*     */     }
/*     */     
/* 333 */     if (!isOrderedMap(filters)) {
/* 334 */       throw new IllegalArgumentException("filters is not an ordered map. Please try " + LinkedHashMap.class.getName() + ".");
/*     */     }
/*     */ 
/*     */     
/* 338 */     filters = new LinkedHashMap<String, IoFilter>(filters);
/* 339 */     for (Map.Entry<String, ? extends IoFilter> e : filters.entrySet()) {
/* 340 */       if (e.getKey() == null) {
/* 341 */         throw new IllegalArgumentException("filters contains a null key.");
/*     */       }
/* 343 */       if (e.getValue() == null) {
/* 344 */         throw new IllegalArgumentException("filters contains a null value.");
/*     */       }
/*     */     } 
/*     */     
/* 348 */     synchronized (this) {
/* 349 */       clear();
/* 350 */       for (Map.Entry<String, ? extends IoFilter> e : filters.entrySet()) {
/* 351 */         addLast(e.getKey(), e.getValue());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isOrderedMap(Map map) {
/*     */     Map<String, IoFilter> newMap;
/* 358 */     Class<?> mapType = map.getClass();
/* 359 */     if (LinkedHashMap.class.isAssignableFrom(mapType)) {
/* 360 */       if (LOGGER.isDebugEnabled()) {
/* 361 */         LOGGER.debug(mapType.getSimpleName() + " is an ordered map.");
/*     */       }
/* 363 */       return true;
/*     */     } 
/*     */     
/* 366 */     if (LOGGER.isDebugEnabled()) {
/* 367 */       LOGGER.debug(mapType.getName() + " is not a " + LinkedHashMap.class.getSimpleName());
/*     */     }
/*     */ 
/*     */     
/* 371 */     Class<?> type = mapType;
/* 372 */     while (type != null) {
/* 373 */       for (Class<?> clazz : type.getInterfaces()) {
/* 374 */         if (clazz.getName().endsWith("OrderedMap")) {
/* 375 */           if (LOGGER.isDebugEnabled()) {
/* 376 */             LOGGER.debug(mapType.getSimpleName() + " is an ordered map (guessed from that it " + " implements OrderedMap interface.)");
/*     */           }
/*     */           
/* 379 */           return true;
/*     */         } 
/*     */       } 
/* 382 */       type = type.getSuperclass();
/*     */     } 
/*     */     
/* 385 */     if (LOGGER.isDebugEnabled()) {
/* 386 */       LOGGER.debug(mapType.getName() + " doesn't implement OrderedMap interface.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 391 */     LOGGER.debug("Last resort; trying to create a new map instance with a default constructor and test if insertion order is maintained.");
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 396 */       newMap = (Map)mapType.newInstance();
/* 397 */     } catch (Exception e) {
/* 398 */       if (LOGGER.isDebugEnabled()) {
/* 399 */         LOGGER.debug("Failed to create a new map instance of '" + mapType.getName() + "'.", e);
/*     */       }
/* 401 */       return false;
/*     */     } 
/*     */     
/* 404 */     Random rand = new Random();
/* 405 */     List<String> expectedNames = new ArrayList<String>();
/* 406 */     IoFilter dummyFilter = new IoFilterAdapter();
/* 407 */     for (int i = 0; i < 65536; i++) {
/*     */       String filterName;
/*     */       do {
/* 410 */         filterName = String.valueOf(rand.nextInt());
/* 411 */       } while (newMap.containsKey(filterName));
/*     */       
/* 413 */       newMap.put(filterName, dummyFilter);
/* 414 */       expectedNames.add(filterName);
/*     */       
/* 416 */       Iterator<String> it = expectedNames.iterator();
/* 417 */       for (String key : newMap.keySet()) {
/* 418 */         if (!((String)it.next()).equals(key)) {
/* 419 */           if (LOGGER.isDebugEnabled()) {
/* 420 */             LOGGER.debug("The specified map didn't pass the insertion order test after " + (i + 1) + " tries.");
/*     */           }
/*     */           
/* 423 */           return false;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 428 */     if (LOGGER.isDebugEnabled()) {
/* 429 */       LOGGER.debug("The specified map passed the insertion order test.");
/*     */     }
/* 431 */     return true;
/*     */   }
/*     */   
/*     */   public void buildFilterChain(IoFilterChain chain) throws Exception {
/* 435 */     for (IoFilterChain.Entry e : this.entries) {
/* 436 */       chain.addLast(e.getName(), e.getFilter());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 442 */     StringBuilder buf = new StringBuilder();
/* 443 */     buf.append("{ ");
/*     */     
/* 445 */     boolean empty = true;
/*     */     
/* 447 */     for (IoFilterChain.Entry e : this.entries) {
/* 448 */       if (!empty) {
/* 449 */         buf.append(", ");
/*     */       } else {
/* 451 */         empty = false;
/*     */       } 
/*     */       
/* 454 */       buf.append('(');
/* 455 */       buf.append(e.getName());
/* 456 */       buf.append(':');
/* 457 */       buf.append(e.getFilter());
/* 458 */       buf.append(')');
/*     */     } 
/*     */     
/* 461 */     if (empty) {
/* 462 */       buf.append("empty");
/*     */     }
/*     */     
/* 465 */     buf.append(" }");
/*     */     
/* 467 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private void checkBaseName(String baseName) {
/* 471 */     if (baseName == null) {
/* 472 */       throw new IllegalArgumentException("baseName");
/*     */     }
/*     */     
/* 475 */     if (!contains(baseName)) {
/* 476 */       throw new IllegalArgumentException("Unknown filter name: " + baseName);
/*     */     }
/*     */   }
/*     */   
/*     */   private void register(int index, IoFilterChain.Entry e) {
/* 481 */     if (contains(e.getName())) {
/* 482 */       throw new IllegalArgumentException("Other filter is using the same name: " + e.getName());
/*     */     }
/*     */     
/* 485 */     this.entries.add(index, e);
/*     */   }
/*     */   
/*     */   private class EntryImpl
/*     */     implements IoFilterChain.Entry {
/*     */     private final String name;
/*     */     private volatile IoFilter filter;
/*     */     
/*     */     private EntryImpl(String name, IoFilter filter) {
/* 494 */       if (name == null) {
/* 495 */         throw new IllegalArgumentException("name");
/*     */       }
/* 497 */       if (filter == null) {
/* 498 */         throw new IllegalArgumentException("filter");
/*     */       }
/*     */       
/* 501 */       this.name = name;
/* 502 */       this.filter = filter;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 506 */       return this.name;
/*     */     }
/*     */     
/*     */     public IoFilter getFilter() {
/* 510 */       return this.filter;
/*     */     }
/*     */     
/*     */     private void setFilter(IoFilter filter) {
/* 514 */       this.filter = filter;
/*     */     }
/*     */     
/*     */     public IoFilter.NextFilter getNextFilter() {
/* 518 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 523 */       return "(" + getName() + ':' + this.filter + ')';
/*     */     }
/*     */     
/*     */     public void addAfter(String name, IoFilter filter) {
/* 527 */       DefaultIoFilterChainBuilder.this.addAfter(getName(), name, filter);
/*     */     }
/*     */     
/*     */     public void addBefore(String name, IoFilter filter) {
/* 531 */       DefaultIoFilterChainBuilder.this.addBefore(getName(), name, filter);
/*     */     }
/*     */     
/*     */     public void remove() {
/* 535 */       DefaultIoFilterChainBuilder.this.remove(getName());
/*     */     }
/*     */     
/*     */     public void replace(IoFilter newFilter) {
/* 539 */       DefaultIoFilterChainBuilder.this.replace(getName(), newFilter);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/filterchain/DefaultIoFilterChainBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */