package BaseTask.AsynTask;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTask;

public abstract class AsyncCallBackTaskBase<T> implements SyncTask {
    private T _m_OBJ;

    public void setCallBackParam(T _obj) {
        this._m_OBJ = _obj;
    }

    @Override
    public void run() {
        if (this._m_OBJ == null) {
            this.runError();
        } else {
            try {
                this.runSuc(this._m_OBJ);
            } catch (Exception var2) {
                CommLog.error("AsyncCallBackTaskBase.run", (Throwable)var2);
                this.runError();
            }
        }
    }

    public abstract void runSuc(T var1);

    public abstract void runError();
}
