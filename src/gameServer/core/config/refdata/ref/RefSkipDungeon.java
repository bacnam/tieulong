/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.utils.Maps;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefSkipDungeon
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public int Level;
/*    */   public int Cost;
/* 19 */   public static Map<Integer, RefSkipDungeon> LevelMap = Maps.newConcurrentHashMap();
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 24 */     LevelMap.put(Integer.valueOf(this.Level), this);
/*    */     
/* 26 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 31 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefSkipDungeon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */