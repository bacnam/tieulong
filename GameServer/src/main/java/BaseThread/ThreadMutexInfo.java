package BaseThread;

import java.util.ArrayList;

public class ThreadMutexInfo
{
private long _m_lThreadID;
private ArrayList<MutexInfo> _m_lThreadMutexList;

public ThreadMutexInfo(long _threadID) {
this._m_lThreadID = _threadID;
this._m_lThreadMutexList = new ArrayList<>();
}

public long getThreadID() {
return this._m_lThreadID;
}

public void tryLock(CosMutex _cosObj) throws CosMutexException {
if (_cosObj == null) {
return;
}
int objMutexLevel = _cosObj.getMutexLevel();

MutexInfo mutexInfo = null;
if (!this._m_lThreadMutexList.isEmpty()) {

MutexInfo topLvMutexInfo = this._m_lThreadMutexList.get(0);

if (topLvMutexInfo.getLockLevel() >= _cosObj.getMutexLevel()) {

mutexInfo = _getMutexLevelInfo(objMutexLevel);
if (mutexInfo == null || mutexInfo.getObj() != _cosObj)
{
throw new CosMutexException("无法获取高等级锁");
}
} 
} 

if (mutexInfo == null) {
mutexInfo = new MutexInfo(_cosObj);
this._m_lThreadMutexList.add(0, mutexInfo);
} 

mutexInfo.addLockTime();
}

public void tryUnlock(CosMutex _cosObj) throws CosMutexException {
if (_cosObj == null) {
return;
}
int objMutexLevel = _cosObj.getMutexLevel();
MutexInfo mutexInfo = _getMutexLevelInfo(objMutexLevel);

if (mutexInfo == null || mutexInfo.getObj() != _cosObj) {
throw new CosMutexException("尝试释放未获取的锁");
}

mutexInfo.reduceLockTime();

if (mutexInfo.getLockTime() <= 0)
{
this._m_lThreadMutexList.remove(mutexInfo);
}
}

public boolean judgeAllMutexRelease() {
return this._m_lThreadMutexList.isEmpty();
}

public void releaseAllMutex() {
while (!this._m_lThreadMutexList.isEmpty()) {
MutexInfo info = this._m_lThreadMutexList.get(0);

if (info == null)
continue; 
if (info.getLockTime() <= 0) {
this._m_lThreadMutexList.remove(info);
}
info.releaseAllLock();
} 
}

protected MutexInfo _getMutexLevelInfo(int _level) {
for (int i = 0; i < this._m_lThreadMutexList.size(); i++) {
MutexInfo info = this._m_lThreadMutexList.get(i);

if (info.getLockLevel() == _level) {
return info;
}
} 
return null;
}
}

