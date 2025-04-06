/*    */ package com.mchange.net;
/*    */ 
/*    */ import com.mchange.util.RobustMessageLogger;
/*    */ import java.io.IOException;
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
/*    */ public final class SocketUtils
/*    */ {
/*    */   public static void attemptClose(Socket paramSocket) {
/* 48 */     attemptClose(paramSocket, null);
/*    */   }
/*    */   
/*    */   public static void attemptClose(Socket paramSocket, RobustMessageLogger paramRobustMessageLogger) {
/*    */     try {
/* 53 */       paramSocket.close();
/* 54 */     } catch (IOException iOException) {
/* 55 */       if (paramRobustMessageLogger != null) paramRobustMessageLogger.log(iOException, "IOException trying to close Socket"); 
/* 56 */     } catch (NullPointerException nullPointerException) {
/* 57 */       if (paramRobustMessageLogger != null) paramRobustMessageLogger.log(nullPointerException, "NullPointerException trying to close Socket"); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/net/SocketUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */