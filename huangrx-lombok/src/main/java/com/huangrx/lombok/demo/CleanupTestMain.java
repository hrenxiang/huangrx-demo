package com.huangrx.lombok.demo;

import lombok.Cleanup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * `@Cleanup`：自动管理资源，用在局部变量之前，在当前变量范围内即将执行完毕退出之前会自动清理资源，自动生成`try-finally`这样的代码来关闭流
 *
 * @author hrenxiang
 * @since 2022-04-25 2:21 PM
 */
public class CleanupTestMain {

    public static void main(String[] args) throws IOException {
        try {
            @Cleanup InputStream inputStream = new FileInputStream("");
            System.out.println(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // 上述等同于如下
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("");
            System.out.println(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
