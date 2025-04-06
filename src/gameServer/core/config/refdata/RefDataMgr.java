/*    */ package core.config.refdata;
/*    */ 
/*    */ import com.zhonglian.server.common.data.AbstractRefDataMgr;
/*    */ import core.config.refdata.ref.RefBaseGame;
/*    */ import java.io.File;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefDataMgr
/*    */   extends AbstractRefDataMgr
/*    */ {
/* 22 */   private static final String Defaule_RefData_Path = "data" + File.separatorChar + "refData";
/* 23 */   private String _refPath = System.getProperty("GameServer.RefPath", Defaule_RefData_Path);
/*    */   
/* 25 */   private static RefDataMgr _instance = new RefDataMgr();
/*    */   
/*    */   public static RefDataMgr getInstance() {
/* 28 */     return _instance;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onCustomLoad() {
/* 33 */     load(RefBaseGame.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRefPath() {
/* 38 */     return this._refPath;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean assertAll() {
/* 43 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/RefDataMgr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */