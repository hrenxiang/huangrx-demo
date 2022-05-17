package com.huangrx.zxing.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.Hashtable;

/**
 * 二维码生成 控制器
 *
 * @author hrenxiang
 * @since 2022-05-17 1:20 PM
 */
@Slf4j
@SpringBootTest
public class Api {

    @Test
    public void generateQrCode() throws IOException, WriterException {
        String content = "https://www.baidu.com";

        Hashtable hints = new Hashtable();
        // 指定纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300,
                hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        // 存到磁盘
        OutputStream outputStream = Files.newOutputStream(new File("/Users/hrenxiang/Downloads/badu.jpg").toPath());
        ImageIO.write(image, "jpg", outputStream);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * hutool util
     */
    @Test
    public void hutoolImage() {
        QrCodeUtil.generate("https://www.baidu.com", 200, 200, new File("/Users/hrenxiang/Downloads/badu1.jpg"));

        QrConfig config = new QrConfig(300, 300);
        // 设置边距，既二维码和背景之间的边距
        config.setMargin(3);
        // 设置前景色，既二维码颜色（青色）
        config.setForeColor(Color.GREEN);
        // 设置背景色（灰色）
        config.setBackColor(Color.BLACK);

        // 生成二维码到文件，也可以到流
        QrCodeUtil.generate("http://hutool.cn/", config, FileUtil.file("/Users/hrenxiang/Downloads/qrcode.jpg"));
    }

    /**
     * 附带logo小图标
     */
    @Test
    public void logoQrCode() {
        QrCodeUtil.generate(
                "http://hutool.cn/",
                QrConfig.create().setImg("/Users/hrenxiang/Downloads/qrcode.jpg"),
                FileUtil.file("/Users/hrenxiang/Downloads/qrcodeWithLogo.jpg")
        );
    }

    /**
     * 识别二维码
     */
    @Test
    public void decodeQrCode() {
        String decode = QrCodeUtil.decode(FileUtil.file("/Users/hrenxiang/Downloads/qrcodeWithLogo.jpg"));
        System.out.println(decode);
    }
}

