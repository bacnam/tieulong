package core.network.proto;

import business.global.guild.Guild;
import business.global.guild.GuildWarConfig;
import business.global.guild.GuildWarMgr;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerBase;
import com.zhonglian.server.common.utils.CommTime;
import core.database.game.bo.LongnvResultBO;
import java.util.ArrayList;
import java.util.List;

public class LongnvWarFightProtol
{
Guild.GuildSummary atk;
Guild.GuildSummary def;
List<GuildInfo> guildInfo;
List<ResultRecord> resultInfo;
int endTime;
int killnum;
boolean isDead = false;
List<RoadSummry> road;
int rebirthCD;
int rebirthTime;

public static class GuildInfo
{
String name;
int left;
int dead;
int puppet;

public GuildInfo(String name, int left, int dead, int puppet) {
this.name = name;
this.left = left;
this.dead = dead;
this.puppet = puppet;
}
}

public int getRebirthTime() {
return this.rebirthTime;
}

public void setRebirthTime(int rebirthTime) {
this.rebirthTime = rebirthTime;
}

public Guild.GuildSummary getAtk() {
return this.atk;
}

public void setAtk(Guild.GuildSummary atk) {
this.atk = atk;
}

public Guild.GuildSummary getDef() {
return this.def;
}

public void setDef(Guild.GuildSummary def) {
this.def = def;
}

public List<GuildInfo> getGuildInfo() {
return this.guildInfo;
}

public void setGuildInfo(List<GuildInfo> guildInfo) {
this.guildInfo = guildInfo;
}

public List<ResultRecord> getResultInfo() {
return this.resultInfo;
}

public void setResultInfo(List<ResultRecord> resultInfo) {
this.resultInfo = resultInfo;
}

public int getKillnum() {
return this.killnum;
}

public void setKillnum(int killnum) {
this.killnum = killnum;
}

public List<RoadSummry> getRoad() {
return this.road;
}

public void setRoad(List<RoadSummry> road) {
this.road = road;
}

public int getEndTime() {
return this.endTime;
}

public void setEndTime(int endTime) {
this.endTime = endTime;
}

public boolean isDead() {
return this.isDead;
}

public void setDead(boolean isDead) {
this.isDead = isDead;
}

public int getRebirthCD() {
return this.rebirthCD;
}

public void setRebirthCD(int rebirthCD) {
this.rebirthCD = rebirthCD;
}

public static class RoadSummry {
List<Player.showModle> atkplayers = new ArrayList<>();
List<Player.showModle> defplayers = new ArrayList<>();
long Winner;
int overTime;
int oneFightOver;

public RoadSummry(GuildWarMgr.Road road) {
road.getAtkplayers().stream().forEach(x -> this.atkplayers.add(((PlayerBase)x.getFeature(PlayerBase.class)).modle()));

road.getDefplayers().stream().forEach(x -> this.defplayers.add(((PlayerBase)x.getFeature(PlayerBase.class)).modle()));

this.Winner = road.getWinner();
this.overTime = Math.max(0, GuildWarConfig.overTime() - CommTime.nowSecond() - road.getOverTime());
this.oneFightOver = Math.max(0, road.getBegin() + GuildWarConfig.oneFightTime() / 1000 - CommTime.nowSecond());
}
}

public static class ResultRecord
{
private long sid;
private long atkpid;
private String atkName;
private long defpid;
private String defName;
private int result;
private int fightTime;

public ResultRecord(LongnvResultBO bo) {
this.sid = bo.getId();
this.atkpid = bo.getAtkpid();
this.atkName = PlayerMgr.getInstance().getPlayer(this.atkpid).getName();
this.defpid = bo.getDefpid();
this.defName = PlayerMgr.getInstance().getPlayer(this.defpid).getName();
this.result = bo.getResult();
this.fightTime = bo.getFightTime();
}
}
}

