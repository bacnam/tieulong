/*     */ package javolution.test;
/*     */ 
/*     */ import javolution.context.LogContext;
/*     */ import javolution.lang.Configurable;
/*     */ import javolution.lang.MathLib;
/*     */ import javolution.text.TextBuilder;
/*     */ import javolution.util.FastTable;
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
/*     */ 
/*     */ public abstract class Perfometer<T>
/*     */ {
/*  58 */   public static final Configurable<Integer> DURATION_MS = new Configurable<Integer>()
/*     */     {
/*     */       public String getName()
/*     */       {
/*  62 */         return getClass().getEnclosingClass().getName() + "#DURATION_MS";
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       protected Integer getDefault() {
/*  68 */         return Integer.valueOf(1000);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public static final Configurable<Boolean> SKIP = new Configurable<Boolean>()
/*     */     {
/*     */       public String getName()
/*     */       {
/*  82 */         return getClass().getEnclosingClass().getName() + "#SKIP";
/*     */       }
/*     */ 
/*     */       
/*     */       protected Boolean getDefault() {
/*  87 */         return Boolean.valueOf(false);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   private final String description;
/*     */   
/*     */   private T input;
/*     */   
/*     */   private long[] times;
/*     */ 
/*     */   
/*     */   public Perfometer(String description) {
/* 101 */     this.description = description;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getAvgTimeInSeconds() {
/* 108 */     if (this.times == null) return Double.NaN; 
/* 109 */     long sum = 0L;
/* 110 */     for (long time : this.times) {
/* 111 */       sum += time;
/*     */     }
/* 113 */     return sum / 1.0E9D / this.times.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 120 */     return this.description;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T getInput() {
/* 127 */     return this.input;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNbrOfIterations() {
/* 134 */     return (this.times != null) ? this.times.length : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getTimesInSeconds() {
/* 141 */     if (this.times == null) return new double[0]; 
/* 142 */     double[] timesSec = new double[this.times.length];
/* 143 */     for (int i = 0; i < this.times.length; i++) {
/* 144 */       timesSec[i] = this.times[i] / 1.0E9D;
/*     */     }
/* 146 */     return timesSec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Perfometer<T> measure(T input, int nbrOfIterations) {
/* 157 */     if (((Boolean)SKIP.get()).booleanValue()) return this; 
/* 158 */     this.input = input;
/* 159 */     this.times = new long[nbrOfIterations];
/* 160 */     long[] calibrations = longArray(nbrOfIterations, Long.MAX_VALUE);
/* 161 */     long[] measures = longArray(nbrOfIterations, Long.MAX_VALUE);
/*     */     try {
/* 163 */       long exitTime = System.currentTimeMillis() + ((Integer)DURATION_MS.get()).intValue();
/*     */       
/*     */       while (true)
/* 166 */       { initialize(); int i;
/* 167 */         for (i = 0; i < nbrOfIterations; i++) {
/* 168 */           long start = System.nanoTime();
/* 169 */           run(false);
/* 170 */           long time = System.nanoTime() - start;
/* 171 */           calibrations[i] = MathLib.min(calibrations[i], time);
/*     */         } 
/*     */         
/* 174 */         initialize();
/* 175 */         for (i = 0; i < nbrOfIterations; i++) {
/* 176 */           long start = System.nanoTime();
/* 177 */           run(true);
/* 178 */           long time = System.nanoTime() - start;
/* 179 */           measures[i] = MathLib.min(measures[i], time);
/*     */         } 
/* 181 */         if (System.currentTimeMillis() >= exitTime)
/* 182 */         { for (i = 0; i < nbrOfIterations; i++) {
/* 183 */             this.times[i] = measures[i] - calibrations[i];
/*     */           }
/* 185 */           return this; }  } 
/* 186 */     } catch (Exception error) {
/* 187 */       throw new RuntimeException("Perfometer Exception", error);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print() {
/* 195 */     if (((Boolean)SKIP.get()).booleanValue())
/* 196 */       return;  TextBuilder txt = new TextBuilder();
/* 197 */     txt.append(this.description).append(" (").append(getNbrOfIterations()).append(") for ").append(this.input).append(": ");
/*     */     
/* 199 */     while (txt.length() < 80)
/* 200 */       txt.append(' '); 
/* 201 */     txt.append(getAvgTimeInSeconds() * 1.0E9D, 8, false, true);
/* 202 */     txt.append(" ns (avg), ");
/* 203 */     txt.append(getWCETinSeconds() * 1.0E9D, 8, false, true);
/* 204 */     txt.append(" ns (wcet#").append(getWorstCaseNumber()).append(")");
/* 205 */     LogContext.info(new Object[] { txt });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printDetails() {
/* 212 */     if (((Boolean)SKIP.get()).booleanValue())
/* 213 */       return;  FastTable<Long> measurements = new FastTable();
/* 214 */     for (long time : this.times)
/* 215 */       measurements.add(Long.valueOf(time)); 
/* 216 */     LogContext.debug(new Object[] { measurements });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getWCETinSeconds() {
/* 223 */     if (this.times == null) return Double.NaN; 
/* 224 */     long wcet = 0L;
/* 225 */     for (long time : this.times) {
/* 226 */       if (time > wcet) wcet = time; 
/*     */     } 
/* 228 */     return wcet / 1.0E9D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWorstCaseNumber() {
/* 235 */     if (this.times == null) return -1; 
/* 236 */     long wcet = 0L;
/* 237 */     int j = -1;
/* 238 */     for (int i = 0; i < this.times.length; i++) {
/* 239 */       if (this.times[i] > wcet) {
/* 240 */         wcet = this.times[i];
/* 241 */         j = i;
/*     */       } 
/*     */     } 
/* 244 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void initialize() throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void run(boolean paramBoolean) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long[] longArray(int length, long initialValue) {
/* 266 */     long[] array = new long[length];
/* 267 */     for (int i = 0; i < length; i++)
/* 268 */       array[i] = initialValue; 
/* 269 */     return array;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/test/Perfometer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */