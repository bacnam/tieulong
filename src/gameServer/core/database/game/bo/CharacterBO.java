/*     */ package core.database.game.bo;
/*     */ 
/*     */ import com.zhonglian.server.common.db.BaseBO;
/*     */ import com.zhonglian.server.common.db.annotation.DataBaseField;
/*     */ import core.server.ServerConfig;
/*     */ import java.sql.ResultSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ public class CharacterBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "char_id", comment = "角色id")
/*     */   private int char_id;
/*     */   @DataBaseField(type = "int(11)", size = 9, fieldname = "strengthen", comment = "强化等级")
/*     */   private List<Integer> strengthen;
/*     */   @DataBaseField(type = "int(11)", size = 9, fieldname = "gem", comment = "宝石")
/*     */   private List<Integer> gem;
/*     */   @DataBaseField(type = "int(11)", size = 9, fieldname = "star", comment = "星级")
/*     */   private List<Integer> star;
/*     */   @DataBaseField(type = "int(11)", fieldname = "meridian", comment = "经脉")
/*     */   private int meridian;
/*     */   @DataBaseField(type = "int(11)", fieldname = "wing", comment = "翅膀")
/*     */   private int wing;
/*     */   @DataBaseField(type = "int(11)", fieldname = "wingExp", comment = "翅膀当前经验")
/*     */   private int wingExp;
/*     */   @DataBaseField(type = "int(11)", size = 5, fieldname = "skill", comment = "技能等级")
/*     */   private List<Integer> skill;
/*     */   @DataBaseField(type = "int(11)", fieldname = "rebirth", comment = "重生等级")
/*     */   private int rebirth;
/*     */   @DataBaseField(type = "int(11)", fieldname = "rebirthExp", comment = "重生经验")
/*     */   private int rebirthExp;
/*     */   @DataBaseField(type = "int(11)", size = 9, fieldname = "artifice", comment = "炼化当前等级")
/*     */   private List<Integer> artifice;
/*     */   @DataBaseField(type = "int(11)", size = 9, fieldname = "artificeMax", comment = "炼化最大等级")
/*     */   private List<Integer> artificeMax;
/*     */   @DataBaseField(type = "int(11)", size = 9, fieldname = "artificeTimes", comment = "炼化次数")
/*     */   private List<Integer> artificeTimes;
/*     */   
/*     */   public CharacterBO() {
/*  46 */     this.id = 0L;
/*  47 */     this.pid = 0L;
/*  48 */     this.char_id = 0;
/*  49 */     this.strengthen = new ArrayList<>(9); int i;
/*  50 */     for (i = 0; i < 9; i++) {
/*  51 */       this.strengthen.add(Integer.valueOf(0));
/*     */     }
/*  53 */     this.gem = new ArrayList<>(9);
/*  54 */     for (i = 0; i < 9; i++) {
/*  55 */       this.gem.add(Integer.valueOf(0));
/*     */     }
/*  57 */     this.star = new ArrayList<>(9);
/*  58 */     for (i = 0; i < 9; i++) {
/*  59 */       this.star.add(Integer.valueOf(0));
/*     */     }
/*  61 */     this.meridian = 0;
/*  62 */     this.wing = 0;
/*  63 */     this.wingExp = 0;
/*  64 */     this.skill = new ArrayList<>(5);
/*  65 */     for (i = 0; i < 5; i++) {
/*  66 */       this.skill.add(Integer.valueOf(0));
/*     */     }
/*  68 */     this.rebirth = 0;
/*  69 */     this.rebirthExp = 0;
/*  70 */     this.artifice = new ArrayList<>(9);
/*  71 */     for (i = 0; i < 9; i++) {
/*  72 */       this.artifice.add(Integer.valueOf(0));
/*     */     }
/*  74 */     this.artificeMax = new ArrayList<>(9);
/*  75 */     for (i = 0; i < 9; i++) {
/*  76 */       this.artificeMax.add(Integer.valueOf(0));
/*     */     }
/*  78 */     this.artificeTimes = new ArrayList<>(9);
/*  79 */     for (i = 0; i < 9; i++) {
/*  80 */       this.artificeTimes.add(Integer.valueOf(0));
/*     */     }
/*     */   }
/*     */   
/*     */   public CharacterBO(ResultSet rs) throws Exception {
/*  85 */     this.id = rs.getLong(1);
/*  86 */     this.pid = rs.getLong(2);
/*  87 */     this.char_id = rs.getInt(3);
/*  88 */     this.strengthen = new ArrayList<>(9); int i;
/*  89 */     for (i = 0; i < 9; i++) {
/*  90 */       this.strengthen.add(Integer.valueOf(rs.getInt(i + 4)));
/*     */     }
/*  92 */     this.gem = new ArrayList<>(9);
/*  93 */     for (i = 0; i < 9; i++) {
/*  94 */       this.gem.add(Integer.valueOf(rs.getInt(i + 13)));
/*     */     }
/*  96 */     this.star = new ArrayList<>(9);
/*  97 */     for (i = 0; i < 9; i++) {
/*  98 */       this.star.add(Integer.valueOf(rs.getInt(i + 22)));
/*     */     }
/* 100 */     this.meridian = rs.getInt(31);
/* 101 */     this.wing = rs.getInt(32);
/* 102 */     this.wingExp = rs.getInt(33);
/* 103 */     this.skill = new ArrayList<>(5);
/* 104 */     for (i = 0; i < 5; i++) {
/* 105 */       this.skill.add(Integer.valueOf(rs.getInt(i + 34)));
/*     */     }
/* 107 */     this.rebirth = rs.getInt(39);
/* 108 */     this.rebirthExp = rs.getInt(40);
/* 109 */     this.artifice = new ArrayList<>(9);
/* 110 */     for (i = 0; i < 9; i++) {
/* 111 */       this.artifice.add(Integer.valueOf(rs.getInt(i + 41)));
/*     */     }
/* 113 */     this.artificeMax = new ArrayList<>(9);
/* 114 */     for (i = 0; i < 9; i++) {
/* 115 */       this.artificeMax.add(Integer.valueOf(rs.getInt(i + 50)));
/*     */     }
/* 117 */     this.artificeTimes = new ArrayList<>(9);
/* 118 */     for (i = 0; i < 9; i++) {
/* 119 */       this.artificeTimes.add(Integer.valueOf(rs.getInt(i + 59)));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<CharacterBO> list) throws Exception {
/* 126 */     list.add(new CharacterBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/* 131 */     return getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/* 136 */     return "`id`, `pid`, `char_id`, `strengthen_0`, `strengthen_1`, `strengthen_2`, `strengthen_3`, `strengthen_4`, `strengthen_5`, `strengthen_6`, `strengthen_7`, `strengthen_8`, `gem_0`, `gem_1`, `gem_2`, `gem_3`, `gem_4`, `gem_5`, `gem_6`, `gem_7`, `gem_8`, `star_0`, `star_1`, `star_2`, `star_3`, `star_4`, `star_5`, `star_6`, `star_7`, `star_8`, `meridian`, `wing`, `wingExp`, `skill_0`, `skill_1`, `skill_2`, `skill_3`, `skill_4`, `rebirth`, `rebirthExp`, `artifice_0`, `artifice_1`, `artifice_2`, `artifice_3`, `artifice_4`, `artifice_5`, `artifice_6`, `artifice_7`, `artifice_8`, `artificeMax_0`, `artificeMax_1`, `artificeMax_2`, `artificeMax_3`, `artificeMax_4`, `artificeMax_5`, `artificeMax_6`, `artificeMax_7`, `artificeMax_8`, `artificeTimes_0`, `artificeTimes_1`, `artificeTimes_2`, `artificeTimes_3`, `artificeTimes_4`, `artificeTimes_5`, `artificeTimes_6`, `artificeTimes_7`, `artificeTimes_8`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/* 141 */     return "`character`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/* 146 */     StringBuilder strBuf = new StringBuilder();
/* 147 */     strBuf.append("'").append(this.id).append("', ");
/* 148 */     strBuf.append("'").append(this.pid).append("', ");
/* 149 */     strBuf.append("'").append(this.char_id).append("', "); int i;
/* 150 */     for (i = 0; i < this.strengthen.size(); i++) {
/* 151 */       strBuf.append("'").append(this.strengthen.get(i)).append("', ");
/*     */     }
/* 153 */     for (i = 0; i < this.gem.size(); i++) {
/* 154 */       strBuf.append("'").append(this.gem.get(i)).append("', ");
/*     */     }
/* 156 */     for (i = 0; i < this.star.size(); i++) {
/* 157 */       strBuf.append("'").append(this.star.get(i)).append("', ");
/*     */     }
/* 159 */     strBuf.append("'").append(this.meridian).append("', ");
/* 160 */     strBuf.append("'").append(this.wing).append("', ");
/* 161 */     strBuf.append("'").append(this.wingExp).append("', ");
/* 162 */     for (i = 0; i < this.skill.size(); i++) {
/* 163 */       strBuf.append("'").append(this.skill.get(i)).append("', ");
/*     */     }
/* 165 */     strBuf.append("'").append(this.rebirth).append("', ");
/* 166 */     strBuf.append("'").append(this.rebirthExp).append("', ");
/* 167 */     for (i = 0; i < this.artifice.size(); i++) {
/* 168 */       strBuf.append("'").append(this.artifice.get(i)).append("', ");
/*     */     }
/* 170 */     for (i = 0; i < this.artificeMax.size(); i++) {
/* 171 */       strBuf.append("'").append(this.artificeMax.get(i)).append("', ");
/*     */     }
/* 173 */     for (i = 0; i < this.artificeTimes.size(); i++) {
/* 174 */       strBuf.append("'").append(this.artificeTimes.get(i)).append("', ");
/*     */     }
/* 176 */     strBuf.deleteCharAt(strBuf.length() - 2);
/* 177 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/* 182 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/* 183 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 188 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 193 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 197 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 199 */     if (pid == this.pid)
/*     */       return; 
/* 201 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 204 */     if (pid == this.pid)
/*     */       return; 
/* 206 */     this.pid = pid;
/* 207 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public int getCharId() {
/* 211 */     return this.char_id;
/*     */   } public void setCharId(int char_id) {
/* 213 */     if (char_id == this.char_id)
/*     */       return; 
/* 215 */     this.char_id = char_id;
/*     */   }
/*     */   public void saveCharId(int char_id) {
/* 218 */     if (char_id == this.char_id)
/*     */       return; 
/* 220 */     this.char_id = char_id;
/* 221 */     saveField("char_id", Integer.valueOf(char_id));
/*     */   }
/*     */   
/*     */   public int getStrengthenSize() {
/* 225 */     return this.strengthen.size();
/* 226 */   } public List<Integer> getStrengthenAll() { return new ArrayList<>(this.strengthen); }
/* 227 */   public void setStrengthenAll(int value) { for (int i = 0; i < this.strengthen.size(); ) { this.strengthen.set(i, Integer.valueOf(value)); i++; }
/* 228 */      } public void saveStrengthenAll(int value) { setStrengthenAll(value); saveAll(); } public int getStrengthen(int index) {
/* 229 */     return ((Integer)this.strengthen.get(index)).intValue();
/*     */   } public void setStrengthen(int index, int value) {
/* 231 */     if (value == ((Integer)this.strengthen.get(index)).intValue())
/*     */       return; 
/* 233 */     this.strengthen.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void saveStrengthen(int index, int value) {
/* 236 */     if (value == ((Integer)this.strengthen.get(index)).intValue())
/*     */       return; 
/* 238 */     this.strengthen.set(index, Integer.valueOf(value));
/* 239 */     saveField("strengthen_" + index, this.strengthen.get(index));
/*     */   }
/*     */   
/*     */   public int getGemSize() {
/* 243 */     return this.gem.size();
/* 244 */   } public List<Integer> getGemAll() { return new ArrayList<>(this.gem); }
/* 245 */   public void setGemAll(int value) { for (int i = 0; i < this.gem.size(); ) { this.gem.set(i, Integer.valueOf(value)); i++; }
/* 246 */      } public void saveGemAll(int value) { setGemAll(value); saveAll(); } public int getGem(int index) {
/* 247 */     return ((Integer)this.gem.get(index)).intValue();
/*     */   } public void setGem(int index, int value) {
/* 249 */     if (value == ((Integer)this.gem.get(index)).intValue())
/*     */       return; 
/* 251 */     this.gem.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void saveGem(int index, int value) {
/* 254 */     if (value == ((Integer)this.gem.get(index)).intValue())
/*     */       return; 
/* 256 */     this.gem.set(index, Integer.valueOf(value));
/* 257 */     saveField("gem_" + index, this.gem.get(index));
/*     */   }
/*     */   
/*     */   public int getStarSize() {
/* 261 */     return this.star.size();
/* 262 */   } public List<Integer> getStarAll() { return new ArrayList<>(this.star); }
/* 263 */   public void setStarAll(int value) { for (int i = 0; i < this.star.size(); ) { this.star.set(i, Integer.valueOf(value)); i++; }
/* 264 */      } public void saveStarAll(int value) { setStarAll(value); saveAll(); } public int getStar(int index) {
/* 265 */     return ((Integer)this.star.get(index)).intValue();
/*     */   } public void setStar(int index, int value) {
/* 267 */     if (value == ((Integer)this.star.get(index)).intValue())
/*     */       return; 
/* 269 */     this.star.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void saveStar(int index, int value) {
/* 272 */     if (value == ((Integer)this.star.get(index)).intValue())
/*     */       return; 
/* 274 */     this.star.set(index, Integer.valueOf(value));
/* 275 */     saveField("star_" + index, this.star.get(index));
/*     */   }
/*     */   
/*     */   public int getMeridian() {
/* 279 */     return this.meridian;
/*     */   } public void setMeridian(int meridian) {
/* 281 */     if (meridian == this.meridian)
/*     */       return; 
/* 283 */     this.meridian = meridian;
/*     */   }
/*     */   public void saveMeridian(int meridian) {
/* 286 */     if (meridian == this.meridian)
/*     */       return; 
/* 288 */     this.meridian = meridian;
/* 289 */     saveField("meridian", Integer.valueOf(meridian));
/*     */   }
/*     */   
/*     */   public int getWing() {
/* 293 */     return this.wing;
/*     */   } public void setWing(int wing) {
/* 295 */     if (wing == this.wing)
/*     */       return; 
/* 297 */     this.wing = wing;
/*     */   }
/*     */   public void saveWing(int wing) {
/* 300 */     if (wing == this.wing)
/*     */       return; 
/* 302 */     this.wing = wing;
/* 303 */     saveField("wing", Integer.valueOf(wing));
/*     */   }
/*     */   
/*     */   public int getWingExp() {
/* 307 */     return this.wingExp;
/*     */   } public void setWingExp(int wingExp) {
/* 309 */     if (wingExp == this.wingExp)
/*     */       return; 
/* 311 */     this.wingExp = wingExp;
/*     */   }
/*     */   public void saveWingExp(int wingExp) {
/* 314 */     if (wingExp == this.wingExp)
/*     */       return; 
/* 316 */     this.wingExp = wingExp;
/* 317 */     saveField("wingExp", Integer.valueOf(wingExp));
/*     */   }
/*     */   
/*     */   public int getSkillSize() {
/* 321 */     return this.skill.size();
/* 322 */   } public List<Integer> getSkillAll() { return new ArrayList<>(this.skill); }
/* 323 */   public void setSkillAll(int value) { for (int i = 0; i < this.skill.size(); ) { this.skill.set(i, Integer.valueOf(value)); i++; }
/* 324 */      } public void saveSkillAll(int value) { setSkillAll(value); saveAll(); } public int getSkill(int index) {
/* 325 */     return ((Integer)this.skill.get(index)).intValue();
/*     */   } public void setSkill(int index, int value) {
/* 327 */     if (value == ((Integer)this.skill.get(index)).intValue())
/*     */       return; 
/* 329 */     this.skill.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void saveSkill(int index, int value) {
/* 332 */     if (value == ((Integer)this.skill.get(index)).intValue())
/*     */       return; 
/* 334 */     this.skill.set(index, Integer.valueOf(value));
/* 335 */     saveField("skill_" + index, this.skill.get(index));
/*     */   }
/*     */   
/*     */   public int getRebirth() {
/* 339 */     return this.rebirth;
/*     */   } public void setRebirth(int rebirth) {
/* 341 */     if (rebirth == this.rebirth)
/*     */       return; 
/* 343 */     this.rebirth = rebirth;
/*     */   }
/*     */   public void saveRebirth(int rebirth) {
/* 346 */     if (rebirth == this.rebirth)
/*     */       return; 
/* 348 */     this.rebirth = rebirth;
/* 349 */     saveField("rebirth", Integer.valueOf(rebirth));
/*     */   }
/*     */   
/*     */   public int getRebirthExp() {
/* 353 */     return this.rebirthExp;
/*     */   } public void setRebirthExp(int rebirthExp) {
/* 355 */     if (rebirthExp == this.rebirthExp)
/*     */       return; 
/* 357 */     this.rebirthExp = rebirthExp;
/*     */   }
/*     */   public void saveRebirthExp(int rebirthExp) {
/* 360 */     if (rebirthExp == this.rebirthExp)
/*     */       return; 
/* 362 */     this.rebirthExp = rebirthExp;
/* 363 */     saveField("rebirthExp", Integer.valueOf(rebirthExp));
/*     */   }
/*     */   
/*     */   public int getArtificeSize() {
/* 367 */     return this.artifice.size();
/* 368 */   } public List<Integer> getArtificeAll() { return new ArrayList<>(this.artifice); }
/* 369 */   public void setArtificeAll(int value) { for (int i = 0; i < this.artifice.size(); ) { this.artifice.set(i, Integer.valueOf(value)); i++; }
/* 370 */      } public void saveArtificeAll(int value) { setArtificeAll(value); saveAll(); } public int getArtifice(int index) {
/* 371 */     return ((Integer)this.artifice.get(index)).intValue();
/*     */   } public void setArtifice(int index, int value) {
/* 373 */     if (value == ((Integer)this.artifice.get(index)).intValue())
/*     */       return; 
/* 375 */     this.artifice.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void saveArtifice(int index, int value) {
/* 378 */     if (value == ((Integer)this.artifice.get(index)).intValue())
/*     */       return; 
/* 380 */     this.artifice.set(index, Integer.valueOf(value));
/* 381 */     saveField("artifice_" + index, this.artifice.get(index));
/*     */   }
/*     */   
/*     */   public int getArtificeMaxSize() {
/* 385 */     return this.artificeMax.size();
/* 386 */   } public List<Integer> getArtificeMaxAll() { return new ArrayList<>(this.artificeMax); }
/* 387 */   public void setArtificeMaxAll(int value) { for (int i = 0; i < this.artificeMax.size(); ) { this.artificeMax.set(i, Integer.valueOf(value)); i++; }
/* 388 */      } public void saveArtificeMaxAll(int value) { setArtificeMaxAll(value); saveAll(); } public int getArtificeMax(int index) {
/* 389 */     return ((Integer)this.artificeMax.get(index)).intValue();
/*     */   } public void setArtificeMax(int index, int value) {
/* 391 */     if (value == ((Integer)this.artificeMax.get(index)).intValue())
/*     */       return; 
/* 393 */     this.artificeMax.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void saveArtificeMax(int index, int value) {
/* 396 */     if (value == ((Integer)this.artificeMax.get(index)).intValue())
/*     */       return; 
/* 398 */     this.artificeMax.set(index, Integer.valueOf(value));
/* 399 */     saveField("artificeMax_" + index, this.artificeMax.get(index));
/*     */   }
/*     */   
/*     */   public int getArtificeTimesSize() {
/* 403 */     return this.artificeTimes.size();
/* 404 */   } public List<Integer> getArtificeTimesAll() { return new ArrayList<>(this.artificeTimes); }
/* 405 */   public void setArtificeTimesAll(int value) { for (int i = 0; i < this.artificeTimes.size(); ) { this.artificeTimes.set(i, Integer.valueOf(value)); i++; }
/* 406 */      } public void saveArtificeTimesAll(int value) { setArtificeTimesAll(value); saveAll(); } public int getArtificeTimes(int index) {
/* 407 */     return ((Integer)this.artificeTimes.get(index)).intValue();
/*     */   } public void setArtificeTimes(int index, int value) {
/* 409 */     if (value == ((Integer)this.artificeTimes.get(index)).intValue())
/*     */       return; 
/* 411 */     this.artificeTimes.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void saveArtificeTimes(int index, int value) {
/* 414 */     if (value == ((Integer)this.artificeTimes.get(index)).intValue())
/*     */       return; 
/* 416 */     this.artificeTimes.set(index, Integer.valueOf(value));
/* 417 */     saveField("artificeTimes_" + index, this.artificeTimes.get(index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 424 */     StringBuilder sBuilder = new StringBuilder();
/* 425 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 426 */     sBuilder.append(" `char_id` = '").append(this.char_id).append("',"); int i;
/* 427 */     for (i = 0; i < this.strengthen.size(); i++) {
/* 428 */       sBuilder.append(" `strengthen_").append(i).append("` = '").append(this.strengthen.get(i)).append("',");
/*     */     }
/* 430 */     for (i = 0; i < this.gem.size(); i++) {
/* 431 */       sBuilder.append(" `gem_").append(i).append("` = '").append(this.gem.get(i)).append("',");
/*     */     }
/* 433 */     for (i = 0; i < this.star.size(); i++) {
/* 434 */       sBuilder.append(" `star_").append(i).append("` = '").append(this.star.get(i)).append("',");
/*     */     }
/* 436 */     sBuilder.append(" `meridian` = '").append(this.meridian).append("',");
/* 437 */     sBuilder.append(" `wing` = '").append(this.wing).append("',");
/* 438 */     sBuilder.append(" `wingExp` = '").append(this.wingExp).append("',");
/* 439 */     for (i = 0; i < this.skill.size(); i++) {
/* 440 */       sBuilder.append(" `skill_").append(i).append("` = '").append(this.skill.get(i)).append("',");
/*     */     }
/* 442 */     sBuilder.append(" `rebirth` = '").append(this.rebirth).append("',");
/* 443 */     sBuilder.append(" `rebirthExp` = '").append(this.rebirthExp).append("',");
/* 444 */     for (i = 0; i < this.artifice.size(); i++) {
/* 445 */       sBuilder.append(" `artifice_").append(i).append("` = '").append(this.artifice.get(i)).append("',");
/*     */     }
/* 447 */     for (i = 0; i < this.artificeMax.size(); i++) {
/* 448 */       sBuilder.append(" `artificeMax_").append(i).append("` = '").append(this.artificeMax.get(i)).append("',");
/*     */     }
/* 450 */     for (i = 0; i < this.artificeTimes.size(); i++) {
/* 451 */       sBuilder.append(" `artificeTimes_").append(i).append("` = '").append(this.artificeTimes.get(i)).append("',");
/*     */     }
/* 453 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 454 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 458 */     String sql = "CREATE TABLE IF NOT EXISTS `character` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`char_id` int(11) NOT NULL DEFAULT '0' COMMENT '角色id',`strengthen_0` int(11) NOT NULL DEFAULT '0' COMMENT '强化等级',`strengthen_1` int(11) NOT NULL DEFAULT '0' COMMENT '强化等级',`strengthen_2` int(11) NOT NULL DEFAULT '0' COMMENT '强化等级',`strengthen_3` int(11) NOT NULL DEFAULT '0' COMMENT '强化等级',`strengthen_4` int(11) NOT NULL DEFAULT '0' COMMENT '强化等级',`strengthen_5` int(11) NOT NULL DEFAULT '0' COMMENT '强化等级',`strengthen_6` int(11) NOT NULL DEFAULT '0' COMMENT '强化等级',`strengthen_7` int(11) NOT NULL DEFAULT '0' COMMENT '强化等级',`strengthen_8` int(11) NOT NULL DEFAULT '0' COMMENT '强化等级',`gem_0` int(11) NOT NULL DEFAULT '0' COMMENT '宝石',`gem_1` int(11) NOT NULL DEFAULT '0' COMMENT '宝石',`gem_2` int(11) NOT NULL DEFAULT '0' COMMENT '宝石',`gem_3` int(11) NOT NULL DEFAULT '0' COMMENT '宝石',`gem_4` int(11) NOT NULL DEFAULT '0' COMMENT '宝石',`gem_5` int(11) NOT NULL DEFAULT '0' COMMENT '宝石',`gem_6` int(11) NOT NULL DEFAULT '0' COMMENT '宝石',`gem_7` int(11) NOT NULL DEFAULT '0' COMMENT '宝石',`gem_8` int(11) NOT NULL DEFAULT '0' COMMENT '宝石',`star_0` int(11) NOT NULL DEFAULT '0' COMMENT '星级',`star_1` int(11) NOT NULL DEFAULT '0' COMMENT '星级',`star_2` int(11) NOT NULL DEFAULT '0' COMMENT '星级',`star_3` int(11) NOT NULL DEFAULT '0' COMMENT '星级',`star_4` int(11) NOT NULL DEFAULT '0' COMMENT '星级',`star_5` int(11) NOT NULL DEFAULT '0' COMMENT '星级',`star_6` int(11) NOT NULL DEFAULT '0' COMMENT '星级',`star_7` int(11) NOT NULL DEFAULT '0' COMMENT '星级',`star_8` int(11) NOT NULL DEFAULT '0' COMMENT '星级',`meridian` int(11) NOT NULL DEFAULT '0' COMMENT '经脉',`wing` int(11) NOT NULL DEFAULT '0' COMMENT '翅膀',`wingExp` int(11) NOT NULL DEFAULT '0' COMMENT '翅膀当前经验',`skill_0` int(11) NOT NULL DEFAULT '0' COMMENT '技能等级',`skill_1` int(11) NOT NULL DEFAULT '0' COMMENT '技能等级',`skill_2` int(11) NOT NULL DEFAULT '0' COMMENT '技能等级',`skill_3` int(11) NOT NULL DEFAULT '0' COMMENT '技能等级',`skill_4` int(11) NOT NULL DEFAULT '0' COMMENT '技能等级',`rebirth` int(11) NOT NULL DEFAULT '0' COMMENT '重生等级',`rebirthExp` int(11) NOT NULL DEFAULT '0' COMMENT '重生经验',`artifice_0` int(11) NOT NULL DEFAULT '0' COMMENT '炼化当前等级',`artifice_1` int(11) NOT NULL DEFAULT '0' COMMENT '炼化当前等级',`artifice_2` int(11) NOT NULL DEFAULT '0' COMMENT '炼化当前等级',`artifice_3` int(11) NOT NULL DEFAULT '0' COMMENT '炼化当前等级',`artifice_4` int(11) NOT NULL DEFAULT '0' COMMENT '炼化当前等级',`artifice_5` int(11) NOT NULL DEFAULT '0' COMMENT '炼化当前等级',`artifice_6` int(11) NOT NULL DEFAULT '0' COMMENT '炼化当前等级',`artifice_7` int(11) NOT NULL DEFAULT '0' COMMENT '炼化当前等级',`artifice_8` int(11) NOT NULL DEFAULT '0' COMMENT '炼化当前等级',`artificeMax_0` int(11) NOT NULL DEFAULT '0' COMMENT '炼化最大等级',`artificeMax_1` int(11) NOT NULL DEFAULT '0' COMMENT '炼化最大等级',`artificeMax_2` int(11) NOT NULL DEFAULT '0' COMMENT '炼化最大等级',`artificeMax_3` int(11) NOT NULL DEFAULT '0' COMMENT '炼化最大等级',`artificeMax_4` int(11) NOT NULL DEFAULT '0' COMMENT '炼化最大等级',`artificeMax_5` int(11) NOT NULL DEFAULT '0' COMMENT '炼化最大等级',`artificeMax_6` int(11) NOT NULL DEFAULT '0' COMMENT '炼化最大等级',`artificeMax_7` int(11) NOT NULL DEFAULT '0' COMMENT '炼化最大等级',`artificeMax_8` int(11) NOT NULL DEFAULT '0' COMMENT '炼化最大等级',`artificeTimes_0` int(11) NOT NULL DEFAULT '0' COMMENT '炼化次数',`artificeTimes_1` int(11) NOT NULL DEFAULT '0' COMMENT '炼化次数',`artificeTimes_2` int(11) NOT NULL DEFAULT '0' COMMENT '炼化次数',`artificeTimes_3` int(11) NOT NULL DEFAULT '0' COMMENT '炼化次数',`artificeTimes_4` int(11) NOT NULL DEFAULT '0' COMMENT '炼化次数',`artificeTimes_5` int(11) NOT NULL DEFAULT '0' COMMENT '炼化次数',`artificeTimes_6` int(11) NOT NULL DEFAULT '0' COMMENT '炼化次数',`artificeTimes_7` int(11) NOT NULL DEFAULT '0' COMMENT '炼化次数',`artificeTimes_8` int(11) NOT NULL DEFAULT '0' COMMENT '炼化次数',PRIMARY KEY (`id`)) COMMENT='玩家信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 527 */       ServerConfig.getInitialID() + 1L);
/* 528 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/CharacterBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */