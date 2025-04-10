package com.mchange.v2.beans.swing;

import com.mchange.v2.beans.BeansUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class PropertyBoundTextField
extends JTextField
{
PropertyComponentBindingUtility pcbu;
HostBindingInterface myHbi;

public PropertyBoundTextField(Object paramObject, String paramString, int paramInt) throws IntrospectionException {
super(paramInt);
this.myHbi = new MyHbi();
this.pcbu = new PropertyComponentBindingUtility(this.myHbi, paramObject, paramString, true);
this.pcbu.resync();
}

class MyHbi
implements HostBindingInterface
{
public void syncToValue(PropertyEditor param1PropertyEditor, Object param1Object) {
if (param1Object == null) {
PropertyBoundTextField.this.setText("");
} else {

param1PropertyEditor.setValue(param1Object);
String str = param1PropertyEditor.getAsText();
PropertyBoundTextField.this.setText(str);
} 
}

public void addUserModificationListeners() {
PropertyBoundTextField.WeChangedListener weChangedListener = new PropertyBoundTextField.WeChangedListener();
PropertyBoundTextField.this.addActionListener(weChangedListener);
PropertyBoundTextField.this.addFocusListener(weChangedListener);
}

public Object fetchUserModification(PropertyEditor param1PropertyEditor, Object param1Object) {
String str = PropertyBoundTextField.this.getText().trim();
if ("".equals(str)) {
return null;
}

param1PropertyEditor.setAsText(str);
return param1PropertyEditor.getValue();
}

public void alertErroneousInput() {
PropertyBoundTextField.this.getToolkit().beep();
}
}

class WeChangedListener
implements ActionListener, FocusListener {
public void actionPerformed(ActionEvent param1ActionEvent) {
PropertyBoundTextField.this.pcbu.userModification();
}

public void focusLost(FocusEvent param1FocusEvent) {
PropertyBoundTextField.this.pcbu.userModification();
}

public void focusGained(FocusEvent param1FocusEvent) {}
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

PropertyBoundTextField propertyBoundTextField1 = new PropertyBoundTextField(testBean, "theString", 20);
PropertyBoundTextField propertyBoundTextField2 = new PropertyBoundTextField(testBean, "theInt", 5);
PropertyBoundTextField propertyBoundTextField3 = new PropertyBoundTextField(testBean, "theFloat", 5);
JFrame jFrame = new JFrame();
BoxLayout boxLayout = new BoxLayout(jFrame.getContentPane(), 1);
jFrame.getContentPane().setLayout(boxLayout);
jFrame.getContentPane().add(propertyBoundTextField1);
jFrame.getContentPane().add(propertyBoundTextField2);
jFrame.getContentPane().add(propertyBoundTextField3);
jFrame.pack();
jFrame.show();
}
catch (Exception exception) {
exception.printStackTrace();
} 
}
}

