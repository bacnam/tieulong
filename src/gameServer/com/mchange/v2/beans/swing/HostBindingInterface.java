package com.mchange.v2.beans.swing;

import java.beans.PropertyEditor;

interface HostBindingInterface {
  void syncToValue(PropertyEditor paramPropertyEditor, Object paramObject);
  
  void addUserModificationListeners();
  
  Object fetchUserModification(PropertyEditor paramPropertyEditor, Object paramObject);
  
  void alertErroneousInput();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/beans/swing/HostBindingInterface.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */