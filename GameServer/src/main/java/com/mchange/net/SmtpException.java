package com.mchange.net;

public class SmtpException
extends ProtocolException
{
int resp_num;

public SmtpException() {}

public SmtpException(String paramString) {
super(paramString);
}

public SmtpException(int paramInt, String paramString) {
this(paramString);
this.resp_num = paramInt;
}

public int getSmtpResponseNumber() {
return this.resp_num;
}
}

