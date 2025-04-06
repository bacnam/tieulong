/*     */ package core.network.proto;
/*     */ 
/*     */ import business.player.item.Reward;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Player
/*     */ {
/*     */   public static class FullInfo
/*     */   {
/*     */     public long pid;
/*     */     public int sid;
/*     */     public String name;
/*     */     public int icon;
/*     */     public int vipExp;
/*     */     public int vipLv;
/*     */     public int lv;
/*     */     public int exp;
/*     */     public int sex;
/*     */     public int createTime;
/*     */     public int dungeonlv;
/*     */     public int gold;
/*     */     public int crystal;
/*     */     public int strengthenMaterial;
/*     */     public int gemMaterial;
/*     */     public int starMaterial;
/*     */     public int merMaterial;
/*     */     public int wingMaterial;
/*     */     public int arenaToken;
/*     */     public int equipInstanceMaterial;
/*     */     public int gemInstanceMaterial;
/*     */     public int meridianInstanceMaterial;
/*     */     public int redPiece;
/*     */     public int extPackage;
/*     */     public int artificeMaterial;
/*     */     public int warspiritTalentMaterial;
/*     */     public int warspiritLv;
/*     */     public int warspiritExp;
/*     */     public int warspiritTalent;
/*     */     public int buyPackageTimes;
/*     */     public int lottery;
/*     */     public int dressMaterial;
/*     */     public List<Character.CharInfo> characters;
/*     */     public List<WarSpiritInfo> warspirits;
/*     */     public List<Guild.GuildSkill> guildSkill;
/*     */     public List<DressInfo> dresses;
/*     */   }
/*     */   
/*     */   public static class FightInfo
/*     */   {
/*     */     public long pid;
/*     */     public String name;
/*     */     public int icon;
/*     */     public int vipExp;
/*     */     public int vipLv;
/*     */     public int lv;
/*     */     public int warspiritLv;
/*     */     public int warspiritTalent;
/*     */     public List<Character.CharInfo> characters;
/*     */     public WarSpiritInfo warspirit;
/*     */     public List<TitleInfo> title;
/*     */     public int LingBaoLevel;
/*     */     public String guildName;
/*     */     public String guildJob;
/*     */   }
/*     */   
/*     */   public static class Summary
/*     */   {
/*     */     public long pid;
/*     */     public String name;
/*     */     public int lv;
/*     */     public int icon;
/*     */     public int vipLv;
/*     */     public int power;
/*     */     public int sex;
/*     */     public boolean is_married;
/*     */     public boolean MonthCard;
/*     */     public boolean YearCard;
/*     */   }
/*     */   
/*     */   public static class showModle
/*     */   {
/*     */     public long pid;
/*     */     public String name;
/*     */     public int icon;
/*     */     public int model;
/*     */     public int wing;
/*     */     public int modelDress;
/*     */     public int wingDress;
/*     */     public int puppet_id;
/*     */     public boolean is_puppet;
/*     */     public List<Double> hp;
/*     */     public List<Double> maxhp;
/*     */   }
/*     */   
/*     */   public static class Property {
/*     */     public String name;
/*     */     public int value;
/*     */     
/*     */     public Property(String name, int value) {
/* 100 */       this.name = name;
/* 101 */       this.value = value;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class OfflineReward {
/*     */     Reward reward;
/*     */     int time;
/*     */     
/*     */     public OfflineReward(Reward reward, int time) {
/* 110 */       this.reward = reward;
/* 111 */       this.time = time;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RechargeNotify
/*     */   {
/*     */     Reward reward;
/*     */     String goodsId;
/*     */     
/*     */     public RechargeNotify(Reward reward, String goodsId) {
/* 121 */       this.reward = reward;
/* 122 */       this.goodsId = goodsId;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/Player.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */