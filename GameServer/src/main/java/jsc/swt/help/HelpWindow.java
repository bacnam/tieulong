package jsc.swt.help;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class HelpWindow
extends JFrame
{
JEditorPane pane;

public HelpWindow(URL paramURL) throws IOException {
super("Help");

Container container = getContentPane();
container.setLayout(new BorderLayout());

this.pane = new JEditorPane(paramURL);
this.pane.setEditable(false);
this.pane.addHyperlinkListener(new HelpListener(this));

container.add(new JScrollPane(this.pane), "Center");

JButton jButton = new JButton("Close");
jButton.setMnemonic('C');
jButton.addActionListener(new CloseButtonListener(this));

container.add(jButton, "South");

setSize(350, 400);
}

class CloseButtonListener
implements ActionListener
{
private final HelpWindow this$0;

CloseButtonListener(HelpWindow this$0) {
this.this$0 = this$0;
}
public void actionPerformed(ActionEvent param1ActionEvent) {
this.this$0.dispose();
} }
class HelpListener implements HyperlinkListener { HelpListener(HelpWindow this$0) {
this.this$0 = this$0;
} private final HelpWindow this$0;
public void hyperlinkUpdate(HyperlinkEvent param1HyperlinkEvent) {

try { if (param1HyperlinkEvent.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
this.this$0.pane.setPage(param1HyperlinkEvent.getURL());  }
catch (IOException iOException) { iOException.printStackTrace(System.err); }

} }

}

