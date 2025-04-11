package business.player.feature.character;

import business.player.Player;
import core.database.game.bo.WarSpiritBO;

public class WarSpirit {
    Player player;
    Integer power;
    private WarSpiritBO bo;

    WarSpirit(Player player, WarSpiritBO bo) {
        this.power = null;
        this.player = player;
        this.bo = bo;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getPower() {
        if (this.power == null) {
            this.power = Integer.valueOf((new SpiritAttrCalculator(this)).getPower());
        }
        return this.power.intValue();
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

    public SpiritAttrCalculator getAttr() {
        return new SpiritAttrCalculator(this);
    }

    public void onAttrChanged() {
        this.power = Integer.valueOf((new SpiritAttrCalculator(this)).getPower());
        ((CharFeature) this.player.getFeature(CharFeature.class)).updatePower();
    }
}

