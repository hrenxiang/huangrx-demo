package com.huangrx.definition;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * @author hrenxiang
 * @since 2022/6/15 22:37
 */
public class PhotoUtils {

    private final static Integer KB_SIZE = 1024;
    public static final float DEFAULT_QUALITY = 0.2125f;

    /**
     * 在线图片转换成base64字符串
     *
     * @param imgURL 图片线上路径
     * @return
     */
    public static String ImageToBase64ByOnline(String imgURL) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            // 创建URL
            URL url = new URL(imgURL);
            byte[] by = new byte[1024];
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();

            // 将内容读取内存中
            int len = -1;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
            // 关闭流
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        String str = encoder.encode(data.toByteArray());
        //压缩Base64编码并输出
        return resizeImageTo50K(str);
    }

    /**
     * 压缩base64编码至50K以内
     *
     * @param base64Img
     * @return
     */
    public static String resizeImageTo50K(String base64Img) {
        return resizeImage(base64Img, 50);
    }

    /**
     * 压缩base64编码至目标大小以内，单位/kb
     *
     * @param base64Img  目标base64
     * @param targetSize 目标大小，单位kb
     * @return
     */
    public static String resizeImage(String base64Img, int targetSize) {
        try {
            return compressPicCycle(base64Img, targetSize, 0.8);
        } catch (Exception e) {
            e.printStackTrace();
            return base64Img;
        }
    }

    /**
     * @param base64     目标base64
     * @param targetSize 目标大小，单位kb
     * @param accuracy   精度，递归压缩的比率，建议小于0.9
     * @return
     * @throws IOException
     */
    public static String compressPicCycle(String base64, long targetSize, double accuracy) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes1 = decoder.decodeBuffer(base64);
        InputStream stream = new ByteArrayInputStream(bytes1);
        BufferedImage bim = ImageIO.read(stream);
        //判断大小，如果小于50kb，不用压缩，如果大于等于50kb,需要压缩
        if (bytes1.length <= targetSize * KB_SIZE) {
            return base64;
        }
        //计算宽高
        int srcWidth = bim.getWidth();
        int srcHeight = bim.getHeight();
        int destWidth = new BigDecimal(srcWidth).multiply(new BigDecimal(accuracy)).intValue();
        int destHeight = new BigDecimal(srcHeight).multiply(new BigDecimal(accuracy)).intValue();
        BufferedImage output = Thumbnails.of(bim)
                .size(destWidth, destHeight)
                .outputQuality(accuracy).asBufferedImage();
        String base64Out = imageToBase64(output);
        return compressPicCycle(base64Out, targetSize, accuracy);
    }

    /**
     * BufferedImage转换成base64，默认是png类型的图片
     *
     * @param bufferedImage 目标图片
     * @return
     */
    public static String imageToBase64(BufferedImage bufferedImage) {
        Base64.Encoder encoder = Base64.getEncoder();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", baos);
        } catch (IOException e) {
            //todo: handler
        }
        return new String(encoder.encode((baos.toByteArray())));
    }

    /**
     * BufferedImage转换成base64
     *
     * @param bufferedImage 目标图片
     * @param imageType     图片类型
     * @return
     */
    public static String imageToBase64(BufferedImage bufferedImage, String imageType) {
        Base64.Encoder encoder = Base64.getEncoder();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, imageType, baos);
        } catch (IOException e) {
        }
        return new String(encoder.encode((baos.toByteArray())));
    }

    /**
     * 本地图片转换成base64字符串
     *
     * @param imgFile 图片本地路径
     * @return
     */
    public static String ImageToBase64ByLocal(String imgFile) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        InputStream in = null;
        byte[] data = null;

        // 读取图片字节数组
        try {
            in = Files.newInputStream(Paths.get(imgFile));

            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(data);
    }


    /**
     * base64字符串转换成图片
     *
     * @param imgStr      base64字符串
     * @param imgFilePath 图片存放路径
     * @return
     */
    public static boolean Base64ToImage(String imgStr, String imgFilePath) {
        // 对字节数组字符串进行Base64解码并生成图片

        // 图像数据为空
        if (StringUtils.isEmpty(imgStr)) {
            return false;
        }

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                // 调整异常数据
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = Files.newOutputStream(Paths.get(imgFilePath));
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * base64字符串转换为输入流
     *
     * @param base64string
     * @return
     */
    private static InputStream Base64ToInputStream(String base64string) {
        ByteArrayInputStream stream = null;
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] bytes1 = decoder.decode(base64string);
            stream = new ByteArrayInputStream(bytes1);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return stream;
    }

    /**
     * base64字符串转换为BufferedImage
     *
     * @param base64string
     * @return
     */
    public static BufferedImage base64ToBufferedImage(String base64string) {
        BufferedImage image = null;
        try {
            InputStream stream = Base64ToInputStream(base64string);
            image = ImageIO.read(stream);
        } catch (IOException e) {
            //TODO: handler exception
        }
        return image;
    }

    /**
     * 根据base64字符串计算文件大小
     *
     * @param base64String
     * @return
     */
    public static double base64file_size(String base64String) {
        //1.获取base64字符串长度(不含data:audio/wav;base64,文件头)
        int size0 = base64String.length();
        //2.获取字符串的尾巴的最后10个字符，用于判断尾巴是否有等号，正常生成的base64文件'等号'不会超过4个
        String tail = base64String.substring(size0 - 10);

        //3.找到等号，把等号也去掉,(等号其实是空的意思,不能算在文件大小里面)
        int equalIndex = tail.indexOf("=");
        if (equalIndex > 0) {
            size0 = size0 - (10 - equalIndex);
        }
        //4.计算后得到的文件流大小，单位为字节
        return size0 - ((double) size0 / 8) * 2;
    }


    /**
     * 添加图片水印操作(物理存盘,使用默认格式)
     *
     * @param imgPath  待处理图片
     * @param markPath 水印图片
     * @param x        水印位于图片左上角的 x 坐标值
     * @param y        水印位于图片左上角的 y 坐标值
     * @param alpha    水印透明度 0.1f ~ 1.0f
     * @param destPath 文件存放路径
     * @throws Exception
     */
    public static void addWaterMark(String imgPath, String markPath, int x, int y, float alpha, String destPath) throws Exception {
        try {
            BufferedImage bufferedImage = addWaterMark(imgPath, markPath, x, y, alpha);
            ImageIO.write(bufferedImage, imageFormat(imgPath), new File(destPath));
        } catch (Exception e) {
            throw new RuntimeException("添加图片水印异常");
        }
    }


    /**
     * 添加图片水印操作(物理存盘,自定义格式)
     *
     * @param imgPath  待处理图片
     * @param markPath 水印图片
     * @param x        水印位于图片左上角的 x 坐标值
     * @param y        水印位于图片左上角的 y 坐标值
     * @param alpha    水印透明度 0.1f ~ 1.0f
     * @param format   添加水印后存储的格式
     * @param destPath 文件存放路径
     * @throws Exception
     */
    public static void addWaterMark(String imgPath, String markPath, int x, int y, float alpha, String format, String destPath) throws Exception {
        try {
            BufferedImage bufferedImage = addWaterMark(imgPath, markPath, x, y, alpha);
            ImageIO.write(bufferedImage, format, new File(destPath));
        } catch (Exception e) {
            throw new RuntimeException("添加图片水印异常");
        }
    }


    /**
     * 添加图片水印操作,返回BufferedImage对象
     *
     * @param imgPath  待处理图片
     * @param markPath 水印图片
     * @param x        水印位于图片左上角的 x 坐标值
     * @param y        水印位于图片左上角的 y 坐标值
     * @param alpha    水印透明度 0.1f ~ 1.0f
     * @return 处理后的图片对象
     * @throws Exception
     */
    public static BufferedImage addWaterMark(String imgPath, String markPath, int x, int y, float alpha) throws Exception {
        BufferedImage targetImage = null;
        try {
            // 加载待处理图片文件
            Image img = ImageIO.read(new File(imgPath));

            //创建目标图象文件
            targetImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = targetImage.createGraphics();
            g.drawImage(img, 0, 0, null);

            // 加载水印图片文件
            Image markImg = ImageIO.read(new File(markPath));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawImage(markImg, x, y, null);
            g.dispose();
        } catch (Exception e) {
            throw new RuntimeException("添加图片水印操作异常");
        }
        return targetImage;

    }

    /**
     * 添加文字水印操作(物理存盘,使用默认格式)
     *
     * @param imgPath  待处理图片
     * @param text     水印文字
     * @param font     水印字体信息    不写默认值为宋体
     * @param color    水印字体颜色
     * @param x        水印位于图片左上角的 x 坐标值
     * @param y        水印位于图片左上角的 y 坐标值
     * @param alpha    水印透明度 0.1f ~ 1.0f
     * @param destPath 文件存放路径
     * @throws Exception
     */
    public static void addTextMark(String imgPath, String text, Font font, Color color, float x, float y, float alpha, String destPath) throws Exception {
        try {
            BufferedImage bufferedImage = addTextMark(imgPath, text, font, color, x, y, alpha);
            ImageIO.write(bufferedImage, imageFormat(imgPath), new File(destPath));
        } catch (Exception e) {
            throw new RuntimeException("图片添加文字水印异常");
        }
    }

    /**
     * 添加文字水印操作(物理存盘,自定义格式)
     *
     * @param imgPath  待处理图片
     * @param text     水印文字
     * @param font     水印字体信息    不写默认值为宋体
     * @param color    水印字体颜色
     * @param x        水印位于图片左上角的 x 坐标值
     * @param y        水印位于图片左上角的 y 坐标值
     * @param alpha    水印透明度 0.1f ~ 1.0f
     * @param format   添加水印后存储的格式
     * @param destPath 文件存放路径
     * @throws Exception
     */
    public static void addTextMark(String imgPath, String text, Font font, Color color, float x, float y, float alpha, String format, String destPath) throws Exception {
        try {
            BufferedImage bufferedImage = addTextMark(imgPath, text, font, color, x, y, alpha);
            ImageIO.write(bufferedImage, format, new File(destPath));
        } catch (Exception e) {
            throw new RuntimeException("图片添加文字水印异常");
        }
    }

    /**
     * 添加文字水印操作,返回BufferedImage对象
     *
     * @param imgPath 待处理图片
     * @param text    水印文字
     * @param font    水印字体信息    不写默认值为宋体
     * @param color   水印字体颜色
     * @param x       水印位于图片左上角的 x 坐标值
     * @param y       水印位于图片左上角的 y 坐标值
     * @param alpha   水印透明度 0.1f ~ 1.0f
     * @return 处理后的图片对象
     * @throws Exception
     */

    public static BufferedImage addTextMark(String imgPath, String text, Font font, Color color, float x, float y, float alpha) throws Exception {
        BufferedImage targetImage = null;
        try {
            Font Dfont = (font == null) ? new Font("宋体", 20, 13) : font;
            Image img = ImageIO.read(new File(imgPath));
            //创建目标图像文件
            targetImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = targetImage.createGraphics();
            g.drawImage(img, 0, 0, null);
            g.setColor(color);
            g.setFont(Dfont);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawString(text, x, y);
            g.dispose();
        } catch (Exception e) {
            throw new RuntimeException("添加文字水印操作异常");
        }
        return targetImage;
    }


    /**
     * 压缩图片操作(文件物理存盘,使用默认格式)
     *
     * @param imgPath  待处理图片
     * @param quality  图片质量(0-1之間的float值)
     * @param width    输出图片的宽度    输入负数参数表示用原来图片宽
     * @param height   输出图片的高度    输入负数参数表示用原来图片高
     * @param autoSize 是否等比缩放 true表示进行等比缩放 false表示不进行等比缩放
     * @param destPath 文件存放路径
     * @throws Exception
     */
    public static void compressImage(String imgPath, float quality, int width, int height, boolean autoSize, String destPath) throws Exception {
        try {
            BufferedImage bufferedImage = compressImage(imgPath, quality, width, height, autoSize);
            ImageIO.write(bufferedImage, imageFormat(imgPath), new File(destPath));
        } catch (Exception e) {
            throw new RuntimeException("图片压缩异常");
        }

    }


    /**
     * 压缩图片操作(文件物理存盘,可自定义格式)
     *
     * @param imgPath  待处理图片
     * @param quality  图片质量(0-1之間的float值)
     * @param width    输出图片的宽度    输入负数参数表示用原来图片宽
     * @param height   输出图片的高度    输入负数参数表示用原来图片高
     * @param autoSize 是否等比缩放 true表示进行等比缩放 false表示不进行等比缩放
     * @param format   压缩后存储的格式
     * @param destPath 文件存放路径
     * @throws Exception
     */
    public static void compressImage(String imgPath, float quality, int width, int height, boolean autoSize, String format, String destPath) throws Exception {
        try {
            BufferedImage bufferedImage = compressImage(imgPath, quality, width, height, autoSize);
            ImageIO.write(bufferedImage, format, new File(destPath));
        } catch (Exception e) {
            throw new RuntimeException("图片压缩异常");
        }
    }


    /**
     * 压缩图片操作,返回BufferedImage对象
     *
     * @param imgPath  待处理图片
     * @param quality  图片质量(0-1之間的float值)
     * @param width    输出图片的宽度    输入负数参数表示用原来图片宽
     * @param height   输出图片的高度    输入负数参数表示用原来图片高
     * @param autoSize 是否等比缩放 true表示进行等比缩放 false表示不进行等比缩放
     * @return 处理后的图片对象
     * @throws Exception
     */
    public static BufferedImage compressImage(String imgPath, float quality, int width, int height, boolean autoSize) throws Exception {
        BufferedImage targetImage = null;
        if (quality < 0F || quality > 1F) {
            quality = DEFAULT_QUALITY;
        }
        try {
            Image img = ImageIO.read(new File(imgPath));
            //如果用户输入的图片参数合法则按用户定义的复制,负值参数表示执行默认值
            int newwidth = (width > 0) ? width : img.getWidth(null);
            //如果用户输入的图片参数合法则按用户定义的复制,负值参数表示执行默认值
            int newheight = (height > 0) ? height : img.getHeight(null);
            //如果是自适应大小则进行比例缩放
            if (autoSize) {
                // 为等比缩放计算输出的图片宽度及高度
                double Widthrate = ((double) img.getWidth(null)) / (double) width + 0.1;
                double heightrate = ((double) img.getHeight(null)) / (double) height + 0.1;
                double rate = Widthrate > heightrate ? Widthrate : heightrate;
                newwidth = (int) (((double) img.getWidth(null)) / rate);
                newheight = (int) (((double) img.getHeight(null)) / rate);
            }
            //创建目标图像文件
            targetImage = new BufferedImage(newwidth, newheight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = targetImage.createGraphics();
            g.drawImage(img, 0, 0, newwidth, newheight, null);
            //如果添加水印或者文字则继续下面操作,不添加的话直接返回目标文件----------------------
            g.dispose();

        } catch (Exception e) {
            throw new RuntimeException("图片压缩操作异常");
        }
        return targetImage;
    }


    /**
     * 图片黑白化操作(文件物理存盘,使用默认格式)
     *
     * @param imgPath  处理的图片对象
     * @param destPath 目标文件地址
     * @throws Exception
     */
    public static void imageGray(String imgPath, String destPath) throws Exception {
        imageGray(imgPath, imageFormat(imgPath), destPath);
    }


    /**
     * 图片黑白化操作(文件物理存盘,可自定义格式)
     *
     * @param imgPath  处理的图片对象
     * @param format   图片格式
     * @param destPath 目标文件地址
     * @throws Exception
     */
    public static void imageGray(String imgPath, String format, String destPath) throws Exception {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(imgPath));
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(cs, null);
            bufferedImage = op.filter(bufferedImage, null);
            ImageIO.write(bufferedImage, format, new File(destPath));
        } catch (Exception e) {
            throw new RuntimeException("图片灰白化异常");
        }
    }


    /**
     * 图片透明化操作(文件物理存盘,使用默认格式)
     *
     * @param imgPath  图片路径
     * @param destPath 图片存放路径
     * @throws Exception
     */
    public static void imageLucency(String imgPath, String destPath) throws Exception {
        try {
            BufferedImage bufferedImage = imageLucency(imgPath);
            ImageIO.write(bufferedImage, imageFormat(imgPath), new File(destPath));
        } catch (Exception e) {
            throw new RuntimeException("图片透明化异常");
        }
    }


    /**
     * 图片透明化操作(文件物理存盘,可自定义格式)
     *
     * @param imgPath  图片路径
     * @param format   图片格式
     * @param destPath 图片存放路径
     * @throws Exception
     */
    public static void imageLucency(String imgPath, String format, String destPath) throws Exception {
        try {
            BufferedImage bufferedImage = imageLucency(imgPath);
            ImageIO.write(bufferedImage, format, new File(destPath));
        } catch (Exception e) {
            throw new RuntimeException("图片透明化异常");
        }
    }

    /**
     * 图片透明化操作返回BufferedImage对象
     *
     * @param imgPath 图片路径
     * @return 透明化后的图片对象
     * @throws Exception
     */
    public static BufferedImage imageLucency(String imgPath) throws Exception {
        BufferedImage targetImage = null;
        try {
            //读取图片
            BufferedImage img = ImageIO.read(new FileInputStream(imgPath));
            //透明度
            int alpha = 0;
            //执行透明化
            executeRGB(img, alpha);
            targetImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = targetImage.createGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();
        } catch (Exception e) {
            throw new RuntimeException("图片透明化执行异常");
        }
        return targetImage;
    }

    /**
     * 执行透明化的核心算法
     *
     * @param img   图片对象
     * @param alpha 透明度
     * @throws Exception
     */
    public static void executeRGB(BufferedImage img, int alpha) throws Exception {
        int rgb = 0;//RGB值
        //x表示BufferedImage的x坐标，y表示BufferedImage的y坐标
        for (int x = img.getMinX(); x < img.getWidth(); x++) {
            for (int y = img.getMinY(); y < img.getHeight(); y++) {
                //获取点位的RGB值进行比较重新设定
                rgb = img.getRGB(x, y);
                int R = (rgb & 0xff0000) >> 16;
                int G = (rgb & 0xff00) >> 8;
                int B = (rgb & 0xff);
                if (((255 - R) < 30) && ((255 - G) < 30) && ((255 - B) < 30)) {
                    rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                    img.setRGB(x, y, rgb);
                }
            }
        }
    }


    /**
     * 图片格式转化操作(文件物理存盘)
     *
     * @param imgPath  原始图片存放地址
     * @param format   待转换的格式 jpeg,gif,png,bmp等
     * @param destPath 目标文件地址
     * @throws Exception
     */
    public static void formatConvert(String imgPath, String format, String destPath) throws Exception {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(imgPath));
            ImageIO.write(bufferedImage, format, new File(destPath));
        } catch (IOException e) {
            throw new RuntimeException("文件格式转换出错");
        }
    }


    /**
     * 图片格式转化操作返回BufferedImage对象
     *
     * @param bufferedImag BufferedImage图片转换对象
     * @param format        待转换的格式 jpeg,gif,png,bmp等
     * @param destPath      目标文件地址
     * @throws Exception
     */
    public static void formatConvert(BufferedImage bufferedImag, String format, String destPath) throws Exception {
        try {
            ImageIO.write(bufferedImag, format, new File(destPath));
        } catch (IOException e) {
            throw new RuntimeException("文件格式转换出错");
        }
    }


    /**
     * 获取图片文件的真实格式信息
     *
     * @param imgPath 图片原文件存放地址
     * @return 图片格式
     * @throws Exception
     */
    public static String imageFormat(String imgPath) throws Exception {
        String[] filess = imgPath.split("\\\\");
        String[] formats = filess[filess.length - 1].split("\\.");
        return formats[formats.length - 1];
    }

    public static void main(String[] args) {
        try {
            imageGray("D:\\picture\\01.jpg",
                    "D:\\picture\\100.jpg");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
