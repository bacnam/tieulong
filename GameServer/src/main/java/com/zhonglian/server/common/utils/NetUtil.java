package com.zhonglian.server.common.utils;

import BaseCommon.CommLog;

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
        } catch (UnknownHostException ex) {
            CommLog.error(NetUtil.class.getName(), ex);

            return null;
        }
    }

    public static int getHostIpInteger() {
        InetAddress ip = getHostIpAddr();
        if (ip == null) {
            return 0;
        }

        return ipAddressToInt(ip);
    }

    public static int ipAddressToInt(InetAddress addr) {
        byte[] ip_arr = addr.getAddress();

        int res = ip_arr[3] & 0xFF | (ip_arr[2] & 0xFF) << 8 | (ip_arr[1] & 0xFF) << 16 | (ip_arr[0] & 0xFF) << 24;
        return res;
    }

    public static InetAddress getHostIpAddr() {
        try {
            Enumeration<NetworkInterface> niEnums = NetworkInterface.getNetworkInterfaces();
            while (niEnums.hasMoreElements()) {
                NetworkInterface ni = niEnums.nextElement();
                if (ni.isLoopback() || ni.isPointToPoint() || ni.isVirtual() || !ni.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addrs = ni.getInetAddresses();

                while (addrs.hasMoreElements()) {
                    InetAddress ip = addrs.nextElement();
                    if (ip != null && ip instanceof java.net.Inet4Address && !ip.isAnyLocalAddress() && !ip.isMulticastAddress()) {
                        return ip;
                    }
                }

            }

        } catch (SocketException ex) {
            CommLog.error(NetUtil.class.getName(), ex);
        }

        return null;
    }

    public static byte[] ipAddressStringToRaw(String ipString) {
        String[] ipSegs = ipString.split("\\.");
        if (ipSegs.length != 4) {
            return null;
        }
        byte[] targets = new byte[4];
        targets[0] = (byte) (Integer.parseInt(ipSegs[0]) & 0xFF);
        targets[1] = (byte) (Integer.parseInt(ipSegs[1]) & 0xFF);
        targets[2] = (byte) (Integer.parseInt(ipSegs[2]) & 0xFF);
        targets[3] = (byte) (Integer.parseInt(ipSegs[3]) & 0xFF);

        return targets;
    }

    public static int ipAddressStringToInt(String ipString) {
        byte[] targets = ipAddressStringToRaw(ipString);
        if (targets == null) {
            return 0;
        }

        int res = targets[3] & 0xFF | (targets[2] & 0xFF) << 8 | (targets[1] & 0xFF) << 16 | (targets[0] & 0xFF) << 24;
        return res;
    }

    public static InetAddress ipAddressStringToInetAddr(String ipString) throws UnknownHostException {
        byte[] targets = ipAddressStringToRaw(ipString);
        if (targets == null) {
            throw new UnknownHostException(ipString);
        }

        return InetAddress.getByAddress(targets);
    }

    public static String intToIpAddressString(int ip) {
        int[] targets = new int[4];
        targets[3] = ip & 0xFF;
        targets[2] = ip >> 8 & 0xFF;
        targets[1] = ip >> 16 & 0xFF;
        targets[0] = ip >> 24 & 0xFF;

        return String.format("%d.%d.%d.%d", new Object[]{Integer.valueOf(targets[0]), Integer.valueOf(targets[1]), Integer.valueOf(targets[2]), Integer.valueOf(targets[3])});
    }
}

