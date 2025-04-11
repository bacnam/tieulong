package business.global.battle;

import BaseCommon.CommLog;
import business.global.arena.ArenaConfig;
import business.player.Player;
import business.player.feature.guild.GuildMemberFeature;
import com.zhonglian.server.common.enums.Attribute;
import com.zhonglian.server.common.enums.FightResult;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefCrystalPrice;
import core.database.game.bo.GuildMemberBO;
import core.server.ServerConfig;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class SimulatBattle
        extends Battle {
    private static int atk_x;
    private static int atk_y;
    private static int def_x;
    private static int def_y;
    private static int MaxTime = 0;
    private static int tilewidth;
    private static int tileheight;
    private static String map = "BattleBossMap03";

    static {
        String mapPath = String.valueOf(ServerConfig.BattleMapPath()) + File.separator +
                RefDataMgr.getGeneral("BattleArenaMap", SimulatBattle.map) + ".tmx";
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            Element root = dbf.newDocumentBuilder().parse(mapPath).getDocumentElement();
            NamedNodeMap attrnodes = root.getAttributes();
            tilewidth = Integer.valueOf(attrnodes.getNamedItem("tilewidth").getNodeValue()).intValue();
            tileheight = Integer.valueOf(attrnodes.getNamedItem("tileheight").getNodeValue()).intValue();

            String map = ((Element) root.getElementsByTagName("layer").item(0)).getElementsByTagName("data").item(0).getTextContent();

            String[] rows = map.split("\n");
            for (int i = 1; i < rows.length; i++) {
                String[] cols = rows[i].trim().split(",");
                for (int j = 0; j < cols.length; j++) {
                    if (cols[j].equals("2")) {
                        atk_x = j * tilewidth + tilewidth / 2;
                        atk_y = (i - 1) * tileheight + tileheight / 2;
                    } else if (cols[j].equals("3")) {
                        def_x = j * tilewidth + tilewidth / 2;
                        def_y = (i - 1) * tileheight + tileheight / 2;
                    }
                }
            }
        } catch (Exception e) {
            CommLog.error("初始化竞技场战斗出现异常", e);
        }
        MaxTime = ArenaConfig.fightTime();
    }

    public SimulatBattle(Player me, Player opponent) {
        super(me, opponent);
    }

    public void initHp(List<Double> atkHp, List<Double> defHp) {
        if (atkHp != null && atkHp.size() >= 0) {
            int i = 0;
            for (Creature cre : this.team) {
                if (cre.RoleType == RoleType.Character) {
                    cre.hp = ((Double) atkHp.get(i)).doubleValue();
                    cre.initHp = ((Double) atkHp.get(i)).doubleValue();
                    i++;
                }
            }
        }

        if (defHp != null && defHp.size() >= 0) {
            int i = 0;
            for (Creature cre : this.opponents) {
                if (cre.RoleType == RoleType.Character &&
                        cre.RoleType == RoleType.Character) {
                    cre.hp = ((Double) defHp.get(i)).doubleValue();
                    cre.initHp = ((Double) defHp.get(i)).doubleValue();
                    i++;
                }
            }
        }
    }

    public void initInspire() {
        int meTimes = 0;
        int oppoTimes = 0;

        GuildMemberBO boMe = ((GuildMemberFeature) this.me.getFeature(GuildMemberFeature.class)).getBo();
        GuildMemberBO boOppo = ((GuildMemberFeature) this.opponent.getFeature(GuildMemberFeature.class)).getBo();
        if (boMe != null)
            meTimes = ((GuildMemberFeature) this.me.getFeature(GuildMemberFeature.class)).getBo().getGuildwarInspire();
        if (boOppo != null) {
            oppoTimes = ((GuildMemberFeature) this.opponent.getFeature(GuildMemberFeature.class)).getBo().getGuildwarInspire();
        }
        if (meTimes != 0) {
            for (Creature cre : this.team) {
                if (cre.RoleType == RoleType.Character) {
                    Double base = cre.attrs.get(Attribute.ATK);
                    int value = (RefCrystalPrice.getPrize(meTimes)).GuildwarInspireValue;
                    if (base != null) {
                        cre.attrs.put(Attribute.ATK, Double.valueOf(base.doubleValue() * (100 + value) / 100.0D));
                    }
                }
            }
        }

        if (oppoTimes != 0) {
            for (Creature cre : this.opponents) {
                if (cre.RoleType == RoleType.Character) {
                    Double base = cre.attrs.get(Attribute.ATK);
                    int value = (RefCrystalPrice.getPrize(meTimes)).GuildwarInspireValue;
                    if (base != null) {
                        cre.attrs.put(Attribute.ATK, Double.valueOf(base.doubleValue() * (100 + value) / 100.0D));
                    }
                }
            }
        }
    }

    protected String getMap() {
        return map;
    }

    protected int fightTime() {
        return MaxTime;
    }

    protected int tiledWidth() {
        return tilewidth;
    }

    protected int tiledHeight() {
        return tileheight;
    }

    public void onLost() {
        this.stopped = true;
        CommLog.info("def player win!");
    }

    public void onWin() {
        this.stopped = true;
        CommLog.info("atk player win!");
    }

    public List<Double> getWinnerHp() {
        List<Double> list = new LinkedList<>();
        if (getResult() == FightResult.Win) {
            this.team.stream().filter(x -> (x.RoleType == RoleType.Character)).forEach(x -> paramList.add(Double.valueOf(x.hp)));

        } else if (getResult() == FightResult.Lost) {
            this.opponents.stream().filter(x -> (x.RoleType == RoleType.Character)).forEach(x -> paramList.add(Double.valueOf(x.hp)));
        }

        return list;
    }

    protected void initLoc() {
        initLoc(this.opponents, def_x, def_y);
        initLoc(this.team, atk_x, atk_y);
    }

    public FightResult getResult() {
        return this.result;
    }
}

