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
/*     */ public class GlobalRichRiskBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "int(11)", fieldname = "mapId", comment = "地图Id-RichRiskMap表ID")
/*     */   private int mapId;
/*     */   @DataBaseField(type = "int(11)", fieldname = "chapter", comment = "地图章节")
/*     */   private int chapter;
/*     */   @DataBaseField(type = "int(11)", fieldname = "width", comment = "宽度")
/*     */   private int width;
/*     */   @DataBaseField(type = "int(11)", fieldname = "height", comment = "高度")
/*     */   private int height;
/*     */   @DataBaseField(type = "int(11)", fieldname = "createTime", comment = "创建时间")
/*     */   private int createTime;
/*     */   
/*     */   public GlobalRichRiskBO() {
/*  28 */     this.id = 0L;
/*  29 */     this.mapId = 0;
/*  30 */     this.chapter = 0;
/*  31 */     this.width = 0;
/*  32 */     this.height = 0;
/*  33 */     this.createTime = 0;
/*     */   }
/*     */   
/*     */   public GlobalRichRiskBO(ResultSet rs) throws Exception {
/*  37 */     this.id = rs.getLong(1);
/*  38 */     this.mapId = rs.getInt(2);
/*  39 */     this.chapter = rs.getInt(3);
/*  40 */     this.width = rs.getInt(4);
/*  41 */     this.height = rs.getInt(5);
/*  42 */     this.createTime = rs.getInt(6);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<GlobalRichRiskBO> list) throws Exception {
/*  48 */     list.add(new GlobalRichRiskBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  53 */     return getChapter();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  58 */     return "`id`, `mapId`, `chapter`, `width`, `height`, `createTime`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  63 */     return "`globalRichRisk`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  68 */     StringBuilder strBuf = new StringBuilder();
/*  69 */     strBuf.append("'").append(this.id).append("', ");
/*  70 */     strBuf.append("'").append(this.mapId).append("', ");
/*  71 */     strBuf.append("'").append(this.chapter).append("', ");
/*  72 */     strBuf.append("'").append(this.width).append("', ");
/*  73 */     strBuf.append("'").append(this.height).append("', ");
/*  74 */     strBuf.append("'").append(this.createTime).append("', ");
/*  75 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  76 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  81 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  82 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  87 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/*  92 */     return this.id;
/*     */   }
/*     */   
/*     */   public int getMapId() {
/*  96 */     return this.mapId;
/*     */   } public void setMapId(int mapId) {
/*  98 */     if (mapId == this.mapId)
/*     */       return; 
/* 100 */     this.mapId = mapId;
/*     */   }
/*     */   public void saveMapId(int mapId) {
/* 103 */     if (mapId == this.mapId)
/*     */       return; 
/* 105 */     this.mapId = mapId;
/* 106 */     saveField("mapId", Integer.valueOf(mapId));
/*     */   }
/*     */   
/*     */   public int getChapter() {
/* 110 */     return this.chapter;
/*     */   } public void setChapter(int chapter) {
/* 112 */     if (chapter == this.chapter)
/*     */       return; 
/* 114 */     this.chapter = chapter;
/*     */   }
/*     */   public void saveChapter(int chapter) {
/* 117 */     if (chapter == this.chapter)
/*     */       return; 
/* 119 */     this.chapter = chapter;
/* 120 */     saveField("chapter", Integer.valueOf(chapter));
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 124 */     return this.width;
/*     */   } public void setWidth(int width) {
/* 126 */     if (width == this.width)
/*     */       return; 
/* 128 */     this.width = width;
/*     */   }
/*     */   public void saveWidth(int width) {
/* 131 */     if (width == this.width)
/*     */       return; 
/* 133 */     this.width = width;
/* 134 */     saveField("width", Integer.valueOf(width));
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 138 */     return this.height;
/*     */   } public void setHeight(int height) {
/* 140 */     if (height == this.height)
/*     */       return; 
/* 142 */     this.height = height;
/*     */   }
/*     */   public void saveHeight(int height) {
/* 145 */     if (height == this.height)
/*     */       return; 
/* 147 */     this.height = height;
/* 148 */     saveField("height", Integer.valueOf(height));
/*     */   }
/*     */   
/*     */   public int getCreateTime() {
/* 152 */     return this.createTime;
/*     */   } public void setCreateTime(int createTime) {
/* 154 */     if (createTime == this.createTime)
/*     */       return; 
/* 156 */     this.createTime = createTime;
/*     */   }
/*     */   public void saveCreateTime(int createTime) {
/* 159 */     if (createTime == this.createTime)
/*     */       return; 
/* 161 */     this.createTime = createTime;
/* 162 */     saveField("createTime", Integer.valueOf(createTime));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 169 */     StringBuilder sBuilder = new StringBuilder();
/* 170 */     sBuilder.append(" `mapId` = '").append(this.mapId).append("',");
/* 171 */     sBuilder.append(" `chapter` = '").append(this.chapter).append("',");
/* 172 */     sBuilder.append(" `width` = '").append(this.width).append("',");
/* 173 */     sBuilder.append(" `height` = '").append(this.height).append("',");
/* 174 */     sBuilder.append(" `createTime` = '").append(this.createTime).append("',");
/* 175 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 176 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 180 */     String sql = "CREATE TABLE IF NOT EXISTS `globalRichRisk` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`mapId` int(11) NOT NULL DEFAULT '0' COMMENT '地图Id-RichRiskMap表ID',`chapter` int(11) NOT NULL DEFAULT '0' COMMENT '地图章节',`width` int(11) NOT NULL DEFAULT '0' COMMENT '宽度',`height` int(11) NOT NULL DEFAULT '0' COMMENT '高度',`createTime` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',KEY `chapter` (`chapter`),PRIMARY KEY (`id`)) COMMENT='大富翁地图信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 189 */       ServerConfig.getInitialID() + 1L);
/* 190 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/GlobalRichRiskBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */