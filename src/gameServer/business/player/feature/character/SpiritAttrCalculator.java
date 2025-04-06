/*    */ package business.player.feature.character;
/*    */ 
/*    */ import business.player.Player;
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefSkill;
/*    */ import core.config.refdata.ref.RefWarSpirit;
/*    */ import core.config.refdata.ref.RefWarSpiritLv;
/*    */ import core.config.refdata.ref.RefWarSpiritStar;
/*    */ import core.config.refdata.ref.RefWarSpiritTalent;
/*    */ import core.database.game.bo.WarSpiritBO;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class SpiritAttrCalculator extends AttrCalculator {
/*    */   private WarSpirit spirit;
/*    */   private WarSpiritBO bo;
/*    */   private Player player;
/*    */   
/*    */   public SpiritAttrCalculator(WarSpirit spirit) {
/* 20 */     this.spirit = spirit;
/* 21 */     this.player = this.spirit.player;
/* 22 */     this.spirit = spirit;
/* 23 */     this.bo = spirit.getBo();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doUpdate() {
/* 28 */     this.allAttr.put(Attribute.MaxHP, Integer.valueOf(0));
/* 29 */     this.allAttr.put(Attribute.ATK, Integer.valueOf(0));
/* 30 */     this.allAttr.put(Attribute.DEF, Integer.valueOf(0));
/* 31 */     this.allAttr.put(Attribute.RGS, Integer.valueOf(0));
/* 32 */     this.allAttr.put(Attribute.Hit, Integer.valueOf(0));
/* 33 */     this.allAttr.put(Attribute.Dodge, Integer.valueOf(0));
/* 34 */     this.allAttr.put(Attribute.Critical, Integer.valueOf(0));
/* 35 */     this.allAttr.put(Attribute.Tenacity, Integer.valueOf(0));
/*    */ 
/*    */     
/* 38 */     int lv = this.player.getPlayerBO().getWarspiritLv();
/* 39 */     RefWarSpiritLv refLv = (RefWarSpiritLv)RefDataMgr.get(RefWarSpiritLv.class, Integer.valueOf(lv));
/* 40 */     addAttr(refLv.AttrTypeList, refLv.AttrValueList);
/*    */ 
/*    */     
/* 43 */     int telent = this.player.getPlayerBO().getWarspiritTalent();
/* 44 */     RefWarSpiritTalent refTalent = (RefWarSpiritTalent)RefDataMgr.get(RefWarSpiritTalent.class, Integer.valueOf(telent));
/* 45 */     addAttr(refTalent.AttrTypeList, refTalent.AttrValueList);
/*    */ 
/*    */     
/* 48 */     calcStar();
/*    */     
/* 50 */     this.power = PowerUtils.getPower(this.allAttr);
/*    */     
/* 52 */     RefWarSpirit ref = (RefWarSpirit)RefDataMgr.get(RefWarSpirit.class, Integer.valueOf(this.bo.getSpiritId()));
/* 53 */     for (int i = 0; i < ref.SkillList.size(); i++) {
/* 54 */       int skilllv = 0;
/* 55 */       if (((Integer)ref.SkillList.get(i)).intValue() != 0) {
/* 56 */         skilllv = this.bo.getSkill();
/*    */       }
/* 58 */       RefSkill skill = (RefSkill)RefDataMgr.get(RefSkill.class, ref.SkillList.get(i));
/* 59 */       this.power += skill.CE * (skilllv + 1);
/*    */     } 
/*    */   }
/*    */   
/*    */   private void calcStar() {
/* 64 */     RefWarSpiritStar ref = (RefWarSpiritStar)((Map)RefWarSpiritStar.spiritMap.get(Integer.valueOf(this.bo.getSpiritId()))).get(Integer.valueOf(this.bo.getStar()));
/* 65 */     addAttr(ref.AttrTypeList, ref.AttrValueList);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/character/SpiritAttrCalculator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */