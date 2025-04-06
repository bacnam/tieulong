/*     */ package core.network.client2game.handler.player;
/*     */ 
/*     */ import business.global.chat.ChatMgr;
/*     */ import business.global.guild.Guild;
/*     */ import business.player.Player;
/*     */ import business.player.feature.PlayerBase;
/*     */ import business.player.feature.achievement.AchievementFeature;
/*     */ import business.player.feature.guild.GuildMemberFeature;
/*     */ import com.google.gson.Gson;
/*     */ import com.zhonglian.server.common.enums.Achievement;
/*     */ import com.zhonglian.server.common.enums.ChatType;
/*     */ import com.zhonglian.server.common.enums.UnlockType;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefUnlockFunction;
/*     */ import core.database.game.bo.ChatMessageBO;
/*     */ import core.network.client2game.handler.PlayerHandler;
/*     */ import core.network.game2world.WorldConnector;
/*     */ import java.io.IOException;
/*     */ import proto.gameworld.ChatMessage;
/*     */ 
/*     */ public class Chat
/*     */   extends PlayerHandler {
/*     */   private class Request {
/*     */     long toPid;
/*     */     String content;
/*     */     ChatType type;
/*     */   }
/*     */   
/*     */   private class WorldRequest {
/*     */     long senderId;
/*     */     long toPid;
/*     */     String content;
/*     */     ChatType type;
/*     */   }
/*     */   
/*     */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/*     */     int cd, chatTime;
/*     */     Guild guild;
/*     */     int cdGM, chatTimeGM, cdall, chatTimeAll;
/*     */     ChatMessageBO bo;
/*     */     ChatMessage worldrequest;
/*  46 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*  47 */     long toCId = req.toPid;
/*  48 */     String content = req.content;
/*  49 */     if (content.isEmpty()) {
/*  50 */       throw new WSException(ErrorCode.InvalidParam, "参数content=%s的内容不能为空", new Object[] { content });
/*     */     }
/*     */     
/*  53 */     int nowSecord = CommTime.nowSecond();
/*     */     
/*  55 */     int expiredTime = player.getPlayerBO().getBannedChatExpiredTime();
/*  56 */     if (expiredTime == -1 || expiredTime > nowSecord) {
/*  57 */       throw new WSException(ErrorCode.Chat_Banned, "禁言中，不能发言");
/*     */     }
/*     */     
/*  60 */     ChatType chatType = req.type;
/*     */     
/*  62 */     if (WorldConnector.getInstance().isConnected() && chatType == ChatType.CHATTYPE_WORLD) {
/*  63 */       chatType = ChatType.CHATTYPE_AllWORlD;
/*     */     }
/*     */     
/*  66 */     PlayerBase playerBase = (PlayerBase)player.getFeature(PlayerBase.class);
/*     */     
/*  68 */     switch (chatType) {
/*     */       case CHATTYPE_WORLD:
/*  70 */         cd = RefDataMgr.getFactor("WorldChatInterval", 2);
/*  71 */         playerBase = (PlayerBase)player.getFeature(PlayerBase.class);
/*  72 */         chatTime = playerBase.chatTime;
/*  73 */         if (chatTime + cd > nowSecord) {
/*  74 */           throw new WSException(ErrorCode.Chat_CdTime, "发言cd中，还剩%s秒才可以发言", new Object[] { Integer.valueOf(cd - nowSecord - chatTime) });
/*     */         }
/*  76 */         playerBase.chatTime = nowSecord;
/*     */         break;
/*     */       case CHATTYPE_GUILD:
/*  79 */         guild = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getGuild();
/*  80 */         if (guild == null) {
/*  81 */           throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*     */         }
/*     */         break;
/*     */       case CHATTYPE_COMPANY:
/*     */         break;
/*     */       case CHATTYPE_GM:
/*  87 */         RefUnlockFunction.checkUnlock(player, UnlockType.GMTalk);
/*  88 */         cdGM = RefDataMgr.getFactor("GMChatInterval", 1);
/*  89 */         chatTimeGM = playerBase.gmChatTime;
/*  90 */         if (chatTimeGM + cdGM > nowSecord) {
/*  91 */           throw new WSException(ErrorCode.Chat_CdTime, "发言cd中，还剩%s秒才可以发言", new Object[] { Integer.valueOf(cdGM - nowSecord - chatTimeGM) });
/*     */         }
/*  93 */         playerBase.gmChatTime = nowSecord;
/*     */         break;
/*     */       case null:
/*  96 */         cdall = RefDataMgr.getFactor("AllWorldChatInterval", 5);
/*  97 */         chatTimeAll = playerBase.worldChatTime;
/*  98 */         if (chatTimeAll + cdall > nowSecord) {
/*  99 */           throw new WSException(ErrorCode.Chat_CdTime, "发言cd中，还剩%s秒才可以发言", new Object[] { Integer.valueOf(cdall - nowSecord - chatTimeAll) });
/*     */         }
/* 101 */         playerBase.worldChatTime = nowSecord;
/*     */         
/* 103 */         bo = new ChatMessageBO();
/* 104 */         bo.setReceiveCId(toCId);
/* 105 */         bo.setChatType(chatType.ordinal());
/* 106 */         bo.setSenderCId(player.getPid());
/* 107 */         bo.setContent(content);
/* 108 */         bo.setSendTime(CommTime.nowSecond());
/* 109 */         worldrequest = ChatMgr.getInstance().parseProto(bo);
/* 110 */         WorldConnector.notifyMessage("player.Chat", worldrequest);
/* 111 */         ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.ChatTimes);
/* 112 */         ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.ChatTimes_M1);
/* 113 */         ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.ChatTimes_M2);
/* 114 */         request.response();
/*     */         return;
/*     */       default:
/* 117 */         throw new WSException(ErrorCode.InvalidParam, "非法的参数chatType=%s", new Object[] { chatType });
/*     */     } 
/* 119 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.ChatTimes);
/* 120 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.ChatTimes_M1);
/* 121 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.ChatTimes_M2);
/*     */     
/* 123 */     ChatMgr.getInstance().addChat(player, content, chatType, toCId);
/*     */     
/* 125 */     request.response();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/Chat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */