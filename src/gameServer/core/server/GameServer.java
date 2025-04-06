/*     */ package core.server;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import BaseServer.Monitor;
/*     */ import ConsoleTask.ConsoleTaskManager;
/*     */ import bsh.EvalError;
/*     */ import business.global.activity.ActivityMgr;
/*     */ import business.global.arena.ArenaManager;
/*     */ import business.global.confreward.RewardConfMgr;
/*     */ import business.global.gmmail.MailCenter;
/*     */ import business.global.gmmail.TimerMailMgr;
/*     */ import business.global.guild.GuildMgr;
/*     */ import business.global.guild.GuildWarMgr;
/*     */ import business.global.notice.NoticeMgr;
/*     */ import business.global.rank.RankManager;
/*     */ import business.global.recharge.RechargeMgr;
/*     */ import business.global.redpacket.RedPacketMgr;
/*     */ import business.global.refresh.RefreshMgr;
/*     */ import business.global.worldboss.WorldBossMgr;
/*     */ import business.gmcmd.cmd.CommandMgr;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.RobotManager;
/*     */ import com.sun.net.httpserver.HttpServer;
/*     */ import com.zhonglian.server.common.HeartBeat;
/*     */ import com.zhonglian.server.common.IApp;
/*     */ import com.zhonglian.server.common.db.DBCons;
/*     */ import com.zhonglian.server.common.db.DatabaseFactory;
/*     */ import com.zhonglian.server.common.db.IDBConnectionFactory;
/*     */ import com.zhonglian.server.common.db.mgr.Game_DBVersionManager;
/*     */ import com.zhonglian.server.common.db.mgr.Log_DBVersionManager;
/*     */ import com.zhonglian.server.common.mgr.sensitive.SensitiveWordMgr;
/*     */ import com.zhonglian.server.http.server.HttpDispather;
/*     */ import com.zhonglian.server.http.server.MGHttpServer;
/*     */ import com.zhonglian.server.logger.flow.db.DBFlowMgr;
/*     */ import com.zhonglian.server.logger.flow.redis.RedisFlowMgr;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.logger.flow.FlowLogger;
/*     */ import core.logger.flow.GameFlowLogger;
/*     */ import core.network.client2game.ClientAcceptor;
/*     */ import core.network.game2world.WorldConnector;
/*     */ import core.network.game2zone.ZoneConnector;
/*     */ import core.network.http.handler.Recharge;
/*     */ import java.io.File;
/*     */ 
/*     */ public class GameServer
/*     */   extends IApp
/*     */ {
/*     */   public static void main(String[] args) throws EvalError {
/*  49 */     GameServer app = new GameServer();
/*  50 */     app.init(args);
/*  51 */     app.start();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void beforeInit(String configdit) {
/*  56 */     loadBasicConfig(configdit, "logback_game.xml");
/*  57 */     loadRemoteConfig("server_id", System.getProperty("game_sid"), configdit, "game.properties");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean initBase() {
/*  64 */     ConsoleTaskManager.GetInstance().setRunner(new BshRunner());
/*     */     
/*  66 */     RefDataMgr.getInstance().reload();
/*     */ 
/*     */ 
/*     */     
/*  70 */     ServerConfig.checkMapPath();
/*     */     
/*  72 */     SensitiveWordMgr.getInstance().init(String.valueOf(RefDataMgr.getInstance().getRefPath()) + File.separator + "keywords.txt");
/*  73 */     SensitiveWordMgr.getInstance().reload();
/*     */ 
/*     */     
/*  76 */     DBCons.setDBFactory((IDBConnectionFactory)new DatabaseFactory(ServerConfig.DBUrl(), ServerConfig.DBUser(), ServerConfig.DBPassword()));
/*  77 */     DBCons.setLogDBFactory((IDBConnectionFactory)new DatabaseFactory(ServerConfig.LogDBUrl(), ServerConfig.LogDBUser(), ServerConfig.LogDBPassword()));
/*     */     
/*  79 */     Game_DBVersionManager.getInstance().checkAndUpdateVersion("core.database.game.bo");
/*  80 */     DBFlowMgr.getInstance().updateDB("core.logger.flow.impl.db");
/*     */ 
/*     */     
/*  83 */     Game_DBVersionManager.getInstance().setNewestVersion("1.0.0.1");
/*  84 */     Log_DBVersionManager.getInstance().setNewestVersion("1.0.0.1");
/*     */     
/*  86 */     if (!Game_DBVersionManager.getInstance().runAutoVersionUpdate("core.database.game.update") || 
/*  87 */       !Log_DBVersionManager.getInstance().runAutoVersionUpdate("core.database.log.update")) {
/*  88 */       CommLog.error("!!!!!!!!! 数据库版本升级失败 !!!!!!!!!!");
/*  89 */       return false;
/*     */     } 
/*     */ 
/*     */     
/*  93 */     FlowLogger.getInstance().init(GameFlowLogger.class, "core.logger.flow.impl");
/*  94 */     DBFlowMgr.getInstance().start();
/*  95 */     RedisFlowMgr.getInstance().start();
/*     */ 
/*     */     
/*  98 */     OpenSeverTime.getInstance().init();
/*     */     
/* 100 */     Monitor.getInstance().regCleanMemory(CleanMemory.GetInstance());
/* 101 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean startNet() {
/* 106 */     ClientAcceptor.getInstance().startSocket(Integer.getInteger("GameServer.ClientPort"));
/* 107 */     if ("on".equalsIgnoreCase(System.getProperty("GameServer.ZoneOpen"))) {
/* 108 */       ZoneConnector.getInstance().reconnect(System.getProperty("GameServer.Zone_IP", "127.0.0.1"), Integer.getInteger("GameServer.Zone_Port", 8002).intValue());
/*     */     }
/* 110 */     if ("on".equalsIgnoreCase(System.getProperty("GameServer.WorldOpen"))) {
/* 111 */       WorldConnector.getInstance().reconnect(System.getProperty("GameServer.World_IP", "127.0.0.1"), Integer.getInteger("GameServer.World_Port", 8003).intValue());
/*     */     }
/* 113 */     starHttp();
/* 114 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean initLogic() {
/* 120 */     DynamicConfig.init();
/*     */     
/* 122 */     RechargeMgr.getInstance().init();
/*     */     
/* 124 */     RankManager.getInstance().init();
/*     */     
/* 126 */     CommandMgr.getInstance().init();
/*     */     
/* 128 */     PlayerMgr.getInstance().init();
/*     */     
/* 130 */     RobotManager.getInstance().init();
/*     */     
/* 132 */     MailCenter.getInstance().init();
/*     */     
/* 134 */     TimerMailMgr.getInstance().init();
/*     */     
/* 136 */     RewardConfMgr.getInstance().init();
/*     */     
/* 138 */     NoticeMgr.getInstance().init();
/*     */     
/* 140 */     ArenaManager.getInstance().init();
/*     */     
/* 142 */     WorldBossMgr.getInstance().init();
/*     */     
/* 144 */     ActivityMgr.getInstance().init();
/*     */     
/* 146 */     GuildMgr.getInstance().init();
/*     */     
/* 148 */     GuildWarMgr.getInstance().init();
/*     */     
/* 150 */     RedPacketMgr.getInstance().init();
/*     */     
/* 152 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void afterInit() {
/* 158 */     RefreshMgr.getInstance().init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void start() {
/* 165 */     HeartBeat.start();
/*     */   }
/*     */   
/*     */   public void starHttp() {
/*     */     try {
/* 170 */       HttpDispather dispather = new HttpDispather();
/* 171 */       String pack = Recharge.class.getPackage().getName();
/* 172 */       dispather.init(pack);
/* 173 */       HttpServer httpServer = MGHttpServer.createServer(Integer.getInteger("Server.HttpServer", 9888).intValue(), dispather, "/");
/* 174 */       httpServer.start();
/* 175 */     } catch (Exception e) {
/* 176 */       CommLog.error("启动Http错误", e);
/* 177 */       System.exit(-1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void reload() {
/* 182 */     RefDataMgr.getInstance().reload();
/* 183 */     SensitiveWordMgr.getInstance().reload();
/* 184 */     RefreshMgr.getInstance().reload();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/server/GameServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */