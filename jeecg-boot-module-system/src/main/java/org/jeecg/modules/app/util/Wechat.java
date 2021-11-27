package org.jeecg.modules.app.util;

import cn.hutool.json.JSONUtil;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;

public class Wechat {
    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // 初始化密钥
    }

    /**
     * 解密数据
     */
    public static String decrypt(String appId, String sessionKey, String encryptedData, String iv) throws
            InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, InvalidParameterSpecException, BadPaddingException, InvalidKeyException {
        byte[] resultByte = wxDecrypt(encryptedData, sessionKey, iv);
        String result = new String(resultByte, StandardCharsets.UTF_8);
        // 是否与当前 appid 相同
        if (!appId.equals(JSONUtil.parseObj(result).getJSONObject("watermark").getStr("appid"))) {
            result = "";
        }
        return result;
    }

    public static final String KEY_NAME = "AES"; // 算法名
    // 加解密算法/模式/填充方式，ECB 模式只用密钥即可对数据进行加密解密，CBC 模式需要添加一个 iv
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

    /**
     * 接口如果涉及敏感数据（如 wx.getUserInfo 当中的 openId 和 unionId），接口的明文内容将不包含这些敏感数据。
     * 开发者如需要获取敏感数据，需要对接口返回的加密数据(encryptedData) 进行对称解密。 解密算法如下：
     * * 对称解密使用的算法为 AES-128-CBC，数据采用 PKCS#7 填充。
     * * 对称解密的目标密文为 Base64_Decode(encryptedData)。
     * * 对称解密秘钥 aeskey = Base64_Decode(session_key), aeskey 是16字节。
     * * 对称解密算法初始向量 为 Base64_Decode(iv)，其中 iv 由数据接口返回。
     *
     * @param encrypted  目标密文
     * @param sessionKey 会话ID
     * @param iv         加密算法的初始向量
     */
    public static byte[] wxDecrypt(String encrypted, String sessionKey, String iv) throws NoSuchAlgorithmException,
            InvalidParameterSpecException, NoSuchPaddingException, BadPaddingException, InvalidKeyException,
            IllegalBlockSizeException, InvalidAlgorithmParameterException {
        KeyGenerator.getInstance(KEY_NAME).init(128);

        // 生成 iv
        // iv 为一个 16 字节的数组，这里采用和 iOS 端一样的构造方法，数据全为 0
        // Arrays.fill(iv, (byte) 0x00);
        AlgorithmParameters ivj = AlgorithmParameters.getInstance(KEY_NAME);
        ivj.init(new IvParameterSpec(java.util.Base64.getDecoder().decode(iv)));

        // 生成解密
        Key key = new SecretKeySpec(java.util.Base64.getDecoder().decode(sessionKey), KEY_NAME);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, ivj); // 设置为解密模式
        return cipher.doFinal(java.util.Base64.getDecoder().decode(encrypted));
    }
}
