package jsc.swt.plot2d;

import java.awt.BasicStroke;

public class DashStroke
extends BasicStroke
{
static final float DEFAULT_DASH_LENGTH = 5.0F;

public DashStroke(float paramFloat1, float paramFloat2) {
super(paramFloat1, 0, 2, 0.0F, new float[] { paramFloat2, 0.6F * paramFloat2 }, 0.0F);
}

public DashStroke(float paramFloat) {
this(paramFloat, 5.0F);
}
}

