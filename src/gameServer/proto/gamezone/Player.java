/*    */ package proto.gamezone;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class Player {
/*    */   public static class PlayerInfo {
/*    */     public long pid;
/*    */     public int icon;
/*    */     public String name;
/*    */     public int lv;
/*    */     public int vipLv;
/*    */     public long guildID;
/*    */     public String guildName;
/*    */     public int serverId;
/*    */     public int maxPower;
/*    */   }
/*    */   
/*    */   public static class G_PlayerInfo {
/*    */     public List<Player.PlayerInfo> players;
/*    */     
/*    */     public G_PlayerInfo(List<Player.PlayerInfo> playerInfos) {
/* 22 */       this.players = playerInfos;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/proto/gamezone/Player.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */