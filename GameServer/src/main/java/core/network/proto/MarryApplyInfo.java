package core.network.proto;

public class MarryApplyInfo
{
Player.Summary summary;
int leftTime;

public Player.Summary getSummary() {
return this.summary;
}

public void setSummary(Player.Summary summary) {
this.summary = summary;
}

public int getLeftTime() {
return this.leftTime;
}

public void setLeftTime(int leftTime) {
this.leftTime = leftTime;
}
}

