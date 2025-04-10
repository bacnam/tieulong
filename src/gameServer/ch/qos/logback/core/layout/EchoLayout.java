package ch.qos.logback.core.layout;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;

public class EchoLayout<E>
extends LayoutBase<E>
{
public String doLayout(E event) {
return (new StringBuilder()).append(event).append(CoreConstants.LINE_SEPARATOR).toString();
}
}

