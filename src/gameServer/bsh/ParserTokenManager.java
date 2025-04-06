/*      */ package bsh;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ 
/*      */ public class ParserTokenManager
/*      */   implements ParserConstants {
/*    8 */   public PrintStream debugStream = System.out; public void setDebugStream(PrintStream ds) {
/*    9 */     this.debugStream = ds;
/*      */   }
/*      */   private final int jjStopStringLiteralDfa_0(int pos, long active0, long active1, long active2) {
/*   12 */     switch (pos) {
/*      */       
/*      */       case 0:
/*   15 */         if ((active1 & 0x200020000000000L) != 0L)
/*   16 */           return 56; 
/*   17 */         if ((active0 & 0x3EL) != 0L)
/*   18 */           return 0; 
/*   19 */         if ((active1 & 0x10000L) != 0L)
/*   20 */           return 11; 
/*   21 */         if ((active0 & 0xFFFFFFFFFFFFC00L) != 0L) {
/*      */           
/*   23 */           this.jjmatchedKind = 69;
/*   24 */           return 35;
/*      */         } 
/*   26 */         return -1;
/*      */       case 1:
/*   28 */         if ((active0 & 0x100600000L) != 0L)
/*   29 */           return 35; 
/*   30 */         if ((active0 & 0xFFFFFFEFF9FFC00L) != 0L) {
/*      */           
/*   32 */           if (this.jjmatchedPos != 1) {
/*      */             
/*   34 */             this.jjmatchedKind = 69;
/*   35 */             this.jjmatchedPos = 1;
/*      */           } 
/*   37 */           return 35;
/*      */         } 
/*   39 */         return -1;
/*      */       case 2:
/*   41 */         if ((active0 & 0xEFFFECEBFDFFC00L) != 0L) {
/*      */           
/*   43 */           if (this.jjmatchedPos != 2) {
/*      */             
/*   45 */             this.jjmatchedKind = 69;
/*   46 */             this.jjmatchedPos = 2;
/*      */           } 
/*   48 */           return 35;
/*      */         } 
/*   50 */         if ((active0 & 0x100013040000000L) != 0L)
/*   51 */           return 35; 
/*   52 */         return -1;
/*      */       case 3:
/*   54 */         if ((active0 & 0xC7FFCAE3E5D3C00L) != 0L) {
/*      */           
/*   56 */           if (this.jjmatchedPos != 3) {
/*      */             
/*   58 */             this.jjmatchedKind = 69;
/*   59 */             this.jjmatchedPos = 3;
/*      */           } 
/*   61 */           return 35;
/*      */         } 
/*   63 */         if ((active0 & 0x28002408182C000L) != 0L)
/*   64 */           return 35; 
/*   65 */         return -1;
/*      */       case 4:
/*   67 */         if ((active0 & 0x86080003C053000L) != 0L)
/*   68 */           return 35; 
/*   69 */         if ((active0 & 0x41F7CAE02580C00L) != 0L) {
/*      */           
/*   71 */           if (this.jjmatchedPos != 4) {
/*      */             
/*   73 */             this.jjmatchedKind = 69;
/*   74 */             this.jjmatchedPos = 4;
/*      */           } 
/*   76 */           return 35;
/*      */         } 
/*   78 */         return -1;
/*      */       case 5:
/*   80 */         if ((active0 & 0x41A1C2A12180C00L) != 0L) {
/*      */           
/*   82 */           this.jjmatchedKind = 69;
/*   83 */           this.jjmatchedPos = 5;
/*   84 */           return 35;
/*      */         } 
/*   86 */         if ((active0 & 0x45608400400000L) != 0L)
/*   87 */           return 35; 
/*   88 */         return -1;
/*      */       case 6:
/*   90 */         if ((active0 & 0x41A102A00080400L) != 0L) {
/*      */           
/*   92 */           this.jjmatchedKind = 69;
/*   93 */           this.jjmatchedPos = 6;
/*   94 */           return 35;
/*      */         } 
/*   96 */         if ((active0 & 0xC0012100800L) != 0L)
/*   97 */           return 35; 
/*   98 */         return -1;
/*      */       case 7:
/*  100 */         if ((active0 & 0x402000000080400L) != 0L)
/*  101 */           return 35; 
/*  102 */         if ((active0 & 0x18102A00000000L) != 0L) {
/*      */           
/*  104 */           this.jjmatchedKind = 69;
/*  105 */           this.jjmatchedPos = 7;
/*  106 */           return 35;
/*      */         } 
/*  108 */         return -1;
/*      */       case 8:
/*  110 */         if ((active0 & 0x8000A00000000L) != 0L) {
/*      */           
/*  112 */           this.jjmatchedKind = 69;
/*  113 */           this.jjmatchedPos = 8;
/*  114 */           return 35;
/*      */         } 
/*  116 */         if ((active0 & 0x10102000000000L) != 0L)
/*  117 */           return 35; 
/*  118 */         return -1;
/*      */       case 9:
/*  120 */         if ((active0 & 0x8000000000000L) != 0L) {
/*      */           
/*  122 */           this.jjmatchedKind = 69;
/*  123 */           this.jjmatchedPos = 9;
/*  124 */           return 35;
/*      */         } 
/*  126 */         if ((active0 & 0xA00000000L) != 0L)
/*  127 */           return 35; 
/*  128 */         return -1;
/*      */       case 10:
/*  130 */         if ((active0 & 0x8000000000000L) != 0L) {
/*      */           
/*  132 */           if (this.jjmatchedPos != 10) {
/*      */             
/*  134 */             this.jjmatchedKind = 69;
/*  135 */             this.jjmatchedPos = 10;
/*      */           } 
/*  137 */           return 35;
/*      */         } 
/*  139 */         return -1;
/*      */       case 11:
/*  141 */         if ((active0 & 0x8000000000000L) != 0L)
/*  142 */           return 35; 
/*  143 */         return -1;
/*      */     } 
/*  145 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private final int jjStartNfa_0(int pos, long active0, long active1, long active2) {
/*  150 */     return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0, active1, active2), pos + 1);
/*      */   }
/*      */   
/*      */   private final int jjStopAtPos(int pos, int kind) {
/*  154 */     this.jjmatchedKind = kind;
/*  155 */     this.jjmatchedPos = pos;
/*  156 */     return pos + 1;
/*      */   }
/*      */   
/*      */   private final int jjStartNfaWithStates_0(int pos, int kind, int state) {
/*  160 */     this.jjmatchedKind = kind;
/*  161 */     this.jjmatchedPos = pos; 
/*  162 */     try { this.curChar = this.input_stream.readChar(); }
/*  163 */     catch (IOException e) { return pos + 1; }
/*  164 */      return jjMoveNfa_0(state, pos + 1);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa0_0() {
/*  168 */     switch (this.curChar) {
/*      */       
/*      */       case '\t':
/*  171 */         return jjStartNfaWithStates_0(0, 2, 0);
/*      */       case '\n':
/*  173 */         return jjStartNfaWithStates_0(0, 5, 0);
/*      */       case '\f':
/*  175 */         return jjStartNfaWithStates_0(0, 4, 0);
/*      */       case '\r':
/*  177 */         return jjStartNfaWithStates_0(0, 3, 0);
/*      */       case ' ':
/*  179 */         return jjStartNfaWithStates_0(0, 1, 0);
/*      */       case '!':
/*  181 */         this.jjmatchedKind = 86;
/*  182 */         return jjMoveStringLiteralDfa1_0(0L, 2147483648L, 0L);
/*      */       case '%':
/*  184 */         this.jjmatchedKind = 111;
/*  185 */         return jjMoveStringLiteralDfa1_0(0L, Long.MIN_VALUE, 0L);
/*      */       case '&':
/*  187 */         this.jjmatchedKind = 106;
/*  188 */         return jjMoveStringLiteralDfa1_0(0L, 288230393331580928L, 0L);
/*      */       case '(':
/*  190 */         return jjStopAtPos(0, 72);
/*      */       case ')':
/*  192 */         return jjStopAtPos(0, 73);
/*      */       case '*':
/*  194 */         this.jjmatchedKind = 104;
/*  195 */         return jjMoveStringLiteralDfa1_0(0L, 72057594037927936L, 0L);
/*      */       case '+':
/*  197 */         this.jjmatchedKind = 102;
/*  198 */         return jjMoveStringLiteralDfa1_0(0L, 18014467228958720L, 0L);
/*      */       case ',':
/*  200 */         return jjStopAtPos(0, 79);
/*      */       case '-':
/*  202 */         this.jjmatchedKind = 103;
/*  203 */         return jjMoveStringLiteralDfa1_0(0L, 36028934457917440L, 0L);
/*      */       case '.':
/*  205 */         return jjStartNfaWithStates_0(0, 80, 11);
/*      */       case '/':
/*  207 */         this.jjmatchedKind = 105;
/*  208 */         return jjMoveStringLiteralDfa1_0(0L, 144115188075855872L, 0L);
/*      */       case ':':
/*  210 */         return jjStopAtPos(0, 89);
/*      */       case ';':
/*  212 */         return jjStopAtPos(0, 78);
/*      */       case '<':
/*  214 */         this.jjmatchedKind = 84;
/*  215 */         return jjMoveStringLiteralDfa1_0(0L, 281475110928384L, 1L);
/*      */       case '=':
/*  217 */         this.jjmatchedKind = 81;
/*  218 */         return jjMoveStringLiteralDfa1_0(0L, 67108864L, 0L);
/*      */       case '>':
/*  220 */         this.jjmatchedKind = 82;
/*  221 */         return jjMoveStringLiteralDfa1_0(0L, 5629500071084032L, 20L);
/*      */       case '?':
/*  223 */         return jjStopAtPos(0, 88);
/*      */       case '@':
/*  225 */         return jjMoveStringLiteralDfa1_0(0L, 2894169735298547712L, 42L);
/*      */       case '[':
/*  227 */         return jjStopAtPos(0, 76);
/*      */       case ']':
/*  229 */         return jjStopAtPos(0, 77);
/*      */       case '^':
/*  231 */         this.jjmatchedKind = 110;
/*  232 */         return jjMoveStringLiteralDfa1_0(0L, 4611686018427387904L, 0L);
/*      */       case 'a':
/*  234 */         return jjMoveStringLiteralDfa1_0(1024L, 0L, 0L);
/*      */       case 'b':
/*  236 */         return jjMoveStringLiteralDfa1_0(22528L, 0L, 0L);
/*      */       case 'c':
/*  238 */         return jjMoveStringLiteralDfa1_0(1024000L, 0L, 0L);
/*      */       case 'd':
/*  240 */         return jjMoveStringLiteralDfa1_0(7340032L, 0L, 0L);
/*      */       case 'e':
/*  242 */         return jjMoveStringLiteralDfa1_0(58720256L, 0L, 0L);
/*      */       case 'f':
/*  244 */         return jjMoveStringLiteralDfa1_0(2080374784L, 0L, 0L);
/*      */       case 'g':
/*  246 */         return jjMoveStringLiteralDfa1_0(2147483648L, 0L, 0L);
/*      */       case 'i':
/*  248 */         return jjMoveStringLiteralDfa1_0(270582939648L, 0L, 0L);
/*      */       case 'l':
/*  250 */         return jjMoveStringLiteralDfa1_0(274877906944L, 0L, 0L);
/*      */       case 'n':
/*  252 */         return jjMoveStringLiteralDfa1_0(3848290697216L, 0L, 0L);
/*      */       case 'p':
/*  254 */         return jjMoveStringLiteralDfa1_0(65970697666560L, 0L, 0L);
/*      */       case 'r':
/*  256 */         return jjMoveStringLiteralDfa1_0(70368744177664L, 0L, 0L);
/*      */       case 's':
/*  258 */         return jjMoveStringLiteralDfa1_0(4362862139015168L, 0L, 0L);
/*      */       case 't':
/*  260 */         return jjMoveStringLiteralDfa1_0(139611588448485376L, 0L, 0L);
/*      */       case 'v':
/*  262 */         return jjMoveStringLiteralDfa1_0(432345564227567616L, 0L, 0L);
/*      */       case 'w':
/*  264 */         return jjMoveStringLiteralDfa1_0(576460752303423488L, 0L, 0L);
/*      */       case '{':
/*  266 */         return jjStopAtPos(0, 74);
/*      */       case '|':
/*  268 */         this.jjmatchedKind = 108;
/*  269 */         return jjMoveStringLiteralDfa1_0(0L, 1152921508901814272L, 0L);
/*      */       case '}':
/*  271 */         return jjStopAtPos(0, 75);
/*      */       case '~':
/*  273 */         return jjStopAtPos(0, 87);
/*      */     } 
/*  275 */     return jjMoveNfa_0(6, 0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa1_0(long active0, long active1, long active2) {
/*      */     try {
/*  280 */       this.curChar = this.input_stream.readChar();
/*  281 */     } catch (IOException e) {
/*  282 */       jjStopStringLiteralDfa_0(0, active0, active1, active2);
/*  283 */       return 1;
/*      */     } 
/*  285 */     switch (this.curChar) {
/*      */       
/*      */       case '&':
/*  288 */         if ((active1 & 0x400000000L) != 0L)
/*  289 */           return jjStopAtPos(1, 98); 
/*      */         break;
/*      */       case '+':
/*  292 */         if ((active1 & 0x1000000000L) != 0L)
/*  293 */           return jjStopAtPos(1, 100); 
/*      */         break;
/*      */       case '-':
/*  296 */         if ((active1 & 0x2000000000L) != 0L)
/*  297 */           return jjStopAtPos(1, 101); 
/*      */         break;
/*      */       case '<':
/*  300 */         if ((active1 & 0x1000000000000L) != 0L) {
/*      */           
/*  302 */           this.jjmatchedKind = 112;
/*  303 */           this.jjmatchedPos = 1;
/*      */         } 
/*  305 */         return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 0L, active2, 1L);
/*      */       case '=':
/*  307 */         if ((active1 & 0x4000000L) != 0L)
/*  308 */           return jjStopAtPos(1, 90); 
/*  309 */         if ((active1 & 0x8000000L) != 0L)
/*  310 */           return jjStopAtPos(1, 91); 
/*  311 */         if ((active1 & 0x20000000L) != 0L)
/*  312 */           return jjStopAtPos(1, 93); 
/*  313 */         if ((active1 & 0x80000000L) != 0L)
/*  314 */           return jjStopAtPos(1, 95); 
/*  315 */         if ((active1 & 0x40000000000000L) != 0L)
/*  316 */           return jjStopAtPos(1, 118); 
/*  317 */         if ((active1 & 0x80000000000000L) != 0L)
/*  318 */           return jjStopAtPos(1, 119); 
/*  319 */         if ((active1 & 0x100000000000000L) != 0L)
/*  320 */           return jjStopAtPos(1, 120); 
/*  321 */         if ((active1 & 0x200000000000000L) != 0L)
/*  322 */           return jjStopAtPos(1, 121); 
/*  323 */         if ((active1 & 0x400000000000000L) != 0L)
/*  324 */           return jjStopAtPos(1, 122); 
/*  325 */         if ((active1 & 0x1000000000000000L) != 0L)
/*  326 */           return jjStopAtPos(1, 124); 
/*  327 */         if ((active1 & 0x4000000000000000L) != 0L)
/*  328 */           return jjStopAtPos(1, 126); 
/*  329 */         if ((active1 & Long.MIN_VALUE) != 0L)
/*  330 */           return jjStopAtPos(1, 127); 
/*      */         break;
/*      */       case '>':
/*  333 */         if ((active1 & 0x4000000000000L) != 0L) {
/*      */           
/*  335 */           this.jjmatchedKind = 114;
/*  336 */           this.jjmatchedPos = 1;
/*      */         } 
/*  338 */         return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 4503599627370496L, active2, 20L);
/*      */       case 'a':
/*  340 */         return jjMoveStringLiteralDfa2_0(active0, 4947869532160L, active1, 576460786663161856L, active2, 0L);
/*      */       case 'b':
/*  342 */         return jjMoveStringLiteralDfa2_0(active0, 1024L, active1, 43980465111040L, active2, 0L);
/*      */       case 'e':
/*  344 */         return jjMoveStringLiteralDfa2_0(active0, 71468256854016L, active1, 0L, active2, 0L);
/*      */       case 'f':
/*  346 */         if ((active0 & 0x100000000L) != 0L)
/*  347 */           return jjStartNfaWithStates_0(1, 32, 35); 
/*      */         break;
/*      */       case 'g':
/*  350 */         return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 1074266112L, active2, 0L);
/*      */       case 'h':
/*  352 */         return jjMoveStringLiteralDfa2_0(active0, 603623087556132864L, active1, 0L, active2, 0L);
/*      */       case 'i':
/*  354 */         return jjMoveStringLiteralDfa2_0(active0, 402653184L, active1, 0L, active2, 0L);
/*      */       case 'l':
/*  356 */         return jjMoveStringLiteralDfa2_0(active0, 545267712L, active1, 562950223953920L, active2, 2L);
/*      */       case 'm':
/*  358 */         return jjMoveStringLiteralDfa2_0(active0, 25769803776L, active1, 0L, active2, 0L);
/*      */       case 'n':
/*  360 */         return jjMoveStringLiteralDfa2_0(active0, 240534945792L, active1, 0L, active2, 0L);
/*      */       case 'o':
/*  362 */         if ((active0 & 0x200000L) != 0L) {
/*      */           
/*  364 */           this.jjmatchedKind = 21;
/*  365 */           this.jjmatchedPos = 1;
/*      */         } 
/*  367 */         return jjMoveStringLiteralDfa2_0(active0, 432345842331682816L, active1, 2305843017803628544L, active2, 0L);
/*      */       case 'r':
/*  369 */         return jjMoveStringLiteralDfa2_0(active0, 112616378963333120L, active1, 11258999068426240L, active2, 40L);
/*      */       case 't':
/*  371 */         return jjMoveStringLiteralDfa2_0(active0, 844424930131968L, active1, 0L, active2, 0L);
/*      */       case 'u':
/*  373 */         return jjMoveStringLiteralDfa2_0(active0, 37383395344384L, active1, 0L, active2, 0L);
/*      */       case 'w':
/*  375 */         return jjMoveStringLiteralDfa2_0(active0, 1125899906842624L, active1, 0L, active2, 0L);
/*      */       case 'x':
/*  377 */         return jjMoveStringLiteralDfa2_0(active0, 33554432L, active1, 0L, active2, 0L);
/*      */       case 'y':
/*  379 */         return jjMoveStringLiteralDfa2_0(active0, 2251799813701632L, active1, 0L, active2, 0L);
/*      */       case '|':
/*  381 */         if ((active1 & 0x100000000L) != 0L) {
/*  382 */           return jjStopAtPos(1, 96);
/*      */         }
/*      */         break;
/*      */     } 
/*      */     
/*  387 */     return jjStartNfa_0(0, active0, active1, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa2_0(long old0, long active0, long old1, long active1, long old2, long active2) {
/*  391 */     if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
/*  392 */       return jjStartNfa_0(0, old0, old1, old2);  try {
/*  393 */       this.curChar = this.input_stream.readChar();
/*  394 */     } catch (IOException e) {
/*  395 */       jjStopStringLiteralDfa_0(1, active0, active1, active2);
/*  396 */       return 2;
/*      */     } 
/*  398 */     switch (this.curChar) {
/*      */       
/*      */       case '=':
/*  401 */         if ((active2 & 0x1L) != 0L)
/*  402 */           return jjStopAtPos(2, 128); 
/*  403 */         if ((active2 & 0x4L) != 0L)
/*  404 */           return jjStopAtPos(2, 130); 
/*      */         break;
/*      */       case '>':
/*  407 */         if ((active1 & 0x10000000000000L) != 0L) {
/*      */           
/*  409 */           this.jjmatchedKind = 116;
/*  410 */           this.jjmatchedPos = 2;
/*      */         } 
/*  412 */         return jjMoveStringLiteralDfa3_0(active0, 0L, active1, 0L, active2, 16L);
/*      */       case 'a':
/*  414 */         return jjMoveStringLiteralDfa3_0(active0, 4785074604220416L, active1, 0L, active2, 0L);
/*      */       case 'b':
/*  416 */         return jjMoveStringLiteralDfa3_0(active0, 35184372088832L, active1, 0L, active2, 0L);
/*      */       case 'c':
/*  418 */         return jjMoveStringLiteralDfa3_0(active0, 4398046511104L, active1, 0L, active2, 0L);
/*      */       case 'e':
/*  420 */         return jjMoveStringLiteralDfa3_0(active0, 4096L, active1, 562949953421312L, active2, 2L);
/*      */       case 'f':
/*  422 */         return jjMoveStringLiteralDfa3_0(active0, 1048576L, active1, 0L, active2, 0L);
/*      */       case 'i':
/*  424 */         return jjMoveStringLiteralDfa3_0(active0, 721710636379144192L, active1, 11302979533537280L, active2, 40L);
/*      */       case 'l':
/*  426 */         return jjMoveStringLiteralDfa3_0(active0, 288232575242076160L, active1, 0L, active2, 0L);
/*      */       case 'n':
/*  428 */         return jjMoveStringLiteralDfa3_0(active0, 2252075095031808L, active1, 576460786663161856L, active2, 0L);
/*      */       case 'o':
/*  430 */         return jjMoveStringLiteralDfa3_0(active0, 158330211272704L, active1, 0L, active2, 0L);
/*      */       case 'p':
/*  432 */         return jjMoveStringLiteralDfa3_0(active0, 25769803776L, active1, 0L, active2, 0L);
/*      */       case 'r':
/*  434 */         if ((active0 & 0x40000000L) != 0L)
/*  435 */           return jjStartNfaWithStates_0(2, 30, 35); 
/*  436 */         if ((active1 & 0x200000000L) != 0L) {
/*      */           
/*  438 */           this.jjmatchedKind = 97;
/*  439 */           this.jjmatchedPos = 2;
/*      */         } 
/*  441 */         return jjMoveStringLiteralDfa3_0(active0, 27584547717644288L, active1, 2305843009213693952L, active2, 0L);
/*      */       case 's':
/*  443 */         return jjMoveStringLiteralDfa3_0(active0, 34368160768L, active1, 0L, active2, 0L);
/*      */       case 't':
/*  445 */         if ((active0 & 0x1000000000L) != 0L) {
/*      */           
/*  447 */           this.jjmatchedKind = 36;
/*  448 */           this.jjmatchedPos = 2;
/*      */         }
/*  450 */         else if ((active1 & 0x80000L) != 0L) {
/*      */           
/*  452 */           this.jjmatchedKind = 83;
/*  453 */           this.jjmatchedPos = 2;
/*      */         }
/*  455 */         else if ((active1 & 0x200000L) != 0L) {
/*      */           
/*  457 */           this.jjmatchedKind = 85;
/*  458 */           this.jjmatchedPos = 2;
/*      */         } 
/*  460 */         return jjMoveStringLiteralDfa3_0(active0, 71058120065024L, active1, 1342177280L, active2, 0L);
/*      */       case 'u':
/*  462 */         return jjMoveStringLiteralDfa3_0(active0, 36028797039935488L, active1, 0L, active2, 0L);
/*      */       case 'w':
/*  464 */         if ((active0 & 0x10000000000L) != 0L)
/*  465 */           return jjStartNfaWithStates_0(2, 40, 35); 
/*      */         break;
/*      */       case 'y':
/*  468 */         if ((active0 & 0x100000000000000L) != 0L) {
/*  469 */           return jjStartNfaWithStates_0(2, 56, 35);
/*      */         }
/*      */         break;
/*      */     } 
/*      */     
/*  474 */     return jjStartNfa_0(1, active0, active1, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa3_0(long old0, long active0, long old1, long active1, long old2, long active2) {
/*  478 */     if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
/*  479 */       return jjStartNfa_0(1, old0, old1, old2);  try {
/*  480 */       this.curChar = this.input_stream.readChar();
/*  481 */     } catch (IOException e) {
/*  482 */       jjStopStringLiteralDfa_0(2, active0, active1, active2);
/*  483 */       return 3;
/*      */     } 
/*  485 */     switch (this.curChar) {
/*      */       
/*      */       case '=':
/*  488 */         if ((active2 & 0x10L) != 0L)
/*  489 */           return jjStopAtPos(3, 132); 
/*      */         break;
/*      */       case '_':
/*  492 */         return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 2305843009213693952L, active2, 0L);
/*      */       case 'a':
/*  494 */         return jjMoveStringLiteralDfa4_0(active0, 288230377092288512L, active1, 0L, active2, 0L);
/*      */       case 'b':
/*  496 */         return jjMoveStringLiteralDfa4_0(active0, 4194304L, active1, 0L, active2, 0L);
/*      */       case 'c':
/*  498 */         return jjMoveStringLiteralDfa4_0(active0, 2251799813750784L, active1, 0L, active2, 0L);
/*      */       case 'd':
/*  500 */         if ((active0 & 0x200000000000000L) != 0L)
/*  501 */           return jjStartNfaWithStates_0(3, 57, 35); 
/*  502 */         if ((active1 & 0x800000000L) != 0L) {
/*      */           
/*  504 */           this.jjmatchedKind = 99;
/*  505 */           this.jjmatchedPos = 3;
/*      */         } 
/*  507 */         return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 576460752303423488L, active2, 0L);
/*      */       case 'e':
/*  509 */         if ((active0 & 0x4000L) != 0L)
/*  510 */           return jjStartNfaWithStates_0(3, 14, 35); 
/*  511 */         if ((active0 & 0x8000L) != 0L)
/*  512 */           return jjStartNfaWithStates_0(3, 15, 35); 
/*  513 */         if ((active0 & 0x800000L) != 0L)
/*  514 */           return jjStartNfaWithStates_0(3, 23, 35); 
/*  515 */         if ((active0 & 0x80000000000000L) != 0L)
/*  516 */           return jjStartNfaWithStates_0(3, 55, 35); 
/*  517 */         return jjMoveStringLiteralDfa4_0(active0, 137472507904L, active1, 1342177280L, active2, 0L);
/*      */       case 'f':
/*  519 */         return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 562949953421312L, active2, 2L);
/*      */       case 'g':
/*  521 */         if ((active0 & 0x4000000000L) != 0L)
/*  522 */           return jjStartNfaWithStates_0(3, 38, 35); 
/*  523 */         return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 11258999068426240L, active2, 40L);
/*      */       case 'i':
/*  525 */         return jjMoveStringLiteralDfa4_0(active0, 563499709235200L, active1, 0L, active2, 0L);
/*      */       case 'k':
/*  527 */         return jjMoveStringLiteralDfa4_0(active0, 4398046511104L, active1, 0L, active2, 0L);
/*      */       case 'l':
/*  529 */         if ((active0 & 0x20000000000L) != 0L)
/*  530 */           return jjStartNfaWithStates_0(3, 41, 35); 
/*  531 */         return jjMoveStringLiteralDfa4_0(active0, 576495945265448960L, active1, 0L, active2, 0L);
/*      */       case 'm':
/*  533 */         if ((active0 & 0x1000000L) != 0L)
/*  534 */           return jjStartNfaWithStates_0(3, 24, 35); 
/*      */         break;
/*      */       case 'n':
/*  537 */         return jjMoveStringLiteralDfa4_0(active0, 4503599627370496L, active1, 0L, active2, 0L);
/*      */       case 'o':
/*  539 */         if ((active0 & 0x80000000L) != 0L)
/*  540 */           return jjStartNfaWithStates_0(3, 31, 35); 
/*  541 */         return jjMoveStringLiteralDfa4_0(active0, 27021614944092160L, active1, 0L, active2, 0L);
/*      */       case 'r':
/*  543 */         if ((active0 & 0x20000L) != 0L)
/*  544 */           return jjStartNfaWithStates_0(3, 17, 35); 
/*  545 */         return jjMoveStringLiteralDfa4_0(active0, 140737488355328L, active1, 0L, active2, 0L);
/*      */       case 's':
/*  547 */         return jjMoveStringLiteralDfa4_0(active0, 67379200L, active1, 0L, active2, 0L);
/*      */       case 't':
/*  549 */         return jjMoveStringLiteralDfa4_0(active0, 1425001429861376L, active1, 43980465111040L, active2, 0L);
/*      */       case 'u':
/*  551 */         return jjMoveStringLiteralDfa4_0(active0, 70368744177664L, active1, 0L, active2, 0L);
/*      */       case 'v':
/*  553 */         return jjMoveStringLiteralDfa4_0(active0, 8796093022208L, active1, 0L, active2, 0L);
/*      */     } 
/*      */ 
/*      */     
/*  557 */     return jjStartNfa_0(2, active0, active1, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa4_0(long old0, long active0, long old1, long active1, long old2, long active2) {
/*  561 */     if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
/*  562 */       return jjStartNfa_0(2, old0, old1, old2);  try {
/*  563 */       this.curChar = this.input_stream.readChar();
/*  564 */     } catch (IOException e) {
/*  565 */       jjStopStringLiteralDfa_0(3, active0, active1, active2);
/*  566 */       return 4;
/*      */     } 
/*  568 */     switch (this.curChar) {
/*      */       
/*      */       case '_':
/*  571 */         return jjMoveStringLiteralDfa5_0(active0, 0L, active1, 576460752303423488L, active2, 0L);
/*      */       case 'a':
/*  573 */         return jjMoveStringLiteralDfa5_0(active0, 13228499271680L, active1, 2305843009213693952L, active2, 0L);
/*      */       case 'c':
/*  575 */         return jjMoveStringLiteralDfa5_0(active0, 1688849860263936L, active1, 0L, active2, 0L);
/*      */       case 'e':
/*  577 */         if ((active0 & 0x4000000L) != 0L)
/*  578 */           return jjStartNfaWithStates_0(4, 26, 35); 
/*  579 */         if ((active0 & 0x800000000000000L) != 0L)
/*  580 */           return jjStartNfaWithStates_0(4, 59, 35); 
/*  581 */         return jjMoveStringLiteralDfa5_0(active0, 17600775981056L, active1, 0L, active2, 0L);
/*      */       case 'h':
/*  583 */         if ((active0 & 0x10000L) != 0L)
/*  584 */           return jjStartNfaWithStates_0(4, 16, 35); 
/*  585 */         return jjMoveStringLiteralDfa5_0(active0, 2251799813685248L, active1, 11258999068426240L, active2, 40L);
/*      */       case 'i':
/*  587 */         return jjMoveStringLiteralDfa5_0(active0, 316659349323776L, active1, 0L, active2, 0L);
/*      */       case 'k':
/*  589 */         if ((active0 & 0x1000L) != 0L)
/*  590 */           return jjStartNfaWithStates_0(4, 12, 35); 
/*      */         break;
/*      */       case 'l':
/*  593 */         if ((active0 & 0x8000000L) != 0L) {
/*      */           
/*  595 */           this.jjmatchedKind = 27;
/*  596 */           this.jjmatchedPos = 4;
/*      */         } 
/*  598 */         return jjMoveStringLiteralDfa5_0(active0, 272629760L, active1, 0L, active2, 0L);
/*      */       case 'n':
/*  600 */         return jjMoveStringLiteralDfa5_0(active0, 33554432L, active1, 0L, active2, 0L);
/*      */       case 'q':
/*  602 */         if ((active1 & 0x10000000L) != 0L)
/*  603 */           return jjStopAtPos(4, 92); 
/*  604 */         if ((active1 & 0x40000000L) != 0L)
/*  605 */           return jjStopAtPos(4, 94); 
/*      */         break;
/*      */       case 'r':
/*  608 */         return jjMoveStringLiteralDfa5_0(active0, 70523363001344L, active1, 0L, active2, 0L);
/*      */       case 's':
/*  610 */         if ((active0 & 0x2000L) != 0L)
/*  611 */           return jjStartNfaWithStates_0(4, 13, 35); 
/*  612 */         return jjMoveStringLiteralDfa5_0(active0, 4503599627370496L, active1, 0L, active2, 0L);
/*      */       case 't':
/*  614 */         if ((active0 & 0x40000L) != 0L)
/*  615 */           return jjStartNfaWithStates_0(4, 18, 35); 
/*  616 */         if ((active0 & 0x20000000L) != 0L)
/*  617 */           return jjStartNfaWithStates_0(4, 29, 35); 
/*  618 */         if ((active0 & 0x800000000000L) != 0L)
/*  619 */           return jjStartNfaWithStates_0(4, 47, 35); 
/*  620 */         return jjMoveStringLiteralDfa5_0(active0, 288230376151711744L, active1, 562949953421312L, active2, 2L);
/*      */       case 'u':
/*  622 */         return jjMoveStringLiteralDfa5_0(active0, 1048576L, active1, 0L, active2, 0L);
/*      */       case 'v':
/*  624 */         return jjMoveStringLiteralDfa5_0(active0, 549755813888L, active1, 0L, active2, 0L);
/*      */       case 'w':
/*  626 */         if ((active0 & 0x20000000000000L) != 0L) {
/*      */           
/*  628 */           this.jjmatchedKind = 53;
/*  629 */           this.jjmatchedPos = 4;
/*      */         } 
/*  631 */         return jjMoveStringLiteralDfa5_0(active0, 18014398509481984L, active1, 43980465111040L, active2, 0L);
/*      */     } 
/*      */ 
/*      */     
/*  635 */     return jjStartNfa_0(3, active0, active1, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa5_0(long old0, long active0, long old1, long active1, long old2, long active2) {
/*  639 */     if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
/*  640 */       return jjStartNfa_0(3, old0, old1, old2);  try {
/*  641 */       this.curChar = this.input_stream.readChar();
/*  642 */     } catch (IOException e) {
/*  643 */       jjStopStringLiteralDfa_0(4, active0, active1, active2);
/*  644 */       return 5;
/*      */     } 
/*  646 */     switch (this.curChar) {
/*      */       
/*      */       case '_':
/*  649 */         return jjMoveStringLiteralDfa6_0(active0, 0L, active1, 562949953421312L, active2, 2L);
/*      */       case 'a':
/*  651 */         return jjMoveStringLiteralDfa6_0(active0, 3072L, active1, 576460752303423488L, active2, 0L);
/*      */       case 'c':
/*  653 */         if ((active0 & 0x200000000000L) != 0L)
/*  654 */           return jjStartNfaWithStates_0(5, 45, 35); 
/*  655 */         if ((active0 & 0x1000000000000L) != 0L)
/*  656 */           return jjStartNfaWithStates_0(5, 48, 35); 
/*  657 */         return jjMoveStringLiteralDfa6_0(active0, 17592186044416L, active1, 0L, active2, 0L);
/*      */       case 'd':
/*  659 */         return jjMoveStringLiteralDfa6_0(active0, 33554432L, active1, 0L, active2, 0L);
/*      */       case 'e':
/*  661 */         if ((active0 & 0x400000L) != 0L)
/*  662 */           return jjStartNfaWithStates_0(5, 22, 35); 
/*  663 */         if ((active0 & 0x8000000000L) != 0L)
/*  664 */           return jjStartNfaWithStates_0(5, 39, 35); 
/*      */         break;
/*      */       case 'f':
/*  667 */         return jjMoveStringLiteralDfa6_0(active0, 137438953472L, active1, 0L, active2, 0L);
/*      */       case 'g':
/*  669 */         return jjMoveStringLiteralDfa6_0(active0, 4398046511104L, active1, 0L, active2, 0L);
/*      */       case 'h':
/*  671 */         if ((active0 & 0x4000000000000L) != 0L)
/*  672 */           return jjStartNfaWithStates_0(5, 50, 35); 
/*      */         break;
/*      */       case 'i':
/*  675 */         return jjMoveStringLiteralDfa6_0(active0, 292733975779082240L, active1, 43980465111040L, active2, 0L);
/*      */       case 'l':
/*  677 */         return jjMoveStringLiteralDfa6_0(active0, 269484032L, active1, 0L, active2, 0L);
/*      */       case 'm':
/*  679 */         return jjMoveStringLiteralDfa6_0(active0, 8589934592L, active1, 0L, active2, 0L);
/*      */       case 'n':
/*  681 */         if ((active0 & 0x400000000000L) != 0L)
/*  682 */           return jjStartNfaWithStates_0(5, 46, 35); 
/*  683 */         return jjMoveStringLiteralDfa6_0(active0, 34360262656L, active1, 0L, active2, 0L);
/*      */       case 'r':
/*  685 */         return jjMoveStringLiteralDfa6_0(active0, 2251799813685248L, active1, 0L, active2, 0L);
/*      */       case 's':
/*  687 */         if ((active0 & 0x40000000000000L) != 0L)
/*  688 */           return jjStartNfaWithStates_0(5, 54, 35); 
/*  689 */         return jjMoveStringLiteralDfa6_0(active0, 0L, active1, 2305843009213693952L, active2, 0L);
/*      */       case 't':
/*  691 */         if ((active0 & 0x400000000L) != 0L)
/*  692 */           return jjStartNfaWithStates_0(5, 34, 35); 
/*  693 */         return jjMoveStringLiteralDfa6_0(active0, 571746046443520L, active1, 11258999068426240L, active2, 40L);
/*      */     } 
/*      */ 
/*      */     
/*  697 */     return jjStartNfa_0(4, active0, active1, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa6_0(long old0, long active0, long old1, long active1, long old2, long active2) {
/*  701 */     if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
/*  702 */       return jjStartNfa_0(4, old0, old1, old2);  try {
/*  703 */       this.curChar = this.input_stream.readChar();
/*  704 */     } catch (IOException e) {
/*  705 */       jjStopStringLiteralDfa_0(5, active0, active1, active2);
/*  706 */       return 6;
/*      */     } 
/*  708 */     switch (this.curChar) {
/*      */       
/*      */       case '_':
/*  711 */         return jjMoveStringLiteralDfa7_0(active0, 0L, active1, 11258999068426240L, active2, 40L);
/*      */       case 'a':
/*  713 */         return jjMoveStringLiteralDfa7_0(active0, 137438953472L, active1, 0L, active2, 0L);
/*      */       case 'c':
/*  715 */         return jjMoveStringLiteralDfa7_0(active0, 34359739392L, active1, 0L, active2, 0L);
/*      */       case 'e':
/*  717 */         if ((active0 & 0x40000000000L) != 0L)
/*  718 */           return jjStartNfaWithStates_0(6, 42, 35); 
/*  719 */         if ((active0 & 0x80000000000L) != 0L)
/*  720 */           return jjStartNfaWithStates_0(6, 43, 35); 
/*  721 */         return jjMoveStringLiteralDfa7_0(active0, 4503608217305088L, active1, 0L, active2, 0L);
/*      */       case 'f':
/*  723 */         return jjMoveStringLiteralDfa7_0(active0, 562949953421312L, active1, 0L, active2, 0L);
/*      */       case 'l':
/*  725 */         return jjMoveStringLiteralDfa7_0(active0, 288230376151711744L, active1, 0L, active2, 0L);
/*      */       case 'n':
/*  727 */         if ((active0 & 0x800L) != 0L)
/*  728 */           return jjStartNfaWithStates_0(6, 11, 35); 
/*      */         break;
/*      */       case 'o':
/*  731 */         return jjMoveStringLiteralDfa7_0(active0, 2251799813685248L, active1, 0L, active2, 0L);
/*      */       case 's':
/*  733 */         if ((active0 & 0x2000000L) != 0L)
/*  734 */           return jjStartNfaWithStates_0(6, 25, 35); 
/*  735 */         return jjMoveStringLiteralDfa7_0(active0, 0L, active1, 2882910691935649792L, active2, 2L);
/*      */       case 't':
/*  737 */         if ((active0 & 0x100000L) != 0L)
/*  738 */           return jjStartNfaWithStates_0(6, 20, 35); 
/*  739 */         return jjMoveStringLiteralDfa7_0(active0, 17592186044416L, active1, 0L, active2, 0L);
/*      */       case 'u':
/*  741 */         return jjMoveStringLiteralDfa7_0(active0, 524288L, active1, 0L, active2, 0L);
/*      */       case 'y':
/*  743 */         if ((active0 & 0x10000000L) != 0L) {
/*  744 */           return jjStartNfaWithStates_0(6, 28, 35);
/*      */         }
/*      */         break;
/*      */     } 
/*      */     
/*  749 */     return jjStartNfa_0(5, active0, active1, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa7_0(long old0, long active0, long old1, long active1, long old2, long active2) {
/*  753 */     if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
/*  754 */       return jjStartNfa_0(5, old0, old1, old2);  try {
/*  755 */       this.curChar = this.input_stream.readChar();
/*  756 */     } catch (IOException e) {
/*  757 */       jjStopStringLiteralDfa_0(6, active0, active1, active2);
/*  758 */       return 7;
/*      */     } 
/*  760 */     switch (this.curChar) {
/*      */       
/*      */       case 'c':
/*  763 */         return jjMoveStringLiteralDfa8_0(active0, 137438953472L, active1, 0L, active2, 0L);
/*      */       case 'e':
/*  765 */         if ((active0 & 0x80000L) != 0L)
/*  766 */           return jjStartNfaWithStates_0(7, 19, 35); 
/*  767 */         if ((active0 & 0x400000000000000L) != 0L)
/*  768 */           return jjStartNfaWithStates_0(7, 58, 35); 
/*  769 */         return jjMoveStringLiteralDfa8_0(active0, 17626545782784L, active1, 43980465111040L, active2, 0L);
/*      */       case 'h':
/*  771 */         return jjMoveStringLiteralDfa8_0(active0, 0L, active1, 562949953421312L, active2, 2L);
/*      */       case 'i':
/*  773 */         return jjMoveStringLiteralDfa8_0(active0, 0L, active1, 2305843009213693952L, active2, 0L);
/*      */       case 'n':
/*  775 */         return jjMoveStringLiteralDfa8_0(active0, 6755408030990336L, active1, 0L, active2, 0L);
/*      */       case 'p':
/*  777 */         if ((active0 & 0x2000000000000L) != 0L)
/*  778 */           return jjStartNfaWithStates_0(7, 49, 35); 
/*      */         break;
/*      */       case 's':
/*  781 */         return jjMoveStringLiteralDfa8_0(active0, 0L, active1, 578712552117108736L, active2, 8L);
/*      */       case 't':
/*  783 */         if ((active0 & 0x400L) != 0L)
/*  784 */           return jjStartNfaWithStates_0(7, 10, 35); 
/*      */         break;
/*      */       case 'u':
/*  787 */         return jjMoveStringLiteralDfa8_0(active0, 0L, active1, 9007199254740992L, active2, 32L);
/*      */     } 
/*      */ 
/*      */     
/*  791 */     return jjStartNfa_0(6, active0, active1, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa8_0(long old0, long active0, long old1, long active1, long old2, long active2) {
/*  795 */     if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
/*  796 */       return jjStartNfa_0(6, old0, old1, old2);  try {
/*  797 */       this.curChar = this.input_stream.readChar();
/*  798 */     } catch (IOException e) {
/*  799 */       jjStopStringLiteralDfa_0(7, active0, active1, active2);
/*  800 */       return 8;
/*      */     } 
/*  802 */     switch (this.curChar) {
/*      */       
/*      */       case '_':
/*  805 */         return jjMoveStringLiteralDfa9_0(active0, 0L, active1, 43980465111040L, active2, 0L);
/*      */       case 'd':
/*  807 */         if ((active0 & 0x100000000000L) != 0L)
/*  808 */           return jjStartNfaWithStates_0(8, 44, 35); 
/*      */         break;
/*      */       case 'e':
/*  811 */         if ((active0 & 0x2000000000L) != 0L)
/*  812 */           return jjStartNfaWithStates_0(8, 37, 35); 
/*      */         break;
/*      */       case 'g':
/*  815 */         return jjMoveStringLiteralDfa9_0(active0, 0L, active1, 2305843009213693952L, active2, 0L);
/*      */       case 'h':
/*  817 */         return jjMoveStringLiteralDfa9_0(active0, 0L, active1, 2251799813685248L, active2, 8L);
/*      */       case 'i':
/*  819 */         return jjMoveStringLiteralDfa9_0(active0, 2251799813685248L, active1, 577023702256844800L, active2, 2L);
/*      */       case 'n':
/*  821 */         return jjMoveStringLiteralDfa9_0(active0, 0L, active1, 9007199254740992L, active2, 32L);
/*      */       case 'o':
/*  823 */         return jjMoveStringLiteralDfa9_0(active0, 34359738368L, active1, 0L, active2, 0L);
/*      */       case 't':
/*  825 */         if ((active0 & 0x10000000000000L) != 0L)
/*  826 */           return jjStartNfaWithStates_0(8, 52, 35); 
/*  827 */         return jjMoveStringLiteralDfa9_0(active0, 8589934592L, active1, 0L, active2, 0L);
/*      */     } 
/*      */ 
/*      */     
/*  831 */     return jjStartNfa_0(7, active0, active1, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa9_0(long old0, long active0, long old1, long active1, long old2, long active2) {
/*  835 */     if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
/*  836 */       return jjStartNfa_0(7, old0, old1, old2);  try {
/*  837 */       this.curChar = this.input_stream.readChar();
/*  838 */     } catch (IOException e) {
/*  839 */       jjStopStringLiteralDfa_0(8, active0, active1, active2);
/*  840 */       return 9;
/*      */     } 
/*  842 */     switch (this.curChar) {
/*      */       
/*      */       case 'a':
/*  845 */         return jjMoveStringLiteralDfa10_0(active0, 0L, active1, 8796093022208L, active2, 0L);
/*      */       case 'f':
/*  847 */         if ((active0 & 0x800000000L) != 0L)
/*  848 */           return jjStartNfaWithStates_0(9, 35, 35); 
/*  849 */         return jjMoveStringLiteralDfa10_0(active0, 0L, active1, 562949953421312L, active2, 2L);
/*      */       case 'g':
/*  851 */         return jjMoveStringLiteralDfa10_0(active0, 0L, active1, 576460752303423488L, active2, 0L);
/*      */       case 'i':
/*  853 */         return jjMoveStringLiteralDfa10_0(active0, 0L, active1, 2251799813685248L, active2, 8L);
/*      */       case 'n':
/*  855 */         if ((active1 & 0x2000000000000000L) != 0L)
/*  856 */           return jjStopAtPos(9, 125); 
/*      */         break;
/*      */       case 'o':
/*  859 */         return jjMoveStringLiteralDfa10_0(active0, 0L, active1, 35184372088832L, active2, 0L);
/*      */       case 's':
/*  861 */         if ((active0 & 0x200000000L) != 0L)
/*  862 */           return jjStartNfaWithStates_0(9, 33, 35); 
/*  863 */         return jjMoveStringLiteralDfa10_0(active0, 0L, active1, 9007199254740992L, active2, 32L);
/*      */       case 'z':
/*  865 */         return jjMoveStringLiteralDfa10_0(active0, 2251799813685248L, active1, 0L, active2, 0L);
/*      */     } 
/*      */ 
/*      */     
/*  869 */     return jjStartNfa_0(8, active0, active1, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa10_0(long old0, long active0, long old1, long active1, long old2, long active2) {
/*  873 */     if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
/*  874 */       return jjStartNfa_0(8, old0, old1, old2);  try {
/*  875 */       this.curChar = this.input_stream.readChar();
/*  876 */     } catch (IOException e) {
/*  877 */       jjStopStringLiteralDfa_0(9, active0, active1, active2);
/*  878 */       return 10;
/*      */     } 
/*  880 */     switch (this.curChar) {
/*      */       
/*      */       case 'e':
/*  883 */         return jjMoveStringLiteralDfa11_0(active0, 2251799813685248L, active1, 0L, active2, 0L);
/*      */       case 'f':
/*  885 */         return jjMoveStringLiteralDfa11_0(active0, 0L, active1, 2251799813685248L, active2, 8L);
/*      */       case 'i':
/*  887 */         return jjMoveStringLiteralDfa11_0(active0, 0L, active1, 9007199254740992L, active2, 32L);
/*      */       case 'n':
/*  889 */         if ((active1 & 0x800000000000000L) != 0L)
/*  890 */           return jjStopAtPos(10, 123); 
/*  891 */         return jjMoveStringLiteralDfa11_0(active0, 0L, active1, 8796093022208L, active2, 0L);
/*      */       case 'r':
/*  893 */         if ((active1 & 0x200000000000L) != 0L)
/*  894 */           return jjStopAtPos(10, 109); 
/*      */         break;
/*      */       case 't':
/*  897 */         if ((active1 & 0x2000000000000L) != 0L) {
/*      */           
/*  899 */           this.jjmatchedKind = 113;
/*  900 */           this.jjmatchedPos = 10;
/*      */         } 
/*  902 */         return jjMoveStringLiteralDfa11_0(active0, 0L, active1, 0L, active2, 2L);
/*      */     } 
/*      */ 
/*      */     
/*  906 */     return jjStartNfa_0(9, active0, active1, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa11_0(long old0, long active0, long old1, long active1, long old2, long active2) {
/*  910 */     if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
/*  911 */       return jjStartNfa_0(9, old0, old1, old2);  try {
/*  912 */       this.curChar = this.input_stream.readChar();
/*  913 */     } catch (IOException e) {
/*  914 */       jjStopStringLiteralDfa_0(10, active0, active1, active2);
/*  915 */       return 11;
/*      */     } 
/*  917 */     switch (this.curChar) {
/*      */       
/*      */       case '_':
/*  920 */         return jjMoveStringLiteralDfa12_0(active0, 0L, active1, 0L, active2, 2L);
/*      */       case 'd':
/*  922 */         if ((active0 & 0x8000000000000L) != 0L)
/*  923 */           return jjStartNfaWithStates_0(11, 51, 35); 
/*  924 */         if ((active1 & 0x80000000000L) != 0L)
/*  925 */           return jjStopAtPos(11, 107); 
/*      */         break;
/*      */       case 'g':
/*  928 */         return jjMoveStringLiteralDfa12_0(active0, 0L, active1, 9007199254740992L, active2, 32L);
/*      */       case 't':
/*  930 */         if ((active1 & 0x8000000000000L) != 0L) {
/*      */           
/*  932 */           this.jjmatchedKind = 115;
/*  933 */           this.jjmatchedPos = 11;
/*      */         } 
/*  935 */         return jjMoveStringLiteralDfa12_0(active0, 0L, active1, 0L, active2, 8L);
/*      */     } 
/*      */ 
/*      */     
/*  939 */     return jjStartNfa_0(10, active0, active1, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa12_0(long old0, long active0, long old1, long active1, long old2, long active2) {
/*  943 */     if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
/*  944 */       return jjStartNfa_0(10, old0, old1, old2);  try {
/*  945 */       this.curChar = this.input_stream.readChar();
/*  946 */     } catch (IOException e) {
/*  947 */       jjStopStringLiteralDfa_0(11, 0L, active1, active2);
/*  948 */       return 12;
/*      */     } 
/*  950 */     switch (this.curChar) {
/*      */       
/*      */       case '_':
/*  953 */         return jjMoveStringLiteralDfa13_0(active1, 0L, active2, 8L);
/*      */       case 'a':
/*  955 */         return jjMoveStringLiteralDfa13_0(active1, 0L, active2, 2L);
/*      */       case 'n':
/*  957 */         return jjMoveStringLiteralDfa13_0(active1, 9007199254740992L, active2, 32L);
/*      */     } 
/*      */ 
/*      */     
/*  961 */     return jjStartNfa_0(11, 0L, active1, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa13_0(long old1, long active1, long old2, long active2) {
/*  965 */     if (((active1 &= old1) | (active2 &= old2)) == 0L)
/*  966 */       return jjStartNfa_0(11, 0L, old1, old2);  try {
/*  967 */       this.curChar = this.input_stream.readChar();
/*  968 */     } catch (IOException e) {
/*  969 */       jjStopStringLiteralDfa_0(12, 0L, active1, active2);
/*  970 */       return 13;
/*      */     } 
/*  972 */     switch (this.curChar) {
/*      */       
/*      */       case 'a':
/*  975 */         return jjMoveStringLiteralDfa14_0(active1, 0L, active2, 8L);
/*      */       case 'e':
/*  977 */         return jjMoveStringLiteralDfa14_0(active1, 9007199254740992L, active2, 32L);
/*      */       case 's':
/*  979 */         return jjMoveStringLiteralDfa14_0(active1, 0L, active2, 2L);
/*      */     } 
/*      */ 
/*      */     
/*  983 */     return jjStartNfa_0(12, 0L, active1, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa14_0(long old1, long active1, long old2, long active2) {
/*  987 */     if (((active1 &= old1) | (active2 &= old2)) == 0L)
/*  988 */       return jjStartNfa_0(12, 0L, old1, old2);  try {
/*  989 */       this.curChar = this.input_stream.readChar();
/*  990 */     } catch (IOException e) {
/*  991 */       jjStopStringLiteralDfa_0(13, 0L, active1, active2);
/*  992 */       return 14;
/*      */     } 
/*  994 */     switch (this.curChar) {
/*      */       
/*      */       case 'd':
/*  997 */         return jjMoveStringLiteralDfa15_0(active1, 9007199254740992L, active2, 32L);
/*      */       case 's':
/*  999 */         return jjMoveStringLiteralDfa15_0(active1, 0L, active2, 10L);
/*      */     } 
/*      */ 
/*      */     
/* 1003 */     return jjStartNfa_0(13, 0L, active1, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa15_0(long old1, long active1, long old2, long active2) {
/* 1007 */     if (((active1 &= old1) | (active2 &= old2)) == 0L)
/* 1008 */       return jjStartNfa_0(13, 0L, old1, old2);  try {
/* 1009 */       this.curChar = this.input_stream.readChar();
/* 1010 */     } catch (IOException e) {
/* 1011 */       jjStopStringLiteralDfa_0(14, 0L, active1, active2);
/* 1012 */       return 15;
/*      */     } 
/* 1014 */     switch (this.curChar) {
/*      */       
/*      */       case '_':
/* 1017 */         return jjMoveStringLiteralDfa16_0(active1, 9007199254740992L, active2, 32L);
/*      */       case 'i':
/* 1019 */         return jjMoveStringLiteralDfa16_0(active1, 0L, active2, 2L);
/*      */       case 's':
/* 1021 */         return jjMoveStringLiteralDfa16_0(active1, 0L, active2, 8L);
/*      */     } 
/*      */ 
/*      */     
/* 1025 */     return jjStartNfa_0(14, 0L, active1, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa16_0(long old1, long active1, long old2, long active2) {
/* 1029 */     if (((active1 &= old1) | (active2 &= old2)) == 0L)
/* 1030 */       return jjStartNfa_0(14, 0L, old1, old2);  try {
/* 1031 */       this.curChar = this.input_stream.readChar();
/* 1032 */     } catch (IOException e) {
/* 1033 */       jjStopStringLiteralDfa_0(15, 0L, active1, active2);
/* 1034 */       return 16;
/*      */     } 
/* 1036 */     switch (this.curChar) {
/*      */       
/*      */       case 'g':
/* 1039 */         return jjMoveStringLiteralDfa17_0(active1, 0L, active2, 2L);
/*      */       case 'i':
/* 1041 */         return jjMoveStringLiteralDfa17_0(active1, 0L, active2, 8L);
/*      */       case 's':
/* 1043 */         return jjMoveStringLiteralDfa17_0(active1, 9007199254740992L, active2, 32L);
/*      */     } 
/*      */ 
/*      */     
/* 1047 */     return jjStartNfa_0(15, 0L, active1, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa17_0(long old1, long active1, long old2, long active2) {
/* 1051 */     if (((active1 &= old1) | (active2 &= old2)) == 0L)
/* 1052 */       return jjStartNfa_0(15, 0L, old1, old2);  try {
/* 1053 */       this.curChar = this.input_stream.readChar();
/* 1054 */     } catch (IOException e) {
/* 1055 */       jjStopStringLiteralDfa_0(16, 0L, active1, active2);
/* 1056 */       return 17;
/*      */     } 
/* 1058 */     switch (this.curChar) {
/*      */       
/*      */       case 'g':
/* 1061 */         return jjMoveStringLiteralDfa18_0(active1, 0L, active2, 8L);
/*      */       case 'h':
/* 1063 */         return jjMoveStringLiteralDfa18_0(active1, 9007199254740992L, active2, 32L);
/*      */       case 'n':
/* 1065 */         if ((active2 & 0x2L) != 0L) {
/* 1066 */           return jjStopAtPos(17, 129);
/*      */         }
/*      */         break;
/*      */     } 
/*      */     
/* 1071 */     return jjStartNfa_0(16, 0L, active1, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa18_0(long old1, long active1, long old2, long active2) {
/* 1075 */     if (((active1 &= old1) | (active2 &= old2)) == 0L)
/* 1076 */       return jjStartNfa_0(16, 0L, old1, old2);  try {
/* 1077 */       this.curChar = this.input_stream.readChar();
/* 1078 */     } catch (IOException e) {
/* 1079 */       jjStopStringLiteralDfa_0(17, 0L, active1, active2);
/* 1080 */       return 18;
/*      */     } 
/* 1082 */     switch (this.curChar) {
/*      */       
/*      */       case 'i':
/* 1085 */         return jjMoveStringLiteralDfa19_0(active1, 9007199254740992L, active2, 32L);
/*      */       case 'n':
/* 1087 */         if ((active2 & 0x8L) != 0L) {
/* 1088 */           return jjStopAtPos(18, 131);
/*      */         }
/*      */         break;
/*      */     } 
/*      */     
/* 1093 */     return jjStartNfa_0(17, 0L, active1, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa19_0(long old1, long active1, long old2, long active2) {
/* 1097 */     if (((active1 &= old1) | (active2 &= old2)) == 0L)
/* 1098 */       return jjStartNfa_0(17, 0L, old1, old2);  try {
/* 1099 */       this.curChar = this.input_stream.readChar();
/* 1100 */     } catch (IOException e) {
/* 1101 */       jjStopStringLiteralDfa_0(18, 0L, active1, active2);
/* 1102 */       return 19;
/*      */     } 
/* 1104 */     switch (this.curChar) {
/*      */       
/*      */       case 'f':
/* 1107 */         return jjMoveStringLiteralDfa20_0(active1, 9007199254740992L, active2, 32L);
/*      */     } 
/*      */ 
/*      */     
/* 1111 */     return jjStartNfa_0(18, 0L, active1, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa20_0(long old1, long active1, long old2, long active2) {
/* 1115 */     if (((active1 &= old1) | (active2 &= old2)) == 0L)
/* 1116 */       return jjStartNfa_0(18, 0L, old1, old2);  try {
/* 1117 */       this.curChar = this.input_stream.readChar();
/* 1118 */     } catch (IOException e) {
/* 1119 */       jjStopStringLiteralDfa_0(19, 0L, active1, active2);
/* 1120 */       return 20;
/*      */     } 
/* 1122 */     switch (this.curChar) {
/*      */       
/*      */       case 't':
/* 1125 */         if ((active1 & 0x20000000000000L) != 0L) {
/*      */           
/* 1127 */           this.jjmatchedKind = 117;
/* 1128 */           this.jjmatchedPos = 20;
/*      */         } 
/* 1130 */         return jjMoveStringLiteralDfa21_0(active1, 0L, active2, 32L);
/*      */     } 
/*      */ 
/*      */     
/* 1134 */     return jjStartNfa_0(19, 0L, active1, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa21_0(long old1, long active1, long old2, long active2) {
/* 1138 */     if (((active1 &= old1) | (active2 &= old2)) == 0L)
/* 1139 */       return jjStartNfa_0(19, 0L, old1, old2);  try {
/* 1140 */       this.curChar = this.input_stream.readChar();
/* 1141 */     } catch (IOException e) {
/* 1142 */       jjStopStringLiteralDfa_0(20, 0L, 0L, active2);
/* 1143 */       return 21;
/*      */     } 
/* 1145 */     switch (this.curChar) {
/*      */       
/*      */       case '_':
/* 1148 */         return jjMoveStringLiteralDfa22_0(active2, 32L);
/*      */     } 
/*      */ 
/*      */     
/* 1152 */     return jjStartNfa_0(20, 0L, 0L, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa22_0(long old2, long active2) {
/* 1156 */     if ((active2 &= old2) == 0L)
/* 1157 */       return jjStartNfa_0(20, 0L, 0L, old2);  try {
/* 1158 */       this.curChar = this.input_stream.readChar();
/* 1159 */     } catch (IOException e) {
/* 1160 */       jjStopStringLiteralDfa_0(21, 0L, 0L, active2);
/* 1161 */       return 22;
/*      */     } 
/* 1163 */     switch (this.curChar) {
/*      */       
/*      */       case 'a':
/* 1166 */         return jjMoveStringLiteralDfa23_0(active2, 32L);
/*      */     } 
/*      */ 
/*      */     
/* 1170 */     return jjStartNfa_0(21, 0L, 0L, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa23_0(long old2, long active2) {
/* 1174 */     if ((active2 &= old2) == 0L)
/* 1175 */       return jjStartNfa_0(21, 0L, 0L, old2);  try {
/* 1176 */       this.curChar = this.input_stream.readChar();
/* 1177 */     } catch (IOException e) {
/* 1178 */       jjStopStringLiteralDfa_0(22, 0L, 0L, active2);
/* 1179 */       return 23;
/*      */     } 
/* 1181 */     switch (this.curChar) {
/*      */       
/*      */       case 's':
/* 1184 */         return jjMoveStringLiteralDfa24_0(active2, 32L);
/*      */     } 
/*      */ 
/*      */     
/* 1188 */     return jjStartNfa_0(22, 0L, 0L, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa24_0(long old2, long active2) {
/* 1192 */     if ((active2 &= old2) == 0L)
/* 1193 */       return jjStartNfa_0(22, 0L, 0L, old2);  try {
/* 1194 */       this.curChar = this.input_stream.readChar();
/* 1195 */     } catch (IOException e) {
/* 1196 */       jjStopStringLiteralDfa_0(23, 0L, 0L, active2);
/* 1197 */       return 24;
/*      */     } 
/* 1199 */     switch (this.curChar) {
/*      */       
/*      */       case 's':
/* 1202 */         return jjMoveStringLiteralDfa25_0(active2, 32L);
/*      */     } 
/*      */ 
/*      */     
/* 1206 */     return jjStartNfa_0(23, 0L, 0L, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa25_0(long old2, long active2) {
/* 1210 */     if ((active2 &= old2) == 0L)
/* 1211 */       return jjStartNfa_0(23, 0L, 0L, old2);  try {
/* 1212 */       this.curChar = this.input_stream.readChar();
/* 1213 */     } catch (IOException e) {
/* 1214 */       jjStopStringLiteralDfa_0(24, 0L, 0L, active2);
/* 1215 */       return 25;
/*      */     } 
/* 1217 */     switch (this.curChar) {
/*      */       
/*      */       case 'i':
/* 1220 */         return jjMoveStringLiteralDfa26_0(active2, 32L);
/*      */     } 
/*      */ 
/*      */     
/* 1224 */     return jjStartNfa_0(24, 0L, 0L, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa26_0(long old2, long active2) {
/* 1228 */     if ((active2 &= old2) == 0L)
/* 1229 */       return jjStartNfa_0(24, 0L, 0L, old2);  try {
/* 1230 */       this.curChar = this.input_stream.readChar();
/* 1231 */     } catch (IOException e) {
/* 1232 */       jjStopStringLiteralDfa_0(25, 0L, 0L, active2);
/* 1233 */       return 26;
/*      */     } 
/* 1235 */     switch (this.curChar) {
/*      */       
/*      */       case 'g':
/* 1238 */         return jjMoveStringLiteralDfa27_0(active2, 32L);
/*      */     } 
/*      */ 
/*      */     
/* 1242 */     return jjStartNfa_0(25, 0L, 0L, active2);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa27_0(long old2, long active2) {
/* 1246 */     if ((active2 &= old2) == 0L)
/* 1247 */       return jjStartNfa_0(25, 0L, 0L, old2);  try {
/* 1248 */       this.curChar = this.input_stream.readChar();
/* 1249 */     } catch (IOException e) {
/* 1250 */       jjStopStringLiteralDfa_0(26, 0L, 0L, active2);
/* 1251 */       return 27;
/*      */     } 
/* 1253 */     switch (this.curChar) {
/*      */       
/*      */       case 'n':
/* 1256 */         if ((active2 & 0x20L) != 0L) {
/* 1257 */           return jjStopAtPos(27, 133);
/*      */         }
/*      */         break;
/*      */     } 
/*      */     
/* 1262 */     return jjStartNfa_0(26, 0L, 0L, active2);
/*      */   }
/*      */   
/*      */   private final void jjCheckNAdd(int state) {
/* 1266 */     if (this.jjrounds[state] != this.jjround) {
/*      */       
/* 1268 */       this.jjstateSet[this.jjnewStateCnt++] = state;
/* 1269 */       this.jjrounds[state] = this.jjround;
/*      */     } 
/*      */   }
/*      */   
/*      */   private final void jjAddStates(int start, int end) {
/*      */     do {
/* 1275 */       this.jjstateSet[this.jjnewStateCnt++] = jjnextStates[start];
/* 1276 */     } while (start++ != end);
/*      */   }
/*      */   
/*      */   private final void jjCheckNAddTwoStates(int state1, int state2) {
/* 1280 */     jjCheckNAdd(state1);
/* 1281 */     jjCheckNAdd(state2);
/*      */   }
/*      */   
/*      */   private final void jjCheckNAddStates(int start, int end) {
/*      */     do {
/* 1286 */       jjCheckNAdd(jjnextStates[start]);
/* 1287 */     } while (start++ != end);
/*      */   }
/*      */   
/*      */   private final void jjCheckNAddStates(int start) {
/* 1291 */     jjCheckNAdd(jjnextStates[start]);
/* 1292 */     jjCheckNAdd(jjnextStates[start + 1]);
/*      */   }
/* 1294 */   static final long[] jjbitVec0 = new long[] { 0L, 0L, -1L, -1L };
/*      */ 
/*      */   
/* 1297 */   static final long[] jjbitVec1 = new long[] { -2L, -1L, -1L, -1L };
/*      */ 
/*      */   
/* 1300 */   static final long[] jjbitVec3 = new long[] { 2301339413881290750L, -16384L, 4294967295L, 432345564227567616L };
/*      */ 
/*      */   
/* 1303 */   static final long[] jjbitVec4 = new long[] { 0L, 0L, 0L, -36028797027352577L };
/*      */ 
/*      */   
/* 1306 */   static final long[] jjbitVec5 = new long[] { 0L, -1L, -1L, -1L };
/*      */ 
/*      */   
/* 1309 */   static final long[] jjbitVec6 = new long[] { -1L, -1L, 65535L, 0L };
/*      */ 
/*      */   
/* 1312 */   static final long[] jjbitVec7 = new long[] { -1L, -1L, 0L, 0L };
/*      */ 
/*      */   
/* 1315 */   static final long[] jjbitVec8 = new long[] { 70368744177663L, 0L, 0L, 0L };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int jjMoveNfa_0(int startState, int curPos) {
/* 1321 */     int startsAt = 0;
/* 1322 */     this.jjnewStateCnt = 74;
/* 1323 */     int i = 1;
/* 1324 */     this.jjstateSet[0] = startState;
/* 1325 */     int kind = Integer.MAX_VALUE;
/*      */     
/*      */     while (true) {
/* 1328 */       if (++this.jjround == Integer.MAX_VALUE)
/* 1329 */         ReInitRounds(); 
/* 1330 */       if (this.curChar < '@') {
/*      */         
/* 1332 */         long l = 1L << this.curChar;
/*      */         
/*      */         do {
/* 1335 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 6:
/* 1338 */               if ((0x1FFFFFFFFL & l) != 0L) {
/*      */                 
/* 1340 */                 if (kind > 6)
/* 1341 */                   kind = 6; 
/* 1342 */                 jjCheckNAdd(0);
/*      */               }
/* 1344 */               else if ((0x3FF000000000000L & l) != 0L) {
/* 1345 */                 jjCheckNAddStates(0, 6);
/* 1346 */               } else if (this.curChar == '/') {
/* 1347 */                 jjAddStates(7, 9);
/* 1348 */               } else if (this.curChar == '$') {
/*      */                 
/* 1350 */                 if (kind > 69)
/* 1351 */                   kind = 69; 
/* 1352 */                 jjCheckNAdd(35);
/*      */               }
/* 1354 */               else if (this.curChar == '"') {
/* 1355 */                 jjCheckNAddStates(10, 12);
/* 1356 */               } else if (this.curChar == '\'') {
/* 1357 */                 jjAddStates(13, 14);
/* 1358 */               } else if (this.curChar == '.') {
/* 1359 */                 jjCheckNAdd(11);
/* 1360 */               } else if (this.curChar == '#') {
/* 1361 */                 this.jjstateSet[this.jjnewStateCnt++] = 1;
/* 1362 */               }  if ((0x3FE000000000000L & l) != 0L) {
/*      */                 
/* 1364 */                 if (kind > 60)
/* 1365 */                   kind = 60; 
/* 1366 */                 jjCheckNAddTwoStates(8, 9); break;
/*      */               } 
/* 1368 */               if (this.curChar == '0') {
/*      */                 
/* 1370 */                 if (kind > 60)
/* 1371 */                   kind = 60; 
/* 1372 */                 jjCheckNAddStates(15, 17);
/*      */               } 
/*      */               break;
/*      */             case 56:
/* 1376 */               if (this.curChar == '*') {
/* 1377 */                 this.jjstateSet[this.jjnewStateCnt++] = 67;
/* 1378 */               } else if (this.curChar == '/') {
/*      */                 
/* 1380 */                 if (kind > 7)
/* 1381 */                   kind = 7; 
/* 1382 */                 jjCheckNAddStates(18, 20);
/*      */               } 
/* 1384 */               if (this.curChar == '*')
/* 1385 */                 jjCheckNAdd(62); 
/*      */               break;
/*      */             case 0:
/* 1388 */               if ((0x1FFFFFFFFL & l) == 0L)
/*      */                 break; 
/* 1390 */               if (kind > 6)
/* 1391 */                 kind = 6; 
/* 1392 */               jjCheckNAdd(0);
/*      */               break;
/*      */             case 1:
/* 1395 */               if (this.curChar == '!')
/* 1396 */                 jjCheckNAddStates(21, 23); 
/*      */               break;
/*      */             case 2:
/* 1399 */               if ((0xFFFFFFFFFFFFDBFFL & l) != 0L)
/* 1400 */                 jjCheckNAddStates(21, 23); 
/*      */               break;
/*      */             case 3:
/* 1403 */               if ((0x2400L & l) != 0L && kind > 8)
/* 1404 */                 kind = 8; 
/*      */               break;
/*      */             case 4:
/* 1407 */               if (this.curChar == '\n' && kind > 8)
/* 1408 */                 kind = 8; 
/*      */               break;
/*      */             case 5:
/* 1411 */               if (this.curChar == '\r')
/* 1412 */                 this.jjstateSet[this.jjnewStateCnt++] = 4; 
/*      */               break;
/*      */             case 7:
/* 1415 */               if ((0x3FE000000000000L & l) == 0L)
/*      */                 break; 
/* 1417 */               if (kind > 60)
/* 1418 */                 kind = 60; 
/* 1419 */               jjCheckNAddTwoStates(8, 9);
/*      */               break;
/*      */             case 8:
/* 1422 */               if ((0x3FF000000000000L & l) == 0L)
/*      */                 break; 
/* 1424 */               if (kind > 60)
/* 1425 */                 kind = 60; 
/* 1426 */               jjCheckNAddTwoStates(8, 9);
/*      */               break;
/*      */             case 10:
/* 1429 */               if (this.curChar == '.')
/* 1430 */                 jjCheckNAdd(11); 
/*      */               break;
/*      */             case 11:
/* 1433 */               if ((0x3FF000000000000L & l) == 0L)
/*      */                 break; 
/* 1435 */               if (kind > 64)
/* 1436 */                 kind = 64; 
/* 1437 */               jjCheckNAddStates(24, 26);
/*      */               break;
/*      */             case 13:
/* 1440 */               if ((0x280000000000L & l) != 0L)
/* 1441 */                 jjCheckNAdd(14); 
/*      */               break;
/*      */             case 14:
/* 1444 */               if ((0x3FF000000000000L & l) == 0L)
/*      */                 break; 
/* 1446 */               if (kind > 64)
/* 1447 */                 kind = 64; 
/* 1448 */               jjCheckNAddTwoStates(14, 15);
/*      */               break;
/*      */             case 16:
/* 1451 */               if (this.curChar == '\'')
/* 1452 */                 jjAddStates(13, 14); 
/*      */               break;
/*      */             case 17:
/* 1455 */               if ((0xFFFFFF7FFFFFDBFFL & l) != 0L)
/* 1456 */                 jjCheckNAdd(18); 
/*      */               break;
/*      */             case 18:
/* 1459 */               if (this.curChar == '\'' && kind > 66)
/* 1460 */                 kind = 66; 
/*      */               break;
/*      */             case 20:
/* 1463 */               if ((0x8400000000L & l) != 0L)
/* 1464 */                 jjCheckNAdd(18); 
/*      */               break;
/*      */             case 21:
/* 1467 */               if ((0xFF000000000000L & l) != 0L)
/* 1468 */                 jjCheckNAddTwoStates(22, 18); 
/*      */               break;
/*      */             case 22:
/* 1471 */               if ((0xFF000000000000L & l) != 0L)
/* 1472 */                 jjCheckNAdd(18); 
/*      */               break;
/*      */             case 23:
/* 1475 */               if ((0xF000000000000L & l) != 0L)
/* 1476 */                 this.jjstateSet[this.jjnewStateCnt++] = 24; 
/*      */               break;
/*      */             case 24:
/* 1479 */               if ((0xFF000000000000L & l) != 0L)
/* 1480 */                 jjCheckNAdd(22); 
/*      */               break;
/*      */             case 25:
/* 1483 */               if (this.curChar == '"')
/* 1484 */                 jjCheckNAddStates(10, 12); 
/*      */               break;
/*      */             case 26:
/* 1487 */               if ((0xFFFFFFFBFFFFDBFFL & l) != 0L)
/* 1488 */                 jjCheckNAddStates(10, 12); 
/*      */               break;
/*      */             case 28:
/* 1491 */               if ((0x8400000000L & l) != 0L)
/* 1492 */                 jjCheckNAddStates(10, 12); 
/*      */               break;
/*      */             case 29:
/* 1495 */               if (this.curChar == '"' && kind > 67)
/* 1496 */                 kind = 67; 
/*      */               break;
/*      */             case 30:
/* 1499 */               if ((0xFF000000000000L & l) != 0L)
/* 1500 */                 jjCheckNAddStates(27, 30); 
/*      */               break;
/*      */             case 31:
/* 1503 */               if ((0xFF000000000000L & l) != 0L)
/* 1504 */                 jjCheckNAddStates(10, 12); 
/*      */               break;
/*      */             case 32:
/* 1507 */               if ((0xF000000000000L & l) != 0L)
/* 1508 */                 this.jjstateSet[this.jjnewStateCnt++] = 33; 
/*      */               break;
/*      */             case 33:
/* 1511 */               if ((0xFF000000000000L & l) != 0L)
/* 1512 */                 jjCheckNAdd(31); 
/*      */               break;
/*      */             case 34:
/* 1515 */               if (this.curChar != '$')
/*      */                 break; 
/* 1517 */               if (kind > 69)
/* 1518 */                 kind = 69; 
/* 1519 */               jjCheckNAdd(35);
/*      */               break;
/*      */             case 35:
/* 1522 */               if ((0x3FF001000000000L & l) == 0L)
/*      */                 break; 
/* 1524 */               if (kind > 69)
/* 1525 */                 kind = 69; 
/* 1526 */               jjCheckNAdd(35);
/*      */               break;
/*      */             case 36:
/* 1529 */               if ((0x3FF000000000000L & l) != 0L)
/* 1530 */                 jjCheckNAddStates(0, 6); 
/*      */               break;
/*      */             case 37:
/* 1533 */               if ((0x3FF000000000000L & l) != 0L)
/* 1534 */                 jjCheckNAddTwoStates(37, 38); 
/*      */               break;
/*      */             case 38:
/* 1537 */               if (this.curChar != '.')
/*      */                 break; 
/* 1539 */               if (kind > 64)
/* 1540 */                 kind = 64; 
/* 1541 */               jjCheckNAddStates(31, 33);
/*      */               break;
/*      */             case 39:
/* 1544 */               if ((0x3FF000000000000L & l) == 0L)
/*      */                 break; 
/* 1546 */               if (kind > 64)
/* 1547 */                 kind = 64; 
/* 1548 */               jjCheckNAddStates(31, 33);
/*      */               break;
/*      */             case 41:
/* 1551 */               if ((0x280000000000L & l) != 0L)
/* 1552 */                 jjCheckNAdd(42); 
/*      */               break;
/*      */             case 42:
/* 1555 */               if ((0x3FF000000000000L & l) == 0L)
/*      */                 break; 
/* 1557 */               if (kind > 64)
/* 1558 */                 kind = 64; 
/* 1559 */               jjCheckNAddTwoStates(42, 15);
/*      */               break;
/*      */             case 43:
/* 1562 */               if ((0x3FF000000000000L & l) != 0L)
/* 1563 */                 jjCheckNAddTwoStates(43, 44); 
/*      */               break;
/*      */             case 45:
/* 1566 */               if ((0x280000000000L & l) != 0L)
/* 1567 */                 jjCheckNAdd(46); 
/*      */               break;
/*      */             case 46:
/* 1570 */               if ((0x3FF000000000000L & l) == 0L)
/*      */                 break; 
/* 1572 */               if (kind > 64)
/* 1573 */                 kind = 64; 
/* 1574 */               jjCheckNAddTwoStates(46, 15);
/*      */               break;
/*      */             case 47:
/* 1577 */               if ((0x3FF000000000000L & l) != 0L)
/* 1578 */                 jjCheckNAddStates(34, 36); 
/*      */               break;
/*      */             case 49:
/* 1581 */               if ((0x280000000000L & l) != 0L)
/* 1582 */                 jjCheckNAdd(50); 
/*      */               break;
/*      */             case 50:
/* 1585 */               if ((0x3FF000000000000L & l) != 0L)
/* 1586 */                 jjCheckNAddTwoStates(50, 15); 
/*      */               break;
/*      */             case 51:
/* 1589 */               if (this.curChar != '0')
/*      */                 break; 
/* 1591 */               if (kind > 60)
/* 1592 */                 kind = 60; 
/* 1593 */               jjCheckNAddStates(15, 17);
/*      */               break;
/*      */             case 53:
/* 1596 */               if ((0x3FF000000000000L & l) == 0L)
/*      */                 break; 
/* 1598 */               if (kind > 60)
/* 1599 */                 kind = 60; 
/* 1600 */               jjCheckNAddTwoStates(53, 9);
/*      */               break;
/*      */             case 54:
/* 1603 */               if ((0xFF000000000000L & l) == 0L)
/*      */                 break; 
/* 1605 */               if (kind > 60)
/* 1606 */                 kind = 60; 
/* 1607 */               jjCheckNAddTwoStates(54, 9);
/*      */               break;
/*      */             case 55:
/* 1610 */               if (this.curChar == '/')
/* 1611 */                 jjAddStates(7, 9); 
/*      */               break;
/*      */             case 57:
/* 1614 */               if ((0xFFFFFFFFFFFFDBFFL & l) == 0L)
/*      */                 break; 
/* 1616 */               if (kind > 7)
/* 1617 */                 kind = 7; 
/* 1618 */               jjCheckNAddStates(18, 20);
/*      */               break;
/*      */             case 58:
/* 1621 */               if ((0x2400L & l) != 0L && kind > 7)
/* 1622 */                 kind = 7; 
/*      */               break;
/*      */             case 59:
/* 1625 */               if (this.curChar == '\n' && kind > 7)
/* 1626 */                 kind = 7; 
/*      */               break;
/*      */             case 60:
/* 1629 */               if (this.curChar == '\r')
/* 1630 */                 this.jjstateSet[this.jjnewStateCnt++] = 59; 
/*      */               break;
/*      */             case 61:
/* 1633 */               if (this.curChar == '*')
/* 1634 */                 jjCheckNAdd(62); 
/*      */               break;
/*      */             case 62:
/* 1637 */               if ((0xFFFFFBFFFFFFFFFFL & l) != 0L)
/* 1638 */                 jjCheckNAddTwoStates(62, 63); 
/*      */               break;
/*      */             case 63:
/* 1641 */               if (this.curChar == '*')
/* 1642 */                 jjCheckNAddStates(37, 39); 
/*      */               break;
/*      */             case 64:
/* 1645 */               if ((0xFFFF7BFFFFFFFFFFL & l) != 0L)
/* 1646 */                 jjCheckNAddTwoStates(65, 63); 
/*      */               break;
/*      */             case 65:
/* 1649 */               if ((0xFFFFFBFFFFFFFFFFL & l) != 0L)
/* 1650 */                 jjCheckNAddTwoStates(65, 63); 
/*      */               break;
/*      */             case 66:
/* 1653 */               if (this.curChar == '/' && kind > 9)
/* 1654 */                 kind = 9; 
/*      */               break;
/*      */             case 67:
/* 1657 */               if (this.curChar == '*')
/* 1658 */                 jjCheckNAddTwoStates(68, 69); 
/*      */               break;
/*      */             case 68:
/* 1661 */               if ((0xFFFFFBFFFFFFFFFFL & l) != 0L)
/* 1662 */                 jjCheckNAddTwoStates(68, 69); 
/*      */               break;
/*      */             case 69:
/* 1665 */               if (this.curChar == '*')
/* 1666 */                 jjCheckNAddStates(40, 42); 
/*      */               break;
/*      */             case 70:
/* 1669 */               if ((0xFFFF7BFFFFFFFFFFL & l) != 0L)
/* 1670 */                 jjCheckNAddTwoStates(71, 69); 
/*      */               break;
/*      */             case 71:
/* 1673 */               if ((0xFFFFFBFFFFFFFFFFL & l) != 0L)
/* 1674 */                 jjCheckNAddTwoStates(71, 69); 
/*      */               break;
/*      */             case 72:
/* 1677 */               if (this.curChar == '/' && kind > 68)
/* 1678 */                 kind = 68; 
/*      */               break;
/*      */             case 73:
/* 1681 */               if (this.curChar == '*') {
/* 1682 */                 this.jjstateSet[this.jjnewStateCnt++] = 67;
/*      */               }
/*      */               break;
/*      */           } 
/* 1686 */         } while (i != startsAt);
/*      */       }
/* 1688 */       else if (this.curChar < '') {
/*      */         
/* 1690 */         long l = 1L << (this.curChar & 0x3F);
/*      */         
/*      */         do {
/* 1693 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 6:
/*      */             case 35:
/* 1697 */               if ((0x7FFFFFE87FFFFFEL & l) == 0L)
/*      */                 break; 
/* 1699 */               if (kind > 69)
/* 1700 */                 kind = 69; 
/* 1701 */               jjCheckNAdd(35);
/*      */               break;
/*      */             case 2:
/* 1704 */               jjAddStates(21, 23);
/*      */               break;
/*      */             case 9:
/* 1707 */               if ((0x100000001000L & l) != 0L && kind > 60)
/* 1708 */                 kind = 60; 
/*      */               break;
/*      */             case 12:
/* 1711 */               if ((0x2000000020L & l) != 0L)
/* 1712 */                 jjAddStates(43, 44); 
/*      */               break;
/*      */             case 15:
/* 1715 */               if ((0x5000000050L & l) != 0L && kind > 64)
/* 1716 */                 kind = 64; 
/*      */               break;
/*      */             case 17:
/* 1719 */               if ((0xFFFFFFFFEFFFFFFFL & l) != 0L)
/* 1720 */                 jjCheckNAdd(18); 
/*      */               break;
/*      */             case 19:
/* 1723 */               if (this.curChar == '\\')
/* 1724 */                 jjAddStates(45, 47); 
/*      */               break;
/*      */             case 20:
/* 1727 */               if ((0x14404410000000L & l) != 0L)
/* 1728 */                 jjCheckNAdd(18); 
/*      */               break;
/*      */             case 26:
/* 1731 */               if ((0xFFFFFFFFEFFFFFFFL & l) != 0L)
/* 1732 */                 jjCheckNAddStates(10, 12); 
/*      */               break;
/*      */             case 27:
/* 1735 */               if (this.curChar == '\\')
/* 1736 */                 jjAddStates(48, 50); 
/*      */               break;
/*      */             case 28:
/* 1739 */               if ((0x14404410000000L & l) != 0L)
/* 1740 */                 jjCheckNAddStates(10, 12); 
/*      */               break;
/*      */             case 40:
/* 1743 */               if ((0x2000000020L & l) != 0L)
/* 1744 */                 jjAddStates(51, 52); 
/*      */               break;
/*      */             case 44:
/* 1747 */               if ((0x2000000020L & l) != 0L)
/* 1748 */                 jjAddStates(53, 54); 
/*      */               break;
/*      */             case 48:
/* 1751 */               if ((0x2000000020L & l) != 0L)
/* 1752 */                 jjAddStates(55, 56); 
/*      */               break;
/*      */             case 52:
/* 1755 */               if ((0x100000001000000L & l) != 0L)
/* 1756 */                 jjCheckNAdd(53); 
/*      */               break;
/*      */             case 53:
/* 1759 */               if ((0x7E0000007EL & l) == 0L)
/*      */                 break; 
/* 1761 */               if (kind > 60)
/* 1762 */                 kind = 60; 
/* 1763 */               jjCheckNAddTwoStates(53, 9);
/*      */               break;
/*      */             case 57:
/* 1766 */               if (kind > 7)
/* 1767 */                 kind = 7; 
/* 1768 */               jjAddStates(18, 20);
/*      */               break;
/*      */             case 62:
/* 1771 */               jjCheckNAddTwoStates(62, 63);
/*      */               break;
/*      */             case 64:
/*      */             case 65:
/* 1775 */               jjCheckNAddTwoStates(65, 63);
/*      */               break;
/*      */             case 68:
/* 1778 */               jjCheckNAddTwoStates(68, 69);
/*      */               break;
/*      */             case 70:
/*      */             case 71:
/* 1782 */               jjCheckNAddTwoStates(71, 69);
/*      */               break;
/*      */           } 
/*      */         
/* 1786 */         } while (i != startsAt);
/*      */       }
/*      */       else {
/*      */         
/* 1790 */         int hiByte = this.curChar >> 8;
/* 1791 */         int i1 = hiByte >> 6;
/* 1792 */         long l1 = 1L << (hiByte & 0x3F);
/* 1793 */         int i2 = (this.curChar & 0xFF) >> 6;
/* 1794 */         long l2 = 1L << (this.curChar & 0x3F);
/*      */         
/*      */         do {
/* 1797 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 6:
/* 1800 */               if (jjCanMove_0(hiByte, i1, i2, l1, l2)) {
/*      */                 
/* 1802 */                 if (kind > 6)
/* 1803 */                   kind = 6; 
/* 1804 */                 jjCheckNAdd(0);
/*      */               } 
/* 1806 */               if (jjCanMove_2(hiByte, i1, i2, l1, l2)) {
/*      */                 
/* 1808 */                 if (kind > 69)
/* 1809 */                   kind = 69; 
/* 1810 */                 jjCheckNAdd(35);
/*      */               } 
/*      */               break;
/*      */             case 0:
/* 1814 */               if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1816 */               if (kind > 6)
/* 1817 */                 kind = 6; 
/* 1818 */               jjCheckNAdd(0);
/*      */               break;
/*      */             case 2:
/* 1821 */               if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/* 1822 */                 jjAddStates(21, 23); 
/*      */               break;
/*      */             case 17:
/* 1825 */               if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/* 1826 */                 this.jjstateSet[this.jjnewStateCnt++] = 18; 
/*      */               break;
/*      */             case 26:
/* 1829 */               if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/* 1830 */                 jjAddStates(10, 12); 
/*      */               break;
/*      */             case 34:
/*      */             case 35:
/* 1834 */               if (!jjCanMove_2(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1836 */               if (kind > 69)
/* 1837 */                 kind = 69; 
/* 1838 */               jjCheckNAdd(35);
/*      */               break;
/*      */             case 57:
/* 1841 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1843 */               if (kind > 7)
/* 1844 */                 kind = 7; 
/* 1845 */               jjAddStates(18, 20);
/*      */               break;
/*      */             case 62:
/* 1848 */               if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/* 1849 */                 jjCheckNAddTwoStates(62, 63); 
/*      */               break;
/*      */             case 64:
/*      */             case 65:
/* 1853 */               if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/* 1854 */                 jjCheckNAddTwoStates(65, 63); 
/*      */               break;
/*      */             case 68:
/* 1857 */               if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/* 1858 */                 jjCheckNAddTwoStates(68, 69); 
/*      */               break;
/*      */             case 70:
/*      */             case 71:
/* 1862 */               if (jjCanMove_1(hiByte, i1, i2, l1, l2)) {
/* 1863 */                 jjCheckNAddTwoStates(71, 69);
/*      */               }
/*      */               break;
/*      */           } 
/* 1867 */         } while (i != startsAt);
/*      */       } 
/* 1869 */       if (kind != Integer.MAX_VALUE) {
/*      */         
/* 1871 */         this.jjmatchedKind = kind;
/* 1872 */         this.jjmatchedPos = curPos;
/* 1873 */         kind = Integer.MAX_VALUE;
/*      */       } 
/* 1875 */       curPos++;
/* 1876 */       if ((i = this.jjnewStateCnt) == (startsAt = 74 - (this.jjnewStateCnt = startsAt)))
/* 1877 */         return curPos;  
/* 1878 */       try { this.curChar = this.input_stream.readChar(); }
/* 1879 */       catch (IOException e) { return curPos; }
/*      */     
/*      */     } 
/* 1882 */   } static final int[] jjnextStates = new int[] { 37, 38, 43, 44, 47, 48, 15, 56, 61, 73, 26, 27, 29, 17, 19, 52, 54, 9, 57, 58, 60, 2, 3, 5, 11, 12, 15, 26, 27, 31, 29, 39, 40, 15, 47, 48, 15, 63, 64, 66, 69, 70, 72, 13, 14, 20, 21, 23, 28, 30, 32, 41, 42, 45, 46, 49, 50 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2) {
/* 1890 */     switch (hiByte) {
/*      */       
/*      */       case 0:
/* 1893 */         return ((jjbitVec0[i2] & l2) != 0L);
/*      */     } 
/* 1895 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean jjCanMove_1(int hiByte, int i1, int i2, long l1, long l2) {
/* 1900 */     switch (hiByte) {
/*      */       
/*      */       case 0:
/* 1903 */         return ((jjbitVec0[i2] & l2) != 0L);
/*      */     } 
/* 1905 */     if ((jjbitVec1[i1] & l1) != 0L)
/* 1906 */       return true; 
/* 1907 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean jjCanMove_2(int hiByte, int i1, int i2, long l1, long l2) {
/* 1912 */     switch (hiByte) {
/*      */       
/*      */       case 0:
/* 1915 */         return ((jjbitVec4[i2] & l2) != 0L);
/*      */       case 48:
/* 1917 */         return ((jjbitVec5[i2] & l2) != 0L);
/*      */       case 49:
/* 1919 */         return ((jjbitVec6[i2] & l2) != 0L);
/*      */       case 51:
/* 1921 */         return ((jjbitVec7[i2] & l2) != 0L);
/*      */       case 61:
/* 1923 */         return ((jjbitVec8[i2] & l2) != 0L);
/*      */     } 
/* 1925 */     if ((jjbitVec3[i1] & l1) != 0L)
/* 1926 */       return true; 
/* 1927 */     return false;
/*      */   }
/*      */   
/* 1930 */   public static final String[] jjstrLiteralImages = new String[] { "", null, null, null, null, null, null, null, null, null, "abstract", "boolean", "break", "class", "byte", "case", "catch", "char", "const", "continue", "default", "do", "double", "else", "enum", "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "switch", "synchronized", "transient", "throw", "throws", "true", "try", "void", "volatile", "while", null, null, null, null, null, null, null, null, null, null, null, null, "(", ")", "{", "}", "[", "]", ";", ",", ".", "=", ">", "@gt", "<", "@lt", "!", "~", "?", ":", "==", "<=", "@lteq", ">=", "@gteq", "!=", "||", "@or", "&&", "@and", "++", "--", "+", "-", "*", "/", "&", "@bitwise_and", "|", "@bitwise_or", "^", "%", "<<", "@left_shift", ">>", "@right_shift", ">>>", "@right_unsigned_shift", "+=", "-=", "*=", "/=", "&=", "@and_assign", "|=", "@or_assign", "^=", "%=", "<<=", "@left_shift_assign", ">>=", "@right_shift_assign", ">>>=", "@right_unsigned_shift_assign" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1961 */   public static final String[] lexStateNames = new String[] { "DEFAULT" };
/*      */ 
/*      */   
/* 1964 */   static final long[] jjtoToken = new long[] { 2305843009213692929L, -195L, 63L };
/*      */ 
/*      */   
/* 1967 */   static final long[] jjtoSkip = new long[] { 1022L, 0L, 0L };
/*      */ 
/*      */   
/* 1970 */   static final long[] jjtoSpecial = new long[] { 896L, 0L, 0L };
/*      */   
/*      */   protected JavaCharStream input_stream;
/*      */   
/* 1974 */   private final int[] jjrounds = new int[74];
/* 1975 */   private final int[] jjstateSet = new int[148];
/*      */   protected char curChar;
/*      */   int curLexState;
/*      */   int defaultLexState;
/*      */   int jjnewStateCnt;
/*      */   int jjround;
/*      */   int jjmatchedPos;
/*      */   int jjmatchedKind;
/*      */   
/*      */   public ParserTokenManager(JavaCharStream stream, int lexState) {
/* 1985 */     this(stream);
/* 1986 */     SwitchTo(lexState);
/*      */   }
/*      */   
/*      */   public void ReInit(JavaCharStream stream) {
/* 1990 */     this.jjmatchedPos = this.jjnewStateCnt = 0;
/* 1991 */     this.curLexState = this.defaultLexState;
/* 1992 */     this.input_stream = stream;
/* 1993 */     ReInitRounds();
/*      */   }
/*      */ 
/*      */   
/*      */   private final void ReInitRounds() {
/* 1998 */     this.jjround = -2147483647;
/* 1999 */     for (int i = 74; i-- > 0;)
/* 2000 */       this.jjrounds[i] = Integer.MIN_VALUE; 
/*      */   }
/*      */   
/*      */   public void ReInit(JavaCharStream stream, int lexState) {
/* 2004 */     ReInit(stream);
/* 2005 */     SwitchTo(lexState);
/*      */   }
/*      */   
/*      */   public void SwitchTo(int lexState) {
/* 2009 */     if (lexState >= 1 || lexState < 0) {
/* 2010 */       throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
/*      */     }
/* 2012 */     this.curLexState = lexState;
/*      */   }
/*      */ 
/*      */   
/*      */   protected Token jjFillToken() {
/* 2017 */     Token t = Token.newToken(this.jjmatchedKind);
/* 2018 */     t.kind = this.jjmatchedKind;
/* 2019 */     String im = jjstrLiteralImages[this.jjmatchedKind];
/* 2020 */     t.image = (im == null) ? this.input_stream.GetImage() : im;
/* 2021 */     t.beginLine = this.input_stream.getBeginLine();
/* 2022 */     t.beginColumn = this.input_stream.getBeginColumn();
/* 2023 */     t.endLine = this.input_stream.getEndLine();
/* 2024 */     t.endColumn = this.input_stream.getEndColumn();
/* 2025 */     return t;
/*      */   }
/*      */   public ParserTokenManager(JavaCharStream stream) {
/* 2028 */     this.curLexState = 0;
/* 2029 */     this.defaultLexState = 0;
/*      */     this.input_stream = stream;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Token getNextToken() {
/* 2038 */     Token specialToken = null;
/*      */     
/* 2040 */     int curPos = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/*      */       try {
/* 2047 */         this.curChar = this.input_stream.BeginToken();
/*      */       }
/* 2049 */       catch (IOException e) {
/*      */         
/* 2051 */         this.jjmatchedKind = 0;
/* 2052 */         Token matchedToken = jjFillToken();
/* 2053 */         matchedToken.specialToken = specialToken;
/* 2054 */         return matchedToken;
/*      */       } 
/*      */       
/* 2057 */       this.jjmatchedKind = Integer.MAX_VALUE;
/* 2058 */       this.jjmatchedPos = 0;
/* 2059 */       curPos = jjMoveStringLiteralDfa0_0();
/* 2060 */       if (this.jjmatchedKind != Integer.MAX_VALUE) {
/*      */         
/* 2062 */         if (this.jjmatchedPos + 1 < curPos)
/* 2063 */           this.input_stream.backup(curPos - this.jjmatchedPos - 1); 
/* 2064 */         if ((jjtoToken[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 0x3F)) != 0L) {
/*      */           
/* 2066 */           Token matchedToken = jjFillToken();
/* 2067 */           matchedToken.specialToken = specialToken;
/* 2068 */           return matchedToken;
/*      */         } 
/*      */ 
/*      */         
/* 2072 */         if ((jjtoSpecial[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 0x3F)) != 0L) {
/*      */           
/* 2074 */           Token matchedToken = jjFillToken();
/* 2075 */           if (specialToken == null) {
/* 2076 */             specialToken = matchedToken;
/*      */             continue;
/*      */           } 
/* 2079 */           matchedToken.specialToken = specialToken;
/* 2080 */           specialToken = specialToken.next = matchedToken;
/*      */         } 
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 2086 */     int error_line = this.input_stream.getEndLine();
/* 2087 */     int error_column = this.input_stream.getEndColumn();
/* 2088 */     String error_after = null;
/* 2089 */     boolean EOFSeen = false; try {
/* 2090 */       this.input_stream.readChar(); this.input_stream.backup(1);
/* 2091 */     } catch (IOException e1) {
/* 2092 */       EOFSeen = true;
/* 2093 */       error_after = (curPos <= 1) ? "" : this.input_stream.GetImage();
/* 2094 */       if (this.curChar == '\n' || this.curChar == '\r') {
/* 2095 */         error_line++;
/* 2096 */         error_column = 0;
/*      */       } else {
/*      */         
/* 2099 */         error_column++;
/*      */       } 
/* 2101 */     }  if (!EOFSeen) {
/* 2102 */       this.input_stream.backup(1);
/* 2103 */       error_after = (curPos <= 1) ? "" : this.input_stream.GetImage();
/*      */     } 
/* 2105 */     throw new TokenMgrError(EOFSeen, this.curLexState, error_line, error_column, error_after, this.curChar, 0);
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/ParserTokenManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */