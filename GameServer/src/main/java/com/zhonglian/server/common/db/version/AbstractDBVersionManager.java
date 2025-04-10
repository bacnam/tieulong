package com.zhonglian.server.common.db.version;

import com.zhonglian.server.common.db.IDBConnectionFactory;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractDBVersionManager
{
protected Map<String, IUpdateDBVersion> m_vesionUpdateDict = new ConcurrentHashMap<>();
protected ArrayList<ICreateDBTable> m_vesionCreateList = new ArrayList<>();
private String m_errorInfo = "";

public abstract String getCurVersion();

protected abstract boolean _setCurVersion(String paramString);

public abstract String getNewestVersion();

protected abstract void _onError(String paramString);

protected abstract void _onMsg(String paramString);

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
_clearLastError();

return true;
} 
_onError("RegVersionUpdate: version=[" + version + "] has registered!!!");
return false;
}

public boolean regVersionCreate(ICreateDBTable tableCreateObj) {
return this.m_vesionCreateList.add(tableCreateObj);
}

protected boolean _createDBTable() {
for (ICreateDBTable obj : this.m_vesionCreateList) {
if (!obj.run(getConn())) {
return false;
}
} 

String newestVersion = getNewestVersion();
if (!_setCurVersion(newestVersion)) {
_onError("AutoUpdate: set db version=[" + newestVersion + "] failed!!!");
return false;
} 

_onMsg("AutoUpdate: create db of the newest version=[" + newestVersion + "]!");
return true;
}

protected boolean _updateContent() {
String curVersion = getCurVersion();

while (this.m_vesionUpdateDict.containsKey(curVersion)) {
IUpdateDBVersion versionUpdataObj = this.m_vesionUpdateDict.get(curVersion);

if (!versionUpdataObj.run()) {
_onError("AutoUpdate: version=[" + curVersion + "] update failed!!!");
return false;
} 

String targetVersion = versionUpdataObj.getTargetVersion();
if (!_setCurVersion(targetVersion)) {
_onError("AutoUpdate: set db version=[" + targetVersion + "] failed!!!");
return false;
} 

_onMsg("AutoUpdate: update from [" + curVersion + "] to [" + targetVersion + "]");
curVersion = targetVersion;
} 

String newestVersion = getNewestVersion();
if (curVersion.equals(newestVersion)) {
_clearLastError();
_onMsg("AutoUpdate: [" + getConn().getCatalog() + "] successfully update to the version=[" + newestVersion + "]");
return true;
} 
_onError("AutoUpdate: [" + getConn().getCatalog() + "] failed by version current=[" + curVersion + "] is not newest=[" + newestVersion + 
"]!!!");
return false;
}

protected boolean _notNeedUpdate() {
_onMsg("AutoUpdate: [" + getConn().getCatalog() + "] current version=[" + getNewestVersion() + "] is the newest.");
return true;
}

protected boolean _checkCurrentVersion() {
return getCurVersion().equals(_getNoVersionString());
}

protected Boolean run() {
boolean isOk = initCurrentVersion();
if (isOk) {
if (getCurVersion().equalsIgnoreCase(getNewestVersion())) {
isOk = _notNeedUpdate();
} else {
isOk = _updateContent();
} 
}

return Boolean.valueOf(isOk);
}
}

