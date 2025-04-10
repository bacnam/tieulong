package core.config.refdata.ref;

import business.player.Player;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.UnlockType;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;

public class RefUnlockFunction
extends RefBaseGame
{
@RefField(iskey = true)
public UnlockType id;
public int UnlockLevel;
public int UnlockVip;

public static void checkUnlock(Player player, UnlockType type) throws WSException {
RefUnlockFunction ref = (RefUnlockFunction)RefDataMgr.get(RefUnlockFunction.class, type);
if (ref == null) {
return;
}
if (ref.UnlockLevel != 0 && player.getLv() >= ref.UnlockLevel) {
return;
}
if (ref.UnlockVip != 0 && player.getVipLevel() >= ref.UnlockVip) {
return;
}

throw new WSException(ErrorCode.NotEnough_UnlockCond, "解锁功能%s 需要等级:%s[或]VIP:%s, 当前值%s,%s", new Object[] {
type, Integer.valueOf(ref.UnlockLevel), Integer.valueOf(ref.UnlockVip), Integer.valueOf(player.getLv()), Integer.valueOf(player.getVipLevel())
});
}

public static boolean checkUnlockSave(Player player, UnlockType type) {
RefUnlockFunction ref = (RefUnlockFunction)RefDataMgr.get(RefUnlockFunction.class, type);
if (ref == null) {
return true;
}
if (ref.UnlockLevel != 0 && player.getLv() >= ref.UnlockLevel) {
return true;
}
if (ref.UnlockVip != 0 && player.getVipLevel() >= ref.UnlockVip) {
return true;
}

return false;
}

public static boolean checkUnlockSave(int teamLevel, int vipLevel, UnlockType type) {
RefUnlockFunction ref = (RefUnlockFunction)RefDataMgr.get(RefUnlockFunction.class, type);
if (ref == null) {
return true;
}
if (ref.UnlockLevel != 0 && teamLevel >= ref.UnlockLevel) {
return true;
}
if (ref.UnlockVip != 0 && vipLevel >= ref.UnlockVip) {
return true;
}
return false;
}

public boolean Assert() {
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}
}

