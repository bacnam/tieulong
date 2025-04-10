package business.global.broadcast;

import business.player.Player;
import java.util.LinkedList;

public class BroadcastRecord
{
public LinkedList<Player> players;
public BroadcastTask task;

public BroadcastRecord(LinkedList<Player> players, BroadcastTask task) {
this.players = players;
this.task = task;
}
}

