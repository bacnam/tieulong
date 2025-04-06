/*    */ package core.network.client2game;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ 
/*    */ public class ProtoForward
/*    */ {
/*  7 */   public static HashSet<String> ZoneForwardList = new HashSet<>();
/*  8 */   public static HashSet<String> WorldForwardList = new HashSet<>();
/*    */   
/*    */   static {
/* 11 */     ZoneForwardList.add("zforward.client.test");
/*    */     
/* 13 */     WorldForwardList.add("wforward.client.test");
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/ProtoForward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */