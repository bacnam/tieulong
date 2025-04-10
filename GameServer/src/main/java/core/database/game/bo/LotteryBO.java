package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LotteryBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "所属玩家pid")
private long pid;
@DataBaseField(type = "int(11)", fieldname = "num", comment = "期数")
private int num;
@DataBaseField(type = "int(11)", fieldname = "number", comment = "号码")
private int number;
@DataBaseField(type = "int(11)", fieldname = "type", comment = "种类")
private int type;
@DataBaseField(type = "int(11)", fieldname = "buyday", comment = "购买日期")
private int buyday;
@DataBaseField(type = "int(11)", fieldname = "rewardday", comment = "中奖日期")
private int rewardday;

public LotteryBO() {
this.id = 0L;
this.pid = 0L;
this.num = 0;
this.number = 0;
this.type = 0;
this.buyday = 0;
this.rewardday = 0;
}

public LotteryBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.pid = rs.getLong(2);
this.num = rs.getInt(3);
this.number = rs.getInt(4);
this.type = rs.getInt(5);
this.buyday = rs.getInt(6);
this.rewardday = rs.getInt(7);
}

public void getFromResultSet(ResultSet rs, List<LotteryBO> list) throws Exception {
list.add(new LotteryBO(rs));
}

public long getAsynTaskTag() {
return getPid();
}

public String getItemsName() {
return "`id`, `pid`, `num`, `number`, `type`, `buyday`, `rewardday`";
}

public String getTableName() {
return "`lottery`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.pid).append("', ");
strBuf.append("'").append(this.num).append("', ");
strBuf.append("'").append(this.number).append("', ");
strBuf.append("'").append(this.type).append("', ");
strBuf.append("'").append(this.buyday).append("', ");
strBuf.append("'").append(this.rewardday).append("', ");
strBuf.deleteCharAt(strBuf.length() - 2);
return strBuf.toString();
}

public ArrayList<byte[]> getInsertValueBytes() {
ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
return ret;
}

public void setId(long iID) {
this.id = iID;
}

public long getId() {
return this.id;
}

public long getPid() {
return this.pid;
} public void setPid(long pid) {
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

public int getNum() {
return this.num;
} public void setNum(int num) {
if (num == this.num)
return; 
this.num = num;
}
public void saveNum(int num) {
if (num == this.num)
return; 
this.num = num;
saveField("num", Integer.valueOf(num));
}

public int getNumber() {
return this.number;
} public void setNumber(int number) {
if (number == this.number)
return; 
this.number = number;
}
public void saveNumber(int number) {
if (number == this.number)
return; 
this.number = number;
saveField("number", Integer.valueOf(number));
}

public int getType() {
return this.type;
} public void setType(int type) {
if (type == this.type)
return; 
this.type = type;
}
public void saveType(int type) {
if (type == this.type)
return; 
this.type = type;
saveField("type", Integer.valueOf(type));
}

public int getBuyday() {
return this.buyday;
} public void setBuyday(int buyday) {
if (buyday == this.buyday)
return; 
this.buyday = buyday;
}
public void saveBuyday(int buyday) {
if (buyday == this.buyday)
return; 
this.buyday = buyday;
saveField("buyday", Integer.valueOf(buyday));
}

public int getRewardday() {
return this.rewardday;
} public void setRewardday(int rewardday) {
if (rewardday == this.rewardday)
return; 
this.rewardday = rewardday;
}
public void saveRewardday(int rewardday) {
if (rewardday == this.rewardday)
return; 
this.rewardday = rewardday;
saveField("rewardday", Integer.valueOf(rewardday));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `pid` = '").append(this.pid).append("',");
sBuilder.append(" `num` = '").append(this.num).append("',");
sBuilder.append(" `number` = '").append(this.number).append("',");
sBuilder.append(" `type` = '").append(this.type).append("',");
sBuilder.append(" `buyday` = '").append(this.buyday).append("',");
sBuilder.append(" `rewardday` = '").append(this.rewardday).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `lottery` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家pid',`num` int(11) NOT NULL DEFAULT '0' COMMENT '期数',`number` int(11) NOT NULL DEFAULT '0' COMMENT '号码',`type` int(11) NOT NULL DEFAULT '0' COMMENT '种类',`buyday` int(11) NOT NULL DEFAULT '0' COMMENT '购买日期',`rewardday` int(11) NOT NULL DEFAULT '0' COMMENT '中奖日期',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='一元购表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

