package proto.common;

public class Mail {
public static class SendMail {
public long scid;
public int mailID;
public String uniformIDList;
public String uniformCountList;
public String params;

public SendMail(long scid, int mailID, String uniformIDList, String uniformCountList, String params) {
this.scid = scid;
this.mailID = mailID;
this.uniformIDList = uniformIDList;
this.uniformCountList = uniformCountList;
this.params = params;
}
}
}

