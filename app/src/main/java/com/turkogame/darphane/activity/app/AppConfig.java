package com.turkogame.darphane.activity.app;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AppConfig {

    public static String URL = "https://turkogame.com/uygulamalar/bilgi_oyunu/darphane_api";

    public static final String APP_ID = "ca-app-pub-5156810606852734~7405908617";

    public static final String odullu_reklam_id = "ca-app-pub-3940256099942544/5224354917"; //test için
    // public static final String odullu_reklam_id =  "ca-app-pub-5156810606852734/8279828975";

    public static String gecis_reklam_id = "ca-app-pub-3940256099942544/1033173712"; //test için
    //public static String gecis_reklam_id = "ca-app-pub-5156810606852734/8358518310";





    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
