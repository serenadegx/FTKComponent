package com.fantaike.component.plug_in;


import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class FTKSafe {
    private static final String LOWERCASE_HEX_DIGITS[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    private static final String UPPERCASE_HEX_DIGITS[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    public static String md5Encode(String data, boolean isLowercase, int length) {
        String result = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            result = byteArrayToHexString(md5.digest(data.getBytes()), isLowercase, length);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String byteArrayToHexString(byte b[], boolean isLowercase, int length) {
        String result;
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i], isLowercase));
        }
        if (length == 16) {
            result = resultSb.toString().substring(8, 24);
        } else if (length == 32) {
            result = resultSb.toString();
        } else {
            result = null;
        }
        return result;
    }

    private static String byteToHexString(byte b, boolean isLowercase) {
        String[] hexDigIts = isLowercase ? LOWERCASE_HEX_DIGITS : UPPERCASE_HEX_DIGITS;
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigIts[d1] + hexDigIts[d2];
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String aesGenerateKey(int keySize) {
        String keyStr = null;
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
//            generator.init(keySize, new SecureRandom());
            generator.init(keySize);//默认传入 new SecureRandom()
            SecretKey secretKey = generator.generateKey();
            keyStr = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keyStr;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String aesEncode(String key, String data) {
        String encode = null;
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] bytes = cipher.doFinal(data.getBytes());
            encode = Base64.getEncoder().encodeToString(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return encode;
    }

    /**
     * AES带偏移量加密
     *
     * @param key
     * @param offset must be 16 bytes long
     * @param data
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String aesEncodeByIv(String key, String offset, String data) {
        String encode = null;
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(offset.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] bytes = cipher.doFinal(data.getBytes());
            encode = Base64.getEncoder().encodeToString(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return encode;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String aesDecode(String key, String data) {
        String decode = null;
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(data));
            decode = new String(bytes, "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decode;
    }

    /**
     * AES带偏移量解密
     *
     * @param key
     * @param offset must be 16 bytes long
     * @param data
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String aesDecodeByIv(String key, String offset, String data) {
        String decode = null;
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(offset.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(data));
            decode = new String(bytes, "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decode;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Map<String, String> generateRSAKey(int keySize) {
        Map<String, String> keys = new HashMap<>();
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(keySize);
            KeyPair keyPair = generator.generateKeyPair();
            keys.put("privateKey", Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
            keys.put("publicKey", Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keys;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String sign(String privateKey, String data) {
        String sign = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PrivateKey privateK = factory.generatePrivate(keySpec);
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initSign(privateK);
            signature.update(data.getBytes());
            sign = Base64.getEncoder().encodeToString(signature.sign());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return sign;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean verify(String publicKey, String sign, String origin) {
        boolean flag = false;
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
//        byte[] bytes = Base64.getDecoder().decode(encode);
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey publicK = factory.generatePublic(keySpec);
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initVerify(publicK);
            signature.update(origin.getBytes());
            flag = signature.verify(Base64.getDecoder().decode(sign));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return flag;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encodeByPrivateKey(String privateKey, String data) {
        String encode = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            Key privateK = keyFactory.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateK);
            encode = Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return encode;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decodeByPublicKey(String publicKey, String data) {
        String decode = null;
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
        byte[] encode = Base64.getDecoder().decode(data);
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey publicK = factory.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicK);
            byte[] bytes = cipher.doFinal(encode);
            decode = new String(bytes, "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decode;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encodeByPublicKey(String publicKey, String data) {
        String encode = null;
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey publicK = factory.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicK);
            encode = Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return encode;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decodeByPrivateKey(String privateKey, String encode) {
        String decode = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        byte[] bytes = Base64.getDecoder().decode(encode);
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PrivateKey privateK = factory.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateK);
            decode = new String(cipher.doFinal(bytes), "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return decode;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String[] args) {
//        System.out.println("result:" + md5Encode("199498fld", true, 16));
//        System.out.println("AES_KEY:" + aesGenerateKey(256));
//        String origin = "mango,I love you.";
//        String key = aesGenerateKey(128);
//        String encode = aesEncode(key, origin);
//        System.out.println("AES_origin:" + origin);
//        System.out.println("AES_key:" + key);
//        System.out.println("AES_encode:" + encode);
//        System.out.println("AES_decode:" + aesDecode(key,encode));

//        String origin = "mango,I love you.";
//        String offset = "0123456789abcdef";
//        String key = aesGenerateKey(256);
//        String encode = aesEncodeByIv(key, offset, origin);
//        System.out.println("AES_origin:" + origin);
//        System.out.println("AES_key:" + key);
//        System.out.println("AES_encode:" + encode);
//        System.out.println("AES_decode:" + aesDecodeByIv(key, offset, encode));

//        String origin = "a big mouse!";
//        Map<String, String> keys = generateRSAKey(512);
//        String encode = encodeByPrivateKey(keys.get("privateKey"), origin);
//        String decode = decodeByPublicKey(keys.get("publicKey"), encode);
//        System.out.println("RSA_origin:" + origin);
//        System.out.println("privateKey:" + keys.get("privateKey"));
//        System.out.println("publicKey:" + keys.get("publicKey"));
//        System.out.println("RSA_encode:" + encode);
//        System.out.println("RSA_decode:" + decode);

//        String origin = "a big mouse!";
//        Map<String, String> keys = generateRSAKey(1024);
//        String encode = encodeByPublicKey(keys.get("publicKey"), origin);
//        String decode = decodeByPrivateKey(keys.get("privateKey"), encode);
//        System.out.println("RSA_origin:" + origin);
//        System.out.println("privateKey:" + keys.get("privateKey"));
//        System.out.println("publicKey:" + keys.get("publicKey"));
//        System.out.println("RSA_encode:" + encode);
//        System.out.println("RSA_decode:" + decode);

        String origin = "a big mouse!";
        Map<String, String> keys = generateRSAKey(1024);
        String encode = encodeByPrivateKey(keys.get("privateKey"), origin);
        String sign = sign(keys.get("privateKey"), origin);
        System.out.println("RSA_origin:" + origin);
        System.out.println("privateKey:" + keys.get("privateKey"));
        System.out.println("publicKey:" + keys.get("publicKey"));
        System.out.println("RSA_encode:" + encode);
        System.out.println("RSA_sign:" + sign);
        System.out.println("RSA_verify:" + verify(keys.get("publicKey"), sign, origin));

    }
}
