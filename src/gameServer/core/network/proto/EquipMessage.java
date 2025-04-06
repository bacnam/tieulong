/*    */ package core.network.proto;
/*    */ 
/*    */ import com.zhonglian.server.common.enums.EquipPos;
/*    */ import core.database.game.bo.EquipBO;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class EquipMessage
/*    */ {
/*    */   int equipId;
/*    */   long sid;
/*    */   List<Integer> attrs;
/*    */   int charId;
/*    */   EquipPos pos;
/*    */   
/*    */   public EquipMessage(EquipBO bo) {
/* 17 */     this.equipId = bo.getEquipId();
/* 18 */     this.sid = bo.getId();
/* 19 */     this.attrs = bo.getAttrAll();
/* 20 */     this.pos = EquipPos.values()[bo.getPos()];
/* 21 */     this.charId = bo.getCharId();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/EquipMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */