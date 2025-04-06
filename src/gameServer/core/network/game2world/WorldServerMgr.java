/*    */ package core.network.game2world;
/*    */ 
/*    */ import com.zhonglian.server.common.ServerStatus;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import proto.common.Server;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WorldServerMgr
/*    */ {
/*    */   private static WorldServerMgr instance;
/* 15 */   private Map<Integer, Server.ServerInfo> servers = new ConcurrentHashMap<>();
/*    */   
/*    */   public static WorldServerMgr getInstance() {
/* 18 */     if (instance == null) {
/* 19 */       instance = new WorldServerMgr();
/*    */     }
/* 21 */     return instance;
/*    */   }
/*    */   
/*    */   public void addServer(Server.ServerInfo s) {
/* 25 */     if (s != null) {
/* 26 */       this.servers.put(Integer.valueOf(s.serverId), s);
/*    */     }
/*    */   }
/*    */   
/*    */   public void updateServer(Server.ServerInfo serverInfo) {
/* 31 */     if (serverInfo.status == ServerStatus.Stop.ordinal()) {
/* 32 */       this.servers.remove(Integer.valueOf(serverInfo.serverId));
/* 33 */     } else if (serverInfo.status == ServerStatus.Stop.ordinal()) {
/* 34 */       this.servers.put(Integer.valueOf(serverInfo.serverId), serverInfo);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/game2world/WorldServerMgr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */