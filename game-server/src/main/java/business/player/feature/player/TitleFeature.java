package business.player.feature.player;

import business.global.gmmail.MailCenter;
import business.global.notice.NoticeMgr;
import business.player.Player;
import business.player.feature.Feature;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.Title;
import com.zhonglian.server.common.enums.ConstEnum.AchieveReset;
import com.zhonglian.server.common.enums.ConstEnum.UniverseMessageType;
import com.zhonglian.server.common.utils.CommTime;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefTitle;
import core.database.game.bo.TitleBO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TitleFeature extends Feature {
    public final Map<Title, TitleFeature.TitleIns> titleMap = new HashMap<>();
    private List<TitleFeature.TitleIns> oldData = new ArrayList<>();

    public TitleFeature(Player owner) {
        super(owner);
    }

    @Override
    public void loadDB() {
        for (TitleBO bo : BM.getBM(TitleBO.class).findAll("pid", this.player.getPid())) {
            RefTitle refData = RefDataMgr.get(RefTitle.class, bo.getTitleId());
            if (refData == null) {
                bo.del();
            } else {
                TitleFeature.TitleIns info = new TitleFeature.TitleIns(bo, refData);
                this.titleMap.put(refData.Type, info);
                if (info.bo.getIsActive() && !info.bo.getIsReward()) {
                    this.oldData.add(info);
                }
            }
        }
    }

    public TitleFeature.TitleIns getOrCreate(Title type) {
        TitleFeature.TitleIns ret = this.titleMap.get(type);
        if (ret == null) {
            synchronized (this.titleMap) {
                RefTitle ref = RefTitle.getTitleByType(type);
                if (ref == null) {
                    return null;
                }

                TitleBO bo = new TitleBO();
                bo.setTitleId(ref.id);
                bo.setPid(this.player.getPid());
                bo.insert();
                ret = new TitleFeature.TitleIns(bo, ref);
                this.titleMap.put(type, ret);
            }
        }

        return ret;
    }

    public void update0(Title type, IUpdateTitle iUpdate, Integer... args) {
        TitleFeature.TitleIns ins = this.getOrCreate(type);
        if (ins != null) {
            if (!ins.bo.getIsActive()) {
                iUpdate.update(ins.bo, ins.refData, args);
                ins.bo.getIsActive();
            }
        }
    }

    public void updateInc(Title type, Integer value) {
        this.update0(type, (bo, ref, values) -> {
            int addCount = values[0];
            synchronized (bo) {
                long count = bo.getValue();
                bo.saveValue(count + addCount);
                this.taskActive(bo, ref);
            }
        }, value);
    }

    public void updateInc(Title type) {
        this.updateInc(type, 1);
    }

    public void updateMax(Title type, Integer value) {
        this.update0(type, (bo, ref, values) -> {
            int newvalue = values[0];
            synchronized (bo) {
                long count = bo.getValue();
                bo.saveValue(Math.max(count, (long)newvalue));
                this.taskActive(bo, ref);
            }
        }, value);
    }

    public void updateMin(Title type, Integer value) {
        this.update0(type, (bo, ref, values) -> {
            int newvalue = values[0];
            synchronized (bo) {
                long count = bo.getValue();
                if (count == 0L) {
                    count = newvalue;
                }

                bo.saveValue(Math.min(count, (long)newvalue));
                this.taskActive(bo, ref);
            }
        }, value);
    }

    private void taskActive(TitleBO bo, RefTitle ref) {
        if (ref.NumRange.within((int)bo.getValue())) {
            bo.saveActiveTime(CommTime.getTodayZeroClockS());
            bo.saveIsActive(true);
            if (ref.id == 6 && bo.getIsActive()) {
                NoticeMgr.getInstance().sendMarquee(UniverseMessageType.Richman, this.player.getName());
            }

            MailCenter.getInstance().sendMail(this.player.getPid(), ref.MailId);
            bo.saveIsReward(true);
        }
    }

    public TitleFeature.TitleIns getTitleByType(Title type) {
        return this.titleMap.get(type);
    }

    public void checkAllTitle() {
        if (this.oldData.size() != 0) {
            for (TitleFeature.TitleIns ins : this.oldData) {
                MailCenter.getInstance().sendMail(this.player.getPid(), ins.refData.MailId);
                ins.bo.saveIsReward(true);
            }

            this.oldData.clear();
        }
    }

    public void resetAchievement(AchieveReset resetType) {
        synchronized (this.titleMap) {
            for (TitleFeature.TitleIns achieve : this.titleMap.values()) {
                if (achieve.refData.Reset == resetType) {
                    achieve.bo.setActiveTime(0);
                    achieve.bo.setIsActive(false);
                    achieve.bo.setIsReward(false);
                    achieve.bo.setValue(0L);
                    achieve.bo.saveAll();
                }
            }
        }
    }

    public void dailyRefresh() {
        try {
            this.resetAchievement(AchieveReset.EveryDay);
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    public static class TitleIns {
        public TitleBO bo;
        public RefTitle refData;

        public TitleIns(TitleBO bo, RefTitle refData) {
            this.bo = bo;
            this.refData = refData;
        }
    }
}
