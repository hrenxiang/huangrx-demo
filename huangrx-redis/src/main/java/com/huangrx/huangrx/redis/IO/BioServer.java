package com.huangrx.huangrx.redis.IO;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * BIO服务端 socket
 *
 * @author hrenxiang
 * @since 2022-08-29 15:39
 */
public class BioServer {

    private static byte[] bytes = new byte[1024];

    public static void main(String[] args) throws RuntimeException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(8080));

            while (true) {
                System.out.println("开始等待接收数据中...");
                Socket accept = serverSocket.accept();
                InputStream inputStream = accept.getInputStream();
//                int len;
//                while ((len = inputStream.read(bytes)) != -1) {
//                    System.out.println("接收到的数据是：" + new String(bytes, 0, len));
//                }
                int read = inputStream.read(bytes);
                System.out.println("接收到的数据是：" + new String(bytes,0 , read));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {

            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
