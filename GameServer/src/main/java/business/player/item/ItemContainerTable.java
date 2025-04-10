package business.player.item;

import BaseCommon.CommClass;
import BaseCommon.CommLog;
import business.player.feature.Feature;
import com.zhonglian.server.common.enums.PrizeType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemContainerTable
{
private static Map<PrizeType, Class<? extends Feature>> table = new HashMap<>();

static {
List<Class<?>> clazzList = CommClass.getAllClassByInterface(IUniItemContainer.class, "business.player.feature");
for (Class<?> clazz : clazzList) {
try {
Method method = clazz.getDeclaredMethod("getType", new Class[0]);
Constructor<?> constructor = clazz.getConstructors()[0];
PrizeType type = (PrizeType)method.invoke(constructor.newInstance(new Object[1]), new Object[0]);
table.put(type, clazz);
} catch (Exception e) {
CommLog.error("[ItemContainerTable]初始化物品管理列表失败", e);
} 
} 
}

public static Class<? extends Feature> getItemLogic(PrizeType type) {
return table.get(type);
}
}

