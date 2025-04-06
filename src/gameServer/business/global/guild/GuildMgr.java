/*     */ package business.global.guild;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import BaseTask.SyncTask.SyncTaskManager;
/*     */ import business.global.chat.ChatMgr;
/*     */ import business.global.rank.RankManager;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.feature.guild.GuildMemberFeature;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.GuildJob;
/*     */ import com.zhonglian.server.common.enums.JoinState;
/*     */ import com.zhonglian.server.common.mgr.sensitive.SensitiveWordMgr;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.common.utils.Maps;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefGuildWarCenter;
/*     */ import core.database.game.bo.GuildApplyBO;
/*     */ import core.database.game.bo.GuildBO;
/*     */ import core.database.game.bo.GuildBoardBO;
/*     */ import core.database.game.bo.GuildLogBO;
/*     */ import core.database.game.bo.GuildMemberBO;
/*     */ import core.database.game.bo.GuildwarpuppetBO;
/*     */ import core.server.ServerConfig;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuildMgr
/*     */ {
/*  35 */   private static GuildMgr instance = new GuildMgr();
/*     */   
/*     */   public static GuildMgr getInstance() {
/*  38 */     return instance;
/*     */   }
/*     */   
/*  41 */   public Map<Long, Guild> guildDirId = Maps.newConcurrentHashMap();
/*  42 */   public Map<String, Guild> guildDirName = Maps.newConcurrentHashMap();
/*  43 */   public Map<Long, List<GuildApplyBO>> applysDirCid = Maps.newConcurrentHashMap();
/*     */ 
/*     */   
/*     */   public void init() {
/*  47 */     List<GuildBO> guildBOs = BM.getBM(GuildBO.class).findAll();
/*  48 */     for (GuildBO guildBO : guildBOs) {
/*  49 */       if (!guildBO.getName().matches("s\\d+\\..+")) {
/*  50 */         guildBO.saveName("s" + ServerConfig.ServerID() + "." + guildBO.getName());
/*     */       }
/*  52 */       Guild guild = new Guild(guildBO);
/*  53 */       this.guildDirId.put(Long.valueOf(guildBO.getId()), guild);
/*  54 */       this.guildDirName.put(guildBO.getName(), guild);
/*  55 */       guild.init();
/*     */     } 
/*     */     
/*  58 */     List<GuildApplyBO> applys = BM.getBM(GuildApplyBO.class).findAll();
/*  59 */     for (GuildApplyBO apply : applys) {
/*  60 */       Guild guild = this.guildDirId.get(Long.valueOf(apply.getGuildId()));
/*  61 */       if (guild == null) {
/*  62 */         apply.del();
/*     */         continue;
/*     */       } 
/*  65 */       if (guild.applyDirCid.get(Long.valueOf(apply.getPid())) != null) {
/*  66 */         ((GuildApplyBO)guild.applyDirCid.remove(Long.valueOf(apply.getPid()))).del();
/*     */       }
/*  68 */       guild.applyDirCid.put(Long.valueOf(apply.getPid()), apply);
/*  69 */       List<GuildApplyBO> playerApply = this.applysDirCid.get(Long.valueOf(apply.getPid()));
/*  70 */       if (playerApply == null) {
/*  71 */         this.applysDirCid.put(Long.valueOf(apply.getPid()), playerApply = new ArrayList<>());
/*     */       }
/*  73 */       playerApply.add(apply);
/*     */     } 
/*     */     
/*  76 */     List<GuildMemberBO> memberBOs = BM.getBM(GuildMemberBO.class).findAll();
/*  77 */     for (GuildMemberBO memberBO : memberBOs) {
/*     */       
/*  79 */       if (memberBO.getGuildId() == 0L || memberBO.getGuildId() == -1L) {
/*     */         continue;
/*     */       }
/*  82 */       Guild guild = this.guildDirId.get(Long.valueOf(memberBO.getGuildId()));
/*  83 */       if (guild == null) {
/*  84 */         memberBO.del();
/*     */         continue;
/*     */       } 
/*  87 */       guild.members.add(Long.valueOf(memberBO.getPid()));
/*  88 */       GuildJob job = GuildJob.values()[memberBO.getJob()];
/*  89 */       if (job == null || job == GuildJob.None) {
/*  90 */         job = GuildJob.Member;
/*  91 */         memberBO.saveJob(job.ordinal());
/*     */       } 
/*  93 */       List<Long> jobMembers = guild.membersDirJob.get(job);
/*  94 */       if (jobMembers == null) {
/*  95 */         guild.membersDirJob.put(job, jobMembers = new ArrayList<>());
/*     */       }
/*  97 */       jobMembers.add(Long.valueOf(memberBO.getPid()));
/*  98 */       if (memberBO.getSacrificeStatus() != 0)
/*     */       {
/* 100 */         guild.sacrificeMember.add(Long.valueOf(memberBO.getPid()));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 105 */     List<GuildwarpuppetBO> puppets = BM.getBM(GuildwarpuppetBO.class).findAll();
/* 106 */     for (GuildwarpuppetBO puppet : puppets) {
/* 107 */       Guild guild = this.guildDirId.get(Long.valueOf(puppet.getGuildId()));
/* 108 */       if (guild == null) {
/* 109 */         puppet.del();
/*     */         continue;
/*     */       } 
/* 112 */       if (puppet.getApplyTime() < CommTime.getTodayZeroClockS()) {
/* 113 */         puppet.del();
/*     */         continue;
/*     */       } 
/* 116 */       if (guild.puppets.get(Long.valueOf(puppet.getPid())) != null) {
/* 117 */         ((List<GuildwarpuppetBO>)guild.puppets.get(Long.valueOf(puppet.getPid()))).add(puppet); continue;
/*     */       } 
/* 119 */       List<GuildwarpuppetBO> list = new ArrayList<>();
/* 120 */       list.add(puppet);
/* 121 */       guild.puppets.put(Long.valueOf(puppet.getPid()), list);
/*     */     } 
/*     */ 
/*     */     
/* 125 */     List<GuildBoardBO> boards = BM.getBM(GuildBoardBO.class).findAll();
/* 126 */     for (GuildBoardBO board : boards) {
/* 127 */       Guild guild = this.guildDirId.get(Long.valueOf(board.getGuildId()));
/* 128 */       if (guild == null) {
/* 129 */         board.del();
/*     */         continue;
/*     */       } 
/* 132 */       guild.boards.add(board);
/*     */     } 
/*     */ 
/*     */     
/* 136 */     List<GuildLogBO> logs = BM.getBM(GuildLogBO.class).findAll();
/* 137 */     logs.sort((left, right) -> left.getTime() - right.getTime());
/*     */ 
/*     */     
/* 140 */     for (GuildLogBO log : logs) {
/* 141 */       Guild guild = this.guildDirId.get(Long.valueOf(log.getGuildId()));
/* 142 */       if (guild == null) {
/* 143 */         log.del();
/*     */         continue;
/*     */       } 
/* 146 */       guild.logs.add(log);
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<Guild> getAllGuild() {
/* 151 */     return new ArrayList<>(this.guildDirId.values());
/*     */   }
/*     */   
/*     */   public Guild getGuild(String name) {
/* 155 */     Guild guild = this.guildDirName.get(name);
/* 156 */     if (guild != null) {
/* 157 */       return guild;
/*     */     }
/* 159 */     return null;
/*     */   }
/*     */   
/*     */   public Guild getGuild(long guildid) {
/* 163 */     Guild guild = this.guildDirId.get(Long.valueOf(guildid));
/* 164 */     if (guild != null) {
/* 165 */       return guild;
/*     */     }
/* 167 */     return null;
/*     */   }
/*     */   
/*     */   public Guild getGuildData(long guildId) {
/* 171 */     return this.guildDirId.get(Long.valueOf(guildId));
/*     */   }
/*     */   
/*     */   public void createGuild(String name, int iconId, Player player) {
/* 175 */     GuildBO bo = new GuildBO();
/* 176 */     bo.setName("s" + ServerConfig.ServerID() + "." + name);
/* 177 */     bo.setIcon(iconId);
/* 178 */     bo.setLevel(1);
/* 179 */     bo.setJoinState(JoinState.AccptAll.ordinal());
/* 180 */     bo.setLastLoginTime(CommTime.nowSecond());
/* 181 */     bo.setCreateTime(CommTime.nowSecond());
/* 182 */     bo.insert();
/*     */     
/* 184 */     Guild guild = new Guild(bo);
/* 185 */     guild.members.add(Long.valueOf(player.getPid()));
/* 186 */     guild.membersDirJob.put(GuildJob.President, new ArrayList<>());
/* 187 */     ((List<Long>)guild.membersDirJob.get(GuildJob.President)).add(Long.valueOf(player.getPid()));
/*     */     
/* 189 */     this.guildDirId.put(Long.valueOf(bo.getId()), guild);
/* 190 */     this.guildDirName.put(bo.getName(), guild);
/*     */     
/* 192 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 193 */     GuildMemberBO memberBO = guildMember.getOrCreate();
/* 194 */     memberBO.setGuildId(bo.getId());
/* 195 */     memberBO.setJob(GuildJob.President.ordinal());
/* 196 */     memberBO.setJoinTime(CommTime.nowSecond());
/* 197 */     memberBO.saveAll();
/* 198 */     player.notify2Zone();
/*     */     
/* 200 */     guild.init();
/*     */     
/* 202 */     guild.updatePower();
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
/*     */   public void deleteGuild(long guildId) {
/* 215 */     Guild guild = this.guildDirId.get(Long.valueOf(guildId));
/* 216 */     if (guild == null) {
/*     */       return;
/*     */     }
/* 219 */     this.guildDirName.remove(guild.bo.getName());
/*     */     
/* 221 */     PlayerMgr playerMgr = PlayerMgr.getInstance();
/* 222 */     (new ArrayList(guild.members)).forEach(x -> ((GuildMemberFeature)paramPlayerMgr.getPlayer(x.longValue()).getFeature(GuildMemberFeature.class)).leave());
/*     */ 
/*     */ 
/*     */     
/* 226 */     guild.membersDirJob.clear();
/*     */     
/* 228 */     guild.applyDirCid.values().forEach(x -> {
/*     */           ((List)this.applysDirCid.get(Long.valueOf(x.getPid()))).remove(x);
/*     */           x.del();
/*     */         });
/* 232 */     guild.applyDirCid.clear();
/*     */     
/* 234 */     guild.boards.forEach(x -> x.del());
/*     */ 
/*     */     
/* 237 */     guild.boards.clear();
/*     */     
/* 239 */     ChatMgr.getInstance().cleanGuildMessage(guild.bo.getId());
/*     */     
/* 241 */     guild.bo.del();
/*     */     
/* 243 */     this.guildDirId.remove(Long.valueOf(guildId));
/*     */ 
/*     */     
/* 246 */     GuildWarMgr mgr = GuildWarMgr.getInstance();
/* 247 */     for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
/* 248 */       mgr.guildWarCenterOwner.remove(Integer.valueOf(ref.id), guild);
/* 249 */       ((List)mgr.guildWarApplyer.get(Integer.valueOf(ref.id))).remove(guild);
/* 250 */       ((List)mgr.guildWarAtk.get(Integer.valueOf(ref.id))).remove(guild);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 256 */     RankManager.getInstance().clearGuildDataById(guildId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rename(String oldname, String newname) {
/* 261 */     Guild guild = this.guildDirName.remove(oldname);
/* 262 */     if (guild != null) {
/* 263 */       this.guildDirName.put(newname, guild);
/*     */     }
/*     */   }
/*     */   
/*     */   public List<GuildApplyBO> getApplyByCid(long cid) {
/* 268 */     List<GuildApplyBO> find = this.applysDirCid.get(Long.valueOf(cid));
/* 269 */     if (find != null) {
/* 270 */       return this.applysDirCid.get(Long.valueOf(cid));
/*     */     }
/* 272 */     return new ArrayList<>();
/*     */   }
/*     */ 
/*     */   
/*     */   public void apply(long cid, long guildId) {
/* 277 */     Guild guildL = this.guildDirId.get(Long.valueOf(guildId));
/* 278 */     if (guildL == null) {
/* 279 */       CommLog.warn("玩家[{}]企图加入一个不存在的公会[{}]", Long.valueOf(cid), Long.valueOf(guildId));
/*     */       return;
/*     */     } 
/* 282 */     List<GuildApplyBO> applysCid = this.applysDirCid.get(Long.valueOf(cid));
/* 283 */     if (applysCid == null) {
/* 284 */       this.applysDirCid.put(Long.valueOf(cid), applysCid = new ArrayList<>());
/*     */     }
/* 286 */     GuildApplyBO find = null;
/* 287 */     for (GuildApplyBO applyBo : applysCid) {
/* 288 */       if (applyBo.getGuildId() == guildId) {
/* 289 */         find = applyBo;
/*     */       }
/*     */     } 
/* 292 */     if (find != null) {
/* 293 */       find.saveApplyTime(CommTime.nowSecond());
/*     */     } else {
/* 295 */       GuildApplyBO newApply = new GuildApplyBO();
/* 296 */       newApply.setPid(cid);
/* 297 */       newApply.setGuildId(guildId);
/* 298 */       newApply.setApplyTime(CommTime.nowSecond());
/* 299 */       newApply.insert();
/* 300 */       applysCid.add(newApply);
/* 301 */       find = guildL.applyDirCid.get(Long.valueOf(cid));
/* 302 */       if (find != null && find.getId() != newApply.getId()) {
/* 303 */         find.del();
/*     */       }
/* 305 */       guildL.applyDirCid.put(Long.valueOf(cid), newApply);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void removeApply(long cid) {
/* 310 */     List<GuildApplyBO> list = this.applysDirCid.remove(Long.valueOf(cid));
/* 311 */     if (list == null) {
/*     */       return;
/*     */     }
/*     */     
/* 315 */     for (GuildApplyBO applyBO : list) {
/* 316 */       Guild guild = this.guildDirId.get(Long.valueOf(applyBO.getGuildId()));
/* 317 */       if (guild != null) {
/* 318 */         guild.applyDirCid.remove(Long.valueOf(applyBO.getPid()));
/*     */       }
/* 320 */       applyBO.del();
/*     */     } 
/* 322 */     list.clear();
/*     */   }
/*     */   
/*     */   public void removeApply(long cid, long guildid) {
/* 326 */     Guild guild = this.guildDirId.get(Long.valueOf(guildid));
/* 327 */     if (guild != null) {
/* 328 */       guild.applyDirCid.remove(Long.valueOf(cid));
/*     */     }
/*     */     
/* 331 */     List<GuildApplyBO> list = this.applysDirCid.get(Long.valueOf(cid));
/* 332 */     if (list != null) {
/* 333 */       for (GuildApplyBO bo : list) {
/* 334 */         if (bo.getGuildId() == guildid) {
/* 335 */           bo.del();
/*     */         }
/*     */       } 
/* 338 */       list.removeIf(x -> (x.getGuildId() == paramLong));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void CheckNameValid(String name, long operator) throws WSException {
/* 345 */     if (SensitiveWordMgr.getInstance().isContainsSensitiveWord(name)) {
/* 346 */       throw new WSException(ErrorCode.Dirty_Word, "玩家[%s]帮会名[%s]使用敏感词", new Object[] { Long.valueOf(operator), name });
/*     */     }
/* 348 */     int maxLength = RefDataMgr.getFactor("Guild_NameMaxLength", 6);
/* 349 */     if (name.length() > maxLength) {
/* 350 */       throw new WSException(ErrorCode.Dirty_Word, "玩家[%s]帮会名[%s]超过最大长度[%s]", new Object[] { Long.valueOf(operator), name, Integer.valueOf(maxLength) });
/*     */     }
/* 352 */     int minLength = RefDataMgr.getFactor("Guild_NameMinLength", 2);
/* 353 */     if (name.length() < minLength) {
/* 354 */       throw new WSException(ErrorCode.Dirty_Word, "玩家[%s]帮会名[%s]超过最小长度[%s]", new Object[] { Long.valueOf(operator), name, Integer.valueOf(minLength) });
/*     */     }
/* 356 */     String tmp_name = "s" + ServerConfig.ServerID() + "." + name;
/* 357 */     Guild duplicate = getInstance().getGuild(tmp_name);
/* 358 */     if (duplicate != null) {
/* 359 */       throw new WSException(ErrorCode.Guild_AlreadyExist, "此名称已被使用，请重新命名");
/*     */     }
/*     */   }
/*     */   
/*     */   public static void CheckNoteValid(String notice, long operator) throws WSException {
/* 364 */     if (SensitiveWordMgr.getInstance().isContainsSensitiveWord(notice)) {
/* 365 */       throw new WSException(ErrorCode.Dirty_Word, "玩家[%s]帮会公告/宣言[%s]使用敏感词", new Object[] { Long.valueOf(operator), notice });
/*     */     }
/* 367 */     int maxLength = RefDataMgr.getFactor("Guild_NoticeMaxLength", 20);
/* 368 */     if (notice.length() > maxLength) {
/* 369 */       throw new WSException(ErrorCode.Dirty_Word, "玩家[%s]帮会公告/宣言[%s]超过最大长度[%s]", new Object[] { Long.valueOf(operator), notice, Integer.valueOf(maxLength) });
/*     */     }
/* 371 */     int minLength = RefDataMgr.getFactor("Guild_NoticeMinLength", 2);
/* 372 */     if (notice.length() < minLength) {
/* 373 */       throw new WSException(ErrorCode.Dirty_Word, "玩家[%s]帮会公告/宣言[%s]超过最小长度[%s]", new Object[] { Long.valueOf(operator), notice, Integer.valueOf(minLength) });
/*     */     }
/*     */   }
/*     */   
/*     */   public void dailyEvent() {
/* 378 */     List<Guild> guilds = new ArrayList<>(this.guildDirId.values());
/* 379 */     for (Guild guild : guilds) {
/*     */       try {
/* 381 */         SyncTaskManager.task(() -> paramGuild.dailyEvent());
/*     */       
/*     */       }
/* 384 */       catch (Exception e) {
/*     */         
/* 386 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void longnvEvent() {
/* 392 */     List<Guild> guilds = new ArrayList<>(this.guildDirId.values());
/* 393 */     for (Guild guild : guilds) {
/*     */       try {
/* 395 */         SyncTaskManager.task(() -> paramGuild.getOrCreateLongnv().Start());
/*     */       
/*     */       }
/* 398 */       catch (Exception e) {
/*     */         
/* 400 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/guild/GuildMgr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */