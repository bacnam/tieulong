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

public class TaskActivityFeature extends Feature {
    public DailyactiveBO dailyactiveBO;

    public TaskActivityFeature(Player owner) {
        super(owner);
    }

    @Override
    public void loadDB() {
        this.dailyactiveBO = BM.getBM(DailyactiveBO.class).findOne("pid", this.player.getPid());
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
        DailyactiveBO dailyactiveBO = this.getOrCreate();
        dailyactiveBO.saveValue(dailyactiveBO.getValue() + value);
        this.pushTaskActiveInfo();
    }

    public void pushTaskActiveInfo() {
        DailyactiveBO dailyactiveBO = this.getOrCreate();
        TaskActive builder = new TaskActive();
        int refId = RefDailyActive.level2refId.get(dailyactiveBO.getTeamLevel());
        builder.setId(refId);
        builder.setValue(dailyactiveBO.getValue());
        List<Integer> fetchedTaskIndex = StringUtils.string2Integer(dailyactiveBO.getFetchedTaskIndex());
        RefDailyActive refDailyActive = RefDataMgr.get(RefDailyActive.class, refId);

        for (int i = 0; i < refDailyActive.Condition.size(); i++) {
            if (fetchedTaskIndex.contains(i)) {
                builder.stepStatus.add(FetchStatus.Fetched.ordinal());
            } else {
                Integer needValue = refDailyActive.Condition.get(i);
                if (dailyactiveBO.getValue() >= needValue) {
                    builder.stepStatus.add(FetchStatus.Can.ordinal());
                } else {
                    builder.stepStatus.add(FetchStatus.Cannot.ordinal());
                }
            }
        }

        this.player.pushProto("taskActiveInfo", builder);
    }

    public void dailyRefresh() {
        try {
            DailyactiveBO dailyactiveBO = this.getOrCreate();
            dailyactiveBO.setValue(0);
            dailyactiveBO.setTeamLevel(this.player.getPlayerBO().getLv());
            dailyactiveBO.setFetchedTaskIndex("");
            dailyactiveBO.saveAll();
            this.pushTaskActiveInfo();
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }
}
