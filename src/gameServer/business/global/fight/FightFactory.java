/*    */ package business.global.fight;
/*    */ 
/*    */ import business.global.arena.Competitor;
/*    */ import business.player.Player;
/*    */ import business.player.feature.pvp.DroiyanFeature;
/*    */ 
/*    */ 
/*    */ public class FightFactory
/*    */ {
/*    */   public static DroiyanFight createFight(DroiyanFeature atk, DroiyanFeature def, boolean revenge) {
/* 11 */     return new DroiyanFight(atk, def, revenge);
/*    */   }
/*    */ 
/*    */   
/*    */   public static BossFight createFight(Player player, int level) {
/* 16 */     return new BossFight(player, level);
/*    */   }
/*    */ 
/*    */   
/*    */   public static ArenaFight createFight(Competitor atk, Competitor def) {
/* 21 */     return new ArenaFight(atk, def);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/fight/FightFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */