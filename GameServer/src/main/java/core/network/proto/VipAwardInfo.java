package core.network.proto;

import business.player.item.Reward;

public class VipAwardInfo {
    int awardId;
    int MaxTimes;
    int buyTimes;
    String icon;
    int vip;
    int price;
    int discount;
    Reward reward;
    String timeslist;

    public String getTimeslist() {
        return this.timeslist;
    }

    public void setTimeslist(String timeslist) {
        this.timeslist = timeslist;
    }

    public Reward getReward() {
        return this.reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getVip() {
        return this.vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return this.discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getAwardId() {
        return this.awardId;
    }

    public void setAwardId(int awardId) {
        this.awardId = awardId;
    }

    public int getMaxTimes() {
        return this.MaxTimes;
    }

    public void setMaxTimes(int maxTimes) {
        this.MaxTimes = maxTimes;
    }

    public int getBuyTimes() {
        return this.buyTimes;
    }

    public void setBuyTimes(int buyTimes) {
        this.buyTimes = buyTimes;
    }
}

