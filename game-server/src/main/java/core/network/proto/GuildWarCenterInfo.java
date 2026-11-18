package core.network.proto;

import business.global.guild.GuildMgr;
import core.database.game.bo.GuildwarGuildResultBO;
import java.util.List;

public class GuildWarCenterInfo {
    int centerId;
    boolean isOpen;
    List<business.global.guild.Guild.GuildSummary> atkGuild;
    business.global.guild.Guild.GuildSummary owner;
    business.global.guild.Guild.GuildSummary atkingGuild;
    business.global.guild.Guild.GuildSummary winner;
    List<GuildWarCenterInfo.GuildwarGuildResultRecord> result;

    public business.global.guild.Guild.GuildSummary getAtkingGuild() {
        return this.atkingGuild;
    }

    public void setAtkingGuild(business.global.guild.Guild.GuildSummary atkingGuild) {
        this.atkingGuild = atkingGuild;
    }

    public int getCenterId() {
        return this.centerId;
    }

    public void setCenterId(int centerId) {
        this.centerId = centerId;
    }

    public List<business.global.guild.Guild.GuildSummary> getAtkGuild() {
        return this.atkGuild;
    }

    public void setAtkGuild(List<business.global.guild.Guild.GuildSummary> atkGuild) {
        this.atkGuild = atkGuild;
    }

    public business.global.guild.Guild.GuildSummary getWinner() {
        return this.winner;
    }

    public void setWinner(business.global.guild.Guild.GuildSummary winner) {
        this.winner = winner;
    }

    public business.global.guild.Guild.GuildSummary getOwner() {
        return this.owner;
    }

    public void setOwner(business.global.guild.Guild.GuildSummary owner) {
        this.owner = owner;
    }

    public List<GuildWarCenterInfo.GuildwarGuildResultRecord> getResult() {
        return this.result;
    }

    public void setResult(List<GuildWarCenterInfo.GuildwarGuildResultRecord> result) {
        this.result = result;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public static class GuildwarGuildResultRecord {
        long id;
        long atkGuildId;
        String atkName;
        long defGuildId;
        String defName;
        int result;
        int centerId;
        int fightTime;

        public GuildwarGuildResultRecord(GuildwarGuildResultBO bo) {
            this.id = bo.getId();
            this.atkGuildId = bo.getAtkGuildId();
            this.atkName = GuildMgr.getInstance().getGuild(bo.getAtkGuildId()).getName();
            this.defGuildId = bo.getDefGuildId();
            if (bo.getDefGuildId() != 0L) {
                this.defName = GuildMgr.getInstance().getGuild(bo.getDefGuildId()).getName();
            } else {
                this.defName = "神秘帮会";
            }

            this.result = bo.getResult();
            this.centerId = bo.getCenterId();
            this.fightTime = bo.getFightTime();
        }
    }
}
