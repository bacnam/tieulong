package jsc.swt.dialogue;

import jsc.Utilities;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Vector;

public class NamePairDialogue
        extends Dialogue {
    JList nameList1;
    JList nameList2;
    private Color focusColour = Color.red;

    public NamePairDialogue(Component paramComponent, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString1, int paramInt1, String paramString4, String[] paramArrayOfString2, int paramInt2) {
        this(paramComponent, paramString1, paramString2, paramString3, Utilities.toVector(paramArrayOfString1), paramInt1, paramString4, Utilities.toVector(paramArrayOfString2), paramInt2);
    }

    public NamePairDialogue(Component paramComponent, String paramString1, String paramString2, String paramString3, Vector paramVector1, int paramInt1, String paramString4, Vector paramVector2, int paramInt2) {
        super(paramComponent, paramString1, paramString2, -1, 2);

        setDefaultButtonEnabled(false);
        ListListener listListener = new ListListener(this);

        this.nameList1 = new JList(paramVector1);
        this.nameList1.setSelectionMode(paramInt1);
        this.nameList1.addListSelectionListener(listListener);
        JScrollPane jScrollPane1 = new JScrollPane(this.nameList1);
        this.nameList2 = new JList(paramVector2);
        this.nameList2.setSelectionMode(paramInt2);
        this.nameList2.addListSelectionListener(listListener);
        this.nameList1.addFocusListener(new KeyboardFocusListener(this, this.nameList1));
        this.nameList2.addFocusListener(new KeyboardFocusListener(this, this.nameList2));
        JScrollPane jScrollPane2 = new JScrollPane(this.nameList2);

        Font font = new Font("SansSerif", 0, 12);
        JLabel jLabel1 = new JLabel(paramString3, 0);
        jLabel1.setFont(font);
        JViewport jViewport1 = new JViewport();
        jViewport1.setView(jLabel1);
        jScrollPane1.setColumnHeader(jViewport1);
        JLabel jLabel2 = new JLabel(paramString4, 0);
        jLabel2.setFont(font);
        JViewport jViewport2 = new JViewport();
        jViewport2.setView(jLabel2);
        jScrollPane2.setColumnHeader(jViewport2);

        JPanel jPanel = new JPanel(new GridLayout(1, 2, 1, 1));
        jPanel.add(jScrollPane1);
        jPanel.add(jScrollPane2);

        add(jPanel, "Center");
        setSize(500, 200);
    }

    public int[] getIndices1() {
        return this.nameList1.getSelectedIndices();
    }

    public int[] getIndices2() {
        return this.nameList2.getSelectedIndices();
    }

    public String[] getNames1() {
        if (this.nameList1.isSelectionEmpty()) return null;
        Object[] arrayOfObject = this.nameList1.getSelectedValues();
        String[] arrayOfString = new String[arrayOfObject.length];
        for (byte b = 0; b < arrayOfObject.length; ) {
            arrayOfString[b] = (String) arrayOfObject[b];
            b++;
        }
        return arrayOfString;
    }

    public String[] getNames2() {
        if (this.nameList2.isSelectionEmpty()) return null;
        Object[] arrayOfObject = this.nameList2.getSelectedValues();
        String[] arrayOfString = new String[arrayOfObject.length];
        for (byte b = 0; b < arrayOfObject.length; ) {
            arrayOfString[b] = (String) arrayOfObject[b];
            b++;
        }
        return arrayOfString;
    }

    public void setFocusColour(Color paramColor) {
        this.focusColour = paramColor;
    }

    public String[][] showNames() {
        if (show() == null) return null;
        if (this.nameList1.isSelectionEmpty() || this.nameList2.isSelectionEmpty()) return null;

        String[] arrayOfString1 = getNames1();
        String[] arrayOfString2 = getNames2();
        String[][] arrayOfString = new String[2][];
        arrayOfString[0] = arrayOfString1;
        arrayOfString[1] = arrayOfString2;
        return arrayOfString;
    }

    class KeyboardFocusListener extends FocusAdapter {
        private final NamePairDialogue this$0;
        JList list;

        public KeyboardFocusListener(NamePairDialogue this$0, JList param1JList) {
            this.this$0 = this$0;
            this.list = param1JList;
        }

        public void focusGained(FocusEvent param1FocusEvent) {
            this.list.setBorder(BorderFactory.createLineBorder(this.this$0.focusColour, 1));
        }

        public void focusLost(FocusEvent param1FocusEvent) {
            this.list.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        }
    }

    class ListListener implements ListSelectionListener {
        private final NamePairDialogue this$0;

        ListListener(NamePairDialogue this$0) {
            this.this$0 = this$0;
        }

        public void valueChanged(ListSelectionEvent param1ListSelectionEvent) {
            if (this.this$0.nameList1.isSelectionEmpty() || this.this$0.nameList2.isSelectionEmpty()) {
                this.this$0.setDefaultButtonEnabled(false);
            } else {
                this.this$0.setDefaultButtonEnabled(true);
            }
        }
    }

}

