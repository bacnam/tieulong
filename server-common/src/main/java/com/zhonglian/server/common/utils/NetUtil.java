package com.zhonglian.server.common.utils;

import BaseCommon.CommLog;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class NetUtil {
    public static String getHostName() {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            return ia.getHostName();
        } catch (UnknownHostException var1) {
            CommLog.error(NetUtil.class.getName(), (Throwable)var1);
            return null;
        }
    }

    public static int getHostIpInteger() {
        InetAddress ip = getHostIpAddr();
        return ip == null ? 0 : ipAddressToInt(ip);
    }

    public static int ipAddressToInt(InetAddress addr) {
        byte[] ip_arr = addr.getAddress();
        return ip_arr[3] & 0xFF | (ip_arr[2] & 0xFF) << 8 | (ip_arr[1] & 0xFF) << 16 | (ip_arr[0] & 0xFF) << 24;
    }

    public static InetAddress getHostIpAddr() {
        try {
            Enumeration<NetworkInterface> niEnums = NetworkInterface.getNetworkInterfaces();

            while (niEnums.hasMoreElements()) {
                NetworkInterface ni = niEnums.nextElement();
                if (!ni.isLoopback() && !ni.isPointToPoint() && !ni.isVirtual() && ni.isUp()) {
                    Enumeration<InetAddress> addrs = ni.getInetAddresses();

                    while (addrs.hasMoreElements()) {
                        InetAddress ip = addrs.nextElement();
                        if (ip != null && ip instanceof Inet4Address && !ip.isAnyLocalAddress() && !ip.isMulticastAddress()) {
                            return ip;
                        }
                    }
                }
            }
        } catch (SocketException var4) {
            CommLog.error(NetUtil.class.getName(), (Throwable)var4);
        }

        return null;
    }

    public static byte[] ipAddressStringToRaw(String ipString) {
        String[] ipSegs = ipString.split("\\.");
        return ipSegs.length != 4
            ? null
            : new byte[]{
                (byte)(Integer.parseInt(ipSegs[0]) & 0xFF),
                (byte)(Integer.parseInt(ipSegs[1]) & 0xFF),
                (byte)(Integer.parseInt(ipSegs[2]) & 0xFF),
                (byte)(Integer.parseInt(ipSegs[3]) & 0xFF)
            };
    }

    public static int ipAddressStringToInt(String ipString) {
        byte[] targets = ipAddressStringToRaw(ipString);
        return targets == null ? 0 : targets[3] & 0xFF | (targets[2] & 0xFF) << 8 | (targets[1] & 0xFF) << 16 | (targets[0] & 0xFF) << 24;
    }

    public static InetAddress ipAddressStringToInetAddr(String ipString) throws UnknownHostException {
        byte[] targets = ipAddressStringToRaw(ipString);
        if (targets == null) {
            throw new UnknownHostException(ipString);
        } else {
            return InetAddress.getByAddress(targets);
        }
    }

    public static String intToIpAddressString(int ip) {
        int[] targets = new int[]{ip >> 24 & 0xFF, ip >> 16 & 0xFF, ip >> 8 & 0xFF, ip & 0xFF};
        return String.format("%d.%d.%d.%d", targets[0], targets[1], targets[2], targets[3]);
    }
}
