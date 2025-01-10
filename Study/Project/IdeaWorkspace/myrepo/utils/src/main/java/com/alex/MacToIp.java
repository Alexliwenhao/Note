package com.alex;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class MacToIp {
    public static void main(String[] args) throws SocketException, UnknownHostException {
        // 创建一个IPv4地址的InetAddress对象
        byte[] ipv4Address = new byte[] { (byte) 192, (byte) 168, 1, 100 };
        InetAddress ipv4 = InetAddress.getByAddress(ipv4Address);

        // 创建一个IPv6地址的InetAddress对象
        byte[] ipv6Address = new byte[] {
                0x20, 0x01, 0x0d, (byte) 0xb8, (byte) 0x85, (byte) 0xa3, 0x08, (byte) 0xd3,
                0x13, 0x19, 0x24, 0x08, 0x00, 0x20, 0x0c, 0x41
        };
        InetAddress ipv6 = InetAddress.getByAddress(ipv6Address);

        // 要查询的设备的MAC地址
        String macAddress = "60:30:d4:7b:dd:de";
        byte[] macBytes = parseMacAddress2(macAddress);
        InetAddress address = ipv6.getByAddress(macBytes);
        NetworkInterface ni = NetworkInterface.getByInetAddress(address);
        byte[] hardwareAddress = ni.getHardwareAddress();
        System.out.println("IP address: " + address.getHostAddress());
    }

    private static byte[] parseMacAddress(String macAddress) {
        String[] macBytes = macAddress.split(":");
        byte[] bytes = new byte[macBytes.length];
        for (int i = 0; i < macBytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(macBytes[i], 16);
        }
        return bytes;
    }

    private static byte[] parseMacAddress2(String macAddress) {
        String[] macBytes = macAddress.split(":");
        byte[] bytes = new byte[16];

        // Set the prefix to fe80::/10
        bytes[0] = (byte) 0xfe;
        bytes[1] = (byte) 0x80;

        // Set the suffix to the MAC address
        for (int i = 0; i < macBytes.length; i++) {
            bytes[i + 8] = (byte) Integer.parseInt(macBytes[i], 16);
        }

        // Set the universal/local bit
        bytes[8] |= (byte) 0x02;

        return bytes;
    }

}