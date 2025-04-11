package jsc.swt.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.print.*;
import java.net.URL;

public class PrintMenu
        extends JMenu {
    public JMenuItem pageSetup;
    public JMenuItem print;
    public PageSetupAction pageSetupAction;
    public PrintAction printAction;
    Component parentComponent;
    PageFormat pageFormat;
    Printable printer;

    public PrintMenu(Component paramComponent, PageFormat paramPageFormat, Printable paramPrintable) {
        super("Print");
        this.parentComponent = paramComponent;
        this.pageFormat = paramPageFormat;
        this.printer = paramPrintable;
        setMnemonic('P');

        this.pageSetupAction = new PageSetupAction(this, "Page Setup...");
        this.pageSetup = add(this.pageSetupAction);
        this.pageSetup.setMnemonic('U');

        ImageIcon imageIcon = null;
        URL uRL = getClass().getResource("images/print24.gif");
        if (uRL != null) imageIcon = new ImageIcon(uRL);
        this.printAction = new PrintAction(this, "Print...", imageIcon);
        this.print = add(this.printAction);
        this.print.setMnemonic('P');
        this.print.setAccelerator(KeyStroke.getKeyStroke(80, 2, false));
    }

    public void setEnabled(boolean paramBoolean) {
        super.setEnabled(paramBoolean);
        this.pageSetupAction.setEnabled(paramBoolean);
        this.printAction.setEnabled(paramBoolean);
    }

    class Pager implements Pageable {
        private final PrintMenu this$0;
        int pageCount;

        public Pager(PrintMenu this$0, int param1Int) {
            this.this$0 = this$0;
            this.pageCount = param1Int;
        }

        public int getNumberOfPages() {
            return this.pageCount;
        }

        public PageFormat getPageFormat(int param1Int) {
            return this.this$0.pageFormat;
        }

        public Printable getPrintable(int param1Int) {
            return this.this$0.printer;
        }
    }

    class PageSetupAction extends AbstractAction {
        private final PrintMenu this$0;

        public PageSetupAction(PrintMenu this$0, String param1String) {
            super(param1String);
            this.this$0 = this$0;
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            this.this$0.pageFormat = printerJob.pageDialog(this.this$0.pageFormat);
        }
    }

    class PrintAction extends AbstractAction {
        private final PrintMenu this$0;

        public PrintAction(PrintMenu this$0, String param1String, Icon param1Icon) {
            super(param1String, param1Icon);
            this.this$0 = this$0;
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            PrinterJob printerJob = PrinterJob.getPrinterJob();

            printerJob.setPrintable(this.this$0.printer, this.this$0.pageFormat);
            printerJob.setPageable(new PrintMenu.Pager(this.this$0, 1));
            if (printerJob.printDialog())
                try {
                    printerJob.print();
                } catch (PrinterException printerException) {

                    JOptionPane.showMessageDialog(this.this$0.parentComponent, "Cannot print: " + printerException.getMessage(), "System error", 0);
                }
        }
    }

}

