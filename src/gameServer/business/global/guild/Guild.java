/*      */ package business.global.guild;
/*      */ 
/*      */ import BaseCommon.CommClass;
/*      */ import BaseCommon.CommLog;
/*      */ import BaseThread.BaseMutexManager;
/*      */ import business.global.activity.ActivityMgr;
/*      */ import business.global.activity.detail.RankGuild;
/*      */ import business.global.gmmail.MailCenter;
/*      */ import business.global.guild.rank.catagere.GuildNormalRank;
/*      */ import business.global.rank.RankManager;
/*      */ import business.player.Player;
/*      */ import business.player.PlayerMgr;
/*      */ import business.player.feature.PlayerBase;
/*      */ import business.player.feature.PlayerCurrency;
/*      */ import business.player.feature.character.CharFeature;
/*      */ import business.player.feature.features.PlayerRecord;
/*      */ import business.player.feature.guild.GuildMemberFeature;
/*      */ import com.zhonglian.server.common.db.BM;
/*      */ import com.zhonglian.server.common.enums.ConstEnum;
/*      */ import com.zhonglian.server.common.enums.GuildJob;
/*      */ import com.zhonglian.server.common.enums.GuildRankType;
/*      */ import com.zhonglian.server.common.enums.JoinState;
/*      */ import com.zhonglian.server.common.enums.PrizeType;
/*      */ import com.zhonglian.server.common.enums.RankType;
/*      */ import com.zhonglian.server.common.utils.CommTime;
/*      */ import com.zhonglian.server.common.utils.Lists;
/*      */ import com.zhonglian.server.common.utils.Maps;
/*      */ import com.zhonglian.server.logger.flow.ItemFlow;
/*      */ import com.zhonglian.server.websocket.def.ErrorCode;
/*      */ import com.zhonglian.server.websocket.exception.WSException;
/*      */ import core.config.refdata.RefDataMgr;
/*      */ import core.config.refdata.ref.RefCrystalPrice;
/*      */ import core.config.refdata.ref.RefGuildBoss;
/*      */ import core.config.refdata.ref.RefGuildLevel;
/*      */ import core.config.refdata.ref.RefGuildWarCenter;
/*      */ import core.config.refdata.ref.RefLongnvLevel;
/*      */ import core.config.refdata.ref.RefMonster;
/*      */ import core.database.game.bo.GuildApplyBO;
/*      */ import core.database.game.bo.GuildBO;
/*      */ import core.database.game.bo.GuildBoardBO;
/*      */ import core.database.game.bo.GuildBossBO;
/*      */ import core.database.game.bo.GuildLogBO;
/*      */ import core.database.game.bo.GuildMemberBO;
/*      */ import core.database.game.bo.GuildRankRecordBO;
/*      */ import core.database.game.bo.GuildwarapplyBO;
/*      */ import core.database.game.bo.GuildwarpuppetBO;
/*      */ import core.network.proto.Player;
/*      */ import core.server.OpenSeverTime;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Guild
/*      */ {
/*      */   public final GuildBO bo;
/*   69 */   public final List<Long> members = Lists.newArrayList();
/*   70 */   public final Map<GuildJob, List<Long>> membersDirJob = Maps.newConcurrentHashMap();
/*      */   
/*   72 */   public final Map<Long, GuildApplyBO> applyDirCid = Maps.newConcurrentHashMap();
/*      */   
/*   74 */   public List<GuildBoardBO> boards = Lists.newArrayList();
/*      */   
/*   76 */   public Queue<GuildLogBO> logs = Lists.newLinkedList();
/*      */   
/*   78 */   public Set<Long> sacrificeMember = Lists.newHashSet();
/*      */   
/*      */   public int rank;
/*      */   
/*   82 */   public List<GuildBossBO> guildboss = Lists.newArrayList();
/*      */   
/*   84 */   public Map<GuildBossBO, List<Player>> joinPlayers = new HashMap<>();
/*      */ 
/*      */   
/*      */   public GuildwarapplyBO guildwarCenter;
/*      */   
/*   89 */   public Map<Long, List<GuildwarpuppetBO>> puppets = Maps.newConcurrentHashMap();
/*      */ 
/*      */   
/*      */   LongnvWar longnv;
/*      */ 
/*      */   
/*   95 */   protected final BaseMutexManager m_mutex = new BaseMutexManager(); private Integer power; private Map<GuildRankType, GuildRank> ranks;
/*      */   
/*      */   public void lock() {
/*   98 */     this.m_mutex.lock();
/*      */   }
/*      */   
/*      */   public void unlock() {
/*  102 */     this.m_mutex.unlock();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public GuildBO getBo() {
/*  110 */     return this.bo;
/*      */   }
/*      */   
/*      */   public long getGuildId() {
/*  114 */     return this.bo.getId();
/*      */   }
/*      */   
/*      */   public String getName() {
/*  118 */     return this.bo.getName();
/*      */   }
/*      */   
/*      */   public LongnvWar getOrCreateLongnv() {
/*  122 */     LongnvWar longnv = this.longnv;
/*  123 */     if (longnv != null) {
/*  124 */       return longnv;
/*      */     }
/*  126 */     synchronized (this) {
/*  127 */       longnv = this.longnv;
/*  128 */       if (longnv != null) {
/*  129 */         return longnv;
/*      */       }
/*  131 */       longnv = new LongnvWar(this);
/*  132 */       this.longnv = longnv;
/*      */     } 
/*  134 */     return longnv;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int gainExp(int guildExp) {
/*  140 */     RefGuildLevel nextLeveLinfo = (RefGuildLevel)RefDataMgr.get(RefGuildLevel.class, Integer.valueOf(getLevel() + 1));
/*  141 */     if (nextLeveLinfo == null) {
/*  142 */       return 0;
/*      */     }
/*  144 */     this.bo.saveExp(this.bo.getExp() + guildExp);
/*  145 */     RefGuildLevel levelinfo = (RefGuildLevel)RefDataMgr.get(RefGuildLevel.class, Integer.valueOf(getLevel()));
/*  146 */     GuildBO bo = getBo();
/*  147 */     if (bo.getExp() < levelinfo.UpgradeValue) {
/*  148 */       return guildExp;
/*      */     }
/*  150 */     bo.setExp(bo.getExp() - levelinfo.UpgradeValue);
/*  151 */     bo.setLevel(bo.getLevel() + 1);
/*  152 */     if (RefDataMgr.get(RefGuildLevel.class, Integer.valueOf(getLevel() + 1)) == null) {
/*  153 */       bo.setExp(0);
/*      */     }
/*  155 */     bo.saveAll();
/*      */     
/*  157 */     broadcast("guildLevelUp", new levelUpInfo(bo.getLevel(), bo.getExp(), null));
/*  158 */     return guildExp;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int gainLongnvExp(int Exp) {
/*  165 */     RefLongnvLevel nextLeveLinfo = (RefLongnvLevel)RefDataMgr.get(RefLongnvLevel.class, Integer.valueOf(getLongnvLevel() + 1));
/*  166 */     if (nextLeveLinfo == null) {
/*  167 */       return 0;
/*      */     }
/*  169 */     this.bo.saveLnexp(this.bo.getLnexp() + Exp);
/*  170 */     RefLongnvLevel levelinfo = (RefLongnvLevel)RefDataMgr.get(RefLongnvLevel.class, Integer.valueOf(getLongnvLevel()));
/*  171 */     GuildBO bo = getBo();
/*  172 */     if (bo.getLnexp() < levelinfo.UpgradeValue) {
/*  173 */       return Exp;
/*      */     }
/*  175 */     bo.setLnexp(bo.getLnexp() - levelinfo.UpgradeValue);
/*  176 */     bo.setLnlevel(bo.getLnlevel() + 1);
/*  177 */     if (RefDataMgr.get(RefLongnvLevel.class, Integer.valueOf(getLongnvLevel() + 1)) == null) {
/*  178 */       bo.setLnexp(0);
/*      */     }
/*  180 */     bo.saveAll();
/*      */     
/*  182 */     broadcast("guildLongnvLevelUp", new levelUpInfo(bo.getLnlevel(), bo.getLnexp(), null));
/*  183 */     return Exp;
/*      */   }
/*      */ 
/*      */   
/*      */   private class levelUpInfo
/*      */   {
/*      */     int level;
/*      */     int exp;
/*      */     
/*      */     private levelUpInfo(int level, int exp) {
/*  193 */       this.level = level;
/*  194 */       this.exp = exp;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public int getLevel() {
/*  200 */     return this.bo.getLevel();
/*      */   }
/*      */   
/*      */   public int getLongnvLevel() {
/*  204 */     return this.bo.getLnlevel();
/*      */   }
/*      */   
/*      */   public JoinState getJoinState() {
/*  208 */     return JoinState.values()[this.bo.getJoinState()];
/*      */   }
/*      */   
/*      */   public int getLevelLimit() {
/*  212 */     return this.bo.getLevelLimit();
/*      */   }
/*      */   
/*      */   public void setLastLoginTime(int current) {
/*  216 */     this.bo.saveLastLoginTime(current);
/*      */   }
/*      */   
/*      */   public List<Long> getMembers() {
/*  220 */     return Lists.newArrayList(this.members);
/*      */   }
/*      */   
/*      */   public int getMemberCount(GuildJob job) {
/*  224 */     return getMember(job).size();
/*      */   }
/*      */   
/*      */   public List<Long> getMember(GuildJob job) {
/*  228 */     List<Long> find = this.membersDirJob.get(job);
/*  229 */     if (find == null) {
/*  230 */       this.membersDirJob.put(job, find = new ArrayList<>());
/*      */     }
/*  232 */     return find;
/*      */   }
/*      */   
/*      */   public int getMemberCnt() {
/*  236 */     return this.members.size();
/*      */   }
/*      */   
/*      */   public Collection<GuildApplyBO> getApplies() {
/*  240 */     return this.applyDirCid.values();
/*      */   }
/*      */   
/*      */   public GuildApplyBO getApply(long cid) {
/*  244 */     return this.applyDirCid.get(Long.valueOf(cid));
/*      */   }
/*      */   
/*      */   public List<GuildBoardBO> getBoardList() {
/*  248 */     return this.boards;
/*      */   }
/*      */   
/*      */   public int getSacrificeCount() {
/*  252 */     return this.sacrificeMember.size();
/*      */   }
/*      */   
/*      */   public void incSacrificeCount(long cid) {
/*  256 */     this.sacrificeMember.add(Long.valueOf(cid));
/*      */   }
/*      */   
/*      */   public List<GuildBossBO> getbosslist() {
/*  260 */     return this.guildboss;
/*      */   }
/*      */   
/*      */   public GuildBossBO getboss(int bossId) {
/*  264 */     for (GuildBossBO bo : this.guildboss) {
/*  265 */       if (bo.getBossId() == bossId) {
/*  266 */         return bo;
/*      */       }
/*      */     } 
/*  269 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void broadcast(String protoName, Object proto, long cid) {
/*  280 */     PlayerMgr mgr = PlayerMgr.getInstance();
/*  281 */     for (Long member : new ArrayList(this.members)) {
/*  282 */       if (cid != member.longValue()) {
/*  283 */         mgr.getPlayer(member.longValue()).pushProto(protoName, proto);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void broadcast(String protoName, Object proto) {
/*  294 */     PlayerMgr mgr = PlayerMgr.getInstance();
/*  295 */     for (Long member : new ArrayList(this.members)) {
/*  296 */       mgr.getPlayer(member.longValue()).pushProto(protoName, proto);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void broadcastMember(long... cids) {
/*  306 */     PlayerMgr playerMgr = PlayerMgr.getInstance();
/*  307 */     List<core.network.proto.Guild.member> members = Lists.newArrayList(); byte b; int i; long[] arrayOfLong;
/*  308 */     for (i = (arrayOfLong = cids).length, b = 0; b < i; ) { long cid = arrayOfLong[b];
/*  309 */       if (this.members.contains(Long.valueOf(cid)))
/*      */       {
/*      */         
/*  312 */         members.add(((GuildMemberFeature)playerMgr.getPlayer(cid).getFeature(GuildMemberFeature.class)).memberInfo()); }  b++; }
/*      */     
/*  314 */     broadcast("memberInfo", members);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<GuildMemberFeature> getAllMemberFeatures() {
/*  323 */     List<GuildMemberFeature> members = Lists.newArrayList();
/*  324 */     PlayerMgr playerMgr = PlayerMgr.getInstance();
/*  325 */     for (Long memberid : new ArrayList(this.members)) {
/*  326 */       members.add((GuildMemberFeature)playerMgr.getPlayer(memberid.longValue()).getFeature(GuildMemberFeature.class));
/*      */     }
/*  328 */     return members;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public GuildMemberFeature getMember(long cid) {
/*  338 */     if (!this.members.contains(Long.valueOf(cid))) {
/*  339 */       return null;
/*      */     }
/*  341 */     return (GuildMemberFeature)PlayerMgr.getInstance().getPlayer(cid).getFeature(GuildMemberFeature.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void takeinMember(Player player) {
/*  350 */     this.members.add(Long.valueOf(player.getPid()));
/*  351 */     getMember(GuildJob.Member).add(Long.valueOf(player.getPid()));
/*      */     
/*  353 */     GuildMemberBO memberBO = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getOrCreate();
/*  354 */     memberBO.setGuildId(getGuildId());
/*  355 */     memberBO.setJob(GuildJob.Member.ordinal());
/*  356 */     memberBO.setJoinTime(CommTime.nowSecond());
/*  357 */     memberBO.saveAll();
/*  358 */     player.notify2Zone();
/*      */ 
/*      */     
/*  361 */     GuildMgr.getInstance().removeApply(player.getPid());
/*  362 */     broadcastMember(new long[] { player.getPid() });
/*      */     
/*  364 */     player.pushProto("guildSkill", ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getGuildSkillList());
/*      */ 
/*      */     
/*  367 */     ((CharFeature)player.getFeature(CharFeature.class)).updateCharPower();
/*  368 */     updatePower(player.getPlayerBO().getMaxFightPower());
/*  369 */     MailCenter.getInstance().sendMail(player.getPid(), GuildConfig.JoinGuildMailID(), new String[] { this.bo.getName() });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeMember(Player player) {
/*  378 */     this.members.remove(Long.valueOf(player.getPid()));
/*  379 */     this.membersDirJob.remove(((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getJob());
/*  380 */     this.sacrificeMember.remove(Long.valueOf(player.getPid()));
/*      */     
/*  382 */     updatePower(-player.getPlayerBO().getMaxFightPower());
/*  383 */     ((CharFeature)player.getFeature(CharFeature.class)).updateCharPower();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addBoardMessage(GuildBoardBO bo) {
/*  392 */     int maxCount = GuildConfig.MaxBoardMessageCount();
/*  393 */     List<GuildBoardBO> bList = new ArrayList<>(this.boards);
/*  394 */     bList.add(bo);
/*  395 */     bList.sort((left, right) -> (left.getTop() == right.getTop()) ? (right.getMessageTime() - left.getMessageTime()) : (left.getTop() ? -1 : 1));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  406 */     for (int i = maxCount; i < bList.size(); i++) {
/*  407 */       ((GuildBoardBO)bList.get(i)).del();
/*      */     }
/*  409 */     this.boards = bList.subList(0, Math.min(bList.size(), maxCount));
/*      */   }
/*      */   
/*      */   public void dailyEvent() {
/*  413 */     List<GuildMemberFeature> members = getAllMemberFeatures();
/*      */     
/*  415 */     promotePresident(members);
/*      */     
/*  417 */     this.sacrificeMember.clear();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  423 */     for (GuildBossBO boss : this.guildboss) {
/*  424 */       boss.del();
/*      */     }
/*  426 */     this.guildboss.clear();
/*      */ 
/*      */     
/*  429 */     getBo().saveGuildbossOpenNum(0);
/*      */     
/*  431 */     clear(GuildRankType.GuildBoss);
/*      */ 
/*      */ 
/*      */     
/*  435 */     this.guildwarCenter = null;
/*      */     
/*  437 */     if (CommTime.nowSecond() >= OpenSeverTime.getInstance().getOpenZeroTime() + RefDataMgr.getFactor("GuildwarOpenServerTime", 604800)) {
/*  438 */       for (Map.Entry<Integer, Guild> entry : (GuildWarMgr.getInstance()).historyWinner.entrySet()) {
/*  439 */         RefGuildWarCenter ref = (RefGuildWarCenter)RefDataMgr.get(RefGuildWarCenter.class, entry.getKey());
/*      */         
/*  441 */         if (!ref.isOpenTime()) {
/*      */           continue;
/*      */         }
/*      */         
/*  445 */         if (entry.getValue() == this) {
/*  446 */           GuildwarapplyBO bo = new GuildwarapplyBO();
/*  447 */           bo.setGuildId(getGuildId());
/*  448 */           bo.setCenterId(((Integer)entry.getKey()).intValue());
/*  449 */           bo.setApplyTime(CommTime.nowSecond());
/*  450 */           bo.insert();
/*  451 */           this.guildwarCenter = bo;
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*  456 */     this.puppets.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void promotePresident(List<GuildMemberFeature> members) {
/*  466 */     Long oldPresidentCid = ((List<Long>)this.membersDirJob.get(GuildJob.President)).get(0);
/*  467 */     Player president = PlayerMgr.getInstance().getPlayer(oldPresidentCid.longValue());
/*      */     
/*  469 */     if (CommTime.nowSecond() < president.getPlayerBO().getLastLogin() + 604800) {
/*      */       return;
/*      */     }
/*      */     
/*  473 */     GuildMemberFeature newPresident = null;
/*  474 */     GuildMemberFeature oldPresident = null;
/*  475 */     for (int i = 0; i < members.size(); i++) {
/*  476 */       GuildMemberFeature m = members.get(i);
/*  477 */       if (oldPresidentCid.longValue() == m.getPlayer().getPid()) {
/*  478 */         oldPresident = m;
/*      */       
/*      */       }
/*  481 */       else if (newPresident == null) {
/*  482 */         newPresident = m;
/*  483 */       } else if (m.getJob().ordinal() < newPresident.getJob().ordinal()) {
/*  484 */         newPresident = m;
/*  485 */       } else if (m.getJob() == newPresident.getJob() && m.getDonate() > newPresident.getDonate()) {
/*  486 */         newPresident = m;
/*      */       } 
/*      */     } 
/*      */     
/*  490 */     if (newPresident == null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  495 */     getMember(oldPresident.getJob()).remove(Long.valueOf(oldPresident.getPid()));
/*  496 */     oldPresident.setJob(GuildJob.Member);
/*      */     
/*  498 */     getMember(newPresident.getJob()).remove(Long.valueOf(newPresident.getPid()));
/*  499 */     newPresident.setJob(GuildJob.President);
/*      */     
/*  501 */     broadcastMember(new long[] { oldPresident.getPlayer().getPid(), newPresident.getPlayer().getPid() });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public core.network.proto.Guild.JoinInfo joinInfo() {
/*  511 */     core.network.proto.Guild.JoinInfo builder = new core.network.proto.Guild.JoinInfo();
/*  512 */     builder.setsId(this.bo.getId());
/*  513 */     builder.setLevel(this.bo.getLevel());
/*  514 */     builder.setMemberCnt(getMemberCnt());
/*  515 */     RefGuildLevel refGuildLevel = (RefGuildLevel)RefDataMgr.get(RefGuildLevel.class, Integer.valueOf(this.bo.getLevel()));
/*  516 */     builder.setMaxmemberCnt(refGuildLevel.MaxMemberAmount);
/*  517 */     builder.setIconId(this.bo.getIcon());
/*  518 */     builder.setBorderId(this.bo.getBorder());
/*  519 */     builder.setName(this.bo.getName());
/*  520 */     Long mster = getMember(GuildJob.President).get(0);
/*  521 */     builder.setMaster(((PlayerBase)PlayerMgr.getInstance().getPlayer(mster.longValue()).getFeature(PlayerBase.class)).summary());
/*  522 */     builder.setManifesto(this.bo.getManifesto());
/*  523 */     builder.setJoinState(getJoinState());
/*  524 */     builder.setJoinLevel(this.bo.getLevelLimit());
/*  525 */     builder.setRank(this.rank);
/*  526 */     return builder;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public core.network.proto.Guild.guildInfo guildInfo() {
/*  535 */     core.network.proto.Guild.guildInfo builder = new core.network.proto.Guild.guildInfo();
/*  536 */     builder.setsId(this.bo.getId());
/*  537 */     builder.setLevel(this.bo.getLevel());
/*  538 */     builder.setExp(this.bo.getExp());
/*  539 */     builder.setName(this.bo.getName());
/*  540 */     builder.setIconId(this.bo.getIcon());
/*  541 */     builder.setBorderId(this.bo.getBorder());
/*  542 */     builder.setCreateTime(this.bo.getCreateTime());
/*  543 */     builder.setNotice(this.bo.getNotice());
/*  544 */     builder.setManifesto(this.bo.getManifesto());
/*  545 */     builder.setJoinState(getJoinState());
/*  546 */     builder.setJoinLevel(this.bo.getLevelLimit());
/*  547 */     Long mster = getMember(GuildJob.President).get(0);
/*  548 */     builder.setMaster(((PlayerBase)PlayerMgr.getInstance().getPlayer(mster.longValue()).getFeature(PlayerBase.class)).summary());
/*  549 */     builder.setNowNum(getMemberCnt());
/*  550 */     RefGuildLevel refGuildLevel = (RefGuildLevel)RefDataMgr.get(RefGuildLevel.class, Integer.valueOf(this.bo.getLevel()));
/*  551 */     builder.setMaxNum(refGuildLevel.MaxMemberAmount);
/*      */ 
/*      */ 
/*      */     
/*  555 */     return builder;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int calculatePower() {
/*  561 */     int power = 0;
/*      */     
/*  563 */     for (Long pid : getMembers()) {
/*  564 */       Player player = PlayerMgr.getInstance().getPlayer(pid.longValue());
/*  565 */       power += ((CharFeature)player.getFeature(CharFeature.class)).getPower();
/*      */     } 
/*      */     
/*  568 */     if (power > this.bo.getMaxFightPower()) {
/*  569 */       this.bo.saveMaxFightPower(power);
/*      */     }
/*  571 */     return power;
/*      */   }
/*      */   
/*      */   public int updatePower() {
/*  575 */     this.power = Integer.valueOf(calculatePower());
/*      */ 
/*      */     
/*  578 */     RankManager.getInstance().update(RankType.Guild, getGuildId(), this.power.intValue());
/*  579 */     for (Iterator<Long> iterator = this.members.iterator(); iterator.hasNext(); ) { long pid = ((Long)iterator.next()).longValue();
/*  580 */       Player player = PlayerMgr.getInstance().getPlayer(pid);
/*  581 */       ((RankGuild)ActivityMgr.getActivity(RankGuild.class)).UpdateMaxRequire_cost(player, this.power.intValue()); }
/*      */     
/*  583 */     return this.power.intValue();
/*      */   }
/*      */   
/*      */   public int updatePower(int change) {
/*  587 */     if (this.power == null) {
/*  588 */       return getPower();
/*      */     }
/*  590 */     this.power = Integer.valueOf(this.power.intValue() + change);
/*      */ 
/*      */     
/*  593 */     if (this.power.intValue() > this.bo.getMaxFightPower()) {
/*  594 */       this.bo.saveMaxFightPower(this.power.intValue());
/*      */     }
/*      */     
/*  597 */     RankManager.getInstance().update(RankType.Guild, getGuildId(), this.power.intValue());
/*  598 */     for (Iterator<Long> iterator = this.members.iterator(); iterator.hasNext(); ) { long pid = ((Long)iterator.next()).longValue();
/*  599 */       Player player = PlayerMgr.getInstance().getPlayer(pid);
/*  600 */       ((RankGuild)ActivityMgr.getActivity(RankGuild.class)).UpdateMaxRequire_cost(player, this.power.intValue()); }
/*      */     
/*  602 */     return this.power.intValue();
/*      */   }
/*      */   
/*      */   public int getPower() {
/*  606 */     if (this.power != null) {
/*  607 */       return this.power.intValue();
/*      */     }
/*  609 */     return updatePower();
/*      */   }
/*      */   
/*      */   public void openBoss(RefGuildBoss refboss) {
/*  613 */     RefMonster ref = (RefMonster)RefDataMgr.get(RefMonster.class, Integer.valueOf(refboss.MonsterId));
/*  614 */     GuildBossBO bossBO = new GuildBossBO();
/*  615 */     bossBO.setBossId(refboss.id);
/*  616 */     bossBO.setGuildId(getGuildId());
/*  617 */     bossBO.setBossMaxHp(ref.MaxHP);
/*  618 */     bossBO.setBossHp(bossBO.getBossMaxHp());
/*  619 */     bossBO.setIsOpen(true);
/*  620 */     bossBO.insert();
/*  621 */     this.guildboss.add(bossBO);
/*  622 */     this.bo.saveGuildbossOpenNum(this.bo.getGuildbossOpenNum() + 1);
/*  623 */     broadcast("GuildBossOpen", bossBO);
/*      */   }
/*      */   
/*      */   public boolean isInOpenHour() {
/*  627 */     return GuildConfig.getGuildBossOpenTime().within(CommTime.getTodayHour());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void doEnterGuildBoss(Player player, GuildBossBO Boss) {
/*  636 */     List<Player> players = this.joinPlayers.get(Boss);
/*  637 */     if (players == null) {
/*  638 */       List<Player> playerL = new ArrayList<>();
/*  639 */       playerL.add(player);
/*  640 */       this.joinPlayers.put(Boss, playerL);
/*      */     }
/*  642 */     else if (!players.contains(player)) {
/*  643 */       players.add(player);
/*  644 */       this.joinPlayers.put(Boss, players);
/*      */     } 
/*      */ 
/*      */     
/*  648 */     for (Player tmpPlayer : this.joinPlayers.get(Boss)) {
/*  649 */       if (tmpPlayer != null && tmpPlayer != player)
/*  650 */         tmpPlayer.pushProto("newGuildBossPlayer", ((PlayerBase)player.getFeature(PlayerBase.class)).fightInfo()); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public List<Player> getPlayerList(GuildBossBO boss) {
/*  655 */     return this.joinPlayers.get(boss);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean doHurtGuildBoss(Player player, int damage, int bossId) throws WSException {
/*  667 */     boolean isKill = false;
/*  668 */     synchronized (this) {
/*  669 */       GuildBossBO bo = getboss(bossId);
/*  670 */       if (bo.getIsDead()) {
/*  671 */         throw new WSException(ErrorCode.GuildBoss_IsDeath, "Boss已被消灭");
/*      */       }
/*  673 */       bo.setBossHp(bo.getBossHp() - damage);
/*  674 */       if (bo.getBossHp() <= 0L) {
/*  675 */         bo.setDeadTime(CommTime.nowSecond());
/*  676 */         bo.setBossHp(0L);
/*  677 */         bo.setIsDead(true);
/*  678 */         bo.saveIsOpen(false);
/*  679 */         bo.setLastKillCid(player.getPid());
/*  680 */         isKill = true;
/*      */       } 
/*  682 */       bo.saveAll();
/*      */       
/*  684 */       if (isKill) {
/*      */         
/*  686 */         for (Long mem : this.members) {
/*  687 */           RefGuildBoss refBoss = (RefGuildBoss)RefDataMgr.get(RefGuildBoss.class, Integer.valueOf(bossId));
/*  688 */           MailCenter.getInstance().sendMail(mem.longValue(), refBoss.MailId, new String[0]);
/*      */         } 
/*  690 */         this.bo.saveGuildbossLevel(Math.max(this.bo.getGuildbossLevel(), bossId));
/*  691 */         broadCastProtoToPlayers(bo);
/*      */       } 
/*      */     } 
/*  694 */     return isKill;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void broadCastProtoToPlayers(GuildBossBO boss) {
/*  703 */     broadcast("guildbossDead", boss);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void doLeaveGuildBoss(Player player, int bossId) throws WSException {
/*  713 */     GuildBossBO boss = getboss(bossId);
/*  714 */     if (this.joinPlayers.get(boss) != null) {
/*  715 */       boolean remove = ((List)this.joinPlayers.get(boss)).remove(player);
/*  716 */       if (!remove) {
/*  717 */         throw new WSException(ErrorCode.GuildBoss_NotEnter, "未进入帮派BOSS");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public Guild(GuildBO bo) {
/*  724 */     this.ranks = new HashMap<>();
/*      */     this.bo = bo;
/*      */   } public void init() {
/*  727 */     this.guildboss = BM.getBM(GuildBossBO.class).findAll("guildId", Long.valueOf(getGuildId()));
/*      */     
/*  729 */     Set<Class<?>> clazzs = CommClass.getClasses(GuildNormalRank.class.getPackage().getName());
/*  730 */     for (Class<?> clz : clazzs) {
/*      */       
/*      */       try {
/*  733 */         Class<? extends GuildRank> clazz = (Class)clz;
/*  734 */         GuildRanks types = clazz.<GuildRanks>getAnnotation(GuildRanks.class);
/*  735 */         if (types == null || (types.value()).length == 0) {
/*  736 */           CommLog.error("[{}]的类型没有相关排行榜类型，请检查", clazz.getSimpleName());
/*  737 */           System.exit(0);
/*      */         }  byte b; int i; GuildRankType[] arrayOfGuildRankType;
/*  739 */         for (i = (arrayOfGuildRankType = types.value()).length, b = 0; b < i; ) { GuildRankType type = arrayOfGuildRankType[b];
/*  740 */           if (this.ranks.containsKey(type)) {
/*  741 */             String preClass = ((GuildRank)this.ranks.get(type)).getClass().getSimpleName();
/*  742 */             CommLog.error("[{},{}]重复定义[{}]类型的排行榜", preClass, clazz.getSimpleName());
/*  743 */             System.exit(0);
/*      */           } 
/*  745 */           GuildRank instance = clazz.getConstructor(new Class[] { GuildRankType.class }).newInstance(new Object[] { type });
/*  746 */           this.ranks.put(type, instance); b++; }
/*      */       
/*  748 */       } catch (Exception e) {
/*  749 */         CommLog.error("排行榜[{}]初始化失败", clz.getSimpleName(), e);
/*  750 */         System.exit(0);
/*      */       } 
/*      */     } 
/*  753 */     List<GuildRankRecordBO> list = BM.getBM(GuildRankRecordBO.class).findAll("guildid", Long.valueOf(getGuildId()));
/*  754 */     for (GuildRankRecordBO recordBO : list) {
/*  755 */       GuildRankType type = GuildRankType.values()[recordBO.getType()];
/*  756 */       GuildRank rank = this.ranks.get(type);
/*  757 */       GuildRecord record = new GuildRecord(recordBO);
/*  758 */       rank.map.put(Long.valueOf(recordBO.getOwner()), record);
/*      */     } 
/*  760 */     for (GuildRank rank : this.ranks.values()) {
/*  761 */       rank.resort();
/*      */     }
/*      */ 
/*      */     
/*  765 */     getOrCreateLongnv().Restart();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<GuildRecord> getRankList(GuildRankType type, int size) {
/*  774 */     List<GuildRecord> records, list = (getRank(type)).list;
/*  775 */     if (list.size() > size + 1) {
/*  776 */       records = list.subList(0, size + 1);
/*      */     } else {
/*  778 */       records = new ArrayList<>(list);
/*      */     } 
/*  780 */     return records;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getPlayerId(GuildRankType type, int rank) {
/*      */     int num;
/*  788 */     List<GuildRecord> records = (getRank(type)).list;
/*      */     
/*  790 */     if (rank < 1 || records.size() < 2) {
/*  791 */       return 0L;
/*      */     }
/*  793 */     if (records.size() - 1 < rank) {
/*  794 */       num = records.size() - 1;
/*      */     } else {
/*  796 */       num = rank;
/*      */     } 
/*      */     
/*  799 */     return ((GuildRecord)records.get(num)).getPid();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getRank(GuildRankType type, long ownerid) {
/*  804 */     return getRank(type).getRank(ownerid);
/*      */   }
/*      */   
/*      */   public long getValue(GuildRankType type, long ownerid) {
/*  808 */     return getRank(type).getValue(ownerid);
/*      */   }
/*      */   
/*      */   public int update(GuildRankType type, long ownerid, long value) {
/*  812 */     return getRank(type).update(ownerid, value, new long[0]);
/*      */   }
/*      */   
/*      */   public int update(GuildRankType type, long ownerid, long value, long... ext) {
/*  816 */     return getRank(type).update(ownerid, value, ext);
/*      */   }
/*      */   
/*      */   public int minus(GuildRankType type, long ownerid, int value) {
/*  820 */     return getRank(type).minus(ownerid, value);
/*      */   }
/*      */   
/*      */   private GuildRank getRank(GuildRankType type) {
/*  824 */     GuildRank rank = this.ranks.get(type);
/*  825 */     if (rank == null) {
/*  826 */       CommLog.error("排行榜[]未注册", type);
/*      */     }
/*  828 */     return rank;
/*      */   }
/*      */   
/*      */   public int getRankSize(GuildRankType type) {
/*  832 */     GuildRank rank = this.ranks.get(type);
/*  833 */     if (rank == null) {
/*  834 */       CommLog.error("排行榜[]未注册", type);
/*      */     }
/*  836 */     return rank.list.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear(GuildRankType type) {
/*  861 */     getRank(type).clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearPlayerData(Player player) {
/*  868 */     BM.getBM(GuildRankRecordBO.class).delAll("owner", Long.valueOf(player.getPid()));
/*  869 */     GuildRanks types = GuildNormalRank.class.<GuildRanks>getAnnotation(GuildRanks.class); byte b; int i; GuildRankType[] arrayOfGuildRankType;
/*  870 */     for (i = (arrayOfGuildRankType = types.value()).length, b = 0; b < i; ) { GuildRankType type = arrayOfGuildRankType[b];
/*  871 */       ((GuildRank)this.ranks.get(type)).del(player.getPid());
/*      */       b++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void applyGuildWar(int centerId) throws WSException {
/*  886 */     RefGuildWarCenter ref = (RefGuildWarCenter)RefDataMgr.get(RefGuildWarCenter.class, Integer.valueOf(centerId));
/*      */     
/*  888 */     if (CommTime.nowSecond() < OpenSeverTime.getInstance().getOpenZeroTime() + RefDataMgr.getFactor("GuildwarOpenServerTime", 604800)) {
/*  889 */       throw new WSException(ErrorCode.GuildWar_TimeLimit, "帮派战功能未开启");
/*      */     }
/*      */     
/*  892 */     if (ref == null) {
/*  893 */       throw new WSException(ErrorCode.GuildWar_NotFoundCenter, "据点不存在");
/*      */     }
/*      */     
/*  896 */     if (this.guildwarCenter != null) {
/*  897 */       throw new WSException(ErrorCode.GuildWar_AlreadyOpen, "帮派已报名");
/*      */     }
/*      */     
/*  900 */     if (!ref.isOpenTime()) {
/*  901 */       throw new WSException(ErrorCode.GuildWar_NotOpen, "据点未开启");
/*      */     }
/*      */     
/*  904 */     if (!GuildWarConfig.applyTime()) {
/*  905 */       throw new WSException(ErrorCode.GuildWar_ApplyNotOpen, "申请时间没到");
/*      */     }
/*      */     
/*  908 */     if (getLevel() < RefDataMgr.getFactor("GuildWarApplyLevel", 2)) {
/*  909 */       throw new WSException(ErrorCode.GuildWar_LevelLimit, "帮派等级不足[%s]级，无法报名", new Object[] { Integer.valueOf(RefDataMgr.getFactor("GuildWarApplyLevel", 2)) });
/*      */     }
/*  911 */     GuildWarMgr.getInstance().applyGuildWar(centerId, this);
/*  912 */     GuildwarapplyBO bo = new GuildwarapplyBO();
/*  913 */     bo.setGuildId(getGuildId());
/*  914 */     bo.setCenterId(centerId);
/*  915 */     bo.setApplyTime(CommTime.nowSecond());
/*  916 */     bo.insert();
/*  917 */     this.guildwarCenter = bo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Player> getGuildWarPlayer() {
/*  930 */     List<Player> list = new ArrayList<>();
/*  931 */     getMembers().stream().forEach(mem -> paramList.add(PlayerMgr.getInstance().getPlayer(mem.longValue())));
/*      */ 
/*      */ 
/*      */     
/*  935 */     Collections.shuffle(list);
/*  936 */     return list;
/*      */   }
/*      */   
/*      */   public static class GuildSummary {
/*      */     long guildId;
/*      */     String guildName;
/*      */     Player.showModle president;
/*      */     
/*      */     public Player.showModle getPresident() {
/*  945 */       return this.president;
/*      */     }
/*      */     
/*      */     public void setPresident(Player.showModle president) {
/*  949 */       this.president = president;
/*      */     }
/*      */ 
/*      */     
/*      */     public GuildSummary() {}
/*      */ 
/*      */     
/*      */     public GuildSummary(Guild guild) {
/*  957 */       if (guild != null) {
/*  958 */         this.guildId = guild.getGuildId();
/*  959 */         this.guildName = guild.getName();
/*  960 */         this.president = ((PlayerBase)PlayerMgr.getInstance().getPlayer(((Long)guild.getMember(GuildJob.President).get(0)).longValue()).getFeature(PlayerBase.class)).modle();
/*      */       } 
/*      */     }
/*      */     
/*      */     public long getGuildId() {
/*  965 */       return this.guildId;
/*      */     }
/*      */     
/*      */     public void setGuildId(long guildId) {
/*  969 */       this.guildId = guildId;
/*      */     }
/*      */     
/*      */     public String getGuildName() {
/*  973 */       return this.guildName;
/*      */     }
/*      */     
/*      */     public void setGuildName(String guildName) {
/*  977 */       this.guildName = guildName;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public rebirth rebirth(Player player) throws WSException {
/*  984 */     if (this.guildwarCenter == null) {
/*  985 */       throw new WSException(ErrorCode.GuildWar_NotApply, "帮派未报名");
/*      */     }
/*  987 */     int centerId = GuildWarMgr.getInstance().isFighting(this);
/*  988 */     if (centerId != 0) {
/*  989 */       GuildWarMgr.getInstance().rebirth(player, centerId);
/*  990 */       PlayerRecord playerRecord = (PlayerRecord)player.getFeature(PlayerRecord.class);
/*  991 */       return new rebirth(getTotalPuppet(), getPersonPuppet(player.getPid()), playerRecord.getValue(ConstEnum.DailyRefresh.GuildWarRebirth));
/*      */     } 
/*      */     
/*  994 */     PlayerCurrency currency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/*  995 */     PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);
/*      */     
/*  997 */     int curTimes = recorder.getValue(ConstEnum.DailyRefresh.GuildWarRebirth);
/*  998 */     RefCrystalPrice prize = RefCrystalPrice.getPrize(curTimes);
/*  999 */     if (!currency.check(PrizeType.Crystal, prize.GuildWarRebirth)) {
/* 1000 */       throw new WSException(ErrorCode.NotEnough_Crystal, "玩家召唤傀儡需要钻石%s", new Object[] { Integer.valueOf(prize.GuildWarRebirth) });
/*      */     }
/* 1002 */     currency.consume(PrizeType.Crystal, prize.GuildWarRebirth, ItemFlow.GuildWar_Rebirth);
/* 1003 */     recorder.addValue(ConstEnum.DailyRefresh.GuildWarRebirth);
/* 1004 */     GuildwarpuppetBO bo = new GuildwarpuppetBO();
/* 1005 */     bo.setPid(player.getPid());
/* 1006 */     bo.setGuildId(getGuildId());
/* 1007 */     bo.savePuppetId(curTimes);
/* 1008 */     bo.setApplyTime(CommTime.nowSecond());
/* 1009 */     bo.insert();
/* 1010 */     if (this.puppets.get(Long.valueOf(player.getPid())) != null) {
/* 1011 */       ((List<GuildwarpuppetBO>)this.puppets.get(Long.valueOf(player.getPid()))).add(bo);
/*      */     } else {
/* 1013 */       List<GuildwarpuppetBO> list = new ArrayList<>();
/* 1014 */       list.add(bo);
/* 1015 */       this.puppets.put(Long.valueOf(player.getPid()), list);
/*      */     } 
/*      */     
/* 1018 */     broadcast("beforePuppet", Integer.valueOf(getTotalPuppet()), player.getPid());
/*      */     
/* 1020 */     return new rebirth(getTotalPuppet(), getPersonPuppet(player.getPid()), recorder.getValue(ConstEnum.DailyRefresh.GuildWarRebirth));
/*      */   }
/*      */ 
/*      */   
/*      */   public rebirth getPuppetInfo(Player player) {
/* 1025 */     PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);
/* 1026 */     return new rebirth(getTotalPuppet(), getPersonPuppet(player.getPid()), recorder.getValue(ConstEnum.DailyRefresh.GuildWarRebirth));
/*      */   }
/*      */   
/*      */   public int getTotalPuppet() {
/* 1030 */     int num = 0;
/* 1031 */     for (List<GuildwarpuppetBO> list : this.puppets.values()) {
/* 1032 */       num += list.size();
/*      */     }
/*      */     
/* 1035 */     return num;
/*      */   }
/*      */   
/*      */   public int getPersonPuppet(long pid) {
/* 1039 */     int num = 0;
/* 1040 */     List<GuildwarpuppetBO> list = this.puppets.get(Long.valueOf(pid));
/* 1041 */     if (list != null) {
/* 1042 */       num = list.size();
/*      */     }
/* 1044 */     return num;
/*      */   }
/*      */   
/*      */   public static class rebirth {
/*      */     int total;
/*      */     int my;
/*      */     int rebirthTime;
/*      */     
/*      */     public rebirth(int total, int my, int rebirthTime) {
/* 1053 */       this.total = total;
/* 1054 */       this.my = my;
/* 1055 */       this.rebirthTime = rebirthTime;
/*      */     }
/*      */   }
/*      */   
/*      */   public List<Player> createPuppets() {
/* 1060 */     List<Player> puppets = new ArrayList<>();
/* 1061 */     for (Map.Entry<Long, List<GuildwarpuppetBO>> entry : this.puppets.entrySet()) {
/* 1062 */       Player player = PlayerMgr.getInstance().getPlayer(((Long)entry.getKey()).longValue());
/* 1063 */       for (GuildwarpuppetBO bo : entry.getValue()) {
/* 1064 */         GuildWarConfig.puppetPlayer p_player = new GuildWarConfig.puppetPlayer(player.getPlayerBO());
/* 1065 */         p_player.setPuppet_id(bo.getPuppetId());
/* 1066 */         p_player.setIs_puppet(true);
/* 1067 */         puppets.add(p_player);
/*      */       } 
/*      */     } 
/*      */     
/* 1071 */     return puppets;
/*      */   }
/*      */   
/*      */   public void removePuppet(Player puppet) {
/* 1075 */     List<GuildwarpuppetBO> puppets = this.puppets.get(Long.valueOf(puppet.getPid()));
/* 1076 */     GuildwarpuppetBO find = null;
/* 1077 */     if (puppets != null) {
/* 1078 */       GuildWarConfig.puppetPlayer puppetplayer = (GuildWarConfig.puppetPlayer)puppet;
/* 1079 */       for (GuildwarpuppetBO bo : puppets) {
/* 1080 */         if (bo.getPuppetId() == puppetplayer.getPuppet_id()) {
/* 1081 */           find = bo;
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1086 */     if (find != null)
/* 1087 */       puppets.remove(find); 
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/guild/Guild.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */