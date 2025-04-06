/*     */ package core.network.client2game.handler.chars;
/*     */ 
/*     */ import business.global.rank.RankManager;
/*     */ import business.player.Player;
/*     */ import business.player.feature.PlayerBase;
/*     */ import business.player.feature.PlayerCurrency;
/*     */ import business.player.feature.character.CharFeature;
/*     */ import business.player.feature.character.Character;
/*     */ import com.google.gson.Gson;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefRebirth;
/*     */ import core.network.client2game.handler.PlayerHandler;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Rebirth
/*     */   extends PlayerHandler
/*     */ {
/*     */   public static class Request
/*     */   {
/*     */     int charId;
/*     */   }
/*     */   
/*     */   private static class WingNotify {
/*     */     int charId;
/*     */     long rebirthLevel;
/*     */     long rebirthExp;
/*     */     long gainExp;
/*     */     
/*     */     private WingNotify(int charId, long rebirthLevel, long rebirthExp, long gainExp) {
/*  37 */       this.charId = charId;
/*  38 */       this.rebirthLevel = rebirthLevel;
/*  39 */       this.rebirthExp = rebirthExp;
/*  40 */       this.gainExp = gainExp;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CharRebirth
/*     */   {
/*     */     int charId;
/*     */     int rebirthLevel;
/*     */     int rebirthExp;
/*     */     
/*     */     private CharRebirth(int charId, int rebirthLevel, int rebirthExp) {
/*  51 */       this.charId = charId;
/*  52 */       this.rebirthLevel = rebirthLevel;
/*  53 */       this.rebirthExp = rebirthExp;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/*  61 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*  62 */     Character character = ((CharFeature)player.getFeature(CharFeature.class)).getCharacter(req.charId);
/*  63 */     if (character == null) {
/*  64 */       throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[] { Integer.valueOf(req.charId) });
/*     */     }
/*     */     
/*  67 */     int nowLevel = character.getBo().getRebirth();
/*  68 */     if (nowLevel >= RefDataMgr.size(RefRebirth.class) - 1) {
/*  69 */       throw new WSException(ErrorCode.Rebirth_LevelFull, "转生已满级");
/*     */     }
/*     */ 
/*     */     
/*  73 */     RefRebirth now_ref = (RefRebirth)RefDataMgr.get(RefRebirth.class, Integer.valueOf(nowLevel));
/*  74 */     RefRebirth next_ref = (RefRebirth)RefDataMgr.get(RefRebirth.class, Integer.valueOf(nowLevel + 1));
/*  75 */     int gainExp = 0;
/*  76 */     if (now_ref.Level != next_ref.Level) {
/*  77 */       for (Character tmpchar : ((CharFeature)player.getFeature(CharFeature.class)).getAll().values()) {
/*  78 */         RefRebirth tmp_ref = (RefRebirth)RefDataMgr.get(RefRebirth.class, Integer.valueOf(tmpchar.getBo().getRebirth()));
/*  79 */         int level = tmp_ref.Level;
/*     */         
/*  81 */         if (tmp_ref.Star != ((List)RefRebirth.sameLevel.get(Integer.valueOf(level))).size()) {
/*  82 */           throw new WSException(ErrorCode.Rebirth_NotEnough, "转生条件不足");
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*  87 */       for (Character char1 : ((CharFeature)player.getFeature(CharFeature.class)).getAll().values()) {
/*  88 */         RefRebirth now_ref1 = (RefRebirth)RefDataMgr.get(RefRebirth.class, Integer.valueOf(char1.getBo().getRebirth()));
/*  89 */         RefRebirth next_ref1 = (RefRebirth)RefDataMgr.get(RefRebirth.class, Integer.valueOf(char1.getBo().getRebirth() + 1));
/*  90 */         int nowExp1 = char1.getBo().getRebirthExp();
/*  91 */         rebirthLevelUp(now_ref1, next_ref1, char1, nowExp1, true);
/*  92 */         player.pushProto("charRebirth", new CharRebirth(char1.getCharId(), char1.getBo().getRebirth(), char1.getBo().getRebirthExp(), null));
/*     */       } 
/*     */ 
/*     */       
/*  96 */       player.getPlayerBO().saveLv(player.getLv() + RefDataMgr.getFactor("RebirthAddLevel", 10));
/*  97 */       ((PlayerBase)player.getFeature(PlayerBase.class)).onLevelUp(player.getPlayerBO().getLv());
/*  98 */       player.pushProperties("lv", player.getPlayerBO().getLv());
/*     */     } else {
/*     */       
/* 101 */       PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 102 */       if (!playerCurrency.check(PrizeType.Exp, next_ref.CostTeamExp)) {
/* 103 */         throw new WSException(ErrorCode.NotEnough_Exp, "玩家经验:%s<升级经验:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getExp()), Integer.valueOf(next_ref.CostTeamExp) });
/*     */       }
/* 105 */       playerCurrency.consume(PrizeType.Exp, next_ref.CostTeamExp, ItemFlow.Rebirth);
/* 106 */       gainExp = next_ref.GainExp * next_ref.getCrit();
/* 107 */       int nowExp = character.getBo().getRebirthExp() + gainExp;
/* 108 */       rebirthLevelUp(now_ref, next_ref, character, nowExp, false);
/*     */     } 
/*     */     
/* 111 */     int newLevel = character.getBo().getRebirth();
/* 112 */     if (newLevel != nowLevel) {
/* 113 */       int rebirthLevel = 0;
/* 114 */       for (Character charac : ((CharFeature)player.getFeature(CharFeature.class)).getAll().values()) {
/* 115 */         rebirthLevel += charac.getBo().getRebirth();
/*     */       }
/*     */       
/* 118 */       character.onAttrChanged();
/* 119 */       RankManager.getInstance().update(RankType.Level, player.getPid(), player.getLv(), new long[] { rebirthLevel });
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     WingNotify notify = new WingNotify(req.charId, character.getBo().getRebirth(), character.getBo().getRebirthExp(), gainExp, null);
/* 127 */     request.response(notify);
/*     */   }
/*     */ 
/*     */   
/*     */   private void rebirthLevelUp(RefRebirth now_ref, RefRebirth next_ref, Character character, int nowExp, boolean isRebirth) {
/* 132 */     int levelUp = 0;
/* 133 */     for (int i = now_ref.id; ((RefRebirth)RefDataMgr.get(RefRebirth.class, Integer.valueOf(i))).Star <= ((List)RefRebirth.sameLevel.get(Integer.valueOf(now_ref.Level))).size(); i++) {
/* 134 */       RefRebirth temp_now_ref = (RefRebirth)RefDataMgr.get(RefRebirth.class, Integer.valueOf(i));
/* 135 */       RefRebirth temp_next_ref = (RefRebirth)RefDataMgr.get(RefRebirth.class, Integer.valueOf(i + 1));
/*     */       
/* 137 */       if (temp_next_ref == null)
/*     */         break; 
/* 139 */       long needExp = temp_next_ref.Exp;
/*     */       
/* 141 */       if (needExp > nowExp) {
/*     */         break;
/*     */       }
/* 144 */       if (temp_now_ref.Level != temp_next_ref.Level && !isRebirth) {
/*     */         break;
/*     */       }
/* 147 */       levelUp++;
/* 148 */       nowExp = (int)(nowExp - needExp);
/*     */     } 
/*     */     
/* 151 */     character.getBo().saveRebirthExp(nowExp);
/* 152 */     character.getBo().saveRebirth(character.getBo().getRebirth() + levelUp);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/chars/Rebirth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */