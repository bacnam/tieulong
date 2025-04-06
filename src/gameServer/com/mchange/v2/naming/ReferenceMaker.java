package com.mchange.v2.naming;

import javax.naming.NamingException;
import javax.naming.Reference;

public interface ReferenceMaker {
  Reference createReference(Object paramObject) throws NamingException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/naming/ReferenceMaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */