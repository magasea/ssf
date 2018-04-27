package com.shellshellfish.aaas.zhongzhengapi.util;

import com.google.gson.Gson;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.digest.UnixCrypt;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

/**
 * Created by chenwei on 2018- 四月 - 23
 */

public class ZhongZhengAPIUtils {
  private static final String ALGORITHM = "AES";
  private static final Gson gson = new Gson();

  public static byte[] encrypt(byte[] plainText) throws Exception
  {
    SecretKeySpec secretKey = new SecretKeySpec(ZhongZhengAPIConstants.ZZ_PLATFORM_PRIVATE_KEY.getBytes(),
        ALGORITHM);
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    return cipher.doFinal(plainText);
  }

  /**
   * Decrypts the given byte array
   *
   * @param cipherText The data to decrypt
   */
  public static byte[] decrypt(byte[] cipherText) throws Exception
  {
    SecretKeySpec secretKey = new SecretKeySpec(ZhongZhengAPIConstants.ZZ_PLATFORM_PRIVATE_KEY.getBytes(),
        ALGORITHM);
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, secretKey);

    return cipher.doFinal(cipherText);
  }

  public static void main(String[] args) {
    String key = "Bar12345Bar12345"; // 128 bit key
    String initVector = "RandomInitVector"; // 16 bytes IV

    try {
      System.out.println(new String(decrypt(
          encrypt( "Hello World".getBytes()))));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public static TreeMap<String, String> makeInfo(boolean isDefault, TreeMap<String, String>
      origInfo)
      throws Exception {
    TreeMap<String, String> info = null;
    if(CollectionUtils.isEmpty(origInfo)){
      info = new TreeMap<>();
    }else{
      info = origInfo;
    }
    info.put("public_key", ZhongZhengAPIConstants.ZZ_PLATFORM_PUBLIC_KEY);

    info.put("platform_openid",ZhongZhengAPIConstants.ZZ_PLATFORM_DEFAULT_OPENID );
    info.put("platform_code", ZhongZhengAPIConstants.ZZ_PLATFORM_CODE);
    if(isDefault){
      String[] data = {ZhongZhengAPIConstants.ZZ_PLATFORM_DEFAULT_OPENID};
      info.put("data",  gson.toJson(data));
    }
    info.put("time", TradeUtil.getUTCTimeInSeconds().toString());

    String key = UnixCrypt.crypt(ZhongZhengAPIConstants.ZZ_PLATFORM_PRIVATE_KEY, "en");



    info.put("key", key);
//    String encryptKey =
//    info.put("key");
    if(isDefault){
      info.put("sign", makeDefaultMsg(info));
    }
    return info;
  }


  public static String makeDefaultMsg(TreeMap<String, String> param)
      throws NoSuchAlgorithmException, UnsupportedEncodingException {
    StringBuilder tempSB = new StringBuilder();
    param.forEach(
        (key, value) -> {
          tempSB.append(key).append(value);
        }
    );
    return DigestUtils.md5DigestAsHex(tempSB.toString().getBytes("UTF-8"));

  }



}
