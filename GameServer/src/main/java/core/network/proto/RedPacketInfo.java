package core.network.proto;

import business.player.PlayerMgr;
import business.player.feature.PlayerBase;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefRedPacket;
import core.database.game.bo.RedPacketBO;

public class RedPacketInfo {
    public long id;
    public Player.Summary summary;
    public String packetName;
    public int packetValue;
    public int leftMoney;
    public int maxMoney;
    public int alreadyPick;
    public int maxPick;
    public int createtime;
    public int status;

    public RedPacketInfo(RedPacketBO bo) {
        if (bo != null) {
            this.id = bo.getId();
            this.summary = ((PlayerBase) PlayerMgr.getInstance().getPlayer(bo.getPid()).getFeature(PlayerBase.class)).summary();
            RefRedPacket refRedPacket = (RefRedPacket) RefDataMgr.get(RefRedPacket.class, Integer.valueOf(bo.getPacketTypeId()));
            this.packetName = refRedPacket.Name;
            this.packetValue = refRedPacket.Money;
            this.leftMoney = bo.getLeftMoney();
            this.maxMoney = bo.getMaxMoney();
            this.alreadyPick = bo.getAlreadyPick();
            this.maxPick = bo.getMaxPick();
            this.createtime = bo.getTime();
        }
    }
}

