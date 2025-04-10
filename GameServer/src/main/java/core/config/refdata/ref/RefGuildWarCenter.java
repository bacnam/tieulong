package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import com.zhonglian.server.common.utils.CommTime;

public class RefGuildWarCenter
extends RefBaseGame
{
@RefField(iskey = true)
public int id;
public String Name;
public NumberRange OpenTime;
public int MailId;
public int FailMail;
public int TakeMail;

public boolean Assert() {
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}

public boolean isOpenTime() {
return this.OpenTime.within(CommTime.getDayOfWeek());
}
}

