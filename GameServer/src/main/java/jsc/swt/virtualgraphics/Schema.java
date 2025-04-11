package jsc.swt.virtualgraphics;

import jsc.descriptive.OrderStatistics;

public class Schema
        extends Path {
    public Schema(OrderStatistics paramOrderStatistics, double paramDouble1, double paramDouble2) {
        super(9);

        moveTo(paramDouble1 - 0.25D * paramDouble2, paramOrderStatistics.getMinimum());
        lineTo(paramDouble1 + 0.25D * paramDouble2, paramOrderStatistics.getMinimum());
        moveTo(paramDouble1, paramOrderStatistics.getMinimum());
        lineTo(paramDouble1, paramOrderStatistics.getLowerQuartile());

        moveTo(paramDouble1 - 0.25D * paramDouble2, paramOrderStatistics.getMaximum());
        lineTo(paramDouble1 + 0.25D * paramDouble2, paramOrderStatistics.getMaximum());
        moveTo(paramDouble1, paramOrderStatistics.getMaximum());
        lineTo(paramDouble1, paramOrderStatistics.getUpperQuartile());

        moveTo(paramDouble1 - 0.5D * paramDouble2, paramOrderStatistics.getMedian());
        lineTo(paramDouble1 + 0.5D * paramDouble2, paramOrderStatistics.getMedian());

        append(new VRectangle(paramDouble1 - 0.5D * paramDouble2, paramOrderStatistics.getLowerQuartile(), paramDouble2, paramOrderStatistics.getInterquartileRange()), false);

        this.path.closePath();
    }
}

