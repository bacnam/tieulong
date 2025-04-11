package core.network.proto;

import business.player.PlayerMgr;
import business.player.feature.PlayerBase;
import core.database.game.bo.RedPacketPickBO;

public class RedPacketPickInfo {
    public long id;
    public Player.Summary summary;
    public int pickMoney;

    public RedPacketPickInfo(RedPacketPickBO bo) {
        this.id = bo.getId();
        this.summary = ((PlayerBase) PlayerMgr.getInstance().getPlayer(bo.getPid()).getFeature(PlayerBase.class)).summary();
        this.pickMoney = bo.getMoney();
    }
}

