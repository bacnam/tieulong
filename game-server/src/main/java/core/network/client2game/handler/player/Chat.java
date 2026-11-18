package core.network.client2game.handler.player;

import business.global.chat.ChatMgr;
import business.global.guild.Guild;
import business.player.Player;
import business.player.feature.PlayerBase;
import business.player.feature.achievement.AchievementFeature;
import business.player.feature.guild.GuildMemberFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ChatType;
import com.zhonglian.server.common.enums.UnlockType;
import com.zhonglian.server.common.enums.Achievement.AchievementType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefUnlockFunction;
import core.database.game.bo.ChatMessageBO;
import core.network.client2game.handler.PlayerHandler;
import core.network.game2world.WorldConnector;
import java.io.IOException;
import proto.gameworld.ChatMessage;

public class Chat extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Chat.Request req = new Gson().fromJson(message, Chat.Request.class);
        long toCId = req.toPid;
        String content = req.content;
        if (content.isEmpty()) {
            throw new WSException(ErrorCode.InvalidParam, "参数content=%s的内容不能为空", content);
        } else {
            int nowSecord = CommTime.nowSecond();
            int expiredTime = player.getPlayerBO().getBannedChatExpiredTime();
            if (expiredTime != -1 && expiredTime <= nowSecord) {
                ChatType chatType = req.type;
                if (WorldConnector.getInstance().isConnected() && chatType == ChatType.CHATTYPE_WORLD) {
                    chatType = ChatType.CHATTYPE_AllWORlD;
                }

                PlayerBase playerBase = player.getFeature(PlayerBase.class);
                switch (chatType) {
                    case CHATTYPE_WORLD:
                        int cd = RefDataMgr.getFactor("WorldChatInterval", 2);
                        playerBase = player.getFeature(PlayerBase.class);
                        int chatTime = playerBase.chatTime;
                        if (chatTime + cd > nowSecord) {
                            throw new WSException(ErrorCode.Chat_CdTime, "发言cd中，还剩%s秒才可以发言", cd - (nowSecord - chatTime));
                        }

                        playerBase.chatTime = nowSecord;
                        break;
                    case CHATTYPE_GUILD:
                        Guild guild = player.getFeature(GuildMemberFeature.class).getGuild();
                        if (guild == null) {
                            throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", player.getPid());
                        }
                    case CHATTYPE_COMPANY:
                        break;
                    case CHATTYPE_SYSTEM:
                    default:
                        throw new WSException(ErrorCode.InvalidParam, "非法的参数chatType=%s", chatType);
                    case CHATTYPE_AllWORlD:
                        int cdall = RefDataMgr.getFactor("AllWorldChatInterval", 5);
                        int chatTimeAll = playerBase.worldChatTime;
                        if (chatTimeAll + cdall > nowSecord) {
                            throw new WSException(ErrorCode.Chat_CdTime, "发言cd中，还剩%s秒才可以发言", cdall - (nowSecord - chatTimeAll));
                        }

                        playerBase.worldChatTime = nowSecord;
                        ChatMessageBO bo = new ChatMessageBO();
                        bo.setReceiveCId(toCId);
                        bo.setChatType(chatType.ordinal());
                        bo.setSenderCId(player.getPid());
                        bo.setContent(content);
                        bo.setSendTime(CommTime.nowSecond());
                        ChatMessage worldrequest = ChatMgr.getInstance().parseProto(bo);
                        WorldConnector.notifyMessage("player.Chat", worldrequest);
                        player.getFeature(AchievementFeature.class).updateInc(AchievementType.ChatTimes);
                        player.getFeature(AchievementFeature.class).updateInc(AchievementType.ChatTimes_M1);
                        player.getFeature(AchievementFeature.class).updateInc(AchievementType.ChatTimes_M2);
                        request.response();
                        return;
                    case CHATTYPE_GM:
                        RefUnlockFunction.checkUnlock(player, UnlockType.GMTalk);
                        int cdGM = RefDataMgr.getFactor("GMChatInterval", 1);
                        int chatTimeGM = playerBase.gmChatTime;
                        if (chatTimeGM + cdGM > nowSecord) {
                            throw new WSException(ErrorCode.Chat_CdTime, "发言cd中，还剩%s秒才可以发言", cdGM - (nowSecord - chatTimeGM));
                        }

                        playerBase.gmChatTime = nowSecord;
                }

                player.getFeature(AchievementFeature.class).updateInc(AchievementType.ChatTimes);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.ChatTimes_M1);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.ChatTimes_M2);
                ChatMgr.getInstance().addChat(player, content, chatType, toCId);
                request.response();
            } else {
                throw new WSException(ErrorCode.Chat_Banned, "禁言中，不能发言");
            }
        }
    }

    private class Request {
        long toPid;
        String content;
        ChatType type;
    }

    private class WorldRequest {
        long senderId;
        long toPid;
        String content;
        ChatType type;
    }
}
