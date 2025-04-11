package jsc.swt.control;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ToolBar
        extends JToolBar {
    private Color focusColour = Color.red;

    private Dimension buttonSize;

    public ToolBar(String paramString, Dimension paramDimension) {
        super(paramString);
        this.buttonSize = paramDimension;
        setBorder(new EtchedBorder());

        setFloatable(false);
    }

    public JButton add(Action paramAction, String paramString) {
        return add(paramAction, paramString, false);
    }

    public JButton add(Action paramAction, String paramString, boolean paramBoolean) {
        JButton jButton = add(paramAction);
        jButton.setPreferredSize(this.buttonSize);
        jButton.setText("");
        jButton.setToolTipText(paramString);
        jButton.setFocusable(paramBoolean);
        if (paramBoolean)
            jButton.addFocusListener(new KeyboardFocusListener(this, jButton));
        return jButton;
    }

    public void addToDesktop(JDesktopPane paramJDesktopPane) {
        Dimension dimension = getPreferredSize();
        setBounds(0, 0, dimension.width, dimension.height);

        paramJDesktopPane.add(this, JLayeredPane.PALETTE_LAYER);
    }

    public void setFocusColour(Color paramColor) {
        this.focusColour = paramColor;
    }

    public int getVisibleHeight() {
        return isVisible() ? (getPreferredSize()).height : 0;
    }

    class KeyboardFocusListener
            extends FocusAdapter {
        private final ToolBar this$0;
        JButton button;
        Color colour;

        public KeyboardFocusListener(ToolBar this$0, JButton param1JButton) {
            this.this$0 = this$0;
            this.button = param1JButton;
        }

        public void focusGained(FocusEvent param1FocusEvent) {
            this.colour = this.button.getBackground();
            this.button.setBackground(this.this$0.focusColour);
        }

        public void focusLost(FocusEvent param1FocusEvent) {
            this.button.setBackground(this.colour);
        }
    }
}

