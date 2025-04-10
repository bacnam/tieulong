package jsc.swt.mdi;

import java.io.File;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public abstract class MDIWindow
extends JInternalFrame
{
boolean changed;
boolean selection;
File currentFile = null;

MDIWindow frame;

MDIApplication parentApp;

public MDIWindow(File paramFile) {
super("", true, true, true, true);
this.frame = this;
this.currentFile = paramFile;
if (paramFile != null) setTitle(paramFile.getName());

addInternalFrameListener(new MyWindowListener(this));
}

public abstract void clear();

public abstract void copy();

public abstract void cut();

public File getFile() {
return this.currentFile;
}

public MDIApplication getParentApp() {
return this.parentApp;
}

public boolean hasSelection() {
return this.selection;
}

public boolean isChanged() {
return this.changed;
}

public abstract void paste();

public abstract void selectAll();

public abstract void selectNone();

void setApp(MDIApplication paramMDIApplication) {
this.parentApp = paramMDIApplication;
}

public void setChanged(boolean paramBoolean) {
this.changed = paramBoolean;
}

public void setFile(File paramFile) {
this.currentFile = paramFile;
}

public void setSelection(boolean paramBoolean) {
this.selection = paramBoolean;
}

public abstract boolean write(File paramFile);

class MyWindowListener
extends InternalFrameAdapter
{
private final MDIWindow this$0;

MyWindowListener(MDIWindow this$0) {
this.this$0 = this$0;
}

public void internalFrameClosing(InternalFrameEvent param1InternalFrameEvent) {
if (this.this$0.isChanged()) {

String str = this.this$0.getTitle();
int i = JOptionPane.showConfirmDialog(this.this$0.parentApp, str + " has changed." + "\nDo you want to save it?", "Closing " + str, 0);

if (i == 0)
this.this$0.parentApp.save(false, this.this$0.frame); 
} 
}
}
}

