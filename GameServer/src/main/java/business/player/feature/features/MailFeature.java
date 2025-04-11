package business.player.feature.features;

import business.global.gmmail.MailCenter;
import business.player.Player;
import business.player.feature.Feature;
import business.player.feature.PlayerCurrency;
import business.player.feature.PlayerItem;
import business.player.item.Reward;
import com.zhonglian.server.common.utils.CommString;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Lists;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.database.game.bo.GlobalMailBO;
import core.database.game.bo.PlayerMailBO;
import core.network.proto.MailInfo;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MailFeature
        extends Feature {
    public final Map<Long, PlayerMailBO> mailMap;

    public MailFeature(Player owner) {
        super(owner);

        this.mailMap = new ConcurrentHashMap<>();
    }

    public void loadDB() {
        this.mailMap.clear();
        MailCenter logic = MailCenter.getInstance();
        List<PlayerMailBO> boList = logic.loadPrivateMail(this.player.getPid());
        for (PlayerMailBO bo : boList) {
            this.mailMap.put(Long.valueOf(bo.getId()), bo);
        }
        logic.delPrivateMail(this.player.getPid());
    }

    public void onConnect() {
        achieveGlobalMail();
    }

    public void achieveGlobalMail() {
        synchronized (this) {

            int curTime = CommTime.nowSecond();

            int playerCreateTime = this.player.getPlayerBO().getCreateTime();

            long oldCheckId = this.player.getPlayerBO().getGmMailCheckId();

            long newCheckId = oldCheckId;

            List<GlobalMailBO> gms = MailCenter.getInstance().getGlobalMailList();

            for (int index = gms.size() - 1; index >= 0; index--) {
                GlobalMailBO bo = gms.get(index);

                if (bo.getId() <= oldCheckId) {
                    break;
                }

                if (bo.getCreateTime() >= playerCreateTime) {

                    if (bo.getCreateTime() + bo.getExistTime() >= curTime) {

                        PlayerMailBO pmail = new PlayerMailBO();
                        pmail.setPid(this.player.getPid());
                        pmail.setSenderName(bo.getSenderName());
                        pmail.setTitle(bo.getTitle());
                        pmail.setContent(bo.getContent());
                        pmail.setCreateTime(bo.getCreateTime());
                        pmail.setExistTime(bo.getExistTime());
                        pmail.setPickUpExistTime(bo.getPickUpExistTime());
                        pmail.setUniformIDList(bo.getUniformIDList());
                        pmail.setUniformCountList(bo.getUniformCountList());
                        pmail.setGlobalMailID(bo.getId());

                        pmail.insert();

                        addMail(pmail);

                        newCheckId = Math.max(newCheckId, pmail.getGlobalMailID());
                    }
                }
            }
            if (oldCheckId != newCheckId) {
                ((PlayerCurrency) this.player.getFeature(PlayerCurrency.class)).updateMaxGlobalMailID(newCheckId);
            }
        }
    }

    private int getExistTime(PlayerMailBO bo) {
        if (bo.getPickUpTime() == 0) {
            return bo.getCreateTime() + bo.getExistTime();
        }
        return bo.getPickUpTime() + bo.getPickUpExistTime();
    }

    public void addMail(PlayerMailBO bo) {
        this.mailMap.put(Long.valueOf(bo.getId()), bo);
        this.player.pushProto("addMail", parseToProto(bo));
    }

    public void removeMail(PlayerMailBO bo) {
        this.mailMap.remove(Long.valueOf(bo.getId()));
        bo.del();
    }

    public List<MailInfo> cmd_getMailList() {
        List<PlayerMailBO> mailList = Lists.newArrayList();
        List<PlayerMailBO> overdueMailList = Lists.newArrayList();

        int curTime = CommTime.nowSecond();

        for (PlayerMailBO bo : this.mailMap.values()) {
            int maxExistTime = getExistTime(bo);
            if (maxExistTime < curTime) {
                overdueMailList.add(bo);
                continue;
            }
            mailList.add(bo);
        }

        Collections.sort(mailList, (o1, o2) -> {
            if (o2.getPickUpTime() == 0 && o1.getPickUpTime() != 0) {
                return 1;
            }
            if (o2.getPickUpTime() != 0 && o1.getPickUpTime() == 0) {
                return -1;
            }
            int o1Max = getExistTime(o1);
            int o2Max = getExistTime(o2);
            return o1Max - o2Max;
        });
        int MaxMailCount = RefDataMgr.getFactor("MaxMailCount", 50);
        if (mailList.size() >= MaxMailCount) {
            mailList = mailList.subList(0, MaxMailCount);
        }

        for (PlayerMailBO bo : overdueMailList) {
            removeMail(bo);
        }

        List<MailInfo> protoList = Lists.newArrayList();
        for (PlayerMailBO bo : mailList) {
            protoList.add(parseToProto(bo));
        }
        return protoList;
    }

    public void pickUpMail(long mailId, WebSocketRequest request) {
        PlayerMailBO PlayerMailBO = this.mailMap.get(Long.valueOf(mailId));
        if (PlayerMailBO == null) {
            request.error(ErrorCode.InvalidParam, "没有找到邮件，邮件id:" + mailId, new Object[0]);
            return;
        }
        if (PlayerMailBO.getPickUpTime() != 0) {
            request.error(ErrorCode.InvalidParam, "邮件已领取，邮件id:%s,领取时间%s", new Object[]{Long.valueOf(mailId), Integer.valueOf(PlayerMailBO.getPickUpTime())});

            return;
        }
        request.response(pickUp(mailId));
    }

    public void pickUpAllReward(WebSocketRequest request) {
        if (this.mailMap.isEmpty()) {
            request.error(ErrorCode.InvalidParam, "邮箱里没有邮件", new Object[0]);
            return;
        }
        List<MailInfo> protoList = Lists.newArrayList();
        for (PlayerMailBO bo : this.mailMap.values()) {

            if (bo.getPickUpTime() != 0) {
                continue;
            }
            protoList.add(pickUp(bo.getId()));
        }
        request.response(protoList);
    }

    private MailInfo pickUp(long mailId) {
        PlayerMailBO bo = this.mailMap.get(Long.valueOf(mailId));
        MailInfo mailInfo = new MailInfo(bo);

        List<Integer> idList = CommString.getIntegerList(bo.getUniformIDList(), ";");
        List<Integer> countList = CommString.getIntegerList(bo.getUniformCountList(), ";");

        Reward reward = null;
        if (idList.size() != 0) {
            reward = new Reward(idList, countList);
        }
        if (reward != null) {
            ((PlayerItem) this.player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.Mail);
        }

        if (bo.getPickUpExistTime() == 0) {
            removeMail(bo);
            mailInfo.setLeftTime(0);
        } else {
            int nowSecond = CommTime.nowSecond();
            bo.savePickUpTime(nowSecond);
            int leftTime = getExistTime(bo) - nowSecond;
            mailInfo.setPickUpTime(nowSecond);
            mailInfo.setLeftTime(Math.max(0, leftTime));
        }

        return mailInfo;
    }

    public MailInfo parseToProto(PlayerMailBO bo) {
        MailInfo mailInfo = new MailInfo(bo);
        int leftTime = getExistTime(bo) - CommTime.nowSecond();
        mailInfo.setLeftTime(leftTime);

        return mailInfo;
    }
}

