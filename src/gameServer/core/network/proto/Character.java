/*    */ package core.network.proto;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class Character {
/*    */   public static class CharInfo {
/*    */     public long sid;
/*    */     public int charId;
/*    */     public int level;
/*    */     public int meridian;
/*    */     public int wing;
/*    */     public int wingExp;
/*    */     public int rebirth;
/*    */     public int rebirthExp;
/* 16 */     public List<Integer> strengthen = new ArrayList<>();
/* 17 */     public List<Integer> gem = new ArrayList<>();
/* 18 */     public List<Integer> star = new ArrayList<>();
/* 19 */     public List<Integer> skill = new ArrayList<>();
/* 20 */     public List<Integer> artifice = new ArrayList<>();
/* 21 */     public List<Integer> artificeMax = new ArrayList<>();
/* 22 */     public List<EquipMessage> equips = new ArrayList<>();
/* 23 */     public List<DressInfo> dresses = new ArrayList<>();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/Character.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */