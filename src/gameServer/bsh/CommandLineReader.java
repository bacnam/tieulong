/*     */ package bsh;
/*     */ 
/*     */ import java.io.FilterReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
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
/*     */ class CommandLineReader
/*     */   extends FilterReader
/*     */ {
/*     */   static final int normal = 0;
/*     */   static final int lastCharNL = 1;
/*     */   static final int sentSemi = 2;
/*     */   int state;
/*     */   
/*     */   public CommandLineReader(Reader in) {
/*  50 */     super(in);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  58 */     this.state = 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  63 */     if (this.state == 2) {
/*  64 */       this.state = 1;
/*  65 */       return 10;
/*     */     } 
/*     */     
/*     */     int b;
/*  69 */     while ((b = this.in.read()) == 13);
/*     */     
/*  71 */     if (b == 10)
/*  72 */     { if (this.state == 1) {
/*  73 */         b = 59;
/*  74 */         this.state = 2;
/*     */       } else {
/*  76 */         this.state = 1;
/*     */       }  }
/*  78 */     else { this.state = 0; }
/*     */     
/*  80 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(char[] buff, int off, int len) throws IOException {
/*  90 */     int b = read();
/*  91 */     if (b == -1) {
/*  92 */       return -1;
/*     */     }
/*  94 */     buff[off] = (char)b;
/*  95 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/* 101 */     Reader in = new CommandLineReader(new InputStreamReader(System.in));
/*     */     while (true)
/* 103 */       System.out.println(in.read()); 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/CommandLineReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */