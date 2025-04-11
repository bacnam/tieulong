package proto.gameworld;

import com.zhonglian.server.common.enums.ChatType;

public class ChatMessage {
    long id;
    ChatType type;
    long senderPid;
    String senderName;
    int senderLv;
    int senderVipLv;
    int senderIcon;
    int senderServerId;
    String message;
    String content;
    int sendTime;
    long receivePid;
    String receiveName;
    boolean MonthCard;
    boolean YearCard;
    boolean is_married;

    public int getSenderServerId() {
        return this.senderServerId;
    }

    public void setSenderServerId(int senderServerId) {
        this.senderServerId = senderServerId;
    }

    public boolean isIs_married() {
        return this.is_married;
    }

    public void setIs_married(boolean is_married) {
        this.is_married = is_married;
    }

    public boolean isMonthCard() {
        return this.MonthCard;
    }

    public void setMonthCard(boolean monthCard) {
        this.MonthCard = monthCard;
    }

    public boolean isYearCard() {
        return this.YearCard;
    }

    public void setYearCard(boolean yearCard) {
        this.YearCard = yearCard;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ChatType getType() {
        return this.type;
    }

    public void setType(ChatType type) {
        this.type = type;
    }

    public long getSenderPid() {
        return this.senderPid;
    }

    public void setSenderPid(long senderPid) {
        this.senderPid = senderPid;
    }

    public String getSenderName() {
        return this.senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getSenderLv() {
        return this.senderLv;
    }

    public void setSenderLv(int senderLv) {
        this.senderLv = senderLv;
    }

    public int getSenderVipLv() {
        return this.senderVipLv;
    }

    public void setSenderVipLv(int senderVipLv) {
        this.senderVipLv = senderVipLv;
    }

    public int getSenderIcon() {
        return this.senderIcon;
    }

    public void setSenderIcon(int senderIcon) {
        this.senderIcon = senderIcon;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSendTime() {
        return this.sendTime;
    }

    public void setSendTime(int sendTime) {
        this.sendTime = sendTime;
    }

    public long getReceivePid() {
        return this.receivePid;
    }

    public void setReceivePid(long receivePid) {
        this.receivePid = receivePid;
    }

    public String getReceiveName() {
        return this.receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }
}

