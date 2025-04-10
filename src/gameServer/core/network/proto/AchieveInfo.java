package core.network.proto;

import java.util.List;

public class AchieveInfo
{
int achieveId;
int completeCount;
int achieveCount;
long argument1;
long argument2;
long argument3;
List<Integer> gainPrizeList;

public int getAchieveId() {
return this.achieveId;
}

public void setAchieveId(int achieveId) {
this.achieveId = achieveId;
}

public int getCompleteCount() {
return this.completeCount;
}

public void setCompleteCount(int completeCount) {
this.completeCount = completeCount;
}

public int getAchieveCount() {
return this.achieveCount;
}

public void setAchieveCount(int achieveCount) {
this.achieveCount = achieveCount;
}

public List<Integer> getGainPrizeList() {
return this.gainPrizeList;
}

public void setGainPrizeList(List<Integer> gainPrizeList) {
this.gainPrizeList = gainPrizeList;
}

public long getArgument1() {
return this.argument1;
}

public void setArgument1(long argument1) {
this.argument1 = argument1;
}

public long getArgument2() {
return this.argument2;
}

public void setArgument2(long argument2) {
this.argument2 = argument2;
}

public long getArgument3() {
return this.argument3;
}

public void setArgument3(long argument3) {
this.argument3 = argument3;
}
}

