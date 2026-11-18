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
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.ChatType;
import com.zhonglian.server.common.enums.Achievement.AchievementType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.common.mgr.sensitive.SensitiveWordMgr;
import com.zhonglian.server.common.utils.CommTime;
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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import proto.gameworld.ChatMessage;

public class ChatMgr {
    private static ChatMgr instance;
    protected final BaseMutexObject m_mutex = new BaseMutexObject();
    public final Queue<ChatMessageBO> worldChat = new LinkedList<>();
    public final Map<Long, Queue<ChatMessageBO>> guildChat = Maps.newConcurrentMap();
    public final Map<Long, Queue<ChatMessageBO>> companyChat = Maps.newConcurrentMap();
    public final Map<Long, Queue<ChatMessageBO>> GMChat = Maps.newConcurrentMap();
    public final LinkedList<ChatMessageBO> systemChat = new LinkedList<>();
    public final Queue<ChatMessageBO> chatQueue = new LinkedList<>();
    public final Queue<ChatMessage> allWorldChat = new LinkedList<>();
    private int chatRecordCount;
    private int systemRecordCount;

    public static ChatMgr getInstance() {
        if (instance == null) {
            instance = new ChatMgr();
        }

        return instance;
    }

    public ChatMgr() {
        this.loadChatMessages();
        this.broadCastWorldChat();
        this.chatRecordCount = RefDataMgr.getFactor("ChatRecordCount", 30);
        this.systemRecordCount = RefDataMgr.getFactor("ChatSystemRecordCount", 10);
    }

    public void init() {
        WorldConnector.request("player.LoadChatMessage", new Object(), new ResponseHandler() {
            @Override
            public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException {
                CommLog.info("读取跨服聊天成功");
                List<ChatMessage> req = new Gson().fromJson(body, (new TypeToken<List<ChatMessage>>() {}).getType());
                ChatMgr.getInstance().allWorldChat.addAll(req);
            }

            @Override
            public void handleError(WebSocketResponse ssresponse, short statusCode, String message) {
                CommLog.error("读取跨服聊天失败");
            }
        });
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
                case CHATTYPE_WORLD:
                    this.worldChat.add(bo);
                    break;
                case CHATTYPE_GUILD:
                    Player player = PlayerMgr.getInstance().getPlayer(bo.getSenderCId());
                    long guildId = player.getFeature(GuildMemberFeature.class).getGuildID();
                    Queue<ChatMessageBO> guildchat = this.guildChat.get(guildId);
                    if (guildchat == null) {
                        guildchat = Lists.newLinkedList();
                        this.guildChat.put(guildId, guildchat);
                    }

                    guildchat.add(bo);
                    break;
                case CHATTYPE_COMPANY:
                    Queue<ChatMessageBO> sender = this.companyChat.get(bo.getSenderCId());
                    if (sender == null) {
                        sender = Lists.newLinkedList();
                        this.companyChat.put(bo.getSenderCId(), sender);
                    }

                    sender.add(bo);
                    Queue<ChatMessageBO> company = this.companyChat.get(bo.getReceiveCId());
                    if (company == null) {
                        company = Lists.newLinkedList();
                        this.companyChat.put(bo.getReceiveCId(), company);
                    }

                    company.add(bo);
                    break;
                case CHATTYPE_SYSTEM:
                    this.systemChat.push(bo);
                case CHATTYPE_AllWORlD:
                default:
                    break;
                case CHATTYPE_GM:
                    Queue<ChatMessageBO> senderGM = this.companyChat.get(bo.getSenderCId());
                    if (senderGM == null) {
                        senderGM = Lists.newLinkedList();
                        this.companyChat.put(bo.getSenderCId(), senderGM);
                    }

                    senderGM.add(bo);
                    Queue<ChatMessageBO> companyGM = this.companyChat.get(bo.getReceiveCId());
                    if (companyGM == null) {
                        companyGM = Lists.newLinkedList();
                        this.companyChat.put(bo.getReceiveCId(), companyGM);
                    }

                    companyGM.add(bo);
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

    public int getSystemRecordCount() {
        return this.systemRecordCount;
    }

    public void addChat(Player sender, String content, ChatType type, long toCId) throws WSException {
        ChatMessageBO bo = new ChatMessageBO();
        bo.setReceiveCId(toCId);
        bo.setChatType(type.ordinal());
        if (sender != null) {
            bo.setSenderCId(sender.getPid());
        }

        bo.setContent(content);
        bo.setSendTime(CommTime.nowSecond());
        bo.insert();
        this.chatQueue.add(bo);
    }

    public void broadcast() {
        ChatMessageBO bo = this.chatQueue.poll();
        if (bo != null) {
            this.lock();
            switch (ChatType.values()[bo.getChatType()]) {
                case CHATTYPE_WORLD:
                    this.worldChatAdd(bo);
                    this.broadcastToAllOnline(bo);
                    break;
                case CHATTYPE_GUILD:
                    Player playerx = PlayerMgr.getInstance().getOnlinePlayerByCid(bo.getSenderCId());
                    Guild guild = playerx.getFeature(GuildMemberFeature.class).getGuild();
                    if (guild != null) {
                        this.guildChatAdd(guild.getGuildId(), bo);
                        guild.broadcast("chat", this.parseProto(bo));
                    }
                    break;
                case CHATTYPE_COMPANY:
                    Player receiver = PlayerMgr.getInstance().getOnlinePlayerByCid(bo.getReceiveCId());
                    if (receiver != null) {
                        receiver.pushProto("chat", this.parseProto(bo));
                    }

                    Player sender = PlayerMgr.getInstance().getPlayer(bo.getSenderCId());
                    sender.pushProto("chat", this.parseProto(bo));
                    this.companyChatAdd(bo);
                    break;
                case CHATTYPE_SYSTEM:
                    this.SystemChatAdd(bo);
                    this.broadcastToAllOnline(bo);
                case CHATTYPE_AllWORlD:
                default:
                    break;
                case CHATTYPE_GM:
                    Player player = PlayerMgr.getInstance().getOnlinePlayerByCid(bo.getReceiveCId());
                    Player sender1 = PlayerMgr.getInstance().getPlayer(bo.getSenderCId());
                    if (player != null) {
                        player.pushProto("chat", this.parseProto(bo));
                    }

                    if (sender1 != null) {
                        sender1.pushProto("chat", this.parseProto(bo));
                        GMParam params = new GMParam();
                        params.put("senderId", sender1.getPid());
                        params.put("content", bo.getContent());
                        params.put("sendTime", bo.getSendTime());
                        params.put("senderName", sender1.getName());
                        params.put("vip", sender1.getVipLevel());
                        params.put("totalRecharge", sender1.getPlayerBO().getTotalRecharge() / 10);
                        params.put("todayRecharge", sender1.getFeature(PlayerRecord.class).getValue(DailyRefresh.DailyRecharge));
                        params.put("serverId", Config.ServerID());
                        params.put("worldId", Integer.getInteger("world_sid", 0));
                        String baseurl = System.getProperty("downConfUrl");
                        HttpUtils.RequestGM("http://" + baseurl + "/gm/chat!add", params, new IResponseHandler() {
                            @Override
                            public void compeleted(String response) {
                                try {
                                    JsonObject json = new JsonParser().parse(response).getAsJsonObject();
                                    if (json.get("state").getAsInt() != 1000) {
                                        CommLog.error("发送GM用户信息失败" + json.get("state").getAsInt());
                                    }
                                } catch (Exception var3) {
                                }
                            }

                            @Override
                            public void failed(Exception exception) {
                                CommLog.error("发送GM用户信息失败");
                            }
                        });
                    }

                    this.GMChatAdd(bo);
            }

            this.unlock();
        }
    }

    public void broadcastToAllOnline(ChatMessageBO bo) {
        for (Player p : PlayerMgr.getInstance().getOnlinePlayers()) {
            p.pushProto("chat", this.parseProto(bo));
        }
    }

    public void SystemChatAdd(ChatMessageBO bo) {
        if (this.systemChat.size() >= this.getSystemRecordCount()) {
            ChatMessageBO chatMessageBO = this.systemChat.poll();
            chatMessageBO.del();
        }

        this.systemChat.add(bo);
    }

    private void addChat(Queue<ChatMessageBO> list, ChatMessageBO chat) {
        if (list.size() >= this.getChatRecordCount()) {
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
        this.addChat(this.worldChat, bo);
    }

    public void guildChatAdd(long guildId, ChatMessageBO bo) {
        Queue<ChatMessageBO> guildchat = this.guildChat.get(guildId);
        if (guildchat == null) {
            guildchat = Lists.newLinkedList();
            this.guildChat.put(guildId, guildchat);
        }

        this.addChat(guildchat, bo);
    }

    public void companyChatAdd(ChatMessageBO bo) {
        Queue<ChatMessageBO> receiveChat = this.companyChat.get(bo.getReceiveCId());
        if (receiveChat == null) {
            receiveChat = Lists.newLinkedList();
            this.companyChat.put(bo.getReceiveCId(), receiveChat);
        }

        if (receiveChat.size() >= this.getChatRecordCount()) {
            ChatMessageBO chatMessageBO = receiveChat.poll();
            this.tryDel(chatMessageBO);
        }

        receiveChat.add(bo);
        Queue<ChatMessageBO> sendChat = this.companyChat.get(bo.getSenderCId());
        if (sendChat == null) {
            sendChat = Lists.newLinkedList();
            this.companyChat.put(bo.getSenderCId(), sendChat);
        }

        if (sendChat.size() >= this.getChatRecordCount()) {
            ChatMessageBO chatMessageBO = sendChat.poll();
            this.tryDel(chatMessageBO);
        }

        sendChat.add(bo);
    }

    public void GMChatAdd(ChatMessageBO bo) {
        Queue<ChatMessageBO> receiveChat = this.GMChat.get(bo.getReceiveCId());
        if (receiveChat == null) {
            receiveChat = Lists.newLinkedList();
            this.GMChat.put(bo.getReceiveCId(), receiveChat);
        }

        if (receiveChat.size() >= this.getChatRecordCount()) {
            ChatMessageBO chatMessageBO = receiveChat.poll();
            this.tryDelGM(chatMessageBO);
        }

        receiveChat.add(bo);
        Queue<ChatMessageBO> sendChat = this.GMChat.get(bo.getSenderCId());
        if (sendChat == null) {
            sendChat = Lists.newLinkedList();
            this.GMChat.put(bo.getSenderCId(), sendChat);
        }

        if (sendChat.size() >= this.getChatRecordCount()) {
            ChatMessageBO chatMessageBO = sendChat.poll();
            this.tryDelGM(chatMessageBO);
        }

        sendChat.add(bo);
    }

    private void tryDel(ChatMessageBO bo) {
        Queue<ChatMessageBO> sender = this.companyChat.get(bo.getSenderCId());
        Queue<ChatMessageBO> receiver = this.companyChat.get(bo.getReceiveCId());
        if ((sender == null || !sender.contains(bo)) && (receiver == null || !receiver.contains(bo))) {
            bo.del();
        }
    }

    private void tryDelGM(ChatMessageBO bo) {
        Queue<ChatMessageBO> sender = this.GMChat.get(bo.getSenderCId());
        Queue<ChatMessageBO> receiver = this.GMChat.get(bo.getReceiveCId());
        if ((sender == null || !sender.contains(bo)) && (receiver == null || !receiver.contains(bo))) {
            bo.del();
        }
    }

    public List<ChatMessage> loadMessage(Player player) {
        List<ChatMessageBO> worldChat = new ArrayList<>(this.worldChat);
        Guild guild = player.getFeature(GuildMemberFeature.class).getGuild();
        if (guild != null) {
            Queue<ChatMessageBO> guildChat = this.guildChat.get(guild.getGuildId());
            if (guildChat != null) {
                worldChat.addAll(guildChat);
            }
        }

        if (this.companyChat.get(player.getPid()) != null) {
            worldChat.addAll(this.companyChat.get(player.getPid()));
        }

        if (this.GMChat.get(player.getPid()) != null) {
            worldChat.addAll(this.GMChat.get(player.getPid()));
        }

        worldChat.addAll(this.systemChat);
        List<ChatMessage> list = worldChat.stream()
            .sorted((o1, o2) -> o2.getSendTime() - o1.getSendTime())
            .limit(30L)
            .map(this::parseProto)
            .collect(Collectors.toList());
        list.addAll(this.allWorldChat);
        return list;
    }

    public void cleanCompanyMessage(Player player) {
        if (this.companyChat.get(player.getPid()) != null) {
            this.lock();

            try {
                for (ChatMessageBO bo : this.companyChat.get(player.getPid())) {
                    bo.del();
                }

                this.companyChat.remove(player.getPid());
            } finally {
                this.unlock();
            }
        }
    }

    public void cleanGuildMessage(long guildId) {
        Queue<ChatMessageBO> guildChat = this.guildChat.remove(guildId);
        if (guildChat != null) {
            for (ChatMessageBO chat : guildChat) {
                chat.del();
            }

            guildChat.clear();
        }
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
            builder.setIs_married(player.getFeature(MarryFeature.class).isMarried());
            RechargeFeature rechargeFeature = player.getFeature(RechargeFeature.class);
            int monthNum = rechargeFeature.getRebateRemains(AchievementType.MonthCardCrystal);
            builder.setMonthCard(monthNum > 0);
            int yearNum = rechargeFeature.getRebateRemains(AchievementType.YearCardCrystal);
            builder.setYearCard(yearNum == -1);
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
