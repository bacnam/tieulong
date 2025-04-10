package core.config.refdata.ref;

import business.player.item.Reward;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import com.zhonglian.server.common.utils.Maps;
import core.config.refdata.RefDataMgr;
import java.util.List;
import java.util.Map;

public class RefGuildBossDamageReward
extends RefBaseGame
{
@RefField(iskey = true)
public int id;
public NumberRange DamageRange;
public int RewardId;
@RefField(isfield = false)
private static Map<Integer, List<RefGuildBossDamageReward>> damageRewardByBossId = Maps.newConcurrentMap();

public boolean Assert() {
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}

public static Reward getReward(int damage) {
for (RefGuildBossDamageReward ref : RefDataMgr.getAll(RefGuildBossDamageReward.class).values()) {
if (ref.DamageRange.within(damage)) {
RefReward refreward = (RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(ref.RewardId));
return refreward.genReward();
} 
} 
return null;
}
}

