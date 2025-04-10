package com.mchange.v2.beans.swing;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.IntrospectionException;
import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;

class PropertyBoundButtonGroup
extends ButtonGroup
{
PropertyComponentBindingUtility pcbu;
HostBindingInterface myHbi;
WeChangedListener wcl = new WeChangedListener();

Map buttonsModelsToValues = new HashMap<Object, Object>();
Map valuesToButtonModels = new HashMap<Object, Object>();
JButton fakeButton = new JButton();

public PropertyBoundButtonGroup(Object paramObject, String paramString) throws IntrospectionException {
this.myHbi = new MyHbi();
this.pcbu = new PropertyComponentBindingUtility(this.myHbi, paramObject, paramString, false);
add(this.fakeButton, (Object)null);
this.pcbu.resync();
}

public void add(AbstractButton paramAbstractButton, Object paramObject) {
super.add(paramAbstractButton);
this.buttonsModelsToValues.put(paramAbstractButton.getModel(), paramObject);
this.valuesToButtonModels.put(paramObject, paramAbstractButton.getModel());

paramAbstractButton.addActionListener(this.wcl);
this.pcbu.resync();
}

public void add(AbstractButton paramAbstractButton) {
System.err.println(this + "Warning: The button '" + paramAbstractButton + "' has been implicitly associated with a null value!");
System.err.println("To avoid this warning, please use public void add(AbstractButton button, Object associatedValue)");
System.err.println("instead of the single-argument add method.");
super.add(paramAbstractButton);

paramAbstractButton.addActionListener(this.wcl);
this.pcbu.resync();
}

public void remove(AbstractButton paramAbstractButton) {
paramAbstractButton.removeActionListener(this.wcl);
super.remove(paramAbstractButton);
}

class MyHbi
implements HostBindingInterface
{
public void syncToValue(PropertyEditor param1PropertyEditor, Object param1Object) {
ButtonModel buttonModel = (ButtonModel)PropertyBoundButtonGroup.this.valuesToButtonModels.get(param1Object);
if (buttonModel != null) {
PropertyBoundButtonGroup.this.setSelected(buttonModel, true);
} else {
PropertyBoundButtonGroup.this.setSelected(PropertyBoundButtonGroup.this.fakeButton.getModel(), true);
} 
}

public void addUserModificationListeners() {}

public Object fetchUserModification(PropertyEditor param1PropertyEditor, Object param1Object) {
ButtonModel buttonModel = PropertyBoundButtonGroup.this.getSelection();
return PropertyBoundButtonGroup.this.buttonsModelsToValues.get(buttonModel);
}

public void alertErroneousInput() {
Toolkit.getDefaultToolkit().beep();
}
}

class WeChangedListener implements ActionListener {
public void actionPerformed(ActionEvent param1ActionEvent) {
PropertyBoundButtonGroup.this.pcbu.userModification();
}
}
}

