package com.mchange.v2.uid;

import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.security.SecureRandom;

public final class UidUtils
{
static final MLogger logger = MLog.getLogger(UidUtils.class);

public static final String VM_ID = generateVmId();

private static long within_vm_seq_counter = 0L;

private static String generateVmId() {
DataOutputStream dataOutputStream = null;
DataInputStream dataInputStream = null;

try {
SecureRandom secureRandom = new SecureRandom();
ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
dataOutputStream = new DataOutputStream(byteArrayOutputStream);

try {
dataOutputStream.write(InetAddress.getLocalHost().getAddress());
}
catch (Exception exception) {

if (logger.isLoggable(MLevel.INFO))
logger.log(MLevel.INFO, "Failed to get local InetAddress for VMID. This is unlikely to matter. At all. We'll add some extra randomness", exception); 
dataOutputStream.write(secureRandom.nextInt());
} 
dataOutputStream.writeLong(System.currentTimeMillis());
dataOutputStream.write(secureRandom.nextInt());

int i = byteArrayOutputStream.size() % 4;
if (i > 0) {

int k = 4 - i;
byte[] arrayOfByte1 = new byte[k];
secureRandom.nextBytes(arrayOfByte1);
dataOutputStream.write(arrayOfByte1);
} 

StringBuffer stringBuffer = new StringBuffer(32);
byte[] arrayOfByte = byteArrayOutputStream.toByteArray();
dataInputStream = new DataInputStream(new ByteArrayInputStream(arrayOfByte)); byte b; int j;
for (b = 0, j = arrayOfByte.length / 4; b < j; b++) {

int k = dataInputStream.readInt();
long l = k & 0xFFFFFFFFL;
stringBuffer.append(Long.toString(l, 36));
} 
return stringBuffer.toString();
}
catch (IOException iOException) {

if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, "Bizarro! IOException while reading/writing from ByteArray-based streams? We're skipping the VMID thing. It almost certainly doesn't matter, but please report the error.", iOException);
}

return "";
} finally {

try {

if (dataOutputStream != null) dataOutputStream.close(); 
} catch (IOException iOException) {
logger.log(MLevel.WARNING, "Huh? Exception close()ing a byte-array bound OutputStream.", iOException);
}  try { if (dataInputStream != null) dataInputStream.close();  }
catch (IOException iOException)
{ logger.log(MLevel.WARNING, "Huh? Exception close()ing a byte-array bound IntputStream.", iOException); }

} 
}
private static synchronized long nextWithinVmSeq() {
return ++within_vm_seq_counter;
}
public static String allocateWithinVmSequential() {
return VM_ID + "#" + nextWithinVmSeq();
}
}

