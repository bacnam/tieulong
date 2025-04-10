package ch.qos.logback.core.recovery;

public class RecoveryCoordinator
{
public static final long BACKOFF_COEFFICIENT_MIN = 20L;
static long BACKOFF_COEFFICIENT_MAX = 327680L;

private long backOffCoefficient = 20L;

private static long UNSET = -1L;
private long currentTime = UNSET;
long next = System.currentTimeMillis() + getBackoffCoefficient();

public boolean isTooSoon() {
long now = getCurrentTime();
if (now > this.next) {
this.next = now + getBackoffCoefficient();
return false;
} 
return true;
}

void setCurrentTime(long forcedTime) {
this.currentTime = forcedTime;
}

private long getCurrentTime() {
if (this.currentTime != UNSET) {
return this.currentTime;
}
return System.currentTimeMillis();
}

private long getBackoffCoefficient() {
long currentCoeff = this.backOffCoefficient;
if (this.backOffCoefficient < BACKOFF_COEFFICIENT_MAX) {
this.backOffCoefficient *= 4L;
}
return currentCoeff;
}
}

