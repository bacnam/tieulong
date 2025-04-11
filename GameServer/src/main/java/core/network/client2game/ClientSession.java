package core.network.client2game;

import business.player.Player;
import com.zhonglian.server.websocket.def.MessageType;
import com.zhonglian.server.websocket.def.TerminalType;
import com.zhonglian.server.websocket.handler.MessageHeader;
import com.zhonglian.server.websocket.server.ServerSession;
import core.server.ServerConfig;
import org.apache.mina.core.session.IoSession;

public class ClientSession
        extends ServerSession {
    private Player player;
    private String playerOpenId;
    private int playerSid;
    private String accessToken;
    private int encryptedkey;
    private boolean valid;
    private boolean isPrint = false;

    public ClientSession(IoSession session, long sessionID) {
        super(TerminalType.GameServer, ServerConfig.ServerID(), TerminalType.Client, session, sessionID);
        this.valid = false;
        this.encryptedkey = (int) sessionID;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void bindPlayer(Player player) {
        this.player = player;
    }

    public void losePlayer() {
        if (this.player != null) {
            this.player.loseSession();
            this.player = null;
        }
        notifyMessage("kickout", "账户已失效，请重新连接");
        close();
    }

    public boolean isPrint() {
        return this.isPrint;
    }

    public void setPrint(boolean isPrint) {
        this.isPrint = isPrint;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String access_token) {
        this.accessToken = access_token;
    }

    public int getEncryptedkey() {
        return this.encryptedkey;
    }

    public void setEncryptedkey(int encryptedkey) {
        this.encryptedkey = encryptedkey;
    }

    public boolean isValid() {
        return this.valid;
    }

    public void setValid(boolean isValid) {
        this.valid = isValid;
    }

    public String getOpenId() {
        return this.playerOpenId;
    }

    public void setOpenId(String playerPid) {
        this.playerOpenId = playerPid;
    }

    public int getPlayerSid() {
        return this.playerSid;
    }

    public void setPlayerSid(int playerSid) {
        this.playerSid = playerSid;
    }

    public void onCreated() {
    }

    public void onClosed() {
        if (this.player != null)
            this.player.loseSession();
    }

    protected void onSendPacket(MessageHeader header, String body) {
        if (this.player != null && header.messageType != MessageType.Notify)
            this.player.getPacketCache().cacheSent(header, body);
    }
}

