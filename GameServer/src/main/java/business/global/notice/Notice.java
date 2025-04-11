package business.global.notice;

public class Notice {
    public long id;
    public String content;
    public int clientType;
    public int beginTime;
    public int endTime;
    public int interval;
    public int lastSendTime;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getClientType() {
        return this.clientType;
    }

    public void setClientType(int clientType) {
        this.clientType = clientType;
    }

    public int getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(int beginTime) {
        this.beginTime = beginTime;
    }

    public int getEndTime() {
        return this.endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getInterval() {
        return this.interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getLastSendTime() {
        return this.lastSendTime;
    }

    public void setLastSendTime(int lastSendTime) {
        this.lastSendTime = lastSendTime;
    }
}

