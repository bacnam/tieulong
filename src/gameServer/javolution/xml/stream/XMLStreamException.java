/*     */ package javolution.xml.stream;
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
/*     */ public class XMLStreamException
/*     */   extends Exception
/*     */ {
/*     */   private Throwable _nested;
/*     */   private Location _location;
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public XMLStreamException() {}
/*     */   
/*     */   public XMLStreamException(String msg) {
/*  42 */     super(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLStreamException(Throwable nested) {
/*  51 */     this._nested = nested;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLStreamException(String msg, Throwable nested) {
/*  61 */     super(msg);
/*  62 */     this._nested = nested;
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
/*     */   public XMLStreamException(String msg, Location location, Throwable nested) {
/*  74 */     super(msg);
/*  75 */     this._nested = nested;
/*  76 */     this._location = location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLStreamException(String msg, Location location) {
/*  87 */     super(msg);
/*  88 */     this._location = location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getNestedException() {
/*  97 */     return this._nested;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Location getLocation() {
/* 107 */     return this._location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 116 */     String msg = super.toString();
/* 117 */     if (this._location != null) {
/* 118 */       msg = msg + " (at line " + this._location.getLineNumber() + ", column " + this._location.getColumnNumber() + ")";
/*     */     }
/*     */     
/* 121 */     if (this._nested != null) {
/* 122 */       msg = msg + " caused by " + this._nested.toString();
/*     */     }
/* 124 */     return msg;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/stream/XMLStreamException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */