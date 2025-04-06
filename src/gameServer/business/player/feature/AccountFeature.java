/*    */ package business.player.feature;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.zhonglian.server.common.db.BM;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import core.database.game.bo.AccountBO;
/*    */ import core.logger.flow.FlowLogger;
/*    */ 
/*    */ public class AccountFeature extends Feature {
/*    */   public AccountBO bo;
/*    */   
/*    */   public AccountFeature(Player owner) {
/* 16 */     super(owner);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void loadDB() {
/* 23 */     this.bo = (AccountBO)BM.getBM(AccountBO.class).findOne("open_id", this.player.getOpenId());
/* 24 */     if (this.bo == null) {
/* 25 */       this.bo = new AccountBO();
/* 26 */       this.bo.setOpenId(this.player.getOpenId());
/* 27 */       this.bo.setUsername(this.player.getName());
/* 28 */       this.bo.setCreateTime(this.player.getPlayerBO().getCreateTime());
/* 29 */       this.bo.insert();
/*    */     } 
/*    */   }
/*    */   
/*    */   enum LoginState {
/* 34 */     NONE, CREATE, LOGIN, LOGOUT, CHANGE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AccountBO getAccount() {
/* 43 */     return this.bo;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void updateCreateRole() {
/* 50 */     AccountBO bo = getAccount();
/*    */     
/* 52 */     JsonObject playerJson = PlayerMgr.getInstance().getPlayerJson(bo.getOpenId());
/*    */     try {
/* 54 */       if (playerJson.get("channelId") != null)
/* 55 */         bo.setAdFrom(playerJson.get("channelId").getAsString()); 
/* 56 */     } catch (Exception e) {
/* 57 */       CommLog.warn("发生错误:{}", e.toString());
/*    */     } finally {
/* 59 */       bo.saveAll();
/*    */     } 
/*    */     
/* 62 */     FlowLogger.createRoleLog(bo.getOpenId(), this.player.getPid(), CommTime.nowSecond(), bo.getAdFrom(), this.player.getSid());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/AccountFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */