package com.media.mfcloud;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;

import android.util.Base64;


 
public class Aes{
  static String IV = "AAAAAAAAAAAAAAAA";
  static String plaintext = ""; /*Note null padding*/
  private static String encryptionKey = "0123456789abcdef";
  static String encrypttext = "";
 /* public static void main(String [] args) {
    try {
    	
      byte[] cipher = encrypt(plaintext, encryptionKey);
 
      System.out.print("cipher:  ");
      for (int i=0; i<cipher.length; i++)
        System.out.print(new Integer(cipher[i])+" ");
      System.out.println("");
 
      String decrypted = decrypt(cipher, encryptionKey);
 
      System.out.println("decrypt: " + decrypted);
 
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }*/
  
 public static  String encrypt_string(String toencrypt) {
	
	 
	String res =toencrypt;
	Integer r = toencrypt.getBytes().length;
	for(int i =0; i<16-r%16;i++)
	{
		res+='\0';
	}
	
	byte[] cipher;
	try {
		cipher = encrypt(res,encryptionKey);
	} catch (Exception e) {
		e.printStackTrace();
		return "";
	}
	
	String result = Base64.encodeToString(cipher, Base64.DEFAULT);
		
	
	return result;
	
 }
 public static String decrypt_string(String todecrypt) {
	 String decrypted;
	
		try {
			decrypted = decrypt(Base64.decode(todecrypt, Base64.DEFAULT), encryptionKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		decrypted = decrypted.replaceAll("\0", "");

	return decrypted;
 }
 private static byte[] encrypt(String pText, String eKey) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    SecretKeySpec key = new SecretKeySpec(eKey.getBytes("UTF-8"), "AES");
    cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
    return cipher.doFinal(pText.getBytes("UTF-8"));
  }
 
  private static String decrypt(byte[] cText, String eKey) throws Exception{
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    SecretKeySpec key = new SecretKeySpec(eKey.getBytes("UTF-8"), "AES");
    cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
    return new String(cipher.doFinal(cText),"UTF-8");
  }
}