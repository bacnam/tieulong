package com.mchange.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface MailSender {
  void sendMail(String paramString1, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, String paramString2, String paramString3, String paramString4) throws IOException, ProtocolException, UnsupportedEncodingException;
  
  void sendMail(String paramString1, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, String paramString2, String paramString3) throws IOException, ProtocolException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/net/MailSender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */