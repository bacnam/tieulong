/*     */ package core.network.client2game.handler.chars;
/*     */ 
/*     */ import business.global.activity.ActivityMgr;
/*     */ import business.global.activity.detail.RankWing;
/*     */ import business.global.notice.NoticeMgr;
/*     */ import business.global.rank.RankManager;
/*     */ import business.player.Player;
/*     */ import business.player.feature.PlayerCurrency;
/*     */ import business.player.feature.achievement.AchievementFeature;
/*     */ import business.player.feature.character.CharFeature;
/*     */ import business.player.feature.character.Character;
/*     */ import business.player.feature.player.TitleFeature;
/*     */ import com.google.gson.Gson;
/*     */ import com.zhonglian.server.common.enums.Achievement;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.common.enums.Title;
/*     */ import com.zhonglian.server.common.utils.Random;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefCharacter;
/*     */ import core.config.refdata.ref.RefWing;
/*     */ import core.network.client2game.handler.PlayerHandler;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class WingUp
/*     */   extends PlayerHandler
/*     */ {
/*     */   public static class Request
/*     */   {
/*     */     int charId;
/*     */     String way;
/*     */   }
/*     */   
/*     */   public static class WingNotify
/*     */   {
/*     */     int charId;
/*     */     long wingLevel;
/*     */     long wingExp;
/*     */     long gainExp;
/*     */     
/*     */     public WingNotify(int charId, long wingLevel, long wingExp, long gainExp) {
/*  47 */       this.charId = charId;
/*  48 */       this.wingLevel = wingLevel;
/*  49 */       this.wingExp = wingExp;
/*  50 */       this.gainExp = gainExp;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/*  56 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*  57 */     Character character = ((CharFeature)player.getFeature(CharFeature.class)).getCharacter(req.charId);
/*  58 */     if (character == null) {
/*  59 */       throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[] { Integer.valueOf(req.charId) });
/*     */     }
/*     */     
/*  62 */     int nowLevel = character.getBo().getWing();
/*  63 */     if (nowLevel >= RefDataMgr.size(RefWing.class) - 1) {
/*  64 */       throw new WSException(ErrorCode.Wing_LevelFull, "翅膀已满级");
/*     */     }
/*  66 */     RefWing ref = (RefWing)RefDataMgr.get(RefWing.class, Integer.valueOf(nowLevel + 1));
/*  67 */     RefWing now_ref = (RefWing)RefDataMgr.get(RefWing.class, Integer.valueOf(nowLevel));
/*  68 */     int gainExp = 0;
/*     */     
/*  70 */     if (now_ref.Level != ref.Level) {
/*  71 */       character.getBo().saveWing(nowLevel + 1);
/*  72 */       if (ref.Level >= RefDataMgr.getFactor("WingMarqueeLevel", 5)) {
/*  73 */         RefCharacter refc = (RefCharacter)RefDataMgr.get(RefCharacter.class, Integer.valueOf(character.getCharId()));
/*  74 */         NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.Wing, new String[] { player.getName(), refc.Name, (new StringBuilder(String.valueOf(ref.Level))).toString() });
/*     */       } 
/*     */     } else {
/*     */       
/*  78 */       int need_count = 0;
/*  79 */       int player_count = 0;
/*  80 */       PrizeType prizetype = null;
/*  81 */       float crit = 0.0F;
/*  82 */       int exp = 0;
/*  83 */       ErrorCode error = null;
/*     */       
/*  85 */       if (req.way.equalsIgnoreCase("material")) {
/*  86 */         prizetype = PrizeType.WingMaterial;
/*  87 */         need_count = ref.Material;
/*  88 */         player_count = player.getPlayerBO().getWingMaterial();
/*  89 */         crit = ref.MaterialCrit;
/*  90 */         exp = ref.MaterialExp;
/*  91 */         error = ErrorCode.NotEnough_WingMaterial;
/*     */       }
/*  93 */       else if (req.way.equalsIgnoreCase("gold")) {
/*  94 */         prizetype = PrizeType.Gold;
/*  95 */         need_count = ref.Gold;
/*  96 */         player_count = player.getPlayerBO().getGold();
/*  97 */         crit = ref.GoldCrit;
/*  98 */         exp = ref.GoldExp;
/*  99 */         error = ErrorCode.NotEnough_Money;
/*     */       } else {
/* 101 */         throw new WSException(ErrorCode.Wing_NotChooseWay, "没有选择翅膀升阶方式");
/*     */       } 
/*     */       
/* 104 */       PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 105 */       if (!playerCurrency.check(prizetype, need_count)) {
/* 106 */         throw new WSException(error, "玩家材料:%s<升级材料:%s", new Object[] { Integer.valueOf(player_count), Integer.valueOf(need_count) });
/*     */       }
/* 108 */       playerCurrency.consume(prizetype, need_count, ItemFlow.WingLevelUp);
/* 109 */       if (Random.nextInt(10000) < crit * 10000.0F) {
/* 110 */         gainExp = exp * 2;
/*     */       } else {
/* 112 */         gainExp = exp;
/*     */       } 
/* 114 */       int nowExp = character.getBo().getWingExp() + gainExp;
/* 115 */       int levelUp = 0;
/* 116 */       for (int i = ref.id; i <= RefDataMgr.size(RefWing.class); i++) {
/* 117 */         RefWing temp_ref = (RefWing)RefDataMgr.get(RefWing.class, Integer.valueOf(i - 1));
/* 118 */         RefWing next_ref = (RefWing)RefDataMgr.get(RefWing.class, Integer.valueOf(i));
/*     */         
/* 120 */         if (next_ref == null)
/*     */           break; 
/* 122 */         long needExp = next_ref.Exp;
/*     */         
/* 124 */         if (needExp > nowExp) {
/*     */           break;
/*     */         }
/* 127 */         if (temp_ref.Level != next_ref.Level) {
/*     */           break;
/*     */         }
/* 130 */         levelUp++;
/* 131 */         nowExp = (int)(nowExp - needExp);
/*     */       } 
/*     */       
/* 134 */       character.getBo().saveWingExp(nowExp);
/* 135 */       character.getBo().saveWing(nowLevel + levelUp);
/*     */     } 
/*     */     
/* 138 */     int newLevel = character.getBo().getWing();
/* 139 */     if (newLevel != nowLevel) {
/* 140 */       int wingLevel = 0;
/* 141 */       for (Character charac : ((CharFeature)player.getFeature(CharFeature.class)).getAll().values()) {
/* 142 */         wingLevel += charac.getBo().getWing();
/*     */       }
/*     */       
/* 145 */       character.onAttrChanged();
/*     */       
/* 147 */       RankManager.getInstance().update(RankType.WingLevel, player.getPid(), wingLevel);
/*     */       
/* 149 */       ((RankWing)ActivityMgr.getActivity(RankWing.class)).UpdateMaxRequire_cost(player, wingLevel);
/*     */ 
/*     */       
/* 152 */       ((TitleFeature)player.getFeature(TitleFeature.class)).updateMax(Title.WingLevel, Integer.valueOf(newLevel));
/*     */     } 
/*     */ 
/*     */     
/* 156 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.WingUp);
/* 157 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.WingUp_M1);
/* 158 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.WingUp_M2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 167 */     WingNotify notify = new WingNotify(req.charId, character.getBo().getWing(), character.getBo().getWingExp(), gainExp);
/* 168 */     request.response(notify);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/chars/WingUp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */