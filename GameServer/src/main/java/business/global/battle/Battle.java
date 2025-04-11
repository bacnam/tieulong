package business.global.battle;

import business.player.Player;
import business.player.feature.character.*;
import business.player.feature.character.Character;
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
    protected List<Creature> all;
    protected List<Creature> team;
    protected List<Creature> opponents;
    protected List<Bullet> bullets;
    protected boolean stopped;
    protected double time;
    int fightId = nextFightId++;
    long seed = this.fightId;
    FightResult result;
    Player me;
    Player opponent;
    public Battle(Player me, Player opponent) {
        this.team = getTeam(me);
        this.team.forEach(c -> {
        });
        this.opponents = getTeam(opponent);
        this.opponents.forEach(c -> {

        });
        this.all = new ArrayList<>();
        this.all.addAll(this.team);
        this.all.addAll(this.opponents);

        this.bullets = new ArrayList<>();
        this.me = me;
        this.opponent = opponent;
    }

    public Battle(Player me, int monsterId) {
        this.team = getTeam(me);
        this.team.forEach(c -> {
        });
        this.opponents = getMonster(monsterId);
        this.opponents.forEach(c -> {

        });
        this.all = new ArrayList<>();
        this.all.addAll(this.team);
        this.all.addAll(this.opponents);
        this.bullets = new ArrayList<>();
    }

    public Battle() {
    }

    double random() {
        this.seed = (this.seed * 9301L + 49297L) % 233280L;
        return this.seed / 233280.0D;
    }

    public int getFightId() {
        return this.fightId;
    }

    public Fight.Battle proto() {
        return new Fight.Battle(this.fightId, getMap(), this.team, this.opponents);
    }

    List<Creature> getTeam(Player player) {
        List<Creature> creatures = new ArrayList<>();

        List<Character> characters =
                new ArrayList<>(((CharFeature) player.getFeature(CharFeature.class)).getAll().values());
        characters.sort((left, right) -> (left.getBo().getId() < right.getBo().getId()) ? -1 : 1);
        for (Character character : characters) {
            int charid = character.getCharId();
            CharacterBO bo = character.getBo();

            Equip equip = character.getEquip(EquipPos.Body);
            int equipid = (equip == null) ? 0 : equip.getEquipId();

            CharAttrCalculator charAttrCalculator = character.getAttr();
            Creature fighter = new Creature(this, charid, equipid, bo.getWing(), charAttrCalculator.getAttrs());
            fighter.level = player.getLv();
            fighter.RoleType = RoleType.Character;

            RefCharacter refChar = (RefCharacter) RefDataMgr.get(RefCharacter.class, Integer.valueOf(charid));
            fighter.SPD = refChar.SPD;
            fighter.RNG = refChar.RNG;
            for (int i = 0; i < refChar.SkillList.size(); i++) {
                int skillid = ((Integer) refChar.SkillList.get(i)).intValue();
                RefSkill skill = (RefSkill) RefDataMgr.get(RefSkill.class, Integer.valueOf(skillid));
                if (player.getLv() >= skill.Require) {

                    fighter.skillLauncher.addSkill(new Skill(fighter, skill, Math.max(bo.getSkill(i), 1)));
                }
            }
            creatures.add(fighter);
        }

        WarSpirit spirit = ((WarSpiritFeature) player.getFeature(WarSpiritFeature.class)).getWarSpiritNow();
        if (spirit != null) {
            WarSpiritBO bo = spirit.getBo();
            RefWarSpirit refSpirit = (RefWarSpirit) RefDataMgr.get(RefWarSpirit.class, Integer.valueOf(bo.getSpiritId()));

            SpiritAttrCalculator calculator = spirit.getAttr();
            Creature fighter = new Creature(this, spirit.getSpiritId(), spirit.getSpiritId(), 0, calculator.getAttrs());
            fighter.level = player.getPlayerBO().getWarspiritLv();
            fighter.RoleType = RoleType.WarSpirit;

            fighter.SPD = refSpirit.SPD;
            fighter.RNG = refSpirit.RNG;
            for (int i = 0; i < refSpirit.SkillList.size(); i++) {
                int skillid = ((Integer) refSpirit.SkillList.get(i)).intValue();
                RefSkill skill = (RefSkill) RefDataMgr.get(RefSkill.class, Integer.valueOf(skillid));
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

        RefMonster refMonster = (RefMonster) RefDataMgr.get(RefMonster.class, Integer.valueOf(monsterId));

        Map<Attribute, Integer> attrs = new HashMap<>();
        attrs.put(Attribute.MaxHP, Integer.valueOf(refMonster.MaxHP));
        attrs.put(Attribute.ATK, Integer.valueOf(refMonster.ATK));
        attrs.put(Attribute.DEF, Integer.valueOf(refMonster.DEF));
        attrs.put(Attribute.RGS, Integer.valueOf(refMonster.RGS));
        attrs.put(Attribute.Hit, Integer.valueOf(refMonster.Hit));
        attrs.put(Attribute.Dodge, Integer.valueOf(refMonster.Dodge));
        attrs.put(Attribute.Critical, Integer.valueOf(refMonster.Critical));
        attrs.put(Attribute.Tenacity, Integer.valueOf(refMonster.Tenacity));
        Creature fighter = new Creature(this, monsterId, 0, 0, attrs);
        fighter.level = refMonster.Level;
        fighter.RoleType = RoleType.Monster;

        fighter.SPD = refMonster.SPD;
        fighter.RNG = refMonster.RNG;
        for (int i = 0; i < refMonster.SkillList.size(); i++) {
            int skillid = ((Integer) refMonster.SkillList.get(i)).intValue();
            RefSkill skill = (RefSkill) RefDataMgr.get(RefSkill.class, Integer.valueOf(skillid));
            if (fighter.level >= skill.Require) {

                fighter.skillLauncher.addSkill(new Skill(fighter, skill, Math.max(0, 1)));
            }
        }
        creatures.add(fighter);

        return creatures;
    }

    private void win() {
        this.result = FightResult.Win;
        onWin();
    }

    protected void lost() {
        this.result = FightResult.Lost;
        onLost();
    }

    public void fight() {
        initLoc();

        this.time = 0.0D;
        this.stopped = false;
        int maxTime = fightTime();

        while (this.time < maxTime && !this.stopped) {
            double dt = 0.016666666666666666D;

            List<Bullet> toremove = new ArrayList<>();
            for (int i = this.bullets.size() - 1; i >= 0; i--) {
                Bullet bullet = this.bullets.get(i);
                bullet.update(dt);
                if (!bullet.alive())
                    toremove.add(bullet);
            }
            this.bullets.removeAll(toremove);

            Creature leader = getLeader(this.team);
            if (leader == null) {
                lost();

                return;
            }

            for (int j = 0; j < this.all.size(); j++) {
                Creature creature = this.all.get(j);
                creature.update(dt);
                if (!creature.isDead()) {

                    Creature enemy = getOpponent(creature);
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
        }
        Creature closest = null;
        double d = Double.MAX_VALUE;
        for (int i = 0; i < this.all.size(); i++) {
            Creature e = this.all.get(i);
            if (!e.isDead()) {

                if (!c.isFriend(e)) {

                    double d2 = distance(e, c);
                    if (d2 < d) {
                        d = d2;
                        closest = e;
                    }
                }
            }
        }
        return closest;
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

    protected void initLoc(List<Creature> fighters, int initx, int inity) {
        List<Point> list = new ArrayList<>();

        int size = fighters.size();
        for (int i = -size; i < size; i++) {
            for (int j = -size; j < size; j++) {
                list.add(new Point(initx + tiledWidth() * i, inity + tiledHeight() * j));
            }
        }
        shuffle(list);
        int idx = 0;
        for (Creature f : fighters) {
            Point point = list.get(idx++);
            f.x = point.x;
            f.y = point.y;
        }
    }

    protected <T> void shuffle(List<T> inputList) {
        for (int i = inputList.size() - 1; i >= 0; i--) {
            int randomIndex = (int) Math.floor(random() * (i + 1));
            T itemAtIndex = inputList.get(randomIndex);

            inputList.set(randomIndex, inputList.get(i));
            inputList.set(i, itemAtIndex);
        }
    }

    public void onDie(Creature creature) {
        if (getLeader(this.team) == null) {
            lost();
        } else if (getLeader(this.opponents) == null) {
            win();
        }
    }

    public void addBullet(Bullet bullet) {
        this.bullets.add(bullet);
    }

    protected abstract String getMap();

    protected abstract int fightTime();

    protected abstract int tiledWidth();

    protected abstract int tiledHeight();

    protected abstract void onLost();

    protected abstract void onWin();

    protected abstract void initLoc();
}

