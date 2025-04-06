/*     */ package ch.qos.logback.core.subst;
/*     */ 
/*     */ import ch.qos.logback.core.spi.ScanException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Tokenizer
/*     */ {
/*     */   final String pattern;
/*     */   final int patternLength;
/*     */   TokenizerState state;
/*     */   int pointer;
/*     */   
/*     */   enum TokenizerState
/*     */   {
/*  24 */     LITERAL_STATE, START_STATE, DEFAULT_VAL_STATE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tokenizer(String pattern) {
/*  34 */     this.state = TokenizerState.LITERAL_STATE;
/*  35 */     this.pointer = 0;
/*     */     this.pattern = pattern;
/*     */     this.patternLength = pattern.length(); } List<Token> tokenize() throws ScanException {
/*  38 */     List<Token> tokenList = new ArrayList<Token>();
/*  39 */     StringBuilder buf = new StringBuilder();
/*     */     
/*  41 */     while (this.pointer < this.patternLength) {
/*  42 */       char c = this.pattern.charAt(this.pointer);
/*  43 */       this.pointer++;
/*     */       
/*  45 */       switch (this.state) {
/*     */         case LITERAL_STATE:
/*  47 */           handleLiteralState(c, tokenList, buf);
/*     */         
/*     */         case START_STATE:
/*  50 */           handleStartState(c, tokenList, buf);
/*     */         
/*     */         case DEFAULT_VAL_STATE:
/*  53 */           handleDefaultValueState(c, tokenList, buf);
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/*  58 */     switch (this.state) {
/*     */       case LITERAL_STATE:
/*  60 */         addLiteralToken(tokenList, buf);
/*     */         break;
/*     */       case START_STATE:
/*  63 */         throw new ScanException("Unexpected end of pattern string");
/*     */     } 
/*  65 */     return tokenList;
/*     */   }
/*     */   
/*     */   private void handleDefaultValueState(char c, List<Token> tokenList, StringBuilder stringBuilder) {
/*  69 */     switch (c) {
/*     */       case '-':
/*  71 */         tokenList.add(Token.DEFAULT_SEP_TOKEN);
/*  72 */         this.state = TokenizerState.LITERAL_STATE;
/*     */         return;
/*     */       case '$':
/*  75 */         stringBuilder.append(':');
/*  76 */         addLiteralToken(tokenList, stringBuilder);
/*  77 */         stringBuilder.setLength(0);
/*  78 */         this.state = TokenizerState.START_STATE;
/*     */         return;
/*     */     } 
/*  81 */     stringBuilder.append(':').append(c);
/*  82 */     this.state = TokenizerState.LITERAL_STATE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleStartState(char c, List<Token> tokenList, StringBuilder stringBuilder) {
/*  88 */     if (c == '{') {
/*  89 */       tokenList.add(Token.START_TOKEN);
/*     */     } else {
/*  91 */       stringBuilder.append('$').append(c);
/*     */     } 
/*  93 */     this.state = TokenizerState.LITERAL_STATE;
/*     */   }
/*     */   
/*     */   private void handleLiteralState(char c, List<Token> tokenList, StringBuilder stringBuilder) {
/*  97 */     if (c == '$') {
/*  98 */       addLiteralToken(tokenList, stringBuilder);
/*  99 */       stringBuilder.setLength(0);
/* 100 */       this.state = TokenizerState.START_STATE;
/* 101 */     } else if (c == ':') {
/* 102 */       addLiteralToken(tokenList, stringBuilder);
/* 103 */       stringBuilder.setLength(0);
/* 104 */       this.state = TokenizerState.DEFAULT_VAL_STATE;
/* 105 */     } else if (c == '{') {
/* 106 */       addLiteralToken(tokenList, stringBuilder);
/* 107 */       tokenList.add(Token.CURLY_LEFT_TOKEN);
/* 108 */       stringBuilder.setLength(0);
/* 109 */     } else if (c == '}') {
/* 110 */       addLiteralToken(tokenList, stringBuilder);
/* 111 */       tokenList.add(Token.CURLY_RIGHT_TOKEN);
/* 112 */       stringBuilder.setLength(0);
/*     */     } else {
/* 114 */       stringBuilder.append(c);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void addLiteralToken(List<Token> tokenList, StringBuilder stringBuilder) {
/* 120 */     if (stringBuilder.length() == 0)
/*     */       return; 
/* 122 */     tokenList.add(new Token(Token.Type.LITERAL, stringBuilder.toString()));
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/subst/Tokenizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */