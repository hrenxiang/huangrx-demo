package com.huangrx.huangrx.redis.IO;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author hrenxiang
 * @since 2022-08-29 15:53
 */
public class Client1 {
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
