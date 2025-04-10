package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.ActivityType;

public class RefActivity
extends RefBaseGame
{
@RefField(iskey = true)
public ActivityType id;
public int ActNo;
public boolean Open;
public int BeginTime;
public int EndTime;
public int CloseTime;
public String MailTitle;
public String MailContent;
public String MailSender;
public String Json;

public boolean Assert() {
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}
}

