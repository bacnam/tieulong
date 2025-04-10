package ch.qos.logback.core.recovery;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.core.status.InfoStatus;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusManager;
import java.io.IOException;
import java.io.OutputStream;

public abstract class ResilientOutputStreamBase
extends OutputStream
{
static final int STATUS_COUNT_LIMIT = 8;
private int noContextWarning = 0;
private int statusCount = 0;

private Context context;

private RecoveryCoordinator recoveryCoordinator;

protected OutputStream os;
protected boolean presumedClean = true;

private boolean isPresumedInError() {
return (this.recoveryCoordinator != null && !this.presumedClean);
}

public void write(byte[] b, int off, int len) {
if (isPresumedInError()) {
if (!this.recoveryCoordinator.isTooSoon()) {
attemptRecovery();
}

return;
} 
try {
this.os.write(b, off, len);
postSuccessfulWrite();
} catch (IOException e) {
postIOFailure(e);
} 
}

public void write(int b) {
if (isPresumedInError()) {
if (!this.recoveryCoordinator.isTooSoon()) {
attemptRecovery();
}
return;
} 
try {
this.os.write(b);
postSuccessfulWrite();
} catch (IOException e) {
postIOFailure(e);
} 
}

public void flush() {
if (this.os != null) {
try {
this.os.flush();
postSuccessfulWrite();
} catch (IOException e) {
postIOFailure(e);
} 
}
}

abstract String getDescription();

abstract OutputStream openNewOutputStream() throws IOException;

private void postSuccessfulWrite() {
if (this.recoveryCoordinator != null) {
this.recoveryCoordinator = null;
this.statusCount = 0;
addStatus((Status)new InfoStatus("Recovered from IO failure on " + getDescription(), this));
} 
}

public void postIOFailure(IOException e) {
addStatusIfCountNotOverLimit((Status)new ErrorStatus("IO failure while writing to " + getDescription(), this, e));

this.presumedClean = false;
if (this.recoveryCoordinator == null) {
this.recoveryCoordinator = new RecoveryCoordinator();
}
}

public void close() throws IOException {
if (this.os != null) {
this.os.close();
}
}

void attemptRecovery() {
try {
close();
} catch (IOException e) {}

addStatusIfCountNotOverLimit((Status)new InfoStatus("Attempting to recover from IO failure on " + getDescription(), this));

try {
this.os = openNewOutputStream();
this.presumedClean = true;
} catch (IOException e) {
addStatusIfCountNotOverLimit((Status)new ErrorStatus("Failed to open " + getDescription(), this, e));
} 
}

void addStatusIfCountNotOverLimit(Status s) {
this.statusCount++;
if (this.statusCount < 8) {
addStatus(s);
}

if (this.statusCount == 8) {
addStatus(s);
addStatus((Status)new InfoStatus("Will supress future messages regarding " + getDescription(), this));
} 
}

public void addStatus(Status status) {
if (this.context == null) {
if (this.noContextWarning++ == 0) {
System.out.println("LOGBACK: No context given for " + this);
}
return;
} 
StatusManager sm = this.context.getStatusManager();
if (sm != null) {
sm.add(status);
}
}

public Context getContext() {
return this.context;
}

public void setContext(Context context) {
this.context = context;
}
}

