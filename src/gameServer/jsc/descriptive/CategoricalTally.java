/*     */ package jsc.descriptive;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import jsc.util.CaseInsensitiveVector;
/*     */ import jsc.util.Sort;
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
/*     */ public class CategoricalTally
/*     */   extends AbstractFrequencyTable
/*     */   implements Cloneable
/*     */ {
/*  31 */   public static final String[] LETTER_LABELS = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  36 */   protected Vector labels = new Vector();
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
/*     */   public CategoricalTally(String paramString, String[] paramArrayOfString) {
/*  48 */     super(paramString);
/*     */     
/*  50 */     this.labels = Sort.getLabels(paramArrayOfString);
/*  51 */     this.numberOfBins = this.labels.size();
/*     */ 
/*     */     
/*  54 */     this.n = 0;
/*  55 */     this.freq = new int[this.numberOfBins]; byte b;
/*  56 */     for (b = 0; b < this.numberOfBins; ) { this.freq[b] = 0; b++; }
/*  57 */      for (b = 0; b < paramArrayOfString.length; ) { addValue(paramArrayOfString[b]); b++; }
/*     */   
/*     */   }
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
/*     */   public CategoricalTally(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2) {
/*  77 */     super(paramString);
/*     */     
/*  79 */     this.numberOfBins = paramArrayOfString1.length;
/*  80 */     this.labels = (Vector)new CaseInsensitiveVector(this.numberOfBins); byte b;
/*  81 */     for (b = 0; b < this.numberOfBins; ) { this.labels.add(paramArrayOfString1[b]); b++; }
/*     */ 
/*     */     
/*  84 */     this.n = 0;
/*  85 */     this.freq = new int[this.numberOfBins];
/*  86 */     for (b = 0; b < this.numberOfBins; ) { this.freq[b] = 0; b++; }
/*  87 */      for (b = 0; b < paramArrayOfString2.length; ) { addValue(paramArrayOfString2[b]); b++; }
/*     */   
/*     */   }
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
/*     */   public CategoricalTally(String paramString, String[] paramArrayOfString, int[] paramArrayOfint) {
/* 106 */     super(paramString);
/*     */     
/* 108 */     this.numberOfBins = paramArrayOfString.length;
/* 109 */     if (this.numberOfBins != paramArrayOfint.length)
/* 110 */       throw new IllegalArgumentException("Arrays not same length."); 
/* 111 */     this.labels = (Vector)new CaseInsensitiveVector(this.numberOfBins); byte b;
/* 112 */     for (b = 0; b < this.numberOfBins; ) { this.labels.add(paramArrayOfString[b]); b++; }
/*     */ 
/*     */     
/* 115 */     this.n = 0;
/* 116 */     this.freq = new int[this.numberOfBins];
/* 117 */     for (b = 0; b < this.numberOfBins; ) { this.freq[b] = paramArrayOfint[b]; this.n += paramArrayOfint[b]; b++; }
/*     */   
/*     */   }
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
/*     */   public boolean addValue(String paramString) {
/* 133 */     int i = this.labels.indexOf(paramString);
/* 134 */     if (i < 0) return false; 
/* 135 */     this.n++;
/* 136 */     this.freq[i] = this.freq[i] + 1;
/* 137 */     return true;
/*     */   }
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
/*     */   public Object clone() {
/* 159 */     Object object = null; try {
/* 160 */       object = super.clone();
/*     */     } catch (CloneNotSupportedException cloneNotSupportedException) {
/* 162 */       System.out.println("CategoricalTally can't clone");
/* 163 */     }  return object;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLabel(int paramInt) {
/* 173 */     return this.labels.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getLabels() {
/* 182 */     String[] arrayOfString = new String[this.labels.size()];
/* 183 */     return (String[])this.labels.toArray((Object[])arrayOfString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(String paramString) {
/* 192 */     return this.labels.indexOf(paramString);
/*     */   }
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 217 */       String[] arrayOfString = { "D", "B", "C", "D", "A", "e", "B", "a", "c", "A", "B", "E", "A", "b", "C", "a", "E", "c", "E" };
/*     */       
/* 219 */       CategoricalTally categoricalTally = new CategoricalTally("Test", arrayOfString);
/*     */       
/* 221 */       System.out.println("Tally " + categoricalTally.getN() + " values");
/* 222 */       for (byte b = 0; b < categoricalTally.getNumberOfBins(); b++)
/* 223 */         System.out.println(categoricalTally.getLabel(b) + ", Freq = " + categoricalTally.getFrequency(b) + ", % = " + categoricalTally.getPercentage(b)); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/descriptive/CategoricalTally.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */