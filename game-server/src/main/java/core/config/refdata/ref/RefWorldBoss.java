package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.utils.CommTime;

public class RefWorldBoss extends RefBaseGame {
    @RefField(
        iskey = true
    )
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
        return this.BeginTime <= CommTime.getTodayHour() && this.EndTime > CommTime.getTodayHour();
    }

    @Override
    public boolean Assert() {
        return !RefAssert.inRef(this.MailId, RefMail.class) ? false : this.BeginTime <= this.EndTime;
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}
