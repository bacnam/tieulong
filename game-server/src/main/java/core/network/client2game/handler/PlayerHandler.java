package core.network.client2game.handler;

import BaseCommon.CommLog;
import business.player.Player;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.websocket.PkgCacheMgr.PkgRecv;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.ClientSession;
import java.io.IOException;

public abstract class PlayerHandler extends BaseHandler {
    @Override
    public void handle(WebSocketRequest request, String message) throws IOException {
        ClientSession session = (ClientSession)request.getSession();
        if (session.isValid()) {
            Player player = session.getPlayer();
            if (player == null) {
                request.error(ErrorCode.Player_NotLogin, "玩家未登陆");
                session.close();
            } else {
                PkgRecv cachedRecv = player.getPacketCache().fetchSentOrRegist(request.getHeader());
                if (cachedRecv != null) {
                    if (cachedRecv.getBody() != null) {
                        session.sendPacket(cachedRecv.getHeader(), cachedRecv.getBody());
                    }
                } else {
                    long curTime = CommTime.nowMS();

                    try {
                        player.lockIns();
                        this.handle(player, request, message);
                    } catch (WSException var15) {
                        CommLog.warn("[PlayerHandler]:[{}] cid:{} error:{}", request.getHeader().event, player.getPid(), var15.getMessage());
                        request.error(var15.getErrorCode(), var15.getMessage());
                    } catch (Exception var16) {
                        CommLog.error("[PlayerHandler]:[{}] cid:{} error:{}", request.getHeader().event, player.getPid(), var16.getMessage(), var16);
                        request.error(ErrorCode.Unknown, "服务端发生异常，异常信息：" + var16.getMessage());
                    } finally {
                        player.unlockIns();
                        long costSec = CommTime.nowMS() - curTime;
                        if (costSec >= 1000L) {
                            CommLog.error("[PlayerHandler]:[{}] cid:{} overtime cost:{}", request.getHeader().event, player.getPid(), costSec);
                        }
                    }
                }
            }
        }
    }

    public abstract void handle(Player var1, WebSocketRequest var2, String var3) throws WSException, IOException;
}
