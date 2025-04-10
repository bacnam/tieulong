package com.notnoop.apns;

import com.notnoop.apns.internal.ReconnectPolicies;

public interface ReconnectPolicy
{
boolean shouldReconnect();

void reconnected();

ReconnectPolicy copy();

public enum Provided
{
NEVER
{
public ReconnectPolicy newObject() {
return (ReconnectPolicy)new ReconnectPolicies.Never();
}
},

EVERY_HALF_HOUR
{
public ReconnectPolicy newObject() {
return (ReconnectPolicy)new ReconnectPolicies.EveryHalfHour();
}
},

EVERY_NOTIFICATION
{
public ReconnectPolicy newObject() {
return (ReconnectPolicy)new ReconnectPolicies.Always();
}
};

abstract ReconnectPolicy newObject();
}
}

