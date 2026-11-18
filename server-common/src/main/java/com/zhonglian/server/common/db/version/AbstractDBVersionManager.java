package com.zhonglian.server.common.db.version;

import com.zhonglian.server.common.db.IDBConnectionFactory;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractDBVersionManager {
    protected Map<String, IUpdateDBVersion> m_vesionUpdateDict = new ConcurrentHashMap<>();
    protected ArrayList<ICreateDBTable> m_vesionCreateList = new ArrayList<>();
    private String m_errorInfo = "";

    protected AbstractDBVersionManager() {
    }

    public abstract String getCurVersion();

    protected abstract boolean _setCurVersion(String var1);

    public abstract String getNewestVersion();

    protected abstract void _onError(String var1);

    protected abstract void _onMsg(String var1);

    protected abstract boolean initCurrentVersion();

    public abstract IDBConnectionFactory getConn();

    protected String _getNoVersionString() {
        return "0.0.0";
    }

    public String getLastError() {
        return this.m_errorInfo;
    }

    protected void _setLastError(String errorInfo) {
        this.m_errorInfo = errorInfo;
    }

    protected void _clearLastError() {
        this.m_errorInfo = "";
    }

    public boolean regVersionUpdate(IUpdateDBVersion versionUpdataObj) {
        String version = versionUpdataObj.getRequestVersion();
        if (!this.m_vesionUpdateDict.containsKey(version)) {
            this.m_vesionUpdateDict.put(version, versionUpdataObj);
            this._clearLastError();
            return true;
        } else {
            this._onError("RegVersionUpdate: version=[" + version + "] has registered!!!");
            return false;
        }
    }

    public boolean regVersionCreate(ICreateDBTable tableCreateObj) {
        return this.m_vesionCreateList.add(tableCreateObj);
    }

    protected boolean _createDBTable() {
        for (ICreateDBTable obj : this.m_vesionCreateList) {
            if (!obj.run(this.getConn())) {
                return false;
            }
        }

        String newestVersion = this.getNewestVersion();
        if (!this._setCurVersion(newestVersion)) {
            this._onError("AutoUpdate: set db version=[" + newestVersion + "] failed!!!");
            return false;
        } else {
            this._onMsg("AutoUpdate: create db of the newest version=[" + newestVersion + "]!");
            return true;
        }
    }

    protected boolean _updateContent() {
        String curVersion = this.getCurVersion();

        while (this.m_vesionUpdateDict.containsKey(curVersion)) {
            IUpdateDBVersion versionUpdataObj = this.m_vesionUpdateDict.get(curVersion);
            if (!versionUpdataObj.run()) {
                this._onError("AutoUpdate: version=[" + curVersion + "] update failed!!!");
                return false;
            }

            String targetVersion = versionUpdataObj.getTargetVersion();
            if (!this._setCurVersion(targetVersion)) {
                this._onError("AutoUpdate: set db version=[" + targetVersion + "] failed!!!");
                return false;
            }

            this._onMsg("AutoUpdate: update from [" + curVersion + "] to [" + targetVersion + "]");
            curVersion = targetVersion;
        }

        String newestVersion = this.getNewestVersion();
        if (curVersion.equals(newestVersion)) {
            this._clearLastError();
            this._onMsg("AutoUpdate: [" + this.getConn().getCatalog() + "] successfully update to the version=[" + newestVersion + "]");
            return true;
        } else {
            this._onError(
                "AutoUpdate: [" + this.getConn().getCatalog() + "] failed by version current=[" + curVersion + "] is not newest=[" + newestVersion + "]!!!"
            );
            return false;
        }
    }

    protected boolean _notNeedUpdate() {
        this._onMsg("AutoUpdate: [" + this.getConn().getCatalog() + "] current version=[" + this.getNewestVersion() + "] is the newest.");
        return true;
    }

    protected boolean _checkCurrentVersion() {
        return this.getCurVersion().equals(this._getNoVersionString());
    }

    protected Boolean run() {
        boolean isOk = this.initCurrentVersion();
        if (isOk) {
            if (this.getCurVersion().equalsIgnoreCase(this.getNewestVersion())) {
                isOk = this._notNeedUpdate();
            } else {
                isOk = this._updateContent();
            }
        }

        return isOk;
    }
}
