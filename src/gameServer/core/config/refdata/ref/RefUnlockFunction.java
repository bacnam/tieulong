/*     */ package core.config.refdata.ref;
/*     */ 
/*     */ import business.player.Player;
/*     */ import com.zhonglian.server.common.data.RefContainer;
/*     */ import com.zhonglian.server.common.data.RefField;
/*     */ import com.zhonglian.server.common.enums.UnlockType;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RefUnlockFunction
/*     */   extends RefBaseGame
/*     */ {
/*     */   @RefField(iskey = true)
/*     */   public UnlockType id;
/*     */   public int UnlockLevel;
/*     */   public int UnlockVip;
/*     */   
/*     */   public static void checkUnlock(Player player, UnlockType type) throws WSException {
/*  26 */     RefUnlockFunction ref = (RefUnlockFunction)RefDataMgr.get(RefUnlockFunction.class, type);
/*  27 */     if (ref == null) {
/*     */       return;
/*     */     }
/*  30 */     if (ref.UnlockLevel != 0 && player.getLv() >= ref.UnlockLevel) {
/*     */       return;
/*     */     }
/*  33 */     if (ref.UnlockVip != 0 && player.getVipLevel() >= ref.UnlockVip) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  40 */     throw new WSException(ErrorCode.NotEnough_UnlockCond, "解锁功能%s 需要等级:%s[或]VIP:%s, 当前值%s,%s", new Object[] {
/*  41 */           type, Integer.valueOf(ref.UnlockLevel), Integer.valueOf(ref.UnlockVip), Integer.valueOf(player.getLv()), Integer.valueOf(player.getVipLevel())
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean checkUnlockSave(Player player, UnlockType type) {
/*  52 */     RefUnlockFunction ref = (RefUnlockFunction)RefDataMgr.get(RefUnlockFunction.class, type);
/*  53 */     if (ref == null) {
/*  54 */       return true;
/*     */     }
/*  56 */     if (ref.UnlockLevel != 0 && player.getLv() >= ref.UnlockLevel) {
/*  57 */       return true;
/*     */     }
/*  59 */     if (ref.UnlockVip != 0 && player.getVipLevel() >= ref.UnlockVip) {
/*  60 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  66 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean checkUnlockSave(int teamLevel, int vipLevel, UnlockType type) {
/*  78 */     RefUnlockFunction ref = (RefUnlockFunction)RefDataMgr.get(RefUnlockFunction.class, type);
/*  79 */     if (ref == null) {
/*  80 */       return true;
/*     */     }
/*  82 */     if (ref.UnlockLevel != 0 && teamLevel >= ref.UnlockLevel) {
/*  83 */       return true;
/*     */     }
/*  85 */     if (ref.UnlockVip != 0 && vipLevel >= ref.UnlockVip) {
/*  86 */       return true;
/*     */     }
/*  88 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean Assert() {
/*  97 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean AssertAll(RefContainer<?> all) {
/* 102 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefUnlockFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */