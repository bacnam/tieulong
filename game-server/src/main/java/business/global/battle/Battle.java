package business.global.battle;

import business.player.Player;
import business.player.feature.character.AttrCalculator;
import business.player.feature.character.CharFeature;
import business.player.feature.character.Character;
import business.player.feature.character.Equip;
import business.player.feature.character.SpiritAttrCalculator;
import business.player.feature.character.WarSpirit;
import business.player.feature.character.WarSpiritFeature;
import com.zhonglian.server.common.enums.Attribute;
import com.zhonglian.server.common.enums.EquipPos;
import com.zhonglian.server.common.enums.FightResult;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefCharacter;
import core.config.refdata.ref.RefMonster;
import core.config.refdata.ref.RefSkill;
import core.config.refdata.ref.RefWarSpirit;
import core.database.game.bo.CharacterBO;
import core.database.game.bo.WarSpiritBO;
import core.network.proto.Fight;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Battle {
    private static int nextFightId = 1;
    int fightId = nextFightId++;
    long seed = this.fightId;
    protected List<Creature> all;
    protected List<Creature> team;
    protected List<Creature> opponents;
    protected List<Bullet> bullets;
    protected boolean stopped;
    FightResult result;
    Player me;
    Player opponent;
    protected double time;

    double random() {
        this.seed = (this.seed * 9301L + 49297L) % 233280L;
        return this.seed / 233280.0;
    }

    public int getFightId() {
        return this.fightId;
    }

    public Battle(Player me, Player opponent) {
        this.team = this.getTeam(me);
        this.team.forEach(c -> c.group = Group.Player);
        this.opponents = this.getTeam(opponent);
        this.opponents.forEach(c -> c.group = Group.Creep);
        this.all = new ArrayList<>();
        this.all.addAll(this.team);
        this.all.addAll(this.opponents);
        this.bullets = new ArrayList<>();
        this.me = me;
        this.opponent = opponent;
    }

    public Battle(Player me, int monsterId) {
        this.team = this.getTeam(me);
        this.team.forEach(c -> c.group = Group.Player);
        this.opponents = this.getMonster(monsterId);
        this.opponents.forEach(c -> c.group = Group.Creep);
        this.all = new ArrayList<>();
        this.all.addAll(this.team);
        this.all.addAll(this.opponents);
        this.bullets = new ArrayList<>();
    }

    public Battle() {
    }

    protected abstract String getMap();

    public Fight.Battle proto() {
        return new Fight.Battle(this.fightId, this.getMap(), this.team, this.opponents);
    }

    List<Creature> getTeam(Player player) {
        List<Creature> creatures = new ArrayList<>();
        List<Character> characters = new ArrayList<>(player.getFeature(CharFeature.class).getAll().values());
        characters.sort((left, right) -> left.getBo().getId() < right.getBo().getId() ? -1 : 1);

        for (Character character : characters) {
            int charid = character.getCharId();
            CharacterBO bo = character.getBo();
            Equip equip = character.getEquip(EquipPos.Body);
            int equipid = equip == null ? 0 : equip.getEquipId();
            AttrCalculator attr = character.getAttr();
            Creature fighter = new Creature(this, charid, equipid, bo.getWing(), attr.getAttrs());
            fighter.level = player.getLv();
            fighter.RoleType = RoleType.Character;
            RefCharacter refChar = RefDataMgr.get(RefCharacter.class, charid);
            fighter.SPD = refChar.SPD;
            fighter.RNG = refChar.RNG;

            for (int i = 0; i < refChar.SkillList.size(); i++) {
                int skillid = refChar.SkillList.get(i);
                RefSkill skill = RefDataMgr.get(RefSkill.class, skillid);
                if (player.getLv() >= skill.Require) {
                    fighter.skillLauncher.addSkill(new Skill(fighter, skill, Math.max(bo.getSkill(i), 1)));
                }
            }

            creatures.add(fighter);
        }

        WarSpirit spirit = player.getFeature(WarSpiritFeature.class).getWarSpiritNow();
        if (spirit != null) {
            WarSpiritBO bo = spirit.getBo();
            RefWarSpirit refSpirit = RefDataMgr.get(RefWarSpirit.class, bo.getSpiritId());
            SpiritAttrCalculator calculator = spirit.getAttr();
            Creature fighter = new Creature(this, spirit.getSpiritId(), spirit.getSpiritId(), 0, calculator.getAttrs());
            fighter.level = player.getPlayerBO().getWarspiritLv();
            fighter.RoleType = RoleType.WarSpirit;
            fighter.SPD = refSpirit.SPD;
            fighter.RNG = refSpirit.RNG;

            for (int ix = 0; ix < refSpirit.SkillList.size(); ix++) {
                int skillid = refSpirit.SkillList.get(ix);
                RefSkill skill = RefDataMgr.get(RefSkill.class, skillid);
                if (skillid == 0) {
                    fighter.skillLauncher.addSkill(new Skill(fighter, skill, bo.getSkill()));
                } else {
                    fighter.skillLauncher.addSkill(new Skill(fighter, skill, 0));
                }
            }

            creatures.add(fighter);
        }

        return creatures;
    }

    List<Creature> getMonster(int monsterId) {
        List<Creature> creatures = new ArrayList<>();
        RefMonster refMonster = RefDataMgr.get(RefMonster.class, monsterId);
        Map<Attribute, Integer> attrs = new HashMap<>();
        attrs.put(Attribute.MaxHP, refMonster.MaxHP);
        attrs.put(Attribute.ATK, refMonster.ATK);
        attrs.put(Attribute.DEF, refMonster.DEF);
        attrs.put(Attribute.RGS, refMonster.RGS);
        attrs.put(Attribute.Hit, refMonster.Hit);
        attrs.put(Attribute.Dodge, refMonster.Dodge);
        attrs.put(Attribute.Critical, refMonster.Critical);
        attrs.put(Attribute.Tenacity, refMonster.Tenacity);
        Creature fighter = new Creature(this, monsterId, 0, 0, attrs);
        fighter.level = refMonster.Level;
        fighter.RoleType = RoleType.Monster;
        fighter.SPD = refMonster.SPD;
        fighter.RNG = refMonster.RNG;

        for (int i = 0; i < refMonster.SkillList.size(); i++) {
            int skillid = refMonster.SkillList.get(i);
            RefSkill skill = RefDataMgr.get(RefSkill.class, skillid);
            if (fighter.level >= skill.Require) {
                fighter.skillLauncher.addSkill(new Skill(fighter, skill, Math.max(0, 1)));
            }
        }

        creatures.add(fighter);
        return creatures;
    }

    protected abstract int fightTime();

    protected abstract int tiledWidth();

    protected abstract int tiledHeight();

    private void win() {
        this.result = FightResult.Win;
        this.onWin();
    }

    protected void lost() {
        this.result = FightResult.Lost;
        this.onLost();
    }

    protected abstract void onLost();

    protected abstract void onWin();

    public void fight() {
        this.initLoc();
        this.time = 0.0;
        this.stopped = false;
        int maxTime = this.fightTime();

        while (this.time < maxTime && !this.stopped) {
            double dt = 0.016666666666666666;
            List<Bullet> toremove = new ArrayList<>();

            for (int i = this.bullets.size() - 1; i >= 0; i--) {
                Bullet bullet = this.bullets.get(i);
                bullet.update(dt);
                if (!bullet.alive()) {
                    toremove.add(bullet);
                }
            }

            this.bullets.removeAll(toremove);
            Creature leader = this.getLeader(this.team);
            if (leader == null) {
                this.lost();
                return;
            }

            for (int ix = 0; ix < this.all.size(); ix++) {
                Creature creature = this.all.get(ix);
                creature.update(dt);
                if (!creature.isDead()) {
                    Creature enemy = this.getOpponent(creature);
                    if (enemy != null) {
                        creature.castSkill();
                        creature.stepTo(enemy, dt);
                    }
                }
            }

            this.time += dt;
        }
    }

    protected Creature getOpponent(Creature c) {
        if (c == null) {
            return null;
        } else {
            Creature closest = null;
            double d = Double.MAX_VALUE;

            for (int i = 0; i < this.all.size(); i++) {
                Creature e = this.all.get(i);
                if (!e.isDead() && !c.isFriend(e)) {
                    double d2 = this.distance(e, c);
                    if (d2 < d) {
                        d = d2;
                        closest = e;
                    }
                }
            }

            return closest;
        }
    }

    private double distance(Creature c1, Creature c2) {
        double dx = c1.x - c2.x;
        double dy = c1.y - c2.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    protected Creature getLeader(List<Creature> team) {
        for (int i = 0; i < team.size(); i++) {
            Creature m = team.get(i);
            if (!m.isDead() && m.RoleType != RoleType.WarSpirit) {
                return m;
            }
        }

        return null;
    }

    protected abstract void initLoc();

    protected void initLoc(List<Creature> fighters, int initx, int inity) {
        List<Point> list = new ArrayList<>();
        int size = fighters.size();

        for (int i = -size; i < size; i++) {
            for (int j = -size; j < size; j++) {
                list.add(new Point(initx + this.tiledWidth() * i, inity + this.tiledHeight() * j));
            }
        }

        this.shuffle(list);
        int idx = 0;

        for (Creature f : fighters) {
            Point point = list.get(idx++);
            f.x = point.x;
            f.y = point.y;
        }
    }

    protected <T> void shuffle(List<T> inputList) {
        for (int i = inputList.size() - 1; i >= 0; i--) {
            int randomIndex = (int)Math.floor(this.random() * (i + 1));
            T itemAtIndex = inputList.get(randomIndex);
            inputList.set(randomIndex, inputList.get(i));
            inputList.set(i, itemAtIndex);
        }
    }

    public void onDie(Creature creature) {
        if (this.getLeader(this.team) == null) {
            this.lost();
        } else if (this.getLeader(this.opponents) == null) {
            this.win();
        }
    }

    public void addBullet(Bullet bullet) {
        this.bullets.add(bullet);
    }
}
