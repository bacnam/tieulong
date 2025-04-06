/*    */ package core.network.client2game.handler.chars;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerItem;
/*    */ import business.player.feature.character.WarSpirit;
/*    */ import business.player.feature.character.WarSpiritFeature;
/*    */ import business.player.item.UniformItem;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefWarSpirit;
/*    */ import core.config.refdata.ref.RefWarSpiritStar;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class WarSpiritStar
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int spiritId;
/*    */   }
/*    */   
/*    */   private static class Response {
/*    */     int spiritId;
/*    */     int star;
/*    */     
/*    */     private Response(int spiritId, int star) {
/* 35 */       this.spiritId = spiritId;
/* 36 */       this.star = star;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 43 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 44 */     WarSpirit warspirit = ((WarSpiritFeature)player.getFeature(WarSpiritFeature.class)).getWarSpirit(req.spiritId);
/* 45 */     if (warspirit == null) {
/* 46 */       throw new WSException(ErrorCode.Char_NotFound, "战灵[%s]不存在或未解锁", new Object[] { Integer.valueOf(req.spiritId) });
/*    */     }
/* 48 */     int star = warspirit.getBo().getStar();
/* 49 */     if (star >= ((Map)RefWarSpiritStar.spiritMap.get(Integer.valueOf(req.spiritId))).size() - 1) {
/* 50 */       throw new WSException(ErrorCode.WarSpiritStarFull, "战灵[%s]星级已满", new Object[] { Integer.valueOf(req.spiritId) });
/*    */     }
/*    */     
/* 53 */     RefWarSpirit ref = (RefWarSpirit)RefDataMgr.get(RefWarSpirit.class, Integer.valueOf(warspirit.getSpiritId()));
/* 54 */     int costId = ref.StarMaterial;
/* 55 */     RefWarSpiritStar refstar = (RefWarSpiritStar)((Map)RefWarSpiritStar.spiritMap.get(Integer.valueOf(req.spiritId))).get(Integer.valueOf(star + 1));
/* 56 */     if (refstar.NeedLv > player.getPlayerBO().getWarspiritLv()) {
/* 57 */       throw new WSException(ErrorCode.WarSpiritLevelRequire, "战灵[%s]等级[%s]不足[%s]", new Object[] { Integer.valueOf(req.spiritId), Integer.valueOf(player.getPlayerBO().getWarspiritLv()), Integer.valueOf(refstar.NeedLv) });
/*    */     }
/*    */     
/* 60 */     int costCount = refstar.WarspiritNum;
/* 61 */     UniformItem costItem = new UniformItem(costId, costCount);
/* 62 */     UniformItem extraItem = new UniformItem(refstar.ExtraId, refstar.ExtraCount);
/* 63 */     List<UniformItem> list = new ArrayList<>();
/* 64 */     list.add(costItem);
/* 65 */     list.add(extraItem);
/* 66 */     if (!((PlayerItem)player.getFeature(PlayerItem.class)).check(list)) {
/* 67 */       throw new WSException(ErrorCode.NotEnough_Currency, "材料不足");
/*    */     }
/* 69 */     ((PlayerItem)player.getFeature(PlayerItem.class)).consume(list, ItemFlow.WarSpiritStar);
/* 70 */     warspirit.getBo().saveStar(star + 1);
/*    */     
/* 72 */     warspirit.onAttrChanged();
/* 73 */     request.response(new Response(req.spiritId, warspirit.getBo().getStar(), null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/chars/WarSpiritStar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */