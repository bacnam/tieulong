package jsc.swt.dialogue;

import jsc.swt.panel.FieldPanel;

import javax.swing.*;
import java.awt.*;

public class FieldDialogue
        extends Dialogue {
    FieldPanel panel;

    public FieldDialogue(Component paramComponent, String paramString1, String paramString2, int paramInt1, int paramInt2) {
        super(paramComponent, paramString1, paramString2, paramInt1, paramInt2);

        this.panel = new FieldPanel();
        add((Component) this.panel, "Center");
    }

    public FieldDialogue(Component paramComponent, String paramString1, String paramString2) {
        this(paramComponent, paramString1, paramString2, -1, 2);
    }

    public void addComponent(JLabel paramJLabel, Component paramComponent, int paramInt) {
        this.panel.addComponent(paramJLabel, paramComponent, paramInt, 0);
        this.dialog.pack();
    }

    public void addField(String paramString, JTextField paramJTextField, int paramInt) {
        this.panel.addField(paramString, paramJTextField, paramInt, 0);
        this.dialog.pack();
    }

    public void addField(String paramString, JTextField paramJTextField, int paramInt, char paramChar) {
        this.panel.addField(paramString, paramJTextField, paramInt, 0, paramChar);
        this.dialog.pack();
    }

    public void addField(JLabel paramJLabel1, JTextField paramJTextField, JLabel paramJLabel2, int paramInt) {
        this.panel.addField(paramJLabel1, paramJTextField, paramJLabel2, paramInt, 0);
        this.dialog.pack();
    }

    public void addField(JLabel paramJLabel1, JTextField paramJTextField, JLabel paramJLabel2, int paramInt, char paramChar) {
        this.panel.addField(paramJLabel1, paramJTextField, paramJLabel2, paramInt, 0, paramChar);
        this.dialog.pack();
    }

    public void addToggleButton(JToggleButton paramJToggleButton, int paramInt, char paramChar) {
        this.panel.addToggleButton(paramJToggleButton, paramInt, 0, paramChar);
        this.dialog.pack();
    }
}

