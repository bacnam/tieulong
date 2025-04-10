package proto.gamezone;

import java.util.List;

public class Player {
public static class PlayerInfo {
public long pid;
public int icon;
public String name;
public int lv;
public int vipLv;
public long guildID;
public String guildName;
public int serverId;
public int maxPower;
}

public static class G_PlayerInfo {
public List<Player.PlayerInfo> players;

public G_PlayerInfo(List<Player.PlayerInfo> playerInfos) {
this.players = playerInfos;
}
}
}

