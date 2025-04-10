package proto.common;

public class GmCommand {
public static class G_GmCommand {
public long cid;
public String cmd;

public G_GmCommand(long cid, String cmd) {
this.cid = cid;
this.cmd = cmd;
}
}

public static class Z_GmCommand {
public String rslt;

public Z_GmCommand(String rslt) {
this.rslt = rslt;
}
}
}

