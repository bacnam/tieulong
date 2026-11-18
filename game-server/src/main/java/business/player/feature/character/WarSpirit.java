package business.player.feature.character;

import business.player.Player;
import core.database.game.bo.WarSpiritBO;

public class WarSpirit {
    Player player;
    private WarSpiritBO bo;
    Integer power = null;

    WarSpirit(Player player, WarSpiritBO bo) {
        this.player = player;
        this.bo = bo;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public WarSpiritBO getBo() {
        return this.bo;
    }

    public void setBo(WarSpiritBO bo) {
        this.bo = bo;
    }

    public int getSpiritId() {
        return this.bo.getSpiritId();
    }

    public int getPower() {
        if (this.power == null) {
            this.power = new SpiritAttrCalculator(this).getPower();
        }

        return this.power;
    }

    public SpiritAttrCalculator getAttr() {
        return new SpiritAttrCalculator(this);
    }

    public void onAttrChanged() {
        this.power = new SpiritAttrCalculator(this).getPower();
        this.player.getFeature(CharFeature.class).updatePower();
    }
}
