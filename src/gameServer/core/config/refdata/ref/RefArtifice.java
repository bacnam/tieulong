/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ import com.zhonglian.server.common.utils.Maps;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefArtifice
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public int Stars;
/*    */   public int Level;
/*    */   public List<Integer> UniformId;
/*    */   public List<Integer> UniformCount;
/*    */   public int Rate;
/*    */   public int Star;
/*    */   public int ATK;
/*    */   public int MaxHP;
/*    */   public int DEF;
/*    */   public int RGS;
/*    */   public int Hit;
/*    */   public int Dodge;
/*    */   public int Critical;
/*    */   public int Tenacity;
/*    */   public int Timemin;
/*    */   public int TimeMax;
/* 36 */   public static Map<Integer, RefArtifice> maxLevelMap = Maps.newConcurrentHashMap();
/*    */   
/*    */   public int getValue(Attribute attr) {
/* 39 */     switch (attr) {
/*    */       case null:
/* 41 */         return this.ATK;
/*    */       case MaxHP:
/* 43 */         return this.MaxHP;
/*    */       case DEF:
/* 45 */         return this.DEF;
/*    */       case RGS:
/* 47 */         return this.RGS;
/*    */       case Hit:
/* 49 */         return this.Hit;
/*    */       case Dodge:
/* 51 */         return this.Dodge;
/*    */       case Critical:
/* 53 */         return this.Critical;
/*    */       case Tenacity:
/* 55 */         return this.Tenacity;
/*    */     } 
/* 57 */     return 0;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 63 */     RefArtifice ref = maxLevelMap.get(Integer.valueOf(this.Level));
/*    */     
/* 65 */     if (ref == null) {
/* 66 */       maxLevelMap.put(Integer.valueOf(this.Level), this);
/* 67 */     } else if (ref != null && ref.Stars < this.Stars) {
/* 68 */       maxLevelMap.put(Integer.valueOf(this.Level), this);
/*    */     } 
/*    */     
/* 71 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 76 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefArtifice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */