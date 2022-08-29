package com.huangrx.huangrx.redis.IO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author hrenxiang
 * @since 2022-08-29 16:00
 */
public class Client2 {
    public static void main(String[] args) {
        Socket socket = new Socket();
        try {
            // 与服务端建立连接
            SocketAddress socketAddress = new InetSocketAddress(InetAddress.getLocalHost(), 8080);
            socket.connect(socketAddress);

            BufferedReader br =new BufferedReader(new InputStreamReader(System.in));

            String line;
            while ((line = br.readLine()) != null) {
                socket.getOutputStream().write(line.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
