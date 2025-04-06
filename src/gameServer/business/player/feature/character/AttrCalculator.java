/*    */ package business.player.feature.character;
/*    */ 
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public abstract class AttrCalculator
/*    */ {
/* 11 */   protected Map<Attribute, Integer> allAttr = new HashMap<>();
/* 12 */   protected int power = 0;
/*    */   protected boolean updated = false;
/*    */   
/*    */   protected void addAttr(Attribute attr, int value) {
/* 16 */     this.allAttr.put(attr, Integer.valueOf(((Integer)this.allAttr.get(attr)).intValue() + value));
/*    */   }
/*    */   
/*    */   protected void addAttr(List<Attribute> attr, List<Integer> value) {
/* 20 */     for (int i = 0; i < attr.size(); i++) {
/* 21 */       addAttr(attr.get(i), ((Integer)value.get(i)).intValue());
/*    */     }
/*    */   }
/*    */   
/*    */   protected abstract void doUpdate();
/*    */   
/*    */   public void update() {
/* 28 */     if (this.updated) {
/*    */       return;
/*    */     }
/* 31 */     this.allAttr.clear();
/* 32 */     doUpdate();
/* 33 */     this.updated = true;
/*    */   }
/*    */   
/*    */   public int getPower() {
/* 37 */     update();
/* 38 */     return this.power;
/*    */   }
/*    */   
/*    */   public Map<Attribute, Integer> getAttrs() {
/* 42 */     update();
/* 43 */     return this.allAttr;
/*    */   }
/*    */   
/*    */   public Integer getAttr(Attribute attr) {
/* 47 */     update();
/* 48 */     Integer value = this.allAttr.get(attr);
/* 49 */     return Integer.valueOf((value == null) ? 0 : value.intValue());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/character/AttrCalculator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */