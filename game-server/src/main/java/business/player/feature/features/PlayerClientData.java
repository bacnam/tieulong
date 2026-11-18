package business.player.feature.features;

import business.player.Player;
import business.player.feature.Feature;
import com.zhonglian.server.common.db.BM;
import core.database.game.bo.ClientdataBO;
import core.database.game.bo.ClientguideBO;

public class PlayerClientData extends Feature {
    private static final int FIELD_MAXLENGTH = 25000;
    public ClientdataBO clientData;
    public ClientguideBO ClientGuide;

    public PlayerClientData(Player owner) {
        super(owner);
    }

    @Override
    public void loadDB() {
        long cid = this.player.getPid();
        this.clientData = BM.getBM(ClientdataBO.class).findOne("cid", cid);
        if (this.clientData == null) {
            this.clientData = new ClientdataBO();
            this.clientData.setPid(this.player.getPid());
            this.clientData.insert();
        }

        this.ClientGuide = BM.getBM(ClientguideBO.class).findOne("cid", cid);
        if (this.ClientGuide == null) {
            this.ClientGuide = new ClientguideBO();
            this.ClientGuide.setPid(this.player.getPid());
            this.ClientGuide.insert();
        }
    }

    public boolean setClientDataField(int idx, String val) {
        if (val.toCharArray().length > 25000) {
            return false;
        } else {
            this.clientData.saveVstr(idx, val);
            return true;
        }
    }

    public String getClientDataField(int idx) {
        return this.clientData.getVstr(idx);
    }
}
