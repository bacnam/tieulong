/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.data.ref.matcher.NumberRange;
/*    */ import com.zhonglian.server.common.enums.EquipPos;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefRobotCharacter
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public int CharId;
/*    */   public NumberRange Wing;
/*    */   public List<Integer> Strengthen;
/*    */   public List<Integer> Equip;
/*    */   
/*    */   public boolean Assert() {
/* 30 */     if (this.Strengthen.size() >= (EquipPos.values()).length) {
/* 31 */       CommLog.error("角色的强化部位只有{}个，实际配置{}个", Integer.valueOf((EquipPos.values()).length - 1), Integer.valueOf(this.Strengthen.size()));
/* 32 */       return false;
/*    */     } 
/* 34 */     if (!RefAssert.inRef(this.Equip, RefEquip.class, new Object[] { Integer.valueOf(0) })) {
/* 35 */       return false;
/*    */     }
/* 37 */     if (!RefAssert.inRef(Integer.valueOf(this.CharId), RefCharacter.class, new Object[0])) {
/* 38 */       return false;
/*    */     }
/* 40 */     if (this.Wing.getTop() < this.Wing.getLow() || this.Wing.getTop() >= RefDataMgr.size(RefWing.class)) {
/* 41 */       CommLog.error("角色的翅膀等级上下限配置错误，或者超出最大翅膀等级");
/* 42 */       return false;
/*    */     } 
/* 44 */     if (this.Equip.size() >= (EquipPos.values()).length) {
/* 45 */       CommLog.error("角色的装备部位只有{}个，实际配置{}个", Integer.valueOf((EquipPos.values()).length - 1), Integer.valueOf(this.Equip.size()));
/* 46 */       return false;
/*    */     } 
/* 48 */     boolean rtn = true;
/* 49 */     for (int i = 0; i < this.Equip.size(); i++) {
/* 50 */       if (((Integer)this.Equip.get(i)).intValue() != 0) {
/*    */ 
/*    */         
/* 53 */         RefEquip equip = (RefEquip)RefDataMgr.get(RefEquip.class, this.Equip.get(i));
/* 54 */         EquipPos pos = EquipPos.values()[i + 1];
/* 55 */         if (!equip.couldEquipOn(pos)) {
/* 56 */           CommLog.error("装备{}不能装备在第{}个位置{}上", new Object[] { Integer.valueOf(equip.id), Integer.valueOf(i), pos });
/* 57 */           rtn = false;
/*    */         } 
/* 59 */         if (equip.CharID != this.CharId) {
/* 60 */           CommLog.error("装备{}不能装备在角色{}上", Integer.valueOf(equip.id), Integer.valueOf(this.CharId));
/* 61 */           rtn = false;
/*    */         } 
/*    */       } 
/* 64 */     }  return rtn;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 69 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefRobotCharacter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */