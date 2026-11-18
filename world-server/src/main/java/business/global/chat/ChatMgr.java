package business.global.chat;

import BaseTask.SyncTask.SyncTaskManager;
import BaseThread.BaseMutexObject;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.ChatType;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.database.world.bo.ChatMessageBO;
import core.logger.flow.FlowLogger;
import core.network.game2world.GameSessionMgr;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import proto.gameworld.ChatMessage;

public class ChatMgr {
    private static ChatMgr instance;
    protected final BaseMutexObject m_mutex = new BaseMutexObject();
    public final Queue<ChatMessageBO> worldChat = new LinkedList<>();
    public final Queue<ChatMessageBO> chatQueue = new LinkedList<>();
    private int chatRecordCount;

    public static ChatMgr getInstance() {
        if (instance == null) {
            instance = new ChatMgr();
        }

        return instance;
    }

    public ChatMgr() {
        this.loadChatMessages();
        this.broadCastWorldChat();
        this.chatRecordCount = RefDataMgr.getFactor("AllWorldChatRecordCount", 30);
    }

    public void lock() {
        this.m_mutex.lock();
    }

    public void unlock() {
        this.m_mutex.unlock();
    }

    public boolean loadChatMessages() {
        List<ChatMessageBO> all = BM.getBM(ChatMessageBO.class).findAll();
        Collections.sort(all, (o1, o2) -> o1.getSendTime() - o2.getSendTime());

        for (ChatMessageBO bo : all) {
            switch (ChatType.values()[bo.getChatType()]) {
                case CHATTYPE_AllWORlD:
                    this.worldChat.add(bo);
            }
        }

        return true;
    }

    public boolean broadCastWorldChat() {
        SyncTaskManager.schedule(100, () -> {
            this.broadcast();
            return true;
        });
        return true;
    }

    public int getChatRecordCount() {
        return this.chatRecordCount;
    }

    public void addChat(ChatMessage message) throws WSException {
        ChatMessageBO bo = new ChatMessageBO();
        bo.setChatType(message.getType().ordinal());
        bo.setSenderPid(message.getSenderPid());
        if (message.getSenderName() != null) {
            bo.setSenderName(message.getSenderName());
        }

        bo.setSenderLv(message.getSenderLv());
        bo.setSenderVipLv(message.getSenderVipLv());
        bo.setSenderIcon(message.getSenderIcon());
        if (message.getMessage() != null) {
            bo.setMessage(message.getMessage());
        }

        if (message.getContent() != null) {
            bo.setContent(message.getContent());
        }

        bo.setSendTime(message.getSendTime());
        bo.setReceivePid(message.getReceivePid());
        if (message.getReceiveName() != null) {
            bo.setReceiveName(message.getReceiveName());
        }

        bo.setMonthCard(message.isMonthCard());
        bo.setYearCard(message.isYearCard());
        bo.setIsMarried(message.isIs_married());
        bo.setSenderServerId(message.getSenderServerId());
        bo.insert();
        this.chatQueue.add(bo);
        FlowLogger.chatLog(bo.getChatType(), bo.getReceivePid(), bo.getSenderPid(), bo.getMessage(), bo.getSendTime(), bo.getSenderServerId());
    }

    public void broadcast() {
        ChatMessageBO bo = this.chatQueue.poll();
        if (bo != null) {
            this.lock();
            switch (ChatType.values()[bo.getChatType()]) {
                case CHATTYPE_AllWORlD:
                    this.worldChatAdd(bo);
                    this.broadcastToAllOnline(bo);
                default:
                    this.unlock();
            }
        }
    }

    public void broadcastToAllOnline(ChatMessageBO bo) {
        GameSessionMgr.getInstance().notifyAllServerMessage("player.WorldChat", this.parseProto(bo));
    }

    private void addChat(Queue<ChatMessageBO> list, ChatMessageBO chat) {
        if (list.size() >= this.getChatRecordCount()) {
            ChatMessageBO chatMessageBO = list.poll();
            chatMessageBO.del();
        }

        list.add(chat);
    }

    public void worldChatAdd(ChatMessageBO bo) {
        this.addChat(this.worldChat, bo);
    }

    public List<ChatMessage> loadMessage() {
        List<ChatMessageBO> worldChat = new ArrayList<>(this.worldChat);
        return worldChat.stream().sorted((o1, o2) -> o2.getSendTime() - o1.getSendTime()).limit(30L).map(this::parseProto).collect(Collectors.toList());
    }

    public ChatMessage parseProto(ChatMessageBO bo) {
        ChatMessage builder = new ChatMessage();
        builder.setType(ChatType.values()[bo.getChatType()]);
        builder.setSenderPid(bo.getSenderPid());
        builder.setSenderName(bo.getSenderName());
        builder.setSenderLv(bo.getSenderLv());
        builder.setSenderVipLv(bo.getSenderVipLv());
        builder.setSenderIcon(bo.getSenderIcon());
        builder.setSenderServerId(bo.getSenderServerId());
        builder.setMessage(bo.getMessage());
        builder.setContent(bo.getContent());
        builder.setSendTime(bo.getSendTime());
        builder.setReceivePid(bo.getReceivePid());
        builder.setReceiveName(bo.getReceiveName());
        builder.setMonthCard(bo.getMonthCard());
        builder.setYearCard(bo.getYearCard());
        builder.setIs_married(bo.getIsMarried());
        return builder;
    }
}
