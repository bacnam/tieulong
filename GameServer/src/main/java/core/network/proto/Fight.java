package core.network.proto;

import business.global.battle.Creature;
import com.zhonglian.server.common.enums.Attribute;
import com.zhonglian.server.common.enums.FightResult;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Fight
{
public static class Begin
{
public int fightId;

public Begin(int id) {
this.fightId = id;
}
}

public static class End
{
public int fightId;
public FightResult result;
public List<Fight.CheckData> checks;
}

public static class Settle
{
public int victim;
public int vgroup;
public double value;

public Settle(int victim, int vgroup, double value) {
this.victim = victim;
this.vgroup = vgroup;
this.value = value;
}
}

public static class CheckData
{
public int time;
public int attacker;
public int agroup;
public int skill;
public Fight.Settle[] settles;

public CheckData(int time, int attacker, int agroup, int skill, Fight.Settle[] settles) {
this.time = time;
this.attacker = attacker;
this.agroup = agroup;
this.skill = skill;
this.settles = settles;
}
}

static class Skill
{
int id;

int level;
}

public static class Fighter
{
int type;
int id;
int level;
int Model;
int Wing;
double inithp;
double MaxHP;
double ATK;
double DEF;
double RGS;
double Hit;
double Dodge;
double Critical;
double Tenacity;
double RNG;
double SPD;
List<Fight.Skill> skills;

public Fighter(Creature c) {
this.type = c.getType();
this.id = c.getId();
this.level = c.getLevel();
this.Model = c.getModel();
this.Wing = c.getWing();

this.inithp = c.initHp;
this.MaxHP = c.baseAttr(Attribute.MaxHP);
this.ATK = c.baseAttr(Attribute.ATK);
this.DEF = c.baseAttr(Attribute.DEF);
this.RGS = c.baseAttr(Attribute.RGS);
this.Hit = c.baseAttr(Attribute.Hit);
this.Dodge = c.baseAttr(Attribute.Dodge);
this.Critical = c.baseAttr(Attribute.Critical);
this.Tenacity = c.baseAttr(Attribute.Tenacity);

this.RNG = c.getRange();
this.SPD = c.getSpeed();

this.skills = new ArrayList<>();
for (business.global.battle.Skill s : c.getSkills()) {
Fight.Skill s0 = new Fight.Skill();
s0.id = s.getId();
s0.level = s.getLevel();
this.skills.add(s0);
} 
} }

public static class Battle {
int id;

public Battle(int id, String map, List<Creature> team, List<Creature> opponents) {
this.id = id;
this.map = map;
this.team = (List<Fight.Fighter>)team.stream().map(c -> new Fight.Fighter(c)).collect(Collectors.toList());
this.oppos = (List<Fight.Fighter>)opponents.stream().map(c -> new Fight.Fighter(c)).collect(Collectors.toList());
}

String map;
List<Fight.Fighter> team;
List<Fight.Fighter> oppos;
}
}

