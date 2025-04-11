package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EquipBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
    private long pid;
    @DataBaseField(type = "int(11)", fieldname = "equip_id", comment = "装备id")
    private int equip_id;
    @DataBaseField(type = "int(11)", size = 6, fieldname = "attr", comment = "浮动属性")
    private List<Integer> attr;
    @DataBaseField(type = "int(11)", fieldname = "pos", comment = "装备位置")
    private int pos;
    @DataBaseField(type = "int(11)", fieldname = "char_id", comment = "装备的角色")
    private int char_id;
    @DataBaseField(type = "int(11)", fieldname = "gain_time", comment = "获取时间")
    private int gain_time;

    public EquipBO() {
        this.id = 0L;
        this.pid = 0L;
        this.equip_id = 0;
        this.attr = new ArrayList<>(6);
        for (int i = 0; i < 6; i++) {
            this.attr.add(Integer.valueOf(0));
        }
        this.pos = 0;
        this.char_id = 0;
        this.gain_time = 0;
    }

    public EquipBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.equip_id = rs.getInt(3);
        this.attr = new ArrayList<>(6);
        for (int i = 0; i < 6; i++) {
            this.attr.add(Integer.valueOf(rs.getInt(i + 4)));
        }
        this.pos = rs.getInt(10);
        this.char_id = rs.getInt(11);
        this.gain_time = rs.getInt(12);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `equip` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`equip_id` int(11) NOT NULL DEFAULT '0' COMMENT '装备id',`attr_0` int(11) NOT NULL DEFAULT '0' COMMENT '浮动属性',`attr_1` int(11) NOT NULL DEFAULT '0' COMMENT '浮动属性',`attr_2` int(11) NOT NULL DEFAULT '0' COMMENT '浮动属性',`attr_3` int(11) NOT NULL DEFAULT '0' COMMENT '浮动属性',`attr_4` int(11) NOT NULL DEFAULT '0' COMMENT '浮动属性',`attr_5` int(11) NOT NULL DEFAULT '0' COMMENT '浮动属性',`pos` int(11) NOT NULL DEFAULT '0' COMMENT '装备位置',`char_id` int(11) NOT NULL DEFAULT '0' COMMENT '装备的角色',`gain_time` int(11) NOT NULL DEFAULT '0' COMMENT '获取时间',PRIMARY KEY (`id`)) COMMENT='玩家信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<EquipBO> list) throws Exception {
        list.add(new EquipBO(rs));
    }

    public long getAsynTaskTag() {
        return getId();
    }

    public String getItemsName() {
        return "`id`, `pid`, `equip_id`, `attr_0`, `attr_1`, `attr_2`, `attr_3`, `attr_4`, `attr_5`, `pos`, `char_id`, `gain_time`";
    }

    public String getTableName() {
        return "`equip`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.equip_id).append("', ");
        for (int i = 0; i < this.attr.size(); i++) {
            strBuf.append("'").append(this.attr.get(i)).append("', ");
        }
        strBuf.append("'").append(this.pos).append("', ");
        strBuf.append("'").append(this.char_id).append("', ");
        strBuf.append("'").append(this.gain_time).append("', ");
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

    public long getPid() {
        return this.pid;
    }

    public void setPid(long pid) {
        if (pid == this.pid)
            return;
        this.pid = pid;
    }

    public void savePid(long pid) {
        if (pid == this.pid)
            return;
        this.pid = pid;
        saveField("pid", Long.valueOf(pid));
    }

    public int getEquipId() {
        return this.equip_id;
    }

    public void setEquipId(int equip_id) {
        if (equip_id == this.equip_id)
            return;
        this.equip_id = equip_id;
    }

    public void saveEquipId(int equip_id) {
        if (equip_id == this.equip_id)
            return;
        this.equip_id = equip_id;
        saveField("equip_id", Integer.valueOf(equip_id));
    }

    public int getAttrSize() {
        return this.attr.size();
    }

    public List<Integer> getAttrAll() {
        return new ArrayList<>(this.attr);
    }

    public void setAttrAll(int value) {
        for (int i = 0; i < this.attr.size(); ) {
            this.attr.set(i, Integer.valueOf(value));
            i++;
        }
    }

    public void saveAttrAll(int value) {
        setAttrAll(value);
        saveAll();
    }

    public int getAttr(int index) {
        return ((Integer) this.attr.get(index)).intValue();
    }

    public void setAttr(int index, int value) {
        if (value == ((Integer) this.attr.get(index)).intValue())
            return;
        this.attr.set(index, Integer.valueOf(value));
    }

    public void saveAttr(int index, int value) {
        if (value == ((Integer) this.attr.get(index)).intValue())
            return;
        this.attr.set(index, Integer.valueOf(value));
        saveField("attr_" + index, this.attr.get(index));
    }

    public int getPos() {
        return this.pos;
    }

    public void setPos(int pos) {
        if (pos == this.pos)
            return;
        this.pos = pos;
    }

    public void savePos(int pos) {
        if (pos == this.pos)
            return;
        this.pos = pos;
        saveField("pos", Integer.valueOf(pos));
    }

    public int getCharId() {
        return this.char_id;
    }

    public void setCharId(int char_id) {
        if (char_id == this.char_id)
            return;
        this.char_id = char_id;
    }

    public void saveCharId(int char_id) {
        if (char_id == this.char_id)
            return;
        this.char_id = char_id;
        saveField("char_id", Integer.valueOf(char_id));
    }

    public int getGainTime() {
        return this.gain_time;
    }

    public void setGainTime(int gain_time) {
        if (gain_time == this.gain_time)
            return;
        this.gain_time = gain_time;
    }

    public void saveGainTime(int gain_time) {
        if (gain_time == this.gain_time)
            return;
        this.gain_time = gain_time;
        saveField("gain_time", Integer.valueOf(gain_time));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `equip_id` = '").append(this.equip_id).append("',");
        for (int i = 0; i < this.attr.size(); i++) {
            sBuilder.append(" `attr_").append(i).append("` = '").append(this.attr.get(i)).append("',");
        }
        sBuilder.append(" `pos` = '").append(this.pos).append("',");
        sBuilder.append(" `char_id` = '").append(this.char_id).append("',");
        sBuilder.append(" `gain_time` = '").append(this.gain_time).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

