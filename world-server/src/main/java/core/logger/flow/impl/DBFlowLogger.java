package core.logger.flow.impl;

import com.zhonglian.server.logger.flow.db.DBFlowMgr;
import core.logger.flow.WorldFlowLogger;
import core.logger.flow.impl.db.ActivityRequestFlow;
import core.logger.flow.impl.db.ChatLogFlow;

public class DBFlowLogger extends WorldFlowLogger {
    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public void activityRequest(
        String activity, boolean isActive, String gm_id, int beginTime, int endTime, int closeTime, String json, String type, String operator
    ) {
        DBFlowMgr.getInstance().add(new ActivityRequestFlow(activity, isActive, gm_id, beginTime, endTime, closeTime, json, type, operator));
    }

    @Override
    public void chatLog(int chatType, long receivePId, long senderPId, String content, int sendTime, int serverId) {
        DBFlowMgr.getInstance().add(new ChatLogFlow(chatType, receivePId, senderPId, content, sendTime, serverId));
    }
}
