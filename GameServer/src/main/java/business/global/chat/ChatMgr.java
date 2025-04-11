package business.global.chat;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTaskManager;
import BaseThread.BaseMutexObject;
import business.global.guild.Guild;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.features.PlayerRecord;
import business.player.feature.features.RechargeFeature;
import business.player.feature.guild.GuildMemberFeature;
import business.player.feature.marry.MarryFeature;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.zhonglian.server.common.Config;
import com.zhonglian.server.common.enums.Achievement;
import com.zhonglian.server.common.enums.ChatType;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.mgr.sensitive.SensitiveWordMgr;
import com.zhonglian.server.common.utils.Lists;
import com.zhonglian.server.http.client.IResponseHandler;
import com.zhonglian.server.http.server.GMParam;
import com.zhonglian.server.http.server.HttpUtils;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.response.ResponseHandler;
import com.zhonglian.server.websocket.handler.response.WebSocketResponse;
import core.config.refdata.RefDataMgr;
import core.database.game.bo.ChatMessageBO;
import core.network.game2world.WorldConnector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import proto.gameworld.ChatMessage;

public class ChatMgr {
    private static ChatMgr instance;
    protected final BaseMutexObject m_mutex;
    public final Queue<ChatMessageBO> worldChat;
    public final Map<Long, Queue<ChatMessageBO>> guildChat;
    public final Map<Long, Queue<ChatMessageBO>> companyChat;
    public final Map<Long, Queue<ChatMessageBO>> GMChat;
    public final LinkedList<ChatMessageBO> systemChat;
    public final Queue<ChatMessageBO> chatQueue;
    public final Queue<ChatMessage> allWorldChat;
    private int chatRecordCount;
    private int systemRecordCount;

    public static ChatMgr getInstance() {
        if (instance == null) {
            instance = new ChatMgr();
        }
        return instance;
    }

    public ChatMgr() {
        this.m_mutex = new BaseMutexObject();

        this.worldChat = new LinkedList<>();

        this.guildChat = Maps.newConcurrentMap();

        this.companyChat = Maps.newConcurrentMap();

        this.GMChat = Maps.newConcurrentMap();

        this.systemChat = new LinkedList<>();

        this.chatQueue = new LinkedList<>();

        this.allWorldChat = new LinkedList<>();
        loadChatMessages();
        broadCastWorldChat();
        this.chatRecordCount = RefDataMgr.getFactor("ChatRecordCount", 30);
        this.systemRecordCount = RefDataMgr.getFactor("ChatSystemRecordCount", 10);
    }

    public void init() {
        WorldConnector.request("player.LoadChatMessage", new Object(), new ResponseHandler() {
                    public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException {
                        CommLog.info("读取跨服聊天成功");
                        List<ChatMessage> req = (List<ChatMessage>) (new Gson()).fromJson(body, (new TypeToken<List<ChatMessage>>() {
                        }
                        ).getType());
                        (ChatMgr.getInstance()).allWorldChat.addAll(req);
                    }

                    public void handleError(WebSocketResponse ssresponse, short statusCode, String message) {
                        CommLog.error("读取跨服聊天失败");
                    }
                }
        );
    }

    public boolean loadChatMessages() {
        List<ChatMessageBO> all = BM.getBM(ChatMessageBO.class).findAll();
        Collections.sort(all, (o1, o2) -> o1.getSendTime() - o2.getSendTime());

        for (ChatMessageBO bo : all) {
            Player player;
            long guildId;
            Queue<ChatMessageBO> guildchat;
            Queue<ChatMessageBO> sender;
            Queue<ChatMessageBO> company;
            Queue<ChatMessageBO> senderGM;
            Queue<ChatMessageBO> companyGM;
            switch (ChatType.values()[bo.getChatType()]) {
                case CHATTYPE_WORLD:
                    this.worldChat.add(bo);

                case CHATTYPE_GUILD:
                    player = PlayerMgr.getInstance().getPlayer(bo.getSenderCId());
                    guildId = ((GuildMemberFeature) player.getFeature(GuildMemberFeature.class)).getGuildID();
                    guildchat = this.guildChat.get(Long.valueOf(guildId));
                    if (guildchat == null) {
                        guildchat = Lists.newLinkedList();
                        this.guildChat.put(Long.valueOf(guildId), guildchat);
                    }
                    guildchat.add(bo);

                case CHATTYPE_COMPANY:
                    sender = this.companyChat.get(Long.valueOf(bo.getSenderCId()));
                    if (sender == null) {
                        sender = Lists.newLinkedList();
                        this.companyChat.put(Long.valueOf(bo.getSenderCId()), sender);
                    }
                    sender.add(bo);

                    company = this.companyChat.get(Long.valueOf(bo.getReceiveCId()));
                    if (company == null) {
                        company = Lists.newLinkedList();
                        this.companyChat.put(Long.valueOf(bo.getReceiveCId()), company);
                    }
                    company.add(bo);

                case CHATTYPE_SYSTEM:
                    this.systemChat.push(bo);

                case CHATTYPE_GM:
                    senderGM = this.companyChat.get(Long.valueOf(bo.getSenderCId()));
                    if (senderGM == null) {
                        senderGM = Lists.newLinkedList();
                        this.companyChat.put(Long.valueOf(bo.getSenderCId()), senderGM);
                    }
                    senderGM.add(bo);

                    companyGM = this.companyChat.get(Long.valueOf(bo.getReceiveCId()));
                    if (companyGM == null) {
                        companyGM = Lists.newLinkedList();
                        this.companyChat.put(Long.valueOf(bo.getReceiveCId()), companyGM);
                    }
                    companyGM.add(bo);
            }

        }
        return true;
    }

