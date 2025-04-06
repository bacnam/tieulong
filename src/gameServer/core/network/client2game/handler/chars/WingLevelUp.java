/*     */ package core.network.client2game.handler.chars;
/*     */ 
/*     */ import business.global.activity.ActivityMgr;
/*     */ import business.global.activity.detail.RankWing;
/*     */ import business.global.rank.RankManager;
/*     */ import business.player.Player;
/*     */ import business.player.feature.PlayerCurrency;
/*     */ import business.player.feature.achievement.AchievementFeature;
/*     */ import business.player.feature.character.CharFeature;
/*     */ import business.player.feature.character.Character;
/*     */ import business.player.feature.player.TitleFeature;
/*     */ import com.google.gson.Gson;
/*     */ import com.zhonglian.server.common.enums.Achievement;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.common.enums.Title;
/*     */ import com.zhonglian.server.common.utils.Random;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefWing;
/*     */ import core.network.client2game.handler.PlayerHandler;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class WingLevelUp
/*     */   extends PlayerHandler
/*     */ {
/*     */   public static class Request
/*     */   {
/*     */     int charId;
/*     */     String way;
/*     */     int useCrystal;
/*     */   }
/*     */   
/*     */   public static class WingLevelNotify
/*     */   {
/*     */     int charId;
/*     */     long wingLevel;
/*     */     long wingExp;
/*     */     
/*     */     public WingLevelNotify(int charId, long wingLevel, long wingExp) {
/*  44 */       this.charId = charId;
/*  45 */       this.wingLevel = wingLevel;
/*  46 */       this.wingExp = wingExp;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/*  52 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*  53 */     Character character = ((CharFeature)player.getFeature(CharFeature.class)).getCharacter(req.charId);
/*  54 */     if (character == null) {
/*  55 */       throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[] { Integer.valueOf(req.charId) });
/*     */     }
/*     */     
/*  58 */     if (player.getVipLevel() < RefDataMgr.getFactor("VipUnlockUpWing", 4)) {
/*  59 */       throw new WSException(ErrorCode.NotEnough_VIP, "vip等级不足");
/*     */     }
/*     */ 
/*     */     
/*  63 */     int nowLevel = character.getBo().getWing();
/*  64 */     if (nowLevel >= RefDataMgr.size(RefWing.class) - 1) {
/*  65 */       throw new WSException(ErrorCode.Wing_LevelFull, "翅膀已满级");
/*     */     }
/*     */     
/*  68 */     RefWing now_ref = (RefWing)RefDataMgr.get(RefWing.class, Integer.valueOf(nowLevel));
/*  69 */     int Num = 0;
/*  70 */     int totalMoney = 0;
/*  71 */     int totalCrystal = 0;
/*  72 */     boolean useCrystal = false;
/*  73 */     int finalexp = character.getBo().getWingExp();
/*  74 */     int finallevel = character.getBo().getWing();
/*     */ 
/*     */     
/*  77 */     PrizeType prizetype = null;
/*  78 */     ErrorCode error = null;
/*     */     
/*  80 */     if (req.way.equalsIgnoreCase("material")) {
/*  81 */       prizetype = PrizeType.WingMaterial;
/*  82 */       error = ErrorCode.NotEnough_WingMaterial;
/*  83 */       if (req.useCrystal == 1) {
/*  84 */         useCrystal = true;
/*     */       }
/*     */     }
/*  87 */     else if (req.way.equalsIgnoreCase("gold")) {
/*  88 */       prizetype = PrizeType.Gold;
/*  89 */       error = ErrorCode.NotEnough_Money;
/*     */     } else {
/*  91 */       throw new WSException(ErrorCode.Wing_NotChooseWay, "没有选择翅膀升阶方式");
/*     */     } 
/*     */     
/*     */     while (true) {
/*  95 */       int tmp_nowLevel = finallevel;
/*  96 */       RefWing ref = (RefWing)RefDataMgr.get(RefWing.class, Integer.valueOf(tmp_nowLevel + 1));
/*  97 */       if (ref == null) {
/*     */         break;
/*     */       }
/* 100 */       if (now_ref.Level != ref.Level) {
/* 101 */         finallevel = ref.id;
/*     */         break;
/*     */       } 
/* 104 */       int gainExp = 0;
/*     */       
/* 106 */       int need_count = 0;
/* 107 */       float crit = 0.0F;
/* 108 */       int exp = 0;
/*     */       
/* 110 */       if (req.way.equalsIgnoreCase("material")) {
/* 111 */         need_count = ref.Material;
/* 112 */         crit = ref.MaterialCrit;
/* 113 */         exp = ref.MaterialExp;
/*     */       }
/* 115 */       else if (req.way.equalsIgnoreCase("gold")) {
/* 116 */         need_count = ref.Gold;
/* 117 */         crit = ref.GoldCrit;
/* 118 */         exp = ref.GoldExp;
/*     */       } else {
/* 120 */         throw new WSException(ErrorCode.Wing_NotChooseWay, "没有选择翅膀升阶方式");
/*     */       } 
/*     */       
/* 123 */       totalMoney += need_count;
/*     */       
/* 125 */       PlayerCurrency playerCurrency1 = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 126 */       if (!playerCurrency1.check(prizetype, totalMoney)) {
/* 127 */         if (useCrystal) {
/* 128 */           totalMoney -= need_count;
/* 129 */           totalCrystal += need_count * RefDataMgr.getFactor("WingPrice", 5);
/*     */         } else {
/* 131 */           totalMoney -= need_count;
/*     */           
/*     */           break;
/*     */         } 
/*     */       }
/* 136 */       if (Random.nextInt(10000) < crit * 10000.0F) {
/* 137 */         gainExp = exp * 2;
/*     */       } else {
/* 139 */         gainExp = exp;
/*     */       } 
/* 141 */       int nowExp = finalexp + gainExp;
/* 142 */       int levelUp = 0;
/* 143 */       for (int i = ref.id; i <= RefDataMgr.size(RefWing.class); i++) {
/* 144 */         RefWing temp_ref = (RefWing)RefDataMgr.get(RefWing.class, Integer.valueOf(i - 1));
/* 145 */         RefWing next_ref = (RefWing)RefDataMgr.get(RefWing.class, Integer.valueOf(i));
/*     */         
/* 147 */         if (next_ref == null)
/*     */           break; 
/* 149 */         long needExp = next_ref.Exp;
/*     */         
/* 151 */         if (needExp > nowExp) {
/*     */           break;
/*     */         }
/* 154 */         if (temp_ref.Level != next_ref.Level) {
/*     */           break;
/*     */         }
/* 157 */         levelUp++;
/* 158 */         nowExp = (int)(nowExp - needExp);
/*     */       } 
/* 160 */       finalexp = nowExp;
/* 161 */       finallevel = tmp_nowLevel + levelUp;
/* 162 */       Num++;
/*     */     } 
/* 164 */     PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 165 */     if (!playerCurrency.check(prizetype, totalMoney)) {
/* 166 */       throw new WSException(error, "玩家材料:<升级材料:%s", new Object[] { Integer.valueOf(totalMoney) });
/*     */     }
/* 168 */     if (!playerCurrency.check(PrizeType.Crystal, totalCrystal)) {
/* 169 */       throw new WSException(ErrorCode.NotEnough_Crystal, "元宝不足");
/*     */     }
/* 171 */     if (totalMoney > 0)
/* 172 */       playerCurrency.consume(prizetype, totalMoney, ItemFlow.WingLevelUp); 
/* 173 */     if (totalCrystal > 0) {
/* 174 */       playerCurrency.consume(PrizeType.Crystal, totalCrystal, ItemFlow.WingLevelUp);
/*     */     }
/* 176 */     character.getBo().setWingExp(finalexp);
/* 177 */     character.getBo().setWing(finallevel);
/* 178 */     character.getBo().saveAll();
/*     */     
/* 180 */     int newLevel = character.getBo().getWing();
/* 181 */     if (newLevel != nowLevel) {
/* 182 */       int wingLevel = 0;
/* 183 */       for (Character charac : ((CharFeature)player.getFeature(CharFeature.class)).getAll().values()) {
/* 184 */         wingLevel += charac.getBo().getWing();
/*     */       }
/*     */       
/* 187 */       character.onAttrChanged();
/*     */       
/* 189 */       RankManager.getInstance().update(RankType.WingLevel, player.getPid(), wingLevel);
/*     */       
/* 191 */       ((RankWing)ActivityMgr.getActivity(RankWing.class)).UpdateMaxRequire_cost(player, wingLevel);
/*     */       
/* 193 */       ((TitleFeature)player.getFeature(TitleFeature.class)).updateMax(Title.WingLevel, Integer.valueOf(newLevel));
/*     */     } 
/*     */ 
/*     */     
/* 197 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.WingUp, Integer.valueOf(Num));
/* 198 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.WingUp_M1, Integer.valueOf(Num));
/* 199 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.WingUp_M2, Integer.valueOf(Num));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 208 */     WingLevelNotify notify = new WingLevelNotify(req.charId, character.getBo().getWing(), character.getBo().getWingExp());
/* 209 */     request.response(notify);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/chars/WingLevelUp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */