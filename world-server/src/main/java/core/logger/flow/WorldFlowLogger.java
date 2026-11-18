package core.logger.flow;

import com.zhonglian.server.logger.flow.FlowLoggerBase;

public abstract class WorldFlowLogger extends FlowLoggerBase {
    public void activityRequest(
        String activity, boolean isActive, String gm_id, int beginTime, int endTime, int closeTime, String json, String type, String operator
    ) {
    }

    public void chatLog(int chatType, long receivePId, long senderPId, String content, int sendTime, int serverId) {
    }
}
