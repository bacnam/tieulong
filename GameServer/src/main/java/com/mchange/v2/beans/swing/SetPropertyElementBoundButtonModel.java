package com.mchange.v2.beans.swing;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.beans.IntrospectionException;
import java.beans.PropertyEditor;
import java.util.HashSet;
import java.util.Set;

class SetPropertyElementBoundButtonModel
        implements ButtonModel {
    Object putativeElement;
    ButtonModel inner;
    PropertyComponentBindingUtility pcbu;

    public SetPropertyElementBoundButtonModel(ButtonModel paramButtonModel, Object paramObject1, String paramString, Object paramObject2) throws IntrospectionException {
        this.inner = paramButtonModel;
        this.putativeElement = paramObject2;
        this.pcbu = new PropertyComponentBindingUtility(new MyHbi(), paramObject1, paramString, false);
        this.pcbu.resync();
    }

    public static void bind(AbstractButton[] paramArrayOfAbstractButton, Object[] paramArrayOfObject, Object paramObject, String paramString) throws IntrospectionException {
        byte b;
        int i;
        for (b = 0, i = paramArrayOfAbstractButton.length; b < i; b++) {

            AbstractButton abstractButton = paramArrayOfAbstractButton[b];
            abstractButton.setModel(new SetPropertyElementBoundButtonModel(abstractButton.getModel(), paramObject, paramString, paramArrayOfObject[b]));
        }
    }

    public boolean isArmed() {
        return this.inner.isArmed();
    }

    public void setArmed(boolean paramBoolean) {
        this.inner.setArmed(paramBoolean);
    }

    public boolean isSelected() {
        return this.inner.isSelected();
    }

    public void setSelected(boolean paramBoolean) {
        this.inner.setSelected(paramBoolean);
    }

    public boolean isEnabled() {
        return this.inner.isEnabled();
    }

    public void setEnabled(boolean paramBoolean) {
        this.inner.setEnabled(paramBoolean);
    }

    public boolean isPressed() {
        return this.inner.isPressed();
    }

    public void setPressed(boolean paramBoolean) {
        this.inner.setPressed(paramBoolean);
    }

    public boolean isRollover() {
        return this.inner.isRollover();
    }

    public void setRollover(boolean paramBoolean) {
        this.inner.setRollover(paramBoolean);
    }

    public int getMnemonic() {
        return this.inner.getMnemonic();
    }

    public void setMnemonic(int paramInt) {
        this.inner.setMnemonic(paramInt);
    }

    public String getActionCommand() {
        return this.inner.getActionCommand();
    }

    public void setActionCommand(String paramString) {
        this.inner.setActionCommand(paramString);
    }

    public void setGroup(ButtonGroup paramButtonGroup) {
        this.inner.setGroup(paramButtonGroup);
    }

    public Object[] getSelectedObjects() {
        return this.inner.getSelectedObjects();
    }

    public void addActionListener(ActionListener paramActionListener) {
        this.inner.addActionListener(paramActionListener);
    }

    public void removeActionListener(ActionListener paramActionListener) {
        this.inner.removeActionListener(paramActionListener);
    }

    public void addItemListener(ItemListener paramItemListener) {
        this.inner.addItemListener(paramItemListener);
    }

    public void removeItemListener(ItemListener paramItemListener) {
        this.inner.removeItemListener(paramItemListener);
    }

    public void addChangeListener(ChangeListener paramChangeListener) {
        this.inner.addChangeListener(paramChangeListener);
    }

    public void removeChangeListener(ChangeListener paramChangeListener) {
        this.inner.removeChangeListener(paramChangeListener);
    }

    class MyHbi
            implements HostBindingInterface {
        public void syncToValue(PropertyEditor param1PropertyEditor, Object param1Object) {
            if (param1Object == null) {
                SetPropertyElementBoundButtonModel.this.setSelected(false);
            } else {
                SetPropertyElementBoundButtonModel.this.setSelected(((Set) param1Object).contains(SetPropertyElementBoundButtonModel.this.putativeElement));
            }
        }

        public void addUserModificationListeners() {
            ActionListener actionListener = new ActionListener() {
                public void actionPerformed(ActionEvent param2ActionEvent) {
                    SetPropertyElementBoundButtonModel.this.pcbu.userModification();
                }
            };
            SetPropertyElementBoundButtonModel.this.addActionListener(actionListener);
        }

        public Object fetchUserModification(PropertyEditor param1PropertyEditor, Object param1Object) {
            HashSet<Object> hashSet;
            if (param1Object == null) {

                if (!SetPropertyElementBoundButtonModel.this.isSelected()) {
                    return null;
                }
                hashSet = new HashSet();
            } else {

                hashSet = new HashSet((Set) param1Object);
            }
            if (SetPropertyElementBoundButtonModel.this.isSelected()) {
                hashSet.add(SetPropertyElementBoundButtonModel.this.putativeElement);
            } else {
                hashSet.remove(SetPropertyElementBoundButtonModel.this.putativeElement);
            }
            return hashSet;
        }

        public void alertErroneousInput() {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}

