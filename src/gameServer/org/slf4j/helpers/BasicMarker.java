/*     */ package org.slf4j.helpers;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import org.slf4j.Marker;
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
/*     */ public class BasicMarker
/*     */   implements Marker
/*     */ {
/*     */   private static final long serialVersionUID = 1803952589649545191L;
/*     */   private final String name;
/*     */   private List<Marker> referenceList;
/*     */   
/*     */   BasicMarker(String name) {
/*  48 */     if (name == null) {
/*  49 */       throw new IllegalArgumentException("A marker name cannot be null");
/*     */     }
/*  51 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  55 */     return this.name;
/*     */   }
/*     */   
/*     */   public synchronized void add(Marker reference) {
/*  59 */     if (reference == null) {
/*  60 */       throw new IllegalArgumentException("A null value cannot be added to a Marker as reference.");
/*     */     }
/*     */ 
/*     */     
/*  64 */     if (contains(reference)) {
/*     */       return;
/*     */     }
/*  67 */     if (reference.contains(this)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  72 */     if (this.referenceList == null) {
/*  73 */       this.referenceList = new Vector<Marker>();
/*     */     }
/*  75 */     this.referenceList.add(reference);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean hasReferences() {
/*  81 */     return (this.referenceList != null && this.referenceList.size() > 0);
/*     */   }
/*     */   
/*     */   public boolean hasChildren() {
/*  85 */     return hasReferences();
/*     */   }
/*     */   
/*     */   public synchronized Iterator<Marker> iterator() {
/*  89 */     if (this.referenceList != null) {
/*  90 */       return this.referenceList.iterator();
/*     */     }
/*  92 */     List<Marker> emptyList = Collections.emptyList();
/*  93 */     return emptyList.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean remove(Marker referenceToRemove) {
/*  98 */     if (this.referenceList == null) {
/*  99 */       return false;
/*     */     }
/*     */     
/* 102 */     int size = this.referenceList.size();
/* 103 */     for (int i = 0; i < size; i++) {
/* 104 */       Marker m = this.referenceList.get(i);
/* 105 */       if (referenceToRemove.equals(m)) {
/* 106 */         this.referenceList.remove(i);
/* 107 */         return true;
/*     */       } 
/*     */     } 
/* 110 */     return false;
/*     */   }
/*     */   
/*     */   public boolean contains(Marker other) {
/* 114 */     if (other == null) {
/* 115 */       throw new IllegalArgumentException("Other cannot be null");
/*     */     }
/*     */     
/* 118 */     if (equals(other)) {
/* 119 */       return true;
/*     */     }
/*     */     
/* 122 */     if (hasReferences()) {
/* 123 */       for (int i = 0; i < this.referenceList.size(); i++) {
/* 124 */         Marker ref = this.referenceList.get(i);
/* 125 */         if (ref.contains(other)) {
/* 126 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 130 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(String name) {
/* 137 */     if (name == null) {
/* 138 */       throw new IllegalArgumentException("Other cannot be null");
/*     */     }
/*     */     
/* 141 */     if (this.name.equals(name)) {
/* 142 */       return true;
/*     */     }
/*     */     
/* 145 */     if (hasReferences()) {
/* 146 */       for (int i = 0; i < this.referenceList.size(); i++) {
/* 147 */         Marker ref = this.referenceList.get(i);
/* 148 */         if (ref.contains(name)) {
/* 149 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 153 */     return false;
/*     */   }
/*     */   
/* 156 */   private static String OPEN = "[ ";
/* 157 */   private static String CLOSE = " ]";
/* 158 */   private static String SEP = ", ";
/*     */   
/*     */   public boolean equals(Object obj) {
/* 161 */     if (this == obj)
/* 162 */       return true; 
/* 163 */     if (obj == null)
/* 164 */       return false; 
/* 165 */     if (!(obj instanceof Marker)) {
/* 166 */       return false;
/*     */     }
/* 168 */     Marker other = (Marker)obj;
/* 169 */     return this.name.equals(other.getName());
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 173 */     return this.name.hashCode();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 177 */     if (!hasReferences()) {
/* 178 */       return getName();
/*     */     }
/* 180 */     Iterator<Marker> it = iterator();
/*     */     
/* 182 */     StringBuilder sb = new StringBuilder(getName());
/* 183 */     sb.append(' ').append(OPEN);
/* 184 */     while (it.hasNext()) {
/* 185 */       Marker reference = it.next();
/* 186 */       sb.append(reference.getName());
/* 187 */       if (it.hasNext()) {
/* 188 */         sb.append(SEP);
/*     */       }
/*     */     } 
/* 191 */     sb.append(CLOSE);
/*     */     
/* 193 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/slf4j/helpers/BasicMarker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */