package core.config.refdata.ref;

import business.player.Player;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.UnlockType;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;

public class RefUnlockFunction extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public UnlockType id;
    public int UnlockLevel;
    public int UnlockVip;

    public static void checkUnlock(Player player, UnlockType type) throws WSException {
        RefUnlockFunction ref = RefDataMgr.get(RefUnlockFunction.class, type);
        if (ref != null) {
            if (ref.UnlockLevel == 0 || player.getLv() < ref.UnlockLevel) {
                if (ref.UnlockVip == 0 || player.getVipLevel() < ref.UnlockVip) {
                    throw new WSException(
                        ErrorCode.NotEnough_UnlockCond,
                        "解锁功能%s 需要等级:%s[或]VIP:%s, 当前值%s,%s",
                        type,
                        ref.UnlockLevel,
                        ref.UnlockVip,
                        player.getLv(),
                        player.getVipLevel()
                    );
                }
            }
        }
    }

    public static boolean checkUnlockSave(Player player, UnlockType type) {
        RefUnlockFunction ref = RefDataMgr.get(RefUnlockFunction.class, type);
        if (ref == null) {
            return true;
        } else {
            return ref.UnlockLevel != 0 && player.getLv() >= ref.UnlockLevel ? true : ref.UnlockVip != 0 && player.getVipLevel() >= ref.UnlockVip;
        }
    }

    public static boolean checkUnlockSave(int teamLevel, int vipLevel, UnlockType type) {
        RefUnlockFunction ref = RefDataMgr.get(RefUnlockFunction.class, type);
        if (ref == null) {
            return true;
        } else {
            return ref.UnlockLevel != 0 && teamLevel >= ref.UnlockLevel ? true : ref.UnlockVip != 0 && vipLevel >= ref.UnlockVip;
        }
    }

    @Override
    public boolean Assert() {
        return true;
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}
