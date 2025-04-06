/*     */ package com.zhonglian.server.common.db.version;
/*     */ 
/*     */ import com.zhonglian.server.common.db.IDBConnectionFactory;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractDBVersionManager
/*     */ {
/*  30 */   protected Map<String, IUpdateDBVersion> m_vesionUpdateDict = new ConcurrentHashMap<>();
/*  31 */   protected ArrayList<ICreateDBTable> m_vesionCreateList = new ArrayList<>();
/*  32 */   private String m_errorInfo = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getCurVersion();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean _setCurVersion(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getNewestVersion();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void _onError(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void _onMsg(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean initCurrentVersion();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract IDBConnectionFactory getConn();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String _getNoVersionString() {
/*  97 */     return "0.0.0";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLastError() {
/* 106 */     return this.m_errorInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _setLastError(String errorInfo) {
/* 115 */     this.m_errorInfo = errorInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _clearLastError() {
/* 122 */     this.m_errorInfo = "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean regVersionUpdate(IUpdateDBVersion versionUpdataObj) {
/* 132 */     String version = versionUpdataObj.getRequestVersion();
/*     */     
/* 134 */     if (!this.m_vesionUpdateDict.containsKey(version)) {
/* 135 */       this.m_vesionUpdateDict.put(version, versionUpdataObj);
/* 136 */       _clearLastError();
/*     */ 
/*     */       
/* 139 */       return true;
/*     */     } 
/* 141 */     _onError("RegVersionUpdate: version=[" + version + "] has registered!!!");
/* 142 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean regVersionCreate(ICreateDBTable tableCreateObj) {
/* 155 */     return this.m_vesionCreateList.add(tableCreateObj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean _createDBTable() {
/* 164 */     for (ICreateDBTable obj : this.m_vesionCreateList) {
/* 165 */       if (!obj.run(getConn())) {
/* 166 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 170 */     String newestVersion = getNewestVersion();
/* 171 */     if (!_setCurVersion(newestVersion)) {
/* 172 */       _onError("AutoUpdate: set db version=[" + newestVersion + "] failed!!!");
/* 173 */       return false;
/*     */     } 
/*     */     
/* 176 */     _onMsg("AutoUpdate: create db of the newest version=[" + newestVersion + "]!");
/* 177 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean _updateContent() {
/* 186 */     String curVersion = getCurVersion();
/*     */     
/* 188 */     while (this.m_vesionUpdateDict.containsKey(curVersion)) {
/* 189 */       IUpdateDBVersion versionUpdataObj = this.m_vesionUpdateDict.get(curVersion);
/*     */       
/* 191 */       if (!versionUpdataObj.run()) {
/* 192 */         _onError("AutoUpdate: version=[" + curVersion + "] update failed!!!");
/* 193 */         return false;
/*     */       } 
/*     */       
/* 196 */       String targetVersion = versionUpdataObj.getTargetVersion();
/* 197 */       if (!_setCurVersion(targetVersion)) {
/* 198 */         _onError("AutoUpdate: set db version=[" + targetVersion + "] failed!!!");
/* 199 */         return false;
/*     */       } 
/*     */       
/* 202 */       _onMsg("AutoUpdate: update from [" + curVersion + "] to [" + targetVersion + "]");
/* 203 */       curVersion = targetVersion;
/*     */     } 
/*     */     
/* 206 */     String newestVersion = getNewestVersion();
/* 207 */     if (curVersion.equals(newestVersion)) {
/* 208 */       _clearLastError();
/* 209 */       _onMsg("AutoUpdate: [" + getConn().getCatalog() + "] successfully update to the version=[" + newestVersion + "]");
/* 210 */       return true;
/*     */     } 
/* 212 */     _onError("AutoUpdate: [" + getConn().getCatalog() + "] failed by version current=[" + curVersion + "] is not newest=[" + newestVersion + 
/* 213 */         "]!!!");
/* 214 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean _notNeedUpdate() {
/* 224 */     _onMsg("AutoUpdate: [" + getConn().getCatalog() + "] current version=[" + getNewestVersion() + "] is the newest.");
/* 225 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean _checkCurrentVersion() {
/* 234 */     return getCurVersion().equals(_getNoVersionString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Boolean run() {
/* 243 */     boolean isOk = initCurrentVersion();
/* 244 */     if (isOk) {
/* 245 */       if (getCurVersion().equalsIgnoreCase(getNewestVersion())) {
/* 246 */         isOk = _notNeedUpdate();
/*     */       } else {
/* 248 */         isOk = _updateContent();
/*     */       } 
/*     */     }
/*     */     
/* 252 */     return Boolean.valueOf(isOk);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/db/version/AbstractDBVersionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */