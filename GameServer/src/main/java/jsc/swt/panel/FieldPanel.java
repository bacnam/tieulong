package jsc.swt.panel;

import javax.swing.*;
import java.awt.*;

public class FieldPanel
        extends JPanel {
    public GridBagConstraints c;

    public FieldPanel() {
        super(new GridBagLayout());
        setOpaque(false);
        this.c = new GridBagConstraints();
        this.c.insets = new Insets(2, 2, 2, 2);
    }

    public void addComponent(JLabel paramJLabel, Component paramComponent, int paramInt1, int paramInt2) {
        addComponents(paramJLabel, paramComponent, paramInt1, paramInt2);
    }

    public void addComponents(Component paramComponent1, Component paramComponent2, int paramInt1, int paramInt2) {
        this.c.gridy = paramInt1;
        this.c.anchor = 13;

        this.c.gridx = 2 * paramInt2;
        add(paramComponent1, this.c);
        this.c.anchor = 17;

        this.c.gridx++;
        add(paramComponent2, this.c);
    }

    public void addField(String paramString, JTextField paramJTextField, int paramInt1, int paramInt2) {
        addField(new JLabel(paramString), paramJTextField, paramInt1, paramInt2);
    }

    public void addField(String paramString, JTextField paramJTextField, int paramInt1, int paramInt2, char paramChar) {
        addField(new JLabel(paramString), paramJTextField, paramInt1, paramInt2, paramChar);
    }

    public void addField(JLabel paramJLabel, JTextField paramJTextField, int paramInt1, int paramInt2) {
        addComponent(paramJLabel, paramJTextField, paramInt1, paramInt2);
    }

    public void addField(JLabel paramJLabel, JTextField paramJTextField, int paramInt1, int paramInt2, char paramChar) {
        paramJLabel.setDisplayedMnemonic(paramChar);
        paramJTextField.setFocusAccelerator(paramChar);
        addField(paramJLabel, paramJTextField, paramInt1, paramInt2);
    }

    public void addField(JLabel paramJLabel1, JTextField paramJTextField, JLabel paramJLabel2, int paramInt1, int paramInt2) {
        JPanel jPanel = new JPanel(new FlowLayout(0, 0, 2));
        jPanel.setOpaque(false);
        jPanel.add(paramJTextField);
        jPanel.add(paramJLabel2);
        addComponent(paramJLabel1, jPanel, paramInt1, paramInt2);
    }

    public void addField(JLabel paramJLabel1, JTextField paramJTextField, JLabel paramJLabel2, int paramInt1, int paramInt2, char paramChar) {
        paramJLabel1.setDisplayedMnemonic(paramChar);
        paramJTextField.setFocusAccelerator(paramChar);
        addField(paramJLabel1, paramJTextField, paramJLabel2, paramInt1, paramInt2);
    }

    public void addToggleButton(JToggleButton paramJToggleButton, int paramInt1, int paramInt2, char paramChar) {
        JLabel jLabel = new JLabel(paramJToggleButton.getText());
        paramJToggleButton.setText("");
        jLabel.setDisplayedMnemonic(paramChar);
        paramJToggleButton.setMnemonic(paramChar);
        addComponents(jLabel, paramJToggleButton, paramInt1, paramInt2);
    }
}

