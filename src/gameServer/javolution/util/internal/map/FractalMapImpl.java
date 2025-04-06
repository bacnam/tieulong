/*     */ package javolution.util.internal.map;
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
/*     */ final class FractalMapImpl
/*     */ {
/*     */   static final int EMPTINESS_LEVEL = 2;
/*     */   static final int INITIAL_BLOCK_CAPACITY = 8;
/*     */   static final int SHIFT = 10;
/*     */   private int count;
/*  25 */   private MapEntryImpl[] entries = new MapEntryImpl[8];
/*     */   private final int shift;
/*     */   
/*     */   public FractalMapImpl() {
/*  29 */     this.shift = 0;
/*     */   }
/*     */   
/*     */   public FractalMapImpl(int shift) {
/*  33 */     this.shift = shift;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapEntryImpl addEntry(MapEntryImpl newEntry, Object key, int hash) {
/*  40 */     int i = indexOfKey(key, hash);
/*  41 */     MapEntryImpl entry = this.entries[i];
/*  42 */     if (entry != null) return entry; 
/*  43 */     this.entries[i] = newEntry;
/*  44 */     newEntry.key = (K)key;
/*  45 */     newEntry.hash = hash;
/*     */     
/*  47 */     if (++this.count << 2 > this.entries.length) {
/*  48 */       resize(this.entries.length << 1);
/*     */     }
/*  50 */     return newEntry;
/*     */   }
/*     */   
/*     */   public void clear() {
/*  54 */     this.entries = new MapEntryImpl[8];
/*  55 */     this.count = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public MapEntryImpl getEntry(Object key, int hash) {
/*  60 */     return this.entries[indexOfKey(key, hash)];
/*     */   }
/*     */ 
/*     */   
/*     */   public MapEntryImpl removeEntry(Object key, int hash) {
/*  65 */     int i = indexOfKey(key, hash);
/*  66 */     MapEntryImpl oldEntry = this.entries[i];
/*  67 */     if (oldEntry == null) return null; 
/*  68 */     this.entries[i] = null;
/*     */ 
/*     */     
/*     */     while (true) {
/*  72 */       i = i + 1 & this.entries.length - 1;
/*  73 */       MapEntryImpl entry = this.entries[i];
/*  74 */       if (entry == null)
/*  75 */         break;  int correctIndex = indexOfKey(entry.key, entry.hash);
/*  76 */       if (correctIndex != i) {
/*  77 */         this.entries[correctIndex] = this.entries[i];
/*  78 */         this.entries[i] = null;
/*     */       } 
/*     */     } 
/*     */     
/*  82 */     if (--this.count << 3 <= this.entries.length && this.entries.length > 8)
/*     */     {
/*  84 */       resize(this.entries.length >> 1);
/*     */     }
/*  86 */     return oldEntry;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int indexOfKey(Object key, int hash) {
/*  92 */     int mask = this.entries.length - 1;
/*  93 */     int i = hash >> this.shift & mask;
/*     */     while (true) {
/*  95 */       MapEntryImpl entry = this.entries[i];
/*  96 */       if (entry == null) return i; 
/*  97 */       if (entry.hash == hash && key.equals(entry.key)) return i; 
/*  98 */       i = i + 1 & mask;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void resize(int newCapacity) {
/* 106 */     MapEntryImpl[] newEntries = new MapEntryImpl[newCapacity];
/* 107 */     int newMask = newEntries.length - 1;
/* 108 */     for (int i = 0, n = this.entries.length; i < n; i++) {
/* 109 */       MapEntryImpl entry = this.entries[i];
/* 110 */       if (entry != null) {
/* 111 */         int newIndex = entry.hash & newMask;
/* 112 */         while (newEntries[newIndex] != null) {
/* 113 */           newIndex = newIndex + 1 & newMask;
/*     */         }
/* 115 */         newEntries[newIndex] = entry;
/*     */       } 
/* 117 */     }  this.entries = newEntries;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/map/FractalMapImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */