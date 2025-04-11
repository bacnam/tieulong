package core.network.proto;

import java.util.List;

public class LoverInfo {
    Player.Summary husband;
    Player.Summary wife;
    int level;
    int exp;
    int flowerTimes;
    List<Integer> alreadyPick;

    public List<Integer> getAlreadyPick() {
        return this.alreadyPick;
    }

    public void setAlreadyPick(List<Integer> alreadyPick) {
        this.alreadyPick = alreadyPick;
    }

    public int getFlowerTimes() {
        return this.flowerTimes;
    }

    public void setFlowerTimes(int flowerTimes) {
        this.flowerTimes = flowerTimes;
    }

    public Player.Summary getHusband() {
        return this.husband;
    }

    public void setHusband(Player.Summary husband) {
        this.husband = husband;
    }

    public Player.Summary getWife() {
        return this.wife;
    }

    public void setWife(Player.Summary wife) {
        this.wife = wife;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return this.exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}

