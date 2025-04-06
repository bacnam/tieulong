/*    */ package ch.qos.logback.core.net;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ObjectWriterFactory
/*    */ {
/*    */   public AutoFlushingObjectWriter newAutoFlushingObjectWriter(OutputStream outputStream) throws IOException {
/* 37 */     return new AutoFlushingObjectWriter(new ObjectOutputStream(outputStream), 70);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/net/ObjectWriterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */