/*    */ package proto.common;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class Login {
/*    */   public static class G_Login {
/*    */     public int serverId;
/*    */     
/*    */     public G_Login(int server) {
/* 10 */       this.serverId = server;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public static class Z_Login
/*    */   {
/*    */     public List<Server.ServerInfo> servers;
/*    */     
/*    */     public Z_Login(List<Server.ServerInfo> serverlist) {
/* 20 */       this.servers = serverlist;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/proto/common/Login.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */