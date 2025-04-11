package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GlobalRichRiskBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "int(11)", fieldname = "mapId", comment = "地图Id-RichRiskMap表ID")
    private int mapId;
    @DataBaseField(type = "int(11)", fieldname = "chapter", comment = "地图章节")
    private int chapter;
    @DataBaseField(type = "int(11)", fieldname = "width", comment = "宽度")
    private int width;
    @DataBaseField(type = "int(11)", fieldname = "height", comment = "高度")
    private int height;
    @DataBaseField(type = "int(11)", fieldname = "createTime", comment = "创建时间")
    private int createTime;

    public GlobalRichRiskBO() {
        this.id = 0L;
        this.mapId = 0;
        this.chapter = 0;
        this.width = 0;
        this.height = 0;
        this.createTime = 0;
    }

    public GlobalRichRiskBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.mapId = rs.getInt(2);
        this.chapter = rs.getInt(3);
        this.width = rs.getInt(4);
        this.height = rs.getInt(5);
        this.createTime = rs.getInt(6);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `globalRichRisk` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`mapId` int(11) NOT NULL DEFAULT '0' COMMENT '地图Id-RichRiskMap表ID',`chapter` int(11) NOT NULL DEFAULT '0' COMMENT '地图章节',`width` int(11) NOT NULL DEFAULT '0' COMMENT '宽度',`height` int(11) NOT NULL DEFAULT '0' COMMENT '高度',`createTime` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',KEY `chapter` (`chapter`),PRIMARY KEY (`id`)) COMMENT='大富翁地图信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<GlobalRichRiskBO> list) throws Exception {
        list.add(new GlobalRichRiskBO(rs));
    }

    public long getAsynTaskTag() {
        return getChapter();
    }

    public String getItemsName() {
        return "`id`, `mapId`, `chapter`, `width`, `height`, `createTime`";
    }

    public String getTableName() {
        return "`globalRichRisk`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.mapId).append("', ");
        strBuf.append("'").append(this.chapter).append("', ");
        strBuf.append("'").append(this.width).append("', ");
        strBuf.append("'").append(this.height).append("', ");
        strBuf.append("'").append(this.createTime).append("', ");
        strBuf.deleteCharAt(strBuf.length() - 2);
        return strBuf.toString();
    }

    public ArrayList<byte[]> getInsertValueBytes() {
        ArrayList<byte[]> ret = (ArrayList) new ArrayList<>();
        return ret;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long iID) {
        this.id = iID;
    }

    public int getMapId() {
        return this.mapId;
    }

    public void setMapId(int mapId) {
        if (mapId == this.mapId)
            return;
        this.mapId = mapId;
    }

    public void saveMapId(int mapId) {
        if (mapId == this.mapId)
            return;
        this.mapId = mapId;
        saveField("mapId", Integer.valueOf(mapId));
    }

    public int getChapter() {
        return this.chapter;
    }

    public void setChapter(int chapter) {
        if (chapter == this.chapter)
            return;
        this.chapter = chapter;
    }

    public void saveChapter(int chapter) {
        if (chapter == this.chapter)
            return;
        this.chapter = chapter;
        saveField("chapter", Integer.valueOf(chapter));
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        if (width == this.width)
            return;
        this.width = width;
    }

    public void saveWidth(int width) {
        if (width == this.width)
            return;
        this.width = width;
        saveField("width", Integer.valueOf(width));
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        if (height == this.height)
            return;
        this.height = height;
    }

    public void saveHeight(int height) {
        if (height == this.height)
            return;
        this.height = height;
        saveField("height", Integer.valueOf(height));
    }

    public int getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(int createTime) {
        if (createTime == this.createTime)
            return;
        this.createTime = createTime;
    }

    public void saveCreateTime(int createTime) {
        if (createTime == this.createTime)
            return;
        this.createTime = createTime;
        saveField("createTime", Integer.valueOf(createTime));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `mapId` = '").append(this.mapId).append("',");
        sBuilder.append(" `chapter` = '").append(this.chapter).append("',");
        sBuilder.append(" `width` = '").append(this.width).append("',");
        sBuilder.append(" `height` = '").append(this.height).append("',");
        sBuilder.append(" `createTime` = '").append(this.createTime).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

