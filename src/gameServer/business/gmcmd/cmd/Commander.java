/*    */ package business.gmcmd.cmd;
/*    */ 
/*    */ import business.player.Player;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class Commander
/*    */ {
/*    */   private String name;
/*    */   private String comment;
/*    */   private Map<String, Command> commands;
/*    */   
/*    */   public Commander(business.gmcmd.annotation.Commander annotation) {
/* 14 */     this.name = annotation.name();
/* 15 */     this.comment = annotation.comment();
/* 16 */     this.commands = new HashMap<>();
/*    */   }
/*    */   
/*    */   public String getName() {
/* 20 */     return this.name;
/*    */   }
/*    */   
/*    */   public String run(Player player, String cmdname, String[] args) throws Exception {
/* 24 */     Command command = this.commands.get(cmdname);
/* 25 */     if (command == null) {
/* 26 */       return "command[" + cmdname + "] not found in commander[" + this.name + "]";
/*    */     }
/* 28 */     return command.run(player, args);
/*    */   }
/*    */   
/*    */   public String getHelp() {
/* 32 */     StringBuilder sBuilder = new StringBuilder("");
/* 33 */     for (Command command : this.commands.values()) {
/* 34 */       sBuilder.append(command.toString()).append("\n");
/*    */     }
/* 36 */     return sBuilder.toString();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 40 */     return String.valueOf(this.name) + " : " + this.comment;
/*    */   }
/*    */   
/*    */   public void addCommand(Command command) {
/* 44 */     this.commands.put(command.getName(), command);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/gmcmd/cmd/Commander.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */