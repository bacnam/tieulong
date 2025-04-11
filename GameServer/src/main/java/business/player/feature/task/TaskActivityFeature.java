package business.player.feature.task;

import business.player.Player;
import business.player.feature.Feature;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.FetchStatus;
import com.zhonglian.server.common.utils.StringUtils;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefDailyActive;
import core.database.game.bo.DailyactiveBO;
import core.network.proto.TaskActive;

import java.util.List;

public class TaskActivityFeature
        extends Feature {
    public DailyactiveBO dailyactiveBO;

    public TaskActivityFeature(Player owner) {
        super(owner);
    }

    public void loadDB() {
        this.dailyactiveBO = (DailyactiveBO) BM.getBM(DailyactiveBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
    }

    public DailyactiveBO getOrCreate() {
        DailyactiveBO dailyactiveBO = this.dailyactiveBO;
        if (dailyactiveBO == null) {
            dailyactiveBO = new DailyactiveBO();
            dailyactiveBO.setPid(this.player.getPid());
            dailyactiveBO.setValue(0);
            dailyactiveBO.setTeamLevel(this.player.getPlayerBO().getLv());
            dailyactiveBO.insert();
            this.dailyactiveBO = dailyactiveBO;
        }
        return dailyactiveBO;
    }

    public void addDailyActive(int value) {
        DailyactiveBO dailyactiveBO = getOrCreate();
        dailyactiveBO.saveValue(dailyactiveBO.getValue() + value);
        pushTaskActiveInfo();
    }

    public void pushTaskActiveInfo() {
        DailyactiveBO dailyactiveBO = getOrCreate();
        TaskActive builder = new TaskActive();
        int refId = ((Integer) RefDailyActive.level2refId.get(Integer.valueOf(dailyactiveBO.getTeamLevel()))).intValue();
        builder.setId(refId);
        builder.setValue(dailyactiveBO.getValue());

        List<Integer> fetchedTaskIndex = StringUtils.string2Integer(dailyactiveBO.getFetchedTaskIndex());
        RefDailyActive refDailyActive = (RefDailyActive) RefDataMgr.get(RefDailyActive.class, Integer.valueOf(refId));
        for (int i = 0; i < refDailyActive.Condition.size(); i++) {
            if (fetchedTaskIndex.contains(Integer.valueOf(i))) {
                builder.stepStatus.add(Integer.valueOf(FetchStatus.Fetched.ordinal()));
            } else {

                Integer needValue = refDailyActive.Condition.get(i);
                if (dailyactiveBO.getValue() >= needValue.intValue()) {
                    builder.stepStatus.add(Integer.valueOf(FetchStatus.Can.ordinal()));
                } else {
                    builder.stepStatus.add(Integer.valueOf(FetchStatus.Cannot.ordinal()));
                }
            }
        }
        this.player.pushProto("taskActiveInfo", builder);
    }

    public void dailyRefresh() {
        try {
            DailyactiveBO dailyactiveBO = getOrCreate();
            dailyactiveBO.setValue(0);
            dailyactiveBO.setTeamLevel(this.player.getPlayerBO().getLv());
            dailyactiveBO.setFetchedTaskIndex("");
            dailyactiveBO.saveAll();
            pushTaskActiveInfo();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}

