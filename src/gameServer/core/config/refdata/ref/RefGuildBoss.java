/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.common.utils.Maps;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public class RefGuildBoss
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public String Name;
/*    */   public int NeedGuildLevel;
/*    */   public int UniformId;
/*    */   public int UniformCount;
/*    */   public int MailId;
/*    */   public int MonsterId;
/*    */   @RefField(isfield = false)
/* 22 */   public static Map<ConstEnum.BuffType, RefGuildBoss> buffMap = Maps.newMap();
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 26 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 31 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefGuildBoss.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */