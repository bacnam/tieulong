package com.mchange.v2.beans.swing;

import java.beans.IntrospectionException;
import javax.swing.AbstractButton;

public final class BoundButtonUtils
{
public static void bindToSetProperty(AbstractButton[] paramArrayOfAbstractButton, Object[] paramArrayOfObject, Object paramObject, String paramString) throws IntrospectionException {
SetPropertyElementBoundButtonModel.bind(paramArrayOfAbstractButton, paramArrayOfObject, paramObject, paramString);
}

public static void bindAsRadioButtonsToProperty(AbstractButton[] paramArrayOfAbstractButton, Object[] paramArrayOfObject, Object paramObject, String paramString) throws IntrospectionException {
PropertyBoundButtonGroup propertyBoundButtonGroup = new PropertyBoundButtonGroup(paramObject, paramString);
for (byte b = 0; b < paramArrayOfAbstractButton.length; b++)
propertyBoundButtonGroup.add(paramArrayOfAbstractButton[b], paramArrayOfObject[b]); 
}
}

