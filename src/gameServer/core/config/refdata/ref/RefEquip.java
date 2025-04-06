/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import business.player.item.ItemUtils;
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ import com.zhonglian.server.common.enums.EquipPos;
/*    */ import com.zhonglian.server.common.enums.EquipType;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import com.zhonglian.server.common.enums.Quality;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefEquip
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public String Name;
/*    */   public int CharID;
/*    */   public EquipType Type;
/*    */   public int Level;
/*    */   public List<Attribute> AttrTypeList;
/*    */   public List<Integer> AttrValueList;
/*    */   @RefField(isfield = false)
/*    */   private Quality quality;
/*    */   @RefField(isfield = false)
/*    */   private List<EquipPos> equipPos;
/*    */   @RefField(isfield = false)
/* 38 */   public static Map<String, RefEquip> redEquip = new HashMap<>();
/*    */   
/*    */   public boolean couldEquipOn(EquipPos p) {
/* 41 */     return validEquipPos().contains(p);
/*    */   }
/*    */   
/*    */   public Quality getQuality() {
/* 45 */     if (this.quality == null) {
/* 46 */       int uniformid = ItemUtils.getUniformId(PrizeType.Equip, this.id);
/* 47 */       int q = ((RefUniformItem)RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformid))).Quality;
/* 48 */       this.quality = Quality.values()[q];
/*    */     } 
/* 50 */     return this.quality;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 55 */     if (!RefAssert.inRef(Integer.valueOf(this.CharID), RefCharacter.class, new Object[0])) {
/* 56 */       return false;
/*    */     }
/* 58 */     if (!RefAssert.listSize(this.AttrTypeList, this.AttrValueList, new List[0])) {
/* 59 */       return false;
/*    */     }
/*    */     
/* 62 */     if (getQuality() == Quality.Red) {
/* 63 */       String key = String.valueOf(this.CharID) + this.Type.toString() + this.Level;
/* 64 */       redEquip.put(key, this);
/*    */     } 
/*    */     
/* 67 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 72 */     return true;
/*    */   }
/*    */   
/*    */   public List<EquipPos> validEquipPos() {
/* 76 */     if (this.equipPos == null) {
/* 77 */       this.equipPos = new ArrayList<>(); byte b; int i; EquipPos[] arrayOfEquipPos;
/* 78 */       for (i = (arrayOfEquipPos = EquipPos.values()).length, b = 0; b < i; ) { EquipPos pos = arrayOfEquipPos[b];
/* 79 */         if (pos.toString().startsWith(this.Type.name()))
/* 80 */           this.equipPos.add(pos);  b++; }
/*    */     
/*    */     } 
/* 83 */     return this.equipPos;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefEquip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */