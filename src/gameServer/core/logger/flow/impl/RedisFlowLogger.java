/*     */ package core.logger.flow.impl;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.zhonglian.server.common.enums.ChatType;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.logger.flow.redis.RedisFlowMgr;
/*     */ import core.logger.flow.GameFlowLogger;
/*     */ import core.server.ServerConfig;
/*     */ 
/*     */ public class RedisFlowLogger
/*     */   extends GameFlowLogger
/*     */ {
/*  14 */   private Boolean isopen = null;
/*     */   
/*     */   public boolean isOpen() {
/*  17 */     if (this.isopen == null) {
/*  18 */       String ADDR = System.getProperty("Redis.ADDR");
/*  19 */       this.isopen = Boolean.valueOf((ADDR != null && !ADDR.trim().isEmpty()));
/*     */     } 
/*  21 */     return this.isopen.booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void arenaTokenChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
/*  26 */     JsonObject param = new JsonObject();
/*  27 */     param.addProperty("srv", "lncq");
/*  28 */     param.addProperty("type", "arenatoken");
/*  29 */     param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
/*  30 */     param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
/*  31 */     param.addProperty("MoneyType", "竞技币");
/*  32 */     param.addProperty("MoneyNum", Integer.valueOf(num));
/*  33 */     param.addProperty("cur_value", Integer.valueOf(cur_remainder));
/*  34 */     param.addProperty("pre_value", Integer.valueOf(pre_value));
/*  35 */     param.addProperty("Reason", reason);
/*  36 */     param.addProperty("RoleID", Long.valueOf(pid));
/*  37 */     param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
/*  38 */     param.addProperty("vip_level", vip_level);
/*  39 */     param.addProperty("level", level);
/*  40 */     param.addProperty("world", System.getProperty("world_sid", "0"));
/*  41 */     RedisFlowMgr.getInstance().add(param);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void artificeMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
/*  47 */     JsonObject param = new JsonObject();
/*  48 */     param.addProperty("srv", "lncq");
/*  49 */     param.addProperty("type", "artificematerial");
/*  50 */     param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
/*  51 */     param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
/*  52 */     param.addProperty("MoneyType", "炼化石");
/*  53 */     param.addProperty("MoneyNum", Integer.valueOf(num));
/*  54 */     param.addProperty("cur_value", Integer.valueOf(cur_remainder));
/*  55 */     param.addProperty("pre_value", Integer.valueOf(pre_value));
/*  56 */     param.addProperty("Reason", reason);
/*  57 */     param.addProperty("RoleID", Long.valueOf(pid));
/*  58 */     param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
/*  59 */     param.addProperty("vip_level", vip_level);
/*  60 */     param.addProperty("level", level);
/*  61 */     param.addProperty("world", System.getProperty("world_sid", "0"));
/*  62 */     RedisFlowMgr.getInstance().add(param);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void createRoleLog(String openid, long pid, int createTime, String channel, int serverid) {
/*  68 */     JsonObject param = new JsonObject();
/*  69 */     param.addProperty("srv", "lncq");
/*  70 */     param.addProperty("type", "createrole");
/*  71 */     param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
/*  72 */     param.addProperty("openid", openid);
/*  73 */     param.addProperty("pid", Long.valueOf(pid));
/*  74 */     param.addProperty("createTime", Integer.valueOf(CommTime.nowSecond()));
/*  75 */     param.addProperty("channel", channel);
/*  76 */     param.addProperty("serverid", Integer.valueOf(serverid));
/*  77 */     param.addProperty("world", System.getProperty("world_sid", "0"));
/*  78 */     RedisFlowMgr.getInstance().add(param);
/*     */   }
/*     */ 
/*     */   
/*     */   public void crystalChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
/*  83 */     JsonObject param = new JsonObject();
/*  84 */     param.addProperty("srv", "lncq");
/*  85 */     param.addProperty("type", "crystal");
/*  86 */     param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
/*  87 */     param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
/*  88 */     param.addProperty("MoneyType", "元宝");
/*  89 */     param.addProperty("MoneyNum", Integer.valueOf(num));
/*  90 */     param.addProperty("cur_value", Integer.valueOf(cur_remainder));
/*  91 */     param.addProperty("pre_value", Integer.valueOf(pre_value));
/*  92 */     param.addProperty("Reason", reason);
/*  93 */     param.addProperty("RoleID", Long.valueOf(pid));
/*  94 */     param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
/*  95 */     param.addProperty("vip_level", vip_level);
/*  96 */     param.addProperty("level", level);
/*  97 */     param.addProperty("world", System.getProperty("world_sid", "0"));
/*  98 */     RedisFlowMgr.getInstance().add(param);
/*     */   }
/*     */ 
/*     */   
/*     */   public void EquipInstanceMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
/* 103 */     JsonObject param = new JsonObject();
/* 104 */     param.addProperty("srv", "lncq");
/* 105 */     param.addProperty("type", "equipinstancematerial");
/* 106 */     param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
/* 107 */     param.addProperty("MoneyType", "装备副本材料");
/* 108 */     param.addProperty("MoneyNum", Integer.valueOf(num));
/* 109 */     param.addProperty("cur_value", Integer.valueOf(cur_remainder));
/* 110 */     param.addProperty("pre_value", Integer.valueOf(pre_value));
/* 111 */     param.addProperty("Reason", reason);
/* 112 */     param.addProperty("RoleID", Long.valueOf(pid));
/* 113 */     param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
/* 114 */     param.addProperty("vip_level", vip_level);
/* 115 */     param.addProperty("level", level);
/* 116 */     param.addProperty("world", System.getProperty("world_sid", "0"));
/* 117 */     RedisFlowMgr.getInstance().add(param);
/*     */   }
/*     */ 
/*     */   
/*     */   public void expChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
/* 122 */     JsonObject param = new JsonObject();
/* 123 */     param.addProperty("srv", "lncq");
/* 124 */     param.addProperty("type", "exp");
/* 125 */     param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
/* 126 */     param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
/* 127 */     param.addProperty("MoneyType", "经验");
/* 128 */     param.addProperty("MoneyNum", Integer.valueOf(num));
/* 129 */     param.addProperty("cur_value", Integer.valueOf(cur_remainder));
/* 130 */     param.addProperty("pre_value", Integer.valueOf(pre_value));
/* 131 */     param.addProperty("Reason", reason);
/* 132 */     param.addProperty("RoleID", Long.valueOf(pid));
/* 133 */     param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
/* 134 */     param.addProperty("vip_level", vip_level);
/* 135 */     param.addProperty("level", level);
/* 136 */     param.addProperty("world", System.getProperty("world_sid", "0"));
/* 137 */     RedisFlowMgr.getInstance().add(param);
/*     */   }
/*     */ 
/*     */   
/*     */   public void GemInstanceMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
/* 142 */     JsonObject param = new JsonObject();
/* 143 */     param.addProperty("srv", "lncq");
/* 144 */     param.addProperty("type", "geminstancematerial");
/* 145 */     param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
/* 146 */     param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
/* 147 */     param.addProperty("MoneyType", "宝石副本材料");
/* 148 */     param.addProperty("MoneyNum", Integer.valueOf(num));
/* 149 */     param.addProperty("cur_value", Integer.valueOf(cur_remainder));
/* 150 */     param.addProperty("pre_value", Integer.valueOf(pre_value));
/* 151 */     param.addProperty("Reason", reason);
/* 152 */     param.addProperty("RoleID", Long.valueOf(pid));
/* 153 */     param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
/* 154 */     param.addProperty("vip_level", vip_level);
/* 155 */     param.addProperty("level", level);
/* 156 */     param.addProperty("world", System.getProperty("world_sid", "0"));
/* 157 */     RedisFlowMgr.getInstance().add(param);
/*     */   }
/*     */ 
/*     */   
/*     */   public void gemMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
/* 162 */     JsonObject param = new JsonObject();
/* 163 */     param.addProperty("srv", "lncq");
/* 164 */     param.addProperty("type", "gemmaterial");
/* 165 */     param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
/* 166 */     param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
/* 167 */     param.addProperty("MoneyType", "宝石");
/* 168 */     param.addProperty("MoneyNum", Integer.valueOf(num));
/* 169 */     param.addProperty("cur_value", Integer.valueOf(cur_remainder));
/* 170 */     param.addProperty("pre_value", Integer.valueOf(pre_value));
/* 171 */     param.addProperty("Reason", reason);
/* 172 */     param.addProperty("RoleID", Long.valueOf(pid));
/* 173 */     param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
/* 174 */     param.addProperty("vip_level", vip_level);
/* 175 */     param.addProperty("level", level);
/* 176 */     param.addProperty("world", System.getProperty("world_sid", "0"));
/* 177 */     RedisFlowMgr.getInstance().add(param);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void guildDonateChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
/* 183 */     JsonObject param = new JsonObject();
/* 184 */     param.addProperty("srv", "lncq");
/* 185 */     param.addProperty("type", "guilddonate");
/* 186 */     param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
/* 187 */     param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
/* 188 */     param.addProperty("MoneyType", "帮贡");
/* 189 */     param.addProperty("MoneyNum", Integer.valueOf(num));
/* 190 */     param.addProperty("cur_value", Integer.valueOf(cur_remainder));
/* 191 */     param.addProperty("pre_value", Integer.valueOf(pre_value));
/* 192 */     param.addProperty("Reason", reason);
/* 193 */     param.addProperty("RoleID", Long.valueOf(pid));
/* 194 */     param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
/* 195 */     param.addProperty("vip_level", vip_level);
/* 196 */     param.addProperty("level", level);
/* 197 */     param.addProperty("world", System.getProperty("world_sid", "0"));
/* 198 */     RedisFlowMgr.getInstance().add(param);
/*     */   }
/*     */ 
/*     */   
/*     */   public void goldChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
/* 203 */     JsonObject param = new JsonObject();
/* 204 */     param.addProperty("srv", "lncq");
/* 205 */     param.addProperty("type", "gold");
/* 206 */     param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
/* 207 */     param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
/* 208 */     param.addProperty("MoneyType", "金币");
/* 209 */     param.addProperty("MoneyNum", Integer.valueOf(num));
/* 210 */     param.addProperty("cur_value", Integer.valueOf(cur_remainder));
/* 211 */     param.addProperty("pre_value", Integer.valueOf(pre_value));
/* 212 */     param.addProperty("Reason", reason);
/* 213 */     param.addProperty("RoleID", Long.valueOf(pid));
/* 214 */     param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
/* 215 */     param.addProperty("vip_level", vip_level);
/* 216 */     param.addProperty("level", level);
/* 217 */     param.addProperty("world", System.getProperty("world_sid", "0"));
/* 218 */     RedisFlowMgr.getInstance().add(param);
/*     */   }
/*     */ 
/*     */   
/*     */   public void meridianInstanceMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
/* 223 */     JsonObject param = new JsonObject();
/* 224 */     param.addProperty("srv", "lncq");
/* 225 */     param.addProperty("type", "meridianinstancematerial");
/* 226 */     param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
/* 227 */     param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
/* 228 */     param.addProperty("MoneyType", "经脉副本材料");
/* 229 */     param.addProperty("MoneyNum", Integer.valueOf(num));
/* 230 */     param.addProperty("cur_value", Integer.valueOf(cur_remainder));
/* 231 */     param.addProperty("pre_value", Integer.valueOf(pre_value));
/* 232 */     param.addProperty("Reason", reason);
/* 233 */     param.addProperty("RoleID", Long.valueOf(pid));
/* 234 */     param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
/* 235 */     param.addProperty("vip_level", vip_level);
/* 236 */     param.addProperty("level", level);
/* 237 */     param.addProperty("world", System.getProperty("world_sid", "0"));
/* 238 */     RedisFlowMgr.getInstance().add(param);
/*     */   }
/*     */ 
/*     */   
/*     */   public void merMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
/* 243 */     JsonObject param = new JsonObject();
/* 244 */     param.addProperty("srv", "lncq");
/* 245 */     param.addProperty("type", "mermaterial");
/* 246 */     param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
/* 247 */     param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
/* 248 */     param.addProperty("MoneyType", "经脉丹");
/* 249 */     param.addProperty("MoneyNum", Integer.valueOf(num));
/* 250 */     param.addProperty("cur_value", Integer.valueOf(cur_remainder));
/* 251 */     param.addProperty("pre_value", Integer.valueOf(pre_value));
/* 252 */     param.addProperty("Reason", reason);
/* 253 */     param.addProperty("RoleID", Long.valueOf(pid));
/* 254 */     param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
/* 255 */     param.addProperty("vip_level", vip_level);
/* 256 */     param.addProperty("level", level);
/* 257 */     param.addProperty("world", System.getProperty("world_sid", "0"));
/* 258 */     RedisFlowMgr.getInstance().add(param);
/*     */   }
/*     */ 
/*     */   
/*     */   public void redPieceChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
/* 263 */     JsonObject param = new JsonObject();
/* 264 */     param.addProperty("srv", "lncq");
/* 265 */     param.addProperty("type", "redpiece");
/* 266 */     param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
/* 267 */     param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
/* 268 */     param.addProperty("MoneyType", "神装碎片");
/* 269 */     param.addProperty("MoneyNum", Integer.valueOf(num));
/* 270 */     param.addProperty("cur_value", Integer.valueOf(cur_remainder));
/* 271 */     param.addProperty("pre_value", Integer.valueOf(pre_value));
/* 272 */     param.addProperty("Reason", reason);
/* 273 */     param.addProperty("RoleID", Long.valueOf(pid));
/* 274 */     param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
/* 275 */     param.addProperty("vip_level", vip_level);
/* 276 */     param.addProperty("level", level);
/* 277 */     param.addProperty("world", System.getProperty("world_sid", "0"));
/* 278 */     RedisFlowMgr.getInstance().add(param);
/*     */   }
/*     */ 
/*     */   
/*     */   public void starMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
/* 283 */     JsonObject param = new JsonObject();
/* 284 */     param.addProperty("srv", "lncq");
/* 285 */     param.addProperty("type", "starmaterial");
/* 286 */     param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
/* 287 */     param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
/* 288 */     param.addProperty("MoneyType", "升星石");
/* 289 */     param.addProperty("MoneyNum", Integer.valueOf(num));
/* 290 */     param.addProperty("cur_value", Integer.valueOf(cur_remainder));
/* 291 */     param.addProperty("pre_value", Integer.valueOf(pre_value));
/* 292 */     param.addProperty("Reason", reason);
/* 293 */     param.addProperty("RoleID", Long.valueOf(pid));
/* 294 */     param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
/* 295 */     param.addProperty("vip_level", vip_level);
/* 296 */     param.addProperty("level", level);
/* 297 */     param.addProperty("world", System.getProperty("world_sid", "0"));
/* 298 */     RedisFlowMgr.getInstance().add(param);
/*     */   }
/*     */ 
/*     */   
/*     */   public void strengthenMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
/* 303 */     JsonObject param = new JsonObject();
/* 304 */     param.addProperty("srv", "lncq");
/* 305 */     param.addProperty("type", "strengthenmaterial");
/* 306 */     param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
/* 307 */     param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
/* 308 */     param.addProperty("MoneyType", "强化石");
/* 309 */     param.addProperty("MoneyNum", Integer.valueOf(num));
/* 310 */     param.addProperty("cur_value", Integer.valueOf(cur_remainder));
/* 311 */     param.addProperty("pre_value", Integer.valueOf(pre_value));
/* 312 */     param.addProperty("Reason", reason);
/* 313 */     param.addProperty("RoleID", Long.valueOf(pid));
/* 314 */     param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
/* 315 */     param.addProperty("vip_level", vip_level);
/* 316 */     param.addProperty("level", level);
/* 317 */     param.addProperty("world", System.getProperty("world_sid", "0"));
/* 318 */     RedisFlowMgr.getInstance().add(param);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void warspiritTalentMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
/* 324 */     JsonObject param = new JsonObject();
/* 325 */     param.addProperty("srv", "lncq");
/* 326 */     param.addProperty("type", "warspirittalentmaterial");
/* 327 */     param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
/* 328 */     param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
/* 329 */     param.addProperty("MoneyType", "战灵天赋丹");
/* 330 */     param.addProperty("MoneyNum", Integer.valueOf(num));
/* 331 */     param.addProperty("cur_value", Integer.valueOf(cur_remainder));
/* 332 */     param.addProperty("pre_value", Integer.valueOf(pre_value));
/* 333 */     param.addProperty("Reason", reason);
/* 334 */     param.addProperty("RoleID", Long.valueOf(pid));
/* 335 */     param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
/* 336 */     param.addProperty("vip_level", vip_level);
/* 337 */     param.addProperty("level", level);
/* 338 */     param.addProperty("world", System.getProperty("world_sid", "0"));
/* 339 */     RedisFlowMgr.getInstance().add(param);
/*     */   }
/*     */ 
/*     */   
/*     */   public void wingMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
/* 344 */     JsonObject param = new JsonObject();
/* 345 */     param.addProperty("srv", "lncq");
/* 346 */     param.addProperty("type", "wingmaterial");
/* 347 */     param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
/* 348 */     param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
/* 349 */     param.addProperty("MoneyType", "羽毛");
/* 350 */     param.addProperty("MoneyNum", Integer.valueOf(num));
/* 351 */     param.addProperty("cur_value", Integer.valueOf(cur_remainder));
/* 352 */     param.addProperty("pre_value", Integer.valueOf(pre_value));
/* 353 */     param.addProperty("Reason", reason);
/* 354 */     param.addProperty("RoleID", Long.valueOf(pid));
/* 355 */     param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
/* 356 */     param.addProperty("vip_level", vip_level);
/* 357 */     param.addProperty("level", level);
/* 358 */     param.addProperty("world", System.getProperty("world_sid", "0"));
/* 359 */     RedisFlowMgr.getInstance().add(param);
/*     */   }
/*     */ 
/*     */   
/*     */   public void lotteryChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
/* 364 */     JsonObject param = new JsonObject();
/* 365 */     param.addProperty("srv", "lncq");
/* 366 */     param.addProperty("type", "lottery");
/* 367 */     param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
/* 368 */     param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
/* 369 */     param.addProperty("MoneyType", "奖券");
/* 370 */     param.addProperty("MoneyNum", Integer.valueOf(num));
/* 371 */     param.addProperty("cur_value", Integer.valueOf(cur_remainder));
/* 372 */     param.addProperty("pre_value", Integer.valueOf(pre_value));
/* 373 */     param.addProperty("Reason", reason);
/* 374 */     param.addProperty("RoleID", Long.valueOf(pid));
/* 375 */     param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
/* 376 */     param.addProperty("vip_level", vip_level);
/* 377 */     param.addProperty("level", level);
/* 378 */     param.addProperty("world", System.getProperty("world_sid", "0"));
/* 379 */     RedisFlowMgr.getInstance().add(param);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void itemLog(long pid, int vip_level, int level, int itemId, int reason, int num, int cur_remainder, int pre_value, int type) {
/* 385 */     JsonObject param = new JsonObject();
/* 386 */     param.addProperty("srv", "lncq");
/* 387 */     param.addProperty("type", "item");
/* 388 */     param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
/* 389 */     param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
/* 390 */     param.addProperty("MoneyType", "道具");
/* 391 */     param.addProperty("MoneyNum", Integer.valueOf(num));
/* 392 */     param.addProperty("ItemId", Integer.valueOf(itemId));
/* 393 */     param.addProperty("cur_value", Integer.valueOf(cur_remainder));
/* 394 */     param.addProperty("pre_value", Integer.valueOf(pre_value));
/* 395 */     param.addProperty("Reason", reason);
/* 396 */     param.addProperty("RoleID", Long.valueOf(pid));
/* 397 */     param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
/* 398 */     param.addProperty("vip_level", vip_level);
/* 399 */     param.addProperty("level", level);
/* 400 */     param.addProperty("world", System.getProperty("world_sid", "0"));
/* 401 */     RedisFlowMgr.getInstance().add(param);
/*     */   }
/*     */   
/*     */   public static void chatLog(long pid, String content, ChatType type, long toCId) {
/* 405 */     JsonObject param = new JsonObject();
/* 406 */     param.addProperty("srv", "lncq");
/* 407 */     param.addProperty("type", "chat");
/* 408 */     String types = "";
/* 409 */     switch (type) {
/*     */       case CHATTYPE_WORLD:
/* 411 */         types = "world";
/*     */         break;
/*     */       case CHATTYPE_GUILD:
/* 414 */         types = "guild";
/*     */         break;
/*     */       case CHATTYPE_COMPANY:
/* 417 */         types = "company";
/*     */         break;
/*     */       case CHATTYPE_SYSTEM:
/* 420 */         types = "system";
/*     */         break;
/*     */       default:
/* 423 */         types = "none";
/*     */         break;
/*     */     } 
/* 426 */     param.addProperty("gaintype", types);
/* 427 */     param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
/* 428 */     param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
/* 429 */     param.addProperty("world", System.getProperty("world_sid", "0"));
/* 430 */     param.addProperty("senderId", pid);
/* 431 */     param.addProperty("content", content);
/* 432 */     param.addProperty("toPid", toCId);
/*     */     
/* 434 */     RedisFlowMgr.getInstance().add(param);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/logger/flow/impl/RedisFlowLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */