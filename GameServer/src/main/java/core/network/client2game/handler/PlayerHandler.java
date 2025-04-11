package core.network.client2game.handler;

import BaseCommon.CommLog;
import business.player.Player;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.websocket.PkgCacheMgr;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.ClientSession;

import java.io.IOException;

public abstract class PlayerHandler
        extends BaseHandler {
    public void handle(WebSocketRequest request, String message) throws IOException {
        long costSec;
        ClientSession session = (ClientSession) request.getSession();
        if (!session.isValid()) {
            return;
        }
        Player player = session.getPlayer();
        if (player == null) {
            request.error(ErrorCode.Player_NotLogin, "玩家未登陆", new Object[0]);
            session.close();
            return;
        }
        PkgCacheMgr.PkgRecv cachedRecv = player.getPacketCache().fetchSentOrRegist(request.getHeader());
        if (cachedRecv != null) {
            if (cachedRecv.getBody() != null) {
                session.sendPacket(cachedRecv.getHeader(), cachedRecv.getBody());
            }
            return;
        }
        long curTime = CommTime.nowMS();
        try {
            player.lockIns();
            handle(player, request, message);
        } catch (WSException e) {
            CommLog.warn("[PlayerHandler]:[{}] cid:{} error:{}", new Object[]{(request.getHeader()).event, Long.valueOf(player.getPid()), e.getMessage()});
            request.error(e.getErrorCode(), e.getMessage(), new Object[0]);
        } catch (Exception e) {
            CommLog.error("[PlayerHandler]:[{}] cid:{} error:{}", new Object[]{(request.getHeader()).event, Long.valueOf(player.getPid()), e.getMessage(), e});
            request.error(ErrorCode.Unknown, "服务端发生异常，异常信息：" + e.getMessage(), new Object[0]);
        } finally {
            player.unlockIns();
            long l = CommTime.nowMS() - curTime;
            if (l >= 1000L)
                CommLog.error("[PlayerHandler]:[{}] cid:{} overtime cost:{}", new Object[]{(request.getHeader()).event, Long.valueOf(player.getPid()), Long.valueOf(l)});
        }
    }

    public abstract void handle(Player paramPlayer, WebSocketRequest paramWebSocketRequest, String paramString) throws WSException, IOException;
}

