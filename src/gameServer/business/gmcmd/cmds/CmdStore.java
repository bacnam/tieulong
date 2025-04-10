package business.gmcmd.cmds;

import business.gmcmd.annotation.Command;
import business.gmcmd.annotation.Commander;
import business.player.Player;
import business.player.feature.store.PlayerStore;
import business.player.feature.store.StoreFeature;
import business.player.feature.store.StoreRecord;
import com.zhonglian.server.common.enums.StoreType;
import com.zhonglian.server.common.utils.Lists;
import com.zhonglian.server.websocket.exception.WSException;
import java.util.List;

@Commander(name = "store", comment = "商城相关命令")
public class CmdStore
{
@Command(comment = "增加免费刷新次数")
public String addfreetimes(Player player, int times) {
List<Integer> list = Lists.newArrayList(); byte b; int i; StoreType[] arrayOfStoreType;
for (i = (arrayOfStoreType = StoreType.values()).length, b = 0; b < i; ) { StoreType type = arrayOfStoreType[b];
list.add(Integer.valueOf(type.ordinal())); b++; }

((StoreFeature)player.getFeature(StoreFeature.class)).doAddFreeRefreshTimes(list, times);
return "增加免费刷新次数";
}

@Command(comment = "手动刷新")
public String hander(Player player, int typeId) throws WSException {
if (typeId <= StoreType.None.ordinal() || typeId >= StoreType.Max.ordinal()) {
return "非法的刷新类型";
}
StoreType storeType = StoreType.values()[typeId];
StoreFeature feature = (StoreFeature)player.getFeature(StoreFeature.class);

PlayerStore playerStore = feature.getOrCreate(storeType);

playerStore.doAutoRefresh();

return "手动刷新成功";
}

@Command(comment = "重置刷新次数等信息")
public String record(Player player) throws WSException {
((StoreRecord)player.getFeature(StoreRecord.class)).dailyRefresh();
return "重置刷新次数成功";
}
}

