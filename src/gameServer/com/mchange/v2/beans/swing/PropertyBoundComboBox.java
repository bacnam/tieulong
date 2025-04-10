package com.mchange.v2.beans.swing;

import com.mchange.v2.beans.BeansUtils;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;

public class PropertyBoundComboBox
extends JComboBox
{
PropertyComponentBindingUtility pcbu;
MyHbi myHbi;
Object itemsSrc = null;
Object nullObject = null;

public PropertyBoundComboBox(Object paramObject1, String paramString, Object paramObject2, Object paramObject3) throws IntrospectionException {
this.myHbi = new MyHbi();
this.pcbu = new PropertyComponentBindingUtility(this.myHbi, paramObject1, paramString, false);

this.nullObject = paramObject3;
setItemsSrc(paramObject2);
}

public Object getItemsSrc() {
return this.itemsSrc;
}

public void setItemsSrc(Object paramObject) {
this.myHbi.suspendNotifications();

removeAllItems();
if (paramObject instanceof Object[]) {

Object[] arrayOfObject = (Object[])paramObject; byte b; int i;
for (b = 0, i = arrayOfObject.length; b < i; b++) {
addItem((E)arrayOfObject[b]);
}
} else if (paramObject instanceof Collection) {

Collection collection = (Collection)paramObject;
for (Iterator<E> iterator = collection.iterator(); iterator.hasNext();) {
addItem(iterator.next());
}
} else if (paramObject instanceof ComboBoxModel) {
setModel((ComboBoxModel<E>)paramObject);
} else {
throw new IllegalArgumentException("itemsSrc must be an Object[], a Collection, or a ComboBoxModel");
} 
this.itemsSrc = paramObject;

this.pcbu.resync();

this.myHbi.resumeNotifications();
}

public void setNullObject(Object paramObject) {
this.nullObject = null;
this.pcbu.resync();
}

public Object getNullObject() {
return this.nullObject;
}

class MyHbi implements HostBindingInterface {
boolean suspend_notice = false;

public void suspendNotifications() {
this.suspend_notice = true;
}
public void resumeNotifications() {
this.suspend_notice = false;
}

public void syncToValue(PropertyEditor param1PropertyEditor, Object param1Object) {
if (param1Object == null) {
PropertyBoundComboBox.this.setSelectedItem(PropertyBoundComboBox.this.nullObject);
} else {
PropertyBoundComboBox.this.setSelectedItem(param1Object);
} 
}

public void addUserModificationListeners() {
ItemListener itemListener = new ItemListener()
{
public void itemStateChanged(ItemEvent param2ItemEvent)
{
if (!PropertyBoundComboBox.MyHbi.this.suspend_notice)
PropertyBoundComboBox.this.pcbu.userModification(); 
}
};
PropertyBoundComboBox.this.addItemListener(itemListener);
}

public Object fetchUserModification(PropertyEditor param1PropertyEditor, Object param1Object) {
Object object = PropertyBoundComboBox.this.getSelectedItem();
if (PropertyBoundComboBox.this.nullObject != null && PropertyBoundComboBox.this.nullObject.equals(object))
object = null; 
return object;
}

public void alertErroneousInput() {
PropertyBoundComboBox.this.getToolkit().beep();
}
}

public static void main(String[] paramArrayOfString) {
try {
TestBean testBean = new TestBean();
PropertyChangeListener propertyChangeListener = new PropertyChangeListener()
{
public void propertyChange(PropertyChangeEvent param1PropertyChangeEvent) {
BeansUtils.debugShowPropertyChange(param1PropertyChangeEvent); }
};
testBean.addPropertyChangeListener(propertyChangeListener);

PropertyBoundComboBox propertyBoundComboBox = new PropertyBoundComboBox(testBean, "theString", new String[] { "SELECT", "Frog", "Fish", "Puppy" }, "SELECT");
PropertyBoundTextField propertyBoundTextField1 = new PropertyBoundTextField(testBean, "theInt", 5);
PropertyBoundTextField propertyBoundTextField2 = new PropertyBoundTextField(testBean, "theFloat", 5);
JFrame jFrame = new JFrame();
BoxLayout boxLayout = new BoxLayout(jFrame.getContentPane(), 1);
jFrame.getContentPane().setLayout(boxLayout);
jFrame.getContentPane().add(propertyBoundComboBox);
jFrame.getContentPane().add(propertyBoundTextField1);
jFrame.getContentPane().add(propertyBoundTextField2);
jFrame.pack();
jFrame.show();
}
catch (Exception exception) {
exception.printStackTrace();
} 
}
}

