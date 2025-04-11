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
            this.uniformId = ((RefGoodsInfo) RefDataMgr.get(RefGoodsInfo.class, Long.valueOf(this.goodsId))).UniformID;
            this.costItem = Integer.valueOf(bo.getCostUniformId()).intValue();
            this.price = Double.valueOf(Double.valueOf(bo.getPrice()).doubleValue() * bo.getAmount());
            this.discount = Double.valueOf(Double.valueOf(bo.getDiscount()).doubleValue() * bo.getAmount());
            this.isSoldout = bo.getSoldout();
        }
    }
}

