/*    */ package ch.qos.logback.core.util;
/*    */ 
/*    */ import java.io.Closeable;
/*    */ import java.io.IOException;
/*    */ import java.net.ServerSocket;
/*    */ import java.net.Socket;
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
/*    */ public class CloseUtil
/*    */ {
/*    */   public static void closeQuietly(Closeable closeable) {
/* 33 */     if (closeable == null)
/*    */       return;  try {
/* 35 */       closeable.close();
/*    */     }
/* 37 */     catch (IOException ex) {}
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void closeQuietly(Socket socket) {
/* 47 */     if (socket == null)
/*    */       return;  try {
/* 49 */       socket.close();
/*    */     }
/* 51 */     catch (IOException ex) {}
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void closeQuietly(ServerSocket serverSocket) {
/* 62 */     if (serverSocket == null)
/*    */       return;  try {
/* 64 */       serverSocket.close();
/*    */     }
/* 66 */     catch (IOException ex) {}
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/util/CloseUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */