package com.huangrx.hutool;

import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.sun.crypto.provider.HmacSHA1;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;
import org.springframework.util.Base64Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmConstraints;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * 加解密测试
 *
 * @author hrenxiang
 * @since 2022/6/13 21:07
 */
public class EncryptionApiTest {

    @Test
    public void testEncrypt() throws UnsupportedEncodingException, NoSuchAlgorithmException {

        String test = "AppId=app_temp&Timestamp=1548840381";

        String str = URLEncoder.encode(test, CharsetUtil.UTF_8);

        String lowerCase = str.toLowerCase();

        byte[] key = "c3556d279cf0545543a3ed1d38492424".getBytes(StandardCharsets.UTF_8);

        HMac hMac = new HMac(HmacAlgorithm.HmacSHA1, key);

        String s = hMac.digestBase64("appid%3dapp_temp%26timestamp%3d1548840381", false);
        System.out.println(s);

        System.out.println(URLUtil.encode(s));

        //MessageDigest instance = MessageDigest.getInstance(HmacAlgorithm.HmacSHA1.getValue());
        //System.out.println(Base64Utils.encodeToString(instance.digest(key)));

        System.out.println(genHMAC(lowerCase, "c3556d279cf0545543a3ed1d38492424"));

        String encode = URLEncoder.encode(s, CharsetUtil.UTF_8);
        System.out.println(encode);
        //q2vAPbGgOT5Co0zvzGOowFIubwA%3d
        //q2vAPbGgOT5Co0zvzGOowFIubwA%3D
    }

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    /**
     * 使用 HMAC-SHA1 签名方法对data进行签名
     *
     * @param data 被签名的字符串
     * @param key  密钥
     * @return 加密后的字符串
     */
    public static String genHMAC(String data, String key) {
        byte[] result = null;
        try {
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKeySpec signinKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            //用给定密钥初始化 Mac 对象
            mac.init(signinKey);
            //完成 Mac 操作
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = Base64.encodeBase64(rawHmac);

        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        } catch (InvalidKeyException e) {
            System.err.println(e.getMessage());
        }
        if (null != result) {
            return new String(result);
        } else {
            return null;
        }
    }
}
