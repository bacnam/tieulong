package jsc.swt.dialogue;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.Vector;

public class NameTabbedDialogue
        extends Dialogue {
    int n;
    JList[] nameLists;
    boolean[] selectionRequired;

    public NameTabbedDialogue(Component paramComponent, String paramString1, String paramString2, String[] paramArrayOfString, Vector[] paramArrayOfVector, int[] paramArrayOfint, boolean[] paramArrayOfboolean) {
        super(paramComponent, paramString1, paramString2, -1, 2);
        createTabbedPane(paramArrayOfString, paramArrayOfVector, paramArrayOfint, paramArrayOfboolean);
    }

    public NameTabbedDialogue(Component paramComponent, String paramString1, String paramString2, String[] paramArrayOfString, Vector paramVector, int paramInt, boolean[] paramArrayOfboolean) {
        super(paramComponent, paramString1, paramString2, -1, 2);
        this.n = paramArrayOfString.length;
        Vector[] arrayOfVector = new Vector[this.n];
        int[] arrayOfInt = new int[this.n];
        for (byte b = 0; b < this.n; b++) {

            arrayOfVector[b] = paramVector;
            arrayOfInt[b] = paramInt;
        }
        createTabbedPane(paramArrayOfString, arrayOfVector, arrayOfInt, paramArrayOfboolean);
    }

    public NameTabbedDialogue(Component paramComponent, String paramString1, String paramString2, String[] paramArrayOfString, Vector paramVector, int[] paramArrayOfint, boolean[] paramArrayOfboolean) {
        super(paramComponent, paramString1, paramString2, -1, 2);
        this.n = paramArrayOfString.length;
        Vector[] arrayOfVector = new Vector[this.n];
        for (byte b = 0; b < this.n; ) {
            arrayOfVector[b] = paramVector;
            b++;
        }
        createTabbedPane(paramArrayOfString, arrayOfVector, paramArrayOfint, paramArrayOfboolean);
    }

    private void createTabbedPane(String[] paramArrayOfString, Vector[] paramArrayOfVector, int[] paramArrayOfint, boolean[] paramArrayOfboolean) {
        this.n = paramArrayOfString.length;
        if (paramArrayOfVector.length != this.n || paramArrayOfint.length != this.n || paramArrayOfboolean.length != this.n) {
            throw new IllegalArgumentException("Arrays different lengths.");
        }
        this.selectionRequired = paramArrayOfboolean;

        JTabbedPane jTabbedPane = new JTabbedPane();
        this.nameLists = new JList[this.n];
        Font font = new Font("SansSerif", 0, 12);
        ListListener listListener = new ListListener(this);
        for (byte b = 0; b < this.n; b++) {

            this.nameLists[b] = new JList(paramArrayOfVector[b]);
            this.nameLists[b].setSelectionMode(paramArrayOfint[b]);
            this.nameLists[b].addListSelectionListener(listListener);
            JScrollPane jScrollPane = new JScrollPane(this.nameLists[b]);

            jTabbedPane.addTab(paramArrayOfString[b], jScrollPane);
            if (paramArrayOfboolean[b]) setDefaultButtonEnabled(false);

        }

        add(jTabbedPane, "Center");
    }

    public int getN() {
        return this.n;
    }

    public String[] getNames(int paramInt) {
        if (this.nameLists[paramInt].isSelectionEmpty()) return null;
        Object[] arrayOfObject = this.nameLists[paramInt].getSelectedValues();
        String[] arrayOfString = new String[arrayOfObject.length];
        for (byte b = 0; b < arrayOfObject.length; ) {
            arrayOfString[b] = (String) arrayOfObject[b];
            b++;
        }
        return arrayOfString;
    }

    public String[][] showNames() {
        if (show() == null) return null;
        String[][] arrayOfString = new String[this.n][];
        for (byte b = 0; b < this.n; b++) {

            String[] arrayOfString1 = getNames(b);
            arrayOfString[b] = arrayOfString1;
        }
        return arrayOfString;
    }

    class ListListener implements ListSelectionListener {
        private final NameTabbedDialogue this$0;

        ListListener(NameTabbedDialogue this$0) {
            this.this$0 = this$0;
        }

        public void valueChanged(ListSelectionEvent param1ListSelectionEvent) {
            for (byte b = 0; b < this.this$0.n; b++) {
                if (this.this$0.selectionRequired[b] && this.this$0.nameLists[b].isSelectionEmpty()) {
                    this.this$0.setDefaultButtonEnabled(false);
                    return;
                }
            }
            this.this$0.setDefaultButtonEnabled(true);
        }
    }

}

