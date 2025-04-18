package com.mchange.v2.c3p0.test;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.impl.C3P0JavaBeanObjectFactory;
import com.mchange.v2.naming.JavaBeanObjectFactory;
import javax.naming.Reference;

public final class JavaBeanRefTest
{
public static void main(String[] argv) {
try {
ComboPooledDataSource cpds = new ComboPooledDataSource();
Reference ref = cpds.getReference();
ComboPooledDataSource cpdsJBOF = (ComboPooledDataSource)(new JavaBeanObjectFactory()).getObjectInstance(ref, null, null, null);
ComboPooledDataSource cpdsCJBOF = (ComboPooledDataSource)(new C3P0JavaBeanObjectFactory()).getObjectInstance(ref, null, null, null);
System.err.println("cpds: " + cpds);
System.err.println("cpdsJBOF: " + cpdsJBOF);
System.err.println("cpdsCJBOF: " + cpdsCJBOF);
}
catch (Exception e) {
e.printStackTrace();
} 
}
}

