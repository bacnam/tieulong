package bsh.util;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

class HttpdConnection
        extends Thread {
    Socket client;
    BufferedReader in;
    OutputStream out;
    PrintStream pout;
    boolean isHttp1;

    HttpdConnection(Socket client) {
        this.client = client;
        setPriority(4);
    }

    public void run() {
        try {
            this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));

            this.out = this.client.getOutputStream();
            this.pout = new PrintStream(this.out);

            String request = this.in.readLine();
            if (request == null) {
                error(400, "Empty Request");
            }
            if (request.toLowerCase().indexOf("http/1.") != -1) {
                String s;

                while (!(s = this.in.readLine()).equals("") && s != null) ;

                this.isHttp1 = true;
            }

            StringTokenizer st = new StringTokenizer(request);
            if (st.countTokens() < 2) {
                error(400, "Bad Request");
            } else {

                String command = st.nextToken();
                if (command.equals("GET")) {
                    serveFile(st.nextToken());
                } else {
                    error(400, "Bad Request");
                }
            }
            this.client.close();
        } catch (IOException e) {

            System.out.println("I/O error " + e);

            try {
                this.client.close();
            } catch (Exception e2) {
            }
        }
    }

    private void serveFile(String file) throws FileNotFoundException, IOException {
        if (file.equals("/")) {
            file = "/remote/remote.html";
        }
        if (file.startsWith("/remote/")) {
            file = "/bsh/util/lib/" + file.substring(8);
        }

        if (file.startsWith("/java")) {
            error(404, "Object Not Found");
        } else {
            try {
                System.out.println("sending file: " + file);
                sendFileData(file);
            } catch (FileNotFoundException e) {
                error(404, "Object Not Found");
            }
        }
    }

    private void sendFileData(String file) throws IOException, FileNotFoundException {
        InputStream fis = getClass().getResourceAsStream(file);
        if (fis == null)
            throw new FileNotFoundException(file);
        byte[] data = new byte[fis.available()];

        if (this.isHttp1) {

            this.pout.println("HTTP/1.0 200 Document follows");

            this.pout.println("Content-length: " + data.length);

            if (file.endsWith(".gif")) {
                this.pout.println("Content-type: image/gif");
            } else if (file.endsWith(".html") || file.endsWith(".htm")) {
                this.pout.println("Content-Type: text/html");
            } else {
                this.pout.println("Content-Type: application/octet-stream");
            }
            this.pout.println();
        }

        int bytesread = 0;

        while (true) {
            bytesread = fis.read(data);
            if (bytesread > 0)
                this.pout.write(data, 0, bytesread);
            if (bytesread == -1) {
                this.pout.flush();
                return;
            }
        }
    }

    private void error(int num, String s) {
        s = "<html><h1>" + s + "</h1></html>";
        if (this.isHttp1) {

            this.pout.println("HTTP/1.0 " + num + " " + s);
            this.pout.println("Content-type: text/html");
            this.pout.println("Content-length: " + s.length() + "\n");
        }

        this.pout.println(s);
    }
}

