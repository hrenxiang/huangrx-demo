package com.huangrx.lombok.demo;

import lombok.SneakyThrows;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * ' @SneakyThrows '：自动抛受检异常，而无需显式在方法上使用throws语句
 *
 * @author hrenxiang
 * @since 2022-04-25 2:21 PM
 */
public class SneakyThrowsTestMain {

    @SneakyThrows(value = IOException.class)
    public void read() {
        InputStream inputStream = Files.newInputStream(Paths.get(""));
    }
    @SneakyThrows(value = UnsupportedEncodingException.class)
    public void write() {
        throw new UnsupportedEncodingException();
    }

    //相当于
    // public void read() throws FileNotFoundException { InputStream inputStream = new FileInputStream(""); }
    // public void write() throws UnsupportedEncodingException { throw new UnsupportedEncodingException(); }



}
