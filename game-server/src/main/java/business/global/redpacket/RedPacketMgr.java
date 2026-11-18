package business.global.redpacket;

import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerCurrency;
import business.player.feature.features.PlayerRecord;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.FetchStatus;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Random;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefRedPacket;
import core.database.game.bo.RedPacketBO;
import core.database.game.bo.RedPacketPickBO;
import core.network.proto.RedPacketInfo;
import core.network.proto.RedPacketPickInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RedPacketMgr {
    private static volatile RedPacketMgr instance = null;
    private Map<Long, RedPacketBO> packetMap = new ConcurrentHashMap<>();
    private Map<Long, Map<Long, RedPacketPickBO>> pickMap = new ConcurrentHashMap<>();

    private RedPacketMgr() {
    }

    public static RedPacketMgr getInstance() {
        if (instance == null) {
            synchronized (RedPacketMgr.class) {
                if (instance == null) {
                    instance = new RedPacketMgr();
                }
            }
        }

        return instance;
    }

    public void init() {
        List<RedPacketBO> packetList = BM.getBM(RedPacketBO.class).findAll();
        List<RedPacketPickBO> pickList = BM.getBM(RedPacketPickBO.class).findAll();

        for (RedPacketBO packetBO : packetList) {
            this.packetMap.put(packetBO.getId(), packetBO);
        }

        for (RedPacketPickBO pickBO : pickList) {
            Map<Long, RedPacketPickBO> map = this.pickMap.get(pickBO.getPacketId());
            if (map == null) {
                map = new HashMap<>();
                this.pickMap.put(pickBO.getPacketId(), map);
            }

            map.put(pickBO.getPid(), pickBO);
        }
    }

    public void handleRecharge(int money, Player player) {
        RefRedPacket refpacket = null;

        for (RefRedPacket ref : RefDataMgr.getAll(RefRedPacket.class).values()) {
            if (ref.Range.within(money)) {
                refpacket = ref;
                break;
            }
        }

        if (refpacket != null) {
            RedPacketBO bo = new RedPacketBO();
            bo.setPid(player.getPid());
            bo.setMaxMoney(refpacket.Money);
            bo.setLeftMoney(bo.getMaxMoney());
            bo.setMaxPick(refpacket.PickNum);
            bo.setPacketTypeId(refpacket.id);
            bo.setTime(CommTime.nowSecond());
            bo.insert();
            this.packetMap.put(bo.getId(), bo);

            for (Player online : PlayerMgr.getInstance().getOnlinePlayers()) {
                online.pushProto("RedPacket", this.toProtocol(bo, player));
            }
        }
    }

    public RedPacketInfo toProtocol(RedPacketBO bo, Player player) {
        RedPacketInfo info = new RedPacketInfo(bo);
        Map<Long, RedPacketPickBO> pickMap = this.pickMap.get(bo.getId());
        if (pickMap != null && pickMap.get(player.getPid()) != null) {
            info.status = FetchStatus.Fetched.ordinal();
        } else if (bo.getAlreadyPick() == bo.getMaxPick()) {
            info.status = FetchStatus.Cannot.ordinal();
        } else {
            info.status = FetchStatus.Can.ordinal();
        }

        return info;
    }

    public RedPacketInfo getPacket(long id, Player player) {
        RedPacketBO bo = this.packetMap.get(id);
        RedPacketInfo info = null;
        if (bo != null) {
            info = this.toProtocol(bo, player);
        }

        return info;
    }

    public List<RedPacketInfo> getList(Player player) {
        List<RedPacketInfo> list = new ArrayList<>();
        List<RedPacketBO> removeList = new ArrayList<>();
        synchronized (this) {
            this.packetMap.values().stream().forEach(x -> {
                if (x.getLeftMoney() == 0 && x.getTime() + 259200 < CommTime.nowSecond()) {
                    removeList.add(x);
                } else {
                    list.add(this.toProtocol(x, player));
                }
            });
            this.removePacket(removeList);
            return list;
        }
    }

    public void removePacket(List<RedPacketBO> removeList) {
        for (RedPacketBO bo : removeList) {
            this.packetMap.remove(bo.getId());
            Map<Long, RedPacketPickBO> pick = this.pickMap.get(bo.getId());
            if (pick != null) {
                for (RedPacketPickBO pickBO : pick.values()) {
                    pickBO.del();
                }

                this.pickMap.remove(bo.getId());
            }

            bo.del();
        }
    }

    public List<RedPacketPickInfo> getPickInfo(long id) {
        List<RedPacketPickInfo> list = new ArrayList<>();
        Map<Long, RedPacketPickBO> boMap = this.pickMap.get(id);
        if (boMap != null) {
            boMap.values().stream().forEach(x -> list.add(new RedPacketPickInfo(x)));
        }

        return list;
    }

    public int snatchPacket(long id, Player player) throws WSException {
        int times = player.getFeature(PlayerRecord.class).getValue(DailyRefresh.RedPacket);
        if (times >= this.getMaxTime()) {
            throw new WSException(ErrorCode.Packet_NotExist, "红包今日领取次数已满");
        } else {
            synchronized (this) {
                RedPacketBO packetBO = this.packetMap.get(id);
                if (packetBO == null) {
                    throw new WSException(ErrorCode.Packet_NotExist, "红包不存在");
                } else {
                    Map<Long, RedPacketPickBO> pickMap = this.pickMap.get(id);
                    if (pickMap != null && pickMap.get(player.getPid()) != null) {
                        throw new WSException(ErrorCode.Packet_Picked, "红包已领取");
                    } else {
                        int leftMoney = packetBO.getLeftMoney();
                        int leftpick = packetBO.getMaxPick() - packetBO.getAlreadyPick();
                        if (leftpick <= 0) {
                            throw new WSException(ErrorCode.Packet_Out, "红包已抢完");
                        } else {
                            int gain = 0;
                            if (leftpick == 1) {
                                gain = leftMoney;
                            } else {
                                int avg = leftMoney / leftpick;
                                if (avg == 0) {
                                    avg++;
                                }

                                int maxavg = avg * 2;
                                int min;
                                if ((leftpick - 1) * maxavg >= leftMoney) {
                                    min = 1;
                                } else {
                                    min = leftMoney - (leftpick - 1) * maxavg;
                                }

                                int max;
                                if (maxavg + (leftpick - 1) * 1 <= leftMoney) {
                                    max = maxavg;
                                } else {
                                    max = leftMoney - (leftpick - 1);
                                }

                                int rand = Random.nextInt(max) + 1;
                                gain = Math.max(min, rand);
                            }

                            packetBO.setLeftMoney(packetBO.getLeftMoney() - gain);
                            packetBO.setAlreadyPick(packetBO.getAlreadyPick() + 1);
                            packetBO.saveAll();
                            RedPacketPickBO bo = new RedPacketPickBO();
                            bo.setPid(player.getPid());
                            bo.setMoney(gain);
                            bo.setPacketId(packetBO.getId());
                            bo.setTime(CommTime.nowSecond());
                            bo.insert();
                            Map<Long, RedPacketPickBO> map = this.pickMap.get(packetBO.getId());
                            if (map == null) {
                                map = new HashMap<>();
                                this.pickMap.put(packetBO.getId(), map);
                            }

                            map.put(player.getPid(), bo);
                            player.getFeature(PlayerCurrency.class).gain(PrizeType.Crystal, gain, ItemFlow.RedPacket);
                            player.getFeature(PlayerRecord.class).addValue(DailyRefresh.RedPacket);
                            return gain;
                        }
                    }
                }
            }
        }
    }

    public int getMaxTime() {
        return RefDataMgr.getFactor("RedPacketPickTimes", 10);
    }
}
