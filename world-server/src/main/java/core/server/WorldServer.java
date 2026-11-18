package core.server;

import BaseCommon.CommLog;
import ConsoleTask.ConsoleTaskManager;
import bsh.EvalError;
import business.global.activity.ActivityMgr;
import business.global.mail.MailCenter;
import business.global.rank.RankManager;
import business.global.refresh.RefreshMgr;
import business.gmcmd.cmd.CommandMgr;
import com.sun.net.httpserver.HttpServer;
import com.zhonglian.server.common.IApp;
import com.zhonglian.server.common.db.DBCons;
import com.zhonglian.server.common.db.DatabaseFactory;
import com.zhonglian.server.common.db.mgr.Game_DBVersionManager;
import com.zhonglian.server.common.db.mgr.Log_DBVersionManager;
import com.zhonglian.server.http.server.HttpDispather;
import com.zhonglian.server.http.server.MGHttpServer;
import com.zhonglian.server.logger.flow.db.DBFlowMgr;
import core.config.refdata.RefDataMgr;
import core.logger.flow.FlowLogger;
import core.logger.flow.WorldFlowLogger;
import core.network.game2world.GameAcceptor;
import core.network.httpserver.handler.Sample;

public class WorldServer extends IApp {
    public static void main(String[] args) throws EvalError {
        WorldServer app = new WorldServer();
        app.init(args);
        app.start();
    }

    @Override
    protected void beforeInit(String configdit) {
        this.loadBasicConfig(configdit, "logback_world.xml");
        this.loadRemoteConfig("server_id", System.getProperty("world_sid"), configdit, "world.properties");
    }

    @Override
    protected boolean initBase() {
        ConsoleTaskManager.GetInstance().setRunner(new BshRunner());
        RefDataMgr.getInstance().reload();
        RefDataMgr.getInstance().isAssertPassed();
        DBCons.setDBFactory(new DatabaseFactory(ServerConfig.DBUrl(), ServerConfig.DBUser(), ServerConfig.DBPassword()));
        DBCons.setLogDBFactory(new DatabaseFactory(ServerConfig.LogDBUrl(), ServerConfig.LogDBUser(), ServerConfig.LogDBPassword()));
        Game_DBVersionManager.getInstance().checkAndUpdateVersion("core.database.world.bo");
        DBFlowMgr.getInstance().updateDB("core.logger.flow.impl.db");
        Game_DBVersionManager.getInstance().setNewestVersion("1.0.0.1");
        Log_DBVersionManager.getInstance().setNewestVersion("1.0.0.1");
        if (Game_DBVersionManager.getInstance().runAutoVersionUpdate("core.database.world.update")
            && Log_DBVersionManager.getInstance().runAutoVersionUpdate("core.database.worldlog.update")) {
            FlowLogger.getInstance().init(WorldFlowLogger.class, "core.logger.flow.impl");
            DBFlowMgr.getInstance().start();
            return true;
        } else {
            CommLog.error("!!!!!!!!!数据库升级失败!!!!!!!!!!");
            return false;
        }
    }

    @Override
    protected boolean startNet() {
        GameAcceptor.getInstance().startSocket(Integer.getInteger("WorldServer.GameServer_Port"));
        this.starHttp();
        return true;
    }

    public void starHttp() {
        try {
            HttpDispather dispather = new HttpDispather();
            String pack = Sample.class.getPackage().getName();
            dispather.init(pack);
            HttpServer httpServer = MGHttpServer.createServer(Integer.getInteger("Server.HttpServer", 8888), dispather, "/");
            httpServer.start();
        } catch (Exception var4) {
            CommLog.error("启动Http错误", (Throwable)var4);
            System.exit(-1);
        }
    }

    @Override
    protected boolean initLogic() {
        MailCenter.getInstance().init();
        CommandMgr.getInstance().init();
        RankManager.getInstance().init();
        ActivityMgr.getInstance().init();
        return true;
    }

    @Override
    protected void afterInit() {
        RefreshMgr.getInstance().init();
    }

    @Override
    protected void start() {
    }
}
