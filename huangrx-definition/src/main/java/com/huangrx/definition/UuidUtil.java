package com.huangrx.definition;

import java.util.UUID;

/**
 * @author    hrenxiang
 * @since     2022/7/1 15:25
 */
public class UuidUtil {

    public static String uuid32() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    /**
     * 获得16个长度的十六进制的UUID
     */
    public static String get16UUID() {
        UUID id = UUID.randomUUID();
        String[] idd = id.toString().split("-");
        return idd[0] + idd[1] + idd[2];
    }

    /**
     * 获得20个长度的十六进制的UUID
     */
    public static String uuid20() {
        UUID id = UUID.randomUUID();
        String[] idd = id.toString().split("-");
        return idd[0] + idd[1] + idd[2] + idd[3];
    }
    /**
     * 获得12个长度的十六进制的UUID
     */
    public static String get12UUID() {
        UUID id = UUID.randomUUID();
        String[] idd = id.toString().split("-");
        return idd[0] + idd[1] ;
    }
    /**
     * 获得6个长度的十六进制的UUID
     */
    public static String get6UUID() {
        UUID id = UUID.randomUUID();
        String[] idd = id.toString().split("-");
        return idd[0].substring(0,6) ;
    }

    /**
     * 获得4个长度的十六进制的UUID
     */
    public static String get4UUID() {
        UUID id = UUID.randomUUID();
        String[] idd = id.toString().split("-");
        return idd[0].substring(0,4) ;
    }


}