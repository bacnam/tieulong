package org.apache.mina.filter.ssl;

import java.io.*;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

public class KeyStoreFactory {
    private String type = "JKS";

    private String provider = null;

    private char[] password = null;

    private byte[] data = null;

    public KeyStore newInstance() throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore ks;
        if (this.data == null) {
            throw new IllegalStateException("data property is not set.");
        }

        if (this.provider == null) {
            ks = KeyStore.getInstance(this.type);
        } else {
            ks = KeyStore.getInstance(this.type, this.provider);
        }

        InputStream is = new ByteArrayInputStream(this.data);
        try {
            ks.load(is, this.password);
        } finally {
            try {
                is.close();
            } catch (IOException ignored) {
            }
        }

        return ks;
    }

    public void setType(String type) {
        if (type == null) {
            throw new IllegalArgumentException("type");
        }
        this.type = type;
    }

    public void setPassword(String password) {
        if (password != null) {
            this.password = password.toCharArray();
        } else {
            this.password = null;
        }
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setData(byte[] data) {
        byte[] copy = new byte[data.length];
        System.arraycopy(data, 0, copy, 0, data.length);
        this.data = copy;
    }

    private void setData(InputStream dataStream) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            while (true) {
                int data = dataStream.read();
                if (data < 0) {
                    break;
                }
                out.write(data);
            }
            setData(out.toByteArray());
        } finally {
            try {
                dataStream.close();
            } catch (IOException e) {
            }
        }
    }

    public void setDataFile(File dataFile) throws IOException {
        setData(new BufferedInputStream(new FileInputStream(dataFile)));
    }

    public void setDataUrl(URL dataUrl) throws IOException {
        setData(dataUrl.openStream());
    }
}

