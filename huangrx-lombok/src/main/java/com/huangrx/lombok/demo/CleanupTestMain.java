package com.huangrx.lombok.demo;

import lombok.Cleanup;

import java.io.*;

/**
 * `@Cleanup`：自动管理资源，用在局部变量之前，在当前变量范围内即将执行完毕退出之前会自动清理资源，自动生成`try-finally`这样的代码来关闭流
 *
 * @author hrenxiang
 * @since 2022-04-25 2:21 PM
 */
public class CleanupTestMain {

    /**
     * // main方法中的代码，将生成以下代码
     *         @Cleanup FileInputStream inStream = new FileInputStream(in);
     *         try {
     *             @Cleanup FileOutputStream outStream = new FileOutputStream(out);
     *             try {
     *                 byte[] b = new byte[65536];
     *                 while (true) {
     *                     int r = inStream.read(b);
     *                     if (r == -1) {
     *                          break;
     *                     }
     *                     outStream.write(b, 0, r);
     *                 }
     *             } finally {
     *                 if (outStream != null) outStream.close();
     *             }
     *         } finally {
     *             if (inStream != null) inStream.close();
     *         }
     */
    public static void main(String[] args) throws IOException {
        @Cleanup FileInputStream inStream = new FileInputStream(new File(""));
        @Cleanup FileOutputStream outStream = new FileOutputStream(new File(""));
        byte[] b = new byte[65536];
        while (true) {
            int r = inStream.read(b);
            if (r == -1) {
                break;
            }
            outStream.write(b, 0, r);
        }
    }

}
