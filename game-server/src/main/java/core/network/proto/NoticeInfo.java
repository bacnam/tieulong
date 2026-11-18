package core.network.proto;

import com.zhonglian.server.common.enums.ConstEnum.UniverseMessageType;
import java.util.List;

public class NoticeInfo {
    UniverseMessageType type;
    List<String> content;

    public NoticeInfo(UniverseMessageType type, List<String> content) {
        this.type = type;
        this.content = content;
    }
}
