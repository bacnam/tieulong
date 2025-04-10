package core.network.proto;

import business.player.item.Reward;

public class AccumRechargeInfo
{
private int awardId;
private int status;
private int recharge;
private Reward prize;

public int getAwardId() {
return this.awardId;
}

public void setAwardId(int awardId) {
this.awardId = awardId;
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

