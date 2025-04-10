package business.gmcmd.cmds;

import BaseCommon.CommLog;
import business.global.fight.Fighter;
import business.gmcmd.annotation.Command;
import business.gmcmd.annotation.Commander;
import business.player.Player;
import business.player.feature.PlayerBase;
import business.player.feature.character.CharFeature;
import business.player.feature.character.Character;
import com.zhonglian.server.common.enums.Attribute;
import core.config.refdata.ref.RefRebirth;
import java.util.List;
import java.util.Map;

@Commander(name = "team", comment = "队伍设置相关")
public class CmdTeam
{
@Command(comment = "输出玩家队伍属性")
public String power(Player player) {
Map<Integer, Fighter> fighters = ((CharFeature)player.getFeature(CharFeature.class)).getFighters();
for (Map.Entry<Integer, Fighter> pair : fighters.entrySet()) {
CommLog.info("============分割线============");
Fighter fighter = pair.getValue();
CommLog.info("角色ID：{}", Integer.valueOf(fighter.id));
CommLog.info("生命：{}", Double.valueOf(fighter.attr(Attribute.MaxHP)));
CommLog.info("攻击：{}", Double.valueOf(fighter.attr(Attribute.ATK)));
CommLog.info("防御：{}", Double.valueOf(fighter.attr(Attribute.DEF)));
CommLog.info("法防：{}", Double.valueOf(fighter.attr(Attribute.RGS)));
CommLog.info("命中：{}", Double.valueOf(fighter.attr(Attribute.Hit)));
CommLog.info("闪避：{}", Double.valueOf(fighter.attr(Attribute.Dodge)));
CommLog.info("暴击：{}", Double.valueOf(fighter.attr(Attribute.Critical)));
CommLog.info("暴抗：{}", Double.valueOf(fighter.attr(Attribute.Tenacity)));
CommLog.info("============分割线============");
} 
return "查看服务端控制台输出";
}

@Command(comment = "设置人物等级")
public String level(Player player, int level) {
player.getPlayerBO().saveLv(level);
if (level > 100) {
int temp1 = level % 100;
int temp2 = temp1 / 10;
int rebirth = ((RefRebirth)((List)RefRebirth.sameLevel.get(Integer.valueOf(temp2))).get(0)).id;
Map<Integer, Character> characters = ((CharFeature)player.getFeature(CharFeature.class)).getAll();
for (Character chars : characters.values()) {
chars.getBo().saveRebirth(rebirth);
chars.onAttrChanged();
} 
} 

((PlayerBase)player.getFeature(PlayerBase.class)).onLevelUp(level);
player.pushProperties("lv", player.getPlayerBO().getLv());
return "ok";
}

@Command(comment = "设置翅膀等级")
public String wing(Player player, int level) {
Map<Integer, Character> characters = ((CharFeature)player.getFeature(CharFeature.class)).getAll();
for (Character chars : characters.values()) {
chars.getBo().saveWing(level);
chars.onAttrChanged();
} 
return "ok";
}

@Command(comment = "设置转生等级")
public String rebirth(Player player, int level) {
Map<Integer, Character> characters = ((CharFeature)player.getFeature(CharFeature.class)).getAll();
for (Character chars : characters.values()) {
chars.getBo().saveRebirth(level);
chars.onAttrChanged();
} 
return "ok";
}

@Command(comment = "设置经脉等级")
public String meridian(Player player, int level) {
Map<Integer, Character> characters = ((CharFeature)player.getFeature(CharFeature.class)).getAll();
for (Character chars : characters.values()) {
chars.getBo().saveMeridian(level);
chars.onAttrChanged();
} 
return "ok";
}

@Command(comment = "设置技能等级")
public String skill(Player player, int level) {
Map<Integer, Character> characters = ((CharFeature)player.getFeature(CharFeature.class)).getAll();
for (Character chars : characters.values()) {
chars.getBo().saveSkillAll(level);
chars.onAttrChanged();
} 
return "ok";
}

@Command(comment = "设置强化等级")
public String strengthen(Player player, int level) {
Map<Integer, Character> characters = ((CharFeature)player.getFeature(CharFeature.class)).getAll();
for (Character chars : characters.values()) {
chars.getBo().saveStrengthenAll(level);
chars.onAttrChanged();
} 
return "ok";
}

@Command(comment = "设置宝石等级")
public String gem(Player player, int level) {
Map<Integer, Character> characters = ((CharFeature)player.getFeature(CharFeature.class)).getAll();
for (Character chars : characters.values()) {
chars.getBo().saveGemAll(level);
chars.onAttrChanged();
} 
return "ok";
}

@Command(comment = "设置星级")
public String star(Player player, int level) {
Map<Integer, Character> characters = ((CharFeature)player.getFeature(CharFeature.class)).getAll();
for (Character chars : characters.values()) {
chars.getBo().saveStarAll(level);
chars.onAttrChanged();
} 
return "ok";
}

@Command(comment = "设置炼化等级")
public String artifice(Player player, int level) {
Map<Integer, Character> characters = ((CharFeature)player.getFeature(CharFeature.class)).getAll();
for (Character chars : characters.values()) {
chars.getBo().saveArtificeAll(level);
chars.onAttrChanged();
} 
return "ok";
}
}

