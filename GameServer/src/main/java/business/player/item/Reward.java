package business.player.item;

import BaseCommon.CommLog;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.utils.Lists;
import com.zhonglian.server.common.utils.StringUtils;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefUniformItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reward
        extends ArrayList<UniformItem> {
    private static final long serialVersionUID = -7652426360495764584L;

    public Reward() {
    }

    public Reward(PrizeType crystal, int count) {
        add(crystal, count);
    }

    public Reward(int uniformId, int count) {
        add(uniformId, count);
    }

    public Reward(List<Integer> uniformIdList, List<Integer> countList) {
        add(uniformIdList, countList);
    }

    public Reward(String str_uniformIds, String str_counts) {
        List<Integer> uniformIdList = StringUtils.string2Integer(str_uniformIds);
        List<Integer> countList = StringUtils.string2Integer(str_counts);
        add(uniformIdList, countList);
    }

    public Reward(JsonArray itemArray) throws WSException {
        for (JsonElement itemElement : itemArray) {
            JsonObject itemObj = itemElement.getAsJsonObject();
            int uniformId = itemObj.get("uniformId").getAsInt();
            if (RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformId)) == null) {
                throw new WSException(ErrorCode.InvalidParam, "不存在物品%s", new Object[]{Integer.valueOf(uniformId)});
            }
            int count = itemObj.get("count").getAsInt();
            if (count <= 0) {
                throw new WSException(ErrorCode.InvalidParam, "不存在物品数量不允许小于等于0", new Object[]{Integer.valueOf(count)});
            }
            add(uniformId, count);
        }
    }

    public Reward add(PrizeType type, int itemId, int count) {
        int uniformid = ItemUtils.getUniformId(type, itemId);
        if (uniformid == 0) {
            CommLog.error("添加奖励道具错误,Type:{}, ID:{} 不存在", type, Integer.valueOf(itemId));
        }
        return add(uniformid, count);
    }

    public Reward add(PrizeType currency, int count) {
        int uniformid = ItemUtils.getUniformId(currency, 0);
        if (uniformid == 0) {
            CommLog.error("添加奖励货币错误,Type:{}不存在", currency);
        }
        return add(uniformid, count);
    }

    public Reward add(List<Integer> uniformId, List<Integer> count) {
        for (int index = 0; index < uniformId.size(); index++) {
            add(((Integer) uniformId.get(index)).intValue(), ((Integer) count.get(index)).intValue());
        }
        return this;
    }

    public Reward add(int uniformId, int count) {
        if (count <= 0) {
            return this;
        }
        if (uniformId != -1 && !RefDataMgr.getAll(RefUniformItem.class).containsKey(Integer.valueOf(uniformId))) {
            return this;
        }
        add(new UniformItem(uniformId, count));
        return this;
    }

    public boolean add(UniformItem item) {
        if (item != null) {
            return super.add(item);
        }
        return false;
    }

    public void combine(Reward reward) {
        if (reward == null) {
            return;
        }
        for (UniformItem item : reward) {
            add(item.getUniformId(), item.getCount());
        }
    }

    public ArrayList<UniformItem> merge() {
        Map<Integer, UniformItem> map = new HashMap<>();
        for (UniformItem item : this) {
            UniformItem ui = map.get(Integer.valueOf(item.getUniformId()));
            if (ui == null) {
                ui = new UniformItem(item.getUniformId(), item.getCount());
                map.put(Integer.valueOf(item.getUniformId()), ui);
                continue;
            }
            ui.addCount(item.getCount());
        }

        return new ArrayList<>(map.values());
    }

    public void multiply(int number) {
        if (number == 1) {
            return;
        }
        stream().forEach(x -> x.setCount(x.getCount() * paramInt));
    }

    public void replaceItem(int replaceId) {
        for (UniformItem uniformItem : this) {
            if (uniformItem.getUniformId() == -1) {
                uniformItem.setUniformId(replaceId);
            }
        }
    }

    public String uniformItemIds() {
        List<Integer> list = Lists.newArrayList();
        for (UniformItem uniform : this) {
            list.add(Integer.valueOf(uniform.getUniformId()));
        }
        return StringUtils.list2String(list);
    }

    public String uniformItemCounts() {
        List<Integer> list = Lists.newArrayList();
        for (UniformItem uniform : this) {
            list.add(Integer.valueOf(uniform.getCount()));
        }
        return StringUtils.list2String(list);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (UniformItem item : this) {
            sb.append(item.getUniformId()).append(":").append(item.getCount()).append(";");
        }
        return sb.toString();
    }

    public void modify(int remain) {
        int amount = 0;
        for (UniformItem item : this) {
            amount += item.getCount();
        }
        if (amount >= remain)
            return;
    }
}

