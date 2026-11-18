package business.gmcmd.cmds;

import BaseCommon.CommLog;
import business.gmcmd.annotation.Command;
import business.gmcmd.annotation.Commander;

@Commander(
    name = "test",
    comment = "测试指令"
)
public class CmdTest {
    @Command(
        comment = "测试而已"
    )
    public void test(long player) {
        CommLog.info("hello world");
    }
}
