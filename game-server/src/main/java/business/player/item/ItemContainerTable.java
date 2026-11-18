package business.player.item;

import BaseCommon.CommClass;
import BaseCommon.CommLog;
import business.player.feature.Feature;
import com.zhonglian.server.common.enums.PrizeType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ItemContainerTable {
    private static Map<PrizeType, Class<? extends Feature>> table = new HashMap<>();

    static {
        for (Class<?> clazz : CommClass.getAllClassByInterface(IUniItemContainer.class, "business.player.feature")) {
            try {
                Method method = clazz.getDeclaredMethod("getType");
                Constructor<?> constructor = clazz.getConstructors()[0];
                PrizeType type = (PrizeType)method.invoke(constructor.newInstance());
                table.put(type, (Class<? extends Feature>)clazz);
            } catch (Exception var6) {
                CommLog.error("[ItemContainerTable]初始化物品管理列表失败", (Throwable)var6);
            }
        }
    }

    public static Class<? extends Feature> getItemLogic(PrizeType type) {
        return table.get(type);
    }
}
