/*    */ package proto.gameworld;
/*    */ 
/*    */ import com.zhonglian.server.common.data.ref.matcher.NumberRange;
/*    */ import com.zhonglian.server.common.utils.Lists;
/*    */ import com.zhonglian.server.common.utils.StringUtils;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RankAward
/*    */ {
/*    */   public int aid;
/*    */   public NumberRange rankrange;
/*    */   public ArrayList<UniformItemInfo> reward;
/*    */   
/*    */   public String uniformItemIds() {
/* 22 */     List<Integer> list = Lists.newArrayList();
/* 23 */     for (UniformItemInfo uniform : this.reward) {
/* 24 */       list.add(Integer.valueOf(uniform.uniformId));
/*    */     }
/* 26 */     return StringUtils.list2String(list);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String uniformItemCounts() {
/* 35 */     List<Integer> list = Lists.newArrayList();
/* 36 */     for (UniformItemInfo uniform : this.reward) {
/* 37 */       list.add(Integer.valueOf(uniform.count));
/*    */     }
/* 39 */     return StringUtils.list2String(list);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/proto/gameworld/RankAward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */