package core.network.client2game.handler.pvp;

import business.global.arena.ArenaManager;
import business.player.Player;
import business.player.PlayerMgr;
import com.zhonglian.server.common.enums.FightResult;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.database.game.bo.ArenaFightRecordBO;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArenaFightRecord
        extends PlayerHandler {
    static PlayerMgr playerManager = null;

    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Collection<ArenaFightRecordBO> recods = ArenaManager.getInstance().getFightRecords(player.getPid());
        List<Record> rtn = new ArrayList<>();
        if (recods != null) {
            for (ArenaFightRecordBO r : recods) {
                rtn.add(new Record(player.getPid(), r));
            }
        }
        request.response(rtn);
    }

    private static class Record {
        int time;
        String oppoName;
        FightResult rslt;
        int rank;
        boolean isAttack;

        public Record(long reader, ArenaFightRecordBO bo) {
            if (ArenaFightRecord.playerManager == null) {
                ArenaFightRecord.playerManager = PlayerMgr.getInstance();
            }
            this.time = bo.getEndTime();
            if (reader == bo.getAtkPid()) {
                this.rank = bo.getAtkRank();
                this.oppoName = ArenaFightRecord.playerManager.getPlayer(bo.getDefPid()).getName();
                this.rslt = FightResult.values()[bo.getResult()];
                this.isAttack = true;
            } else {
                this.rank = bo.getDefRank();
                this.oppoName = ArenaFightRecord.playerManager.getPlayer(bo.getAtkPid()).getName();
                this.rslt = (FightResult.values()[bo.getResult()] == FightResult.Win) ? FightResult.Lost : FightResult.Win;
                this.isAttack = false;
            }
        }
    }
}

