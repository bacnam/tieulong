/*    */ package business.gmcmd.cmd;
/*    */ 
/*    */ import BaseCommon.CommClass;
/*    */ import BaseCommon.CommLog;
/*    */ import business.gmcmd.annotation.Command;
/*    */ import business.gmcmd.annotation.Commander;
/*    */ import business.player.Player;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Arrays;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommandMgr
/*    */ {
/* 21 */   private Map<String, Commander> commanders = new HashMap<>();
/*    */   
/* 23 */   private static CommandMgr _instance = new CommandMgr();
/*    */   
/*    */   public static CommandMgr getInstance() {
/* 26 */     return _instance;
/*    */   }
/*    */   
/*    */   public void init() {
/* 30 */     Set<Class<?>> clazzs = CommClass.getClasses("business.gmcmd.cmds");
/* 31 */     for (Class<?> clazz : clazzs) {
/* 32 */       Commander aCommander = clazz.<Commander>getAnnotation(Commander.class);
/* 33 */       if (aCommander == null) {
/*    */         continue;
/*    */       }
/* 36 */       Object excuter = null;
/*    */       try {
/* 38 */         excuter = clazz.newInstance();
/* 39 */       } catch (Exception e) {
/*    */         continue;
/*    */       } 
/* 42 */       Commander commander = new Commander(aCommander); byte b; int i; Method[] arrayOfMethod;
/* 43 */       for (i = (arrayOfMethod = clazz.getMethods()).length, b = 0; b < i; ) { Method m = arrayOfMethod[b];
/* 44 */         Command aCommand = m.<Command>getAnnotation(Command.class);
/* 45 */         if (aCommand != null) {
/*    */ 
/*    */           
/* 48 */           Command command = new Command(aCommand, m, excuter);
/* 49 */           commander.addCommand(command);
/*    */         }  b++; }
/* 51 */        this.commanders.put(commander.getName().toLowerCase(), commander);
/*    */     } 
/*    */   }
/*    */   
/*    */   public String run(Player player, String cmdline) {
/* 56 */     if (cmdline == null) {
/* 57 */       return "null command";
/*    */     }
/* 59 */     cmdline = cmdline.trim();
/* 60 */     if (cmdline.isEmpty() || cmdline.equalsIgnoreCase("help")) {
/* 61 */       return getHelp();
/*    */     }
/* 63 */     String[] args = cmdline.split("\\s+");
/* 64 */     Commander commander = this.commanders.get(args[0].toLowerCase());
/* 65 */     if (commander == null) {
/* 66 */       return "commander[" + args[0] + "] not found";
/*    */     }
/* 68 */     if (args.length < 2) {
/* 69 */       return commander.getHelp();
/*    */     }
/* 71 */     String cmd = args[1].toLowerCase();
/* 72 */     if (cmd.equalsIgnoreCase("help")) {
/* 73 */       return commander.getHelp();
/*    */     }
/*    */     try {
/* 76 */       return commander.run(player, cmd, Arrays.<String>copyOfRange(args, 2, args.length));
/* 77 */     } catch (Exception e) {
/* 78 */       CommLog.warn("error occurs while running command:{}", cmdline, e);
/* 79 */       return "error occurs while running command";
/*    */     } 
/*    */   }
/*    */   
/*    */   private String getHelp() {
/* 84 */     StringBuilder sBuilder = new StringBuilder("");
/* 85 */     for (Commander commander : this.commanders.values()) {
/* 86 */       sBuilder.append(commander.toString()).append("\n");
/*    */     }
/* 88 */     return sBuilder.toString();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/gmcmd/cmd/CommandMgr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */