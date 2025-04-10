package core.network.proto;

import business.player.item.Reward;

public class DailyRechargeInfo {
private int awardId;
private int maxTimes;
private int receivedTimes;
private int status;
private int recharge;
private Reward prize;
private int leftTimes;

public int getLeftTimes() {
return this.leftTimes;
}

public void setLeftTimes(int leftTimes) {
this.leftTimes = leftTimes;
}

public int getAwardId() {
return this.awardId;
}

public void setAwardId(int awardId) {
this.awardId = awardId;
}

public int getMaxTimes() {
return this.maxTimes;
}

public void setMaxTimes(int maxTimes) {
this.maxTimes = maxTimes;
}

public int getReceivedTimes() {
return this.receivedTimes;
}

public void setReceivedTimes(int receivedTimes) {
this.receivedTimes = receivedTimes;
}

public int getStatus() {
return this.status;
}

public void setStatus(int status) {
this.status = status;
}

public int getRecharge() {
return this.recharge;
}

public void setRecharge(int recharge) {
this.recharge = recharge;
}

public Reward getPrize() {
return this.prize;
}

public void setPrize(Reward prize) {
this.prize = prize;
}
}

