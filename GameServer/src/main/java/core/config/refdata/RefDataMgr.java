package core.config.refdata;

import com.zhonglian.server.common.data.AbstractRefDataMgr;
import core.config.refdata.ref.RefBaseGame;

import java.io.File;

public class RefDataMgr
        extends AbstractRefDataMgr {
    private static final String Defaule_RefData_Path = "data" + File.separatorChar + "refData";
    private static RefDataMgr _instance = new RefDataMgr();
    private String _refPath = System.getProperty("GameServer.RefPath", Defaule_RefData_Path);

    public static RefDataMgr getInstance() {
        return _instance;
    }

    protected void onCustomLoad() {
        load(RefBaseGame.class);
    }

    public String getRefPath() {
        return this._refPath;
    }

    protected boolean assertAll() {
        return true;
    }
}

