package bsh.util;

import java.io.IOException;
import java.net.ServerSocket;

public class Httpd
        extends Thread {
    ServerSocket ss;

    public Httpd(int port) throws IOException {
        this.ss = new ServerSocket(port);
    }

    public static void main(String[] argv) throws IOException {
        (new Httpd(Integer.parseInt(argv[0]))).start();
    }

    public void run() {
        try {
            while (true) {
                (new HttpdConnection(this.ss.accept())).start();
            }
        } catch (IOException e) {

            System.out.println(e);
            return;
        }
    }
}

