package com.mchange.v2.resourcepool;

import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;

final class ResourcePoolUtils
{
static final MLogger logger = MLog.getLogger(ResourcePoolUtils.class);

static final ResourcePoolException convertThrowable(String msg, Throwable t) {
if (logger.isLoggable(MLevel.FINE)) {
logger.log(MLevel.FINE, "Converting throwable to ResourcePoolException...", t);
}
if (t instanceof ResourcePoolException) {
return (ResourcePoolException)t;
}
return new ResourcePoolException(msg, t);
}

static final ResourcePoolException convertThrowable(Throwable t) {
return convertThrowable("Ouch! " + t.toString(), t);
}
}

