/*     */ package business.global.redpacket;
/*     */ 
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.feature.PlayerCurrency;
/*     */ import business.player.feature.features.PlayerRecord;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.FetchStatus;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.common.utils.Random;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefRedPacket;
/*     */ import core.database.game.bo.RedPacketBO;
/*     */ import core.database.game.bo.RedPacketPickBO;
/*     */ import core.network.proto.RedPacketInfo;
/*     */ import core.network.proto.RedPacketPickInfo;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RedPacketMgr
/*     */ {
/*  32 */   private static volatile RedPacketMgr instance = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RedPacketMgr getInstance() {
/*  38 */     if (instance == null) {
/*  39 */       synchronized (RedPacketMgr.class) {
/*  40 */         if (instance == null) {
/*  41 */           instance = new RedPacketMgr();
/*     */         }
/*     */       } 
/*     */     }
/*  45 */     return instance;
/*     */   }
/*     */ 
/*     */   
/*  49 */   private Map<Long, RedPacketBO> packetMap = new ConcurrentHashMap<>();
/*     */ 
/*     */   
/*  52 */   private Map<Long, Map<Long, RedPacketPickBO>> pickMap = new ConcurrentHashMap<>();
/*     */   
/*     */   public void init() {
/*  55 */     List<RedPacketBO> packetList = BM.getBM(RedPacketBO.class).findAll();
/*  56 */     List<RedPacketPickBO> pickList = BM.getBM(RedPacketPickBO.class).findAll();
/*     */     
/*  58 */     for (RedPacketBO packetBO : packetList) {
/*  59 */       this.packetMap.put(Long.valueOf(packetBO.getId()), packetBO);
/*     */     }
/*     */     
/*  62 */     for (RedPacketPickBO pickBO : pickList) {
/*  63 */       Map<Long, RedPacketPickBO> map = this.pickMap.get(Long.valueOf(pickBO.getPacketId()));
/*  64 */       if (map == null) {
/*  65 */         map = new HashMap<>();
/*  66 */         this.pickMap.put(Long.valueOf(pickBO.getPacketId()), map);
/*     */       } 
/*  68 */       map.put(Long.valueOf(pickBO.getPid()), pickBO);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleRecharge(int money, Player player) {
/*  75 */     RefRedPacket refpacket = null;
/*  76 */     for (RefRedPacket ref : RefDataMgr.getAll(RefRedPacket.class).values()) {
/*  77 */       if (ref.Range.within(money)) {
/*  78 */         refpacket = ref;
/*     */         break;
/*     */       } 
/*     */     } 
/*  82 */     if (refpacket == null) {
/*     */       return;
/*     */     }
/*     */     
/*  86 */     RedPacketBO bo = new RedPacketBO();
/*  87 */     bo.setPid(player.getPid());
/*  88 */     bo.setMaxMoney(refpacket.Money);
/*  89 */     bo.setLeftMoney(bo.getMaxMoney());
/*  90 */     bo.setMaxPick(refpacket.PickNum);
/*  91 */     bo.setPacketTypeId(refpacket.id);
/*  92 */     bo.setTime(CommTime.nowSecond());
/*  93 */     bo.insert();
/*  94 */     this.packetMap.put(Long.valueOf(bo.getId()), bo);
/*     */     
/*  96 */     for (Player online : PlayerMgr.getInstance().getOnlinePlayers()) {
/*  97 */       online.pushProto("RedPacket", toProtocol(bo, player));
/*     */     }
/*     */   }
/*     */   
/*     */   public RedPacketInfo toProtocol(RedPacketBO bo, Player player) {
/* 102 */     RedPacketInfo info = new RedPacketInfo(bo);
/* 103 */     Map<Long, RedPacketPickBO> pickMap = this.pickMap.get(Long.valueOf(bo.getId()));
/* 104 */     if (pickMap != null && pickMap.get(Long.valueOf(player.getPid())) != null) {
/* 105 */       info.status = FetchStatus.Fetched.ordinal();
/* 106 */     } else if (bo.getAlreadyPick() == bo.getMaxPick()) {
/* 107 */       info.status = FetchStatus.Cannot.ordinal();
/*     */     } else {
/* 109 */       info.status = FetchStatus.Can.ordinal();
/*     */     } 
/*     */     
/* 112 */     return info;
/*     */   }
/*     */   
/*     */   public RedPacketInfo getPacket(long id, Player player) {
/* 116 */     RedPacketBO bo = this.packetMap.get(Long.valueOf(id));
/* 117 */     RedPacketInfo info = null;
/* 118 */     if (bo != null) {
/* 119 */       info = toProtocol(bo, player);
/*     */     }
/* 121 */     return info;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<RedPacketInfo> getList(Player player) {
/* 126 */     List<RedPacketInfo> list = new ArrayList<>();
/* 127 */     List<RedPacketBO> removeList = new ArrayList<>();
/* 128 */     synchronized (this) {
/* 129 */       this.packetMap.values().stream().forEach(x -> {
/*     */             if (x.getLeftMoney() == 0 && x.getTime() + 259200 < CommTime.nowSecond()) {
/*     */               paramList1.add(x);
/*     */             } else {
/*     */               paramList2.add(toProtocol(x, paramPlayer));
/*     */             } 
/*     */           });
/* 136 */       removePacket(removeList);
/*     */     } 
/* 138 */     return list;
/*     */   }
/*     */   
/*     */   public void removePacket(List<RedPacketBO> removeList) {
/* 142 */     for (RedPacketBO bo : removeList) {
/* 143 */       this.packetMap.remove(Long.valueOf(bo.getId()));
/* 144 */       Map<Long, RedPacketPickBO> pick = this.pickMap.get(Long.valueOf(bo.getId()));
/* 145 */       if (pick != null) {
/* 146 */         for (RedPacketPickBO pickBO : pick.values()) {
/* 147 */           pickBO.del();
/*     */         }
/* 149 */         this.pickMap.remove(Long.valueOf(bo.getId()));
/*     */       } 
/* 151 */       bo.del();
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<RedPacketPickInfo> getPickInfo(long id) {
/* 156 */     List<RedPacketPickInfo> list = new ArrayList<>();
/* 157 */     Map<Long, RedPacketPickBO> boMap = this.pickMap.get(Long.valueOf(id));
/* 158 */     if (boMap != null) {
/* 159 */       boMap.values().stream().forEach(x -> paramList.add(new RedPacketPickInfo(x)));
/*     */     }
/*     */ 
/*     */     
/* 163 */     return list;
/*     */   }
/*     */   
/*     */   public int snatchPacket(long id, Player player) throws WSException {
/* 167 */     int times = ((PlayerRecord)player.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.RedPacket);
/*     */     
/* 169 */     if (times >= getMaxTime()) {
/* 170 */       throw new WSException(ErrorCode.Packet_NotExist, "红包今日领取次数已满");
/*     */     }
/*     */     
/* 173 */     synchronized (this) {
/* 174 */       RedPacketBO packetBO = this.packetMap.get(Long.valueOf(id));
/* 175 */       if (packetBO == null) {
/* 176 */         throw new WSException(ErrorCode.Packet_NotExist, "红包不存在");
/*     */       }
/* 178 */       Map<Long, RedPacketPickBO> pickMap = this.pickMap.get(Long.valueOf(id));
/* 179 */       if (pickMap != null && 
/* 180 */         pickMap.get(Long.valueOf(player.getPid())) != null) {
/* 181 */         throw new WSException(ErrorCode.Packet_Picked, "红包已领取");
/*     */       }
/*     */ 
/*     */       
/* 185 */       int leftMoney = packetBO.getLeftMoney();
/* 186 */       int leftpick = packetBO.getMaxPick() - packetBO.getAlreadyPick();
/* 187 */       if (leftpick <= 0) {
/* 188 */         throw new WSException(ErrorCode.Packet_Out, "红包已抢完");
/*     */       }
/* 190 */       int gain = 0;
/*     */       
/* 192 */       if (leftpick == 1) {
/* 193 */         gain = leftMoney;
/*     */       }
/*     */       else {
/*     */         
/* 197 */         int min, max, avg = leftMoney / leftpick;
/* 198 */         if (avg == 0) {
/* 199 */           avg++;
/*     */         }
/* 201 */         int maxavg = avg * 2;
/*     */ 
/*     */         
/* 204 */         if ((leftpick - 1) * maxavg >= leftMoney) {
/* 205 */           min = 1;
/*     */         } else {
/* 207 */           min = leftMoney - (leftpick - 1) * maxavg;
/*     */         } 
/*     */ 
/*     */         
/* 211 */         if (maxavg + (leftpick - 1) * 1 <= leftMoney) {
/* 212 */           max = maxavg;
/*     */         } else {
/* 214 */           max = leftMoney - leftpick - 1;
/*     */         } 
/*     */         
/* 217 */         int rand = Random.nextInt(max) + 1;
/* 218 */         gain = Math.max(min, rand);
/*     */       } 
/* 220 */       packetBO.setLeftMoney(packetBO.getLeftMoney() - gain);
/* 221 */       packetBO.setAlreadyPick(packetBO.getAlreadyPick() + 1);
/* 222 */       packetBO.saveAll();
/* 223 */       RedPacketPickBO bo = new RedPacketPickBO();
/* 224 */       bo.setPid(player.getPid());
/* 225 */       bo.setMoney(gain);
/* 226 */       bo.setPacketId(packetBO.getId());
/* 227 */       bo.setTime(CommTime.nowSecond());
/* 228 */       bo.insert();
/* 229 */       Map<Long, RedPacketPickBO> map = this.pickMap.get(Long.valueOf(packetBO.getId()));
/* 230 */       if (map == null) {
/* 231 */         map = new HashMap<>();
/* 232 */         this.pickMap.put(Long.valueOf(packetBO.getId()), map);
/*     */       } 
/* 234 */       map.put(Long.valueOf(player.getPid()), bo);
/*     */       
/* 236 */       ((PlayerCurrency)player.getFeature(PlayerCurrency.class)).gain(PrizeType.Crystal, gain, ItemFlow.RedPacket);
/* 237 */       ((PlayerRecord)player.getFeature(PlayerRecord.class)).addValue(ConstEnum.DailyRefresh.RedPacket);
/*     */       
/* 239 */       return gain;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getMaxTime() {
/* 244 */     return RefDataMgr.getFactor("RedPacketPickTimes", 10);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/redpacket/RedPacketMgr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */