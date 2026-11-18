package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TeamBO extends BaseBO {
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "id",
        comment = "数据库表唯一键值"
    )
    private long id;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "pid",
        comment = "所属玩家ID"
    )
    private long pid;
    @DataBaseField(
        type = "int(11)",
        fieldname = "teamType",
        comment = "队伍类型"
    )
    private int teamType;
    @DataBaseField(
        type = "int(11)",
        fieldname = "fightPower",
        comment = "战斗力"
    )
    private int fightPower;
    @DataBaseField(
        type = "int(11)",
        size = 4,
        fieldname = "battleCard",
        comment = "战斗队伍卡牌信息"
    )
    private List<Integer> battleCard;
    @DataBaseField(
        type = "int(11)",
        size = 3,
        fieldname = "reserveCard",
        comment = "小伙伴卡牌信息"
    )
    private List<Integer> reserveCard;
    @DataBaseField(
        type = "int(11)",
        size = 9,
        fieldname = "position",
        comment = "后援队伍卡牌信息"
    )
    private List<Integer> position;

    public TeamBO() {
        this.id = 0L;
        this.pid = 0L;
        this.teamType = 0;
        this.fightPower = 0;
        this.battleCard = new ArrayList<>(4);

        for (int i = 0; i < 4; i++) {
            this.battleCard.add(0);
        }

        this.reserveCard = new ArrayList<>(3);

        for (int i = 0; i < 3; i++) {
            this.reserveCard.add(0);
        }

        this.position = new ArrayList<>(9);

        for (int i = 0; i < 9; i++) {
            this.position.add(0);
        }
    }

    public TeamBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.teamType = rs.getInt(3);
        this.fightPower = rs.getInt(4);
        this.battleCard = new ArrayList<>(4);

        for (int i = 0; i < 4; i++) {
            this.battleCard.add(rs.getInt(i + 5));
        }

        this.reserveCard = new ArrayList<>(3);

        for (int i = 0; i < 3; i++) {
            this.reserveCard.add(rs.getInt(i + 9));
        }

        this.position = new ArrayList<>(9);

        for (int i = 0; i < 9; i++) {
            this.position.add(rs.getInt(i + 12));
        }
    }

    @Override
    public void getFromResultSet(ResultSet rs, List list) throws Exception {
        list.add(new TeamBO(rs));
    }

    @Override
    public long getAsynTaskTag() {
        return this.getPid();
    }

    @Override
    public String getItemsName() {
        return "`id`, `pid`, `teamType`, `fightPower`, `battleCard_0`, `battleCard_1`, `battleCard_2`, `battleCard_3`, `reserveCard_0`, `reserveCard_1`, `reserveCard_2`, `position_0`, `position_1`, `position_2`, `position_3`, `position_4`, `position_5`, `position_6`, `position_7`, `position_8`";
    }

    @Override
    public String getTableName() {
        return "`team`";
    }

    @Override
    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.teamType).append("', ");
        strBuf.append("'").append(this.fightPower).append("', ");

        for (int i = 0; i < this.battleCard.size(); i++) {
            strBuf.append("'").append(this.battleCard.get(i)).append("', ");
        }

        for (int i = 0; i < this.reserveCard.size(); i++) {
            strBuf.append("'").append(this.reserveCard.get(i)).append("', ");
        }

        for (int i = 0; i < this.position.size(); i++) {
            strBuf.append("'").append(this.position.get(i)).append("', ");
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

    public long getPid() {
        return this.pid;
    }

    public void setPid(long pid) {
        if (pid != this.pid) {
            this.pid = pid;
        }
    }

    public void savePid(long pid) {
        if (pid != this.pid) {
            this.pid = pid;
            this.saveField("pid", pid);
        }
    }

    public int getTeamType() {
        return this.teamType;
    }

    public void setTeamType(int teamType) {
        if (teamType != this.teamType) {
            this.teamType = teamType;
        }
    }

    public void saveTeamType(int teamType) {
        if (teamType != this.teamType) {
            this.teamType = teamType;
            this.saveField("teamType", teamType);
        }
    }

    public int getFightPower() {
        return this.fightPower;
    }

    public void setFightPower(int fightPower) {
        if (fightPower != this.fightPower) {
            this.fightPower = fightPower;
        }
    }

    public void saveFightPower(int fightPower) {
        if (fightPower != this.fightPower) {
            this.fightPower = fightPower;
            this.saveField("fightPower", fightPower);
        }
    }

    public int getBattleCardSize() {
        return this.battleCard.size();
    }

    public List<Integer> getBattleCardAll() {
        return new ArrayList<>(this.battleCard);
    }

    public void setBattleCardAll(int value) {
        for (int i = 0; i < this.battleCard.size(); i++) {
            this.battleCard.set(i, value);
        }
    }

    public void saveBattleCardAll(int value) {
        this.setBattleCardAll(value);
        this.saveAll();
    }

    public int getBattleCard(int index) {
        return this.battleCard.get(index);
    }

    public void setBattleCard(int index, int value) {
        if (value != this.battleCard.get(index)) {
            this.battleCard.set(index, value);
        }
    }

    public void saveBattleCard(int index, int value) {
        if (value != this.battleCard.get(index)) {
            this.battleCard.set(index, value);
            this.saveField("battleCard_" + index, this.battleCard.get(index));
        }
    }

    public int getReserveCardSize() {
        return this.reserveCard.size();
    }

    public List<Integer> getReserveCardAll() {
        return new ArrayList<>(this.reserveCard);
    }

    public void setReserveCardAll(int value) {
        for (int i = 0; i < this.reserveCard.size(); i++) {
            this.reserveCard.set(i, value);
        }
    }

    public void saveReserveCardAll(int value) {
        this.setReserveCardAll(value);
        this.saveAll();
    }

    public int getReserveCard(int index) {
        return this.reserveCard.get(index);
    }

    public void setReserveCard(int index, int value) {
        if (value != this.reserveCard.get(index)) {
            this.reserveCard.set(index, value);
        }
    }

    public void saveReserveCard(int index, int value) {
        if (value != this.reserveCard.get(index)) {
            this.reserveCard.set(index, value);
            this.saveField("reserveCard_" + index, this.reserveCard.get(index));
        }
    }

    public int getPositionSize() {
        return this.position.size();
    }

    public List<Integer> getPositionAll() {
        return new ArrayList<>(this.position);
    }

    public void setPositionAll(int value) {
        for (int i = 0; i < this.position.size(); i++) {
            this.position.set(i, value);
        }
    }

    public void savePositionAll(int value) {
        this.setPositionAll(value);
        this.saveAll();
    }

    public int getPosition(int index) {
        return this.position.get(index);
    }

    public void setPosition(int index, int value) {
        if (value != this.position.get(index)) {
            this.position.set(index, value);
        }
    }

    public void savePosition(int index, int value) {
        if (value != this.position.get(index)) {
            this.position.set(index, value);
            this.saveField("position_" + index, this.position.get(index));
        }
    }

    @Override
    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `teamType` = '").append(this.teamType).append("',");
        sBuilder.append(" `fightPower` = '").append(this.fightPower).append("',");

        for (int i = 0; i < this.battleCard.size(); i++) {
            sBuilder.append(" `battleCard_").append(i).append("` = '").append(this.battleCard.get(i)).append("',");
        }

        for (int i = 0; i < this.reserveCard.size(); i++) {
            sBuilder.append(" `reserveCard_").append(i).append("` = '").append(this.reserveCard.get(i)).append("',");
        }

        for (int i = 0; i < this.position.size(); i++) {
            sBuilder.append(" `position_").append(i).append("` = '").append(this.position.get(i)).append("',");
        }

        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }

    public static String getSql_TableCreate() {
        return "CREATE TABLE IF NOT EXISTS `team` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家ID',`teamType` int(11) NOT NULL DEFAULT '0' COMMENT '队伍类型',`fightPower` int(11) NOT NULL DEFAULT '0' COMMENT '战斗力',`battleCard_0` int(11) NOT NULL DEFAULT '0' COMMENT '战斗队伍卡牌信息',`battleCard_1` int(11) NOT NULL DEFAULT '0' COMMENT '战斗队伍卡牌信息',`battleCard_2` int(11) NOT NULL DEFAULT '0' COMMENT '战斗队伍卡牌信息',`battleCard_3` int(11) NOT NULL DEFAULT '0' COMMENT '战斗队伍卡牌信息',`reserveCard_0` int(11) NOT NULL DEFAULT '0' COMMENT '小伙伴卡牌信息',`reserveCard_1` int(11) NOT NULL DEFAULT '0' COMMENT '小伙伴卡牌信息',`reserveCard_2` int(11) NOT NULL DEFAULT '0' COMMENT '小伙伴卡牌信息',`position_0` int(11) NOT NULL DEFAULT '0' COMMENT '后援队伍卡牌信息',`position_1` int(11) NOT NULL DEFAULT '0' COMMENT '后援队伍卡牌信息',`position_2` int(11) NOT NULL DEFAULT '0' COMMENT '后援队伍卡牌信息',`position_3` int(11) NOT NULL DEFAULT '0' COMMENT '后援队伍卡牌信息',`position_4` int(11) NOT NULL DEFAULT '0' COMMENT '后援队伍卡牌信息',`position_5` int(11) NOT NULL DEFAULT '0' COMMENT '后援队伍卡牌信息',`position_6` int(11) NOT NULL DEFAULT '0' COMMENT '后援队伍卡牌信息',`position_7` int(11) NOT NULL DEFAULT '0' COMMENT '后援队伍卡牌信息',`position_8` int(11) NOT NULL DEFAULT '0' COMMENT '后援队伍卡牌信息',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='玩家队伍信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT="
            + (ServerConfig.getInitialID() + 1L);
    }
}
