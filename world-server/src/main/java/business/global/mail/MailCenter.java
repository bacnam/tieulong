package business.global.mail;

import BaseTask.SyncTask.SyncTask;
import BaseTask.SyncTask.SyncTaskManager;
import BaseThread.BaseMutexObject;
import com.zhonglian.server.common.Scid;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.response.ResponseHandler;
import com.zhonglian.server.websocket.handler.response.WebSocketResponse;
import core.database.world.bo.WorldMailBO;
import core.network.game2world.GameSession;
import core.network.game2world.GameSessionMgr;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import proto.common.Mail.SendMail;

public class MailCenter {
    private static MailCenter instance = new MailCenter();
    private final BaseMutexObject lock = new BaseMutexObject();
    private List<WorldMail> delayMailList = new ArrayList<>();
    List<WorldMailBO> bos = BM.getBM(WorldMailBO.class).findAll();

    public static MailCenter getInstance() {
        return instance;
    }

    private void lock() {
        this.lock.lock();
    }

    private void unlock() {
        this.lock.unlock();
    }

    public MailCenter() {
        this.lock.reduceMutexLevel(3);
    }

    public void init() {
        for (WorldMailBO bo : BM.getBM(WorldMailBO.class).findAll()) {
            this.delayMailList.add(new WorldMail(bo));
        }

        SyncTaskManager.task(new SyncTask() {
            @Override
            public void run() {
                boolean continueDo = MailCenter.this.delaySendMail();
                if (continueDo) {
                    SyncTaskManager.task(this);
                } else {
                    SyncTaskManager.task(this, 10000);
                }
            }
        }, 10000);
    }

    private void addMailWaitDelaySend(long scid, int mailID, String uniformIDList, String uniformCountList, String params) {
        WorldMailBO bo = new WorldMailBO();
        bo.setCid(scid);
        bo.setMailID(mailID);
        bo.setUniformIDList(uniformIDList);
        bo.setUniformCountList(uniformCountList);
        bo.setParams(params);
        bo.insert();
        this.lock();
        this.delayMailList.add(new WorldMail(bo));
        this.unlock();
    }

    private void removeDelaySend(WorldMail worldMail) {
        this.lock();
        this.delayMailList.remove(worldMail);
        this.unlock();
        worldMail.bo.del();
    }

    public void clearAll() {
        for (WorldMail mail : new ArrayList<>(this.delayMailList)) {
            this.removeDelaySend(mail);
        }
    }

    public void sendMail(long scid, int mailID) {
        this.sendMail(scid, mailID, "", "", "");
    }

    public void sendMail(final long scid, int serverId, final int mailID, final String uniformIDList, final String uniformCountList, final String params) {
        GameSession session = GameSessionMgr.getInstance().getSessionByServerID(serverId);
        if (session != null && session.isConnected()) {
            session.request("base.sendMail", new SendMail(scid, mailID, uniformIDList, uniformCountList, params), new ResponseHandler() {
                @Override
                public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException {
                }

                @Override
                public void handleError(WebSocketResponse ssresponse, short statusCode, String message) {
                    if (statusCode != ErrorCode.Player_NotFound.value()) {
                        MailCenter.this.addMailWaitDelaySend(scid, mailID, uniformIDList, uniformCountList, params);
                    }
                }
            });
        } else {
            this.addMailWaitDelaySend(scid, mailID, uniformIDList, uniformCountList, params);
        }
    }

    public void sendMail(final long scid, final int mailID, final String uniformIDList, final String uniformCountList, final String params) {
        GameSession session = GameSessionMgr.getInstance().getSessionByServerID(Scid.getSid(scid));
        if (session != null && session.isConnected()) {
            session.request("base.sendMail", new SendMail(scid, mailID, uniformIDList, uniformCountList, params), new ResponseHandler() {
                @Override
                public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException {
                }

                @Override
                public void handleError(WebSocketResponse ssresponse, short statusCode, String message) {
                    if (statusCode != ErrorCode.Player_NotFound.value()) {
                        MailCenter.this.addMailWaitDelaySend(scid, mailID, uniformIDList, uniformCountList, params);
                    }
                }
            });
        } else {
            this.addMailWaitDelaySend(scid, mailID, uniformIDList, uniformCountList, params);
        }
    }

    private boolean delaySendMail() {
        WorldMail tryData = null;
        GameSession session = null;
        this.lock();

        for (WorldMail worldMail : this.delayMailList) {
            if (worldMail.needTrySend()) {
                int serverId = Scid.getSid(worldMail.bo.getCid());
                session = GameSessionMgr.getInstance().getSessionByServerID(serverId);
                if (session != null && session.isConnected() && worldMail.isSending.compareAndSet(false, true)) {
                    tryData = worldMail;
                    break;
                }
            }
        }

        this.unlock();
        if (tryData == null) {
            return false;
        } else {
            this.doDelaySend(session, tryData);
            return true;
        }
    }

    private void doDelaySend(GameSession session, final WorldMail mail) {
        session.request(
            "base.sendMail",
            new SendMail(mail.bo.getCid(), mail.bo.getMailID(), mail.bo.getUniformIDList(), mail.bo.getUniformCountList(), mail.bo.getParams()),
            new ResponseHandler() {
                @Override
                public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException {
                    MailCenter.this.removeDelaySend(mail);
                }

                @Override
                public void handleError(WebSocketResponse ssresponse, short statusCode, String message) {
                    if (statusCode != ErrorCode.Player_NotFound.value()) {
                        mail.isSending.set(false);
                    }
                }
            }
        );
    }
}
