package core.config.refdata.ref;

import BaseCommon.CommLog;
import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.SignInType;
import java.util.List;

public class RefSignIn
extends RefBaseGame
{
@RefField(iskey = true)
public int id;
public int Day;
public SignInType Type;
public List<Integer> UniformitemId;
public List<Integer> Count;
public int VIPLevel;
public int VIPTimes;
public int CardID;
@RefField(isfield = false)
public static int DailySignInMaxDay = 1;
@RefField(isfield = false)
public static int DailySignInMinDay = 1;
@RefField(isfield = false)
public static int DailySignInCount = 0;
@RefField(isfield = false)
public static int SignInOpenServerMaxDay = 1;
@RefField(isfield = false)
public static int SignInPrizeMaxDay = 1;

public boolean Assert() {
if (this.id % 1000 != this.Day) {
CommLog.error("[DailySignIn]数据出错,ID%1000要等于day");
return false;
} 
if (this.Type == SignInType.SignIn) {
DailySignInCount++;
DailySignInMaxDay = (this.Day > DailySignInMaxDay) ? this.Day : DailySignInMaxDay;
DailySignInMinDay = (this.Day < DailySignInMinDay) ? this.Day : DailySignInMinDay;
} 
if (this.Type == SignInType.SignInOpenServer && this.Day > SignInOpenServerMaxDay) {
SignInOpenServerMaxDay = this.Day;
}
if (this.Type == SignInType.SignInPrize && this.Day > SignInPrizeMaxDay) {
SignInPrizeMaxDay = this.Day;
}
if (!RefAssert.listSize(this.UniformitemId, this.Count, new List[0])) {
return false;
}
if (!RefAssert.inRef(this.UniformitemId, RefUniformItem.class, "id", new Object[0])) {
return false;
}
return true;
}

public boolean AssertAll(RefContainer<?> all) {
if (DailySignInMinDay != 1) {
CommLog.error("[RefSignIn表DailySignIn]签到天数最小为1");
return false;
} 

if (DailySignInCount == 0) {
CommLog.error("[RefSignIn表DailySignIn]缺少数据,签到的记录数目为0");
return false;
} 

if (DailySignInMaxDay != DailySignInCount) {
CommLog.error("[RefSignIn表DailySignIn]活动数目出错,天数出错");
return false;
} 
return true;
}
}

