package core.network.proto;

import business.player.item.Reward;

public class AccumConsumeInfo
{
private int awardId;
private int status;
private int consume;
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

public int getConsume() {
return this.consume;
}

public void setConsume(int consume) {
this.consume = consume;
}

public Reward getPrize() {
return this.prize;
}

public void setPrize(Reward prize) {
this.prize = prize;
}
}

