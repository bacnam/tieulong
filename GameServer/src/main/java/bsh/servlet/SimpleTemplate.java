package bsh.servlet;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SimpleTemplate {
    static String NO_TEMPLATE = "NO_TEMPLATE";
    static Map templateData = new HashMap<Object, Object>();
    static boolean cacheTemplates = true;
    StringBuffer buff;

    public SimpleTemplate(String template) {
        init(template);
    }

    public SimpleTemplate(Reader reader) throws IOException {
        String template = getStringFromStream(reader);
        init(template);
    }

    public SimpleTemplate(URL url) throws IOException {
        String template = getStringFromStream(url.openStream());
        init(template);
    }

    public static SimpleTemplate getTemplate(String file) {
        String templateText = (String) templateData.get(file);

        if (templateText == null || !cacheTemplates) {
            try {
                FileReader fr = new FileReader(file);
                templateText = getStringFromStream(fr);
                templateData.put(file, templateText);
            } catch (IOException e) {

                templateData.put(file, NO_TEMPLATE);
            }

        } else if (templateText.equals(NO_TEMPLATE)) {
            return null;
        }
        if (templateText == null) {
            return null;
        }
        return new SimpleTemplate(templateText);
    }

    public static String getStringFromStream(InputStream ins) throws IOException {
        return getStringFromStream(new InputStreamReader(ins));
    }

    public static String getStringFromStream(Reader reader) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(reader);
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        String filename = args[0];
        String param = args[1];
        String value = args[2];

        FileReader fr = new FileReader(filename);
        String templateText = getStringFromStream(fr);
        SimpleTemplate template = new SimpleTemplate(templateText);

        template.replace(param, value);
        template.write(System.out);
    }

    public static void setCacheTemplates(boolean b) {
        cacheTemplates = b;
    }

    private void init(String s) {
        this.buff = new StringBuffer(s);
    }

    public void replace(String param, String value) {
        int[] range;
        while ((range = findTemplate(param)) != null) {
            this.buff.replace(range[0], range[1], value);
        }
    }

    int[] findTemplate(String name) {
        String text = this.buff.toString();
        int len = text.length();

        int start = 0;

        while (start < len) {

            int cstart = text.indexOf("<!--", start);
            if (cstart == -1)
                return null;
            int cend = text.indexOf("-->", cstart);
            if (cend == -1)
                return null;
            cend += "-->".length();

            int tstart = text.indexOf("TEMPLATE-", cstart);
            if (tstart == -1) {
                start = cend;

                continue;
            }

            if (tstart > cend) {
                start = cend;

                continue;
            }

            int pstart = tstart + "TEMPLATE-".length();

            int pend = len;
            for (pend = pstart; pend < len; pend++) {
                char c = text.charAt(pend);
                if (c == ' ' || c == '\t' || c == '-')
                    break;
            }
            if (pend >= len) {
                return null;
            }
            String param = text.substring(pstart, pend);

            if (param.equals(name)) {
                return new int[]{cstart, cend};
            }

            start = cend;
        }

        return null;
    }

    public String toString() {
        return this.buff.toString();
    }

    public void write(PrintWriter out) {
        out.println(toString());
    }

    public void write(PrintStream out) {
        out.println(toString());
    }
}

