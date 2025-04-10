package jsc.swt.help;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;

public class HelpAction
extends AbstractAction
{
String browserPath;
String initialHelpPage;
Component parent;

public HelpAction(Component paramComponent, String paramString1, Icon paramIcon, String paramString2, String paramString3) {
super(paramString1, paramIcon);
this.browserPath = paramString2;
this.initialHelpPage = paramString3;
this.parent = paramComponent;
}

public void actionPerformed(ActionEvent paramActionEvent) {
showHelp(this.parent, this.browserPath, this.initialHelpPage);
}

public void setBrowserPath(String paramString) {
this.browserPath = paramString;
}

public void setInitialHelpPage(String paramString) {
this.initialHelpPage = paramString;
}

public static void showHelp(Component paramComponent, String paramString) {
showHelp(paramComponent, "explorer", paramString);
}

public static void showHelp(Component paramComponent, String paramString1, String paramString2) {
Runtime runtime = Runtime.getRuntime();

Process process = null;

String str = paramString1 + " " + paramString2;
try {
process = runtime.exec(str);

}
catch (IOException iOException) {

showHelpWindow(paramComponent, paramString2);
} 
}

public static void showHelpWindow(Component paramComponent, String paramString) {
try {
File file = new File(paramString);
URL uRL = file.toURL();
HelpWindow helpWindow = new HelpWindow(uRL);
helpWindow.show();
}
catch (IOException iOException) {

JOptionPane.showMessageDialog(paramComponent, "Cannot find Help page " + paramString, "Error", 0);

}
catch (Exception exception) {

JOptionPane.showMessageDialog(paramComponent, "Cannot display Help page " + paramString, "Error", 0);
} 
}
}

