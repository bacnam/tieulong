package jsc.swt.mdi;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultDesktopManager;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;
import jsc.swt.accessory.Calculator;
import jsc.swt.control.ToolBar;
import jsc.swt.file.ExampleFileFilter;
import jsc.swt.help.HelpAction;
import jsc.swt.menu.LookAndFeelMenu;

public abstract class MDIApplication
extends JFrame
{
public static final int NO_WINDOW = 0;
public static final int CLOSE_CANCELLED = 1;
public static final int CLOSE_CONFIRMED = 2;
protected JMenuBar menuBar;
protected JMenu fileMenu;
protected JMenu editMenu;
protected JMenu windowMenu;
protected JMenu optionsMenu;
protected JMenu helpMenu;
protected ToolBar toolBar;
protected JDesktopPane desktop;
protected JFrame parent;
Hashtable windowHashtable = new Hashtable(10);

CopyAction copyAction;

CutAction cutAction;

ClearAction clearAction;

PasteAction pasteAction;
Object aboutMessage;
String initialHelpPage;
String browserPath = "iexporer.exe";
String appTitle;
File currentFile = null;

String appFileExtension;

int untitledWindowCount = 0;

Calculator calculator;

ImageIcon windowIcon = null;

public MDIApplication(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, Object paramObject) {
super(" " + paramString1);
this.appTitle = paramString1;
this.appFileExtension = paramString4;
this.aboutMessage = paramObject;
this.initialHelpPage = paramString5;

this.parent = this;
addWindowListener(new WL(this));

if (paramString2 != null) {

Toolkit toolkit = Toolkit.getDefaultToolkit();
Image image = toolkit.getImage(paramString2);
setIconImage(image);
} 

if (paramString3 != null) {
this.windowIcon = new ImageIcon(paramString3);
}

this.menuBar = new JMenuBar();
this.menuBar.setBorder(new BevelBorder(0));

this.fileMenu = new JMenu("File");
this.menuBar.add(this.fileMenu);
NewAction newAction = new NewAction(this, "New...", new ImageIcon("images/new.gif"));
OpenAction openAction = new OpenAction(this, "Open...", new ImageIcon("images/open.gif"));
SaveAction saveAction = new SaveAction(this, "Save", new ImageIcon("images/save.gif"));
SaveAsAction saveAsAction = new SaveAsAction(this, "Save as...");
ExitAction exitAction = new ExitAction(this, "Exit");

JMenuItem jMenuItem = this.fileMenu.add(newAction);
jMenuItem.setAccelerator(KeyStroke.getKeyStroke(78, 2, false));
jMenuItem = this.fileMenu.add(openAction);
jMenuItem.setAccelerator(KeyStroke.getKeyStroke(79, 2, false));
jMenuItem = this.fileMenu.add(saveAction);
this.fileMenu.add(saveAsAction);
jMenuItem.setAccelerator(KeyStroke.getKeyStroke(83, 2, false));
this.fileMenu.addSeparator();
this.fileMenu.add(exitAction);

this.editMenu = new JMenu("Edit");
this.menuBar.add(this.editMenu);
this.cutAction = new CutAction(this, "Cut", new ImageIcon("images/cut.gif"));
this.clearAction = new ClearAction(this, "Delete");
this.copyAction = new CopyAction(this, "Copy", new ImageIcon("images/copy.gif"));
this.pasteAction = new PasteAction(this, "Paste", new ImageIcon("images/paste.gif"));
SelectAllAction selectAllAction = new SelectAllAction(this, "Select all");
SelectNoneAction selectNoneAction = new SelectNoneAction(this, "Select none");
jMenuItem = this.editMenu.add(this.cutAction);
jMenuItem.setAccelerator(KeyStroke.getKeyStroke(88, 2, false));
jMenuItem = this.editMenu.add(this.copyAction);
jMenuItem.setAccelerator(KeyStroke.getKeyStroke(67, 2, false));
jMenuItem = this.editMenu.add(this.pasteAction);
jMenuItem.setAccelerator(KeyStroke.getKeyStroke(86, 2, false));
this.editMenu.add(this.clearAction);
this.editMenu.addSeparator();
jMenuItem = this.editMenu.add(selectAllAction);
jMenuItem.setAccelerator(KeyStroke.getKeyStroke(65, 2, false));
this.editMenu.add(selectNoneAction);

this.optionsMenu = new JMenu("Options");
JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem("Show toolbar", true);
jCheckBoxMenuItem.addActionListener(new ShowToolBarListener(this));
this.optionsMenu.add(jCheckBoxMenuItem);

LookAndFeelMenu lookAndFeelMenu = new LookAndFeelMenu(this.parent);
this.optionsMenu.add((JMenuItem)lookAndFeelMenu);
this.menuBar.add(this.optionsMenu);

this.windowMenu = new JMenu("Window");
this.menuBar.add(this.windowMenu);
CascadeAction cascadeAction = new CascadeAction(this, "Cascade");
this.windowMenu.add(cascadeAction);
TileAction tileAction = new TileAction(this, "Tile");
this.windowMenu.add(tileAction);
CloseAllAction closeAllAction = new CloseAllAction(this, "Close all");
this.windowMenu.add(closeAllAction);
this.windowMenu.addSeparator();

this.helpMenu = new JMenu("Help");
this.menuBar.add(this.helpMenu);
CalculatorAction calculatorAction = new CalculatorAction(this, "Calculator...", new ImageIcon("images/calculator.gif"));
this.helpMenu.add(calculatorAction);

HelpAction helpAction = new HelpAction(this.parent, "Help topics...", new ImageIcon("images/help.gif"), this.browserPath, paramString5);

this.helpMenu.add((Action)helpAction);
AboutAction aboutAction = new AboutAction(this, "About...");
this.helpMenu.add(aboutAction);

this.toolBar = new ToolBar(this.appTitle + " tool bar", new Dimension(30, 30));

this.toolBar.add(newAction, "New");

this.toolBar.add(openAction, "Open file");

this.toolBar.add(saveAction, "Save file");

this.toolBar.addSeparator();

setEditEnabled(false);

if (getToolkit().getSystemClipboard().getContents(this) == null) {
this.pasteAction.setEnabled(false);
}
this.toolBar.add(this.cutAction, "Cut");

this.toolBar.add(this.copyAction, "Copy");

this.toolBar.add(this.pasteAction, "Paste");

this.toolBar.addSeparator();
this.toolBar.add((Action)helpAction, "Help");
this.toolBar.add(calculatorAction, "Calculator");

setJMenuBar(this.menuBar);

this.desktop = new JDesktopPane();
setContentPane(this.desktop);
this.desktop.setDesktopManager(new WindowManager(this));

this.toolBar.addToDesktop(this.desktop);

this.calculator = new Calculator(this.parent);
}

public void closeAllWindows() {
JInternalFrame[] arrayOfJInternalFrame = this.desktop.getAllFrames();
for (byte b = 0; b < arrayOfJInternalFrame.length; b++) { 
try { arrayOfJInternalFrame[b].setClosed(true); } catch (Exception exception) {} }

}

public abstract MDIWindow createWindow(File paramFile);

public void fileWriteErrorMessage(File paramFile) {
JOptionPane.showMessageDialog(this.parent, "Cannot save window to file " + paramFile.getName() + "\nCheck the following." + "\nIs the file name valid for your system?" + "\nIs there sufficient free space on your disk?" + "\nAre you allowed to write to the disk?", "Error", 0);
}

public MDIWindow getActiveWindow() {
return (MDIWindow)this.desktop.getSelectedFrame();
}

void newWindow(File paramFile) {
MDIWindow mDIWindow = createWindow(paramFile);
if (mDIWindow == null)
return;  if (paramFile == null) mDIWindow.setTitle("Untitled " + ++this.untitledWindowCount); 
mDIWindow.setApp(this);
if (this.windowIcon != null) mDIWindow.setFrameIcon(this.windowIcon); 
mDIWindow.setBounds(0, this.toolBar.getVisibleHeight(), 400, 300);
this.desktop.add(mDIWindow, JLayeredPane.DEFAULT_LAYER);
mDIWindow.setVisible(true);
JMenuItem jMenuItem = this.windowMenu.add(new WindowAction(this, mDIWindow.getTitle(), mDIWindow));
this.windowHashtable.put(mDIWindow, jMenuItem);
}

protected void processWindowEvent(WindowEvent paramWindowEvent) {
if (paramWindowEvent.getID() == 201 && 
saveQuery() == 1)
return;  super.processWindowEvent(paramWindowEvent);
}

public boolean save(boolean paramBoolean, MDIWindow paramMDIWindow) {
if (paramMDIWindow == null) return false; 
File file = paramMDIWindow.getFile();
if (paramBoolean || file == null) {

JFileChooser jFileChooser = new JFileChooser(this.currentFile);
String[] arrayOfString = { this.appFileExtension };
ExampleFileFilter exampleFileFilter = new ExampleFileFilter(arrayOfString, this.appTitle + " file");
jFileChooser.addChoosableFileFilter((FileFilter)exampleFileFilter);
jFileChooser.setApproveButtonToolTipText("Save window to file");
if (file == null) {
jFileChooser.setSelectedFile(new File("*." + this.appFileExtension));
} else {
jFileChooser.setSelectedFile(file);
}  int i = jFileChooser.showSaveDialog(this.parent);
if (i == 0) {

file = jFileChooser.getSelectedFile();
if (file == null) return false;

} else {
return false;
} 
}  if (paramMDIWindow.write(file)) {

paramMDIWindow.setFile(file);
setTitle(file.getName());
paramMDIWindow.setChanged(false);
this.currentFile = file;
return true;
} 

fileWriteErrorMessage(file); return false;
}

protected int saveQuery() {
JInternalFrame[] arrayOfJInternalFrame = this.desktop.getAllFrames();
for (int i = arrayOfJInternalFrame.length - 1; i >= 0; i--) {

MDIWindow mDIWindow = (MDIWindow)arrayOfJInternalFrame[i];
if (mDIWindow.isChanged()) {

int j = JOptionPane.showConfirmDialog(this.parent, mDIWindow.getTitle() + " has changed." + "\nDo you want to save it?", "Closing " + this.appTitle, 1);

if (j == 0)
{ if (!save(false, mDIWindow)) return 1;  }
else if (j == 2) { return 1; }

} 
}  return 2;
}

public void setClearEnabled(boolean paramBoolean) {
this.clearAction.setEnabled(paramBoolean);
}

public void setCopyEnabled(boolean paramBoolean) {
this.copyAction.setEnabled(paramBoolean);
}

public void setCutEnabled(boolean paramBoolean) {
this.cutAction.setEnabled(paramBoolean);
}

public void setEditEnabled(boolean paramBoolean) {
this.copyAction.setEnabled(paramBoolean);
this.cutAction.setEnabled(paramBoolean);
this.clearAction.setEnabled(paramBoolean);
}

public void setPasteEnabled(boolean paramBoolean) {
this.pasteAction.setEnabled(paramBoolean);
}

public void setTitle(String paramString) {
super.setTitle(this.appTitle + " - " + paramString);
}

class AboutAction extends AbstractAction { public AboutAction(MDIApplication this$0, String param1String) {
super(param1String); this.this$0 = this$0;
} private final MDIApplication this$0; public void actionPerformed(ActionEvent param1ActionEvent) {
JOptionPane.showMessageDialog(this.this$0.parent, this.this$0.aboutMessage, "About " + this.this$0.appTitle, -1);
} }
class CalculatorAction extends AbstractAction { private final MDIApplication this$0;

public CalculatorAction(MDIApplication this$0, String param1String, Icon param1Icon) {
super(param1String, param1Icon); this.this$0 = this$0;
}

public void actionPerformed(ActionEvent param1ActionEvent) {
this.this$0.calculator.show();
} }

class CascadeAction extends AbstractAction { private final MDIApplication this$0;

public CascadeAction(MDIApplication this$0, String param1String) {
super(param1String); this.this$0 = this$0;
}
public void actionPerformed(ActionEvent param1ActionEvent) {
JInternalFrame[] arrayOfJInternalFrame = this.this$0.desktop.getAllFrames();
boolean bool = false;
int i = this.this$0.toolBar.getVisibleHeight();
for (int j = arrayOfJInternalFrame.length - 1; j >= 0; j--) {

Rectangle rectangle = arrayOfJInternalFrame[j].getNormalBounds();
rectangle.setLocation(bool, i);
arrayOfJInternalFrame[j].setBounds(rectangle);
bool += true; i += 25; 
try { arrayOfJInternalFrame[j].setIcon(false); } catch (Exception exception) {}
arrayOfJInternalFrame[j].show();
} 
} }

class ClearAction extends AbstractAction { private final MDIApplication this$0;

public ClearAction(MDIApplication this$0, String param1String) {
super(param1String); this.this$0 = this$0;
}
public void actionPerformed(ActionEvent param1ActionEvent) {
MDIWindow mDIWindow = this.this$0.getActiveWindow();
if (mDIWindow == null)
return;  mDIWindow.clear();
mDIWindow.setChanged(true);
} }

class CloseAllAction extends AbstractAction { private final MDIApplication this$0;

public CloseAllAction(MDIApplication this$0, String param1String) { super(param1String); this.this$0 = this$0; } public void actionPerformed(ActionEvent param1ActionEvent) {
this.this$0.closeAllWindows();
} }
class CopyAction extends AbstractAction { private final MDIApplication this$0;

public CopyAction(MDIApplication this$0, String param1String, Icon param1Icon) {
super(param1String, param1Icon); this.this$0 = this$0;
}
public void actionPerformed(ActionEvent param1ActionEvent) {
MDIWindow mDIWindow = this.this$0.getActiveWindow();
if (mDIWindow == null)
return;  mDIWindow.copy();
this.this$0.pasteAction.setEnabled(true);
} }

class CutAction extends AbstractAction { private final MDIApplication this$0;

public CutAction(MDIApplication this$0, String param1String, Icon param1Icon) {
super(param1String, param1Icon); this.this$0 = this$0;
}
public void actionPerformed(ActionEvent param1ActionEvent) {
MDIWindow mDIWindow = this.this$0.getActiveWindow();
if (mDIWindow == null)
return;  mDIWindow.copy();
mDIWindow.clear();
mDIWindow.setChanged(true);
this.this$0.pasteAction.setEnabled(true);
} }

class ExitAction extends AbstractAction { private final MDIApplication this$0;

public ExitAction(MDIApplication this$0, String param1String) {
super(param1String); this.this$0 = this$0;
}

public void actionPerformed(ActionEvent param1ActionEvent) {
this.this$0.dispatchEvent(new WindowEvent(this.this$0, 201));
} }

class NewAction
extends AbstractAction
{
private final MDIApplication this$0;

public NewAction(MDIApplication this$0, String param1String, Icon param1Icon) {
super(param1String, param1Icon); this.this$0 = this$0;
}

public void actionPerformed(ActionEvent param1ActionEvent) {
this.this$0.newWindow((File)null);
} }

class OpenAction extends AbstractAction { private final MDIApplication this$0;

public OpenAction(MDIApplication this$0, String param1String, Icon param1Icon) {
super(param1String, param1Icon); this.this$0 = this$0;
}

public void actionPerformed(ActionEvent param1ActionEvent) {
JFileChooser jFileChooser = new JFileChooser(this.this$0.currentFile);
String[] arrayOfString = { this.this$0.appFileExtension };
ExampleFileFilter exampleFileFilter = new ExampleFileFilter(arrayOfString, this.this$0.appTitle + " files");
jFileChooser.addChoosableFileFilter((FileFilter)exampleFileFilter);
jFileChooser.setApproveButtonToolTipText("Open file");
int i = jFileChooser.showOpenDialog(this.this$0.parent);
if (i == 0) {

File file = jFileChooser.getSelectedFile();

this.this$0.newWindow(file);
this.this$0.currentFile = file;
} 
} }

class PasteAction extends AbstractAction { private final MDIApplication this$0;

public PasteAction(MDIApplication this$0, String param1String, Icon param1Icon) {
super(param1String, param1Icon); this.this$0 = this$0;
}
public void actionPerformed(ActionEvent param1ActionEvent) {
MDIWindow mDIWindow = this.this$0.getActiveWindow();
if (mDIWindow == null)
return;  mDIWindow.paste();
mDIWindow.setChanged(true);
} }

class SaveAction extends AbstractAction { private final MDIApplication this$0;

public SaveAction(MDIApplication this$0, String param1String, Icon param1Icon) { super(param1String, param1Icon); this.this$0 = this$0; } public void actionPerformed(ActionEvent param1ActionEvent) {
this.this$0.save(false, this.this$0.getActiveWindow());
} }

class SaveAsAction extends AbstractAction { private final MDIApplication this$0;

public SaveAsAction(MDIApplication this$0, String param1String) { super(param1String); this.this$0 = this$0; } public void actionPerformed(ActionEvent param1ActionEvent) {
this.this$0.save(true, this.this$0.getActiveWindow());
} }
class SelectAllAction extends AbstractAction { private final MDIApplication this$0;

public SelectAllAction(MDIApplication this$0, String param1String) {
super(param1String); this.this$0 = this$0;
}
public void actionPerformed(ActionEvent param1ActionEvent) {
MDIWindow mDIWindow = this.this$0.getActiveWindow();
if (mDIWindow == null)
return;  mDIWindow.selectAll();
mDIWindow.setSelection(true);
this.this$0.setEditEnabled(true);
} }

class SelectNoneAction extends AbstractAction { private final MDIApplication this$0;

public SelectNoneAction(MDIApplication this$0, String param1String) {
super(param1String); this.this$0 = this$0;
}
public void actionPerformed(ActionEvent param1ActionEvent) {
MDIWindow mDIWindow = this.this$0.getActiveWindow();
if (mDIWindow == null)
return;  mDIWindow.selectNone();
mDIWindow.setSelection(false);
this.this$0.setEditEnabled(false);
} }

class ShowToolBarListener implements ActionListener { ShowToolBarListener(MDIApplication this$0) {
this.this$0 = this$0;
} private final MDIApplication this$0;
public void actionPerformed(ActionEvent param1ActionEvent) {
this.this$0.toolBar.setVisible(!this.this$0.toolBar.isVisible());
} }
class TileAction extends AbstractAction { private final MDIApplication this$0;

public TileAction(MDIApplication this$0, String param1String) {
super(param1String); this.this$0 = this$0;
}
public void actionPerformed(ActionEvent param1ActionEvent) {
JInternalFrame[] arrayOfJInternalFrame = this.this$0.desktop.getAllFrames();
int i = arrayOfJInternalFrame.length;
if (i == 0)
return;  int j = (int)Math.sqrt(i);
int k = j;
int m = j;
if (k * m < i) {

m++;
if (k * m < i) k++; 
} 
Dimension dimension = this.this$0.desktop.getSize();
int n = dimension.width / m;
int i1 = dimension.height / k - this.this$0.toolBar.getVisibleHeight();
int i2 = 0;
int i3 = this.this$0.toolBar.getVisibleHeight();
for (byte b = 0; b < k; b++) {

for (byte b1 = 0; b1 < m && b * m + b1 < i; b1++) {

JInternalFrame jInternalFrame = arrayOfJInternalFrame[b * m + b1]; 
try { jInternalFrame.setIcon(false); } catch (Exception exception) {}
jInternalFrame.setBounds(new Rectangle(i2, i3, n, i1));
i2 += n;
} 
i3 += i1;
i2 = 0;
} 
} }

class WindowAction extends AbstractAction {
JInternalFrame f;
private final MDIApplication this$0;

public WindowAction(MDIApplication this$0, String param1String, JInternalFrame param1JInternalFrame) {
super(param1String); this.this$0 = this$0; this.f = param1JInternalFrame;
}

public void actionPerformed(ActionEvent param1ActionEvent) {

try { this.f.setIcon(false); } catch (Exception exception) {}

this.f.toFront(); try {
this.f.setSelected(true);
} catch (PropertyVetoException propertyVetoException) {}
} public JInternalFrame getWindow() {
return this.f;
}
}

class WindowManager extends DefaultDesktopManager { private final MDIApplication this$0;

WindowManager(MDIApplication this$0) {
this.this$0 = this$0;
}

public void activateFrame(JInternalFrame param1JInternalFrame) {
super.activateFrame(param1JInternalFrame);

this.this$0.setTitle(param1JInternalFrame.getTitle());
param1JInternalFrame.requestFocus();
}

public void closeFrame(JInternalFrame param1JInternalFrame) {
System.out.println("MDI Window closing");

Object object = this.this$0.windowHashtable.get(param1JInternalFrame);
if (object != null) {

this.this$0.windowMenu.remove((JMenuItem)object);
this.this$0.windowHashtable.remove(param1JInternalFrame);

System.out.println("Removing window menu item");
} 
super.closeFrame(param1JInternalFrame);
this.this$0.desktop.remove(param1JInternalFrame);

param1JInternalFrame = null;

if (this.this$0.getActiveWindow() == null) this.this$0.setTitle(""); 
} }
class WL extends WindowAdapter { private final MDIApplication this$0;

WL(MDIApplication this$0) {
this.this$0 = this$0;
} public void windowClosing(WindowEvent param1WindowEvent) {
System.exit(0);
} }

}

