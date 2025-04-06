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
/*     */ public class ConfRewardBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "int(11)", fieldname = "rewardID", comment = "礼包ID")
/*     */   private int rewardID;
/*     */   @DataBaseField(type = "int(11)", fieldname = "price", comment = "礼包价格")
/*     */   private int price;
/*     */   @DataBaseField(type = "varchar(64)", fieldname = "iconID", comment = "礼包图标")
/*     */   private String iconID;
/*     */   @DataBaseField(type = "varchar(64)", fieldname = "name", comment = "礼包名称")
/*     */   private String name;
/*     */   @DataBaseField(type = "varchar(128)", fieldname = "itemID", comment = "物品ID")
/*     */   private String itemID;
/*     */   @DataBaseField(type = "varchar(128)", fieldname = "itemCount", comment = "物品数量")
/*     */   private String itemCount;
/*     */   @DataBaseField(type = "varchar(512)", fieldname = "rewardDescribe", comment = "礼包描述")
/*     */   private String rewardDescribe;
/*     */   @DataBaseField(type = "int(11)", fieldname = "createTime", comment = "创建时间")
/*     */   private int createTime;
/*     */   
/*     */   public ConfRewardBO() {
/*  34 */     this.id = 0L;
/*  35 */     this.rewardID = 0;
/*  36 */     this.price = 0;
/*  37 */     this.iconID = "";
/*  38 */     this.name = "";
/*  39 */     this.itemID = "";
/*  40 */     this.itemCount = "";
/*  41 */     this.rewardDescribe = "";
/*  42 */     this.createTime = 0;
/*     */   }
/*     */   
/*     */   public ConfRewardBO(ResultSet rs) throws Exception {
/*  46 */     this.id = rs.getLong(1);
/*  47 */     this.rewardID = rs.getInt(2);
/*  48 */     this.price = rs.getInt(3);
/*  49 */     this.iconID = rs.getString(4);
/*  50 */     this.name = rs.getString(5);
/*  51 */     this.itemID = rs.getString(6);
/*  52 */     this.itemCount = rs.getString(7);
/*  53 */     this.rewardDescribe = rs.getString(8);
/*  54 */     this.createTime = rs.getInt(9);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<ConfRewardBO> list) throws Exception {
/*  60 */     list.add(new ConfRewardBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  65 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  70 */     return "`id`, `rewardID`, `price`, `iconID`, `name`, `itemID`, `itemCount`, `rewardDescribe`, `createTime`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  75 */     return "`confReward`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  80 */     StringBuilder strBuf = new StringBuilder();
/*  81 */     strBuf.append("'").append(this.id).append("', ");
/*  82 */     strBuf.append("'").append(this.rewardID).append("', ");
/*  83 */     strBuf.append("'").append(this.price).append("', ");
/*  84 */     strBuf.append("'").append((this.iconID == null) ? null : this.iconID.replace("'", "''")).append("', ");
/*  85 */     strBuf.append("'").append((this.name == null) ? null : this.name.replace("'", "''")).append("', ");
/*  86 */     strBuf.append("'").append((this.itemID == null) ? null : this.itemID.replace("'", "''")).append("', ");
/*  87 */     strBuf.append("'").append((this.itemCount == null) ? null : this.itemCount.replace("'", "''")).append("', ");
/*  88 */     strBuf.append("'").append((this.rewardDescribe == null) ? null : this.rewardDescribe.replace("'", "''")).append("', ");
/*  89 */     strBuf.append("'").append(this.createTime).append("', ");
/*  90 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  91 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  96 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  97 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 102 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 107 */     return this.id;
/*     */   }
/*     */   
/*     */   public int getRewardID() {
/* 111 */     return this.rewardID;
/*     */   } public void setRewardID(int rewardID) {
/* 113 */     if (rewardID == this.rewardID)
/*     */       return; 
/* 115 */     this.rewardID = rewardID;
/*     */   }
/*     */   public void saveRewardID(int rewardID) {
/* 118 */     if (rewardID == this.rewardID)
/*     */       return; 
/* 120 */     this.rewardID = rewardID;
/* 121 */     saveField("rewardID", Integer.valueOf(rewardID));
/*     */   }
/*     */   
/*     */   public int getPrice() {
/* 125 */     return this.price;
/*     */   } public void setPrice(int price) {
/* 127 */     if (price == this.price)
/*     */       return; 
/* 129 */     this.price = price;
/*     */   }
/*     */   public void savePrice(int price) {
/* 132 */     if (price == this.price)
/*     */       return; 
/* 134 */     this.price = price;
/* 135 */     saveField("price", Integer.valueOf(price));
/*     */   }
/*     */   
/*     */   public String getIconID() {
/* 139 */     return this.iconID;
/*     */   } public void setIconID(String iconID) {
/* 141 */     if (iconID.equals(this.iconID))
/*     */       return; 
/* 143 */     this.iconID = iconID;
/*     */   }
/*     */   public void saveIconID(String iconID) {
/* 146 */     if (iconID.equals(this.iconID))
/*     */       return; 
/* 148 */     this.iconID = iconID;
/* 149 */     saveField("iconID", iconID);
/*     */   }
/*     */   
/*     */   public String getName() {
/* 153 */     return this.name;
/*     */   } public void setName(String name) {
/* 155 */     if (name.equals(this.name))
/*     */       return; 
/* 157 */     this.name = name;
/*     */   }
/*     */   public void saveName(String name) {
/* 160 */     if (name.equals(this.name))
/*     */       return; 
/* 162 */     this.name = name;
/* 163 */     saveField("name", name);
/*     */   }
/*     */   
/*     */   public String getItemID() {
/* 167 */     return this.itemID;
/*     */   } public void setItemID(String itemID) {
/* 169 */     if (itemID.equals(this.itemID))
/*     */       return; 
/* 171 */     this.itemID = itemID;
/*     */   }
/*     */   public void saveItemID(String itemID) {
/* 174 */     if (itemID.equals(this.itemID))
/*     */       return; 
/* 176 */     this.itemID = itemID;
/* 177 */     saveField("itemID", itemID);
/*     */   }
/*     */   
/*     */   public String getItemCount() {
/* 181 */     return this.itemCount;
/*     */   } public void setItemCount(String itemCount) {
/* 183 */     if (itemCount.equals(this.itemCount))
/*     */       return; 
/* 185 */     this.itemCount = itemCount;
/*     */   }
/*     */   public void saveItemCount(String itemCount) {
/* 188 */     if (itemCount.equals(this.itemCount))
/*     */       return; 
/* 190 */     this.itemCount = itemCount;
/* 191 */     saveField("itemCount", itemCount);
/*     */   }
/*     */   
/*     */   public String getRewardDescribe() {
/* 195 */     return this.rewardDescribe;
/*     */   } public void setRewardDescribe(String rewardDescribe) {
/* 197 */     if (rewardDescribe.equals(this.rewardDescribe))
/*     */       return; 
/* 199 */     this.rewardDescribe = rewardDescribe;
/*     */   }
/*     */   public void saveRewardDescribe(String rewardDescribe) {
/* 202 */     if (rewardDescribe.equals(this.rewardDescribe))
/*     */       return; 
/* 204 */     this.rewardDescribe = rewardDescribe;
/* 205 */     saveField("rewardDescribe", rewardDescribe);
/*     */   }
/*     */   
/*     */   public int getCreateTime() {
/* 209 */     return this.createTime;
/*     */   } public void setCreateTime(int createTime) {
/* 211 */     if (createTime == this.createTime)
/*     */       return; 
/* 213 */     this.createTime = createTime;
/*     */   }
/*     */   public void saveCreateTime(int createTime) {
/* 216 */     if (createTime == this.createTime)
/*     */       return; 
/* 218 */     this.createTime = createTime;
/* 219 */     saveField("createTime", Integer.valueOf(createTime));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 226 */     StringBuilder sBuilder = new StringBuilder();
/* 227 */     sBuilder.append(" `rewardID` = '").append(this.rewardID).append("',");
/* 228 */     sBuilder.append(" `price` = '").append(this.price).append("',");
/* 229 */     sBuilder.append(" `iconID` = '").append((this.iconID == null) ? null : this.iconID.replace("'", "''")).append("',");
/* 230 */     sBuilder.append(" `name` = '").append((this.name == null) ? null : this.name.replace("'", "''")).append("',");
/* 231 */     sBuilder.append(" `itemID` = '").append((this.itemID == null) ? null : this.itemID.replace("'", "''")).append("',");
/* 232 */     sBuilder.append(" `itemCount` = '").append((this.itemCount == null) ? null : this.itemCount.replace("'", "''")).append("',");
/* 233 */     sBuilder.append(" `rewardDescribe` = '").append((this.rewardDescribe == null) ? null : this.rewardDescribe.replace("'", "''")).append("',");
/* 234 */     sBuilder.append(" `createTime` = '").append(this.createTime).append("',");
/* 235 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 236 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 240 */     String sql = "CREATE TABLE IF NOT EXISTS `confReward` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`rewardID` int(11) NOT NULL DEFAULT '0' COMMENT '礼包ID',`price` int(11) NOT NULL DEFAULT '0' COMMENT '礼包价格',`iconID` varchar(64) NOT NULL DEFAULT '' COMMENT '礼包图标',`name` varchar(64) NOT NULL DEFAULT '' COMMENT '礼包名称',`itemID` varchar(128) NOT NULL DEFAULT '' COMMENT '物品ID',`itemCount` varchar(128) NOT NULL DEFAULT '' COMMENT '物品数量',`rewardDescribe` varchar(512) NOT NULL DEFAULT '' COMMENT '礼包描述',`createTime` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',PRIMARY KEY (`id`)) COMMENT='后台配置礼包'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 251 */       ServerConfig.getInitialID() + 1L);
/* 252 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/ConfRewardBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */