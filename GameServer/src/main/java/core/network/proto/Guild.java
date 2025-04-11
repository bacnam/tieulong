package core.network.proto;

import com.zhonglian.server.common.enums.GuildJob;
import com.zhonglian.server.common.enums.JoinState;

public class Guild {
    public static class GuildApply {
        public Player.Summary applyer;
    }

    public static class JoinInfo {
        private long sid;
        private int level;
        private int memberCnt;
        private int MaxmemberCnt;
        private int iconId;
        private int borderId;
        private String name;
        private Player.Summary master;
        private String manifesto;
        private JoinState joinState = JoinState.AccptAll;

        private boolean hasRequest;
        private int rank;
        private int joinLevel;

        public int getMaxmemberCnt() {
            return this.MaxmemberCnt;
        }

        public void setMaxmemberCnt(int maxmemberCnt) {
            this.MaxmemberCnt = maxmemberCnt;
        }

        public long getsId() {
            return this.sid;
        }

        public void setsId(long sId) {
            this.sid = sId;
        }

        public int getLevel() {
            return this.level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getMemberCnt() {
            return this.memberCnt;
        }

        public void setMemberCnt(int memberCnt) {
            this.memberCnt = memberCnt;
        }

        public int getIconId() {
            return this.iconId;
        }

        public void setIconId(int iconId) {
            this.iconId = iconId;
        }

        public int getBorderId() {
            return this.borderId;
        }

        public void setBorderId(int borderId) {
            this.borderId = borderId;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Player.Summary getMaster() {
            return this.master;
        }

        public void setMaster(Player.Summary master) {
            this.master = master;
        }

        public String getManifesto() {
            return this.manifesto;
        }

        public void setManifesto(String manifesto) {
            this.manifesto = manifesto;
        }

        public JoinState getJoinState() {
            return this.joinState;
        }

        public void setJoinState(JoinState joinState) {
            this.joinState = joinState;
        }

        public boolean isHasRequest() {
            return this.hasRequest;
        }

        public void setHasRequest(boolean hasRequest) {
            this.hasRequest = hasRequest;
        }

        public int getRank() {
            return this.rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getJoinLevel() {
            return this.joinLevel;
        }

        public void setJoinLevel(int joinLevel) {
            this.joinLevel = joinLevel;
        }
    }

    public static class guildInfo {
        private long sid;

        private int level;

        private int exp;
        private String name;
        private int iconId;
        private int borderId;
        private int createTime;
        private String notice;
        private String manifesto;
        private JoinState joinState;
        private int joinLevel;
        private int unlockIcon;
        private int unlockBorder;
        private Player.Summary master;
        private int nowNum;
        private int maxNum;

        public Player.Summary getMaster() {
            return this.master;
        }

        public void setMaster(Player.Summary master) {
            this.master = master;
        }

        public int getNowNum() {
            return this.nowNum;
        }

        public void setNowNum(int nowNum) {
            this.nowNum = nowNum;
        }

        public int getMaxNum() {
            return this.maxNum;
        }

        public void setMaxNum(int maxNum) {
            this.maxNum = maxNum;
        }

        public long getsId() {
            return this.sid;
        }

        public void setsId(long sId) {
            this.sid = sId;
        }

        public int getLevel() {
            return this.level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getExp() {
            return this.exp;
        }

        public void setExp(int exp) {
            this.exp = exp;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIconId() {
            return this.iconId;
        }

        public void setIconId(int iconId) {
            this.iconId = iconId;
        }

        public int getBorderId() {
            return this.borderId;
        }

        public void setBorderId(int borderId) {
            this.borderId = borderId;
        }

        public int getCreateTime() {
            return this.createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public String getNotice() {
            return this.notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }

        public String getManifesto() {
            return this.manifesto;
        }

        public void setManifesto(String manifesto) {
            this.manifesto = manifesto;
        }

        public JoinState getJoinState() {
            return this.joinState;
        }

        public void setJoinState(JoinState joinState) {
            this.joinState = joinState;
        }

        public int getJoinLevel() {
            return this.joinLevel;
        }

        public void setJoinLevel(int joinLevel) {
            this.joinLevel = joinLevel;
        }

        public int getUnlockIcon() {
            return this.unlockIcon;
        }

        public void setUnlockIcon(int unlockIcon) {
            this.unlockIcon = unlockIcon;
        }

        public int getUnlockBorder() {
            return this.unlockBorder;
        }

        public void setUnlockBorder(int unlockBorder) {
            this.unlockBorder = unlockBorder;
        }
    }

    public static class member {
        Player.Summary role;
        GuildJob job;
        int guildDonate;
        int sacrificeDonate;
        int weekDonate;
        boolean online;
        int lastOnlineTime;
        int joinTime;
        int skillUpLeftTimes;
        int sacrificeLeftTimes;
        int sacrificeCrystal;

        public int getSacrificeLeftTimes() {
            return this.sacrificeLeftTimes;
        }

        public void setSacrificeLeftTimes(int sacrificeLeftTimes) {
            this.sacrificeLeftTimes = sacrificeLeftTimes;
        }

        public int getSkillUpLeftTimes() {
            return this.skillUpLeftTimes;
        }

        public void setSkillUpLeftTimes(int skillUpTimes) {
            this.skillUpLeftTimes = skillUpTimes;
        }

        public int getGuildDonate() {
            return this.guildDonate;
        }

        public void setGuildDonate(int guildDonate) {
            this.guildDonate = guildDonate;
        }

        public Player.Summary getRole() {
            return this.role;
        }

        public void setRole(Player.Summary role) {
            this.role = role;
        }

        public GuildJob getJob() {
            return this.job;
        }

        public void setJob(GuildJob job) {
            this.job = job;
        }

        public int getSacrificeDonate() {
            return this.sacrificeDonate;
        }

        public void setSacrificeDonate(int sacrificeDonate) {
            this.sacrificeDonate = sacrificeDonate;
        }

        public int getWeekDonate() {
            return this.weekDonate;
        }

        public void setWeekDonate(int weekDonate) {
            this.weekDonate = weekDonate;
        }

        public boolean isOnline() {
            return this.online;
        }

        public void setOnline(boolean online) {
            this.online = online;
        }

        public int getLastOnlineTime() {
            return this.lastOnlineTime;
        }

        public void setLastOnlineTime(int lastOnlineTime) {
            this.lastOnlineTime = lastOnlineTime;
        }

        public int getJoinTime() {
            return this.joinTime;
        }

        public void setJoinTime(int joinTime) {
            this.joinTime = joinTime;
        }

        public int getSacrificeCrystal() {
            return this.sacrificeCrystal;
        }

        public void setSacrificeCrystal(int sacrificeCrystal) {
            this.sacrificeCrystal = sacrificeCrystal;
        }
    }

    public static class GuildSkill {
        int skillid;
        int level;

        public int getSkillid() {
            return this.skillid;
        }

        public void setSkillid(int skillid) {
            this.skillid = skillid;
        }

        public int getLevel() {
            return this.level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }
}

