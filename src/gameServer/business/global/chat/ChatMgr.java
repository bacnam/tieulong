/*     */ package business.global.chat;
/*     */ import BaseCommon.CommLog;
/*     */ import BaseTask.SyncTask.SyncTaskManager;
/*     */ import BaseThread.BaseMutexObject;
/*     */ import business.global.guild.Guild;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.feature.features.PlayerRecord;
/*     */ import business.player.feature.features.RechargeFeature;
/*     */ import business.player.feature.guild.GuildMemberFeature;
/*     */ import business.player.feature.marry.MarryFeature;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import com.zhonglian.server.common.enums.Achievement;
/*     */ import com.zhonglian.server.common.enums.ChatType;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.mgr.sensitive.SensitiveWordMgr;
/*     */ import com.zhonglian.server.common.utils.Lists;
/*     */ import com.zhonglian.server.http.client.IResponseHandler;
/*     */ import com.zhonglian.server.http.server.GMParam;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import com.zhonglian.server.websocket.handler.response.ResponseHandler;
/*     */ import com.zhonglian.server.websocket.handler.response.WebSocketResponse;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.database.game.bo.ChatMessageBO;
/*     */ import core.network.game2world.WorldConnector;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.stream.Collectors;
/*     */ import proto.gameworld.ChatMessage;
/*     */ 
/*     */ public class ChatMgr {
/*     */   private static ChatMgr instance;
/*     */   protected final BaseMutexObject m_mutex;
/*     */   public final Queue<ChatMessageBO> worldChat;
/*     */   public final Map<Long, Queue<ChatMessageBO>> guildChat;
/*     */   public final Map<Long, Queue<ChatMessageBO>> companyChat;
/*     */   public final Map<Long, Queue<ChatMessageBO>> GMChat;
/*     */   public final LinkedList<ChatMessageBO> systemChat;
/*     */   public final Queue<ChatMessageBO> chatQueue;
/*     */   public final Queue<ChatMessage> allWorldChat;
/*     */   private int chatRecordCount;
/*     */   private int systemRecordCount;
/*     */   
/*     */   public static ChatMgr getInstance() {
/*  53 */     if (instance == null) {
/*  54 */       instance = new ChatMgr();
/*     */     }
/*  56 */     return instance;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChatMgr() {
/*  86 */     this.m_mutex = new BaseMutexObject();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  97 */     this.worldChat = new LinkedList<>();
/*     */     
/*  99 */     this.guildChat = Maps.newConcurrentMap();
/*     */     
/* 101 */     this.companyChat = Maps.newConcurrentMap();
/*     */     
/* 103 */     this.GMChat = Maps.newConcurrentMap();
/*     */     
/* 105 */     this.systemChat = new LinkedList<>();
/*     */     
/* 107 */     this.chatQueue = new LinkedList<>();
/*     */     
/* 109 */     this.allWorldChat = new LinkedList<>(); loadChatMessages(); broadCastWorldChat(); this.chatRecordCount = RefDataMgr.getFactor("ChatRecordCount", 30);
/*     */     this.systemRecordCount = RefDataMgr.getFactor("ChatSystemRecordCount", 10);
/*     */   } public void init() { WorldConnector.request("player.LoadChatMessage", new Object(), new ResponseHandler() { public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException { CommLog.info("读取跨服聊天成功");
/*     */             List<ChatMessage> req = (List<ChatMessage>)(new Gson()).fromJson(body, (new TypeToken<List<ChatMessage>>() {  }
/*     */                 ).getType());
/*     */             (ChatMgr.getInstance()).allWorldChat.addAll(req); } public void handleError(WebSocketResponse ssresponse, short statusCode, String message) { CommLog.error("读取跨服聊天失败"); } }
/* 115 */       ); } public boolean loadChatMessages() { List<ChatMessageBO> all = BM.getBM(ChatMessageBO.class).findAll();
/* 116 */     Collections.sort(all, (o1, o2) -> o1.getSendTime() - o2.getSendTime());
/*     */     
/* 118 */     for (ChatMessageBO bo : all) {
/* 119 */       Player player; long guildId; Queue<ChatMessageBO> guildchat; Queue<ChatMessageBO> sender; Queue<ChatMessageBO> company; Queue<ChatMessageBO> senderGM; Queue<ChatMessageBO> companyGM; switch (ChatType.values()[bo.getChatType()]) {
/*     */         case CHATTYPE_WORLD:
/* 121 */           this.worldChat.add(bo);
/*     */         
/*     */         case CHATTYPE_GUILD:
/* 124 */           player = PlayerMgr.getInstance().getPlayer(bo.getSenderCId());
/* 125 */           guildId = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getGuildID();
/* 126 */           guildchat = this.guildChat.get(Long.valueOf(guildId));
/* 127 */           if (guildchat == null) {
/* 128 */             guildchat = Lists.newLinkedList();
/* 129 */             this.guildChat.put(Long.valueOf(guildId), guildchat);
/*     */           } 
/* 131 */           guildchat.add(bo);
/*     */         
/*     */         case CHATTYPE_COMPANY:
/* 134 */           sender = this.companyChat.get(Long.valueOf(bo.getSenderCId()));
/* 135 */           if (sender == null) {
/* 136 */             sender = Lists.newLinkedList();
/* 137 */             this.companyChat.put(Long.valueOf(bo.getSenderCId()), sender);
/*     */           } 
/* 139 */           sender.add(bo);
/*     */           
/* 141 */           company = this.companyChat.get(Long.valueOf(bo.getReceiveCId()));
/* 142 */           if (company == null) {
/* 143 */             company = Lists.newLinkedList();
/* 144 */             this.companyChat.put(Long.valueOf(bo.getReceiveCId()), company);
/*     */           } 
/* 146 */           company.add(bo);
/*     */         
/*     */         case CHATTYPE_SYSTEM:
/* 149 */           this.systemChat.push(bo);
/*     */         
/*     */         case CHATTYPE_GM:
/* 152 */           senderGM = this.companyChat.get(Long.valueOf(bo.getSenderCId()));
/* 153 */           if (senderGM == null) {
/* 154 */             senderGM = Lists.newLinkedList();
/* 155 */             this.companyChat.put(Long.valueOf(bo.getSenderCId()), senderGM);
/*     */           } 
/* 157 */           senderGM.add(bo);
/*     */           
/* 159 */           companyGM = this.companyChat.get(Long.valueOf(bo.getReceiveCId()));
/* 160 */           if (companyGM == null) {
/* 161 */             companyGM = Lists.newLinkedList();
/* 162 */             this.companyChat.put(Long.valueOf(bo.getReceiveCId()), companyGM);
/*     */           } 
/* 164 */           companyGM.add(bo);
/*     */       } 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 170 */     return true; }
/*     */   public void lock() { this.m_mutex.lock(); } public void unlock() {
/*     */     this.m_mutex.unlock();
/*     */   } public boolean broadCastWorldChat() {
/* 174 */     SyncTaskManager.schedule(100, () -> {
/*     */           broadcast();
/*     */           return true;
/*     */         });
/* 178 */     return true;
/*     */   }
/*     */   
/*     */   public int getChatRecordCount() {
/* 182 */     return this.chatRecordCount;
/*     */   }
/*     */   
/*     */   public int getSystemRecordCount() {
/* 186 */     return this.systemRecordCount;
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
/*     */ 
/*     */   
/*     */   public void addChat(Player sender, String content, ChatType type, long toCId) throws WSException {
/* 200 */     ChatMessageBO bo = new ChatMessageBO();
/* 201 */     bo.setReceiveCId(toCId);
/* 202 */     bo.setChatType(type.ordinal());
/* 203 */     if (sender != null)
/* 204 */       bo.setSenderCId(sender.getPid()); 
/* 205 */     bo.setContent(content);
/* 206 */     bo.setSendTime(CommTime.nowSecond());
/* 207 */     bo.insert();
/* 208 */     this.chatQueue.add(bo);
/*     */   }
/*     */ 
/*     */   
/*     */   public void broadcast() {
/*     */     Player player;
/*     */     Guild guild;
/*     */     Player sender, sender1;
/* 216 */     ChatMessageBO bo = this.chatQueue.poll();
/* 217 */     if (bo == null) {
/*     */       return;
/*     */     }
/*     */     
/* 221 */     lock();
/* 222 */     switch (ChatType.values()[bo.getChatType()]) {
/*     */       case CHATTYPE_WORLD:
/* 224 */         worldChatAdd(bo);
/* 225 */         broadcastToAllOnline(bo);
/*     */         break;
/*     */       case CHATTYPE_SYSTEM:
/* 228 */         SystemChatAdd(bo);
/* 229 */         broadcastToAllOnline(bo);
/*     */         break;
/*     */       case CHATTYPE_GUILD:
/* 232 */         player = PlayerMgr.getInstance().getOnlinePlayerByCid(bo.getSenderCId());
/* 233 */         guild = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getGuild();
/* 234 */         if (guild != null) {
/* 235 */           guildChatAdd(guild.getGuildId(), bo);
/* 236 */           guild.broadcast("chat", parseProto(bo));
/*     */         } 
/*     */         break;
/*     */       case CHATTYPE_COMPANY:
/* 240 */         player = PlayerMgr.getInstance().getOnlinePlayerByCid(bo.getReceiveCId());
/*     */         
/* 242 */         if (player != null) {
/* 243 */           player.pushProto("chat", parseProto(bo));
/*     */         }
/* 245 */         sender = PlayerMgr.getInstance().getPlayer(bo.getSenderCId());
/* 246 */         sender.pushProto("chat", parseProto(bo));
/* 247 */         companyChatAdd(bo);
/*     */         break;
/*     */       case CHATTYPE_GM:
/* 250 */         player = PlayerMgr.getInstance().getOnlinePlayerByCid(bo.getReceiveCId());
/* 251 */         sender1 = PlayerMgr.getInstance().getPlayer(bo.getSenderCId());
/*     */         
/* 253 */         if (player != null) {
/* 254 */           player.pushProto("chat", parseProto(bo));
/*     */         }
/* 256 */         if (sender1 != null) {
/* 257 */           sender1.pushProto("chat", parseProto(bo));
/*     */           
/* 259 */           GMParam params = new GMParam();
/* 260 */           params.put("senderId", Long.valueOf(sender1.getPid()));
/* 261 */           params.put("content", bo.getContent());
/* 262 */           params.put("sendTime", Integer.valueOf(bo.getSendTime()));
/* 263 */           params.put("senderName", sender1.getName());
/* 264 */           params.put("vip", Integer.valueOf(sender1.getVipLevel()));
/* 265 */           params.put("totalRecharge", Integer.valueOf(sender1.getPlayerBO().getTotalRecharge() / 10));
/* 266 */           params.put("todayRecharge", Integer.valueOf(((PlayerRecord)sender1.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.DailyRecharge)));
/* 267 */           params.put("serverId", Integer.valueOf(Config.ServerID()));
/* 268 */           params.put("worldId", Integer.getInteger("world_sid", 0));
/*     */           
/* 270 */           String baseurl = System.getProperty("downConfUrl");
/* 271 */           HttpUtils.RequestGM("http://" + baseurl + "/gm/chat!add", params, new IResponseHandler()
/*     */               {
/*     */                 public void compeleted(String response) {
/*     */                   try {
/* 275 */                     JsonObject json = (new JsonParser()).parse(response).getAsJsonObject();
/* 276 */                     if (json.get("state").getAsInt() != 1000) {
/* 277 */                       CommLog.error("发送GM用户信息失败" + json.get("state").getAsInt());
/*     */                     }
/* 279 */                   } catch (Exception exception) {}
/*     */                 }
/*     */ 
/*     */ 
/*     */                 
/*     */                 public void failed(Exception exception) {
/* 285 */                   CommLog.error("发送GM用户信息失败");
/*     */                 }
/*     */               });
/*     */         } 
/* 289 */         GMChatAdd(bo);
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 294 */     unlock();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void broadcastToAllOnline(ChatMessageBO bo) {
/* 304 */     for (Player p : PlayerMgr.getInstance().getOnlinePlayers()) {
/* 305 */       p.pushProto("chat", parseProto(bo));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void SystemChatAdd(ChatMessageBO bo) {
/* 315 */     if (this.systemChat.size() >= getSystemRecordCount()) {
/* 316 */       ChatMessageBO chatMessageBO = this.systemChat.poll();
/* 317 */       chatMessageBO.del();
/*     */     } 
/* 319 */     this.systemChat.add(bo);
/*     */   }
/*     */   
/*     */   private void addChat(Queue<ChatMessageBO> list, ChatMessageBO chat) {
/* 323 */     if (list.size() >= getChatRecordCount()) {
/* 324 */       ChatMessageBO chatMessageBO = list.poll();
/* 325 */       chatMessageBO.del();
/*     */     } 
/* 327 */     list.add(chat);
/*     */   }
/*     */   
/*     */   public void addAllWorldChat(ChatMessage chat) {
/* 331 */     if (this.allWorldChat.size() >= RefDataMgr.getFactor("AllWorldChatRecordCount", 30)) {
/* 332 */       this.allWorldChat.poll();
/*     */     }
/* 334 */     this.allWorldChat.add(chat);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void worldChatAdd(ChatMessageBO bo) {
/* 343 */     addChat(this.worldChat, bo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void guildChatAdd(long guildId, ChatMessageBO bo) {
/* 354 */     Queue<ChatMessageBO> guildchat = this.guildChat.get(Long.valueOf(guildId));
/* 355 */     if (guildchat == null) {
/* 356 */       guildchat = Lists.newLinkedList();
/* 357 */       this.guildChat.put(Long.valueOf(guildId), guildchat);
/*     */     } 
/* 359 */     addChat(guildchat, bo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void companyChatAdd(ChatMessageBO bo) {
/* 369 */     Queue<ChatMessageBO> receiveChat = this.companyChat.get(Long.valueOf(bo.getReceiveCId()));
/* 370 */     if (receiveChat == null) {
/* 371 */       receiveChat = Lists.newLinkedList();
/* 372 */       this.companyChat.put(Long.valueOf(bo.getReceiveCId()), receiveChat);
/*     */     } 
/* 374 */     if (receiveChat.size() >= getChatRecordCount()) {
/* 375 */       ChatMessageBO chatMessageBO = receiveChat.poll();
/* 376 */       tryDel(chatMessageBO);
/*     */     } 
/* 378 */     receiveChat.add(bo);
/*     */ 
/*     */     
/* 381 */     Queue<ChatMessageBO> sendChat = this.companyChat.get(Long.valueOf(bo.getSenderCId()));
/* 382 */     if (sendChat == null) {
/* 383 */       sendChat = Lists.newLinkedList();
/* 384 */       this.companyChat.put(Long.valueOf(bo.getSenderCId()), sendChat);
/*     */     } 
/* 386 */     if (sendChat.size() >= getChatRecordCount()) {
/* 387 */       ChatMessageBO chatMessageBO = sendChat.poll();
/* 388 */       tryDel(chatMessageBO);
/*     */     } 
/* 390 */     sendChat.add(bo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void GMChatAdd(ChatMessageBO bo) {
/* 400 */     Queue<ChatMessageBO> receiveChat = this.GMChat.get(Long.valueOf(bo.getReceiveCId()));
/* 401 */     if (receiveChat == null) {
/* 402 */       receiveChat = Lists.newLinkedList();
/* 403 */       this.GMChat.put(Long.valueOf(bo.getReceiveCId()), receiveChat);
/*     */     } 
/* 405 */     if (receiveChat.size() >= getChatRecordCount()) {
/* 406 */       ChatMessageBO chatMessageBO = receiveChat.poll();
/* 407 */       tryDelGM(chatMessageBO);
/*     */     } 
/* 409 */     receiveChat.add(bo);
/*     */ 
/*     */     
/* 412 */     Queue<ChatMessageBO> sendChat = this.GMChat.get(Long.valueOf(bo.getSenderCId()));
/* 413 */     if (sendChat == null) {
/* 414 */       sendChat = Lists.newLinkedList();
/* 415 */       this.GMChat.put(Long.valueOf(bo.getSenderCId()), sendChat);
/*     */     } 
/* 417 */     if (sendChat.size() >= getChatRecordCount()) {
/* 418 */       ChatMessageBO chatMessageBO = sendChat.poll();
/* 419 */       tryDelGM(chatMessageBO);
/*     */     } 
/* 421 */     sendChat.add(bo);
/*     */   }
/*     */   
/*     */   private void tryDel(ChatMessageBO bo) {
/* 425 */     Queue<ChatMessageBO> sender = this.companyChat.get(Long.valueOf(bo.getSenderCId()));
/* 426 */     Queue<ChatMessageBO> receiver = this.companyChat.get(Long.valueOf(bo.getReceiveCId()));
/* 427 */     if ((sender == null || !sender.contains(bo)) && (receiver == null || !receiver.contains(bo))) {
/* 428 */       bo.del();
/*     */     }
/*     */   }
/*     */   
/*     */   private void tryDelGM(ChatMessageBO bo) {
/* 433 */     Queue<ChatMessageBO> sender = this.GMChat.get(Long.valueOf(bo.getSenderCId()));
/* 434 */     Queue<ChatMessageBO> receiver = this.GMChat.get(Long.valueOf(bo.getReceiveCId()));
/* 435 */     if ((sender == null || !sender.contains(bo)) && (receiver == null || !receiver.contains(bo))) {
/* 436 */       bo.del();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ChatMessage> loadMessage(Player player) {
/* 447 */     List<ChatMessageBO> worldChat = new ArrayList<>(this.worldChat);
/*     */ 
/*     */     
/* 450 */     Guild guild = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getGuild();
/* 451 */     if (guild != null) {
/* 452 */       Queue<ChatMessageBO> guildChat = this.guildChat.get(Long.valueOf(guild.getGuildId()));
/* 453 */       if (guildChat != null) {
/* 454 */         worldChat.addAll(guildChat);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 459 */     if (this.companyChat.get(Long.valueOf(player.getPid())) != null) {
/* 460 */       worldChat.addAll(this.companyChat.get(Long.valueOf(player.getPid())));
/*     */     }
/*     */ 
/*     */     
/* 464 */     if (this.GMChat.get(Long.valueOf(player.getPid())) != null) {
/* 465 */       worldChat.addAll(this.GMChat.get(Long.valueOf(player.getPid())));
/*     */     }
/*     */ 
/*     */     
/* 469 */     worldChat.addAll(this.systemChat);
/* 470 */     List<ChatMessage> list = (List<ChatMessage>)worldChat.stream().sorted((o1, o2) -> o2.getSendTime() - o1.getSendTime())
/*     */       
/* 472 */       .limit(30L).map(this::parseProto).collect(Collectors.toList());
/*     */ 
/*     */     
/* 475 */     list.addAll(this.allWorldChat);
/*     */     
/* 477 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cleanCompanyMessage(Player player) {
/* 486 */     if (this.companyChat.get(Long.valueOf(player.getPid())) == null) {
/*     */       return;
/*     */     }
/* 489 */     lock();
/*     */     try {
/* 491 */       Queue<ChatMessageBO> list = this.companyChat.get(Long.valueOf(player.getPid()));
/* 492 */       for (ChatMessageBO bo : list) {
/* 493 */         bo.del();
/*     */       }
/* 495 */       this.companyChat.remove(Long.valueOf(player.getPid()));
/*     */     } finally {
/* 497 */       unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cleanGuildMessage(long guildId) {
/* 507 */     Queue<ChatMessageBO> guildChat = this.guildChat.remove(Long.valueOf(guildId));
/* 508 */     if (guildChat == null) {
/*     */       return;
/*     */     }
/* 511 */     for (ChatMessageBO chat : guildChat) {
/* 512 */       chat.del();
/*     */     }
/* 514 */     guildChat.clear();
/*     */   }
/*     */   
/*     */   public ChatMessage parseProto(ChatMessageBO bo) {
/* 518 */     ChatMessage builder = new ChatMessage();
/* 519 */     builder.setId(bo.getId());
/*     */     
/* 521 */     ChatType chatType = ChatType.values()[bo.getChatType()];
/* 522 */     builder.setType(chatType);
/* 523 */     builder.setSenderPid(bo.getSenderCId());
/*     */     
/* 525 */     Player player = PlayerMgr.getInstance().getPlayer(bo.getSenderCId());
/* 526 */     if (player != null) {
/* 527 */       builder.setSenderName(player.getPlayerBO().getName());
/* 528 */       builder.setSenderLv(player.getPlayerBO().getLv());
/* 529 */       builder.setSenderVipLv(player.getPlayerBO().getVipLevel());
/* 530 */       builder.setSenderIcon(player.getPlayerBO().getIcon());
/* 531 */       builder.setSenderServerId(player.getSid());
/*     */ 
/*     */       
/* 534 */       builder.setIs_married(((MarryFeature)player.getFeature(MarryFeature.class)).isMarried());
/*     */ 
/*     */       
/* 537 */       RechargeFeature rechargeFeature = (RechargeFeature)player.getFeature(RechargeFeature.class);
/* 538 */       int monthNum = rechargeFeature.getRebateRemains(Achievement.AchievementType.MonthCardCrystal);
/* 539 */       builder.setMonthCard((monthNum > 0));
/* 540 */       int yearNum = rechargeFeature.getRebateRemains(Achievement.AchievementType.YearCardCrystal);
/* 541 */       builder.setYearCard((yearNum == -1));
/*     */     } else {
/* 543 */       builder.setSenderName("GM");
/*     */     } 
/* 545 */     String message = SensitiveWordMgr.getInstance().replaceSensitiveWord(bo.getContent(), 1, "*");
/* 546 */     builder.setMessage(message);
/* 547 */     builder.setContent(bo.getContent());
/* 548 */     builder.setSendTime(bo.getSendTime());
/* 549 */     builder.setReceivePid(bo.getReceiveCId());
/* 550 */     Player receive = PlayerMgr.getInstance().getPlayer(bo.getReceiveCId());
/* 551 */     if (receive != null) {
/* 552 */       builder.setReceiveName(receive.getName());
/*     */     }
/*     */     
/* 555 */     return builder;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/chat/ChatMgr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */