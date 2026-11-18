package core.network.proto;

import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefGoodsInfo;
import core.database.game.bo.PlayerGoodsBO;

public class Store {
    public static class Goods {
        public long sid;
        public int storeType;
        public long goodsId;
        public int count;
        public int leftBuyTimes;
        public int totalBuyTimes;
        public int uniformId;
        public int costItem;
        public Double price;
        public Double discount;
        public int discountType;
        public int timeLeft;
        public boolean isSoldout;

        public Goods(PlayerGoodsBO bo) {
            this.sid = bo.getId();
            this.storeType = bo.getStoreType();
            this.goodsId = bo.getGoodsId();
            this.count = bo.getAmount();
            this.leftBuyTimes = bo.getTotalBuyTimes() - bo.getBuyTimes();
            this.totalBuyTimes = bo.getTotalBuyTimes();
            this.uniformId = RefDataMgr.get(RefGoodsInfo.class, this.goodsId).UniformID;
            this.costItem = Integer.valueOf(bo.getCostUniformId());
            this.price = Double.valueOf(bo.getPrice()) * bo.getAmount();
            this.discount = Double.valueOf(bo.getDiscount()) * bo.getAmount();
            this.isSoldout = bo.getSoldout();
        }
    }
}
