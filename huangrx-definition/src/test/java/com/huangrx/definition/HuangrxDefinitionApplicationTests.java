package com.huangrx.definition;

import net.coobird.thumbnailator.Thumbnails;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

@SpringBootTest
class HuangrxDefinitionApplicationTests {

    @Value("${goods.sortByName:}")
    String sortByName;

    @Test
    void contextLoads() {

        System.out.println("==" + sortByName + "==");

        System.out.println(EncryptUtil.XOREncode("412824199910290300", "foliday"));

        System.out.println(BigDecimalUtils.div("0.1255", "1", 3));

        //System.out.println(BigDecimalUtils.div("0.1255", "0", 3));

        System.out.println(BigDecimal.ZERO.compareTo(BigDecimal.valueOf(0.0)) != 0);

        System.out.println(Validator.isChinese("huang"));

        System.out.println(UuidUtil.get4UUID());

        System.out.println(UUID.randomUUID());

        System.out.println(BigDecimalUtils.div(0.01, 2, 2));

        Double a = 1d;
        Double b = 2d;
        System.out.println(a.compareTo(b));
        System.out.println(BigDecimalUtils.sub(a,b));
    }

    public static void main(String[] args) {

        //Integer good = 2457;
        //List<Integer> list = new ArrayList<>();
        //Map<String, List<Integer>> map = new HashMap<>();
        //list.add(good);
        //map.put("goodIds", list);
        //System.out.println(JSONObject.toJSONString(map));


        //Integer a = null;
        //
        //if ( a == 0) {
        //    System.out.println(123);
        //}



        List<Model> models = new ArrayList<>();
        Model model1 = new Model();
        model1.setAge(300);
        model1.setName("a");
        models.add(model1);

        Model model2 = new Model();
        model2.setAge(500);
        model2.setName("c");
        models.add(model2);

        Model model3 = new Model();
        model3.setAge(100);
        model3.setName("b");
        models.add(model3);

        System.out.println("-----排序前-----");
        // 排序前
        for (Model contract : models) {
            System.out.println(contract.getName() + " " + contract.getAge());
        }

        System.out.println("-----排序后，根据age排序-----");
        Collections.sort(models, Comparator.comparing(Model::getAge));
        // 排序后
        for (Model model : models) {
            System.out.println(model.getName() + " " + model.getAge());
        }

        System.out.println("-----排序后，根据age排倒序-----");
        Collections.sort(models, Comparator.comparing(Model::getAge).reversed());
        // 排序后
        for (Model model : models) {
            System.out.println(model.getName() + " " + model.getAge());
        }

        System.out.println("-----排序后，根据name排序-----");
        Collections.sort(models, Comparator.comparing(Model::getName));
        // 排序后
        for (Model model : models) {
            System.out.println(model.getName() + " " + model.getAge());
        }

    }

    static class Model {
        public String name;
        public Integer age;

        public Model() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Model{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    @Test
    void test2() {
        List<Model> models = new ArrayList<>();
        Model model1 = new Model();
        model1.setAge(300);
        model1.setName("a");
        models.add(model1);

        Model model2 = new Model();
        model2.setAge(500);
        model2.setName("c");
        models.add(model2);

        Model model3 = new Model();
        model3.setAge(100);
        model3.setName("b");
        models.add(model3);

        Model model = models.get(2);
        model.setAge(1000);

        System.out.println(models.get(2).toString());
    }

    @Test
    void test5() throws IOException {
//        String s = PhotoUtils.ImageToBase64ByOnline("https://images.huangrx.cn/uploads/2022/08/24/630606ff4871e.png");
//        System.out.println(s);
//        PhotoUtils.Base64ToImage(s, "/Users/hrenxiang/Downloads/1.png");


        ByteArrayOutputStream data = new ByteArrayOutputStream();
        //InputStream inputStream = FileUtils.downloadByUrl("https://images.huangrx.cn/uploads/2022/08/24/630606ff4871e.png");
        try (FileInputStream inputStream = new FileInputStream(new File("/Users/hrenxiang/Downloads/630606ff4871e.png"))) {
            byte[] by = new byte[1024];
            // 将内容读取内存中
            int len = -1;
            while ((len = inputStream.read(by)) != -1) {
                data.write(by, 0, len);
            }
        }
//        InputStream stream = new ByteArrayInputStream(data.toByteArray());
//        BufferedImage bim = ImageIO.read(stream);
//        //计算宽高
//        int srcWidth = bim.getWidth();
//        int srcHeight = bim.getHeight();
//        int destWidth = new BigDecimal(srcWidth).multiply(new BigDecimal(0.9)).intValue();
//        int destHeight = new BigDecimal(srcHeight).multiply(new BigDecimal(0.9)).intValue();
//        BufferedImage output = Thumbnails.of(bim)
//                .size(destWidth, destHeight)
//                .outputQuality(0.9).asBufferedImage();
//        String base64Out = PhotoUtils.imageToBase64(output);

        // 转换为base64即可，无需上述累赘操作
        BASE64Encoder encoder = new BASE64Encoder();
        String base64Out = encoder.encode(data.toByteArray());

        // 递归去计算是否达到需要的尺寸
        String s = PhotoUtils.compressPicCycle(base64Out, 1536, 0.9);
        PhotoUtils.Base64ToImage(s, "/Users/hrenxiang/Downloads/2.png");
    }

}