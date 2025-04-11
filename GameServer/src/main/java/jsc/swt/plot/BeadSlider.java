package jsc.swt.plot;

import jsc.swt.control.RealSlider;

import java.awt.*;

public class BeadSlider
        extends RealSlider {
    int index;

    public BeadSlider(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, Color paramColor) {
        super(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble4, paramDouble5, 0.0D, "");
        setOrientation(1);
        setOpaque(false);
        setPaintTrack(false);

        this.index = paramInt;

        setBeadColour(paramColor);
        setFocusColour(paramColor);
    }

    public int getIndex() {
        return this.index;
    }

    public void rescale(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5) {
        rescale(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble4, paramDouble5, 0.0D, "");
    }

    public void setBeadColour(Color paramColor) {
        BeadSliderUI beadSliderUI = (BeadSliderUI) getUI();
        beadSliderUI.setBeadColour(paramColor);
    }

    public void setFocusColour(Color paramColor) {
        BeadSliderUI beadSliderUI = (BeadSliderUI) getUI();
        beadSliderUI.setFocusColour(paramColor);
    }

    public void updateUI() {
        BeadSliderUI beadSliderUI = new BeadSliderUI();
        setUI(beadSliderUI);
    }
}

