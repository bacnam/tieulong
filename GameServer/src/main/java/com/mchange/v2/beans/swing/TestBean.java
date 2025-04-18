package com.mchange.v2.beans.swing;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class TestBean
{
String s;
int i;
float f;
PropertyChangeSupport pcs = new PropertyChangeSupport(this);

public String getTheString() {
return this.s;
}
public int getTheInt() {
return this.i;
}
public float getTheFloat() {
return this.f;
}

public void setTheString(String paramString) {
if (!eqOrBothNull(paramString, this.s)) {

String str = this.s;
this.s = paramString;
this.pcs.firePropertyChange("theString", str, this.s);
} 
}

public void setTheInt(int paramInt) {
if (paramInt != this.i) {

int i = this.i;
this.i = paramInt;
this.pcs.firePropertyChange("theInt", i, this.i);
} 
}

public void setTheFloat(float paramFloat) {
if (paramFloat != this.f) {

float f = this.f;
this.f = paramFloat;
this.pcs.firePropertyChange("theFloat", new Float(f), new Float(this.f));
} 
}

public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener) {
this.pcs.addPropertyChangeListener(paramPropertyChangeListener);
}
public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener) {
this.pcs.removePropertyChangeListener(paramPropertyChangeListener);
}

private boolean eqOrBothNull(Object paramObject1, Object paramObject2) {
return (paramObject1 == paramObject2 || (paramObject1 != null && paramObject1.equals(paramObject2)));
}
}

