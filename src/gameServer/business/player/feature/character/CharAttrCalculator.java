/*     */ package business.player.feature.character;
/*     */ 
/*     */ import business.global.guild.Guild;
/*     */ import business.global.guild.GuildConfig;
/*     */ import business.player.Player;
/*     */ import business.player.feature.guild.GuildMemberFeature;
/*     */ import business.player.feature.player.LingBaoFeature;
/*     */ import business.player.feature.player.NewTitleFeature;
/*     */ import com.zhonglian.server.common.enums.Attribute;
/*     */ import com.zhonglian.server.common.enums.EquipPos;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefArtifice;
/*     */ import core.config.refdata.ref.RefAttribute;
/*     */ import core.config.refdata.ref.RefCharacter;
/*     */ import core.config.refdata.ref.RefDress;
/*     */ import core.config.refdata.ref.RefEquip;
/*     */ import core.config.refdata.ref.RefGem;
/*     */ import core.config.refdata.ref.RefGuildSkill;
/*     */ import core.config.refdata.ref.RefLingBao;
/*     */ import core.config.refdata.ref.RefMeridian;
/*     */ import core.config.refdata.ref.RefNewTitleLevel;
/*     */ import core.config.refdata.ref.RefRebirth;
/*     */ import core.config.refdata.ref.RefSkill;
/*     */ import core.config.refdata.ref.RefStarInfo;
/*     */ import core.config.refdata.ref.RefStrengthenInfo;
/*     */ import core.config.refdata.ref.RefStrengthenType;
/*     */ import core.config.refdata.ref.RefWing;
/*     */ import core.database.game.bo.CharacterBO;
/*     */ import core.database.game.bo.DressBO;
/*     */ import core.database.game.bo.EquipBO;
/*     */ import core.network.proto.Guild;
/*     */ import core.network.proto.TitleInfo;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class CharAttrCalculator
/*     */   extends AttrCalculator
/*     */ {
/*     */   private Character character;
/*     */   private CharacterBO bo;
/*     */   private Player player;
/*     */   private RefCharacter ref;
/*     */   
/*     */   public CharAttrCalculator(Character character) {
/*  45 */     this.character = character;
/*  46 */     this.player = this.character.player;
/*  47 */     this.bo = character.getBo();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPower() {
/*  52 */     DressFeature feature = (DressFeature)this.player.getFeature(DressFeature.class);
/*  53 */     List<DressBO> list = feature.getAllDress();
/*  54 */     if (feature.checkDressList(list) != 0) {
/*  55 */       this.updated = false;
/*     */     }
/*     */     
/*  58 */     return super.getPower();
/*     */   }
/*     */   
/*     */   protected void onUpdated() {
/*  62 */     this.character.updateRank(this.power);
/*     */   }
/*     */   
/*     */   protected void doUpdate() {
/*  66 */     this.ref = (RefCharacter)RefDataMgr.get(RefCharacter.class, Integer.valueOf(this.character.getCharId()));
/*     */     
/*  68 */     this.allAttr.put(Attribute.MaxHP, Integer.valueOf(0));
/*  69 */     this.allAttr.put(Attribute.ATK, Integer.valueOf(0));
/*  70 */     this.allAttr.put(Attribute.DEF, Integer.valueOf(0));
/*  71 */     this.allAttr.put(Attribute.RGS, Integer.valueOf(0));
/*  72 */     this.allAttr.put(Attribute.Hit, Integer.valueOf(0));
/*  73 */     this.allAttr.put(Attribute.Dodge, Integer.valueOf(0));
/*  74 */     this.allAttr.put(Attribute.Critical, Integer.valueOf(0));
/*  75 */     this.allAttr.put(Attribute.Tenacity, Integer.valueOf(0));
/*     */     
/*  77 */     calcBase();
/*  78 */     calcChar();
/*  79 */     calcEquip();
/*  80 */     calcPos();
/*  81 */     calcGuild();
/*  82 */     calcDress();
/*  83 */     calcLingBao();
/*  84 */     calcNewTitle();
/*     */ 
/*     */     
/*  87 */     this.power = PowerUtils.getPower(this.allAttr);
/*     */     
/*  89 */     List<Integer> skills = this.bo.getSkillAll();
/*  90 */     for (int i = 0; i < this.ref.SkillList.size(); i++) {
/*  91 */       RefSkill skill = (RefSkill)RefDataMgr.get(RefSkill.class, this.ref.SkillList.get(i));
/*  92 */       if (this.player.getLv() > skill.Require)
/*  93 */         this.power += skill.CE * (((Integer)skills.get(i)).intValue() + 1); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void calcBase() {
/*  98 */     RefAttribute refAttribute = (RefAttribute)RefDataMgr.get(RefAttribute.class, String.format("%d%03d", new Object[] { Integer.valueOf(this.ref.id), Integer.valueOf(this.player.getLv()) }));
/*  99 */     addAttr(Attribute.MaxHP, refAttribute.MaxHP);
/* 100 */     addAttr(Attribute.ATK, refAttribute.ATK);
/* 101 */     addAttr(Attribute.DEF, refAttribute.DEF);
/* 102 */     addAttr(Attribute.RGS, refAttribute.RGS);
/* 103 */     addAttr(Attribute.Hit, refAttribute.Hit);
/* 104 */     addAttr(Attribute.Dodge, refAttribute.Dodge);
/* 105 */     addAttr(Attribute.Critical, refAttribute.Critical);
/* 106 */     addAttr(Attribute.Tenacity, refAttribute.Tenacity);
/*     */   }
/*     */ 
/*     */   
/*     */   private void calcPos() {
/* 111 */     EquipPos[] equipPos = EquipPos.values();
/* 112 */     for (int i = 1; i < equipPos.length; i++) {
/* 113 */       int artlv = this.bo.getArtifice(i);
/* 114 */       RefStrengthenType refStrengthen = (RefStrengthenType)RefDataMgr.get(RefStrengthenType.class, equipPos[i]);
/* 115 */       if (artlv > 0) {
/* 116 */         RefArtifice ref = (RefArtifice)RefDataMgr.get(RefArtifice.class, Integer.valueOf(artlv));
/* 117 */         if (ref != null) {
/* 118 */           for (int j = 0; j < ref.Level - 1; j++) {
/* 119 */             RefArtifice arc = (RefArtifice)RefArtifice.maxLevelMap.get(Integer.valueOf(j + 1));
/* 120 */             int k = arc.getValue(refStrengthen.ArtificeType.get(j));
/* 121 */             addAttr(refStrengthen.ArtificeType.get(j), k);
/*     */           } 
/* 123 */           int value = ref.getValue(refStrengthen.ArtificeType.get(ref.Level - 1));
/* 124 */           addAttr(refStrengthen.ArtificeType.get(ref.Level - 1), value);
/*     */         } 
/*     */       } 
/*     */       
/* 128 */       int gemlv = this.bo.getGem(i);
/* 129 */       if (gemlv > 0) {
/* 130 */         int value = ((RefGem)RefDataMgr.get(RefGem.class, Integer.valueOf(gemlv))).getValue(refStrengthen.GemType);
/* 131 */         addAttr(refStrengthen.GemType, value);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void calcEquip() {
/* 138 */     List<Equip> equips = this.character.getEquips();
/* 139 */     for (Equip equip : equips) {
/* 140 */       EquipBO equipBO = equip.getBo();
/* 141 */       int pos = equipBO.getPos();
/* 142 */       EquipPos pos2 = EquipPos.values()[pos];
/*     */       
/* 144 */       double starvalue = 0.0D;
/* 145 */       if (this.bo.getStar(pos) > 0) {
/* 146 */         starvalue = ((RefStarInfo)RefDataMgr.get(RefStarInfo.class, Integer.valueOf(this.bo.getStar(pos)))).Attribute / 100.0D;
/*     */       }
/* 148 */       int strenLv = this.bo.getStrengthen(pos);
/* 149 */       Attribute strenattr = ((RefStrengthenType)RefDataMgr.get(RefStrengthenType.class, pos2)).StrengthenType;
/*     */       
/* 151 */       RefEquip ref = equip.getRef();
/* 152 */       for (int i = 0; i < ref.AttrTypeList.size(); i++) {
/* 153 */         int basevalue = ((Integer)ref.AttrValueList.get(i)).intValue() + equipBO.getAttr(i);
/* 154 */         Attribute attrtype = ref.AttrTypeList.get(i);
/* 155 */         int attrvalue = basevalue + (int)(basevalue * starvalue);
/* 156 */         if (strenattr == attrtype && strenLv > 0) {
/* 157 */           attrvalue = (int)(attrvalue + basevalue * ((RefStrengthenInfo)RefDataMgr.get(RefStrengthenInfo.class, Integer.valueOf(strenLv))).Attribute);
/*     */         }
/* 159 */         addAttr(attrtype, attrvalue);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void calcChar() {
/* 166 */     if (this.bo.getRebirth() > 0) {
/* 167 */       RefRebirth refRebirth = (RefRebirth)RefDataMgr.get(RefRebirth.class, Integer.valueOf(this.bo.getRebirth()));
/* 168 */       addAttr(refRebirth.AttrTypeList, refRebirth.AttrValueList);
/*     */     } 
/*     */     
/* 171 */     if (this.bo.getMeridian() > 0) {
/* 172 */       RefMeridian refMeridian = (RefMeridian)RefDataMgr.get(RefMeridian.class, Integer.valueOf(this.bo.getMeridian()));
/* 173 */       addAttr(refMeridian.AttrTypeList, refMeridian.AttrValueList);
/*     */     } 
/*     */     
/* 176 */     if (this.bo.getWing() > 0) {
/* 177 */       RefWing refWing = (RefWing)RefDataMgr.get(RefWing.class, Integer.valueOf(this.bo.getWing()));
/* 178 */       addAttr(refWing.AttrTypeList, refWing.AttrValueList);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void calcGuild() {
/* 183 */     GuildMemberFeature feature = (GuildMemberFeature)this.player.getFeature(GuildMemberFeature.class);
/* 184 */     Guild guild = feature.getGuild();
/* 185 */     if (guild != null) {
/* 186 */       for (Guild.GuildSkill skill : feature.getGuildSkillList()) {
/* 187 */         int maxLevel = GuildConfig.getSkillMaxLevel(skill.getSkillid(), guild.getLevel());
/* 188 */         int level = Math.min(maxLevel, skill.getLevel());
/* 189 */         RefGuildSkill ref = (RefGuildSkill)RefDataMgr.get(RefGuildSkill.class, Integer.valueOf(skill.getSkillid()));
/* 190 */         addAttr(ref.Attribute, ref.BaseValue + ref.GrowthValue * (level - 1));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void calcDress() {
/* 196 */     for (DressBO bo : this.character.getDresses()) {
/* 197 */       RefDress ref = (RefDress)RefDataMgr.get(RefDress.class, Integer.valueOf(bo.getDressId()));
/* 198 */       addAttr(ref.AttrTypeList, ref.AttrValueList);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void calcNewTitle() {
/* 203 */     NewTitleFeature feature = (NewTitleFeature)this.player.getFeature(NewTitleFeature.class);
/* 204 */     for (TitleInfo title : feature.getAllTitleInfo()) {
/* 205 */       Map<Integer, RefNewTitleLevel> refmap = RefNewTitleLevel.getTitleByType(title.getTitleId());
/* 206 */       RefNewTitleLevel ref = refmap.get(Integer.valueOf(title.getLevel()));
/* 207 */       addAttr(ref.AttrTypeList, ref.AttrValueList);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void calcLingBao() {
/* 212 */     LingBaoFeature feature = (LingBaoFeature)this.player.getFeature(LingBaoFeature.class);
/* 213 */     RefLingBao ref = (RefLingBao)RefDataMgr.get(RefLingBao.class, Integer.valueOf(feature.getLevel()));
/* 214 */     addAttr(ref.AttrTypeList, ref.AttrValueList);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/character/CharAttrCalculator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */