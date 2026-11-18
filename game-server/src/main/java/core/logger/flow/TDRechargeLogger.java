package core.logger.flow;

import BaseCommon.CommLog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import core.server.ServerConfig;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.net.HttpURLConnection;

public class TDRechargeLogger {
    private static TDRechargeLogger instance = new TDRechargeLogger();
    private String appID = System.getProperty("TDRecharge.appID");

    public static TDRechargeLogger getInstance() {
        return instance;
    }

    public void sendRechargeLog(
        String msgID,
        String status,
        String OS,
        String accountID,
        String orderID,
        double currencyAmount,
        String currencyType,
        double virtualCurrencyAmount,
        long chargeTime,
        String iapID,
        int level
    ) {
        try {
            JsonArray arry = new JsonArray();
            JsonObject param = new JsonObject();
            param.addProperty("msgID", msgID);
            param.addProperty("status", status);
            param.addProperty("OS", OS);
            param.addProperty("accountID", accountID);
            param.addProperty("orderID", orderID);
            param.addProperty("currencyAmount", currencyAmount);
            param.addProperty("currencyType", currencyType);
            param.addProperty("virtualCurrencyAmount", virtualCurrencyAmount);
            param.addProperty("chargeTime", chargeTime);
            param.addProperty("iapID", iapID);
            param.addProperty("gameServer", this.makeSign());
            param.addProperty("level", level);
            arry.add(param);
            byte[] dataByte = gzip(arry.toString());
            TDRechargeLogger.HttpClient clinet = new TDRechargeLogger.HttpClient("api.talkinggame.com", "80", "/api/charge/" + this.appID);
            CommLog.warn("result data : " + clinet.doPost(dataByte));
        } catch (Exception var19) {
            CommLog.warn("TD充值日志发送失败:{}", var19.toString());
        }
    }

    public String makeSign() {
        String world_id = System.getProperty("world_sid", "0");
        String server_id = String.valueOf(ServerConfig.ServerID());
        if (world_id.equals("3001")) {
            server_id = "T" + ServerConfig.ServerID();
        }

        if (world_id.equals("60001")) {
            server_id = "Y" + ServerConfig.ServerID();
        }

        if (world_id.equals("40001")) {
            server_id = "Z" + ServerConfig.ServerID();
        }

        if (world_id.equals("10001")) {
            server_id = "A" + ServerConfig.ServerID();
        }

        if (world_id.equals("30001")) {
            server_id = "F" + ServerConfig.ServerID();
        }

        return server_id;
    }

    private static byte[] gzip(String content) {
        ByteArrayOutputStream baos = null;
        GZIPOutputStream out = null;
        byte[] ret = null;

        try {
            baos = new ByteArrayOutputStream();
            out = new GZIPOutputStream(baos);
            out.write(content.getBytes());
            out.close();
            baos.close();
            ret = baos.toByteArray();
        } catch (FileNotFoundException var20) {
            var20.printStackTrace();
        } catch (IOException var21) {
            var21.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    baos.close();
                } catch (IOException var19) {
                    var19.printStackTrace();
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException var18) {
                    var18.printStackTrace();
                }
            }
        }

        return ret;
    }

    public static class HttpClient {
        HttpURLConnection _HttpURLConnection = null;
        URL url = null;
        private String DEFAULT_PROTOCOL = "http";
        private String SLASH = "/";
        private String COLON = ":";
        public String DEFAULT_NET_ERROR = "NetError";
        public String POST = "POST";

        public String doPost(byte[] Message) {
            String result = "";

            try {
                this._HttpURLConnection = (HttpURLConnection)this.url.openConnection();
                this._HttpURLConnection.setRequestMethod(this.POST);
                this._HttpURLConnection.setDoOutput(true);
                this._HttpURLConnection.setRequestProperty("Content-Type", "application/msgpack");
                this._HttpURLConnection.setRequestProperty("Content-Length", String.valueOf(Message.length));
                DataOutputStream ds = new DataOutputStream(this._HttpURLConnection.getOutputStream());
                ds.write(Message);
                ds.flush();
                ds.close();
                result = this._gzipStream2Str(this._HttpURLConnection.getInputStream());
                this._HttpURLConnection.disconnect();
            } catch (Exception var4) {
                this._HttpURLConnection.disconnect();
                var4.printStackTrace();
            }

            return result;
        }

        private String _gzipStream2Str(InputStream inputStream) throws IOException {
            GZIPInputStream gzipinputStream = new GZIPInputStream(inputStream);
            byte[] buf = new byte[1024];
            int num = -1;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while ((num = gzipinputStream.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, num);
            }

            return new String(baos.toByteArray(), "utf-8");
        }

        public HttpClient(String ServerName, String ServerPort, String QuestPath) {
            try {
                String ServerURL = "";
                ServerURL = ServerURL + this.DEFAULT_PROTOCOL;
                ServerURL = ServerURL + this.COLON;
                ServerURL = ServerURL + this.SLASH;
                ServerURL = ServerURL + this.SLASH;
                ServerURL = ServerURL + ServerName;
                if (ServerPort != null && ServerPort.trim().length() > 0) {
                    ServerURL = ServerURL + this.COLON;
                    ServerURL = ServerURL + ServerPort.trim();
                }

                if (QuestPath.charAt(0) != '/') {
                    ServerURL = ServerURL + this.SLASH;
                }

                ServerURL = ServerURL + QuestPath;
                this.url = new URL(ServerURL);
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }
    }
}
