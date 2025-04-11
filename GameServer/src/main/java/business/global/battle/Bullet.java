package business.global.battle;

import java.util.List;

public class Bullet {
    double time;
    Skill skill;
    Creature selected;
    List<Creature> castOn;

    public Bullet(Skill skill, Creature selected, List<Creature> castOn) {
        double dx = skill.creature.x - selected.x;
        double dy = skill.creature.y - selected.y;
        this.time = Math.sqrt(dx * dx + dy * dy) / skill.skilldata.BulletSpeed;

        this.skill = skill;
        this.selected = selected;
        this.castOn = castOn;
    }

    public void update(double dt) {
        this.time -= dt;
        if (this.time <= 0.0D) {
            this.skill.settle(this.selected, this.castOn);
        }
    }

    public boolean alive() {
        return (this.time > 0.0D);
    }
}

