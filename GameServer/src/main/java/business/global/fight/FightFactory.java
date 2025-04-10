package business.global.fight;

import business.global.arena.Competitor;
import business.player.Player;
import business.player.feature.pvp.DroiyanFeature;

public class FightFactory
{
public static DroiyanFight createFight(DroiyanFeature atk, DroiyanFeature def, boolean revenge) {
return new DroiyanFight(atk, def, revenge);
}

public static BossFight createFight(Player player, int level) {
return new BossFight(player, level);
}

public static ArenaFight createFight(Competitor atk, Competitor def) {
return new ArenaFight(atk, def);
}
}

