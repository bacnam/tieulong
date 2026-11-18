package core.database.world.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WorldMailBO extends BaseBO {
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "id",
        comment = "数据库表唯一键值"
    )
    private long id;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "cid",
        comment = "玩家cid"
    )
    private long cid;
    @DataBaseField(
        type = "int(11)",
        fieldname = "mailID",
        comment = "邮件静态Id"
    )
    private int mailID;
    @DataBaseField(
        type = "varchar(500)",
        fieldname = "uniformIDList",
        comment = "物品Id列表"
    )
    private String uniformIDList;
    @DataBaseField(
        type = "varchar(500)",
        fieldname = "uniformCountList",
        comment = "物品数量列表"
    )
    private String uniformCountList;
    @DataBaseField(
        type = "varchar(500)",
        fieldname = "params",
        comment = "额外的参数列表"
    )
    private String params;

    public WorldMailBO() {
        this.id = 0L;
        this.cid = 0L;
        this.mailID = 0;
        this.uniformIDList = "";
        this.uniformCountList = "";
        this.params = "";
    }

    public WorldMailBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.cid = rs.getLong(2);
        this.mailID = rs.getInt(3);
        this.uniformIDList = rs.getString(4);
        this.uniformCountList = rs.getString(5);
        this.params = rs.getString(6);
    }

    @Override
    public void getFromResultSet(ResultSet rs, List list) throws Exception {
        list.add(new WorldMailBO(rs));
    }

    @Override
    public long getAsynTaskTag() {
        return this.getCid();
    }

    @Override
    public String getItemsName() {
        return "`id`, `cid`, `mailID`, `uniformIDList`, `uniformCountList`, `params`";
    }

    @Override
    public String getTableName() {
        return "`worldMail`";
    }

    @Override
    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.cid).append("', ");
        strBuf.append("'").append(this.mailID).append("', ");
        strBuf.append("'").append(this.uniformIDList == null ? null : this.uniformIDList.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.uniformCountList == null ? null : this.uniformCountList.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.params == null ? null : this.params.replace("'", "''")).append("', ");
        strBuf.deleteCharAt(strBuf.length() - 2);
        return strBuf.toString();
    }

    @Override
    public ArrayList<byte[]> getInsertValueBytes() {
        return new ArrayList<>();
    }

    @Override
    public void setId(long iID) {
        this.id = iID;
    }

    @Override
    public long getId() {
        return this.id;
    }

    public long getCid() {
        return this.cid;
    }

    public void setCid(long cid) {
        if (cid != this.cid) {
            this.cid = cid;
        }
    }

    public void saveCid(long cid) {
        if (cid != this.cid) {
            this.cid = cid;
            this.saveField("cid", cid);
        }
    }

    public int getMailID() {
        return this.mailID;
    }

    public void setMailID(int mailID) {
        if (mailID != this.mailID) {
            this.mailID = mailID;
        }
    }

    public void saveMailID(int mailID) {
        if (mailID != this.mailID) {
            this.mailID = mailID;
            this.saveField("mailID", mailID);
        }
    }

    public String getUniformIDList() {
        return this.uniformIDList;
    }

    public void setUniformIDList(String uniformIDList) {
        if (!uniformIDList.equals(this.uniformIDList)) {
            this.uniformIDList = uniformIDList;
        }
    }

    public void saveUniformIDList(String uniformIDList) {
        if (!uniformIDList.equals(this.uniformIDList)) {
            this.uniformIDList = uniformIDList;
            this.saveField("uniformIDList", uniformIDList);
        }
    }

    public String getUniformCountList() {
        return this.uniformCountList;
    }

    public void setUniformCountList(String uniformCountList) {
        if (!uniformCountList.equals(this.uniformCountList)) {
            this.uniformCountList = uniformCountList;
        }
    }

    public void saveUniformCountList(String uniformCountList) {
        if (!uniformCountList.equals(this.uniformCountList)) {
            this.uniformCountList = uniformCountList;
            this.saveField("uniformCountList", uniformCountList);
        }
    }

    public String getParams() {
        return this.params;
    }

    public void setParams(String params) {
        if (!params.equals(this.params)) {
            this.params = params;
        }
    }

    public void saveParams(String params) {
        if (!params.equals(this.params)) {
            this.params = params;
            this.saveField("params", params);
        }
    }

    @Override
    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `cid` = '").append(this.cid).append("',");
        sBuilder.append(" `mailID` = '").append(this.mailID).append("',");
        sBuilder.append(" `uniformIDList` = '").append(this.uniformIDList == null ? null : this.uniformIDList.replace("'", "''")).append("',");
        sBuilder.append(" `uniformCountList` = '").append(this.uniformCountList == null ? null : this.uniformCountList.replace("'", "''")).append("',");
        sBuilder.append(" `params` = '").append(this.params == null ? null : this.params.replace("'", "''")).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }

    public static String getSql_TableCreate() {
        return "CREATE TABLE IF NOT EXISTS `worldMail` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`cid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家cid',`mailID` int(11) NOT NULL DEFAULT '0' COMMENT '邮件静态Id',`uniformIDList` varchar(500) NOT NULL DEFAULT '' COMMENT '物品Id列表',`uniformCountList` varchar(500) NOT NULL DEFAULT '' COMMENT '物品数量列表',`params` varchar(500) NOT NULL DEFAULT '' COMMENT '额外的参数列表',PRIMARY KEY (`id`)) COMMENT='Zone延迟邮件信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT="
            + (ServerConfig.getInitialID() + 1L);
    }
}
