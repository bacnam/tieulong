package core.database.world.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ActivityBO extends BaseBO {
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "id",
        comment = "数据库表唯一键值"
    )
    private long id;
    @DataBaseField(
        type = "varchar(500)",
        fieldname = "activity",
        comment = "活动类型"
    )
    private String activity;
    @DataBaseField(
        type = "tinyint(1)",
        fieldname = "isActive",
        comment = "是否启用"
    )
    private boolean isActive;
    @DataBaseField(
        type = "varchar(500)",
        fieldname = "lastStatus",
        comment = "上次检测的时候的状态"
    )
    private String lastStatus;
    @DataBaseField(
        type = "varchar(500)",
        fieldname = "gm_id",
        comment = "PHP后台对应的id"
    )
    private String gm_id;
    @DataBaseField(
        type = "int(11)",
        fieldname = "beginTime",
        comment = "活动开启时间"
    )
    private int beginTime;
    @DataBaseField(
        type = "int(11)",
        fieldname = "endTime",
        comment = "活动结束时间-可能客户端还需要显示"
    )
    private int endTime;
    @DataBaseField(
        type = "int(11)",
        fieldname = "closeTime",
        comment = "活动关闭时间-活动彻底关闭，客户端不再显示"
    )
    private int closeTime;
    @DataBaseField(
        type = "text(500)",
        fieldname = "json",
        comment = "活动具体配置"
    )
    private String json;
    @DataBaseField(
        type = "int(11)",
        fieldname = "joinIn",
        comment = "活动参与人数"
    )
    private int joinIn;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "winnerCid",
        comment = "活动赢家cid"
    )
    private long winnerCid;
    @DataBaseField(
        type = "int(11)",
        size = 10,
        fieldname = "extInt",
        comment = "活动int型扩展字段信息"
    )
    private List<Integer> extInt;
    @DataBaseField(
        type = "varchar(500)",
        size = 10,
        fieldname = "extStr",
        comment = "活动string型扩展字段信息"
    )
    private List<String> extStr;

    public ActivityBO() {
        this.id = 0L;
        this.activity = "";
        this.isActive = false;
        this.lastStatus = "";
        this.gm_id = "";
        this.beginTime = 0;
        this.endTime = 0;
        this.closeTime = 0;
        this.json = "";
        this.joinIn = 0;
        this.winnerCid = 0L;
        this.extInt = new ArrayList<>(10);

        for (int i = 0; i < 10; i++) {
            this.extInt.add(0);
        }

        this.extStr = new ArrayList<>(10);

        for (int i = 0; i < 10; i++) {
            this.extStr.add("");
        }
    }

    public ActivityBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.activity = rs.getString(2);
        this.isActive = rs.getBoolean(3);
        this.lastStatus = rs.getString(4);
        this.gm_id = rs.getString(5);
        this.beginTime = rs.getInt(6);
        this.endTime = rs.getInt(7);
        this.closeTime = rs.getInt(8);
        this.json = rs.getString(9);
        this.joinIn = rs.getInt(10);
        this.winnerCid = rs.getLong(11);
        this.extInt = new ArrayList<>(10);

        for (int i = 0; i < 10; i++) {
            this.extInt.add(rs.getInt(i + 12));
        }

        this.extStr = new ArrayList<>(10);

        for (int i = 0; i < 10; i++) {
            this.extStr.add(rs.getString(i + 22));
        }
    }

    @Override
    public void getFromResultSet(ResultSet rs, List list) throws Exception {
        list.add(new ActivityBO(rs));
    }

    @Override
    public long getAsynTaskTag() {
        return 0L;
    }

    @Override
    public String getItemsName() {
        return "`id`, `activity`, `isActive`, `lastStatus`, `gm_id`, `beginTime`, `endTime`, `closeTime`, `json`, `joinIn`, `winnerCid`, `extInt_0`, `extInt_1`, `extInt_2`, `extInt_3`, `extInt_4`, `extInt_5`, `extInt_6`, `extInt_7`, `extInt_8`, `extInt_9`, `extStr_0`, `extStr_1`, `extStr_2`, `extStr_3`, `extStr_4`, `extStr_5`, `extStr_6`, `extStr_7`, `extStr_8`, `extStr_9`";
    }

    @Override
    public String getTableName() {
        return "`activity`";
    }

    @Override
    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.activity == null ? null : this.activity.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.isActive ? 1 : 0).append("', ");
        strBuf.append("'").append(this.lastStatus == null ? null : this.lastStatus.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.gm_id == null ? null : this.gm_id.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.beginTime).append("', ");
        strBuf.append("'").append(this.endTime).append("', ");
        strBuf.append("'").append(this.closeTime).append("', ");
        strBuf.append("'").append(this.json == null ? null : this.json.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.joinIn).append("', ");
        strBuf.append("'").append(this.winnerCid).append("', ");

        for (int i = 0; i < this.extInt.size(); i++) {
            strBuf.append("'").append(this.extInt.get(i)).append("', ");
        }

        for (int i = 0; i < this.extStr.size(); i++) {
            strBuf.append("'").append(this.extStr.get(i) == null ? null : this.extStr.get(i).replace("'", "''")).append("', ");
        }

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

    public String getActivity() {
        return this.activity;
    }

    public void setActivity(String activity) {
        if (!activity.equals(this.activity)) {
            this.activity = activity;
        }
    }

    public void saveActivity(String activity) {
        if (!activity.equals(this.activity)) {
            this.activity = activity;
            this.saveField("activity", activity);
        }
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        if (isActive != this.isActive) {
            this.isActive = isActive;
        }
    }

    public void saveIsActive(boolean isActive) {
        if (isActive != this.isActive) {
            this.isActive = isActive;
            this.saveField("isActive", isActive ? 1 : 0);
        }
    }

    public String getLastStatus() {
        return this.lastStatus;
    }

    public void setLastStatus(String lastStatus) {
        if (!lastStatus.equals(this.lastStatus)) {
            this.lastStatus = lastStatus;
        }
    }

    public void saveLastStatus(String lastStatus) {
        if (!lastStatus.equals(this.lastStatus)) {
            this.lastStatus = lastStatus;
            this.saveField("lastStatus", lastStatus);
        }
    }

    public String getGmId() {
        return this.gm_id;
    }

    public void setGmId(String gm_id) {
        if (!gm_id.equals(this.gm_id)) {
            this.gm_id = gm_id;
        }
    }

    public void saveGmId(String gm_id) {
        if (!gm_id.equals(this.gm_id)) {
            this.gm_id = gm_id;
            this.saveField("gm_id", gm_id);
        }
    }

    public int getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(int beginTime) {
        if (beginTime != this.beginTime) {
            this.beginTime = beginTime;
        }
    }

    public void saveBeginTime(int beginTime) {
        if (beginTime != this.beginTime) {
            this.beginTime = beginTime;
            this.saveField("beginTime", beginTime);
        }
    }

    public int getEndTime() {
        return this.endTime;
    }

    public void setEndTime(int endTime) {
        if (endTime != this.endTime) {
            this.endTime = endTime;
        }
    }

    public void saveEndTime(int endTime) {
        if (endTime != this.endTime) {
            this.endTime = endTime;
            this.saveField("endTime", endTime);
        }
    }

    public int getCloseTime() {
        return this.closeTime;
    }

    public void setCloseTime(int closeTime) {
        if (closeTime != this.closeTime) {
            this.closeTime = closeTime;
        }
    }

    public void saveCloseTime(int closeTime) {
        if (closeTime != this.closeTime) {
            this.closeTime = closeTime;
            this.saveField("closeTime", closeTime);
        }
    }

    public String getJson() {
        return this.json;
    }

    public void setJson(String json) {
        if (!json.equals(this.json)) {
            this.json = json;
        }
    }

    public void saveJson(String json) {
        if (!json.equals(this.json)) {
            this.json = json;
            this.saveField("json", json);
        }
    }

    public int getJoinIn() {
        return this.joinIn;
    }

    public void setJoinIn(int joinIn) {
        if (joinIn != this.joinIn) {
            this.joinIn = joinIn;
        }
    }

    public void saveJoinIn(int joinIn) {
        if (joinIn != this.joinIn) {
            this.joinIn = joinIn;
            this.saveField("joinIn", joinIn);
        }
    }

    public long getWinnerCid() {
        return this.winnerCid;
    }

    public void setWinnerCid(long winnerCid) {
        if (winnerCid != this.winnerCid) {
            this.winnerCid = winnerCid;
        }
    }

    public void saveWinnerCid(long winnerCid) {
        if (winnerCid != this.winnerCid) {
            this.winnerCid = winnerCid;
            this.saveField("winnerCid", winnerCid);
        }
    }

    public int getExtIntSize() {
        return this.extInt.size();
    }

    public List<Integer> getExtIntAll() {
        return new ArrayList<>(this.extInt);
    }

    public void setExtIntAll(int value) {
        for (int i = 0; i < this.extInt.size(); i++) {
            this.extInt.set(i, value);
        }
    }

    public void saveExtIntAll(int value) {
        this.setExtIntAll(value);
        this.saveAll();
    }

    public int getExtInt(int index) {
        return this.extInt.get(index);
    }

    public void setExtInt(int index, int value) {
        if (value != this.extInt.get(index)) {
            this.extInt.set(index, value);
        }
    }

    public void saveExtInt(int index, int value) {
        if (value != this.extInt.get(index)) {
            this.extInt.set(index, value);
            this.saveField("extInt_" + index, this.extInt.get(index));
        }
    }

    public int getExtStrSize() {
        return this.extStr.size();
    }

    public List<String> getExtStrAll() {
        return new ArrayList<>(this.extStr);
    }

    public void setExtStrAll(String value) {
        for (int i = 0; i < this.extStr.size(); i++) {
            this.extStr.set(i, value);
        }
    }

    public void saveExtStrAll(String value) {
        this.setExtStrAll(value);
        this.saveAll();
    }

    public String getExtStr(int index) {
        return this.extStr.get(index);
    }

    public void setExtStr(int index, String value) {
        if (!value.equals(this.extStr.get(index))) {
            this.extStr.set(index, value);
        }
    }

    public void saveExtStr(int index, String value) {
        if (!value.equals(this.extStr.get(index))) {
            this.extStr.set(index, value);
            this.saveField("extStr_" + index, this.extStr.get(index));
        }
    }

    @Override
    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `activity` = '").append(this.activity == null ? null : this.activity.replace("'", "''")).append("',");
        sBuilder.append(" `isActive` = '").append(this.isActive ? 1 : 0).append("',");
        sBuilder.append(" `lastStatus` = '").append(this.lastStatus == null ? null : this.lastStatus.replace("'", "''")).append("',");
        sBuilder.append(" `gm_id` = '").append(this.gm_id == null ? null : this.gm_id.replace("'", "''")).append("',");
        sBuilder.append(" `beginTime` = '").append(this.beginTime).append("',");
        sBuilder.append(" `endTime` = '").append(this.endTime).append("',");
        sBuilder.append(" `closeTime` = '").append(this.closeTime).append("',");
        sBuilder.append(" `json` = '").append(this.json == null ? null : this.json.replace("'", "''")).append("',");
        sBuilder.append(" `joinIn` = '").append(this.joinIn).append("',");
        sBuilder.append(" `winnerCid` = '").append(this.winnerCid).append("',");

        for (int i = 0; i < this.extInt.size(); i++) {
            sBuilder.append(" `extInt_").append(i).append("` = '").append(this.extInt.get(i)).append("',");
        }

        for (int i = 0; i < this.extStr.size(); i++) {
            sBuilder.append(" `extStr_").append(i).append("` = '").append(this.extStr == null ? null : this.extStr.get(i).replace("'", "''")).append("',");
        }

        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }

    public static String getSql_TableCreate() {
        return "CREATE TABLE IF NOT EXISTS `activity` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`activity` varchar(500) NOT NULL DEFAULT '' COMMENT '活动类型',`isActive` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否启用',`lastStatus` varchar(500) NOT NULL DEFAULT '' COMMENT '上次检测的时候的状态',`gm_id` varchar(500) NOT NULL DEFAULT '' COMMENT 'PHP后台对应的id',`beginTime` int(11) NOT NULL DEFAULT '0' COMMENT '活动开启时间',`endTime` int(11) NOT NULL DEFAULT '0' COMMENT '活动结束时间-可能客户端还需要显示',`closeTime` int(11) NOT NULL DEFAULT '0' COMMENT '活动关闭时间-活动彻底关闭，客户端不再显示',`json` text NULL COMMENT '活动具体配置',`joinIn` int(11) NOT NULL DEFAULT '0' COMMENT '活动参与人数',`winnerCid` bigint(20) NOT NULL DEFAULT '0' COMMENT '活动赢家cid',`extInt_0` int(11) NOT NULL DEFAULT '0' COMMENT '活动int型扩展字段信息',`extInt_1` int(11) NOT NULL DEFAULT '0' COMMENT '活动int型扩展字段信息',`extInt_2` int(11) NOT NULL DEFAULT '0' COMMENT '活动int型扩展字段信息',`extInt_3` int(11) NOT NULL DEFAULT '0' COMMENT '活动int型扩展字段信息',`extInt_4` int(11) NOT NULL DEFAULT '0' COMMENT '活动int型扩展字段信息',`extInt_5` int(11) NOT NULL DEFAULT '0' COMMENT '活动int型扩展字段信息',`extInt_6` int(11) NOT NULL DEFAULT '0' COMMENT '活动int型扩展字段信息',`extInt_7` int(11) NOT NULL DEFAULT '0' COMMENT '活动int型扩展字段信息',`extInt_8` int(11) NOT NULL DEFAULT '0' COMMENT '活动int型扩展字段信息',`extInt_9` int(11) NOT NULL DEFAULT '0' COMMENT '活动int型扩展字段信息',`extStr_0` varchar(500) NOT NULL DEFAULT '' COMMENT '活动string型扩展字段信息',`extStr_1` varchar(500) NOT NULL DEFAULT '' COMMENT '活动string型扩展字段信息',`extStr_2` varchar(500) NOT NULL DEFAULT '' COMMENT '活动string型扩展字段信息',`extStr_3` varchar(500) NOT NULL DEFAULT '' COMMENT '活动string型扩展字段信息',`extStr_4` varchar(500) NOT NULL DEFAULT '' COMMENT '活动string型扩展字段信息',`extStr_5` varchar(500) NOT NULL DEFAULT '' COMMENT '活动string型扩展字段信息',`extStr_6` varchar(500) NOT NULL DEFAULT '' COMMENT '活动string型扩展字段信息',`extStr_7` varchar(500) NOT NULL DEFAULT '' COMMENT '活动string型扩展字段信息',`extStr_8` varchar(500) NOT NULL DEFAULT '' COMMENT '活动string型扩展字段信息',`extStr_9` varchar(500) NOT NULL DEFAULT '' COMMENT '活动string型扩展字段信息',PRIMARY KEY (`id`)) COMMENT='活动信息表，记录每个活动基本信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT="
            + (ServerConfig.getInitialID() + 1L);
    }
}
