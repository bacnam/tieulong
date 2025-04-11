package bsh.util;

import bsh.ConsoleInterface;
import bsh.Interpreter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.peer.TextComponentPeer;
import java.io.*;
import java.util.Vector;

public class AWTConsole
        extends TextArea
        implements ConsoleInterface, Runnable, KeyListener {
    private OutputStream outPipe;
    private InputStream inPipe;
    private InputStream in;
    private PrintStream out;
    private StringBuffer line = new StringBuffer();
    private String startedLine;
    private int textLength = 0;
    private Vector history = new Vector();
    private int histLine = 0;
    public AWTConsole(int rows, int cols, InputStream cin, OutputStream cout) {
        super(rows, cols);
        setFont(new Font("Monospaced", 0, 14));
        setEditable(false);
        addKeyListener(this);

        this.outPipe = cout;
        if (this.outPipe == null) {
            this.outPipe = new PipedOutputStream();
            try {
                this.in = new PipedInputStream((PipedOutputStream) this.outPipe);
            } catch (IOException e) {
                print("Console internal error...");
            }
        }

        this.inPipe = cin;
        (new Thread(this)).start();

        requestFocus();
    }
    public AWTConsole() {
        this(12, 80, (InputStream) null, (OutputStream) null);
    }
    public AWTConsole(InputStream in, OutputStream out) {
        this(12, 80, in, out);
    }

    public static void main(String[] args) {
        AWTConsole console = new AWTConsole();
        final Frame f = new Frame("Bsh Console");
        f.add(console, "Center");
        f.pack();
        f.show();
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                f.dispose();
            }
        });

        Interpreter interpreter = new Interpreter(console);
        interpreter.run();
    }

    public Reader getIn() {
        return new InputStreamReader(this.in);
    }

    public PrintStream getOut() {
        return this.out;
    }

    public PrintStream getErr() {
        return this.out;
    }

    public void keyPressed(KeyEvent e) {
        type(e.getKeyCode(), e.getKeyChar(), e.getModifiers());
        e.consume();
    }

    public void type(int code, char ch, int modifiers) {
        switch (code) {
            case 8:
                if (this.line.length() > 0) {
                    this.line.setLength(this.line.length() - 1);
                    replaceRange("", this.textLength - 1, this.textLength);
                    this.textLength--;
                }
                return;
            case 10:
                enter();
                return;
            case 85:
                if ((modifiers & 0x2) > 0) {
                    int len = this.line.length();
                    replaceRange("", this.textLength - len, this.textLength);
                    this.line.setLength(0);
                    this.histLine = 0;
                    this.textLength = getText().length();
                } else {
                    doChar(ch);
                }
                return;
            case 38:
                historyUp();
                return;
            case 40:
                historyDown();
                return;
            case 9:
                this.line.append("    ");
                append("    ");
                this.textLength += 4;
                return;

            case 67:
                if ((modifiers & 0x2) > 0) {
                    this.line.append("^C");
                    append("^C");
                    this.textLength += 2;
                } else {
                    doChar(ch);
                }
                return;
        }
        doChar(ch);
    }

    private void doChar(char ch) {
        if (ch >= ' ' && ch <= '~') {
            this.line.append(ch);
            append(String.valueOf(ch));
            this.textLength++;
        }
    }

    private void enter() {
        String s;
        if (this.line.length() == 0) {
            s = ";\n";
        } else {
            s = this.line + "\n";
            this.history.addElement(this.line.toString());
        }
        this.line.setLength(0);
        this.histLine = 0;
        append("\n");
        this.textLength = getText().length();
        acceptLine(s);

        setCaretPosition(this.textLength);
    }

    public void setCaretPosition(int pos) {
        ((TextComponentPeer) getPeer()).setCaretPosition(pos + countNLs());
    }

    private int countNLs() {
        String s = getText();
        int c = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '\n')
                c++;
        }
        return c;
    }

    private void historyUp() {
        if (this.history.size() == 0)
            return;
        if (this.histLine == 0)
            this.startedLine = this.line.toString();
        if (this.histLine < this.history.size()) {
            this.histLine++;
            showHistoryLine();
        }
    }

    private void historyDown() {
        if (this.histLine == 0) {
            return;
        }
        this.histLine--;
        showHistoryLine();
    }

    private void showHistoryLine() {
        String showline;
        if (this.histLine == 0) {
            showline = this.startedLine;
        } else {
            showline = this.history.elementAt(this.history.size() - this.histLine);
        }
        replaceRange(showline, this.textLength - this.line.length(), this.textLength);
        this.line = new StringBuffer(showline);
        this.textLength = getText().length();
    }

    private void acceptLine(String line) {
        if (this.outPipe == null) {
            print("Console internal error...");
        } else {
            try {
                this.outPipe.write(line.getBytes());
                this.outPipe.flush();
            } catch (IOException e) {
                this.outPipe = null;
                throw new RuntimeException("Console pipe broken...");
            }
        }
    }

    public void println(Object o) {
        print(String.valueOf(o) + "\n");
    }

    public void error(Object o) {
        print(o, Color.red);
    }

    public void print(Object o, Color c) {
        print("*** " + String.valueOf(o));
    }

    public synchronized void print(Object o) {
        append(String.valueOf(o));
        this.textLength = getText().length();
    }

    private void inPipeWatcher() throws IOException {
        if (this.inPipe == null) {
            PipedOutputStream pout = new PipedOutputStream();
            this.out = new PrintStream(pout);
            this.inPipe = new PipedInputStream(pout);
        }
        byte[] ba = new byte[256];
        int read;
        while ((read = this.inPipe.read(ba)) != -1) {
            print(new String(ba, 0, read));
        }
        println("Console: Input closed...");
    }

    public void run() {
        try {
            inPipeWatcher();
        } catch (IOException e) {
            println("Console: I/O Error...");
        }
    }

    public String toString() {
        return "BeanShell AWTConsole";
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}
