package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.GuildJob;

public class RefGuildJobInfo
        extends RefBaseGame {
    @RefField(iskey = true)
    public GuildJob id;
    public boolean UpgradeGuild;
    public boolean ManageGuild;
    public boolean ChangeManifesto;
    public boolean ChangeNotice;
    public boolean DealRequest;
    public int JobAmount;
    public int UnlockLevel;
    public boolean SetTopMessage;
    public String JobName;
    public boolean OpenInstance;
    public boolean Kickout;
    public boolean GuildwarApply;

    public boolean Assert() {
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}

