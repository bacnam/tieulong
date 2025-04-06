/*    */ package core.network.proto;
/*    */ 
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import java.util.List;
/*    */ 
/*    */ public class NoticeInfo
/*    */ {
/*    */   ConstEnum.UniverseMessageType type;
/*    */   List<String> content;
/*    */   
/*    */   public NoticeInfo(ConstEnum.UniverseMessageType type, List<String> content) {
/* 12 */     this.type = type;
/* 13 */     this.content = content;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/NoticeInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */