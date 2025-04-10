package bsh.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Label;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import javax.swing.JApplet;

public class JRemoteApplet
extends JApplet
{
OutputStream out;
InputStream in;

public void init() {
getContentPane().setLayout(new BorderLayout());

try {
URL base = getDocumentBase();

Socket s = new Socket(base.getHost(), base.getPort() + 1);
this.out = s.getOutputStream();
this.in = s.getInputStream();
} catch (IOException e) {
getContentPane().add("Center", new Label("Remote Connection Failed", 1));

return;
} 

Component console = new JConsole(this.in, this.out);
getContentPane().add("Center", console);
}
}

