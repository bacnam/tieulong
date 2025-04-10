package proto.gameworld;

import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import com.zhonglian.server.common.utils.Lists;
import com.zhonglian.server.common.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class RankAward
{
public int aid;
public NumberRange rankrange;
public ArrayList<UniformItemInfo> reward;

public String uniformItemIds() {
List<Integer> list = Lists.newArrayList();
for (UniformItemInfo uniform : this.reward) {
list.add(Integer.valueOf(uniform.uniformId));
}
return StringUtils.list2String(list);
}

public String uniformItemCounts() {
List<Integer> list = Lists.newArrayList();
for (UniformItemInfo uniform : this.reward) {
list.add(Integer.valueOf(uniform.count));
}
return StringUtils.list2String(list);
}
}

