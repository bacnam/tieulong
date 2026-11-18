package business.global.mail;

import com.zhonglian.server.common.utils.CommTime;
import core.database.world.bo.WorldMailBO;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorldMail {
    public WorldMailBO bo;
    public AtomicBoolean isSending = new AtomicBoolean(false);
    private int lastSendTime = 0;
    private int trySendTimes = 0;

    public WorldMail(WorldMailBO bo) {
        this.bo = bo;
    }

    public boolean needTrySend() {
        if (CommTime.nowSecond() - this.lastSendTime > Math.pow(2.0, this.trySendTimes)) {
            this.lastSendTime = CommTime.nowSecond();
            this.trySendTimes++;
            return true;
        } else {
            return false;
        }
    }
}
