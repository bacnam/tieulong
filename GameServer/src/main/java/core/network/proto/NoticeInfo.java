package core.network.proto;

import com.zhonglian.server.common.enums.ConstEnum;
import java.util.List;

public class NoticeInfo
{
ConstEnum.UniverseMessageType type;
List<String> content;

public NoticeInfo(ConstEnum.UniverseMessageType type, List<String> content) {
this.type = type;
this.content = content;
}
}

