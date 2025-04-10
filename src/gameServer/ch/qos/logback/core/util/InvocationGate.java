package ch.qos.logback.core.util;

public class InvocationGate
{
private static final int MAX_MASK = 65535;
private volatile long mask = 15L;
private volatile long lastMaskCheck = System.currentTimeMillis();

private long invocationCounter = 0L;

private static final long thresholdForMaskIncrease = 100L;

private final long thresholdForMaskDecrease = 800L;

public boolean skipFurtherWork() {
return ((this.invocationCounter++ & this.mask) != this.mask);
}

public void updateMaskIfNecessary(long now) {
long timeElapsedSinceLastMaskUpdateCheck = now - this.lastMaskCheck;
this.lastMaskCheck = now;
if (timeElapsedSinceLastMaskUpdateCheck < 100L && this.mask < 65535L) {
this.mask = this.mask << 1L | 0x1L;
} else if (timeElapsedSinceLastMaskUpdateCheck > 800L) {
this.mask >>>= 2L;
} 
}
}

