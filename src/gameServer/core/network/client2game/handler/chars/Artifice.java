/*     */ package core.network.client2game.handler.chars;
/*     */ 
/*     */ import business.global.activity.ActivityMgr;
/*     */ import business.global.activity.detail.RankArtifice;
/*     */ import business.global.notice.NoticeMgr;
/*     */ import business.global.rank.RankManager;
/*     */ import business.player.Player;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.feature.character.CharFeature;
/*     */ import business.player.feature.character.Character;
/*     */ import com.google.gson.Gson;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.EquipPos;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.common.utils.Random;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefArtifice;
/*     */ import core.config.refdata.ref.RefCharacter;
/*     */ import core.network.client2game.handler.PlayerHandler;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class Artifice
/*     */   extends PlayerHandler
/*     */ {
/*     */   public static class Request {
/*     */     int charId;
/*     */     EquipPos pos;
/*     */   }
/*     */   
/*     */   public static class ArtificeNotify {
/*     */     int charId;
/*     */     EquipPos pos;
/*     */     long Level;
/*     */     
/*     */     public ArtificeNotify(int charId, EquipPos pos, long Level) {
/*  41 */       this.charId = charId;
/*  42 */       this.pos = pos;
/*  43 */       this.Level = Level;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/*  49 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*  50 */     Character character = ((CharFeature)player.getFeature(CharFeature.class)).getCharacter(req.charId);
/*  51 */     if (character == null) {
/*  52 */       throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[] { Integer.valueOf(req.charId) });
/*     */     }
/*     */ 
/*     */     
/*  56 */     int level = character.getBo().getArtifice(req.pos.ordinal());
/*  57 */     if (level >= RefDataMgr.getAll(RefArtifice.class).size() - 1) {
/*  58 */       throw new WSException(ErrorCode.Artifice_Full, "装备炼化等级已满");
/*     */     }
/*     */     
/*  61 */     RefArtifice ref = (RefArtifice)RefDataMgr.get(RefArtifice.class, Integer.valueOf(level + 1));
/*     */ 
/*     */     
/*  64 */     boolean check = ((PlayerItem)player.getFeature(PlayerItem.class)).checkAndConsume(ref.UniformId, ref.UniformCount, ItemFlow.ArtificeEquip);
/*  65 */     if (!check) {
/*  66 */       throw new WSException(ErrorCode.Artifice_NotEnough, "炼化石不足");
/*     */     }
/*     */     
/*  69 */     int rate = 0;
/*  70 */     int MaxLevel = character.getBo().getArtificeMax(req.pos.ordinal());
/*     */     
/*  72 */     if (level < MaxLevel) {
/*  73 */       rate = 10000;
/*     */     }
/*  75 */     else if (ref.Timemin != 0 && character.getBo().getArtificeTimes(req.pos.ordinal()) <= ref.Timemin) {
/*  76 */       rate = 0;
/*  77 */     } else if (ref.TimeMax != 0 && character.getBo().getArtificeTimes(req.pos.ordinal()) >= ref.TimeMax) {
/*  78 */       rate = 100;
/*     */     } else {
/*  80 */       rate = ref.Rate;
/*     */     } 
/*     */     
/*  83 */     if (Random.nextInt(10000) < rate) {
/*  84 */       character.getBo().saveArtifice(req.pos.ordinal(), level + 1);
/*  85 */       if (level + 1 > MaxLevel) {
/*  86 */         character.getBo().saveArtificeMax(req.pos.ordinal(), level + 1);
/*  87 */         character.getBo().saveArtificeTimes(req.pos.ordinal(), 0);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/*  92 */       character.getBo().saveArtifice(req.pos.ordinal(), Math.max(0, level - ref.Star));
/*  93 */       character.getBo().saveArtificeTimes(req.pos.ordinal(), character.getBo().getArtificeTimes(req.pos.ordinal()) + 1);
/*     */     } 
/*     */     
/*  96 */     character.onAttrChanged();
/*     */ 
/*     */     
/*  99 */     int artificeLevel = 0;
/* 100 */     for (Character charac : ((CharFeature)player.getFeature(CharFeature.class)).getAll().values()) {
/* 101 */       for (Iterator<Integer> iterator = charac.getBo().getArtificeAll().iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue();
/* 102 */         artificeLevel += i; }
/*     */     
/*     */     } 
/* 105 */     RankManager.getInstance().update(RankType.Artifice, player.getPid(), artificeLevel);
/*     */ 
/*     */     
/* 108 */     ((RankArtifice)ActivityMgr.getActivity(RankArtifice.class)).UpdateMaxRequire_cost(player, artificeLevel);
/*     */ 
/*     */     
/* 111 */     if (character.getBo().getArtifice(req.pos.ordinal()) >= RefDataMgr.getAll(RefArtifice.class).size() - 1) {
/* 112 */       RefCharacter refc = (RefCharacter)RefDataMgr.get(RefCharacter.class, Integer.valueOf(character.getCharId()));
/*     */       
/* 114 */       String posname = "";
/* 115 */       switch (req.pos) {
/*     */         case Head:
/* 117 */           posname = "头盔";
/*     */           break;
/*     */         case Neck:
/* 120 */           posname = "项链";
/*     */           break;
/*     */         case null:
/* 123 */           posname = "衣服";
/*     */           break;
/*     */         case Weapon:
/* 126 */           posname = "武器";
/*     */           break;
/*     */         case BraceletLeft:
/* 129 */           posname = "左手镯";
/*     */           break;
/*     */         case BraceletRight:
/* 132 */           posname = "右手镯";
/*     */           break;
/*     */         case RingLeft:
/* 135 */           posname = "左戒指";
/*     */           break;
/*     */         case RingRight:
/* 138 */           posname = "右戒指";
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 144 */       NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.Artifice, new String[] { player.getName(), refc.Name, posname });
/*     */     } 
/*     */ 
/*     */     
/* 148 */     ArtificeNotify notify = new ArtificeNotify(character.getCharId(), req.pos, character.getBo().getArtifice(req.pos.ordinal()));
/* 149 */     request.response(notify);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/chars/Artifice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */