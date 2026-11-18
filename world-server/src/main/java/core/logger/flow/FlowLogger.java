package core.logger.flow;

import com.zhonglian.server.logger.flow.FlowLoggerMgrBase;

public class FlowLogger extends FlowLoggerMgrBase<WorldFlowLogger> {
    private static FlowLogger instance = new FlowLogger();

    public static FlowLogger getInstance() {
        return instance;
    }

    public static void activityRequest(
        String activity, boolean isActive, String gm_id, int beginTime, int endTime, int closeTime, String json, String type, String operator
    ) {
        instance.log("activityRequest", new Object[]{activity, isActive, gm_id, beginTime, endTime, closeTime, json, type, operator});
    }

    public static void chatLog(int chatType, long receivePId, long senderPId, String content, int sendTime, int serverId) {
        instance.log("chatLog", new Object[]{chatType, receivePId, senderPId, content, sendTime, serverId});
    }
}
