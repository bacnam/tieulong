package core.server;

import BaseCommon.CommLog;
import BaseServer.Monitor;
import ConsoleTask.ConsoleTaskManager;
import bsh.EvalError;
import business.global.activity.ActivityMgr;
import business.global.arena.ArenaManager;
import business.global.confreward.RewardConfMgr;
import business.global.gmmail.MailCenter;
import business.global.gmmail.TimerMailMgr;
import business.global.guild.GuildMgr;
import business.global.guild.GuildWarMgr;
import business.global.notice.NoticeMgr;
import business.global.rank.RankManager;
import business.global.recharge.RechargeMgr;
import business.global.redpacket.RedPacketMgr;
import business.global.refresh.RefreshMgr;
import business.global.worldboss.WorldBossMgr;
import business.gmcmd.cmd.CommandMgr;
import business.player.PlayerMgr;
import business.player.RobotManager;
import com.sun.net.httpserver.HttpServer;
import com.zhonglian.server.common.HeartBeat;
import com.zhonglian.server.common.IApp;
import com.zhonglian.server.common.db.DBCons;
import com.zhonglian.server.common.db.DatabaseFactory;
import com.zhonglian.server.common.db.mgr.Game_DBVersionManager;
import com.zhonglian.server.common.db.mgr.Log_DBVersionManager;
import com.zhonglian.server.common.mgr.sensitive.SensitiveWordMgr;
import com.zhonglian.server.http.server.HttpDispather;
import com.zhonglian.server.http.server.MGHttpServer;
import com.zhonglian.server.logger.flow.db.DBFlowMgr;
import com.zhonglian.server.logger.flow.redis.RedisFlowMgr;
import core.config.refdata.RefDataMgr;
import core.logger.flow.FlowLogger;
import core.logger.flow.GameFlowLogger;
import core.network.client2game.ClientAcceptor;
import core.network.game2world.WorldConnector;
import core.network.game2zone.ZoneConnector;
import core.network.http.handler.Recharge;
import java.io.File;

public class GameServer extends IApp {
    public static void main(String[] args) throws EvalError {
        GameServer app = new GameServer();
        app.init(args);
        app.start();
    }

    @Override
    protected void beforeInit(String configdit) {
        this.loadBasicConfig(configdit, "logback_game.xml");
        this.loadRemoteConfig("server_id", System.getProperty("game_sid"), configdit, "game.properties");
    }

    @Override
    protected boolean initBase() {
        ConsoleTaskManager.GetInstance().setRunner(new BshRunner());
        RefDataMgr.getInstance().reload();
        ServerConfig.checkMapPath();
        SensitiveWordMgr.getInstance().init(RefDataMgr.getInstance().getRefPath() + File.separator + "keywords.txt");
        SensitiveWordMgr.getInstance().reload();
        DBCons.setDBFactory(new DatabaseFactory(ServerConfig.DBUrl(), ServerConfig.DBUser(), ServerConfig.DBPassword()));
        DBCons.setLogDBFactory(new DatabaseFactory(ServerConfig.LogDBUrl(), ServerConfig.LogDBUser(), ServerConfig.LogDBPassword()));
        Game_DBVersionManager.getInstance().checkAndUpdateVersion("core.database.game.bo");
        DBFlowMgr.getInstance().updateDB("core.logger.flow.impl.db");
        Game_DBVersionManager.getInstance().setNewestVersion("1.0.0.1");
        Log_DBVersionManager.getInstance().setNewestVersion("1.0.0.1");
        if (Game_DBVersionManager.getInstance().runAutoVersionUpdate("core.database.game.update")
            && Log_DBVersionManager.getInstance().runAutoVersionUpdate("core.database.log.update")) {
            FlowLogger.getInstance().init(GameFlowLogger.class, "core.logger.flow.impl");
            DBFlowMgr.getInstance().start();
            RedisFlowMgr.getInstance().start();
            OpenSeverTime.getInstance().init();
            Monitor.getInstance().regCleanMemory(CleanMemory.GetInstance());
            return true;
        } else {
            CommLog.error("!!!!!!!!! 数据库版本升级失败 !!!!!!!!!!");
            return false;
        }
    }

    @Override
    protected boolean startNet() {
        ClientAcceptor.getInstance().startSocket(Integer.getInteger("GameServer.ClientPort"));
        if ("on".equalsIgnoreCase(System.getProperty("GameServer.ZoneOpen"))) {
            ZoneConnector.getInstance().reconnect(System.getProperty("GameServer.Zone_IP", "127.0.0.1"), Integer.getInteger("GameServer.Zone_Port", 8002));
        }

        if ("on".equalsIgnoreCase(System.getProperty("GameServer.WorldOpen"))) {
            WorldConnector.getInstance().reconnect(System.getProperty("GameServer.World_IP", "127.0.0.1"), Integer.getInteger("GameServer.World_Port", 8003));
        }

        this.starHttp();
        return true;
    }

    @Override
    protected boolean initLogic() {
        DynamicConfig.init();
        RechargeMgr.getInstance().init();
        RankManager.getInstance().init();
        CommandMgr.getInstance().init();
        PlayerMgr.getInstance().init();
        RobotManager.getInstance().init();
        MailCenter.getInstance().init();
        TimerMailMgr.getInstance().init();
        RewardConfMgr.getInstance().init();
        NoticeMgr.getInstance().init();
        ArenaManager.getInstance().init();
        WorldBossMgr.getInstance().init();
        ActivityMgr.getInstance().init();
        GuildMgr.getInstance().init();
        GuildWarMgr.getInstance().init();
        RedPacketMgr.getInstance().init();
        return true;
    }

    @Override
    protected void afterInit() {
        RefreshMgr.getInstance().init();
    }

    @Override
    protected void start() {
        HeartBeat.start();
    }

    public void starHttp() {
        try {
            HttpDispather dispather = new HttpDispather();
            String pack = Recharge.class.getPackage().getName();
            dispather.init(pack);
            HttpServer httpServer = MGHttpServer.createServer(Integer.getInteger("Server.HttpServer", 9888), dispather, "/");
            httpServer.start();
        } catch (Exception var4) {
            CommLog.error("启动Http错误", (Throwable)var4);
            System.exit(-1);
        }
    }

    public static void reload() {
        RefDataMgr.getInstance().reload();
        SensitiveWordMgr.getInstance().reload();
        RefreshMgr.getInstance().reload();
    }
}
