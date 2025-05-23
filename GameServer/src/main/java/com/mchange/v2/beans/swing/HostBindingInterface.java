package com.mchange.v2.beans.swing;

import java.beans.PropertyEditor;

interface HostBindingInterface {
  void syncToValue(PropertyEditor paramPropertyEditor, Object paramObject);

  void addUserModificationListeners();

  Object fetchUserModification(PropertyEditor paramPropertyEditor, Object paramObject);

  void alertErroneousInput();
}

