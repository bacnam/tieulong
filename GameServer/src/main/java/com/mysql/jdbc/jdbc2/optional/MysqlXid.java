package com.mysql.jdbc.jdbc2.optional;

import javax.transaction.xa.Xid;

public class MysqlXid
implements Xid
{
int hash = 0;

byte[] myBqual;

int myFormatId;

byte[] myGtrid;

public MysqlXid(byte[] gtrid, byte[] bqual, int formatId) {
this.myGtrid = gtrid;
this.myBqual = bqual;
this.myFormatId = formatId;
}

public boolean equals(Object another) {
if (another instanceof Xid) {
Xid anotherAsXid = (Xid)another;

if (this.myFormatId != anotherAsXid.getFormatId()) {
return false;
}

byte[] otherBqual = anotherAsXid.getBranchQualifier();
byte[] otherGtrid = anotherAsXid.getGlobalTransactionId();

if (otherGtrid != null && otherGtrid.length == this.myGtrid.length) {
int length = otherGtrid.length;
int i;
for (i = 0; i < length; i++) {
if (otherGtrid[i] != this.myGtrid[i]) {
return false;
}
} 

if (otherBqual != null && otherBqual.length == this.myBqual.length) {
length = otherBqual.length;

for (i = 0; i < length; i++) {
if (otherBqual[i] != this.myBqual[i]) {
return false;
}
} 
} else {
return false;
} 

return true;
} 
} 

return false;
}

public byte[] getBranchQualifier() {
return this.myBqual;
}

public int getFormatId() {
return this.myFormatId;
}

public byte[] getGlobalTransactionId() {
return this.myGtrid;
}

public synchronized int hashCode() {
if (this.hash == 0) {
for (int i = 0; i < this.myGtrid.length; i++) {
this.hash = 33 * this.hash + this.myGtrid[i];
}
}

return this.hash;
}
}

