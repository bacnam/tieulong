package ConsoleTask;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTaskManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleTaskDealThread
        extends Thread {
    private boolean _m_bThreadExit;

    public ConsoleTaskDealThread() {
        this._m_bThreadExit = false;
        setName("ConsoleTaskDealThread");
    }

    public void ExitThread() {
        this._m_bThreadExit = true;
    }

    public void run() {
        while (!this._m_bThreadExit) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try {
                String str = br.readLine().trim();
                if (str.equals("")) {
                    continue;
                }
                SyncTaskManager.task(() -> ConsoleTaskManager.GetInstance().run(str));
            } catch (IOException e) {
                CommLog.error("ConsoleTaskDealThread.run", e);
            }
        }
    }
}
