/*     */ package business.player.feature.character;
/*     */ 
/*     */ import business.global.activity.ActivityMgr;
/*     */ import business.global.activity.detail.RankPower;
/*     */ import business.global.fight.Fighter;
/*     */ import business.global.guild.Guild;
/*     */ import business.global.notice.NoticeMgr;
/*     */ import business.global.rank.RankManager;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.RobotManager;
/*     */ import business.player.feature.Feature;
/*     */ import business.player.feature.achievement.AchievementFeature;
/*     */ import business.player.feature.guild.GuildMemberFeature;
/*     */ import business.player.feature.marry.MarryFeature;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.Achievement;
/*     */ import com.zhonglian.server.common.enums.Attribute;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.DressType;
/*     */ import com.zhonglian.server.common.enums.EquipPos;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.common.enums.UnlockType;
/*     */ import com.zhonglian.server.common.utils.CommMath;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefCharacter;
/*     */ import core.config.refdata.ref.RefEquip;
/*     */ import core.config.refdata.ref.RefMonster;
/*     */ import core.config.refdata.ref.RefRobotCharacter;
/*     */ import core.config.refdata.ref.RefSkill;
/*     */ import core.config.refdata.ref.RefUnlockFunction;
/*     */ import core.config.refdata.ref.RefWarSpirit;
/*     */ import core.database.game.bo.CharacterBO;
/*     */ import core.database.game.bo.DressBO;
/*     */ import core.database.game.bo.EquipBO;
/*     */ import core.database.game.bo.WarSpiritBO;
/*     */ import core.network.proto.Character;
/*     */ import core.network.proto.EquipMessage;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class CharFeature extends Feature {
/*     */   private Map<Integer, Character> characters;
/*     */   private Integer power;
/*     */   
/*     */   public CharFeature(Player owner) {
/*  53 */     super(owner);
/*     */ 
/*     */     
/*  56 */     this.characters = new HashMap<>();
/*     */   }
/*     */   
/*     */   public void loadDB() {
/*  60 */     List<CharacterBO> bos = BM.getBM(CharacterBO.class).findAll("pid", Long.valueOf(this.player.getPid()));
/*  61 */     for (CharacterBO bo : bos) {
/*  62 */       this.characters.put(Integer.valueOf(bo.getCharId()), new Character(this.player, bo));
/*     */     }
/*     */     
/*  65 */     List<Equip> equips = ((EquipFeature)this.player.getFeature(EquipFeature.class)).getAllEquips();
/*  66 */     for (Equip equip : equips) {
/*  67 */       int charid = equip.getBo().getCharId();
/*  68 */       if (charid != 0) {
/*  69 */         equip.setOwner(getCharacter(charid), EquipPos.values()[equip.getBo().getPos()]);
/*     */       }
/*     */     } 
/*     */     
/*  73 */     List<DressBO> dresses = ((DressFeature)this.player.getFeature(DressFeature.class)).getAllDress();
/*  74 */     for (DressBO dress : dresses) {
/*  75 */       int charid = dress.getCharId();
/*  76 */       if (charid != 0) {
/*  77 */         getCharacter(charid).activeDress(DressType.values()[dress.getType()], dress);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public Character getCharacter(int character) {
/*  83 */     return this.characters.get(Integer.valueOf(character));
/*     */   }
/*     */   
/*     */   public long unlockChar(int selected, ItemFlow playercreate) throws WSException {
/*  87 */     if (this.characters.size() != 0) {
/*  88 */       UnlockType type = UnlockType.valueOf("Character" + (this.characters.size() + 1));
/*  89 */       RefUnlockFunction.checkUnlock(this.player, type);
/*     */     } 
/*  91 */     if (this.characters.containsKey(Integer.valueOf(selected))) {
/*  92 */       throw new WSException(ErrorCode.Player_AlreadyExist, "该角色[%s]已解锁", new Object[] { Integer.valueOf(selected) });
/*     */     }
/*  94 */     CharacterBO bo = new CharacterBO();
/*  95 */     bo.setPid(this.player.getPid());
/*  96 */     bo.setCharId(selected);
/*  97 */     bo.insert();
/*  98 */     Character ch = new Character(this.player, bo);
/*  99 */     this.characters.put(Integer.valueOf(ch.getCharId()), ch);
/* 100 */     if (this.characters.size() > 1)
/* 101 */       NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.UnlockChar, new String[] { this.player.getName() }); 
/* 102 */     updatePower();
/*     */ 
/*     */     
/* 105 */     ((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.UnlockChar);
/* 106 */     return bo.getId();
/*     */   }
/*     */   
/*     */   public Map<Integer, Character> getAll() {
/* 110 */     return this.characters;
/*     */   }
/*     */   
/*     */   public Map<Integer, Fighter> getFighters() {
/* 114 */     Map<Integer, Fighter> rtn = new HashMap<>();
/* 115 */     for (Character character : this.characters.values()) {
/* 116 */       int charid = character.getCharId();
/* 117 */       CharacterBO bo = character.getBo();
/* 118 */       RefCharacter refChar = (RefCharacter)RefDataMgr.get(RefCharacter.class, Integer.valueOf(charid));
/*     */       
/* 120 */       Fighter fighter = new Fighter();
/* 121 */       fighter.id = charid;
/* 122 */       for (int i = 0; i < refChar.SkillList.size(); i++) {
/* 123 */         int skillid = ((Integer)refChar.SkillList.get(i)).intValue();
/* 124 */         RefSkill skill = (RefSkill)RefDataMgr.get(RefSkill.class, Integer.valueOf(skillid));
/* 125 */         if (this.player.getLv() >= skill.Require) {
/*     */ 
/*     */           
/* 128 */           fighter.skills.put(Integer.valueOf(skillid), skill);
/* 129 */           fighter.skillsLv.put(Integer.valueOf(skillid), Integer.valueOf(bo.getSkill(i)));
/*     */         } 
/* 131 */       }  CharAttrCalculator calculator = character.getAttr();
/* 132 */       fighter.putAllAttr(calculator.getAttrs());
/*     */       
/* 134 */       fighter.hp = fighter.attr(Attribute.MaxHP);
/*     */       
/* 136 */       rtn.put(Integer.valueOf(character.getCharId()), fighter);
/*     */     } 
/* 138 */     WarSpirit spirit = ((WarSpiritFeature)this.player.getFeature(WarSpiritFeature.class)).getWarSpiritNow();
/* 139 */     if (spirit != null) {
/* 140 */       WarSpiritBO bo = spirit.getBo();
/* 141 */       RefWarSpirit refSpirit = (RefWarSpirit)RefDataMgr.get(RefWarSpirit.class, Integer.valueOf(bo.getSpiritId()));
/*     */       
/* 143 */       Fighter fighter = new Fighter();
/* 144 */       fighter.id = spirit.getSpiritId();
/* 145 */       fighter.isMain = false;
/* 146 */       for (int i = 0; i < refSpirit.SkillList.size(); i++) {
/* 147 */         int skillid = ((Integer)refSpirit.SkillList.get(i)).intValue();
/* 148 */         RefSkill skill = (RefSkill)RefDataMgr.get(RefSkill.class, Integer.valueOf(skillid));
/* 149 */         fighter.skills.put(Integer.valueOf(skillid), skill);
/* 150 */         if (skillid == 0) {
/* 151 */           fighter.skillsLv.put(Integer.valueOf(skillid), Integer.valueOf(0));
/*     */         } else {
/* 153 */           fighter.skillsLv.put(Integer.valueOf(skillid), Integer.valueOf(bo.getSkill()));
/*     */         } 
/*     */       } 
/* 156 */       SpiritAttrCalculator calculator = spirit.getAttr();
/* 157 */       fighter.putAllAttr(calculator.getAttrs());
/*     */       
/* 159 */       fighter.hp = fighter.attr(Attribute.MaxHP);
/* 160 */       rtn.put(Integer.valueOf(spirit.getSpiritId()), fighter);
/*     */     } 
/* 162 */     return rtn;
/*     */   }
/*     */   
/*     */   public List<Character.CharInfo> getAllInfo() {
/* 166 */     List<Character.CharInfo> list = new ArrayList<>();
/* 167 */     ((EquipFeature)this.player.getFeature(EquipFeature.class)).getAllEquips();
/* 168 */     this.characters.values().forEach(character -> {
/*     */           Character.CharInfo ch = new Character.CharInfo();
/*     */           
/*     */           CharacterBO bo = character.getBo();
/*     */           ch.sid = bo.getId();
/*     */           ch.level = this.player.getLv();
/*     */           ch.charId = character.getCharId();
/*     */           ch.strengthen.addAll(bo.getStrengthenAll());
/*     */           ch.gem.addAll(bo.getGemAll());
/*     */           ch.star.addAll(bo.getStarAll());
/*     */           ch.meridian = bo.getMeridian();
/*     */           ch.wing = bo.getWing();
/*     */           ch.wingExp = bo.getWingExp();
/*     */           ch.skill.addAll(bo.getSkillAll());
/*     */           ch.rebirth = bo.getRebirth();
/*     */           ch.rebirthExp = bo.getRebirthExp();
/*     */           ch.artifice.addAll(bo.getArtificeAll());
/*     */           ch.artificeMax.addAll(bo.getArtificeMaxAll());
/*     */           for (Equip equip : character.getEquips()) {
/*     */             ch.equips.add(new EquipMessage(equip.getBo()));
/*     */           }
/*     */           ch.dresses = character.getAllDressInfo();
/*     */           paramList.add(ch);
/*     */         });
/* 192 */     return list;
/*     */   }
/*     */   
/*     */   public int simulate(Integer simulateId) {
/* 196 */     RefRobotCharacter ref = (RefRobotCharacter)RefDataMgr.get(RefRobotCharacter.class, simulateId);
/* 197 */     Character character = this.characters.get(Integer.valueOf(ref.CharId));
/* 198 */     if (character == null) {
/* 199 */       CharacterBO characterBO = new CharacterBO();
/* 200 */       characterBO.setPid(this.player.getPid());
/* 201 */       characterBO.setCharId(ref.CharId);
/* 202 */       characterBO.insert();
/* 203 */       character = new Character(this.player, characterBO);
/* 204 */       this.characters.put(Integer.valueOf(ref.CharId), character);
/*     */     } 
/* 206 */     CharacterBO bo = character.getBo(); int i;
/* 207 */     for (i = 0; i < ref.Strengthen.size(); i++) {
/* 208 */       bo.setStrengthen(i + 1, ((Integer)ref.Strengthen.get(i)).intValue());
/*     */     }
/* 210 */     bo.setWing(ref.Wing.random());
/* 211 */     bo.saveAll();
/*     */     
/* 213 */     for (i = 0; i < ref.Equip.size(); i++) {
/* 214 */       int equipId = ((Integer)ref.Equip.get(i)).intValue();
/* 215 */       if (equipId != 0) {
/*     */ 
/*     */ 
/*     */         
/* 219 */         RefEquip equipref = (RefEquip)RefDataMgr.get(RefEquip.class, Integer.valueOf(equipId));
/* 220 */         EquipBO equipBO = new EquipBO();
/* 221 */         equipBO.setPid(this.player.getPid());
/* 222 */         equipBO.setEquipId(equipref.id);
/* 223 */         equipBO.setCharId(bo.getCharId());
/* 224 */         equipBO.setPos(i + 1);
/* 225 */         for (int attridx = 0; attridx < equipref.AttrValueList.size(); attridx++) {
/* 226 */           equipBO.setAttr(attridx, ((Integer)equipref.AttrValueList.get(attridx)).intValue() * CommMath.randomInt(500, 1500) / 10000);
/*     */         }
/* 228 */         equipBO.setGainTime(CommTime.nowSecond());
/* 229 */         equipBO.insert();
/* 230 */         Equip equip = new Equip(this.player, equipBO);
/* 231 */         character.equip(equip, EquipPos.values()[i + 1]);
/* 232 */         equip.saveOwner(character, EquipPos.values()[i + 1]);
/* 233 */         ((EquipFeature)this.player.getFeature(EquipFeature.class)).gain(equip);
/*     */       } 
/* 235 */     }  bo.saveAll();
/* 236 */     return ref.CharId;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int calculatePower() {
/* 242 */     int power = 0;
/*     */     
/* 244 */     for (Character character : this.characters.values()) {
/* 245 */       power += character.getPower();
/*     */     }
/*     */     
/* 248 */     power += ((WarSpiritFeature)this.player.getFeature(WarSpiritFeature.class)).getPower();
/*     */ 
/*     */     
/* 251 */     if (power > this.player.getPlayerBO().getMaxFightPower()) {
/* 252 */       this.player.getPlayerBO().saveMaxFightPower(power);
/* 253 */       this.player.notify2Zone();
/*     */     } 
/* 255 */     return power;
/*     */   }
/*     */   
/*     */   public int updatePower() {
/* 259 */     Guild guild = ((GuildMemberFeature)this.player.getFeature(GuildMemberFeature.class)).getGuild();
/* 260 */     if (guild != null) {
/* 261 */       if (this.power == null) {
/* 262 */         this.power = Integer.valueOf(calculatePower());
/* 263 */         guild.updatePower();
/*     */       } else {
/* 265 */         int newpower = calculatePower();
/* 266 */         int change = newpower - this.power.intValue();
/* 267 */         this.power = Integer.valueOf(newpower);
/* 268 */         guild.updatePower(change);
/*     */       } 
/*     */     } else {
/* 271 */       this.power = Integer.valueOf(calculatePower());
/*     */     } 
/*     */     
/* 274 */     if (!RobotManager.getInstance().isRobot(getPid()) && !this.player.isBanned()) {
/*     */       
/* 276 */       RankManager.getInstance().update(RankType.Power, this.player.getPid(), this.power.intValue());
/*     */       
/* 278 */       ((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.PowerTotal, this.power);
/*     */       
/* 280 */       ((RankPower)ActivityMgr.getActivity(RankPower.class)).UpdateMaxRequire_cost(this.player, this.power.intValue());
/*     */       
/* 282 */       MarryFeature marry = (MarryFeature)this.player.getFeature(MarryFeature.class);
/* 283 */       if (marry.bo.getMarried() != 0) {
/* 284 */         Player man = null;
/* 285 */         Player lover = PlayerMgr.getInstance().getPlayer(marry.bo.getLoverPid());
/* 286 */         if (marry.bo.getSex() == ConstEnum.SexType.Man.ordinal()) {
/* 287 */           man = this.player;
/*     */         } else {
/* 289 */           man = lover;
/*     */         } 
/* 291 */         RankManager.getInstance().update(RankType.Lovers, man.getPid(), (this.power.intValue() + ((CharFeature)lover.getFeature(CharFeature.class)).getPower()));
/*     */       } 
/*     */     } 
/* 294 */     return this.power.intValue();
/*     */   }
/*     */   
/*     */   public int updateRobotPower() {
/* 298 */     return (this.power = Integer.valueOf(calculatePower())).intValue();
/*     */   }
/*     */   
/*     */   public int getPower() {
/* 302 */     if (this.power != null) {
/* 303 */       return this.power.intValue();
/*     */     }
/* 305 */     return updatePower();
/*     */   }
/*     */   
/*     */   public int size() {
/* 309 */     return this.characters.size();
/*     */   }
/*     */   
/*     */   public double getMaxDamage(int bossid) {
/* 313 */     RefMonster refboss = (RefMonster)RefDataMgr.get(RefMonster.class, Integer.valueOf(bossid));
/* 314 */     double maxdamage = 0.0D;
/* 315 */     Map<Integer, Fighter> fighters = getFighters();
/* 316 */     for (Fighter fighter : fighters.values()) {
/* 317 */       for (RefSkill refSkill : fighter.skills.values()) {
/* 318 */         if (!"Damage".equals(refSkill.SettleType)) {
/*     */           continue;
/*     */         }
/* 321 */         double def = 0.0D;
/* 322 */         if ("RGS".equals(refSkill.DefAttr)) {
/* 323 */           def = refboss.RGS;
/* 324 */         } else if ("DEF".equals(refSkill.DefAttr)) {
/* 325 */           def = refboss.DEF;
/*     */         } 
/*     */         
/* 328 */         double d = (fighter.attr(Attribute.ATK) - def) * (
/* 329 */           refSkill.Attr + refSkill.AttrAdd * ((Integer)fighter.skillsLv.get(Integer.valueOf(refSkill.id))).intValue()) / 100.0D * 
/* 330 */           RefDataMgr.getFactor("CriticalMultiple", 2000) / 1000.0D;
/* 331 */         maxdamage = Math.max(maxdamage, d);
/*     */       } 
/*     */     } 
/* 334 */     return maxdamage * 3.5D;
/*     */   }
/*     */   
/*     */   public void updateCharPower() {
/* 338 */     for (Character character : this.characters.values()) {
/* 339 */       character.onAttrChanged();
/*     */     }
/* 341 */     updatePower();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/character/CharFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */