package com.nfc.qukuaiyuan.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5Util {


    public static String getMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
//        String md52 = getMD5("ky.com159{\"mobile\":\"15818180121\",\"password\":\"676556\",\"act\":\"user.user_login\",\"time\":1541508096000,\"appid\":798563,\"sessionkey\":765291}cky.com159");
        String md52=getMD5("ky.com159{\"mobile\":\"15818180121\",\"password\":\"676556\",\"act\":\"user.user_login\",\"time\":1541508096000,\"appid\":798563,\"sessionkey\":765291}cky.com159");
        System.out.println(md52);
    }
}
