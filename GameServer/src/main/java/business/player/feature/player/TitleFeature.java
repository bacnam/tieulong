package business.player.feature.player;

import business.global.gmmail.MailCenter;
import business.global.notice.NoticeMgr;
import business.player.Player;
import business.player.feature.Feature;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.Title;
import com.zhonglian.server.common.utils.CommTime;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefTitle;
import core.database.game.bo.TitleBO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TitleFeature
        extends Feature {
    public final Map<Title, TitleIns> titleMap = new HashMap<>();

    private List<TitleIns> oldData = new ArrayList<>();

    public TitleFeature(Player owner) {
        super(owner);
    }

    public void loadDB() {
        List<TitleBO> titleList = BM.getBM(TitleBO.class).findAll("pid", Long.valueOf(this.player.getPid()));
        for (TitleBO bo : titleList) {
            RefTitle refData = (RefTitle) RefDataMgr.get(RefTitle.class, Integer.valueOf(bo.getTitleId()));
            if (refData == null) {
                bo.del();

                continue;
            }
            TitleIns info = new TitleIns(bo, refData);
            this.titleMap.put(refData.Type, info);

            if (info.bo.getIsActive() && !info.bo.getIsReward())
                this.oldData.add(info);
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

    public TitleIns getOrCreate(Title type) {
        TitleIns ret = this.titleMap.get(type);
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
                ret = new TitleIns(bo, ref);
                this.titleMap.put(type, ret);
            }
        }
        return ret;
    }

    public void update0(Title type, IUpdateTitle iUpdate, Integer... args) {
        TitleIns ins = getOrCreate(type);
        if (ins == null) {
            return;
        }

        if (ins.bo.getIsActive()) {
            return;
        }
        iUpdate.update(ins.bo, ins.refData, args);
        ins.bo.getIsActive();
    }

    public void updateInc(Title type, Integer value) {
        update0(type, (bo, ref, values) -> {
            int addCount = values[0].intValue();
            synchronized (bo) {
                long count = bo.getValue();
                bo.saveValue(count + addCount);
                taskActive(bo, ref);
            }
        }, new Integer[]{ value });
    }

    public void updateInc(Title type) {
        updateInc(type, Integer.valueOf(1));
    }

    public void updateMax(Title type, Integer value) {
        update0(type, (bo, ref, values) -> {
            int newvalue = values[0].intValue();
            synchronized (bo) {
                long count = bo.getValue();
                bo.saveValue(Math.max(count, newvalue));
                taskActive(bo, ref);
            }
        }, new Integer[]{ value });
    }

    public void updateMin(Title type, Integer value) {
        update0(type, (bo, ref, values) -> {
            int newvalue = values[0].intValue();
            synchronized (bo) {
                long count = bo.getValue();
                if (count == 0L) count = newvalue;
                bo.saveValue(Math.min(count, newvalue));
                taskActive(bo, ref);
            }
        }, new Integer[]{ value });
    }

    private void taskActive(TitleBO bo, RefTitle ref) {
        if (ref.NumRange.within((int) bo.getValue())) {
            bo.saveActiveTime(CommTime.getTodayZeroClockS());
            bo.saveIsActive(true);

            if (ref.id == 6 && bo.getIsActive()) {
                NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.Richman, new String[]{this.player.getName()});
            }

            MailCenter.getInstance().sendMail(this.player.getPid(), ref.MailId, new String[0]);
            bo.saveIsReward(true);
        }
    }

    public TitleIns getTitleByType(Title type) {
        TitleIns ins = this.titleMap.get(type);
        return ins;
    }

    public void checkAllTitle() {
        if (this.oldData.size() == 0) {
            return;
        }

        for (TitleIns ins : this.oldData) {
            MailCenter.getInstance().sendMail(this.player.getPid(), ins.refData.MailId, new String[0]);
            ins.bo.saveIsReward(true);
        }
        this.oldData.clear();
    }

    public void resetAchievement(ConstEnum.AchieveReset resetType) {
        synchronized (this.titleMap) {
            for (TitleIns achieve : this.titleMap.values()) {
                if (achieve.refData.Reset != resetType) {
                    continue;
                }
                achieve.bo.setActiveTime(0);
                achieve.bo.setIsActive(false);
                achieve.bo.setIsReward(false);
                achieve.bo.setValue(0L);
                achieve.bo.saveAll();
            }
        }
    }

    public void dailyRefresh() {
        try {
            resetAchievement(ConstEnum.AchieveReset.EveryDay);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}