    public void lock() {
        this.m_mutex.lock();
    }

    public void unlock() {
        this.m_mutex.unlock();
    }

    public boolean broadCastWorldChat() {
        SyncTaskManager.schedule(100, () -> {
            broadcast();
            return true;
        });
        return true;
    }

    public int getChatRecordCount() {
        return this.chatRecordCount;
    }

    public int getSystemRecordCount() {
        return this.systemRecordCount;
    }

    public void addChat(Player sender, String content, ChatType type, long toCId) throws WSException {
        ChatMessageBO bo = new ChatMessageBO();
        bo.setReceiveCId(toCId);
        bo.setChatType(type.ordinal());
        if (sender != null)
            bo.setSenderCId(sender.getPid());
        bo.setContent(content);
        bo.setSendTime(CommTime.nowSecond());
        bo.insert();
        this.chatQueue.add(bo);
    }

    public void broadcast() {
        Player player;
        Guild guild;
        Player sender, sender1;
        ChatMessageBO bo = this.chatQueue.poll();
        if (bo == null) {
            return;
        }

        lock();
        switch (ChatType.values()[bo.getChatType()]) {
            case CHATTYPE_WORLD:
                worldChatAdd(bo);
                broadcastToAllOnline(bo);
                break;
            case CHATTYPE_SYSTEM:
                SystemChatAdd(bo);
                broadcastToAllOnline(bo);
                break;
            case CHATTYPE_GUILD:
                player = PlayerMgr.getInstance().getOnlinePlayerByCid(bo.getSenderCId());
                guild = ((GuildMemberFeature) player.getFeature(GuildMemberFeature.class)).getGuild();
                if (guild != null) {
                    guildChatAdd(guild.getGuildId(), bo);
                    guild.broadcast("chat", parseProto(bo));
                }
                break;
            case CHATTYPE_COMPANY:
                player = PlayerMgr.getInstance().getOnlinePlayerByCid(bo.getReceiveCId());

                if (player != null) {
                    player.pushProto("chat", parseProto(bo));
                }
                sender = PlayerMgr.getInstance().getPlayer(bo.getSenderCId());
                sender.pushProto("chat", parseProto(bo));
                companyChatAdd(bo);
                break;
            case CHATTYPE_GM:
                player = PlayerMgr.getInstance().getOnlinePlayerByCid(bo.getReceiveCId());
                sender1 = PlayerMgr.getInstance().getPlayer(bo.getSenderCId());

                if (player != null) {
                    player.pushProto("chat", parseProto(bo));
                }
                if (sender1 != null) {
                    sender1.pushProto("chat", parseProto(bo));

                    GMParam params = new GMParam();
                    params.put("senderId", Long.valueOf(sender1.getPid()));
                    params.put("content", bo.getContent());
                    params.put("sendTime", Integer.valueOf(bo.getSendTime()));
                    params.put("senderName", sender1.getName());
                    params.put("vip", Integer.valueOf(sender1.getVipLevel()));
                    params.put("totalRecharge", Integer.valueOf(sender1.getPlayerBO().getTotalRecharge() / 10));
                    params.put("todayRecharge", Integer.valueOf(((PlayerRecord) sender1.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.DailyRecharge)));
                    params.put("serverId", Integer.valueOf(Config.ServerID()));
                    params.put("worldId", Integer.getInteger("world_sid", 0));


                    String baseurl = System.getProperty("downConfUrl");
                    HttpUtils.RequestGM(baseurl + "/yourPath", params, new IResponseHandler() {
                        @Override
                        public void compeleted(String response) {
                            try {
                                JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                                if (json.get("state").getAsInt() != 1000) {
                                    CommLog.error("发送GM用户信息失败" + json.get("state").getAsInt());
                                }
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }

                        @Override
                        public void failed(Exception exception) {
                            CommLog.error("发送GM用户信息失败", exception);
                        }
                    });

                }
                GMChatAdd(bo);
                break;
        }

        unlock();
    }

    public void broadcastToAllOnline(ChatMessageBO bo) {
        for (Player p : PlayerMgr.getInstance().getOnlinePlayers()) {
            p.pushProto("chat", parseProto(bo));
        }
    }

    public void SystemChatAdd(ChatMessageBO bo) {
        if (this.systemChat.size() >= getSystemRecordCount()) {
            ChatMessageBO chatMessageBO = this.systemChat.poll();
            chatMessageBO.del();
        }
        this.systemChat.add(bo);
    }

    private void addChat(Queue<ChatMessageBO> list, ChatMessageBO chat) {
        if (list.size() >= getChatRecordCount()) {
            ChatMessageBO chatMessageBO = list.poll();
            chatMessageBO.del();
        }
        list.add(chat);
    }

    public void addAllWorldChat(ChatMessage chat) {
        if (this.allWorldChat.size() >= RefDataMgr.getFactor("AllWorldChatRecordCount", 30)) {
            this.allWorldChat.poll();
        }
        this.allWorldChat.add(chat);
    }

    public void worldChatAdd(ChatMessageBO bo) {
        addChat(this.worldChat, bo);
    }

    public void guildChatAdd(long guildId, ChatMessageBO bo) {
        Queue<ChatMessageBO> guildchat = this.guildChat.get(Long.valueOf(guildId));
        if (guildchat == null) {
            guildchat = Lists.newLinkedList();
            this.guildChat.put(Long.valueOf(guildId), guildchat);
        }
        addChat(guildchat, bo);
    }

    public void companyChatAdd(ChatMessageBO bo) {
        Queue<ChatMessageBO> receiveChat = this.companyChat.get(Long.valueOf(bo.getReceiveCId()));
        if (receiveChat == null) {
            receiveChat = Lists.newLinkedList();
            this.companyChat.put(Long.valueOf(bo.getReceiveCId()), receiveChat);
        }
        if (receiveChat.size() >= getChatRecordCount()) {
            ChatMessageBO chatMessageBO = receiveChat.poll();
            tryDel(chatMessageBO);
        }
        receiveChat.add(bo);

        Queue<ChatMessageBO> sendChat = this.companyChat.get(Long.valueOf(bo.getSenderCId()));
        if (sendChat == null) {
            sendChat = Lists.newLinkedList();
            this.companyChat.put(Long.valueOf(bo.getSenderCId()), sendChat);
        }
        if (sendChat.size() >= getChatRecordCount()) {
            ChatMessageBO chatMessageBO = sendChat.poll();
            tryDel(chatMessageBO);
        }
        sendChat.add(bo);
    }

    public void GMChatAdd(ChatMessageBO bo) {
        Queue<ChatMessageBO> receiveChat = this.GMChat.get(Long.valueOf(bo.getReceiveCId()));
        if (receiveChat == null) {
            receiveChat = Lists.newLinkedList();
            this.GMChat.put(Long.valueOf(bo.getReceiveCId()), receiveChat);
        }
        if (receiveChat.size() >= getChatRecordCount()) {
            ChatMessageBO chatMessageBO = receiveChat.poll();
            tryDelGM(chatMessageBO);
        }
        receiveChat.add(bo);

        Queue<ChatMessageBO> sendChat = this.GMChat.get(Long.valueOf(bo.getSenderCId()));
        if (sendChat == null) {
            sendChat = Lists.newLinkedList();
            this.GMChat.put(Long.valueOf(bo.getSenderCId()), sendChat);
        }
        if (sendChat.size() >= getChatRecordCount()) {
            ChatMessageBO chatMessageBO = sendChat.poll();
            tryDelGM(chatMessageBO);
        }
        sendChat.add(bo);
    }

    private void tryDel(ChatMessageBO bo) {
        Queue<ChatMessageBO> sender = this.companyChat.get(Long.valueOf(bo.getSenderCId()));
        Queue<ChatMessageBO> receiver = this.companyChat.get(Long.valueOf(bo.getReceiveCId()));
        if ((sender == null || !sender.contains(bo)) && (receiver == null || !receiver.contains(bo))) {
            bo.del();
        }
    }

    private void tryDelGM(ChatMessageBO bo) {
        Queue<ChatMessageBO> sender = this.GMChat.get(Long.valueOf(bo.getSenderCId()));
        Queue<ChatMessageBO> receiver = this.GMChat.get(Long.valueOf(bo.getReceiveCId()));
        if ((sender == null || !sender.contains(bo)) && (receiver == null || !receiver.contains(bo))) {
            bo.del();
        }
    }

    public List<ChatMessage> loadMessage(Player player) {
        List<ChatMessageBO> worldChat = new ArrayList<>(this.worldChat);

        Guild guild = ((GuildMemberFeature) player.getFeature(GuildMemberFeature.class)).getGuild();
        if (guild != null) {
            Queue<ChatMessageBO> guildChat = this.guildChat.get(Long.valueOf(guild.getGuildId()));
            if (guildChat != null) {
                worldChat.addAll(guildChat);
            }
        }

        if (this.companyChat.get(Long.valueOf(player.getPid())) != null) {
            worldChat.addAll(this.companyChat.get(Long.valueOf(player.getPid())));
        }

        if (this.GMChat.get(Long.valueOf(player.getPid())) != null) {
            worldChat.addAll(this.GMChat.get(Long.valueOf(player.getPid())));
        }

        worldChat.addAll(this.systemChat);
        List<ChatMessage> list = (List<ChatMessage>) worldChat.stream().sorted((o1, o2) -> o2.getSendTime() - o1.getSendTime())

                .limit(30L).map(this::parseProto).collect(Collectors.toList());

        list.addAll(this.allWorldChat);

        return list;
    }

    public void cleanCompanyMessage(Player player) {
        if (this.companyChat.get(Long.valueOf(player.getPid())) == null) {
            return;
        }
        lock();
        try {
            Queue<ChatMessageBO> list = this.companyChat.get(Long.valueOf(player.getPid()));
            for (ChatMessageBO bo : list) {
                bo.del();
            }
            this.companyChat.remove(Long.valueOf(player.getPid()));
        } finally {
            unlock();
        }
    }

    public void cleanGuildMessage(long guildId) {
        Queue<ChatMessageBO> guildChat = this.guildChat.remove(Long.valueOf(guildId));
        if (guildChat == null) {
            return;
        }
        for (ChatMessageBO chat : guildChat) {
            chat.del();
        }
        guildChat.clear();
    }

    public ChatMessage parseProto(ChatMessageBO bo) {
        ChatMessage builder = new ChatMessage();
        builder.setId(bo.getId());

        ChatType chatType = ChatType.values()[bo.getChatType()];
        builder.setType(chatType);
        builder.setSenderPid(bo.getSenderCId());

        Player player = PlayerMgr.getInstance().getPlayer(bo.getSenderCId());
        if (player != null) {
            builder.setSenderName(player.getPlayerBO().getName());
            builder.setSenderLv(player.getPlayerBO().getLv());
            builder.setSenderVipLv(player.getPlayerBO().getVipLevel());
            builder.setSenderIcon(player.getPlayerBO().getIcon());
            builder.setSenderServerId(player.getSid());

            builder.setIs_married(((MarryFeature) player.getFeature(MarryFeature.class)).isMarried());

            RechargeFeature rechargeFeature = (RechargeFeature) player.getFeature(RechargeFeature.class);
            int monthNum = rechargeFeature.getRebateRemains(Achievement.AchievementType.MonthCardCrystal);
            builder.setMonthCard((monthNum > 0));
            int yearNum = rechargeFeature.getRebateRemains(Achievement.AchievementType.YearCardCrystal);
            builder.setYearCard((yearNum == -1));
        } else {
            builder.setSenderName("GM");
        }
        String message = SensitiveWordMgr.getInstance().replaceSensitiveWord(bo.getContent(), 1, "*");
        builder.setMessage(message);
        builder.setContent(bo.getContent());
        builder.setSendTime(bo.getSendTime());
        builder.setReceivePid(bo.getReceiveCId());
        Player receive = PlayerMgr.getInstance().getPlayer(bo.getReceiveCId());
        if (receive != null) {
            builder.setReceiveName(receive.getName());
        }

        return builder;
    }
}

