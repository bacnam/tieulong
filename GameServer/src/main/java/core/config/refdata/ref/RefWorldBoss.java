package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.utils.CommTime;

public class RefWorldBoss
        extends RefBaseGame {
    @RefField(iskey = true)
    public int id;
    public String Name;
    public String MapId;
    public float UpMultiple;
    public float DownMultiple;
    public int BeginTime;
    public int EndTime;
    public int MailId;
    public int Level;
    public int MaxHP;
    public int BossId;

    public boolean isInOpenHour() {
        return (this.BeginTime <= CommTime.getTodayHour() && this.EndTime > CommTime.getTodayHour());
    }

    public boolean Assert() {
        if (!RefAssert.inRef(Integer.valueOf(this.MailId), RefMail.class, new Object[0])) {
            return false;
        }
        if (this.BeginTime > this.EndTime) {
            return false;
        }
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}

