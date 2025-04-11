package core.network.proto;

import business.player.item.Reward;

public class OpenServerRankRewardInfo {
    int count;
    boolean isPicked;
    Reward reward;

    public OpenServerRankRewardInfo(int count, boolean isPicked) {
        this.count = count;
        this.isPicked = isPicked;
    }

    public OpenServerRankRewardInfo(int count, boolean isPicked, Reward reward) {
        this.count = count;
        this.isPicked = isPicked;
        this.reward = reward;
    }
}

