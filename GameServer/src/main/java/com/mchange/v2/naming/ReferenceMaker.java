package com.mchange.v2.naming;

import javax.naming.NamingException;
import javax.naming.Reference;

public interface ReferenceMaker {
  Reference createReference(Object paramObject) throws NamingException;
}

