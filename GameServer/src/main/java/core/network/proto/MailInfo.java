package core.network.proto;

import core.database.game.bo.PlayerMailBO;

public class MailInfo {
    long id;
    long pid;
    String senderName;
    String title;
    String content;
    int createTime;
    int pickUpTime;
    int pickUpExistTime;
    int existTime;
    int leftTime;
    String uniformIDList;
    String uniformCountList;

    public MailInfo() {
    }

    public MailInfo(PlayerMailBO bo) {
        this.id = bo.getId();
        this.pid = bo.getPid();
        this.senderName = bo.getSenderName();
        this.title = bo.getTitle();
        this.content = bo.getContent();
        this.createTime = bo.getCreateTime();
        this.pickUpTime = bo.getPickUpTime();
        this.pickUpExistTime = bo.getPickUpExistTime();
        this.existTime = bo.getExistTime();
        this.uniformIDList = bo.getUniformIDList();
        this.uniformCountList = bo.getUniformCountList();
    }

    public int getLeftTime() {
        return this.leftTime;
    }

    public void setLeftTime(int leftTime) {
        this.leftTime = leftTime;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPid() {
        return this.pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getSenderName() {
        return this.senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getPickUpTime() {
        return this.pickUpTime;
    }

    public void setPickUpTime(int pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public int getPickUpExistTime() {
        return this.pickUpExistTime;
    }

    public void setPickUpExistTime(int pickUpExistTime) {
        this.pickUpExistTime = pickUpExistTime;
    }

    public int getExistTime() {
        return this.existTime;
    }

    public void setExistTime(int existTime) {
        this.existTime = existTime;
    }

    public String getUniformIDList() {
        return this.uniformIDList;
    }

    public void setUniformIDList(String uniformIDList) {
        this.uniformIDList = uniformIDList;
    }

    public String getUniformCountList() {
        return this.uniformCountList;
    }

    public void setUniformCountList(String uniformCountList) {
        this.uniformCountList = uniformCountList;
    }
}

