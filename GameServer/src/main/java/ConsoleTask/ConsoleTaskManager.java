package ConsoleTask;

import BaseCommon.CommLog;

public class ConsoleTaskManager
{
private static ConsoleTaskManager instance = new ConsoleTaskManager();

public static ConsoleTaskManager GetInstance() {
return instance;
}

private _AConsoleTaskRunner runner = null;

public void setRunner(_AConsoleTaskRunner _runner) {
this.runner = _runner;
}

public void run(String cmd) {
if (this.runner == null) {
CommLog.info(String.format("Class<_ACosCmdRunner> doesn't reg to CosCmdManager, reg it to deal the cmd:%s", new Object[] { cmd }));

return;
} 
this.runner.run(cmd);
}
}

