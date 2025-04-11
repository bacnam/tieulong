package core.network.proto;

import com.zhonglian.server.common.enums.StoreType;

public class StoreRefresh {
    StoreType storeType;
    int nextRefreshTime;
    int remainsec;
    int freeRefreshTimes;
    int paidRefreshTimes;

    public StoreType getStoreType() {
        return this.storeType;
    }

    public void setStoreType(StoreType storeType) {
        this.storeType = storeType;
    }

    public int getNextRefreshTime() {
        return this.nextRefreshTime;
    }

    public void setNextRefreshTime(int nextRefreshTime) {
        this.nextRefreshTime = nextRefreshTime;
    }

    public int getRemainsec() {
        return this.remainsec;
    }

    public void setRemainsec(int remainsec) {
        this.remainsec = remainsec;
    }

    public int getFreeRefreshTimes() {
        return this.freeRefreshTimes;
    }

    public void setFreeRefreshTimes(int freeRefreshTimes) {
        this.freeRefreshTimes = freeRefreshTimes;
    }

    public int getPaidRefreshTimes() {
        return this.paidRefreshTimes;
    }

    public void setPaidRefreshTimes(int paidRefreshTimes) {
        this.paidRefreshTimes = paidRefreshTimes;
    }
}

