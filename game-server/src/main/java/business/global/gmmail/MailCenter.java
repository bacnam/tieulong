package business.global.gmmail;

import BaseTask.SyncTask.SyncTaskManager;
import BaseThread.BaseMutexObject;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.RobotManager;
import business.player.feature.features.MailFeature;
import business.player.item.Reward;
import business.player.item.UniformItem;
import com.google.common.collect.Lists;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.StringUtils;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefMail;
import core.config.refdata.ref.RefReward;
import core.database.game.bo.GlobalMailBO;
import core.database.game.bo.PlayerMailBO;
import core.logger.flow.FlowLogger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MailCenter {
    private static MailCenter instance = new MailCenter();
    public List<GlobalMailBO> allBOs = Lists.newArrayList();
    public HashMap<Long, List<PlayerMailBO>> offLineMailCached = new HashMap<>();
    protected BaseMutexObject m_mutex = new BaseMutexObject();

    public static MailCenter getInstance() {
        return instance;
    }

    public void lock() {
        this.m_mutex.lock();
    }

    public void unlock() {
        this.m_mutex.unlock();
    }

    public void init() {
        this.allBOs = BM.getBM(GlobalMailBO.class).findAll();
        Collections.sort(this.allBOs, new Comparator<GlobalMailBO>() {
            public int compare(GlobalMailBO o1, GlobalMailBO o2) {
                return (int)(o1.getId() - o2.getId());
            }
        });
        SyncTaskManager.schedule(600000, () -> {
            this.clearGlobalMail();
            return true;
        });
    }

    public List<PlayerMailBO> loadPrivateMail(long cid) {
        List<PlayerMailBO> mails = BM.getBM(PlayerMailBO.class).findAll("pid", cid);
        synchronized (this.offLineMailCached) {
            List<PlayerMailBO> cached = this.offLineMailCached.get(cid);
            if (cached != null && cached.size() > 0) {
                mails.addAll(cached);
            }

            return mails;
        }
    }

    public void clearGlobalMail() {
        if (this.allBOs.size() > 0) {
            this.lock();
            List<GlobalMailBO> removeList = new ArrayList<>();
            int curTime = CommTime.nowSecond();

            for (GlobalMailBO bo : this.allBOs.subList(0, this.allBOs.size() - 1)) {
                if (bo.getCreateTime() + bo.getExistTime() < curTime) {
                    removeList.add(bo);
                }
            }

            for (GlobalMailBO box : removeList) {
                this.allBOs.remove(box);
                box.del();
            }

            this.unlock();
        }
    }

    public List<GlobalMailBO> getGlobalMailList() {
        List<GlobalMailBO> list = null;
        this.lock();
        List<GlobalMailBO> var2 = Lists.newArrayList(this.allBOs);
        this.unlock();
        return var2;
    }

    public void sendGlobalMail(
        String senderName, String title, String content, int existTime, int pickUpExistTime, String uniformIDList, String uniformCountList
    ) {
        if (existTime == 0) {
            existTime = RefDataMgr.getFactor("MailDefaultExistTime", 604800);
        }

        GlobalMailBO bo = new GlobalMailBO();
        bo.setSenderName(senderName);
        bo.setTitle(title);
        bo.setContent(content);
        bo.setCreateTime(CommTime.nowSecond());
        bo.setExistTime(existTime);
        bo.setPickUpExistTime(pickUpExistTime);
        bo.setUniformIDList(uniformIDList);
        bo.setUniformCountList(uniformCountList);
        this.lock();
        bo.insert();
        this.allBOs.add(bo);
        this.unlock();

        for (Player p : PlayerMgr.getInstance().getOnlinePlayers()) {
            SyncTaskManager.task(() -> p.getFeature(MailFeature.class).achieveGlobalMail());
        }
    }

    public void sendMail(long cid, String senderName, String title, String content, Reward prize, String... param) {
        for (int i = 0; i < param.length; i++) {
            content = content.replace("{" + i + "}", param[i]);
        }

        String uniformIds = "";
        String counts = "";

        for (UniformItem item : prize) {
            uniformIds = uniformIds + item.getUniformId() + ";";
            counts = counts + item.getCount() + ";";
        }

        this.sendMail(cid, senderName, title, content, uniformIds, counts);
    }

    public void sendMail(long cid, String senderName, String title, String content, List<Integer> uniformIdList, List<Integer> countList) {
        int existTime = RefDataMgr.getFactor("MailDefaultExistTime", 604800);
        int pickUpExistTime = 0;
        if (uniformIdList == null || uniformIdList.size() == 0) {
            pickUpExistTime = RefDataMgr.getFactor("MailDefaultPickExistTime", 259200);
        }

        this.sendMail(cid, senderName, title, content, existTime, pickUpExistTime, uniformIdList, countList);
    }

    public void sendMail(long cid, String senderName, String title, String content, String uniformIds, String counts) {
        int existTime = RefDataMgr.getFactor("MailDefaultExistTime", 604800);
        int pickUpExistTime = 0;
        pickUpExistTime = RefDataMgr.getFactor("MailDefaultPickExistTime", 259200);
        this.sendMail(cid, senderName, title, content, existTime, pickUpExistTime, uniformIds, counts);
    }

    public void sendMail(long cid, int mailId, String... param) {
        RefMail refMail = RefDataMgr.get(RefMail.class, mailId);
        RefReward refReward = RefDataMgr.get(RefReward.class, refMail.RewardId);
        String content = refMail.Content;

        for (int i = 0; i < param.length; i++) {
            content = content.replace("{" + i + "}", param[i]);
        }

        List<Integer> uniformIdList = null;
        List<Integer> countList = null;
        if (refReward != null) {
            uniformIdList = Lists.newArrayList();
            countList = Lists.newArrayList();

            for (UniformItem item : refReward.genReward()) {
                uniformIdList.add(item.getUniformId());
                countList.add(item.getCount());
            }
        }

        this.sendMail(cid, "", refMail.Title, content, refMail.ExistTime, refMail.PickUpExistTime, uniformIdList, countList);
    }

    public void sendMail(long cid, int mailId, int uniformId, int count, String... param) {
        List<Integer> uniformIdList = Lists.newArrayList();
        List<Integer> countList = Lists.newArrayList();
        uniformIdList.add(uniformId);
        countList.add(count);
        this.sendMail(cid, mailId, uniformIdList, countList, param);
    }

    public void sendMail(long cid, int mailId, List<Integer> extUniformIdList, List<Integer> extCountList, String... param) {
        RefMail refMail = RefDataMgr.get(RefMail.class, mailId);
        RefReward refReward = RefDataMgr.get(RefReward.class, refMail.RewardId);
        String content = refMail.Content;

        for (int i = 0; i < param.length; i++) {
            content = content.replace("{" + i + "}", param[i]);
        }

        List<Integer> uniformIdList = Lists.newArrayList();
        List<Integer> countList = Lists.newArrayList();
        if (refReward != null) {
            for (UniformItem uniform : refReward.genReward()) {
                uniformIdList.add(uniform.getUniformId());
                countList.add(uniform.getCount());
            }
        }

        if (extUniformIdList != null && extUniformIdList.size() > 0) {
            uniformIdList.addAll(extUniformIdList);
            countList.addAll(extCountList);
        }

        this.sendMail(cid, "", refMail.Title, content, refMail.ExistTime, refMail.PickUpExistTime, uniformIdList, countList);
    }

    public void sendMail(long cid, int mailId, String extrasUniformIds, String extrasCounts, String params) {
        RefMail refMail = RefDataMgr.get(RefMail.class, mailId);
        String content = refMail.Content;
        if (params != null && params.length() > 0) {
            String[] array = params.split(";");

            for (int i = 0; i < array.length; i++) {
                content = content.replace("{" + i + "}", array[i]);
            }
        }

        StringBuilder itemId = new StringBuilder();
        StringBuilder counts = new StringBuilder();
        RefReward reward = RefDataMgr.get(RefReward.class, refMail.RewardId);
        if (reward != null) {
            for (UniformItem item : reward.genReward()) {
                itemId.append(item.getUniformId()).append(';');
                counts.append(item.getCount()).append(';');
            }
        }

        if (extrasUniformIds != "" && extrasUniformIds.length() > 0) {
            itemId.append(extrasUniformIds);
            counts.append(extrasCounts);
        }

        if (itemId.toString().endsWith(";")) {
            itemId.deleteCharAt(itemId.length() - 1);
        }

        if (counts.toString().endsWith(";")) {
            counts.deleteCharAt(counts.length() - 1);
        }

        this.sendMail(cid, "", refMail.Title, content, refMail.ExistTime, refMail.PickUpExistTime, itemId.toString(), counts.toString());
    }

    private void addCachedPrivateMail(long cid, PlayerMailBO mail) {
        synchronized (this.offLineMailCached) {
            List<PlayerMailBO> mails = this.offLineMailCached.get(cid);
            if (mails == null) {
                this.offLineMailCached.put(cid, mails = new ArrayList<>());
            }

            mails.add(mail);
            this.offLineMailCached.put(cid, mails);
        }
    }

    private void sendMail(
        long cid, String senderName, String title, String content, int existTime, int pickUpExistTime, List<Integer> uniformIdList, List<Integer> countList
    ) {
        String uniformIds = "";
        String counts = "";
        if (uniformIdList != null && uniformIdList.size() > 0) {
            uniformIds = StringUtils.list2String(uniformIdList);
            counts = StringUtils.list2String(countList);
        }

        this.sendMail(cid, senderName, title, content, existTime, pickUpExistTime, uniformIds, counts);
    }

    private void sendMail(
        long cid, String senderName, String title, String content, int existTime, int pickUpExistTime, String uniformIDList, String uniformCountList
    ) {
        if (!RobotManager.getInstance().isRobot(cid)) {
            PlayerMailBO bo = new PlayerMailBO();
            bo.setPid(cid);
            bo.setSenderName(senderName);
            bo.setTitle(title);
            bo.setContent(content);
            bo.setCreateTime(CommTime.nowSecond());
            bo.setExistTime(existTime);
            bo.setPickUpExistTime(pickUpExistTime);
            bo.setUniformIDList(uniformIDList);
            bo.setUniformCountList(uniformCountList);
            bo.insert();
            Player player = PlayerMgr.getInstance().getPlayer(cid);
            if (player != null) {
                if (player.isLoaded(MailFeature.class)) {
                    player.getFeature(MailFeature.class).addMail(bo);
                } else {
                    this.addCachedPrivateMail(cid, bo);
                }

                FlowLogger.mail(
                    player.getPid(),
                    player.getOpenId(),
                    player.getVipLevel(),
                    player.getLv(),
                    bo.getId(),
                    title,
                    uniformIDList,
                    uniformCountList,
                    senderName,
                    "new"
                );
            }
        }
    }

    public void delPrivateMail(long cid) {
        synchronized (this.offLineMailCached) {
            this.offLineMailCached.remove(cid);
        }
    }
}
