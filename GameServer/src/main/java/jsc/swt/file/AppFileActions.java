package jsc.swt.file;

import jsc.Utilities;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class AppFileActions {
    static String writeFileErrorMessage = "\nCheck the following.\nIs the file name valid for your system?\nIs there sufficient free space on your disk?\nAre you allowed to write to the disk?";
    protected JFileChooser openFileChooser;
    protected JFileChooser saveFileChooser;
    String confirmCloseMessage = "Save changes to file?";
    boolean changed;
    Component app;
    AppFile appFile;
    File currentFile = null;
    FileFilter filter;
    String defaultFileName;

    public AppFileActions(Component paramComponent, AppFile paramAppFile, FileFilter paramFileFilter, String paramString) {
        this.app = paramComponent;
        this.appFile = paramAppFile;

        this.filter = paramFileFilter;
        this.defaultFileName = paramString;

        this.openFileChooser = new JFileChooser();
        this.openFileChooser.addChoosableFileFilter(paramFileFilter);
        this.openFileChooser.setApproveButtonToolTipText("Open file");

        this.saveFileChooser = new JFileChooser();
        this.saveFileChooser.addChoosableFileFilter(paramFileFilter);
        this.saveFileChooser.setApproveButtonToolTipText("Save file");
    }

    public AppFileActions(Component paramComponent, AppFile paramAppFile, String paramString1, String paramString2) {
        this(paramComponent, paramAppFile, new ExampleFileFilter(paramString2, paramString1 + " file"), new String("*." + paramString2));
    }

    public boolean confirmClose() {
        if (this.changed) {

            int i = JOptionPane.showConfirmDialog(this.app, this.confirmCloseMessage, " Confirm", 1);
            if (i == 0) {
                if (!save(false)) return false;
            } else if (i == 2) {
                return false;
            }

        }
        return true;
    }

    public File getFile() {
        return this.currentFile;
    }

    public void setFile(File paramFile) {
        this.currentFile = paramFile;
    }

    public AbstractAction getOpenAction(String paramString, Icon paramIcon) {
        return new OpenAction(this, paramString, paramIcon);
    }

    public String getPath() {
        return this.currentFile.getParent();
    }

    public AbstractAction getSaveAction(String paramString, Icon paramIcon) {
        return new SaveAction(this, paramString, paramIcon);
    }

    public AbstractAction getSaveAsAction(String paramString) {
        return new SaveAsAction(this, paramString);
    }

    public boolean isChanged() {
        return this.changed;
    }

    public void setChanged(boolean paramBoolean) {
        this.changed = paramBoolean;
    }

    public boolean save(boolean paramBoolean) {
        File file = this.currentFile;
        if (paramBoolean || file == null || file.isDirectory() || !file.isFile()) {

            if (file != null && file.isDirectory()) {
                this.saveFileChooser.setCurrentDirectory(file);
            }

            if (!this.saveFileChooser.isTraversable(file)) {

                this.saveFileChooser.setCurrentDirectory(Utilities.getUserDirectory());
            }

            if (file == null || file.isDirectory()) {

                this.saveFileChooser.setSelectedFile(new File(this.defaultFileName));
            } else {
                this.saveFileChooser.setSelectedFile(file);
            }

            int i = this.saveFileChooser.showSaveDialog(this.app);
            if (i == 0) {

                file = this.saveFileChooser.getSelectedFile();
                if (file == null) return false;

            } else {
                return false;
            }
        }
        if (this.appFile.write(file)) {

            this.appFile.setFile(file);

            this.currentFile = file;
            setChanged(false);
            return true;
        }

        showFileWriteErrorMessage(file);
        return false;
    }

    public void setConfirmCloseMessage(String paramString) {
        this.confirmCloseMessage = paramString;
    }

    public void setDefaultFileName(String paramString) {
        this.defaultFileName = paramString;
    }

    public void setWriteFileErrorMessage(String paramString) {
        this;
        writeFileErrorMessage = paramString;
    }

    public void showFileWriteErrorMessage(File paramFile) {
        JOptionPane.showMessageDialog(this.app, "Cannot save to file " + paramFile.getName() + writeFileErrorMessage, "Error", 0);
    }

    class OpenAction
            extends AbstractAction {
        private final AppFileActions this$0;

        public OpenAction(AppFileActions this$0, String param1String, Icon param1Icon) {
            super(param1String, param1Icon);
            this.this$0 = this$0;
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            if (!this.this$0.confirmClose()) {
                return;
            }

            if (this.this$0.currentFile == null) {

                this.this$0.openFileChooser.setCurrentDirectory(Utilities.getUserDirectory());
            } else {

                this.this$0.openFileChooser.setCurrentDirectory(this.this$0.currentFile);
            }

            int i = this.this$0.openFileChooser.showOpenDialog(this.this$0.app);
            if (i == 0) {

                File file = this.this$0.openFileChooser.getSelectedFile();
                if (file != null) {
                    if (this.this$0.appFile.read(file)) {

                        this.this$0.currentFile = file;

                        this.this$0.appFile.setFile(file);
                    } else {

                        JOptionPane.showMessageDialog(this.this$0.app, "Cannot read file " + file.getName() + "\nThe file must exist and be a file previously saved by this program.", "Error", 0);
                    }
                }
            }
        }
    }

    class SaveAction
            extends AbstractAction {
        private final AppFileActions this$0;

        public SaveAction(AppFileActions this$0, String param1String, Icon param1Icon) {
            super(param1String, param1Icon);
            this.this$0 = this$0;
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            this.this$0.save(false);
        }
    }

    class SaveAsAction extends AbstractAction {
        private final AppFileActions this$0;

        public SaveAsAction(AppFileActions this$0, String param1String) {
            super(param1String);
            this.this$0 = this$0;
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            this.this$0.save(true);
        }
    }

}

