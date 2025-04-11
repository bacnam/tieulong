package com.mchange.v2.resourcepool;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ResourcePoolEventSupport {
    ResourcePool source;
    Set mlisteners = new HashSet();

    public ResourcePoolEventSupport(ResourcePool source) {
        this.source = source;
    }

    public synchronized void addResourcePoolListener(ResourcePoolListener mlistener) {
        this.mlisteners.add(mlistener);
    }

    public synchronized void removeResourcePoolListener(ResourcePoolListener mlistener) {
        this.mlisteners.remove(mlistener);
    }

    public synchronized void fireResourceAcquired(Object resc, int pool_size, int available_size, int removed_but_unreturned_size) {
        if (!this.mlisteners.isEmpty()) {

            ResourcePoolEvent evt = new ResourcePoolEvent(this.source, resc, false, pool_size, available_size, removed_but_unreturned_size);

            for (Iterator<ResourcePoolListener> i = this.mlisteners.iterator(); i.hasNext(); ) {

                ResourcePoolListener rpl = i.next();
                rpl.resourceAcquired(evt);
            }
        }
    }

    public synchronized void fireResourceCheckedIn(Object resc, int pool_size, int available_size, int removed_but_unreturned_size) {
        if (!this.mlisteners.isEmpty()) {

            ResourcePoolEvent evt = new ResourcePoolEvent(this.source, resc, false, pool_size, available_size, removed_but_unreturned_size);

            for (Iterator<ResourcePoolListener> i = this.mlisteners.iterator(); i.hasNext(); ) {

                ResourcePoolListener rpl = i.next();
                rpl.resourceCheckedIn(evt);
            }
        }
    }

    public synchronized void fireResourceCheckedOut(Object resc, int pool_size, int available_size, int removed_but_unreturned_size) {
        if (!this.mlisteners.isEmpty()) {

            ResourcePoolEvent evt = new ResourcePoolEvent(this.source, resc, true, pool_size, available_size, removed_but_unreturned_size);

            for (Iterator<ResourcePoolListener> i = this.mlisteners.iterator(); i.hasNext(); ) {

                ResourcePoolListener rpl = i.next();
                rpl.resourceCheckedOut(evt);
            }
        }
    }

    public synchronized void fireResourceRemoved(Object resc, boolean checked_out_resource, int pool_size, int available_size, int removed_but_unreturned_size) {
        if (!this.mlisteners.isEmpty()) {

            ResourcePoolEvent evt = new ResourcePoolEvent(this.source, resc, checked_out_resource, pool_size, available_size, removed_but_unreturned_size);

            for (Iterator<ResourcePoolListener> i = this.mlisteners.iterator(); i.hasNext(); ) {

                ResourcePoolListener rpl = i.next();
                rpl.resourceRemoved(evt);
            }
        }
    }
}

