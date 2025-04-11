package BaseTask.AsynTask;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTask;

public abstract class AsyncCallBackTaskBase<T>
        implements SyncTask {
    private T _m_OBJ;

    public void setCallBackParam(T _obj) {
        this._m_OBJ = _obj;
    }

    public void run() {
        if (this._m_OBJ == null) {
            runError();
        } else {
            try {
                runSuc(this._m_OBJ);
            } catch (Exception e) {
                CommLog.error("AsyncCallBackTaskBase.run", e);
                runError();
            }
        }
    }

    public abstract void runSuc(T paramT);

    public abstract void runError();
}

