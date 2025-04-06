/*      */ package bsh;
/*      */ 
/*      */ import java.io.FileReader;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Vector;
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
/*      */ public class Parser
/*      */   implements ParserTreeConstants, ParserConstants
/*      */ {
/*   33 */   protected JJTParserState jjtree = new JJTParserState(); boolean retainComments = false; public ParserTokenManager token_source; JavaCharStream jj_input_stream; public Token token; public Token jj_nt; private int jj_ntk; private Token jj_scanpos; private Token jj_lastpos; private int jj_la;
/*      */   
/*      */   public void setRetainComments(boolean b) {
/*   36 */     this.retainComments = b;
/*      */   }
/*      */   
/*      */   void jjtreeOpenNodeScope(Node n) {
/*   40 */     ((SimpleNode)n).firstToken = getToken(1);
/*      */   }
/*      */   
/*      */   void jjtreeCloseNodeScope(Node n) {
/*   44 */     ((SimpleNode)n).lastToken = getToken(0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void reInitInput(Reader in) {
/*   51 */     ReInit(in);
/*      */   }
/*      */ 
/*      */   
/*      */   public SimpleNode popNode() {
/*   56 */     if (this.jjtree.nodeArity() > 0) {
/*   57 */       return (SimpleNode)this.jjtree.popNode();
/*      */     }
/*   59 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void reInitTokenInput(Reader in) {
/*   68 */     this.jj_input_stream.ReInit(in, this.jj_input_stream.getEndLine(), this.jj_input_stream.getEndColumn());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void main(String[] args) throws IOException, ParseException {
/*   76 */     boolean print = false;
/*   77 */     int i = 0;
/*   78 */     if (args[0].equals("-p")) {
/*   79 */       i++;
/*   80 */       print = true;
/*      */     } 
/*   82 */     for (; i < args.length; i++) {
/*   83 */       Reader in = new FileReader(args[i]);
/*   84 */       Parser parser = new Parser(in);
/*   85 */       parser.setRetainComments(true);
/*   86 */       while (!parser.Line()) {
/*   87 */         if (print) {
/*   88 */           System.out.println(parser.popNode());
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isRegularForStatement() {
/*   98 */     int curTok = 1;
/*      */     
/*  100 */     Token tok = getToken(curTok++);
/*  101 */     if (tok.kind != 30) return false; 
/*  102 */     tok = getToken(curTok++);
/*  103 */     if (tok.kind != 72) return false;
/*      */     
/*      */     while (true)
/*  106 */     { tok = getToken(curTok++);
/*  107 */       switch (tok.kind)
/*      */       { case 89:
/*  109 */           return false;
/*      */         case 78:
/*  111 */           return true;
/*      */         case 0:
/*  113 */           break; }  }  return false;
/*      */   }
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
/*      */   ParseException createParseException(String message) {
/*  126 */     Token errortok = this.token;
/*  127 */     int line = errortok.beginLine, column = errortok.beginColumn;
/*  128 */     String mess = (errortok.kind == 0) ? tokenImage[0] : errortok.image;
/*  129 */     return new ParseException("Parse error at line " + line + ", column " + column + " : " + message);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean Line() throws ParseException {
/*  138 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 0:
/*  140 */         jj_consume_token(0);
/*  141 */         Interpreter.debug("End of File!");
/*  142 */         return true;
/*      */     } 
/*      */     
/*  145 */     this.jj_la1[0] = this.jj_gen;
/*  146 */     if (jj_2_1(1)) {
/*  147 */       BlockStatement();
/*  148 */       return false;
/*      */     } 
/*  150 */     jj_consume_token(-1);
/*  151 */     throw new ParseException();
/*      */   }
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
/*      */   public final Modifiers Modifiers(int context, boolean lookahead) throws ParseException {
/*  168 */     Modifiers mods = null;
/*      */     
/*      */     while (true) {
/*  171 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 10:
/*      */         case 27:
/*      */         case 39:
/*      */         case 43:
/*      */         case 44:
/*      */         case 45:
/*      */         case 48:
/*      */         case 49:
/*      */         case 51:
/*      */         case 52:
/*      */         case 58:
/*      */           break;
/*      */         
/*      */         default:
/*  186 */           this.jj_la1[1] = this.jj_gen;
/*      */           break;
/*      */       } 
/*  189 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 43:
/*  191 */           jj_consume_token(43);
/*      */           break;
/*      */         case 44:
/*  194 */           jj_consume_token(44);
/*      */           break;
/*      */         case 45:
/*  197 */           jj_consume_token(45);
/*      */           break;
/*      */         case 51:
/*  200 */           jj_consume_token(51);
/*      */           break;
/*      */         case 27:
/*  203 */           jj_consume_token(27);
/*      */           break;
/*      */         case 39:
/*  206 */           jj_consume_token(39);
/*      */           break;
/*      */         case 52:
/*  209 */           jj_consume_token(52);
/*      */           break;
/*      */         case 58:
/*  212 */           jj_consume_token(58);
/*      */           break;
/*      */         case 10:
/*  215 */           jj_consume_token(10);
/*      */           break;
/*      */         case 48:
/*  218 */           jj_consume_token(48);
/*      */           break;
/*      */         case 49:
/*  221 */           jj_consume_token(49);
/*      */           break;
/*      */         default:
/*  224 */           this.jj_la1[2] = this.jj_gen;
/*  225 */           jj_consume_token(-1);
/*  226 */           throw new ParseException();
/*      */       } 
/*  228 */       if (!lookahead)
/*      */         try {
/*  230 */           if (mods == null) mods = new Modifiers(); 
/*  231 */           mods.addModifier(context, (getToken(0)).image);
/*  232 */         } catch (IllegalStateException e) {
/*  233 */           throw createParseException(e.getMessage());
/*      */         }  
/*      */     } 
/*  236 */     return mods;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void ClassDeclaration() throws ParseException {
/*  244 */     BSHClassDeclaration jjtn000 = new BSHClassDeclaration(1);
/*  245 */     boolean jjtc000 = true;
/*  246 */     this.jjtree.openNodeScope(jjtn000);
/*  247 */     jjtreeOpenNodeScope(jjtn000);
/*      */     
/*      */     try {
/*      */       int numInterfaces;
/*  251 */       Modifiers mods = Modifiers(0, false);
/*  252 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 13:
/*  254 */           jj_consume_token(13);
/*      */           break;
/*      */         case 37:
/*  257 */           jj_consume_token(37);
/*  258 */           jjtn000.isInterface = true;
/*      */           break;
/*      */         default:
/*  261 */           this.jj_la1[3] = this.jj_gen;
/*  262 */           jj_consume_token(-1);
/*  263 */           throw new ParseException();
/*      */       } 
/*  265 */       Token name = jj_consume_token(69);
/*  266 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 25:
/*  268 */           jj_consume_token(25);
/*  269 */           AmbiguousName();
/*  270 */           jjtn000.extend = true;
/*      */           break;
/*      */         default:
/*  273 */           this.jj_la1[4] = this.jj_gen;
/*      */           break;
/*      */       } 
/*  276 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 33:
/*  278 */           jj_consume_token(33);
/*  279 */           numInterfaces = NameList();
/*  280 */           jjtn000.numInterfaces = numInterfaces;
/*      */           break;
/*      */         default:
/*  283 */           this.jj_la1[5] = this.jj_gen;
/*      */           break;
/*      */       } 
/*  286 */       Block();
/*  287 */       this.jjtree.closeNodeScope(jjtn000, true);
/*  288 */       jjtc000 = false;
/*  289 */       jjtreeCloseNodeScope(jjtn000);
/*  290 */       jjtn000.modifiers = mods;
/*  291 */       jjtn000.name = name.image;
/*  292 */     } catch (Throwable jjte000) {
/*  293 */       if (jjtc000) {
/*  294 */         this.jjtree.clearNodeScope(jjtn000);
/*  295 */         jjtc000 = false;
/*      */       } else {
/*  297 */         this.jjtree.popNode();
/*      */       } 
/*  299 */       if (jjte000 instanceof RuntimeException) {
/*  300 */         throw (RuntimeException)jjte000;
/*      */       }
/*  302 */       if (jjte000 instanceof ParseException) {
/*  303 */         throw (ParseException)jjte000;
/*      */       }
/*  305 */       throw (Error)jjte000;
/*      */     } finally {
/*  307 */       if (jjtc000) {
/*  308 */         this.jjtree.closeNodeScope(jjtn000, true);
/*  309 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void MethodDeclaration() throws ParseException {
/*  316 */     BSHMethodDeclaration jjtn000 = new BSHMethodDeclaration(2);
/*  317 */     boolean jjtc000 = true;
/*  318 */     this.jjtree.openNodeScope(jjtn000);
/*  319 */     jjtreeOpenNodeScope(jjtn000); Token t = null;
/*      */     
/*      */     try {
/*      */       int count;
/*  323 */       Modifiers mods = Modifiers(1, false);
/*  324 */       jjtn000.modifiers = mods;
/*  325 */       if (jj_2_2(2147483647)) {
/*  326 */         t = jj_consume_token(69);
/*  327 */         jjtn000.name = t.image;
/*      */       } else {
/*  329 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 11:
/*      */           case 14:
/*      */           case 17:
/*      */           case 22:
/*      */           case 29:
/*      */           case 36:
/*      */           case 38:
/*      */           case 47:
/*      */           case 57:
/*      */           case 69:
/*  340 */             ReturnType();
/*  341 */             t = jj_consume_token(69);
/*  342 */             jjtn000.name = t.image;
/*      */             break;
/*      */           default:
/*  345 */             this.jj_la1[6] = this.jj_gen;
/*  346 */             jj_consume_token(-1);
/*  347 */             throw new ParseException();
/*      */         } 
/*      */       } 
/*  350 */       FormalParameters();
/*  351 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 54:
/*  353 */           jj_consume_token(54);
/*  354 */           count = NameList();
/*  355 */           jjtn000.numThrows = count;
/*      */           break;
/*      */         default:
/*  358 */           this.jj_la1[7] = this.jj_gen;
/*      */           break;
/*      */       } 
/*  361 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 74:
/*  363 */           Block();
/*      */           break;
/*      */         case 78:
/*  366 */           jj_consume_token(78);
/*      */           break;
/*      */         default:
/*  369 */           this.jj_la1[8] = this.jj_gen;
/*  370 */           jj_consume_token(-1);
/*  371 */           throw new ParseException();
/*      */       } 
/*  373 */     } catch (Throwable jjte000) {
/*  374 */       if (jjtc000) {
/*  375 */         this.jjtree.clearNodeScope(jjtn000);
/*  376 */         jjtc000 = false;
/*      */       } else {
/*  378 */         this.jjtree.popNode();
/*      */       } 
/*  380 */       if (jjte000 instanceof RuntimeException) {
/*  381 */         throw (RuntimeException)jjte000;
/*      */       }
/*  383 */       if (jjte000 instanceof ParseException) {
/*  384 */         throw (ParseException)jjte000;
/*      */       }
/*  386 */       throw (Error)jjte000;
/*      */     } finally {
/*  388 */       if (jjtc000) {
/*  389 */         this.jjtree.closeNodeScope(jjtn000, true);
/*  390 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void PackageDeclaration() throws ParseException {
/*  397 */     BSHPackageDeclaration jjtn000 = new BSHPackageDeclaration(3);
/*  398 */     boolean jjtc000 = true;
/*  399 */     this.jjtree.openNodeScope(jjtn000);
/*  400 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/*  402 */       jj_consume_token(42);
/*  403 */       AmbiguousName();
/*  404 */     } catch (Throwable jjte000) {
/*  405 */       if (jjtc000) {
/*  406 */         this.jjtree.clearNodeScope(jjtn000);
/*  407 */         jjtc000 = false;
/*      */       } else {
/*  409 */         this.jjtree.popNode();
/*      */       } 
/*  411 */       if (jjte000 instanceof RuntimeException) {
/*  412 */         throw (RuntimeException)jjte000;
/*      */       }
/*  414 */       if (jjte000 instanceof ParseException) {
/*  415 */         throw (ParseException)jjte000;
/*      */       }
/*  417 */       throw (Error)jjte000;
/*      */     } finally {
/*  419 */       if (jjtc000) {
/*  420 */         this.jjtree.closeNodeScope(jjtn000, true);
/*  421 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void ImportDeclaration() throws ParseException {
/*  428 */     BSHImportDeclaration jjtn000 = new BSHImportDeclaration(4);
/*  429 */     boolean jjtc000 = true;
/*  430 */     this.jjtree.openNodeScope(jjtn000);
/*  431 */     jjtreeOpenNodeScope(jjtn000); Token s = null;
/*  432 */     Token t = null;
/*      */     try {
/*  434 */       if (jj_2_3(3)) {
/*  435 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 48:
/*  437 */             s = jj_consume_token(48);
/*      */             break;
/*      */           default:
/*  440 */             this.jj_la1[9] = this.jj_gen;
/*      */             break;
/*      */         } 
/*  443 */         jj_consume_token(34);
/*  444 */         AmbiguousName();
/*  445 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 80:
/*  447 */             t = jj_consume_token(80);
/*  448 */             jj_consume_token(104);
/*      */             break;
/*      */           default:
/*  451 */             this.jj_la1[10] = this.jj_gen;
/*      */             break;
/*      */         } 
/*  454 */         jj_consume_token(78);
/*  455 */         this.jjtree.closeNodeScope(jjtn000, true);
/*  456 */         jjtc000 = false;
/*  457 */         jjtreeCloseNodeScope(jjtn000);
/*  458 */         if (s != null) jjtn000.staticImport = true; 
/*  459 */         if (t != null) jjtn000.importPackage = true; 
/*      */       } else {
/*  461 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 34:
/*  463 */             jj_consume_token(34);
/*  464 */             jj_consume_token(104);
/*  465 */             jj_consume_token(78);
/*  466 */             this.jjtree.closeNodeScope(jjtn000, true);
/*  467 */             jjtc000 = false;
/*  468 */             jjtreeCloseNodeScope(jjtn000);
/*  469 */             jjtn000.superImport = true;
/*      */             break;
/*      */           default:
/*  472 */             this.jj_la1[11] = this.jj_gen;
/*  473 */             jj_consume_token(-1);
/*  474 */             throw new ParseException();
/*      */         } 
/*      */       } 
/*  477 */     } catch (Throwable jjte000) {
/*  478 */       if (jjtc000) {
/*  479 */         this.jjtree.clearNodeScope(jjtn000);
/*  480 */         jjtc000 = false;
/*      */       } else {
/*  482 */         this.jjtree.popNode();
/*      */       } 
/*  484 */       if (jjte000 instanceof RuntimeException) {
/*  485 */         throw (RuntimeException)jjte000;
/*      */       }
/*  487 */       if (jjte000 instanceof ParseException) {
/*  488 */         throw (ParseException)jjte000;
/*      */       }
/*  490 */       throw (Error)jjte000;
/*      */     } finally {
/*  492 */       if (jjtc000) {
/*  493 */         this.jjtree.closeNodeScope(jjtn000, true);
/*  494 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void VariableDeclarator() throws ParseException {
/*  501 */     BSHVariableDeclarator jjtn000 = new BSHVariableDeclarator(5);
/*  502 */     boolean jjtc000 = true;
/*  503 */     this.jjtree.openNodeScope(jjtn000);
/*  504 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/*  506 */       Token t = jj_consume_token(69);
/*  507 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 81:
/*  509 */           jj_consume_token(81);
/*  510 */           VariableInitializer();
/*      */           break;
/*      */         default:
/*  513 */           this.jj_la1[12] = this.jj_gen;
/*      */           break;
/*      */       } 
/*  516 */       this.jjtree.closeNodeScope(jjtn000, true);
/*  517 */       jjtc000 = false;
/*  518 */       jjtreeCloseNodeScope(jjtn000);
/*  519 */       jjtn000.name = t.image;
/*  520 */     } catch (Throwable jjte000) {
/*  521 */       if (jjtc000) {
/*  522 */         this.jjtree.clearNodeScope(jjtn000);
/*  523 */         jjtc000 = false;
/*      */       } else {
/*  525 */         this.jjtree.popNode();
/*      */       } 
/*  527 */       if (jjte000 instanceof RuntimeException) {
/*  528 */         throw (RuntimeException)jjte000;
/*      */       }
/*  530 */       if (jjte000 instanceof ParseException) {
/*  531 */         throw (ParseException)jjte000;
/*      */       }
/*  533 */       throw (Error)jjte000;
/*      */     } finally {
/*  535 */       if (jjtc000) {
/*  536 */         this.jjtree.closeNodeScope(jjtn000, true);
/*  537 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
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
/*      */   public final void VariableInitializer() throws ParseException {
/*  553 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 74:
/*  555 */         ArrayInitializer();
/*      */         return;
/*      */       case 11:
/*      */       case 14:
/*      */       case 17:
/*      */       case 22:
/*      */       case 26:
/*      */       case 29:
/*      */       case 36:
/*      */       case 38:
/*      */       case 40:
/*      */       case 41:
/*      */       case 47:
/*      */       case 55:
/*      */       case 57:
/*      */       case 60:
/*      */       case 64:
/*      */       case 66:
/*      */       case 67:
/*      */       case 69:
/*      */       case 72:
/*      */       case 86:
/*      */       case 87:
/*      */       case 100:
/*      */       case 101:
/*      */       case 102:
/*      */       case 103:
/*  582 */         Expression();
/*      */         return;
/*      */     } 
/*  585 */     this.jj_la1[13] = this.jj_gen;
/*  586 */     jj_consume_token(-1);
/*  587 */     throw new ParseException();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void ArrayInitializer() throws ParseException {
/*  593 */     BSHArrayInitializer jjtn000 = new BSHArrayInitializer(6);
/*  594 */     boolean jjtc000 = true;
/*  595 */     this.jjtree.openNodeScope(jjtn000);
/*  596 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/*  598 */       jj_consume_token(74);
/*  599 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 11:
/*      */         case 14:
/*      */         case 17:
/*      */         case 22:
/*      */         case 26:
/*      */         case 29:
/*      */         case 36:
/*      */         case 38:
/*      */         case 40:
/*      */         case 41:
/*      */         case 47:
/*      */         case 55:
/*      */         case 57:
/*      */         case 60:
/*      */         case 64:
/*      */         case 66:
/*      */         case 67:
/*      */         case 69:
/*      */         case 72:
/*      */         case 74:
/*      */         case 86:
/*      */         case 87:
/*      */         case 100:
/*      */         case 101:
/*      */         case 102:
/*      */         case 103:
/*  626 */           VariableInitializer();
/*      */ 
/*      */           
/*  629 */           while (jj_2_4(2)) {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  634 */             jj_consume_token(79);
/*  635 */             VariableInitializer();
/*      */           } 
/*      */           break;
/*      */         default:
/*  639 */           this.jj_la1[14] = this.jj_gen;
/*      */           break;
/*      */       } 
/*  642 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 79:
/*  644 */           jj_consume_token(79);
/*      */           break;
/*      */         default:
/*  647 */           this.jj_la1[15] = this.jj_gen;
/*      */           break;
/*      */       } 
/*  650 */       jj_consume_token(75);
/*  651 */     } catch (Throwable jjte000) {
/*  652 */       if (jjtc000) {
/*  653 */         this.jjtree.clearNodeScope(jjtn000);
/*  654 */         jjtc000 = false;
/*      */       } else {
/*  656 */         this.jjtree.popNode();
/*      */       } 
/*  658 */       if (jjte000 instanceof RuntimeException) {
/*  659 */         throw (RuntimeException)jjte000;
/*      */       }
/*  661 */       if (jjte000 instanceof ParseException) {
/*  662 */         throw (ParseException)jjte000;
/*      */       }
/*  664 */       throw (Error)jjte000;
/*      */     } finally {
/*  666 */       if (jjtc000) {
/*  667 */         this.jjtree.closeNodeScope(jjtn000, true);
/*  668 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void FormalParameters() throws ParseException {
/*  675 */     BSHFormalParameters jjtn000 = new BSHFormalParameters(7);
/*  676 */     boolean jjtc000 = true;
/*  677 */     this.jjtree.openNodeScope(jjtn000);
/*  678 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/*  680 */       jj_consume_token(72);
/*  681 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 11:
/*      */         case 14:
/*      */         case 17:
/*      */         case 22:
/*      */         case 29:
/*      */         case 36:
/*      */         case 38:
/*      */         case 47:
/*      */         case 69:
/*  691 */           FormalParameter();
/*      */           
/*      */           while (true) {
/*  694 */             switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */               case 79:
/*      */                 break;
/*      */               
/*      */               default:
/*  699 */                 this.jj_la1[16] = this.jj_gen;
/*      */                 break;
/*      */             } 
/*  702 */             jj_consume_token(79);
/*  703 */             FormalParameter();
/*      */           } 
/*      */           break;
/*      */         default:
/*  707 */           this.jj_la1[17] = this.jj_gen;
/*      */           break;
/*      */       } 
/*  710 */       jj_consume_token(73);
/*  711 */     } catch (Throwable jjte000) {
/*  712 */       if (jjtc000) {
/*  713 */         this.jjtree.clearNodeScope(jjtn000);
/*  714 */         jjtc000 = false;
/*      */       } else {
/*  716 */         this.jjtree.popNode();
/*      */       } 
/*  718 */       if (jjte000 instanceof RuntimeException) {
/*  719 */         throw (RuntimeException)jjte000;
/*      */       }
/*  721 */       if (jjte000 instanceof ParseException) {
/*  722 */         throw (ParseException)jjte000;
/*      */       }
/*  724 */       throw (Error)jjte000;
/*      */     } finally {
/*  726 */       if (jjtc000) {
/*  727 */         this.jjtree.closeNodeScope(jjtn000, true);
/*  728 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void FormalParameter() throws ParseException {
/*  735 */     BSHFormalParameter jjtn000 = new BSHFormalParameter(8);
/*  736 */     boolean jjtc000 = true;
/*  737 */     this.jjtree.openNodeScope(jjtn000);
/*  738 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/*  740 */       if (jj_2_5(2)) {
/*  741 */         Type();
/*  742 */         Token t = jj_consume_token(69);
/*  743 */         this.jjtree.closeNodeScope(jjtn000, true);
/*  744 */         jjtc000 = false;
/*  745 */         jjtreeCloseNodeScope(jjtn000);
/*  746 */         jjtn000.name = t.image;
/*      */       } else {
/*  748 */         Token t; switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 69:
/*  750 */             t = jj_consume_token(69);
/*  751 */             this.jjtree.closeNodeScope(jjtn000, true);
/*  752 */             jjtc000 = false;
/*  753 */             jjtreeCloseNodeScope(jjtn000);
/*  754 */             jjtn000.name = t.image;
/*      */             break;
/*      */           default:
/*  757 */             this.jj_la1[18] = this.jj_gen;
/*  758 */             jj_consume_token(-1);
/*  759 */             throw new ParseException();
/*      */         } 
/*      */       } 
/*  762 */     } catch (Throwable jjte000) {
/*  763 */       if (jjtc000) {
/*  764 */         this.jjtree.clearNodeScope(jjtn000);
/*  765 */         jjtc000 = false;
/*      */       } else {
/*  767 */         this.jjtree.popNode();
/*      */       } 
/*  769 */       if (jjte000 instanceof RuntimeException) {
/*  770 */         throw (RuntimeException)jjte000;
/*      */       }
/*  772 */       if (jjte000 instanceof ParseException) {
/*  773 */         throw (ParseException)jjte000;
/*      */       }
/*  775 */       throw (Error)jjte000;
/*      */     } finally {
/*  777 */       if (jjtc000) {
/*  778 */         this.jjtree.closeNodeScope(jjtn000, true);
/*  779 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void Type() throws ParseException {
/*  789 */     BSHType jjtn000 = new BSHType(9);
/*  790 */     boolean jjtc000 = true;
/*  791 */     this.jjtree.openNodeScope(jjtn000);
/*  792 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/*  794 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 11:
/*      */         case 14:
/*      */         case 17:
/*      */         case 22:
/*      */         case 29:
/*      */         case 36:
/*      */         case 38:
/*      */         case 47:
/*  803 */           PrimitiveType();
/*      */           break;
/*      */         case 69:
/*  806 */           AmbiguousName();
/*      */           break;
/*      */         default:
/*  809 */           this.jj_la1[19] = this.jj_gen;
/*  810 */           jj_consume_token(-1);
/*  811 */           throw new ParseException();
/*      */       } 
/*      */ 
/*      */       
/*  815 */       while (jj_2_6(2)) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  820 */         jj_consume_token(76);
/*  821 */         jj_consume_token(77);
/*  822 */         jjtn000.addArrayDimension();
/*      */       } 
/*  824 */     } catch (Throwable jjte000) {
/*  825 */       if (jjtc000) {
/*  826 */         this.jjtree.clearNodeScope(jjtn000);
/*  827 */         jjtc000 = false;
/*      */       } else {
/*  829 */         this.jjtree.popNode();
/*      */       } 
/*  831 */       if (jjte000 instanceof RuntimeException) {
/*  832 */         throw (RuntimeException)jjte000;
/*      */       }
/*  834 */       if (jjte000 instanceof ParseException) {
/*  835 */         throw (ParseException)jjte000;
/*      */       }
/*  837 */       throw (Error)jjte000;
/*      */     } finally {
/*  839 */       if (jjtc000) {
/*  840 */         this.jjtree.closeNodeScope(jjtn000, true);
/*  841 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void ReturnType() throws ParseException {
/*  851 */     BSHReturnType jjtn000 = new BSHReturnType(10);
/*  852 */     boolean jjtc000 = true;
/*  853 */     this.jjtree.openNodeScope(jjtn000);
/*  854 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/*  856 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 57:
/*  858 */           jj_consume_token(57);
/*  859 */           this.jjtree.closeNodeScope(jjtn000, true);
/*  860 */           jjtc000 = false;
/*  861 */           jjtreeCloseNodeScope(jjtn000);
/*  862 */           jjtn000.isVoid = true;
/*      */           break;
/*      */         case 11:
/*      */         case 14:
/*      */         case 17:
/*      */         case 22:
/*      */         case 29:
/*      */         case 36:
/*      */         case 38:
/*      */         case 47:
/*      */         case 69:
/*  873 */           Type();
/*      */           break;
/*      */         default:
/*  876 */           this.jj_la1[20] = this.jj_gen;
/*  877 */           jj_consume_token(-1);
/*  878 */           throw new ParseException();
/*      */       } 
/*  880 */     } catch (Throwable jjte000) {
/*  881 */       if (jjtc000) {
/*  882 */         this.jjtree.clearNodeScope(jjtn000);
/*  883 */         jjtc000 = false;
/*      */       } else {
/*  885 */         this.jjtree.popNode();
/*      */       } 
/*  887 */       if (jjte000 instanceof RuntimeException) {
/*  888 */         throw (RuntimeException)jjte000;
/*      */       }
/*  890 */       if (jjte000 instanceof ParseException) {
/*  891 */         throw (ParseException)jjte000;
/*      */       }
/*  893 */       throw (Error)jjte000;
/*      */     } finally {
/*  895 */       if (jjtc000) {
/*  896 */         this.jjtree.closeNodeScope(jjtn000, true);
/*  897 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void PrimitiveType() throws ParseException {
/*  904 */     BSHPrimitiveType jjtn000 = new BSHPrimitiveType(11);
/*  905 */     boolean jjtc000 = true;
/*  906 */     this.jjtree.openNodeScope(jjtn000);
/*  907 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/*  909 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 11:
/*  911 */           jj_consume_token(11);
/*  912 */           this.jjtree.closeNodeScope(jjtn000, true);
/*  913 */           jjtc000 = false;
/*  914 */           jjtreeCloseNodeScope(jjtn000);
/*  915 */           jjtn000.type = boolean.class;
/*      */           break;
/*      */         case 17:
/*  918 */           jj_consume_token(17);
/*  919 */           this.jjtree.closeNodeScope(jjtn000, true);
/*  920 */           jjtc000 = false;
/*  921 */           jjtreeCloseNodeScope(jjtn000);
/*  922 */           jjtn000.type = char.class;
/*      */           break;
/*      */         case 14:
/*  925 */           jj_consume_token(14);
/*  926 */           this.jjtree.closeNodeScope(jjtn000, true);
/*  927 */           jjtc000 = false;
/*  928 */           jjtreeCloseNodeScope(jjtn000);
/*  929 */           jjtn000.type = byte.class;
/*      */           break;
/*      */         case 47:
/*  932 */           jj_consume_token(47);
/*  933 */           this.jjtree.closeNodeScope(jjtn000, true);
/*  934 */           jjtc000 = false;
/*  935 */           jjtreeCloseNodeScope(jjtn000);
/*  936 */           jjtn000.type = short.class;
/*      */           break;
/*      */         case 36:
/*  939 */           jj_consume_token(36);
/*  940 */           this.jjtree.closeNodeScope(jjtn000, true);
/*  941 */           jjtc000 = false;
/*  942 */           jjtreeCloseNodeScope(jjtn000);
/*  943 */           jjtn000.type = int.class;
/*      */           break;
/*      */         case 38:
/*  946 */           jj_consume_token(38);
/*  947 */           this.jjtree.closeNodeScope(jjtn000, true);
/*  948 */           jjtc000 = false;
/*  949 */           jjtreeCloseNodeScope(jjtn000);
/*  950 */           jjtn000.type = long.class;
/*      */           break;
/*      */         case 29:
/*  953 */           jj_consume_token(29);
/*  954 */           this.jjtree.closeNodeScope(jjtn000, true);
/*  955 */           jjtc000 = false;
/*  956 */           jjtreeCloseNodeScope(jjtn000);
/*  957 */           jjtn000.type = float.class;
/*      */           break;
/*      */         case 22:
/*  960 */           jj_consume_token(22);
/*  961 */           this.jjtree.closeNodeScope(jjtn000, true);
/*  962 */           jjtc000 = false;
/*  963 */           jjtreeCloseNodeScope(jjtn000);
/*  964 */           jjtn000.type = double.class;
/*      */           break;
/*      */         default:
/*  967 */           this.jj_la1[21] = this.jj_gen;
/*  968 */           jj_consume_token(-1);
/*  969 */           throw new ParseException();
/*      */       } 
/*      */     } finally {
/*  972 */       if (jjtc000) {
/*  973 */         this.jjtree.closeNodeScope(jjtn000, true);
/*  974 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void AmbiguousName() throws ParseException {
/*  981 */     BSHAmbiguousName jjtn000 = new BSHAmbiguousName(12);
/*  982 */     boolean jjtc000 = true;
/*  983 */     this.jjtree.openNodeScope(jjtn000);
/*  984 */     jjtreeOpenNodeScope(jjtn000);
/*      */     
/*      */     try {
/*  987 */       Token t = jj_consume_token(69);
/*  988 */       StringBuffer s = new StringBuffer(t.image);
/*      */ 
/*      */       
/*  991 */       while (jj_2_7(2)) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  996 */         jj_consume_token(80);
/*  997 */         t = jj_consume_token(69);
/*  998 */         s.append("." + t.image);
/*      */       } 
/* 1000 */       this.jjtree.closeNodeScope(jjtn000, true);
/* 1001 */       jjtc000 = false;
/* 1002 */       jjtreeCloseNodeScope(jjtn000);
/* 1003 */       jjtn000.text = s.toString();
/*      */     } finally {
/* 1005 */       if (jjtc000) {
/* 1006 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 1007 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final int NameList() throws ParseException {
/* 1013 */     int count = 0;
/* 1014 */     AmbiguousName();
/* 1015 */     count++;
/*      */     
/*      */     while (true) {
/* 1018 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 79:
/*      */           break;
/*      */         
/*      */         default:
/* 1023 */           this.jj_la1[22] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 1026 */       jj_consume_token(79);
/* 1027 */       AmbiguousName();
/* 1028 */       count++;
/*      */     } 
/* 1030 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void Expression() throws ParseException {
/* 1038 */     if (jj_2_8(2147483647)) {
/* 1039 */       Assignment();
/*      */     } else {
/* 1041 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 11:
/*      */         case 14:
/*      */         case 17:
/*      */         case 22:
/*      */         case 26:
/*      */         case 29:
/*      */         case 36:
/*      */         case 38:
/*      */         case 40:
/*      */         case 41:
/*      */         case 47:
/*      */         case 55:
/*      */         case 57:
/*      */         case 60:
/*      */         case 64:
/*      */         case 66:
/*      */         case 67:
/*      */         case 69:
/*      */         case 72:
/*      */         case 86:
/*      */         case 87:
/*      */         case 100:
/*      */         case 101:
/*      */         case 102:
/*      */         case 103:
/* 1067 */           ConditionalExpression();
/*      */           return;
/*      */       } 
/* 1070 */       this.jj_la1[23] = this.jj_gen;
/* 1071 */       jj_consume_token(-1);
/* 1072 */       throw new ParseException();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void Assignment() throws ParseException {
/* 1079 */     BSHAssignment jjtn000 = new BSHAssignment(13);
/* 1080 */     boolean jjtc000 = true;
/* 1081 */     this.jjtree.openNodeScope(jjtn000);
/* 1082 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/* 1084 */       PrimaryExpression();
/* 1085 */       int op = AssignmentOperator();
/* 1086 */       jjtn000.operator = op;
/* 1087 */       Expression();
/* 1088 */     } catch (Throwable jjte000) {
/* 1089 */       if (jjtc000) {
/* 1090 */         this.jjtree.clearNodeScope(jjtn000);
/* 1091 */         jjtc000 = false;
/*      */       } else {
/* 1093 */         this.jjtree.popNode();
/*      */       } 
/* 1095 */       if (jjte000 instanceof RuntimeException) {
/* 1096 */         throw (RuntimeException)jjte000;
/*      */       }
/* 1098 */       if (jjte000 instanceof ParseException) {
/* 1099 */         throw (ParseException)jjte000;
/*      */       }
/* 1101 */       throw (Error)jjte000;
/*      */     } finally {
/* 1103 */       if (jjtc000) {
/* 1104 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 1105 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final int AssignmentOperator() throws ParseException {
/*      */     Token t;
/* 1112 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 81:
/* 1114 */         jj_consume_token(81);
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
/* 1163 */         t = getToken(0);
/* 1164 */         return t.kind;case 120: jj_consume_token(120); t = getToken(0); return t.kind;case 121: jj_consume_token(121); t = getToken(0); return t.kind;case 127: jj_consume_token(127); t = getToken(0); return t.kind;case 118: jj_consume_token(118); t = getToken(0); return t.kind;case 119: jj_consume_token(119); t = getToken(0); return t.kind;case 122: jj_consume_token(122); t = getToken(0); return t.kind;case 126: jj_consume_token(126); t = getToken(0); return t.kind;case 124: jj_consume_token(124); t = getToken(0); return t.kind;case 128: jj_consume_token(128); t = getToken(0); return t.kind;case 129: jj_consume_token(129); t = getToken(0); return t.kind;case 130: jj_consume_token(130); t = getToken(0); return t.kind;case 131: jj_consume_token(131); t = getToken(0); return t.kind;case 132: jj_consume_token(132); t = getToken(0); return t.kind;case 133: jj_consume_token(133); t = getToken(0); return t.kind;
/*      */     }  this.jj_la1[24] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException(); } public final void ConditionalExpression() throws ParseException { BSHTernaryExpression jjtn001;
/*      */     boolean jjtc001;
/* 1169 */     ConditionalOrExpression();
/* 1170 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 88:
/* 1172 */         jj_consume_token(88);
/* 1173 */         Expression();
/* 1174 */         jj_consume_token(89);
/* 1175 */         jjtn001 = new BSHTernaryExpression(14);
/* 1176 */         jjtc001 = true;
/* 1177 */         this.jjtree.openNodeScope(jjtn001);
/* 1178 */         jjtreeOpenNodeScope(jjtn001);
/*      */         try {
/* 1180 */           ConditionalExpression();
/* 1181 */         } catch (Throwable jjte001) {
/* 1182 */           if (jjtc001) {
/* 1183 */             this.jjtree.clearNodeScope(jjtn001);
/* 1184 */             jjtc001 = false;
/*      */           } else {
/* 1186 */             this.jjtree.popNode();
/*      */           } 
/* 1188 */           if (jjte001 instanceof RuntimeException) {
/* 1189 */             throw (RuntimeException)jjte001;
/*      */           }
/* 1191 */           if (jjte001 instanceof ParseException) {
/* 1192 */             throw (ParseException)jjte001;
/*      */           }
/* 1194 */           throw (Error)jjte001;
/*      */         } finally {
/* 1196 */           if (jjtc001) {
/* 1197 */             this.jjtree.closeNodeScope(jjtn001, 3);
/* 1198 */             jjtreeCloseNodeScope(jjtn001);
/*      */           } 
/*      */         } 
/*      */         return;
/*      */     } 
/* 1203 */     this.jj_la1[25] = this.jj_gen; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void ConditionalOrExpression() throws ParseException {
/* 1209 */     Token t = null;
/* 1210 */     ConditionalAndExpression();
/*      */     
/*      */     while (true) {
/* 1213 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 96:
/*      */         case 97:
/*      */           break;
/*      */         
/*      */         default:
/* 1219 */           this.jj_la1[26] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 1222 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 96:
/* 1224 */           t = jj_consume_token(96);
/*      */           break;
/*      */         case 97:
/* 1227 */           t = jj_consume_token(97);
/*      */           break;
/*      */         default:
/* 1230 */           this.jj_la1[27] = this.jj_gen;
/* 1231 */           jj_consume_token(-1);
/* 1232 */           throw new ParseException();
/*      */       } 
/* 1234 */       ConditionalAndExpression();
/* 1235 */       BSHBinaryExpression jjtn001 = new BSHBinaryExpression(15);
/* 1236 */       boolean jjtc001 = true;
/* 1237 */       this.jjtree.openNodeScope(jjtn001);
/* 1238 */       jjtreeOpenNodeScope(jjtn001);
/*      */       try {
/* 1240 */         this.jjtree.closeNodeScope(jjtn001, 2);
/* 1241 */         jjtc001 = false;
/* 1242 */         jjtreeCloseNodeScope(jjtn001);
/* 1243 */         jjtn001.kind = t.kind;
/*      */       } finally {
/* 1245 */         if (jjtc001) {
/* 1246 */           this.jjtree.closeNodeScope(jjtn001, 2);
/* 1247 */           jjtreeCloseNodeScope(jjtn001);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void ConditionalAndExpression() throws ParseException {
/* 1254 */     Token t = null;
/* 1255 */     InclusiveOrExpression();
/*      */     
/*      */     while (true) {
/* 1258 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 98:
/*      */         case 99:
/*      */           break;
/*      */         
/*      */         default:
/* 1264 */           this.jj_la1[28] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 1267 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 98:
/* 1269 */           t = jj_consume_token(98);
/*      */           break;
/*      */         case 99:
/* 1272 */           t = jj_consume_token(99);
/*      */           break;
/*      */         default:
/* 1275 */           this.jj_la1[29] = this.jj_gen;
/* 1276 */           jj_consume_token(-1);
/* 1277 */           throw new ParseException();
/*      */       } 
/* 1279 */       InclusiveOrExpression();
/* 1280 */       BSHBinaryExpression jjtn001 = new BSHBinaryExpression(15);
/* 1281 */       boolean jjtc001 = true;
/* 1282 */       this.jjtree.openNodeScope(jjtn001);
/* 1283 */       jjtreeOpenNodeScope(jjtn001);
/*      */       try {
/* 1285 */         this.jjtree.closeNodeScope(jjtn001, 2);
/* 1286 */         jjtc001 = false;
/* 1287 */         jjtreeCloseNodeScope(jjtn001);
/* 1288 */         jjtn001.kind = t.kind;
/*      */       } finally {
/* 1290 */         if (jjtc001) {
/* 1291 */           this.jjtree.closeNodeScope(jjtn001, 2);
/* 1292 */           jjtreeCloseNodeScope(jjtn001);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void InclusiveOrExpression() throws ParseException {
/* 1299 */     Token t = null;
/* 1300 */     ExclusiveOrExpression();
/*      */     
/*      */     while (true) {
/* 1303 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 108:
/*      */         case 109:
/*      */           break;
/*      */         
/*      */         default:
/* 1309 */           this.jj_la1[30] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 1312 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 108:
/* 1314 */           t = jj_consume_token(108);
/*      */           break;
/*      */         case 109:
/* 1317 */           t = jj_consume_token(109);
/*      */           break;
/*      */         default:
/* 1320 */           this.jj_la1[31] = this.jj_gen;
/* 1321 */           jj_consume_token(-1);
/* 1322 */           throw new ParseException();
/*      */       } 
/* 1324 */       ExclusiveOrExpression();
/* 1325 */       BSHBinaryExpression jjtn001 = new BSHBinaryExpression(15);
/* 1326 */       boolean jjtc001 = true;
/* 1327 */       this.jjtree.openNodeScope(jjtn001);
/* 1328 */       jjtreeOpenNodeScope(jjtn001);
/*      */       try {
/* 1330 */         this.jjtree.closeNodeScope(jjtn001, 2);
/* 1331 */         jjtc001 = false;
/* 1332 */         jjtreeCloseNodeScope(jjtn001);
/* 1333 */         jjtn001.kind = t.kind;
/*      */       } finally {
/* 1335 */         if (jjtc001) {
/* 1336 */           this.jjtree.closeNodeScope(jjtn001, 2);
/* 1337 */           jjtreeCloseNodeScope(jjtn001);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void ExclusiveOrExpression() throws ParseException {
/* 1344 */     Token t = null;
/* 1345 */     AndExpression();
/*      */     
/*      */     while (true) {
/* 1348 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 110:
/*      */           break;
/*      */         
/*      */         default:
/* 1353 */           this.jj_la1[32] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 1356 */       t = jj_consume_token(110);
/* 1357 */       AndExpression();
/* 1358 */       BSHBinaryExpression jjtn001 = new BSHBinaryExpression(15);
/* 1359 */       boolean jjtc001 = true;
/* 1360 */       this.jjtree.openNodeScope(jjtn001);
/* 1361 */       jjtreeOpenNodeScope(jjtn001);
/*      */       try {
/* 1363 */         this.jjtree.closeNodeScope(jjtn001, 2);
/* 1364 */         jjtc001 = false;
/* 1365 */         jjtreeCloseNodeScope(jjtn001);
/* 1366 */         jjtn001.kind = t.kind;
/*      */       } finally {
/* 1368 */         if (jjtc001) {
/* 1369 */           this.jjtree.closeNodeScope(jjtn001, 2);
/* 1370 */           jjtreeCloseNodeScope(jjtn001);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void AndExpression() throws ParseException {
/* 1377 */     Token t = null;
/* 1378 */     EqualityExpression();
/*      */     
/*      */     while (true) {
/* 1381 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 106:
/*      */         case 107:
/*      */           break;
/*      */         
/*      */         default:
/* 1387 */           this.jj_la1[33] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 1390 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 106:
/* 1392 */           t = jj_consume_token(106);
/*      */           break;
/*      */         case 107:
/* 1395 */           t = jj_consume_token(107);
/*      */           break;
/*      */         default:
/* 1398 */           this.jj_la1[34] = this.jj_gen;
/* 1399 */           jj_consume_token(-1);
/* 1400 */           throw new ParseException();
/*      */       } 
/* 1402 */       EqualityExpression();
/* 1403 */       BSHBinaryExpression jjtn001 = new BSHBinaryExpression(15);
/* 1404 */       boolean jjtc001 = true;
/* 1405 */       this.jjtree.openNodeScope(jjtn001);
/* 1406 */       jjtreeOpenNodeScope(jjtn001);
/*      */       try {
/* 1408 */         this.jjtree.closeNodeScope(jjtn001, 2);
/* 1409 */         jjtc001 = false;
/* 1410 */         jjtreeCloseNodeScope(jjtn001);
/* 1411 */         jjtn001.kind = t.kind;
/*      */       } finally {
/* 1413 */         if (jjtc001) {
/* 1414 */           this.jjtree.closeNodeScope(jjtn001, 2);
/* 1415 */           jjtreeCloseNodeScope(jjtn001);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void EqualityExpression() throws ParseException {
/* 1422 */     Token t = null;
/* 1423 */     InstanceOfExpression();
/*      */     
/*      */     while (true) {
/* 1426 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 90:
/*      */         case 95:
/*      */           break;
/*      */         
/*      */         default:
/* 1432 */           this.jj_la1[35] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 1435 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 90:
/* 1437 */           t = jj_consume_token(90);
/*      */           break;
/*      */         case 95:
/* 1440 */           t = jj_consume_token(95);
/*      */           break;
/*      */         default:
/* 1443 */           this.jj_la1[36] = this.jj_gen;
/* 1444 */           jj_consume_token(-1);
/* 1445 */           throw new ParseException();
/*      */       } 
/* 1447 */       InstanceOfExpression();
/* 1448 */       BSHBinaryExpression jjtn001 = new BSHBinaryExpression(15);
/* 1449 */       boolean jjtc001 = true;
/* 1450 */       this.jjtree.openNodeScope(jjtn001);
/* 1451 */       jjtreeOpenNodeScope(jjtn001);
/*      */       try {
/* 1453 */         this.jjtree.closeNodeScope(jjtn001, 2);
/* 1454 */         jjtc001 = false;
/* 1455 */         jjtreeCloseNodeScope(jjtn001);
/* 1456 */         jjtn001.kind = t.kind;
/*      */       } finally {
/* 1458 */         if (jjtc001) {
/* 1459 */           this.jjtree.closeNodeScope(jjtn001, 2);
/* 1460 */           jjtreeCloseNodeScope(jjtn001);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   } public final void InstanceOfExpression() throws ParseException {
/*      */     BSHBinaryExpression jjtn001;
/*      */     boolean jjtc001;
/* 1467 */     Token t = null;
/* 1468 */     RelationalExpression();
/* 1469 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 35:
/* 1471 */         t = jj_consume_token(35);
/* 1472 */         Type();
/* 1473 */         jjtn001 = new BSHBinaryExpression(15);
/* 1474 */         jjtc001 = true;
/* 1475 */         this.jjtree.openNodeScope(jjtn001);
/* 1476 */         jjtreeOpenNodeScope(jjtn001);
/*      */         try {
/* 1478 */           this.jjtree.closeNodeScope(jjtn001, 2);
/* 1479 */           jjtc001 = false;
/* 1480 */           jjtreeCloseNodeScope(jjtn001);
/* 1481 */           jjtn001.kind = t.kind;
/*      */         } finally {
/* 1483 */           if (jjtc001) {
/* 1484 */             this.jjtree.closeNodeScope(jjtn001, 2);
/* 1485 */             jjtreeCloseNodeScope(jjtn001);
/*      */           } 
/*      */         } 
/*      */         return;
/*      */     } 
/* 1490 */     this.jj_la1[37] = this.jj_gen;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void RelationalExpression() throws ParseException {
/* 1496 */     Token t = null;
/* 1497 */     ShiftExpression();
/*      */     
/*      */     while (true) {
/* 1500 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 82:
/*      */         case 83:
/*      */         case 84:
/*      */         case 85:
/*      */         case 91:
/*      */         case 92:
/*      */         case 93:
/*      */         case 94:
/*      */           break;
/*      */         
/*      */         default:
/* 1512 */           this.jj_la1[38] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 1515 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 84:
/* 1517 */           t = jj_consume_token(84);
/*      */           break;
/*      */         case 85:
/* 1520 */           t = jj_consume_token(85);
/*      */           break;
/*      */         case 82:
/* 1523 */           t = jj_consume_token(82);
/*      */           break;
/*      */         case 83:
/* 1526 */           t = jj_consume_token(83);
/*      */           break;
/*      */         case 91:
/* 1529 */           t = jj_consume_token(91);
/*      */           break;
/*      */         case 92:
/* 1532 */           t = jj_consume_token(92);
/*      */           break;
/*      */         case 93:
/* 1535 */           t = jj_consume_token(93);
/*      */           break;
/*      */         case 94:
/* 1538 */           t = jj_consume_token(94);
/*      */           break;
/*      */         default:
/* 1541 */           this.jj_la1[39] = this.jj_gen;
/* 1542 */           jj_consume_token(-1);
/* 1543 */           throw new ParseException();
/*      */       } 
/* 1545 */       ShiftExpression();
/* 1546 */       BSHBinaryExpression jjtn001 = new BSHBinaryExpression(15);
/* 1547 */       boolean jjtc001 = true;
/* 1548 */       this.jjtree.openNodeScope(jjtn001);
/* 1549 */       jjtreeOpenNodeScope(jjtn001);
/*      */       try {
/* 1551 */         this.jjtree.closeNodeScope(jjtn001, 2);
/* 1552 */         jjtc001 = false;
/* 1553 */         jjtreeCloseNodeScope(jjtn001);
/* 1554 */         jjtn001.kind = t.kind;
/*      */       } finally {
/* 1556 */         if (jjtc001) {
/* 1557 */           this.jjtree.closeNodeScope(jjtn001, 2);
/* 1558 */           jjtreeCloseNodeScope(jjtn001);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void ShiftExpression() throws ParseException {
/* 1565 */     Token t = null;
/* 1566 */     AdditiveExpression();
/*      */     
/*      */     while (true) {
/* 1569 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 112:
/*      */         case 113:
/*      */         case 114:
/*      */         case 115:
/*      */         case 116:
/*      */         case 117:
/*      */           break;
/*      */         
/*      */         default:
/* 1579 */           this.jj_la1[40] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 1582 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 112:
/* 1584 */           t = jj_consume_token(112);
/*      */           break;
/*      */         case 113:
/* 1587 */           t = jj_consume_token(113);
/*      */           break;
/*      */         case 114:
/* 1590 */           t = jj_consume_token(114);
/*      */           break;
/*      */         case 115:
/* 1593 */           t = jj_consume_token(115);
/*      */           break;
/*      */         case 116:
/* 1596 */           t = jj_consume_token(116);
/*      */           break;
/*      */         case 117:
/* 1599 */           t = jj_consume_token(117);
/*      */           break;
/*      */         default:
/* 1602 */           this.jj_la1[41] = this.jj_gen;
/* 1603 */           jj_consume_token(-1);
/* 1604 */           throw new ParseException();
/*      */       } 
/* 1606 */       AdditiveExpression();
/* 1607 */       BSHBinaryExpression jjtn001 = new BSHBinaryExpression(15);
/* 1608 */       boolean jjtc001 = true;
/* 1609 */       this.jjtree.openNodeScope(jjtn001);
/* 1610 */       jjtreeOpenNodeScope(jjtn001);
/*      */       try {
/* 1612 */         this.jjtree.closeNodeScope(jjtn001, 2);
/* 1613 */         jjtc001 = false;
/* 1614 */         jjtreeCloseNodeScope(jjtn001);
/* 1615 */         jjtn001.kind = t.kind;
/*      */       } finally {
/* 1617 */         if (jjtc001) {
/* 1618 */           this.jjtree.closeNodeScope(jjtn001, 2);
/* 1619 */           jjtreeCloseNodeScope(jjtn001);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void AdditiveExpression() throws ParseException {
/* 1626 */     Token t = null;
/* 1627 */     MultiplicativeExpression();
/*      */     
/*      */     while (true) {
/* 1630 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 102:
/*      */         case 103:
/*      */           break;
/*      */         
/*      */         default:
/* 1636 */           this.jj_la1[42] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 1639 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 102:
/* 1641 */           t = jj_consume_token(102);
/*      */           break;
/*      */         case 103:
/* 1644 */           t = jj_consume_token(103);
/*      */           break;
/*      */         default:
/* 1647 */           this.jj_la1[43] = this.jj_gen;
/* 1648 */           jj_consume_token(-1);
/* 1649 */           throw new ParseException();
/*      */       } 
/* 1651 */       MultiplicativeExpression();
/* 1652 */       BSHBinaryExpression jjtn001 = new BSHBinaryExpression(15);
/* 1653 */       boolean jjtc001 = true;
/* 1654 */       this.jjtree.openNodeScope(jjtn001);
/* 1655 */       jjtreeOpenNodeScope(jjtn001);
/*      */       try {
/* 1657 */         this.jjtree.closeNodeScope(jjtn001, 2);
/* 1658 */         jjtc001 = false;
/* 1659 */         jjtreeCloseNodeScope(jjtn001);
/* 1660 */         jjtn001.kind = t.kind;
/*      */       } finally {
/* 1662 */         if (jjtc001) {
/* 1663 */           this.jjtree.closeNodeScope(jjtn001, 2);
/* 1664 */           jjtreeCloseNodeScope(jjtn001);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void MultiplicativeExpression() throws ParseException {
/* 1671 */     Token t = null;
/* 1672 */     UnaryExpression();
/*      */     
/*      */     while (true) {
/* 1675 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 104:
/*      */         case 105:
/*      */         case 111:
/*      */           break;
/*      */         
/*      */         default:
/* 1682 */           this.jj_la1[44] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 1685 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 104:
/* 1687 */           t = jj_consume_token(104);
/*      */           break;
/*      */         case 105:
/* 1690 */           t = jj_consume_token(105);
/*      */           break;
/*      */         case 111:
/* 1693 */           t = jj_consume_token(111);
/*      */           break;
/*      */         default:
/* 1696 */           this.jj_la1[45] = this.jj_gen;
/* 1697 */           jj_consume_token(-1);
/* 1698 */           throw new ParseException();
/*      */       } 
/* 1700 */       UnaryExpression();
/* 1701 */       BSHBinaryExpression jjtn001 = new BSHBinaryExpression(15);
/* 1702 */       boolean jjtc001 = true;
/* 1703 */       this.jjtree.openNodeScope(jjtn001);
/* 1704 */       jjtreeOpenNodeScope(jjtn001);
/*      */       try {
/* 1706 */         this.jjtree.closeNodeScope(jjtn001, 2);
/* 1707 */         jjtc001 = false;
/* 1708 */         jjtreeCloseNodeScope(jjtn001);
/* 1709 */         jjtn001.kind = t.kind;
/*      */       } finally {
/* 1711 */         if (jjtc001) {
/* 1712 */           this.jjtree.closeNodeScope(jjtn001, 2);
/* 1713 */           jjtreeCloseNodeScope(jjtn001);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   } public final void UnaryExpression() throws ParseException {
/*      */     BSHUnaryExpression jjtn001;
/*      */     boolean jjtc001;
/* 1720 */     Token t = null;
/* 1721 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 102:
/*      */       case 103:
/* 1724 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 102:
/* 1726 */             t = jj_consume_token(102);
/*      */             break;
/*      */           case 103:
/* 1729 */             t = jj_consume_token(103);
/*      */             break;
/*      */           default:
/* 1732 */             this.jj_la1[46] = this.jj_gen;
/* 1733 */             jj_consume_token(-1);
/* 1734 */             throw new ParseException();
/*      */         } 
/* 1736 */         UnaryExpression();
/* 1737 */         jjtn001 = new BSHUnaryExpression(16);
/* 1738 */         jjtc001 = true;
/* 1739 */         this.jjtree.openNodeScope(jjtn001);
/* 1740 */         jjtreeOpenNodeScope(jjtn001);
/*      */         try {
/* 1742 */           this.jjtree.closeNodeScope(jjtn001, 1);
/* 1743 */           jjtc001 = false;
/* 1744 */           jjtreeCloseNodeScope(jjtn001);
/* 1745 */           jjtn001.kind = t.kind;
/*      */         } finally {
/* 1747 */           if (jjtc001) {
/* 1748 */             this.jjtree.closeNodeScope(jjtn001, 1);
/* 1749 */             jjtreeCloseNodeScope(jjtn001);
/*      */           } 
/*      */         } 
/*      */         return;
/*      */       case 100:
/* 1754 */         PreIncrementExpression();
/*      */         return;
/*      */       case 101:
/* 1757 */         PreDecrementExpression();
/*      */         return;
/*      */       case 11:
/*      */       case 14:
/*      */       case 17:
/*      */       case 22:
/*      */       case 26:
/*      */       case 29:
/*      */       case 36:
/*      */       case 38:
/*      */       case 40:
/*      */       case 41:
/*      */       case 47:
/*      */       case 55:
/*      */       case 57:
/*      */       case 60:
/*      */       case 64:
/*      */       case 66:
/*      */       case 67:
/*      */       case 69:
/*      */       case 72:
/*      */       case 86:
/*      */       case 87:
/* 1780 */         UnaryExpressionNotPlusMinus();
/*      */         return;
/*      */     } 
/* 1783 */     this.jj_la1[47] = this.jj_gen;
/* 1784 */     jj_consume_token(-1);
/* 1785 */     throw new ParseException();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void PreIncrementExpression() throws ParseException {
/* 1790 */     Token t = null;
/* 1791 */     t = jj_consume_token(100);
/* 1792 */     PrimaryExpression();
/* 1793 */     BSHUnaryExpression jjtn001 = new BSHUnaryExpression(16);
/* 1794 */     boolean jjtc001 = true;
/* 1795 */     this.jjtree.openNodeScope(jjtn001);
/* 1796 */     jjtreeOpenNodeScope(jjtn001);
/*      */     try {
/* 1798 */       this.jjtree.closeNodeScope(jjtn001, 1);
/* 1799 */       jjtc001 = false;
/* 1800 */       jjtreeCloseNodeScope(jjtn001);
/* 1801 */       jjtn001.kind = t.kind;
/*      */     } finally {
/* 1803 */       if (jjtc001) {
/* 1804 */         this.jjtree.closeNodeScope(jjtn001, 1);
/* 1805 */         jjtreeCloseNodeScope(jjtn001);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void PreDecrementExpression() throws ParseException {
/* 1811 */     Token t = null;
/* 1812 */     t = jj_consume_token(101);
/* 1813 */     PrimaryExpression();
/* 1814 */     BSHUnaryExpression jjtn001 = new BSHUnaryExpression(16);
/* 1815 */     boolean jjtc001 = true;
/* 1816 */     this.jjtree.openNodeScope(jjtn001);
/* 1817 */     jjtreeOpenNodeScope(jjtn001);
/*      */     try {
/* 1819 */       this.jjtree.closeNodeScope(jjtn001, 1);
/* 1820 */       jjtc001 = false;
/* 1821 */       jjtreeCloseNodeScope(jjtn001);
/* 1822 */       jjtn001.kind = t.kind;
/*      */     } finally {
/* 1824 */       if (jjtc001) {
/* 1825 */         this.jjtree.closeNodeScope(jjtn001, 1);
/* 1826 */         jjtreeCloseNodeScope(jjtn001);
/*      */       } 
/*      */     } 
/*      */   } public final void UnaryExpressionNotPlusMinus() throws ParseException {
/*      */     BSHUnaryExpression jjtn001;
/*      */     boolean jjtc001;
/* 1832 */     Token t = null;
/* 1833 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 86:
/*      */       case 87:
/* 1836 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 87:
/* 1838 */             t = jj_consume_token(87);
/*      */             break;
/*      */           case 86:
/* 1841 */             t = jj_consume_token(86);
/*      */             break;
/*      */           default:
/* 1844 */             this.jj_la1[48] = this.jj_gen;
/* 1845 */             jj_consume_token(-1);
/* 1846 */             throw new ParseException();
/*      */         } 
/* 1848 */         UnaryExpression();
/* 1849 */         jjtn001 = new BSHUnaryExpression(16);
/* 1850 */         jjtc001 = true;
/* 1851 */         this.jjtree.openNodeScope(jjtn001);
/* 1852 */         jjtreeOpenNodeScope(jjtn001);
/*      */         try {
/* 1854 */           this.jjtree.closeNodeScope(jjtn001, 1);
/* 1855 */           jjtc001 = false;
/* 1856 */           jjtreeCloseNodeScope(jjtn001);
/* 1857 */           jjtn001.kind = t.kind;
/*      */         } finally {
/* 1859 */           if (jjtc001) {
/* 1860 */             this.jjtree.closeNodeScope(jjtn001, 1);
/* 1861 */             jjtreeCloseNodeScope(jjtn001);
/*      */           } 
/*      */         } 
/*      */         return;
/*      */     } 
/* 1866 */     this.jj_la1[49] = this.jj_gen;
/* 1867 */     if (jj_2_9(2147483647)) {
/* 1868 */       CastExpression();
/*      */     } else {
/* 1870 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 11:
/*      */         case 14:
/*      */         case 17:
/*      */         case 22:
/*      */         case 26:
/*      */         case 29:
/*      */         case 36:
/*      */         case 38:
/*      */         case 40:
/*      */         case 41:
/*      */         case 47:
/*      */         case 55:
/*      */         case 57:
/*      */         case 60:
/*      */         case 64:
/*      */         case 66:
/*      */         case 67:
/*      */         case 69:
/*      */         case 72:
/* 1890 */           PostfixExpression();
/*      */           return;
/*      */       } 
/* 1893 */       this.jj_la1[50] = this.jj_gen;
/* 1894 */       jj_consume_token(-1);
/* 1895 */       throw new ParseException();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void CastLookahead() throws ParseException {
/* 1903 */     if (jj_2_10(2)) {
/* 1904 */       jj_consume_token(72);
/* 1905 */       PrimitiveType();
/* 1906 */     } else if (jj_2_11(2147483647)) {
/* 1907 */       jj_consume_token(72);
/* 1908 */       AmbiguousName();
/* 1909 */       jj_consume_token(76);
/* 1910 */       jj_consume_token(77);
/*      */     } else {
/* 1912 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 72:
/* 1914 */           jj_consume_token(72);
/* 1915 */           AmbiguousName();
/* 1916 */           jj_consume_token(73);
/* 1917 */           switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */             case 87:
/* 1919 */               jj_consume_token(87);
/*      */               return;
/*      */             case 86:
/* 1922 */               jj_consume_token(86);
/*      */               return;
/*      */             case 72:
/* 1925 */               jj_consume_token(72);
/*      */               return;
/*      */             case 69:
/* 1928 */               jj_consume_token(69);
/*      */               return;
/*      */             case 40:
/* 1931 */               jj_consume_token(40);
/*      */               return;
/*      */             case 26:
/*      */             case 41:
/*      */             case 55:
/*      */             case 57:
/*      */             case 60:
/*      */             case 64:
/*      */             case 66:
/*      */             case 67:
/* 1941 */               Literal();
/*      */               return;
/*      */           } 
/* 1944 */           this.jj_la1[51] = this.jj_gen;
/* 1945 */           jj_consume_token(-1);
/* 1946 */           throw new ParseException();
/*      */       } 
/*      */ 
/*      */       
/* 1950 */       this.jj_la1[52] = this.jj_gen;
/* 1951 */       jj_consume_token(-1);
/* 1952 */       throw new ParseException();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void PostfixExpression() throws ParseException {
/* 1958 */     Token t = null;
/* 1959 */     if (jj_2_12(2147483647)) {
/* 1960 */       PrimaryExpression();
/* 1961 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 100:
/* 1963 */           t = jj_consume_token(100);
/*      */           break;
/*      */         case 101:
/* 1966 */           t = jj_consume_token(101);
/*      */           break;
/*      */         default:
/* 1969 */           this.jj_la1[53] = this.jj_gen;
/* 1970 */           jj_consume_token(-1);
/* 1971 */           throw new ParseException();
/*      */       } 
/* 1973 */       BSHUnaryExpression jjtn001 = new BSHUnaryExpression(16);
/* 1974 */       boolean jjtc001 = true;
/* 1975 */       this.jjtree.openNodeScope(jjtn001);
/* 1976 */       jjtreeOpenNodeScope(jjtn001);
/*      */       try {
/* 1978 */         this.jjtree.closeNodeScope(jjtn001, 1);
/* 1979 */         jjtc001 = false;
/* 1980 */         jjtreeCloseNodeScope(jjtn001);
/* 1981 */         jjtn001.kind = t.kind; jjtn001.postfix = true;
/*      */       } finally {
/* 1983 */         if (jjtc001) {
/* 1984 */           this.jjtree.closeNodeScope(jjtn001, 1);
/* 1985 */           jjtreeCloseNodeScope(jjtn001);
/*      */         } 
/*      */       } 
/*      */     } else {
/* 1989 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 11:
/*      */         case 14:
/*      */         case 17:
/*      */         case 22:
/*      */         case 26:
/*      */         case 29:
/*      */         case 36:
/*      */         case 38:
/*      */         case 40:
/*      */         case 41:
/*      */         case 47:
/*      */         case 55:
/*      */         case 57:
/*      */         case 60:
/*      */         case 64:
/*      */         case 66:
/*      */         case 67:
/*      */         case 69:
/*      */         case 72:
/* 2009 */           PrimaryExpression();
/*      */           return;
/*      */       } 
/* 2012 */       this.jj_la1[54] = this.jj_gen;
/* 2013 */       jj_consume_token(-1);
/* 2014 */       throw new ParseException();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void CastExpression() throws ParseException {
/* 2021 */     BSHCastExpression jjtn000 = new BSHCastExpression(17);
/* 2022 */     boolean jjtc000 = true;
/* 2023 */     this.jjtree.openNodeScope(jjtn000);
/* 2024 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/* 2026 */       if (jj_2_13(2147483647)) {
/* 2027 */         jj_consume_token(72);
/* 2028 */         Type();
/* 2029 */         jj_consume_token(73);
/* 2030 */         UnaryExpression();
/*      */       } else {
/* 2032 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 72:
/* 2034 */             jj_consume_token(72);
/* 2035 */             Type();
/* 2036 */             jj_consume_token(73);
/* 2037 */             UnaryExpressionNotPlusMinus();
/*      */             break;
/*      */           default:
/* 2040 */             this.jj_la1[55] = this.jj_gen;
/* 2041 */             jj_consume_token(-1);
/* 2042 */             throw new ParseException();
/*      */         } 
/*      */       } 
/* 2045 */     } catch (Throwable jjte000) {
/* 2046 */       if (jjtc000) {
/* 2047 */         this.jjtree.clearNodeScope(jjtn000);
/* 2048 */         jjtc000 = false;
/*      */       } else {
/* 2050 */         this.jjtree.popNode();
/*      */       } 
/* 2052 */       if (jjte000 instanceof RuntimeException) {
/* 2053 */         throw (RuntimeException)jjte000;
/*      */       }
/* 2055 */       if (jjte000 instanceof ParseException) {
/* 2056 */         throw (ParseException)jjte000;
/*      */       }
/* 2058 */       throw (Error)jjte000;
/*      */     } finally {
/* 2060 */       if (jjtc000) {
/* 2061 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 2062 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void PrimaryExpression() throws ParseException {
/* 2069 */     BSHPrimaryExpression jjtn000 = new BSHPrimaryExpression(18);
/* 2070 */     boolean jjtc000 = true;
/* 2071 */     this.jjtree.openNodeScope(jjtn000);
/* 2072 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/* 2074 */       PrimaryPrefix();
/*      */       
/*      */       while (true) {
/* 2077 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 74:
/*      */           case 76:
/*      */           case 80:
/*      */             break;
/*      */           
/*      */           default:
/* 2084 */             this.jj_la1[56] = this.jj_gen;
/*      */             break;
/*      */         } 
/* 2087 */         PrimarySuffix();
/*      */       } 
/* 2089 */     } catch (Throwable jjte000) {
/* 2090 */       if (jjtc000) {
/* 2091 */         this.jjtree.clearNodeScope(jjtn000);
/* 2092 */         jjtc000 = false;
/*      */       } else {
/* 2094 */         this.jjtree.popNode();
/*      */       } 
/* 2096 */       if (jjte000 instanceof RuntimeException) {
/* 2097 */         throw (RuntimeException)jjte000;
/*      */       }
/* 2099 */       if (jjte000 instanceof ParseException) {
/* 2100 */         throw (ParseException)jjte000;
/*      */       }
/* 2102 */       throw (Error)jjte000;
/*      */     } finally {
/* 2104 */       if (jjtc000) {
/* 2105 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 2106 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void MethodInvocation() throws ParseException {
/* 2113 */     BSHMethodInvocation jjtn000 = new BSHMethodInvocation(19);
/* 2114 */     boolean jjtc000 = true;
/* 2115 */     this.jjtree.openNodeScope(jjtn000);
/* 2116 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/* 2118 */       AmbiguousName();
/* 2119 */       Arguments();
/* 2120 */     } catch (Throwable jjte000) {
/* 2121 */       if (jjtc000) {
/* 2122 */         this.jjtree.clearNodeScope(jjtn000);
/* 2123 */         jjtc000 = false;
/*      */       } else {
/* 2125 */         this.jjtree.popNode();
/*      */       } 
/* 2127 */       if (jjte000 instanceof RuntimeException) {
/* 2128 */         throw (RuntimeException)jjte000;
/*      */       }
/* 2130 */       if (jjte000 instanceof ParseException) {
/* 2131 */         throw (ParseException)jjte000;
/*      */       }
/* 2133 */       throw (Error)jjte000;
/*      */     } finally {
/* 2135 */       if (jjtc000) {
/* 2136 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 2137 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void PrimaryPrefix() throws ParseException {
/* 2143 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 26:
/*      */       case 41:
/*      */       case 55:
/*      */       case 57:
/*      */       case 60:
/*      */       case 64:
/*      */       case 66:
/*      */       case 67:
/* 2152 */         Literal();
/*      */         return;
/*      */       case 72:
/* 2155 */         jj_consume_token(72);
/* 2156 */         Expression();
/* 2157 */         jj_consume_token(73);
/*      */         return;
/*      */       case 40:
/* 2160 */         AllocationExpression();
/*      */         return;
/*      */     } 
/* 2163 */     this.jj_la1[57] = this.jj_gen;
/* 2164 */     if (jj_2_14(2147483647)) {
/* 2165 */       MethodInvocation();
/* 2166 */     } else if (jj_2_15(2147483647)) {
/* 2167 */       Type();
/*      */     } else {
/* 2169 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 69:
/* 2171 */           AmbiguousName();
/*      */           return;
/*      */       } 
/* 2174 */       this.jj_la1[58] = this.jj_gen;
/* 2175 */       jj_consume_token(-1);
/* 2176 */       throw new ParseException();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void PrimarySuffix() throws ParseException {
/* 2184 */     BSHPrimarySuffix jjtn000 = new BSHPrimarySuffix(20);
/* 2185 */     boolean jjtc000 = true;
/* 2186 */     this.jjtree.openNodeScope(jjtn000);
/* 2187 */     jjtreeOpenNodeScope(jjtn000); Token t = null;
/*      */     try {
/* 2189 */       if (jj_2_16(2)) {
/* 2190 */         jj_consume_token(80);
/* 2191 */         jj_consume_token(13);
/* 2192 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 2193 */         jjtc000 = false;
/* 2194 */         jjtreeCloseNodeScope(jjtn000);
/* 2195 */         jjtn000.operation = 0;
/*      */       } else {
/* 2197 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 76:
/* 2199 */             jj_consume_token(76);
/* 2200 */             Expression();
/* 2201 */             jj_consume_token(77);
/* 2202 */             this.jjtree.closeNodeScope(jjtn000, true);
/* 2203 */             jjtc000 = false;
/* 2204 */             jjtreeCloseNodeScope(jjtn000);
/* 2205 */             jjtn000.operation = 1;
/*      */             break;
/*      */           case 80:
/* 2208 */             jj_consume_token(80);
/* 2209 */             t = jj_consume_token(69);
/* 2210 */             switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */               case 72:
/* 2212 */                 Arguments();
/*      */                 break;
/*      */               default:
/* 2215 */                 this.jj_la1[59] = this.jj_gen;
/*      */                 break;
/*      */             } 
/* 2218 */             this.jjtree.closeNodeScope(jjtn000, true);
/* 2219 */             jjtc000 = false;
/* 2220 */             jjtreeCloseNodeScope(jjtn000);
/* 2221 */             jjtn000.operation = 2;
/* 2222 */             jjtn000.field = t.image;
/*      */             break;
/*      */           case 74:
/* 2225 */             jj_consume_token(74);
/* 2226 */             Expression();
/* 2227 */             jj_consume_token(75);
/* 2228 */             this.jjtree.closeNodeScope(jjtn000, true);
/* 2229 */             jjtc000 = false;
/* 2230 */             jjtreeCloseNodeScope(jjtn000);
/* 2231 */             jjtn000.operation = 3;
/*      */             break;
/*      */           default:
/* 2234 */             this.jj_la1[60] = this.jj_gen;
/* 2235 */             jj_consume_token(-1);
/* 2236 */             throw new ParseException();
/*      */         } 
/*      */       } 
/* 2239 */     } catch (Throwable jjte000) {
/* 2240 */       if (jjtc000) {
/* 2241 */         this.jjtree.clearNodeScope(jjtn000);
/* 2242 */         jjtc000 = false;
/*      */       } else {
/* 2244 */         this.jjtree.popNode();
/*      */       } 
/* 2246 */       if (jjte000 instanceof RuntimeException) {
/* 2247 */         throw (RuntimeException)jjte000;
/*      */       }
/* 2249 */       if (jjte000 instanceof ParseException) {
/* 2250 */         throw (ParseException)jjte000;
/*      */       }
/* 2252 */       throw (Error)jjte000;
/*      */     } finally {
/* 2254 */       if (jjtc000) {
/* 2255 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 2256 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void Literal() throws ParseException {
/* 2263 */     BSHLiteral jjtn000 = new BSHLiteral(21);
/* 2264 */     boolean jjtc000 = true;
/* 2265 */     this.jjtree.openNodeScope(jjtn000);
/* 2266 */     jjtreeOpenNodeScope(jjtn000); try {
/*      */       Token x;
/*      */       boolean b;
/*      */       String literal;
/*      */       char ch;
/* 2271 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 60:
/* 2273 */           x = jj_consume_token(60);
/* 2274 */           this.jjtree.closeNodeScope(jjtn000, true);
/* 2275 */           jjtc000 = false;
/* 2276 */           jjtreeCloseNodeScope(jjtn000);
/* 2277 */           literal = x.image;
/* 2278 */           ch = literal.charAt(literal.length() - 1);
/* 2279 */           if (ch == 'l' || ch == 'L') {
/*      */             
/* 2281 */             literal = literal.substring(0, literal.length() - 1);
/*      */ 
/*      */ 
/*      */             
/* 2285 */             jjtn000.value = new Primitive((new Long(literal)).longValue());
/*      */             break;
/*      */           } 
/*      */           try {
/* 2289 */             jjtn000.value = new Primitive(Integer.decode(literal).intValue());
/*      */           }
/* 2291 */           catch (NumberFormatException e) {
/* 2292 */             throw createParseException("Error or number too big for integer type: " + literal);
/*      */           } 
/*      */           break;
/*      */         
/*      */         case 64:
/* 2297 */           x = jj_consume_token(64);
/* 2298 */           this.jjtree.closeNodeScope(jjtn000, true);
/* 2299 */           jjtc000 = false;
/* 2300 */           jjtreeCloseNodeScope(jjtn000);
/* 2301 */           literal = x.image;
/* 2302 */           ch = literal.charAt(literal.length() - 1);
/* 2303 */           if (ch == 'f' || ch == 'F') {
/*      */             
/* 2305 */             literal = literal.substring(0, literal.length() - 1);
/* 2306 */             jjtn000.value = new Primitive((new Float(literal)).floatValue());
/*      */             
/*      */             break;
/*      */           } 
/* 2310 */           if (ch == 'd' || ch == 'D') {
/* 2311 */             literal = literal.substring(0, literal.length() - 1);
/*      */           }
/* 2313 */           jjtn000.value = new Primitive((new Double(literal)).doubleValue());
/*      */           break;
/*      */         
/*      */         case 66:
/* 2317 */           x = jj_consume_token(66);
/* 2318 */           this.jjtree.closeNodeScope(jjtn000, true);
/* 2319 */           jjtc000 = false;
/* 2320 */           jjtreeCloseNodeScope(jjtn000);
/*      */           try {
/* 2322 */             jjtn000.charSetup(x.image.substring(1, x.image.length() - 1));
/* 2323 */           } catch (Exception e) {
/* 2324 */             throw createParseException("Error parsing character: " + x.image);
/*      */           } 
/*      */           break;
/*      */         case 67:
/* 2328 */           x = jj_consume_token(67);
/* 2329 */           this.jjtree.closeNodeScope(jjtn000, true);
/* 2330 */           jjtc000 = false;
/* 2331 */           jjtreeCloseNodeScope(jjtn000);
/*      */           try {
/* 2333 */             jjtn000.stringSetup(x.image.substring(1, x.image.length() - 1));
/* 2334 */           } catch (Exception e) {
/* 2335 */             throw createParseException("Error parsing string: " + x.image);
/*      */           } 
/*      */           break;
/*      */         case 26:
/*      */         case 55:
/* 2340 */           b = BooleanLiteral();
/* 2341 */           this.jjtree.closeNodeScope(jjtn000, true);
/* 2342 */           jjtc000 = false;
/* 2343 */           jjtreeCloseNodeScope(jjtn000);
/* 2344 */           jjtn000.value = new Primitive(b);
/*      */           break;
/*      */         case 41:
/* 2347 */           NullLiteral();
/* 2348 */           this.jjtree.closeNodeScope(jjtn000, true);
/* 2349 */           jjtc000 = false;
/* 2350 */           jjtreeCloseNodeScope(jjtn000);
/* 2351 */           jjtn000.value = Primitive.NULL;
/*      */           break;
/*      */         case 57:
/* 2354 */           VoidLiteral();
/* 2355 */           this.jjtree.closeNodeScope(jjtn000, true);
/* 2356 */           jjtc000 = false;
/* 2357 */           jjtreeCloseNodeScope(jjtn000);
/* 2358 */           jjtn000.value = Primitive.VOID;
/*      */           break;
/*      */         default:
/* 2361 */           this.jj_la1[61] = this.jj_gen;
/* 2362 */           jj_consume_token(-1);
/* 2363 */           throw new ParseException();
/*      */       } 
/* 2365 */     } catch (Throwable jjte000) {
/* 2366 */       if (jjtc000) {
/* 2367 */         this.jjtree.clearNodeScope(jjtn000);
/* 2368 */         jjtc000 = false;
/*      */       } else {
/* 2370 */         this.jjtree.popNode();
/*      */       } 
/* 2372 */       if (jjte000 instanceof RuntimeException) {
/* 2373 */         throw (RuntimeException)jjte000;
/*      */       }
/* 2375 */       if (jjte000 instanceof ParseException) {
/* 2376 */         throw (ParseException)jjte000;
/*      */       }
/* 2378 */       throw (Error)jjte000;
/*      */     } finally {
/* 2380 */       if (jjtc000) {
/* 2381 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 2382 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final boolean BooleanLiteral() throws ParseException {
/* 2388 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 55:
/* 2390 */         jj_consume_token(55);
/* 2391 */         return true;
/*      */       
/*      */       case 26:
/* 2394 */         jj_consume_token(26);
/* 2395 */         return false;
/*      */     } 
/*      */     
/* 2398 */     this.jj_la1[62] = this.jj_gen;
/* 2399 */     jj_consume_token(-1);
/* 2400 */     throw new ParseException();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void NullLiteral() throws ParseException {
/* 2406 */     jj_consume_token(41);
/*      */   }
/*      */   
/*      */   public final void VoidLiteral() throws ParseException {
/* 2410 */     jj_consume_token(57);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void Arguments() throws ParseException {
/* 2415 */     BSHArguments jjtn000 = new BSHArguments(22);
/* 2416 */     boolean jjtc000 = true;
/* 2417 */     this.jjtree.openNodeScope(jjtn000);
/* 2418 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/* 2420 */       jj_consume_token(72);
/* 2421 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 11:
/*      */         case 14:
/*      */         case 17:
/*      */         case 22:
/*      */         case 26:
/*      */         case 29:
/*      */         case 36:
/*      */         case 38:
/*      */         case 40:
/*      */         case 41:
/*      */         case 47:
/*      */         case 55:
/*      */         case 57:
/*      */         case 60:
/*      */         case 64:
/*      */         case 66:
/*      */         case 67:
/*      */         case 69:
/*      */         case 72:
/*      */         case 86:
/*      */         case 87:
/*      */         case 100:
/*      */         case 101:
/*      */         case 102:
/*      */         case 103:
/* 2447 */           ArgumentList();
/*      */           break;
/*      */         default:
/* 2450 */           this.jj_la1[63] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 2453 */       jj_consume_token(73);
/* 2454 */     } catch (Throwable jjte000) {
/* 2455 */       if (jjtc000) {
/* 2456 */         this.jjtree.clearNodeScope(jjtn000);
/* 2457 */         jjtc000 = false;
/*      */       } else {
/* 2459 */         this.jjtree.popNode();
/*      */       } 
/* 2461 */       if (jjte000 instanceof RuntimeException) {
/* 2462 */         throw (RuntimeException)jjte000;
/*      */       }
/* 2464 */       if (jjte000 instanceof ParseException) {
/* 2465 */         throw (ParseException)jjte000;
/*      */       }
/* 2467 */       throw (Error)jjte000;
/*      */     } finally {
/* 2469 */       if (jjtc000) {
/* 2470 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 2471 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void ArgumentList() throws ParseException {
/* 2478 */     Expression();
/*      */     
/*      */     while (true) {
/* 2481 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 79:
/*      */           break;
/*      */         
/*      */         default:
/* 2486 */           this.jj_la1[64] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 2489 */       jj_consume_token(79);
/* 2490 */       Expression();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void AllocationExpression() throws ParseException {
/* 2496 */     BSHAllocationExpression jjtn000 = new BSHAllocationExpression(23);
/* 2497 */     boolean jjtc000 = true;
/* 2498 */     this.jjtree.openNodeScope(jjtn000);
/* 2499 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/* 2501 */       if (jj_2_18(2)) {
/* 2502 */         jj_consume_token(40);
/* 2503 */         PrimitiveType();
/* 2504 */         ArrayDimensions();
/*      */       } else {
/* 2506 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 40:
/* 2508 */             jj_consume_token(40);
/* 2509 */             AmbiguousName();
/* 2510 */             switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */               case 76:
/* 2512 */                 ArrayDimensions();
/*      */                 break;
/*      */               case 72:
/* 2515 */                 Arguments();
/* 2516 */                 if (jj_2_17(2)) {
/* 2517 */                   Block();
/*      */                 }
/*      */                 break;
/*      */             } 
/*      */ 
/*      */             
/* 2523 */             this.jj_la1[65] = this.jj_gen;
/* 2524 */             jj_consume_token(-1);
/* 2525 */             throw new ParseException();
/*      */ 
/*      */           
/*      */           default:
/* 2529 */             this.jj_la1[66] = this.jj_gen;
/* 2530 */             jj_consume_token(-1);
/* 2531 */             throw new ParseException();
/*      */         } 
/*      */       } 
/* 2534 */     } catch (Throwable jjte000) {
/* 2535 */       if (jjtc000) {
/* 2536 */         this.jjtree.clearNodeScope(jjtn000);
/* 2537 */         jjtc000 = false;
/*      */       } else {
/* 2539 */         this.jjtree.popNode();
/*      */       } 
/* 2541 */       if (jjte000 instanceof RuntimeException) {
/* 2542 */         throw (RuntimeException)jjte000;
/*      */       }
/* 2544 */       if (jjte000 instanceof ParseException) {
/* 2545 */         throw (ParseException)jjte000;
/*      */       }
/* 2547 */       throw (Error)jjte000;
/*      */     } finally {
/* 2549 */       if (jjtc000) {
/* 2550 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 2551 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void ArrayDimensions() throws ParseException {
/* 2558 */     BSHArrayDimensions jjtn000 = new BSHArrayDimensions(24);
/* 2559 */     boolean jjtc000 = true;
/* 2560 */     this.jjtree.openNodeScope(jjtn000);
/* 2561 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/* 2563 */       if (jj_2_21(2)) {
/*      */         
/*      */         while (true) {
/* 2566 */           jj_consume_token(76);
/* 2567 */           Expression();
/* 2568 */           jj_consume_token(77);
/* 2569 */           jjtn000.addDefinedDimension();
/* 2570 */           if (jj_2_19(2)) {
/*      */             continue;
/*      */           }
/*      */ 
/*      */           
/*      */           break;
/*      */         } 
/*      */         
/* 2578 */         while (jj_2_20(2)) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2583 */           jj_consume_token(76);
/* 2584 */           jj_consume_token(77);
/* 2585 */           jjtn000.addUndefinedDimension();
/*      */         } 
/*      */       } else {
/* 2588 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           
/*      */           case 76:
/*      */             while (true) {
/* 2592 */               jj_consume_token(76);
/* 2593 */               jj_consume_token(77);
/* 2594 */               jjtn000.addUndefinedDimension();
/* 2595 */               switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */                 case 76:
/*      */                   continue;
/*      */               }  break;
/*      */             } 
/* 2600 */             this.jj_la1[67] = this.jj_gen;
/*      */ 
/*      */ 
/*      */             
/* 2604 */             ArrayInitializer();
/*      */             break;
/*      */           default:
/* 2607 */             this.jj_la1[68] = this.jj_gen;
/* 2608 */             jj_consume_token(-1);
/* 2609 */             throw new ParseException();
/*      */         } 
/*      */       } 
/* 2612 */     } catch (Throwable jjte000) {
/* 2613 */       if (jjtc000) {
/* 2614 */         this.jjtree.clearNodeScope(jjtn000);
/* 2615 */         jjtc000 = false;
/*      */       } else {
/* 2617 */         this.jjtree.popNode();
/*      */       } 
/* 2619 */       if (jjte000 instanceof RuntimeException) {
/* 2620 */         throw (RuntimeException)jjte000;
/*      */       }
/* 2622 */       if (jjte000 instanceof ParseException) {
/* 2623 */         throw (ParseException)jjte000;
/*      */       }
/* 2625 */       throw (Error)jjte000;
/*      */     } finally {
/* 2627 */       if (jjtc000) {
/* 2628 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 2629 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void Statement() throws ParseException {
/* 2638 */     if (jj_2_22(2)) {
/* 2639 */       LabeledStatement();
/*      */     } else {
/* 2641 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 74:
/* 2643 */           Block();
/*      */           return;
/*      */         case 78:
/* 2646 */           EmptyStatement();
/*      */           return;
/*      */         case 11:
/*      */         case 14:
/*      */         case 17:
/*      */         case 22:
/*      */         case 26:
/*      */         case 29:
/*      */         case 36:
/*      */         case 38:
/*      */         case 40:
/*      */         case 41:
/*      */         case 47:
/*      */         case 55:
/*      */         case 57:
/*      */         case 60:
/*      */         case 64:
/*      */         case 66:
/*      */         case 67:
/*      */         case 69:
/*      */         case 72:
/*      */         case 86:
/*      */         case 87:
/*      */         case 100:
/*      */         case 101:
/*      */         case 102:
/*      */         case 103:
/* 2673 */           StatementExpression();
/* 2674 */           jj_consume_token(78);
/*      */           return;
/*      */         case 50:
/* 2677 */           SwitchStatement();
/*      */           return;
/*      */         case 32:
/* 2680 */           IfStatement();
/*      */           return;
/*      */         case 59:
/* 2683 */           WhileStatement();
/*      */           return;
/*      */         case 21:
/* 2686 */           DoStatement();
/*      */           return;
/*      */       } 
/* 2689 */       this.jj_la1[69] = this.jj_gen;
/* 2690 */       if (isRegularForStatement()) {
/* 2691 */         ForStatement();
/*      */       } else {
/* 2693 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 30:
/* 2695 */             EnhancedForStatement();
/*      */             return;
/*      */           case 12:
/* 2698 */             BreakStatement();
/*      */             return;
/*      */           case 19:
/* 2701 */             ContinueStatement();
/*      */             return;
/*      */           case 46:
/* 2704 */             ReturnStatement();
/*      */             return;
/*      */           case 51:
/* 2707 */             SynchronizedStatement();
/*      */             return;
/*      */           case 53:
/* 2710 */             ThrowStatement();
/*      */             return;
/*      */           case 56:
/* 2713 */             TryStatement();
/*      */             return;
/*      */         } 
/* 2716 */         this.jj_la1[70] = this.jj_gen;
/* 2717 */         jj_consume_token(-1);
/* 2718 */         throw new ParseException();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void LabeledStatement() throws ParseException {
/* 2726 */     jj_consume_token(69);
/* 2727 */     jj_consume_token(89);
/* 2728 */     Statement();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void Block() throws ParseException {
/* 2733 */     BSHBlock jjtn000 = new BSHBlock(25);
/* 2734 */     boolean jjtc000 = true;
/* 2735 */     this.jjtree.openNodeScope(jjtn000);
/* 2736 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/* 2738 */       jj_consume_token(74);
/*      */ 
/*      */       
/* 2741 */       while (jj_2_23(1))
/*      */       {
/*      */ 
/*      */ 
/*      */         
/* 2746 */         BlockStatement();
/*      */       }
/* 2748 */       jj_consume_token(75);
/* 2749 */     } catch (Throwable jjte000) {
/* 2750 */       if (jjtc000) {
/* 2751 */         this.jjtree.clearNodeScope(jjtn000);
/* 2752 */         jjtc000 = false;
/*      */       } else {
/* 2754 */         this.jjtree.popNode();
/*      */       } 
/* 2756 */       if (jjte000 instanceof RuntimeException) {
/* 2757 */         throw (RuntimeException)jjte000;
/*      */       }
/* 2759 */       if (jjte000 instanceof ParseException) {
/* 2760 */         throw (ParseException)jjte000;
/*      */       }
/* 2762 */       throw (Error)jjte000;
/*      */     } finally {
/* 2764 */       if (jjtc000) {
/* 2765 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 2766 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void BlockStatement() throws ParseException {
/* 2772 */     if (jj_2_24(2147483647)) {
/* 2773 */       ClassDeclaration();
/* 2774 */     } else if (jj_2_25(2147483647)) {
/* 2775 */       MethodDeclaration();
/* 2776 */     } else if (jj_2_26(2147483647)) {
/* 2777 */       MethodDeclaration();
/* 2778 */     } else if (jj_2_27(2147483647)) {
/* 2779 */       TypedVariableDeclaration();
/* 2780 */       jj_consume_token(78);
/* 2781 */     } else if (jj_2_28(1)) {
/* 2782 */       Statement();
/*      */     } else {
/* 2784 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 34:
/*      */         case 48:
/* 2787 */           ImportDeclaration();
/*      */           return;
/*      */         case 42:
/* 2790 */           PackageDeclaration();
/*      */           return;
/*      */         case 68:
/* 2793 */           FormalComment();
/*      */           return;
/*      */       } 
/* 2796 */       this.jj_la1[71] = this.jj_gen;
/* 2797 */       jj_consume_token(-1);
/* 2798 */       throw new ParseException();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void FormalComment() throws ParseException {
/* 2805 */     BSHFormalComment jjtn000 = new BSHFormalComment(26);
/* 2806 */     boolean jjtc000 = true;
/* 2807 */     this.jjtree.openNodeScope(jjtn000);
/* 2808 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/* 2810 */       Token t = jj_consume_token(68);
/* 2811 */       this.jjtree.closeNodeScope(jjtn000, this.retainComments);
/* 2812 */       jjtc000 = false;
/* 2813 */       jjtreeCloseNodeScope(jjtn000);
/* 2814 */       jjtn000.text = t.image;
/*      */     } finally {
/* 2816 */       if (jjtc000) {
/* 2817 */         this.jjtree.closeNodeScope(jjtn000, this.retainComments);
/* 2818 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void EmptyStatement() throws ParseException {
/* 2824 */     jj_consume_token(78);
/*      */   }
/*      */   
/*      */   public final void StatementExpression() throws ParseException {
/* 2828 */     Expression();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void SwitchStatement() throws ParseException {
/* 2833 */     BSHSwitchStatement jjtn000 = new BSHSwitchStatement(27);
/* 2834 */     boolean jjtc000 = true;
/* 2835 */     this.jjtree.openNodeScope(jjtn000);
/* 2836 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/* 2838 */       jj_consume_token(50);
/* 2839 */       jj_consume_token(72);
/* 2840 */       Expression();
/* 2841 */       jj_consume_token(73);
/* 2842 */       jj_consume_token(74);
/*      */       
/*      */       while (true) {
/* 2845 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 15:
/*      */           case 20:
/*      */             break;
/*      */           
/*      */           default:
/* 2851 */             this.jj_la1[72] = this.jj_gen;
/*      */             break;
/*      */         } 
/* 2854 */         SwitchLabel();
/*      */ 
/*      */         
/* 2857 */         while (jj_2_29(1))
/*      */         {
/*      */ 
/*      */ 
/*      */           
/* 2862 */           BlockStatement();
/*      */         }
/*      */       } 
/* 2865 */       jj_consume_token(75);
/* 2866 */     } catch (Throwable jjte000) {
/* 2867 */       if (jjtc000) {
/* 2868 */         this.jjtree.clearNodeScope(jjtn000);
/* 2869 */         jjtc000 = false;
/*      */       } else {
/* 2871 */         this.jjtree.popNode();
/*      */       } 
/* 2873 */       if (jjte000 instanceof RuntimeException) {
/* 2874 */         throw (RuntimeException)jjte000;
/*      */       }
/* 2876 */       if (jjte000 instanceof ParseException) {
/* 2877 */         throw (ParseException)jjte000;
/*      */       }
/* 2879 */       throw (Error)jjte000;
/*      */     } finally {
/* 2881 */       if (jjtc000) {
/* 2882 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 2883 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void SwitchLabel() throws ParseException {
/* 2890 */     BSHSwitchLabel jjtn000 = new BSHSwitchLabel(28);
/* 2891 */     boolean jjtc000 = true;
/* 2892 */     this.jjtree.openNodeScope(jjtn000);
/* 2893 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/* 2895 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 15:
/* 2897 */           jj_consume_token(15);
/* 2898 */           Expression();
/* 2899 */           jj_consume_token(89);
/*      */           break;
/*      */         case 20:
/* 2902 */           jj_consume_token(20);
/* 2903 */           jj_consume_token(89);
/* 2904 */           this.jjtree.closeNodeScope(jjtn000, true);
/* 2905 */           jjtc000 = false;
/* 2906 */           jjtreeCloseNodeScope(jjtn000);
/* 2907 */           jjtn000.isDefault = true;
/*      */           break;
/*      */         default:
/* 2910 */           this.jj_la1[73] = this.jj_gen;
/* 2911 */           jj_consume_token(-1);
/* 2912 */           throw new ParseException();
/*      */       } 
/* 2914 */     } catch (Throwable jjte000) {
/* 2915 */       if (jjtc000) {
/* 2916 */         this.jjtree.clearNodeScope(jjtn000);
/* 2917 */         jjtc000 = false;
/*      */       } else {
/* 2919 */         this.jjtree.popNode();
/*      */       } 
/* 2921 */       if (jjte000 instanceof RuntimeException) {
/* 2922 */         throw (RuntimeException)jjte000;
/*      */       }
/* 2924 */       if (jjte000 instanceof ParseException) {
/* 2925 */         throw (ParseException)jjte000;
/*      */       }
/* 2927 */       throw (Error)jjte000;
/*      */     } finally {
/* 2929 */       if (jjtc000) {
/* 2930 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 2931 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void IfStatement() throws ParseException {
/* 2938 */     BSHIfStatement jjtn000 = new BSHIfStatement(29);
/* 2939 */     boolean jjtc000 = true;
/* 2940 */     this.jjtree.openNodeScope(jjtn000);
/* 2941 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/* 2943 */       jj_consume_token(32);
/* 2944 */       jj_consume_token(72);
/* 2945 */       Expression();
/* 2946 */       jj_consume_token(73);
/* 2947 */       Statement();
/* 2948 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 23:
/* 2950 */           jj_consume_token(23);
/* 2951 */           Statement();
/*      */           break;
/*      */         default:
/* 2954 */           this.jj_la1[74] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 2957 */     } catch (Throwable jjte000) {
/* 2958 */       if (jjtc000) {
/* 2959 */         this.jjtree.clearNodeScope(jjtn000);
/* 2960 */         jjtc000 = false;
/*      */       } else {
/* 2962 */         this.jjtree.popNode();
/*      */       } 
/* 2964 */       if (jjte000 instanceof RuntimeException) {
/* 2965 */         throw (RuntimeException)jjte000;
/*      */       }
/* 2967 */       if (jjte000 instanceof ParseException) {
/* 2968 */         throw (ParseException)jjte000;
/*      */       }
/* 2970 */       throw (Error)jjte000;
/*      */     } finally {
/* 2972 */       if (jjtc000) {
/* 2973 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 2974 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void WhileStatement() throws ParseException {
/* 2981 */     BSHWhileStatement jjtn000 = new BSHWhileStatement(30);
/* 2982 */     boolean jjtc000 = true;
/* 2983 */     this.jjtree.openNodeScope(jjtn000);
/* 2984 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/* 2986 */       jj_consume_token(59);
/* 2987 */       jj_consume_token(72);
/* 2988 */       Expression();
/* 2989 */       jj_consume_token(73);
/* 2990 */       Statement();
/* 2991 */     } catch (Throwable jjte000) {
/* 2992 */       if (jjtc000) {
/* 2993 */         this.jjtree.clearNodeScope(jjtn000);
/* 2994 */         jjtc000 = false;
/*      */       } else {
/* 2996 */         this.jjtree.popNode();
/*      */       } 
/* 2998 */       if (jjte000 instanceof RuntimeException) {
/* 2999 */         throw (RuntimeException)jjte000;
/*      */       }
/* 3001 */       if (jjte000 instanceof ParseException) {
/* 3002 */         throw (ParseException)jjte000;
/*      */       }
/* 3004 */       throw (Error)jjte000;
/*      */     } finally {
/* 3006 */       if (jjtc000) {
/* 3007 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 3008 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void DoStatement() throws ParseException {
/* 3019 */     BSHWhileStatement jjtn000 = new BSHWhileStatement(30);
/* 3020 */     boolean jjtc000 = true;
/* 3021 */     this.jjtree.openNodeScope(jjtn000);
/* 3022 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/* 3024 */       jj_consume_token(21);
/* 3025 */       Statement();
/* 3026 */       jj_consume_token(59);
/* 3027 */       jj_consume_token(72);
/* 3028 */       Expression();
/* 3029 */       jj_consume_token(73);
/* 3030 */       jj_consume_token(78);
/* 3031 */       this.jjtree.closeNodeScope(jjtn000, true);
/* 3032 */       jjtc000 = false;
/* 3033 */       jjtreeCloseNodeScope(jjtn000);
/* 3034 */       jjtn000.isDoStatement = true;
/* 3035 */     } catch (Throwable jjte000) {
/* 3036 */       if (jjtc000) {
/* 3037 */         this.jjtree.clearNodeScope(jjtn000);
/* 3038 */         jjtc000 = false;
/*      */       } else {
/* 3040 */         this.jjtree.popNode();
/*      */       } 
/* 3042 */       if (jjte000 instanceof RuntimeException) {
/* 3043 */         throw (RuntimeException)jjte000;
/*      */       }
/* 3045 */       if (jjte000 instanceof ParseException) {
/* 3046 */         throw (ParseException)jjte000;
/*      */       }
/* 3048 */       throw (Error)jjte000;
/*      */     } finally {
/* 3050 */       if (jjtc000) {
/* 3051 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 3052 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void ForStatement() throws ParseException {
/* 3059 */     BSHForStatement jjtn000 = new BSHForStatement(31);
/* 3060 */     boolean jjtc000 = true;
/* 3061 */     this.jjtree.openNodeScope(jjtn000);
/* 3062 */     jjtreeOpenNodeScope(jjtn000); Token t = null;
/*      */     try {
/* 3064 */       jj_consume_token(30);
/* 3065 */       jj_consume_token(72);
/* 3066 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 10:
/*      */         case 11:
/*      */         case 14:
/*      */         case 17:
/*      */         case 22:
/*      */         case 26:
/*      */         case 27:
/*      */         case 29:
/*      */         case 36:
/*      */         case 38:
/*      */         case 39:
/*      */         case 40:
/*      */         case 41:
/*      */         case 43:
/*      */         case 44:
/*      */         case 45:
/*      */         case 47:
/*      */         case 48:
/*      */         case 49:
/*      */         case 51:
/*      */         case 52:
/*      */         case 55:
/*      */         case 57:
/*      */         case 58:
/*      */         case 60:
/*      */         case 64:
/*      */         case 66:
/*      */         case 67:
/*      */         case 69:
/*      */         case 72:
/*      */         case 86:
/*      */         case 87:
/*      */         case 100:
/*      */         case 101:
/*      */         case 102:
/*      */         case 103:
/* 3103 */           ForInit();
/* 3104 */           jjtn000.hasForInit = true;
/*      */           break;
/*      */         default:
/* 3107 */           this.jj_la1[75] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 3110 */       jj_consume_token(78);
/* 3111 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 11:
/*      */         case 14:
/*      */         case 17:
/*      */         case 22:
/*      */         case 26:
/*      */         case 29:
/*      */         case 36:
/*      */         case 38:
/*      */         case 40:
/*      */         case 41:
/*      */         case 47:
/*      */         case 55:
/*      */         case 57:
/*      */         case 60:
/*      */         case 64:
/*      */         case 66:
/*      */         case 67:
/*      */         case 69:
/*      */         case 72:
/*      */         case 86:
/*      */         case 87:
/*      */         case 100:
/*      */         case 101:
/*      */         case 102:
/*      */         case 103:
/* 3137 */           Expression();
/* 3138 */           jjtn000.hasExpression = true;
/*      */           break;
/*      */         default:
/* 3141 */           this.jj_la1[76] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 3144 */       jj_consume_token(78);
/* 3145 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 11:
/*      */         case 14:
/*      */         case 17:
/*      */         case 22:
/*      */         case 26:
/*      */         case 29:
/*      */         case 36:
/*      */         case 38:
/*      */         case 40:
/*      */         case 41:
/*      */         case 47:
/*      */         case 55:
/*      */         case 57:
/*      */         case 60:
/*      */         case 64:
/*      */         case 66:
/*      */         case 67:
/*      */         case 69:
/*      */         case 72:
/*      */         case 86:
/*      */         case 87:
/*      */         case 100:
/*      */         case 101:
/*      */         case 102:
/*      */         case 103:
/* 3171 */           ForUpdate();
/* 3172 */           jjtn000.hasForUpdate = true;
/*      */           break;
/*      */         default:
/* 3175 */           this.jj_la1[77] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 3178 */       jj_consume_token(73);
/* 3179 */       Statement();
/* 3180 */     } catch (Throwable jjte000) {
/* 3181 */       if (jjtc000) {
/* 3182 */         this.jjtree.clearNodeScope(jjtn000);
/* 3183 */         jjtc000 = false;
/*      */       } else {
/* 3185 */         this.jjtree.popNode();
/*      */       } 
/* 3187 */       if (jjte000 instanceof RuntimeException) {
/* 3188 */         throw (RuntimeException)jjte000;
/*      */       }
/* 3190 */       if (jjte000 instanceof ParseException) {
/* 3191 */         throw (ParseException)jjte000;
/*      */       }
/* 3193 */       throw (Error)jjte000;
/*      */     } finally {
/* 3195 */       if (jjtc000) {
/* 3196 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 3197 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void EnhancedForStatement() throws ParseException {
/* 3210 */     BSHEnhancedForStatement jjtn000 = new BSHEnhancedForStatement(32);
/* 3211 */     boolean jjtc000 = true;
/* 3212 */     this.jjtree.openNodeScope(jjtn000);
/* 3213 */     jjtreeOpenNodeScope(jjtn000); Token t = null;
/*      */     try {
/* 3215 */       if (jj_2_30(4)) {
/* 3216 */         jj_consume_token(30);
/* 3217 */         jj_consume_token(72);
/* 3218 */         t = jj_consume_token(69);
/* 3219 */         jj_consume_token(89);
/* 3220 */         Expression();
/* 3221 */         jj_consume_token(73);
/* 3222 */         Statement();
/* 3223 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 3224 */         jjtc000 = false;
/* 3225 */         jjtreeCloseNodeScope(jjtn000);
/* 3226 */         jjtn000.varName = t.image;
/*      */       } else {
/* 3228 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 30:
/* 3230 */             jj_consume_token(30);
/* 3231 */             jj_consume_token(72);
/* 3232 */             Type();
/* 3233 */             t = jj_consume_token(69);
/* 3234 */             jj_consume_token(89);
/* 3235 */             Expression();
/* 3236 */             jj_consume_token(73);
/* 3237 */             Statement();
/* 3238 */             this.jjtree.closeNodeScope(jjtn000, true);
/* 3239 */             jjtc000 = false;
/* 3240 */             jjtreeCloseNodeScope(jjtn000);
/* 3241 */             jjtn000.varName = t.image;
/*      */             break;
/*      */           default:
/* 3244 */             this.jj_la1[78] = this.jj_gen;
/* 3245 */             jj_consume_token(-1);
/* 3246 */             throw new ParseException();
/*      */         } 
/*      */       } 
/* 3249 */     } catch (Throwable jjte000) {
/* 3250 */       if (jjtc000) {
/* 3251 */         this.jjtree.clearNodeScope(jjtn000);
/* 3252 */         jjtc000 = false;
/*      */       } else {
/* 3254 */         this.jjtree.popNode();
/*      */       } 
/* 3256 */       if (jjte000 instanceof RuntimeException) {
/* 3257 */         throw (RuntimeException)jjte000;
/*      */       }
/* 3259 */       if (jjte000 instanceof ParseException) {
/* 3260 */         throw (ParseException)jjte000;
/*      */       }
/* 3262 */       throw (Error)jjte000;
/*      */     } finally {
/* 3264 */       if (jjtc000) {
/* 3265 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 3266 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void ForInit() throws ParseException {
/* 3272 */     Token t = null;
/* 3273 */     if (jj_2_31(2147483647)) {
/* 3274 */       TypedVariableDeclaration();
/*      */     } else {
/* 3276 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 11:
/*      */         case 14:
/*      */         case 17:
/*      */         case 22:
/*      */         case 26:
/*      */         case 29:
/*      */         case 36:
/*      */         case 38:
/*      */         case 40:
/*      */         case 41:
/*      */         case 47:
/*      */         case 55:
/*      */         case 57:
/*      */         case 60:
/*      */         case 64:
/*      */         case 66:
/*      */         case 67:
/*      */         case 69:
/*      */         case 72:
/*      */         case 86:
/*      */         case 87:
/*      */         case 100:
/*      */         case 101:
/*      */         case 102:
/*      */         case 103:
/* 3302 */           StatementExpressionList();
/*      */           return;
/*      */       } 
/* 3305 */       this.jj_la1[79] = this.jj_gen;
/* 3306 */       jj_consume_token(-1);
/* 3307 */       throw new ParseException();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void TypedVariableDeclaration() throws ParseException {
/* 3319 */     BSHTypedVariableDeclaration jjtn000 = new BSHTypedVariableDeclaration(33);
/* 3320 */     boolean jjtc000 = true;
/* 3321 */     this.jjtree.openNodeScope(jjtn000);
/* 3322 */     jjtreeOpenNodeScope(jjtn000); Token t = null;
/*      */     
/*      */     try {
/* 3325 */       Modifiers mods = Modifiers(2, false);
/* 3326 */       Type();
/* 3327 */       VariableDeclarator();
/*      */       
/*      */       while (true) {
/* 3330 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 79:
/*      */             break;
/*      */           
/*      */           default:
/* 3335 */             this.jj_la1[80] = this.jj_gen;
/*      */             break;
/*      */         } 
/* 3338 */         jj_consume_token(79);
/* 3339 */         VariableDeclarator();
/*      */       } 
/* 3341 */       this.jjtree.closeNodeScope(jjtn000, true);
/* 3342 */       jjtc000 = false;
/* 3343 */       jjtreeCloseNodeScope(jjtn000);
/* 3344 */       jjtn000.modifiers = mods;
/* 3345 */     } catch (Throwable jjte000) {
/* 3346 */       if (jjtc000) {
/* 3347 */         this.jjtree.clearNodeScope(jjtn000);
/* 3348 */         jjtc000 = false;
/*      */       } else {
/* 3350 */         this.jjtree.popNode();
/*      */       } 
/* 3352 */       if (jjte000 instanceof RuntimeException) {
/* 3353 */         throw (RuntimeException)jjte000;
/*      */       }
/* 3355 */       if (jjte000 instanceof ParseException) {
/* 3356 */         throw (ParseException)jjte000;
/*      */       }
/* 3358 */       throw (Error)jjte000;
/*      */     } finally {
/* 3360 */       if (jjtc000) {
/* 3361 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 3362 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void StatementExpressionList() throws ParseException {
/* 3369 */     BSHStatementExpressionList jjtn000 = new BSHStatementExpressionList(34);
/* 3370 */     boolean jjtc000 = true;
/* 3371 */     this.jjtree.openNodeScope(jjtn000);
/* 3372 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/* 3374 */       StatementExpression();
/*      */       
/*      */       while (true) {
/* 3377 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 79:
/*      */             break;
/*      */           
/*      */           default:
/* 3382 */             this.jj_la1[81] = this.jj_gen;
/*      */             break;
/*      */         } 
/* 3385 */         jj_consume_token(79);
/* 3386 */         StatementExpression();
/*      */       } 
/* 3388 */     } catch (Throwable jjte000) {
/* 3389 */       if (jjtc000) {
/* 3390 */         this.jjtree.clearNodeScope(jjtn000);
/* 3391 */         jjtc000 = false;
/*      */       } else {
/* 3393 */         this.jjtree.popNode();
/*      */       } 
/* 3395 */       if (jjte000 instanceof RuntimeException) {
/* 3396 */         throw (RuntimeException)jjte000;
/*      */       }
/* 3398 */       if (jjte000 instanceof ParseException) {
/* 3399 */         throw (ParseException)jjte000;
/*      */       }
/* 3401 */       throw (Error)jjte000;
/*      */     } finally {
/* 3403 */       if (jjtc000) {
/* 3404 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 3405 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void ForUpdate() throws ParseException {
/* 3411 */     StatementExpressionList();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void BreakStatement() throws ParseException {
/* 3416 */     BSHReturnStatement jjtn000 = new BSHReturnStatement(35);
/* 3417 */     boolean jjtc000 = true;
/* 3418 */     this.jjtree.openNodeScope(jjtn000);
/* 3419 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/* 3421 */       jj_consume_token(12);
/* 3422 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 69:
/* 3424 */           jj_consume_token(69);
/*      */           break;
/*      */         default:
/* 3427 */           this.jj_la1[82] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 3430 */       jj_consume_token(78);
/* 3431 */       this.jjtree.closeNodeScope(jjtn000, true);
/* 3432 */       jjtc000 = false;
/* 3433 */       jjtreeCloseNodeScope(jjtn000);
/* 3434 */       jjtn000.kind = 12;
/*      */     } finally {
/* 3436 */       if (jjtc000) {
/* 3437 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 3438 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void ContinueStatement() throws ParseException {
/* 3445 */     BSHReturnStatement jjtn000 = new BSHReturnStatement(35);
/* 3446 */     boolean jjtc000 = true;
/* 3447 */     this.jjtree.openNodeScope(jjtn000);
/* 3448 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/* 3450 */       jj_consume_token(19);
/* 3451 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 69:
/* 3453 */           jj_consume_token(69);
/*      */           break;
/*      */         default:
/* 3456 */           this.jj_la1[83] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 3459 */       jj_consume_token(78);
/* 3460 */       this.jjtree.closeNodeScope(jjtn000, true);
/* 3461 */       jjtc000 = false;
/* 3462 */       jjtreeCloseNodeScope(jjtn000);
/* 3463 */       jjtn000.kind = 19;
/*      */     } finally {
/* 3465 */       if (jjtc000) {
/* 3466 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 3467 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void ReturnStatement() throws ParseException {
/* 3474 */     BSHReturnStatement jjtn000 = new BSHReturnStatement(35);
/* 3475 */     boolean jjtc000 = true;
/* 3476 */     this.jjtree.openNodeScope(jjtn000);
/* 3477 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/* 3479 */       jj_consume_token(46);
/* 3480 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 11:
/*      */         case 14:
/*      */         case 17:
/*      */         case 22:
/*      */         case 26:
/*      */         case 29:
/*      */         case 36:
/*      */         case 38:
/*      */         case 40:
/*      */         case 41:
/*      */         case 47:
/*      */         case 55:
/*      */         case 57:
/*      */         case 60:
/*      */         case 64:
/*      */         case 66:
/*      */         case 67:
/*      */         case 69:
/*      */         case 72:
/*      */         case 86:
/*      */         case 87:
/*      */         case 100:
/*      */         case 101:
/*      */         case 102:
/*      */         case 103:
/* 3506 */           Expression();
/*      */           break;
/*      */         default:
/* 3509 */           this.jj_la1[84] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 3512 */       jj_consume_token(78);
/* 3513 */       this.jjtree.closeNodeScope(jjtn000, true);
/* 3514 */       jjtc000 = false;
/* 3515 */       jjtreeCloseNodeScope(jjtn000);
/* 3516 */       jjtn000.kind = 46;
/* 3517 */     } catch (Throwable jjte000) {
/* 3518 */       if (jjtc000) {
/* 3519 */         this.jjtree.clearNodeScope(jjtn000);
/* 3520 */         jjtc000 = false;
/*      */       } else {
/* 3522 */         this.jjtree.popNode();
/*      */       } 
/* 3524 */       if (jjte000 instanceof RuntimeException) {
/* 3525 */         throw (RuntimeException)jjte000;
/*      */       }
/* 3527 */       if (jjte000 instanceof ParseException) {
/* 3528 */         throw (ParseException)jjte000;
/*      */       }
/* 3530 */       throw (Error)jjte000;
/*      */     } finally {
/* 3532 */       if (jjtc000) {
/* 3533 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 3534 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void SynchronizedStatement() throws ParseException {
/* 3541 */     BSHBlock jjtn000 = new BSHBlock(25);
/* 3542 */     boolean jjtc000 = true;
/* 3543 */     this.jjtree.openNodeScope(jjtn000);
/* 3544 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/* 3546 */       jj_consume_token(51);
/* 3547 */       jj_consume_token(72);
/* 3548 */       Expression();
/* 3549 */       jj_consume_token(73);
/* 3550 */       Block();
/* 3551 */       this.jjtree.closeNodeScope(jjtn000, true);
/* 3552 */       jjtc000 = false;
/* 3553 */       jjtreeCloseNodeScope(jjtn000);
/* 3554 */       jjtn000.isSynchronized = true;
/* 3555 */     } catch (Throwable jjte000) {
/* 3556 */       if (jjtc000) {
/* 3557 */         this.jjtree.clearNodeScope(jjtn000);
/* 3558 */         jjtc000 = false;
/*      */       } else {
/* 3560 */         this.jjtree.popNode();
/*      */       } 
/* 3562 */       if (jjte000 instanceof RuntimeException) {
/* 3563 */         throw (RuntimeException)jjte000;
/*      */       }
/* 3565 */       if (jjte000 instanceof ParseException) {
/* 3566 */         throw (ParseException)jjte000;
/*      */       }
/* 3568 */       throw (Error)jjte000;
/*      */     } finally {
/* 3570 */       if (jjtc000) {
/* 3571 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 3572 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void ThrowStatement() throws ParseException {
/* 3579 */     BSHThrowStatement jjtn000 = new BSHThrowStatement(36);
/* 3580 */     boolean jjtc000 = true;
/* 3581 */     this.jjtree.openNodeScope(jjtn000);
/* 3582 */     jjtreeOpenNodeScope(jjtn000);
/*      */     try {
/* 3584 */       jj_consume_token(53);
/* 3585 */       Expression();
/* 3586 */       jj_consume_token(78);
/* 3587 */     } catch (Throwable jjte000) {
/* 3588 */       if (jjtc000) {
/* 3589 */         this.jjtree.clearNodeScope(jjtn000);
/* 3590 */         jjtc000 = false;
/*      */       } else {
/* 3592 */         this.jjtree.popNode();
/*      */       } 
/* 3594 */       if (jjte000 instanceof RuntimeException) {
/* 3595 */         throw (RuntimeException)jjte000;
/*      */       }
/* 3597 */       if (jjte000 instanceof ParseException) {
/* 3598 */         throw (ParseException)jjte000;
/*      */       }
/* 3600 */       throw (Error)jjte000;
/*      */     } finally {
/* 3602 */       if (jjtc000) {
/* 3603 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 3604 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void TryStatement() throws ParseException {
/* 3611 */     BSHTryStatement jjtn000 = new BSHTryStatement(37);
/* 3612 */     boolean jjtc000 = true;
/* 3613 */     this.jjtree.openNodeScope(jjtn000);
/* 3614 */     jjtreeOpenNodeScope(jjtn000); boolean closed = false;
/*      */     try {
/* 3616 */       jj_consume_token(56);
/* 3617 */       Block();
/*      */       
/*      */       while (true) {
/* 3620 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 16:
/*      */             break;
/*      */           
/*      */           default:
/* 3625 */             this.jj_la1[85] = this.jj_gen;
/*      */             break;
/*      */         } 
/* 3628 */         jj_consume_token(16);
/* 3629 */         jj_consume_token(72);
/* 3630 */         FormalParameter();
/* 3631 */         jj_consume_token(73);
/* 3632 */         Block();
/* 3633 */         closed = true;
/*      */       } 
/* 3635 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 28:
/* 3637 */           jj_consume_token(28);
/* 3638 */           Block();
/* 3639 */           closed = true;
/*      */           break;
/*      */         default:
/* 3642 */           this.jj_la1[86] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 3645 */       this.jjtree.closeNodeScope(jjtn000, true);
/* 3646 */       jjtc000 = false;
/* 3647 */       jjtreeCloseNodeScope(jjtn000);
/* 3648 */       if (!closed) throw generateParseException(); 
/* 3649 */     } catch (Throwable jjte000) {
/* 3650 */       if (jjtc000) {
/* 3651 */         this.jjtree.clearNodeScope(jjtn000);
/* 3652 */         jjtc000 = false;
/*      */       } else {
/* 3654 */         this.jjtree.popNode();
/*      */       } 
/* 3656 */       if (jjte000 instanceof RuntimeException) {
/* 3657 */         throw (RuntimeException)jjte000;
/*      */       }
/* 3659 */       if (jjte000 instanceof ParseException) {
/* 3660 */         throw (ParseException)jjte000;
/*      */       }
/* 3662 */       throw (Error)jjte000;
/*      */     } finally {
/* 3664 */       if (jjtc000) {
/* 3665 */         this.jjtree.closeNodeScope(jjtn000, true);
/* 3666 */         jjtreeCloseNodeScope(jjtn000);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private final boolean jj_2_1(int xla) {
/* 3672 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3673 */     try { return !jj_3_1(); }
/* 3674 */     catch (LookaheadSuccess ls) { return true; }
/* 3675 */     finally { jj_save(0, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_2(int xla) {
/* 3679 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3680 */     try { return !jj_3_2(); }
/* 3681 */     catch (LookaheadSuccess ls) { return true; }
/* 3682 */     finally { jj_save(1, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_3(int xla) {
/* 3686 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3687 */     try { return !jj_3_3(); }
/* 3688 */     catch (LookaheadSuccess ls) { return true; }
/* 3689 */     finally { jj_save(2, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_4(int xla) {
/* 3693 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3694 */     try { return !jj_3_4(); }
/* 3695 */     catch (LookaheadSuccess ls) { return true; }
/* 3696 */     finally { jj_save(3, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_5(int xla) {
/* 3700 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3701 */     try { return !jj_3_5(); }
/* 3702 */     catch (LookaheadSuccess ls) { return true; }
/* 3703 */     finally { jj_save(4, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_6(int xla) {
/* 3707 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3708 */     try { return !jj_3_6(); }
/* 3709 */     catch (LookaheadSuccess ls) { return true; }
/* 3710 */     finally { jj_save(5, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_7(int xla) {
/* 3714 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3715 */     try { return !jj_3_7(); }
/* 3716 */     catch (LookaheadSuccess ls) { return true; }
/* 3717 */     finally { jj_save(6, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_8(int xla) {
/* 3721 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3722 */     try { return !jj_3_8(); }
/* 3723 */     catch (LookaheadSuccess ls) { return true; }
/* 3724 */     finally { jj_save(7, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_9(int xla) {
/* 3728 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3729 */     try { return !jj_3_9(); }
/* 3730 */     catch (LookaheadSuccess ls) { return true; }
/* 3731 */     finally { jj_save(8, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_10(int xla) {
/* 3735 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3736 */     try { return !jj_3_10(); }
/* 3737 */     catch (LookaheadSuccess ls) { return true; }
/* 3738 */     finally { jj_save(9, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_11(int xla) {
/* 3742 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3743 */     try { return !jj_3_11(); }
/* 3744 */     catch (LookaheadSuccess ls) { return true; }
/* 3745 */     finally { jj_save(10, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_12(int xla) {
/* 3749 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3750 */     try { return !jj_3_12(); }
/* 3751 */     catch (LookaheadSuccess ls) { return true; }
/* 3752 */     finally { jj_save(11, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_13(int xla) {
/* 3756 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3757 */     try { return !jj_3_13(); }
/* 3758 */     catch (LookaheadSuccess ls) { return true; }
/* 3759 */     finally { jj_save(12, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_14(int xla) {
/* 3763 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3764 */     try { return !jj_3_14(); }
/* 3765 */     catch (LookaheadSuccess ls) { return true; }
/* 3766 */     finally { jj_save(13, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_15(int xla) {
/* 3770 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3771 */     try { return !jj_3_15(); }
/* 3772 */     catch (LookaheadSuccess ls) { return true; }
/* 3773 */     finally { jj_save(14, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_16(int xla) {
/* 3777 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3778 */     try { return !jj_3_16(); }
/* 3779 */     catch (LookaheadSuccess ls) { return true; }
/* 3780 */     finally { jj_save(15, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_17(int xla) {
/* 3784 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3785 */     try { return !jj_3_17(); }
/* 3786 */     catch (LookaheadSuccess ls) { return true; }
/* 3787 */     finally { jj_save(16, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_18(int xla) {
/* 3791 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3792 */     try { return !jj_3_18(); }
/* 3793 */     catch (LookaheadSuccess ls) { return true; }
/* 3794 */     finally { jj_save(17, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_19(int xla) {
/* 3798 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3799 */     try { return !jj_3_19(); }
/* 3800 */     catch (LookaheadSuccess ls) { return true; }
/* 3801 */     finally { jj_save(18, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_20(int xla) {
/* 3805 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3806 */     try { return !jj_3_20(); }
/* 3807 */     catch (LookaheadSuccess ls) { return true; }
/* 3808 */     finally { jj_save(19, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_21(int xla) {
/* 3812 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3813 */     try { return !jj_3_21(); }
/* 3814 */     catch (LookaheadSuccess ls) { return true; }
/* 3815 */     finally { jj_save(20, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_22(int xla) {
/* 3819 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3820 */     try { return !jj_3_22(); }
/* 3821 */     catch (LookaheadSuccess ls) { return true; }
/* 3822 */     finally { jj_save(21, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_23(int xla) {
/* 3826 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3827 */     try { return !jj_3_23(); }
/* 3828 */     catch (LookaheadSuccess ls) { return true; }
/* 3829 */     finally { jj_save(22, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_24(int xla) {
/* 3833 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3834 */     try { return !jj_3_24(); }
/* 3835 */     catch (LookaheadSuccess ls) { return true; }
/* 3836 */     finally { jj_save(23, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_25(int xla) {
/* 3840 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3841 */     try { return !jj_3_25(); }
/* 3842 */     catch (LookaheadSuccess ls) { return true; }
/* 3843 */     finally { jj_save(24, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_26(int xla) {
/* 3847 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3848 */     try { return !jj_3_26(); }
/* 3849 */     catch (LookaheadSuccess ls) { return true; }
/* 3850 */     finally { jj_save(25, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_27(int xla) {
/* 3854 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3855 */     try { return !jj_3_27(); }
/* 3856 */     catch (LookaheadSuccess ls) { return true; }
/* 3857 */     finally { jj_save(26, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_28(int xla) {
/* 3861 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3862 */     try { return !jj_3_28(); }
/* 3863 */     catch (LookaheadSuccess ls) { return true; }
/* 3864 */     finally { jj_save(27, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_29(int xla) {
/* 3868 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3869 */     try { return !jj_3_29(); }
/* 3870 */     catch (LookaheadSuccess ls) { return true; }
/* 3871 */     finally { jj_save(28, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_30(int xla) {
/* 3875 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3876 */     try { return !jj_3_30(); }
/* 3877 */     catch (LookaheadSuccess ls) { return true; }
/* 3878 */     finally { jj_save(29, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_31(int xla) {
/* 3882 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 3883 */     try { return !jj_3_31(); }
/* 3884 */     catch (LookaheadSuccess ls) { return true; }
/* 3885 */     finally { jj_save(30, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_3R_47() {
/* 3889 */     if (jj_3R_92()) return true; 
/* 3890 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_169() {
/* 3895 */     Token xsp = this.jj_scanpos;
/* 3896 */     if (jj_scan_token(106)) {
/* 3897 */       this.jj_scanpos = xsp;
/* 3898 */       if (jj_scan_token(107)) return true; 
/*      */     } 
/* 3900 */     if (jj_3R_164()) return true; 
/* 3901 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_46() {
/* 3905 */     if (jj_3R_91()) return true; 
/* 3906 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_28() {
/* 3911 */     Token xsp = this.jj_scanpos;
/* 3912 */     if (jj_3R_46()) {
/* 3913 */       this.jj_scanpos = xsp;
/* 3914 */       if (jj_3R_47()) {
/* 3915 */         this.jj_scanpos = xsp;
/* 3916 */         if (jj_3R_48()) {
/* 3917 */           this.jj_scanpos = xsp;
/* 3918 */           if (jj_3R_49()) {
/* 3919 */             this.jj_scanpos = xsp;
/* 3920 */             if (jj_3_28()) {
/* 3921 */               this.jj_scanpos = xsp;
/* 3922 */               if (jj_3R_50()) {
/* 3923 */                 this.jj_scanpos = xsp;
/* 3924 */                 if (jj_3R_51()) {
/* 3925 */                   this.jj_scanpos = xsp;
/* 3926 */                   if (jj_3R_52()) return true; 
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 3934 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_23() {
/* 3938 */     if (jj_3R_28()) return true; 
/* 3939 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_161() {
/* 3943 */     if (jj_3R_164()) return true;
/*      */     
/*      */     while (true) {
/* 3946 */       Token xsp = this.jj_scanpos;
/* 3947 */       if (jj_3R_169()) { this.jj_scanpos = xsp;
/*      */         
/* 3949 */         return false; }
/*      */     
/*      */     } 
/*      */   } private final boolean jj_3R_38() {
/* 3953 */     if (jj_scan_token(74)) return true;
/*      */     
/*      */     while (true) {
/* 3956 */       Token xsp = this.jj_scanpos;
/* 3957 */       if (jj_3_23()) { this.jj_scanpos = xsp;
/*      */         
/* 3959 */         if (jj_scan_token(75)) return true; 
/* 3960 */         return false; }
/*      */     
/*      */     } 
/*      */   } private final boolean jj_3R_158() {
/* 3964 */     if (jj_3R_161()) return true;
/*      */     
/*      */     while (true) {
/* 3967 */       Token xsp = this.jj_scanpos;
/* 3968 */       if (jj_3R_167()) { this.jj_scanpos = xsp;
/*      */         
/* 3970 */         return false; }
/*      */     
/*      */     } 
/*      */   } private final boolean jj_3R_40() {
/* 3974 */     if (jj_scan_token(69)) return true; 
/* 3975 */     if (jj_scan_token(89)) return true; 
/* 3976 */     if (jj_3R_45()) return true; 
/* 3977 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_156() {
/* 3981 */     if (jj_scan_token(88)) return true; 
/* 3982 */     if (jj_3R_39()) return true; 
/* 3983 */     if (jj_scan_token(89)) return true; 
/* 3984 */     if (jj_3R_108()) return true; 
/* 3985 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_165() {
/* 3990 */     Token xsp = this.jj_scanpos;
/* 3991 */     if (jj_scan_token(108)) {
/* 3992 */       this.jj_scanpos = xsp;
/* 3993 */       if (jj_scan_token(109)) return true; 
/*      */     } 
/* 3995 */     if (jj_3R_158()) return true; 
/* 3996 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_153() {
/* 4000 */     if (jj_3R_158()) return true;
/*      */     
/*      */     while (true) {
/* 4003 */       Token xsp = this.jj_scanpos;
/* 4004 */       if (jj_3R_165()) { this.jj_scanpos = xsp;
/*      */         
/* 4006 */         return false; }
/*      */     
/*      */     } 
/*      */   } private final boolean jj_3R_90() {
/* 4010 */     if (jj_3R_124()) return true; 
/* 4011 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_89() {
/* 4015 */     if (jj_3R_123()) return true; 
/* 4016 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_88() {
/* 4020 */     if (jj_3R_122()) return true; 
/* 4021 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_162() {
/* 4026 */     Token xsp = this.jj_scanpos;
/* 4027 */     if (jj_scan_token(98)) {
/* 4028 */       this.jj_scanpos = xsp;
/* 4029 */       if (jj_scan_token(99)) return true; 
/*      */     } 
/* 4031 */     if (jj_3R_153()) return true; 
/* 4032 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_87() {
/* 4036 */     if (jj_3R_121()) return true; 
/* 4037 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_148() {
/* 4041 */     if (jj_3R_153()) return true;
/*      */     
/*      */     while (true) {
/* 4044 */       Token xsp = this.jj_scanpos;
/* 4045 */       if (jj_3R_162()) { this.jj_scanpos = xsp;
/*      */         
/* 4047 */         return false; }
/*      */     
/*      */     } 
/*      */   } private final boolean jj_3R_86() {
/* 4051 */     if (jj_3R_120()) return true; 
/* 4052 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_85() {
/* 4056 */     if (jj_3R_119()) return true; 
/* 4057 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_84() {
/* 4061 */     if (jj_3R_118()) return true; 
/* 4062 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_159() {
/* 4067 */     Token xsp = this.jj_scanpos;
/* 4068 */     if (jj_scan_token(96)) {
/* 4069 */       this.jj_scanpos = xsp;
/* 4070 */       if (jj_scan_token(97)) return true; 
/*      */     } 
/* 4072 */     if (jj_3R_148()) return true; 
/* 4073 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_83() {
/* 4077 */     if (jj_3R_117()) return true; 
/* 4078 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_135() {
/* 4082 */     if (jj_3R_148()) return true;
/*      */     
/*      */     while (true) {
/* 4085 */       Token xsp = this.jj_scanpos;
/* 4086 */       if (jj_3R_159()) { this.jj_scanpos = xsp;
/*      */         
/* 4088 */         return false; }
/*      */     
/*      */     } 
/*      */   } private final boolean jj_3R_82() {
/* 4092 */     if (jj_3R_116()) return true; 
/* 4093 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_81() {
/* 4097 */     if (jj_3R_115()) return true; 
/* 4098 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_80() {
/* 4102 */     if (jj_3R_114()) return true; 
/* 4103 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_108() {
/* 4107 */     if (jj_3R_135()) return true;
/*      */     
/* 4109 */     Token xsp = this.jj_scanpos;
/* 4110 */     if (jj_3R_156()) this.jj_scanpos = xsp; 
/* 4111 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_79() {
/* 4115 */     if (jj_3R_113()) return true; 
/* 4116 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_78() {
/* 4120 */     if (jj_3R_112()) return true; 
/* 4121 */     if (jj_scan_token(78)) return true; 
/* 4122 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_17() {
/* 4126 */     if (jj_3R_38()) return true; 
/* 4127 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_77() {
/* 4131 */     if (jj_3R_38()) return true; 
/* 4132 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_45() {
/* 4137 */     Token xsp = this.jj_scanpos;
/* 4138 */     if (jj_3_22()) {
/* 4139 */       this.jj_scanpos = xsp;
/* 4140 */       if (jj_3R_77()) {
/* 4141 */         this.jj_scanpos = xsp;
/* 4142 */         if (jj_scan_token(78)) {
/* 4143 */           this.jj_scanpos = xsp;
/* 4144 */           if (jj_3R_78()) {
/* 4145 */             this.jj_scanpos = xsp;
/* 4146 */             if (jj_3R_79()) {
/* 4147 */               this.jj_scanpos = xsp;
/* 4148 */               if (jj_3R_80()) {
/* 4149 */                 this.jj_scanpos = xsp;
/* 4150 */                 if (jj_3R_81()) {
/* 4151 */                   this.jj_scanpos = xsp;
/* 4152 */                   if (jj_3R_82()) {
/* 4153 */                     this.jj_scanpos = xsp;
/* 4154 */                     this.lookingAhead = true;
/* 4155 */                     this.jj_semLA = isRegularForStatement();
/* 4156 */                     this.lookingAhead = false;
/* 4157 */                     if (!this.jj_semLA || jj_3R_83()) {
/* 4158 */                       this.jj_scanpos = xsp;
/* 4159 */                       if (jj_3R_84()) {
/* 4160 */                         this.jj_scanpos = xsp;
/* 4161 */                         if (jj_3R_85()) {
/* 4162 */                           this.jj_scanpos = xsp;
/* 4163 */                           if (jj_3R_86()) {
/* 4164 */                             this.jj_scanpos = xsp;
/* 4165 */                             if (jj_3R_87()) {
/* 4166 */                               this.jj_scanpos = xsp;
/* 4167 */                               if (jj_3R_88()) {
/* 4168 */                                 this.jj_scanpos = xsp;
/* 4169 */                                 if (jj_3R_89()) {
/* 4170 */                                   this.jj_scanpos = xsp;
/* 4171 */                                   if (jj_3R_90()) return true; 
/*      */                                 } 
/*      */                               } 
/*      */                             } 
/*      */                           } 
/*      */                         } 
/*      */                       } 
/*      */                     } 
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 4187 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_22() {
/* 4191 */     if (jj_3R_40()) return true; 
/* 4192 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_34() {
/* 4197 */     Token xsp = this.jj_scanpos;
/* 4198 */     if (jj_scan_token(81)) {
/* 4199 */       this.jj_scanpos = xsp;
/* 4200 */       if (jj_scan_token(120)) {
/* 4201 */         this.jj_scanpos = xsp;
/* 4202 */         if (jj_scan_token(121)) {
/* 4203 */           this.jj_scanpos = xsp;
/* 4204 */           if (jj_scan_token(127)) {
/* 4205 */             this.jj_scanpos = xsp;
/* 4206 */             if (jj_scan_token(118)) {
/* 4207 */               this.jj_scanpos = xsp;
/* 4208 */               if (jj_scan_token(119)) {
/* 4209 */                 this.jj_scanpos = xsp;
/* 4210 */                 if (jj_scan_token(122)) {
/* 4211 */                   this.jj_scanpos = xsp;
/* 4212 */                   if (jj_scan_token(126)) {
/* 4213 */                     this.jj_scanpos = xsp;
/* 4214 */                     if (jj_scan_token(124)) {
/* 4215 */                       this.jj_scanpos = xsp;
/* 4216 */                       if (jj_scan_token(128)) {
/* 4217 */                         this.jj_scanpos = xsp;
/* 4218 */                         if (jj_scan_token(129)) {
/* 4219 */                           this.jj_scanpos = xsp;
/* 4220 */                           if (jj_scan_token(130)) {
/* 4221 */                             this.jj_scanpos = xsp;
/* 4222 */                             if (jj_scan_token(131)) {
/* 4223 */                               this.jj_scanpos = xsp;
/* 4224 */                               if (jj_scan_token(132)) {
/* 4225 */                                 this.jj_scanpos = xsp;
/* 4226 */                                 if (jj_scan_token(133)) return true; 
/*      */                               } 
/*      */                             } 
/*      */                           } 
/*      */                         } 
/*      */                       } 
/*      */                     } 
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 4241 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_111() {
/* 4245 */     if (jj_scan_token(79)) return true; 
/* 4246 */     if (jj_3R_29()) return true; 
/* 4247 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_160() {
/* 4251 */     if (jj_scan_token(76)) return true; 
/* 4252 */     if (jj_scan_token(77)) return true; 
/* 4253 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_152() {
/* 4257 */     if (jj_3R_69()) return true;
/*      */     
/* 4259 */     Token xsp = this.jj_scanpos;
/* 4260 */     if (jj_3_17()) this.jj_scanpos = xsp; 
/* 4261 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_157() {
/* 4266 */     if (jj_3R_160()) return true; 
/*      */     while (true) {
/* 4268 */       Token xsp = this.jj_scanpos;
/* 4269 */       if (jj_3R_160()) { this.jj_scanpos = xsp;
/*      */         
/* 4271 */         if (jj_3R_97()) return true; 
/* 4272 */         return false; }
/*      */     
/*      */     } 
/*      */   } private final boolean jj_3_8() {
/* 4276 */     if (jj_3R_33()) return true; 
/* 4277 */     if (jj_3R_34()) return true; 
/* 4278 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_20() {
/* 4282 */     if (jj_scan_token(76)) return true; 
/* 4283 */     if (jj_scan_token(77)) return true; 
/* 4284 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_151() {
/* 4288 */     if (jj_3R_150()) return true; 
/* 4289 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_19() {
/* 4293 */     if (jj_scan_token(76)) return true; 
/* 4294 */     if (jj_3R_39()) return true; 
/* 4295 */     if (jj_scan_token(77)) return true; 
/* 4296 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_107() {
/* 4300 */     if (jj_3R_33()) return true; 
/* 4301 */     if (jj_3R_34()) return true; 
/* 4302 */     if (jj_3R_39()) return true; 
/* 4303 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_21()
/*      */   {
/* 4308 */     if (jj_3_19()) return true; 
/*      */     while (true) {
/* 4310 */       Token xsp = this.jj_scanpos;
/* 4311 */       if (jj_3_19()) { this.jj_scanpos = xsp;
/*      */         
/*      */         while (true) {
/* 4314 */           xsp = this.jj_scanpos;
/* 4315 */           if (jj_3_20()) { this.jj_scanpos = xsp;
/*      */             
/* 4317 */             return false; }
/*      */         
/*      */         } 
/*      */         break; }
/*      */     
/* 4322 */     }  } private final boolean jj_3R_150() { Token xsp = this.jj_scanpos;
/* 4323 */     if (jj_3_21()) {
/* 4324 */       this.jj_scanpos = xsp;
/* 4325 */       if (jj_3R_157()) return true; 
/*      */     } 
/* 4327 */     return false; }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_71() {
/* 4331 */     if (jj_3R_108()) return true; 
/* 4332 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_39() {
/* 4337 */     Token xsp = this.jj_scanpos;
/* 4338 */     if (jj_3R_70()) {
/* 4339 */       this.jj_scanpos = xsp;
/* 4340 */       if (jj_3R_71()) return true; 
/*      */     } 
/* 4342 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_70() {
/* 4346 */     if (jj_3R_107()) return true; 
/* 4347 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_145() {
/* 4351 */     if (jj_scan_token(40)) return true; 
/* 4352 */     if (jj_3R_29()) return true;
/*      */     
/* 4354 */     Token xsp = this.jj_scanpos;
/* 4355 */     if (jj_3R_151()) {
/* 4356 */       this.jj_scanpos = xsp;
/* 4357 */       if (jj_3R_152()) return true; 
/*      */     } 
/* 4359 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_18() {
/* 4363 */     if (jj_scan_token(40)) return true; 
/* 4364 */     if (jj_3R_36()) return true; 
/* 4365 */     if (jj_3R_150()) return true; 
/* 4366 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_130() {
/* 4371 */     Token xsp = this.jj_scanpos;
/* 4372 */     if (jj_3_18()) {
/* 4373 */       this.jj_scanpos = xsp;
/* 4374 */       if (jj_3R_145()) return true; 
/*      */     } 
/* 4376 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_147() {
/* 4380 */     if (jj_scan_token(79)) return true; 
/* 4381 */     if (jj_3R_39()) return true; 
/* 4382 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_76() {
/* 4386 */     if (jj_3R_29()) return true;
/*      */     
/*      */     while (true) {
/* 4389 */       Token xsp = this.jj_scanpos;
/* 4390 */       if (jj_3R_111()) { this.jj_scanpos = xsp;
/*      */         
/* 4392 */         return false; }
/*      */     
/*      */     } 
/*      */   } private final boolean jj_3R_134() {
/* 4396 */     if (jj_3R_39()) return true;
/*      */     
/*      */     while (true) {
/* 4399 */       Token xsp = this.jj_scanpos;
/* 4400 */       if (jj_3R_147()) { this.jj_scanpos = xsp;
/*      */         
/* 4402 */         return false; }
/*      */     
/*      */     } 
/*      */   } private final boolean jj_3R_106() {
/* 4406 */     if (jj_3R_134()) return true; 
/* 4407 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_7() {
/* 4411 */     if (jj_scan_token(80)) return true; 
/* 4412 */     if (jj_scan_token(69)) return true; 
/* 4413 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_69() {
/* 4417 */     if (jj_scan_token(72)) return true;
/*      */     
/* 4419 */     Token xsp = this.jj_scanpos;
/* 4420 */     if (jj_3R_106()) this.jj_scanpos = xsp; 
/* 4421 */     if (jj_scan_token(73)) return true; 
/* 4422 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_29() {
/* 4426 */     if (jj_scan_token(69)) return true;
/*      */     
/*      */     while (true) {
/* 4429 */       Token xsp = this.jj_scanpos;
/* 4430 */       if (jj_3_7()) { this.jj_scanpos = xsp;
/*      */         
/* 4432 */         return false; }
/*      */     
/*      */     } 
/*      */   } private final boolean jj_3R_68() {
/* 4436 */     if (jj_scan_token(22)) return true; 
/* 4437 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_67() {
/* 4441 */     if (jj_scan_token(29)) return true; 
/* 4442 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_155() {
/* 4446 */     if (jj_scan_token(26)) return true; 
/* 4447 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_66() {
/* 4451 */     if (jj_scan_token(38)) return true; 
/* 4452 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_65() {
/* 4456 */     if (jj_scan_token(36)) return true; 
/* 4457 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_154() {
/* 4461 */     if (jj_scan_token(55)) return true; 
/* 4462 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_149() {
/* 4467 */     Token xsp = this.jj_scanpos;
/* 4468 */     if (jj_3R_154()) {
/* 4469 */       this.jj_scanpos = xsp;
/* 4470 */       if (jj_3R_155()) return true; 
/*      */     } 
/* 4472 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_64() {
/* 4476 */     if (jj_scan_token(47)) return true; 
/* 4477 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_56() {
/* 4481 */     if (jj_3R_29()) return true; 
/* 4482 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_63() {
/* 4486 */     if (jj_scan_token(14)) return true; 
/* 4487 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_62() {
/* 4491 */     if (jj_scan_token(17)) return true; 
/* 4492 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_61() {
/* 4496 */     if (jj_scan_token(11)) return true; 
/* 4497 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_36() {
/* 4502 */     Token xsp = this.jj_scanpos;
/* 4503 */     if (jj_3R_61()) {
/* 4504 */       this.jj_scanpos = xsp;
/* 4505 */       if (jj_3R_62()) {
/* 4506 */         this.jj_scanpos = xsp;
/* 4507 */         if (jj_3R_63()) {
/* 4508 */           this.jj_scanpos = xsp;
/* 4509 */           if (jj_3R_64()) {
/* 4510 */             this.jj_scanpos = xsp;
/* 4511 */             if (jj_3R_65()) {
/* 4512 */               this.jj_scanpos = xsp;
/* 4513 */               if (jj_3R_66()) {
/* 4514 */                 this.jj_scanpos = xsp;
/* 4515 */                 if (jj_3R_67()) {
/* 4516 */                   this.jj_scanpos = xsp;
/* 4517 */                   if (jj_3R_68()) return true; 
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 4525 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_144() {
/* 4529 */     if (jj_scan_token(57)) return true; 
/* 4530 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_74() {
/* 4534 */     if (jj_3R_32()) return true; 
/* 4535 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_42() {
/* 4540 */     Token xsp = this.jj_scanpos;
/* 4541 */     if (jj_3R_73()) {
/* 4542 */       this.jj_scanpos = xsp;
/* 4543 */       if (jj_3R_74()) return true; 
/*      */     } 
/* 4545 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_73() {
/* 4549 */     if (jj_scan_token(57)) return true; 
/* 4550 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_143() {
/* 4554 */     if (jj_scan_token(41)) return true; 
/* 4555 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_6() {
/* 4559 */     if (jj_scan_token(76)) return true; 
/* 4560 */     if (jj_scan_token(77)) return true; 
/* 4561 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_142() {
/* 4565 */     if (jj_3R_149()) return true; 
/* 4566 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_55() {
/* 4570 */     if (jj_3R_36()) return true; 
/* 4571 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_110() {
/* 4575 */     if (jj_scan_token(79)) return true; 
/* 4576 */     if (jj_3R_109()) return true; 
/* 4577 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_141() {
/* 4581 */     if (jj_scan_token(67)) return true; 
/* 4582 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_32() {
/* 4587 */     Token xsp = this.jj_scanpos;
/* 4588 */     if (jj_3R_55()) {
/* 4589 */       this.jj_scanpos = xsp;
/* 4590 */       if (jj_3R_56()) return true; 
/*      */     } 
/*      */     while (true) {
/* 4593 */       xsp = this.jj_scanpos;
/* 4594 */       if (jj_3_6()) { this.jj_scanpos = xsp;
/*      */         
/* 4596 */         return false; }
/*      */     
/*      */     } 
/*      */   } private final boolean jj_3R_140() {
/* 4600 */     if (jj_scan_token(66)) return true; 
/* 4601 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_190() {
/* 4605 */     if (jj_scan_token(28)) return true; 
/* 4606 */     if (jj_3R_38()) return true; 
/* 4607 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_4() {
/* 4611 */     if (jj_scan_token(79)) return true; 
/* 4612 */     if (jj_3R_31()) return true; 
/* 4613 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_189() {
/* 4617 */     if (jj_scan_token(16)) return true; 
/* 4618 */     if (jj_scan_token(72)) return true; 
/* 4619 */     if (jj_3R_109()) return true; 
/* 4620 */     if (jj_scan_token(73)) return true; 
/* 4621 */     if (jj_3R_38()) return true; 
/* 4622 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_136() {
/* 4626 */     if (jj_scan_token(69)) return true; 
/* 4627 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_5() {
/* 4631 */     if (jj_3R_32()) return true; 
/* 4632 */     if (jj_scan_token(69)) return true; 
/* 4633 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_75() {
/* 4637 */     if (jj_3R_109()) return true;
/*      */     
/*      */     while (true) {
/* 4640 */       Token xsp = this.jj_scanpos;
/* 4641 */       if (jj_3R_110()) { this.jj_scanpos = xsp;
/*      */         
/* 4643 */         return false; }
/*      */     
/*      */     } 
/*      */   }
/*      */   private final boolean jj_3R_109() {
/* 4648 */     Token xsp = this.jj_scanpos;
/* 4649 */     if (jj_3_5()) {
/* 4650 */       this.jj_scanpos = xsp;
/* 4651 */       if (jj_3R_136()) return true; 
/*      */     } 
/* 4653 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_124() {
/* 4657 */     if (jj_scan_token(56)) return true; 
/* 4658 */     if (jj_3R_38()) return true;
/*      */     
/*      */     while (true) {
/* 4661 */       Token xsp = this.jj_scanpos;
/* 4662 */       if (jj_3R_189()) { this.jj_scanpos = xsp;
/*      */         
/* 4664 */         xsp = this.jj_scanpos;
/* 4665 */         if (jj_3R_190()) this.jj_scanpos = xsp; 
/* 4666 */         return false; }
/*      */     
/*      */     } 
/*      */   } private final boolean jj_3R_43() {
/* 4670 */     if (jj_scan_token(72)) return true;
/*      */     
/* 4672 */     Token xsp = this.jj_scanpos;
/* 4673 */     if (jj_3R_75()) this.jj_scanpos = xsp; 
/* 4674 */     if (jj_scan_token(73)) return true; 
/* 4675 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_163() {
/* 4679 */     if (jj_3R_31()) return true;
/*      */     
/*      */     while (true) {
/* 4682 */       Token xsp = this.jj_scanpos;
/* 4683 */       if (jj_3_4()) { this.jj_scanpos = xsp;
/*      */         
/* 4685 */         return false; }
/*      */     
/*      */     } 
/*      */   } private final boolean jj_3R_139() {
/* 4689 */     if (jj_scan_token(64)) return true; 
/* 4690 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_97() {
/* 4694 */     if (jj_scan_token(74)) return true;
/*      */     
/* 4696 */     Token xsp = this.jj_scanpos;
/* 4697 */     if (jj_3R_163()) this.jj_scanpos = xsp; 
/* 4698 */     xsp = this.jj_scanpos;
/* 4699 */     if (jj_scan_token(79)) this.jj_scanpos = xsp; 
/* 4700 */     if (jj_scan_token(75)) return true; 
/* 4701 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_30() {
/* 4705 */     if (jj_scan_token(80)) return true; 
/* 4706 */     if (jj_scan_token(104)) return true; 
/* 4707 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_123() {
/* 4711 */     if (jj_scan_token(53)) return true; 
/* 4712 */     if (jj_3R_39()) return true; 
/* 4713 */     if (jj_scan_token(78)) return true; 
/* 4714 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_180() {
/* 4718 */     if (jj_scan_token(81)) return true; 
/* 4719 */     if (jj_3R_31()) return true; 
/* 4720 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_54() {
/* 4724 */     if (jj_3R_39()) return true; 
/* 4725 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_188() {
/* 4729 */     if (jj_3R_39()) return true; 
/* 4730 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_53() {
/* 4734 */     if (jj_3R_97()) return true; 
/* 4735 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_31() {
/* 4740 */     Token xsp = this.jj_scanpos;
/* 4741 */     if (jj_3R_53()) {
/* 4742 */       this.jj_scanpos = xsp;
/* 4743 */       if (jj_3R_54()) return true; 
/*      */     } 
/* 4745 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_122() {
/* 4749 */     if (jj_scan_token(51)) return true; 
/* 4750 */     if (jj_scan_token(72)) return true; 
/* 4751 */     if (jj_3R_39()) return true; 
/* 4752 */     if (jj_scan_token(73)) return true; 
/* 4753 */     if (jj_3R_38()) return true; 
/* 4754 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_177() {
/* 4758 */     if (jj_scan_token(79)) return true; 
/* 4759 */     if (jj_3R_176()) return true; 
/* 4760 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_210() {
/* 4764 */     if (jj_scan_token(79)) return true; 
/* 4765 */     if (jj_3R_112()) return true; 
/* 4766 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_121() {
/* 4770 */     if (jj_scan_token(46)) return true;
/*      */     
/* 4772 */     Token xsp = this.jj_scanpos;
/* 4773 */     if (jj_3R_188()) this.jj_scanpos = xsp; 
/* 4774 */     if (jj_scan_token(78)) return true; 
/* 4775 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_129() {
/* 4780 */     Token xsp = this.jj_scanpos;
/* 4781 */     if (jj_3R_138()) {
/* 4782 */       this.jj_scanpos = xsp;
/* 4783 */       if (jj_3R_139()) {
/* 4784 */         this.jj_scanpos = xsp;
/* 4785 */         if (jj_3R_140()) {
/* 4786 */           this.jj_scanpos = xsp;
/* 4787 */           if (jj_3R_141()) {
/* 4788 */             this.jj_scanpos = xsp;
/* 4789 */             if (jj_3R_142()) {
/* 4790 */               this.jj_scanpos = xsp;
/* 4791 */               if (jj_3R_143()) {
/* 4792 */                 this.jj_scanpos = xsp;
/* 4793 */                 if (jj_3R_144()) return true; 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 4800 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_138() {
/* 4804 */     if (jj_scan_token(60)) return true; 
/* 4805 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_146() {
/* 4809 */     if (jj_3R_69()) return true; 
/* 4810 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_176() {
/* 4814 */     if (jj_scan_token(69)) return true;
/*      */     
/* 4816 */     Token xsp = this.jj_scanpos;
/* 4817 */     if (jj_3R_180()) this.jj_scanpos = xsp; 
/* 4818 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_105() {
/* 4822 */     if (jj_3R_129()) return true; 
/* 4823 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_120() {
/* 4827 */     if (jj_scan_token(19)) return true;
/*      */     
/* 4829 */     Token xsp = this.jj_scanpos;
/* 4830 */     if (jj_scan_token(69)) this.jj_scanpos = xsp; 
/* 4831 */     if (jj_scan_token(78)) return true; 
/* 4832 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_119() {
/* 4836 */     if (jj_scan_token(12)) return true;
/*      */     
/* 4838 */     Token xsp = this.jj_scanpos;
/* 4839 */     if (jj_scan_token(69)) this.jj_scanpos = xsp; 
/* 4840 */     if (jj_scan_token(78)) return true; 
/* 4841 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_195() {
/* 4845 */     if (jj_3R_205()) return true; 
/* 4846 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_128() {
/* 4850 */     if (jj_scan_token(34)) return true; 
/* 4851 */     if (jj_scan_token(104)) return true; 
/* 4852 */     if (jj_scan_token(78)) return true; 
/* 4853 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_133() {
/* 4857 */     if (jj_scan_token(74)) return true; 
/* 4858 */     if (jj_3R_39()) return true; 
/* 4859 */     if (jj_scan_token(75)) return true; 
/* 4860 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_205() {
/* 4864 */     if (jj_3R_112()) return true;
/*      */     
/*      */     while (true) {
/* 4867 */       Token xsp = this.jj_scanpos;
/* 4868 */       if (jj_3R_210()) { this.jj_scanpos = xsp;
/*      */         
/* 4870 */         return false; }
/*      */     
/*      */     } 
/*      */   } private final boolean jj_3R_132() {
/* 4874 */     if (jj_scan_token(80)) return true; 
/* 4875 */     if (jj_scan_token(69)) return true;
/*      */     
/* 4877 */     Token xsp = this.jj_scanpos;
/* 4878 */     if (jj_3R_146()) this.jj_scanpos = xsp; 
/* 4879 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3_3() {
/* 4884 */     Token xsp = this.jj_scanpos;
/* 4885 */     if (jj_scan_token(48)) this.jj_scanpos = xsp; 
/* 4886 */     if (jj_scan_token(34)) return true; 
/* 4887 */     if (jj_3R_29()) return true; 
/* 4888 */     xsp = this.jj_scanpos;
/* 4889 */     if (jj_3R_30()) this.jj_scanpos = xsp; 
/* 4890 */     if (jj_scan_token(78)) return true; 
/* 4891 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_94() {
/* 4896 */     Token xsp = this.jj_scanpos;
/* 4897 */     if (jj_3_3()) {
/* 4898 */       this.jj_scanpos = xsp;
/* 4899 */       if (jj_3R_128()) return true; 
/*      */     } 
/* 4901 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_93() {
/* 4905 */     if (jj_3R_41()) return true; 
/* 4906 */     if (jj_3R_32()) return true; 
/* 4907 */     if (jj_3R_176()) return true;
/*      */     
/*      */     while (true) {
/* 4910 */       Token xsp = this.jj_scanpos;
/* 4911 */       if (jj_3R_177()) { this.jj_scanpos = xsp;
/*      */         
/* 4913 */         return false; }
/*      */     
/*      */     } 
/*      */   } private final boolean jj_3R_131() {
/* 4917 */     if (jj_scan_token(76)) return true; 
/* 4918 */     if (jj_3R_39()) return true; 
/* 4919 */     if (jj_scan_token(77)) return true; 
/* 4920 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_95() {
/* 4924 */     if (jj_scan_token(42)) return true; 
/* 4925 */     if (jj_3R_29()) return true; 
/* 4926 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_2() {
/* 4930 */     if (jj_scan_token(69)) return true; 
/* 4931 */     if (jj_scan_token(72)) return true; 
/* 4932 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_175() {
/* 4936 */     if (jj_3R_38()) return true; 
/* 4937 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_16() {
/* 4941 */     if (jj_scan_token(80)) return true; 
/* 4942 */     if (jj_scan_token(13)) return true; 
/* 4943 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_104() {
/* 4948 */     Token xsp = this.jj_scanpos;
/* 4949 */     if (jj_3_16()) {
/* 4950 */       this.jj_scanpos = xsp;
/* 4951 */       if (jj_3R_131()) {
/* 4952 */         this.jj_scanpos = xsp;
/* 4953 */         if (jj_3R_132()) {
/* 4954 */           this.jj_scanpos = xsp;
/* 4955 */           if (jj_3R_133()) return true; 
/*      */         } 
/*      */       } 
/*      */     } 
/* 4959 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_174() {
/* 4963 */     if (jj_scan_token(54)) return true; 
/* 4964 */     if (jj_3R_76()) return true; 
/* 4965 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_15() {
/* 4969 */     if (jj_3R_32()) return true; 
/* 4970 */     if (jj_scan_token(80)) return true; 
/* 4971 */     if (jj_scan_token(13)) return true; 
/* 4972 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_31() {
/* 4976 */     if (jj_3R_41()) return true; 
/* 4977 */     if (jj_3R_32()) return true; 
/* 4978 */     if (jj_scan_token(69)) return true; 
/* 4979 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_14() {
/* 4983 */     if (jj_3R_37()) return true; 
/* 4984 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_126() {
/* 4988 */     if (jj_scan_token(69)) return true; 
/* 4989 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_127() {
/* 4993 */     if (jj_3R_42()) return true; 
/* 4994 */     if (jj_scan_token(69)) return true; 
/* 4995 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_92() {
/* 4999 */     if (jj_3R_41()) return true;
/*      */     
/* 5001 */     Token xsp = this.jj_scanpos;
/* 5002 */     if (jj_3R_126()) {
/* 5003 */       this.jj_scanpos = xsp;
/* 5004 */       if (jj_3R_127()) return true; 
/*      */     } 
/* 5006 */     if (jj_3R_43()) return true; 
/* 5007 */     xsp = this.jj_scanpos;
/* 5008 */     if (jj_3R_174()) this.jj_scanpos = xsp; 
/* 5009 */     xsp = this.jj_scanpos;
/* 5010 */     if (jj_3R_175()) {
/* 5011 */       this.jj_scanpos = xsp;
/* 5012 */       if (jj_scan_token(78)) return true; 
/*      */     } 
/* 5014 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_204() {
/* 5018 */     if (jj_3R_205()) return true; 
/* 5019 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_103() {
/* 5023 */     if (jj_3R_29()) return true; 
/* 5024 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_203() {
/* 5028 */     if (jj_3R_93()) return true; 
/* 5029 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_194() {
/* 5034 */     Token xsp = this.jj_scanpos;
/* 5035 */     if (jj_3R_203()) {
/* 5036 */       this.jj_scanpos = xsp;
/* 5037 */       if (jj_3R_204()) return true; 
/*      */     } 
/* 5039 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_102() {
/* 5043 */     if (jj_3R_32()) return true; 
/* 5044 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_58() {
/* 5048 */     if (jj_3R_104()) return true; 
/* 5049 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_125() {
/* 5053 */     if (jj_scan_token(37)) return true; 
/* 5054 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_101() {
/* 5058 */     if (jj_3R_37()) return true; 
/* 5059 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_100() {
/* 5063 */     if (jj_3R_130()) return true; 
/* 5064 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_99() {
/* 5068 */     if (jj_scan_token(72)) return true; 
/* 5069 */     if (jj_3R_39()) return true; 
/* 5070 */     if (jj_scan_token(73)) return true; 
/* 5071 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_137() {
/* 5075 */     if (jj_scan_token(30)) return true; 
/* 5076 */     if (jj_scan_token(72)) return true; 
/* 5077 */     if (jj_3R_32()) return true; 
/* 5078 */     if (jj_scan_token(69)) return true; 
/* 5079 */     if (jj_scan_token(89)) return true; 
/* 5080 */     if (jj_3R_39()) return true; 
/* 5081 */     if (jj_scan_token(73)) return true; 
/* 5082 */     if (jj_3R_45()) return true; 
/* 5083 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_184() {
/* 5087 */     if (jj_scan_token(23)) return true; 
/* 5088 */     if (jj_3R_45()) return true; 
/* 5089 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_173() {
/* 5093 */     if (jj_scan_token(33)) return true; 
/* 5094 */     if (jj_3R_76()) return true; 
/* 5095 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_57() {
/* 5100 */     Token xsp = this.jj_scanpos;
/* 5101 */     if (jj_3R_98()) {
/* 5102 */       this.jj_scanpos = xsp;
/* 5103 */       if (jj_3R_99()) {
/* 5104 */         this.jj_scanpos = xsp;
/* 5105 */         if (jj_3R_100()) {
/* 5106 */           this.jj_scanpos = xsp;
/* 5107 */           if (jj_3R_101()) {
/* 5108 */             this.jj_scanpos = xsp;
/* 5109 */             if (jj_3R_102()) {
/* 5110 */               this.jj_scanpos = xsp;
/* 5111 */               if (jj_3R_103()) return true; 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 5117 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_98() {
/* 5121 */     if (jj_3R_129()) return true; 
/* 5122 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_172() {
/* 5126 */     if (jj_scan_token(25)) return true; 
/* 5127 */     if (jj_3R_29()) return true; 
/* 5128 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_30() {
/* 5132 */     if (jj_scan_token(30)) return true; 
/* 5133 */     if (jj_scan_token(72)) return true; 
/* 5134 */     if (jj_scan_token(69)) return true; 
/* 5135 */     if (jj_scan_token(89)) return true; 
/* 5136 */     if (jj_3R_39()) return true; 
/* 5137 */     if (jj_scan_token(73)) return true; 
/* 5138 */     if (jj_3R_45()) return true; 
/* 5139 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_118() {
/* 5144 */     Token xsp = this.jj_scanpos;
/* 5145 */     if (jj_3_30()) {
/* 5146 */       this.jj_scanpos = xsp;
/* 5147 */       if (jj_3R_137()) return true; 
/*      */     } 
/* 5149 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_37() {
/* 5153 */     if (jj_3R_29()) return true; 
/* 5154 */     if (jj_3R_69()) return true; 
/* 5155 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_185() {
/* 5159 */     if (jj_3R_194()) return true; 
/* 5160 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_91() {
/* 5164 */     if (jj_3R_41()) return true;
/*      */     
/* 5166 */     Token xsp = this.jj_scanpos;
/* 5167 */     if (jj_scan_token(13)) {
/* 5168 */       this.jj_scanpos = xsp;
/* 5169 */       if (jj_3R_125()) return true; 
/*      */     } 
/* 5171 */     if (jj_scan_token(69)) return true; 
/* 5172 */     xsp = this.jj_scanpos;
/* 5173 */     if (jj_3R_172()) this.jj_scanpos = xsp; 
/* 5174 */     xsp = this.jj_scanpos;
/* 5175 */     if (jj_3R_173()) this.jj_scanpos = xsp; 
/* 5176 */     if (jj_3R_38()) return true; 
/* 5177 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_13() {
/* 5181 */     if (jj_scan_token(72)) return true; 
/* 5182 */     if (jj_3R_36()) return true; 
/* 5183 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_187() {
/* 5187 */     if (jj_3R_195()) return true; 
/* 5188 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_186() {
/* 5192 */     if (jj_3R_39()) return true; 
/* 5193 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_33() {
/* 5197 */     if (jj_3R_57()) return true;
/*      */     
/*      */     while (true) {
/* 5200 */       Token xsp = this.jj_scanpos;
/* 5201 */       if (jj_3R_58()) { this.jj_scanpos = xsp;
/*      */         
/* 5203 */         return false; }
/*      */     
/*      */     } 
/*      */   } private final boolean jj_3R_217() {
/* 5207 */     if (jj_scan_token(72)) return true; 
/* 5208 */     if (jj_3R_32()) return true; 
/* 5209 */     if (jj_scan_token(73)) return true; 
/* 5210 */     if (jj_3R_208()) return true; 
/* 5211 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_216() {
/* 5215 */     if (jj_scan_token(72)) return true; 
/* 5216 */     if (jj_3R_32()) return true; 
/* 5217 */     if (jj_scan_token(73)) return true; 
/* 5218 */     if (jj_3R_191()) return true; 
/* 5219 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_117() {
/* 5223 */     if (jj_scan_token(30)) return true; 
/* 5224 */     if (jj_scan_token(72)) return true;
/*      */     
/* 5226 */     Token xsp = this.jj_scanpos;
/* 5227 */     if (jj_3R_185()) this.jj_scanpos = xsp; 
/* 5228 */     if (jj_scan_token(78)) return true; 
/* 5229 */     xsp = this.jj_scanpos;
/* 5230 */     if (jj_3R_186()) this.jj_scanpos = xsp; 
/* 5231 */     if (jj_scan_token(78)) return true; 
/* 5232 */     xsp = this.jj_scanpos;
/* 5233 */     if (jj_3R_187()) this.jj_scanpos = xsp; 
/* 5234 */     if (jj_scan_token(73)) return true; 
/* 5235 */     if (jj_3R_45()) return true; 
/* 5236 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_214() {
/* 5241 */     Token xsp = this.jj_scanpos;
/* 5242 */     if (jj_3R_216()) {
/* 5243 */       this.jj_scanpos = xsp;
/* 5244 */       if (jj_3R_217()) return true; 
/*      */     } 
/* 5246 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_12() {
/* 5250 */     if (jj_3R_33()) return true;
/*      */     
/* 5252 */     Token xsp = this.jj_scanpos;
/* 5253 */     if (jj_scan_token(100)) {
/* 5254 */       this.jj_scanpos = xsp;
/* 5255 */       if (jj_scan_token(101)) return true; 
/*      */     } 
/* 5257 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_219() {
/* 5261 */     if (jj_3R_33()) return true; 
/* 5262 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_116() {
/* 5266 */     if (jj_scan_token(21)) return true; 
/* 5267 */     if (jj_3R_45()) return true; 
/* 5268 */     if (jj_scan_token(59)) return true; 
/* 5269 */     if (jj_scan_token(72)) return true; 
/* 5270 */     if (jj_3R_39()) return true; 
/* 5271 */     if (jj_scan_token(73)) return true; 
/* 5272 */     if (jj_scan_token(78)) return true; 
/* 5273 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_11() {
/* 5277 */     if (jj_scan_token(72)) return true; 
/* 5278 */     if (jj_3R_29()) return true; 
/* 5279 */     if (jj_scan_token(76)) return true; 
/* 5280 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_218() {
/* 5284 */     if (jj_3R_33()) return true;
/*      */     
/* 5286 */     Token xsp = this.jj_scanpos;
/* 5287 */     if (jj_scan_token(100)) {
/* 5288 */       this.jj_scanpos = xsp;
/* 5289 */       if (jj_scan_token(101)) return true; 
/*      */     } 
/* 5291 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_215() {
/* 5296 */     Token xsp = this.jj_scanpos;
/* 5297 */     if (jj_3R_218()) {
/* 5298 */       this.jj_scanpos = xsp;
/* 5299 */       if (jj_3R_219()) return true; 
/*      */     } 
/* 5301 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_72() {
/* 5306 */     Token xsp = this.jj_scanpos;
/* 5307 */     if (jj_scan_token(43)) {
/* 5308 */       this.jj_scanpos = xsp;
/* 5309 */       if (jj_scan_token(44)) {
/* 5310 */         this.jj_scanpos = xsp;
/* 5311 */         if (jj_scan_token(45)) {
/* 5312 */           this.jj_scanpos = xsp;
/* 5313 */           if (jj_scan_token(51)) {
/* 5314 */             this.jj_scanpos = xsp;
/* 5315 */             if (jj_scan_token(27)) {
/* 5316 */               this.jj_scanpos = xsp;
/* 5317 */               if (jj_scan_token(39)) {
/* 5318 */                 this.jj_scanpos = xsp;
/* 5319 */                 if (jj_scan_token(52)) {
/* 5320 */                   this.jj_scanpos = xsp;
/* 5321 */                   if (jj_scan_token(58)) {
/* 5322 */                     this.jj_scanpos = xsp;
/* 5323 */                     if (jj_scan_token(10)) {
/* 5324 */                       this.jj_scanpos = xsp;
/* 5325 */                       if (jj_scan_token(48)) {
/* 5326 */                         this.jj_scanpos = xsp;
/* 5327 */                         if (jj_scan_token(49)) return true; 
/*      */                       } 
/*      */                     } 
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 5338 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_115() {
/* 5342 */     if (jj_scan_token(59)) return true; 
/* 5343 */     if (jj_scan_token(72)) return true; 
/* 5344 */     if (jj_3R_39()) return true; 
/* 5345 */     if (jj_scan_token(73)) return true; 
/* 5346 */     if (jj_3R_45()) return true; 
/* 5347 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_60() {
/* 5351 */     if (jj_scan_token(72)) return true; 
/* 5352 */     if (jj_3R_29()) return true; 
/* 5353 */     if (jj_scan_token(73)) return true;
/*      */     
/* 5355 */     Token xsp = this.jj_scanpos;
/* 5356 */     if (jj_scan_token(87)) {
/* 5357 */       this.jj_scanpos = xsp;
/* 5358 */       if (jj_scan_token(86)) {
/* 5359 */         this.jj_scanpos = xsp;
/* 5360 */         if (jj_scan_token(72)) {
/* 5361 */           this.jj_scanpos = xsp;
/* 5362 */           if (jj_scan_token(69)) {
/* 5363 */             this.jj_scanpos = xsp;
/* 5364 */             if (jj_scan_token(40)) {
/* 5365 */               this.jj_scanpos = xsp;
/* 5366 */               if (jj_3R_105()) return true; 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 5372 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_59() {
/* 5376 */     if (jj_scan_token(72)) return true; 
/* 5377 */     if (jj_3R_29()) return true; 
/* 5378 */     if (jj_scan_token(76)) return true; 
/* 5379 */     if (jj_scan_token(77)) return true; 
/* 5380 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_9() {
/* 5384 */     if (jj_3R_35()) return true; 
/* 5385 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_29() {
/* 5389 */     if (jj_3R_28()) return true; 
/* 5390 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_114() {
/* 5394 */     if (jj_scan_token(32)) return true; 
/* 5395 */     if (jj_scan_token(72)) return true; 
/* 5396 */     if (jj_3R_39()) return true; 
/* 5397 */     if (jj_scan_token(73)) return true; 
/* 5398 */     if (jj_3R_45()) return true;
/*      */     
/* 5400 */     Token xsp = this.jj_scanpos;
/* 5401 */     if (jj_3R_184()) this.jj_scanpos = xsp; 
/* 5402 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_41() {
/*      */     while (true) {
/* 5408 */       Token xsp = this.jj_scanpos;
/* 5409 */       if (jj_3R_72()) { this.jj_scanpos = xsp;
/*      */         
/* 5411 */         return false; }
/*      */     
/*      */     } 
/*      */   }
/*      */   private final boolean jj_3R_35() {
/* 5416 */     Token xsp = this.jj_scanpos;
/* 5417 */     if (jj_3_10()) {
/* 5418 */       this.jj_scanpos = xsp;
/* 5419 */       if (jj_3R_59()) {
/* 5420 */         this.jj_scanpos = xsp;
/* 5421 */         if (jj_3R_60()) return true; 
/*      */       } 
/*      */     } 
/* 5424 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_10() {
/* 5428 */     if (jj_scan_token(72)) return true; 
/* 5429 */     if (jj_3R_36()) return true; 
/* 5430 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_213() {
/* 5434 */     if (jj_3R_215()) return true; 
/* 5435 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_212() {
/* 5439 */     if (jj_3R_214()) return true; 
/* 5440 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_202() {
/* 5444 */     if (jj_scan_token(20)) return true; 
/* 5445 */     if (jj_scan_token(89)) return true; 
/* 5446 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_211() {
/* 5451 */     Token xsp = this.jj_scanpos;
/* 5452 */     if (jj_scan_token(87)) {
/* 5453 */       this.jj_scanpos = xsp;
/* 5454 */       if (jj_scan_token(86)) return true; 
/*      */     } 
/* 5456 */     if (jj_3R_191()) return true; 
/* 5457 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_208() {
/* 5462 */     Token xsp = this.jj_scanpos;
/* 5463 */     if (jj_3R_211()) {
/* 5464 */       this.jj_scanpos = xsp;
/* 5465 */       if (jj_3R_212()) {
/* 5466 */         this.jj_scanpos = xsp;
/* 5467 */         if (jj_3R_213()) return true; 
/*      */       } 
/*      */     } 
/* 5470 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_201() {
/* 5474 */     if (jj_scan_token(15)) return true; 
/* 5475 */     if (jj_3R_39()) return true; 
/* 5476 */     if (jj_scan_token(89)) return true; 
/* 5477 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_193() {
/* 5482 */     Token xsp = this.jj_scanpos;
/* 5483 */     if (jj_3R_201()) {
/* 5484 */       this.jj_scanpos = xsp;
/* 5485 */       if (jj_3R_202()) return true; 
/*      */     } 
/* 5487 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_183() {
/* 5491 */     if (jj_3R_193()) return true;
/*      */     
/*      */     while (true) {
/* 5494 */       Token xsp = this.jj_scanpos;
/* 5495 */       if (jj_3_29()) { this.jj_scanpos = xsp;
/*      */         
/* 5497 */         return false; }
/*      */     
/*      */     } 
/*      */   } private final boolean jj_3R_207() {
/* 5501 */     if (jj_scan_token(101)) return true; 
/* 5502 */     if (jj_3R_33()) return true; 
/* 5503 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_1() {
/* 5507 */     if (jj_3R_28()) return true; 
/* 5508 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_113() {
/* 5512 */     if (jj_scan_token(50)) return true; 
/* 5513 */     if (jj_scan_token(72)) return true; 
/* 5514 */     if (jj_3R_39()) return true; 
/* 5515 */     if (jj_scan_token(73)) return true; 
/* 5516 */     if (jj_scan_token(74)) return true;
/*      */     
/*      */     while (true) {
/* 5519 */       Token xsp = this.jj_scanpos;
/* 5520 */       if (jj_3R_183()) { this.jj_scanpos = xsp;
/*      */         
/* 5522 */         if (jj_scan_token(75)) return true; 
/* 5523 */         return false; }
/*      */     
/*      */     } 
/*      */   }
/*      */   private final boolean jj_3R_209() {
/* 5528 */     Token xsp = this.jj_scanpos;
/* 5529 */     if (jj_scan_token(104)) {
/* 5530 */       this.jj_scanpos = xsp;
/* 5531 */       if (jj_scan_token(105)) {
/* 5532 */         this.jj_scanpos = xsp;
/* 5533 */         if (jj_scan_token(111)) return true; 
/*      */       } 
/*      */     } 
/* 5536 */     if (jj_3R_191()) return true; 
/* 5537 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_206() {
/* 5541 */     if (jj_scan_token(100)) return true; 
/* 5542 */     if (jj_3R_33()) return true; 
/* 5543 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_199() {
/* 5547 */     if (jj_3R_208()) return true; 
/* 5548 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_198() {
/* 5552 */     if (jj_3R_207()) return true; 
/* 5553 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_197() {
/* 5557 */     if (jj_3R_206()) return true; 
/* 5558 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_196() {
/* 5563 */     Token xsp = this.jj_scanpos;
/* 5564 */     if (jj_scan_token(102)) {
/* 5565 */       this.jj_scanpos = xsp;
/* 5566 */       if (jj_scan_token(103)) return true; 
/*      */     } 
/* 5568 */     if (jj_3R_191()) return true; 
/* 5569 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_191() {
/* 5574 */     Token xsp = this.jj_scanpos;
/* 5575 */     if (jj_3R_196()) {
/* 5576 */       this.jj_scanpos = xsp;
/* 5577 */       if (jj_3R_197()) {
/* 5578 */         this.jj_scanpos = xsp;
/* 5579 */         if (jj_3R_198()) {
/* 5580 */           this.jj_scanpos = xsp;
/* 5581 */           if (jj_3R_199()) return true; 
/*      */         } 
/*      */       } 
/*      */     } 
/* 5585 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_44() {
/* 5589 */     if (jj_scan_token(54)) return true; 
/* 5590 */     if (jj_3R_76()) return true; 
/* 5591 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_112() {
/* 5595 */     if (jj_3R_39()) return true; 
/* 5596 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_181() {
/* 5600 */     if (jj_3R_191()) return true;
/*      */     
/*      */     while (true) {
/* 5603 */       Token xsp = this.jj_scanpos;
/* 5604 */       if (jj_3R_209()) { this.jj_scanpos = xsp;
/*      */         
/* 5606 */         return false; }
/*      */     
/*      */     } 
/*      */   }
/*      */   private final boolean jj_3R_200() {
/* 5611 */     Token xsp = this.jj_scanpos;
/* 5612 */     if (jj_scan_token(102)) {
/* 5613 */       this.jj_scanpos = xsp;
/* 5614 */       if (jj_scan_token(103)) return true; 
/*      */     } 
/* 5616 */     if (jj_3R_181()) return true; 
/* 5617 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_178() {
/* 5621 */     if (jj_3R_181()) return true;
/*      */     
/*      */     while (true) {
/* 5624 */       Token xsp = this.jj_scanpos;
/* 5625 */       if (jj_3R_200()) { this.jj_scanpos = xsp;
/*      */         
/* 5627 */         return false; }
/*      */     
/*      */     } 
/*      */   } private final boolean jj_3R_96() {
/* 5631 */     if (jj_scan_token(68)) return true; 
/* 5632 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_192() {
/* 5637 */     Token xsp = this.jj_scanpos;
/* 5638 */     if (jj_scan_token(112)) {
/* 5639 */       this.jj_scanpos = xsp;
/* 5640 */       if (jj_scan_token(113)) {
/* 5641 */         this.jj_scanpos = xsp;
/* 5642 */         if (jj_scan_token(114)) {
/* 5643 */           this.jj_scanpos = xsp;
/* 5644 */           if (jj_scan_token(115)) {
/* 5645 */             this.jj_scanpos = xsp;
/* 5646 */             if (jj_scan_token(116)) {
/* 5647 */               this.jj_scanpos = xsp;
/* 5648 */               if (jj_scan_token(117)) return true; 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 5654 */     if (jj_3R_178()) return true; 
/* 5655 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_171() {
/* 5660 */     Token xsp = this.jj_scanpos;
/* 5661 */     if (jj_scan_token(90)) {
/* 5662 */       this.jj_scanpos = xsp;
/* 5663 */       if (jj_scan_token(95)) return true; 
/*      */     } 
/* 5665 */     if (jj_3R_166()) return true; 
/* 5666 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_170() {
/* 5670 */     if (jj_3R_178()) return true;
/*      */     
/*      */     while (true) {
/* 5673 */       Token xsp = this.jj_scanpos;
/* 5674 */       if (jj_3R_192()) { this.jj_scanpos = xsp;
/*      */         
/* 5676 */         return false; }
/*      */     
/*      */     } 
/*      */   } private final boolean jj_3R_52() {
/* 5680 */     if (jj_3R_96()) return true; 
/* 5681 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_182() {
/* 5686 */     Token xsp = this.jj_scanpos;
/* 5687 */     if (jj_scan_token(84)) {
/* 5688 */       this.jj_scanpos = xsp;
/* 5689 */       if (jj_scan_token(85)) {
/* 5690 */         this.jj_scanpos = xsp;
/* 5691 */         if (jj_scan_token(82)) {
/* 5692 */           this.jj_scanpos = xsp;
/* 5693 */           if (jj_scan_token(83)) {
/* 5694 */             this.jj_scanpos = xsp;
/* 5695 */             if (jj_scan_token(91)) {
/* 5696 */               this.jj_scanpos = xsp;
/* 5697 */               if (jj_scan_token(92)) {
/* 5698 */                 this.jj_scanpos = xsp;
/* 5699 */                 if (jj_scan_token(93)) {
/* 5700 */                   this.jj_scanpos = xsp;
/* 5701 */                   if (jj_scan_token(94)) return true; 
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 5709 */     if (jj_3R_170()) return true; 
/* 5710 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_27() {
/* 5714 */     if (jj_3R_41()) return true; 
/* 5715 */     if (jj_3R_32()) return true; 
/* 5716 */     if (jj_scan_token(69)) return true; 
/* 5717 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_51() {
/* 5721 */     if (jj_3R_95()) return true; 
/* 5722 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_168() {
/* 5726 */     if (jj_3R_170()) return true;
/*      */     
/*      */     while (true) {
/* 5729 */       Token xsp = this.jj_scanpos;
/* 5730 */       if (jj_3R_182()) { this.jj_scanpos = xsp;
/*      */         
/* 5732 */         return false; }
/*      */     
/*      */     } 
/*      */   } private final boolean jj_3R_50() {
/* 5736 */     if (jj_3R_94()) return true; 
/* 5737 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_26() {
/* 5741 */     if (jj_3R_41()) return true; 
/* 5742 */     if (jj_scan_token(69)) return true; 
/* 5743 */     if (jj_3R_43()) return true;
/*      */     
/* 5745 */     Token xsp = this.jj_scanpos;
/* 5746 */     if (jj_3R_44()) this.jj_scanpos = xsp; 
/* 5747 */     if (jj_scan_token(74)) return true; 
/* 5748 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_179() {
/* 5752 */     if (jj_scan_token(35)) return true; 
/* 5753 */     if (jj_3R_32()) return true; 
/* 5754 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_28() {
/* 5758 */     if (jj_3R_45()) return true; 
/* 5759 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_166() {
/* 5763 */     if (jj_3R_168()) return true;
/*      */     
/* 5765 */     Token xsp = this.jj_scanpos;
/* 5766 */     if (jj_3R_179()) this.jj_scanpos = xsp; 
/* 5767 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_25() {
/* 5771 */     if (jj_3R_41()) return true; 
/* 5772 */     if (jj_3R_42()) return true; 
/* 5773 */     if (jj_scan_token(69)) return true; 
/* 5774 */     if (jj_scan_token(72)) return true; 
/* 5775 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_49() {
/* 5779 */     if (jj_3R_93()) return true; 
/* 5780 */     if (jj_scan_token(78)) return true; 
/* 5781 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_24() {
/* 5785 */     if (jj_3R_41()) return true;
/*      */     
/* 5787 */     Token xsp = this.jj_scanpos;
/* 5788 */     if (jj_scan_token(13)) {
/* 5789 */       this.jj_scanpos = xsp;
/* 5790 */       if (jj_scan_token(37)) return true; 
/*      */     } 
/* 5792 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_167() {
/* 5796 */     if (jj_scan_token(110)) return true; 
/* 5797 */     if (jj_3R_161()) return true; 
/* 5798 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_48() {
/* 5802 */     if (jj_3R_92()) return true; 
/* 5803 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_164() {
/* 5807 */     if (jj_3R_166()) return true;
/*      */     
/*      */     while (true) {
/* 5810 */       Token xsp = this.jj_scanpos;
/* 5811 */       if (jj_3R_171()) { this.jj_scanpos = xsp;
/*      */         
/* 5813 */         return false; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean lookingAhead = false;
/*      */   
/*      */   private boolean jj_semLA;
/*      */   
/*      */   private int jj_gen;
/*      */   
/* 5825 */   private final int[] jj_la1 = new int[87]; private static int[] jj_la1_0;
/*      */   private static int[] jj_la1_1;
/*      */   private static int[] jj_la1_2;
/*      */   private static int[] jj_la1_3;
/*      */   private static int[] jj_la1_4;
/*      */   
/*      */   static {
/* 5832 */     jj_la1_0();
/* 5833 */     jj_la1_1();
/* 5834 */     jj_la1_2();
/* 5835 */     jj_la1_3();
/* 5836 */     jj_la1_4();
/*      */   }
/*      */   private static void jj_la1_0() {
/* 5839 */     jj_la1_0 = new int[] { 1, 134218752, 134218752, 8192, 33554432, 0, 541214720, 0, 0, 0, 0, 0, 0, 608323584, 608323584, 0, 0, 541214720, 0, 541214720, 541214720, 541214720, 0, 608323584, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 608323584, 0, 0, 608323584, 67108864, 0, 0, 608323584, 0, 0, 67108864, 0, 0, 0, 67108864, 67108864, 608323584, 0, 0, 0, 0, 0, 610420736, 1074270208, 0, 1081344, 1081344, 8388608, 742542336, 608323584, 608323584, 1073741824, 608323584, 0, 0, 0, 0, 608323584, 65536, 268435456 };
/*      */   }
/*      */   private static void jj_la1_1() {
/* 5842 */     jj_la1_1 = new int[] { 0, 68892800, 68892800, 32, 0, 2, 33587280, 4194304, 0, 65536, 0, 4, 0, 310412112, 310412112, 0, 0, 32848, 0, 32848, 33587280, 32848, 0, 310412112, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 310412112, 0, 0, 310412112, 310379264, 0, 0, 310412112, 0, 0, 310379264, 0, 0, 0, 310379008, 8388608, 310412112, 0, 0, 256, 0, 0, 444891985, 19415040, 66564, 0, 0, 0, 379304912, 310412112, 310412112, 0, 310412112, 0, 0, 0, 0, 310412112, 0, 0 };
/*      */   }
/*      */   private static void jj_la1_2() {
/* 5845 */     jj_la1_2 = new int[] { 0, 0, 0, 0, 0, 0, 32, 0, 17408, 0, 65536, 0, 131072, 12584237, 12584237, 32768, 32768, 32, 32, 32, 32, 0, 32768, 12583213, 131072, 16777216, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2080374784, -2080374784, 0, 2017198080, 2017198080, 0, 0, 0, 0, 0, 0, 0, 12583213, 12582912, 12582912, 301, 12583213, 256, 0, 301, 256, 70656, 269, 32, 256, 70656, 13, 0, 12583213, 32768, 4352, 0, 4096, 4096, 12600621, 0, 16, 0, 0, 0, 12583213, 12583213, 12583213, 0, 12583213, 32768, 32768, 32, 32, 12583213, 0, 0 };
/*      */   }
/*      */   private static void jj_la1_3() {
/* 5848 */     jj_la1_3 = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 240, 240, 0, 0, 0, 0, 0, 0, 0, 0, 240, -675282944, 0, 3, 3, 12, 12, 12288, 12288, 16384, 3072, 3072, 0, 0, 0, 0, 0, 4128768, 4128768, 192, 192, 33536, 33536, 192, 240, 0, 0, 0, 0, 0, 48, 0, 0, 0, 0, 0, 0, 0, 0, 0, 240, 0, 0, 0, 0, 0, 240, 0, 0, 0, 0, 0, 240, 240, 240, 0, 240, 0, 0, 0, 0, 240, 0, 0 };
/*      */   }
/*      */   private static void jj_la1_4() {
/* 5851 */     jj_la1_4 = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 63, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
/*      */   }
/* 5853 */   private final JJCalls[] jj_2_rtns = new JJCalls[31];
/*      */   private boolean jj_rescan = false;
/* 5855 */   private int jj_gc = 0;
/*      */   
/*      */   private final LookaheadSuccess jj_ls;
/*      */   
/*      */   private Vector jj_expentries;
/*      */   
/*      */   private int[] jj_expentry;
/*      */   
/*      */   private int jj_kind;
/*      */   private int[] jj_lasttokens;
/*      */   private int jj_endpos;
/*      */   
/*      */   public void ReInit(InputStream stream) {
/* 5868 */     this.jj_input_stream.ReInit(stream, 1, 1);
/* 5869 */     this.token_source.ReInit(this.jj_input_stream);
/* 5870 */     this.token = new Token();
/* 5871 */     this.jj_ntk = -1;
/* 5872 */     this.jjtree.reset();
/* 5873 */     this.jj_gen = 0; int i;
/* 5874 */     for (i = 0; i < 87; ) { this.jj_la1[i] = -1; i++; }
/* 5875 */      for (i = 0; i < this.jj_2_rtns.length; ) { this.jj_2_rtns[i] = new JJCalls(); i++; }
/*      */   
/*      */   }
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
/*      */   public void ReInit(Reader stream) {
/* 5889 */     this.jj_input_stream.ReInit(stream, 1, 1);
/* 5890 */     this.token_source.ReInit(this.jj_input_stream);
/* 5891 */     this.token = new Token();
/* 5892 */     this.jj_ntk = -1;
/* 5893 */     this.jjtree.reset();
/* 5894 */     this.jj_gen = 0; int i;
/* 5895 */     for (i = 0; i < 87; ) { this.jj_la1[i] = -1; i++; }
/* 5896 */      for (i = 0; i < this.jj_2_rtns.length; ) { this.jj_2_rtns[i] = new JJCalls(); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void ReInit(ParserTokenManager tm) {
/* 5909 */     this.token_source = tm;
/* 5910 */     this.token = new Token();
/* 5911 */     this.jj_ntk = -1;
/* 5912 */     this.jjtree.reset();
/* 5913 */     this.jj_gen = 0; int i;
/* 5914 */     for (i = 0; i < 87; ) { this.jj_la1[i] = -1; i++; }
/* 5915 */      for (i = 0; i < this.jj_2_rtns.length; ) { this.jj_2_rtns[i] = new JJCalls(); i++; }
/*      */   
/*      */   }
/*      */   private final Token jj_consume_token(int kind) throws ParseException {
/*      */     Token oldToken;
/* 5920 */     if ((oldToken = this.token).next != null) { this.token = this.token.next; }
/* 5921 */     else { this.token = this.token.next = this.token_source.getNextToken(); }
/* 5922 */      this.jj_ntk = -1;
/* 5923 */     if (this.token.kind == kind) {
/* 5924 */       this.jj_gen++;
/* 5925 */       if (++this.jj_gc > 100) {
/* 5926 */         this.jj_gc = 0;
/* 5927 */         for (int i = 0; i < this.jj_2_rtns.length; i++) {
/* 5928 */           JJCalls c = this.jj_2_rtns[i];
/* 5929 */           while (c != null) {
/* 5930 */             if (c.gen < this.jj_gen) c.first = null; 
/* 5931 */             c = c.next;
/*      */           } 
/*      */         } 
/*      */       } 
/* 5935 */       return this.token;
/*      */     } 
/* 5937 */     this.token = oldToken;
/* 5938 */     this.jj_kind = kind;
/* 5939 */     throw generateParseException();
/*      */   }
/*      */   private static final class LookaheadSuccess extends Error {
/*      */     private LookaheadSuccess() {} }
/* 5943 */   public Parser(InputStream stream) { this.jj_ls = new LookaheadSuccess();
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
/* 5989 */     this.jj_expentries = new Vector();
/*      */     
/* 5991 */     this.jj_kind = -1;
/* 5992 */     this.jj_lasttokens = new int[100]; this.jj_input_stream = new JavaCharStream(stream, 1, 1); this.token_source = new ParserTokenManager(this.jj_input_stream); this.token = new Token(); this.jj_ntk = -1; this.jj_gen = 0; int i; for (i = 0; i < 87; ) { this.jj_la1[i] = -1; i++; }  for (i = 0; i < this.jj_2_rtns.length; ) { this.jj_2_rtns[i] = new JJCalls(); i++; }  } public Parser(Reader stream) { this.jj_ls = new LookaheadSuccess(); this.jj_expentries = new Vector(); this.jj_kind = -1; this.jj_lasttokens = new int[100]; this.jj_input_stream = new JavaCharStream(stream, 1, 1); this.token_source = new ParserTokenManager(this.jj_input_stream); this.token = new Token(); this.jj_ntk = -1; this.jj_gen = 0; int i; for (i = 0; i < 87; ) { this.jj_la1[i] = -1; i++; }  for (i = 0; i < this.jj_2_rtns.length; ) { this.jj_2_rtns[i] = new JJCalls(); i++; }  } public Parser(ParserTokenManager tm) { this.jj_ls = new LookaheadSuccess(); this.jj_expentries = new Vector(); this.jj_kind = -1; this.jj_lasttokens = new int[100]; this.token_source = tm; this.token = new Token(); this.jj_ntk = -1; this.jj_gen = 0; int i; for (i = 0; i < 87; ) { this.jj_la1[i] = -1; i++; }  for (i = 0; i < this.jj_2_rtns.length; ) { this.jj_2_rtns[i] = new JJCalls(); i++; }  }
/*      */   private final boolean jj_scan_token(int kind) { if (this.jj_scanpos == this.jj_lastpos) { this.jj_la--; if (this.jj_scanpos.next == null) { this.jj_lastpos = this.jj_scanpos = this.jj_scanpos.next = this.token_source.getNextToken(); } else { this.jj_lastpos = this.jj_scanpos = this.jj_scanpos.next; }  } else { this.jj_scanpos = this.jj_scanpos.next; }  if (this.jj_rescan) { int i = 0; Token tok = this.token; while (tok != null && tok != this.jj_scanpos) { i++; tok = tok.next; }  if (tok != null) jj_add_error_token(kind, i);  }  if (this.jj_scanpos.kind != kind)
/*      */       return true;  if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos)
/*      */       throw this.jj_ls;  return false; }
/* 5996 */   public final Token getNextToken() { if (this.token.next != null) { this.token = this.token.next; } else { this.token = this.token.next = this.token_source.getNextToken(); }  this.jj_ntk = -1; this.jj_gen++; return this.token; } private void jj_add_error_token(int kind, int pos) { if (pos >= 100)
/* 5997 */       return;  if (pos == this.jj_endpos + 1)
/* 5998 */     { this.jj_lasttokens[this.jj_endpos++] = kind; }
/* 5999 */     else if (this.jj_endpos != 0)
/* 6000 */     { this.jj_expentry = new int[this.jj_endpos];
/* 6001 */       for (int i = 0; i < this.jj_endpos; i++) {
/* 6002 */         this.jj_expentry[i] = this.jj_lasttokens[i];
/*      */       }
/* 6004 */       boolean exists = false;
/* 6005 */       for (Enumeration<int[]> e = this.jj_expentries.elements(); e.hasMoreElements(); ) {
/* 6006 */         int[] oldentry = e.nextElement();
/* 6007 */         if (oldentry.length == this.jj_expentry.length) {
/* 6008 */           exists = true;
/* 6009 */           for (int j = 0; j < this.jj_expentry.length; j++) {
/* 6010 */             if (oldentry[j] != this.jj_expentry[j]) {
/* 6011 */               exists = false;
/*      */               break;
/*      */             } 
/*      */           } 
/* 6015 */           if (exists)
/*      */             break; 
/*      */         } 
/* 6018 */       }  if (!exists) this.jj_expentries.addElement(this.jj_expentry); 
/* 6019 */       if (pos != 0) this.jj_lasttokens[(this.jj_endpos = pos) - 1] = kind;  }  }
/*      */   public final Token getToken(int index) { Token t = this.lookingAhead ? this.jj_scanpos : this.token; for (int i = 0; i < index; i++) { if (t.next != null) { t = t.next; } else { t = t.next = this.token_source.getNextToken(); }
/*      */        }
/*      */      return t; }
/*      */   private final int jj_ntk() { if ((this.jj_nt = this.token.next) == null)
/* 6024 */       return this.jj_ntk = (this.token.next = this.token_source.getNextToken()).kind;  return this.jj_ntk = this.jj_nt.kind; } public ParseException generateParseException() { this.jj_expentries.removeAllElements();
/* 6025 */     boolean[] la1tokens = new boolean[134]; int i;
/* 6026 */     for (i = 0; i < 134; i++) {
/* 6027 */       la1tokens[i] = false;
/*      */     }
/* 6029 */     if (this.jj_kind >= 0) {
/* 6030 */       la1tokens[this.jj_kind] = true;
/* 6031 */       this.jj_kind = -1;
/*      */     } 
/* 6033 */     for (i = 0; i < 87; i++) {
/* 6034 */       if (this.jj_la1[i] == this.jj_gen) {
/* 6035 */         for (int k = 0; k < 32; k++) {
/* 6036 */           if ((jj_la1_0[i] & 1 << k) != 0) {
/* 6037 */             la1tokens[k] = true;
/*      */           }
/* 6039 */           if ((jj_la1_1[i] & 1 << k) != 0) {
/* 6040 */             la1tokens[32 + k] = true;
/*      */           }
/* 6042 */           if ((jj_la1_2[i] & 1 << k) != 0) {
/* 6043 */             la1tokens[64 + k] = true;
/*      */           }
/* 6045 */           if ((jj_la1_3[i] & 1 << k) != 0) {
/* 6046 */             la1tokens[96 + k] = true;
/*      */           }
/* 6048 */           if ((jj_la1_4[i] & 1 << k) != 0) {
/* 6049 */             la1tokens[128 + k] = true;
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/* 6054 */     for (i = 0; i < 134; i++) {
/* 6055 */       if (la1tokens[i]) {
/* 6056 */         this.jj_expentry = new int[1];
/* 6057 */         this.jj_expentry[0] = i;
/* 6058 */         this.jj_expentries.addElement(this.jj_expentry);
/*      */       } 
/*      */     } 
/* 6061 */     this.jj_endpos = 0;
/* 6062 */     jj_rescan_token();
/* 6063 */     jj_add_error_token(0, 0);
/* 6064 */     int[][] exptokseq = new int[this.jj_expentries.size()][];
/* 6065 */     for (int j = 0; j < this.jj_expentries.size(); j++) {
/* 6066 */       exptokseq[j] = this.jj_expentries.elementAt(j);
/*      */     }
/* 6068 */     return new ParseException(this.token, exptokseq, tokenImage); }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void enable_tracing() {}
/*      */ 
/*      */   
/*      */   public final void disable_tracing() {}
/*      */   
/*      */   private final void jj_rescan_token() {
/* 6078 */     this.jj_rescan = true;
/* 6079 */     for (int i = 0; i < 31; ) {
/* 6080 */       JJCalls p = this.jj_2_rtns[i];
/*      */       while (true)
/* 6082 */       { if (p.gen > this.jj_gen) {
/* 6083 */           this.jj_la = p.arg; this.jj_lastpos = this.jj_scanpos = p.first;
/* 6084 */           switch (i) { case 0:
/* 6085 */               jj_3_1(); break;
/* 6086 */             case 1: jj_3_2(); break;
/* 6087 */             case 2: jj_3_3(); break;
/* 6088 */             case 3: jj_3_4(); break;
/* 6089 */             case 4: jj_3_5(); break;
/* 6090 */             case 5: jj_3_6(); break;
/* 6091 */             case 6: jj_3_7(); break;
/* 6092 */             case 7: jj_3_8(); break;
/* 6093 */             case 8: jj_3_9(); break;
/* 6094 */             case 9: jj_3_10(); break;
/* 6095 */             case 10: jj_3_11(); break;
/* 6096 */             case 11: jj_3_12(); break;
/* 6097 */             case 12: jj_3_13(); break;
/* 6098 */             case 13: jj_3_14(); break;
/* 6099 */             case 14: jj_3_15(); break;
/* 6100 */             case 15: jj_3_16(); break;
/* 6101 */             case 16: jj_3_17(); break;
/* 6102 */             case 17: jj_3_18(); break;
/* 6103 */             case 18: jj_3_19(); break;
/* 6104 */             case 19: jj_3_20(); break;
/* 6105 */             case 20: jj_3_21(); break;
/* 6106 */             case 21: jj_3_22(); break;
/* 6107 */             case 22: jj_3_23(); break;
/* 6108 */             case 23: jj_3_24(); break;
/* 6109 */             case 24: jj_3_25(); break;
/* 6110 */             case 25: jj_3_26(); break;
/* 6111 */             case 26: jj_3_27(); break;
/* 6112 */             case 27: jj_3_28(); break;
/* 6113 */             case 28: jj_3_29(); break;
/* 6114 */             case 29: jj_3_30(); break;
/* 6115 */             case 30: jj_3_31(); break; }
/*      */         
/*      */         } 
/* 6118 */         p = p.next;
/* 6119 */         if (p == null)
/*      */           i++;  } 
/* 6121 */     }  this.jj_rescan = false;
/*      */   }
/*      */   
/*      */   private final void jj_save(int index, int xla) {
/* 6125 */     JJCalls p = this.jj_2_rtns[index];
/* 6126 */     while (p.gen > this.jj_gen) {
/* 6127 */       if (p.next == null) { p = p.next = new JJCalls(); break; }
/* 6128 */        p = p.next;
/*      */     } 
/* 6130 */     p.gen = this.jj_gen + xla - this.jj_la; p.first = this.token; p.arg = xla;
/*      */   }
/*      */   
/*      */   static final class JJCalls {
/*      */     int gen;
/*      */     Token first;
/*      */     int arg;
/*      */     JJCalls next;
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/Parser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */