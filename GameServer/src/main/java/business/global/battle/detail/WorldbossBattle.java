package business.global.battle.detail;

import BaseCommon.CommLog;
import business.global.battle.Battle;
import business.global.battle.Creature;
import business.player.Player;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefWorldBoss;
import core.server.ServerConfig;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class WorldbossBattle
        extends Battle {
    private int atk_x;
    private int atk_y;
    private int def_x;
    private int def_y;
    private int MaxTime = 0;
    private int tilewidth;
    private int tileheight;
    private String map;
    private long MaxHp = 0L;

    public WorldbossBattle(Player me, int monsterId) {
        super(me, monsterId);
    }

    public void init(int bossId) {
        RefWorldBoss ref = (RefWorldBoss) RefDataMgr.get(RefWorldBoss.class, Integer.valueOf(bossId));
        this.MaxHp = ref.MaxHP;
        this.map = ref.MapId;
        String mapPath = String.valueOf(ServerConfig.BattleMapPath()) + File.separator +
                ref.MapId + ".tmx";
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            Element root = dbf.newDocumentBuilder().parse(mapPath).getDocumentElement();
            NamedNodeMap attrnodes = root.getAttributes();
            this.tilewidth = Integer.valueOf(attrnodes.getNamedItem("tilewidth").getNodeValue()).intValue();
            this.tileheight = Integer.valueOf(attrnodes.getNamedItem("tileheight").getNodeValue()).intValue();

            String map = ((Element) root.getElementsByTagName("layer").item(0)).getElementsByTagName("data").item(0).getTextContent();

            String[] rows = map.split("\n");
            for (int i = 1; i < rows.length; i++) {
                String[] cols = rows[i].trim().split(",");
                for (int j = 0; j < cols.length; j++) {
                    if (cols[j].equals("2")) {
                        this.atk_x = j * this.tilewidth + this.tilewidth / 2;
                        this.atk_y = (i - 1) * this.tileheight + this.tileheight / 2;
                    } else if (cols[j].equals("3")) {
                        this.def_x = j * this.tilewidth + this.tilewidth / 2;
                        this.def_y = (i - 1) * this.tileheight + this.tileheight / 2;
                    }
                }
            }
        } catch (Exception e) {
            CommLog.error("初始化世界BOSS战斗出现异常", e);
        }
        this.MaxTime = RefDataMgr.getFactor("WorldBossAttackCD", 30);
    }

    protected String getMap() {
        return this.map;
    }

    protected int fightTime() {
        return this.MaxTime;
    }

    protected int tiledWidth() {
        return this.tilewidth;
    }

    protected int tiledHeight() {
        return this.tileheight;
    }

    public void onLost() {
        this.stopped = true;
        CommLog.info("def player win!");
    }

    public void onWin() {
        this.stopped = true;
        CommLog.info("atk player win!");
    }

    protected void initLoc() {
        initLoc(this.opponents, this.def_x, this.def_y);
        initLoc(this.team, this.atk_x, this.atk_y);
    }

    public long getDamage() {
        Creature boss = this.opponents.get(0);
        return this.MaxHp - (long) boss.getHp();
    }
}

