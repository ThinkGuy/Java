package InformationSafty;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Vigenere密码。
 * @author 刘鑫伟
 *
 */
public class Vigenere {
	
	public static final char A = 'A';

	/**
	 * 基密钥。
	 */
	private String keyBase = "lxw";
	
	/**
	 * 加密表。
	 */
	private HashMap<String, HashMap<String, String>> encryptionMap = new HashMap<>();
	
	/*
	 * 构建加密表。
	 */
	public void buildEncryptionMap() {
		HashMap<String, String> map;
		char value;
		for (char i=65; i<91; i++) {
			map = new HashMap<>();
			for (char j=65; j<91; j++) {
				value =(char) ((i + j - A -A)%26 + A);
				map.put(""+j, ""+value);
			}
			encryptionMap.put(""+i, map);
		}
	}
	
	/**
	 * 根据明文长度构造密钥。
	 */
	public String buildKey(int length) {
		String key = "";
		for (int i=1; i<length/keyBase.length()+2; i++) {
			key = key + keyBase;
		}
		key = key.substring(0, length);
		return key;
	}
	
	/**
	 * 加密。
	 * @param clearText 明文。
	 * @return 密文。
	 */
	public String encipher(String clearText) {
		//将明文进行去除字母外的处理与大写化。
		clearText = clearText.trim().replaceAll("[^A-z]", "").toUpperCase();
		//密文。
		String ciphertext = "";
		//密钥。
		String key = buildKey(clearText.length()).toUpperCase();
		//构建加密表。
		buildEncryptionMap();
		
		for (int i=0; i<key.length(); i++) {
			ciphertext = ciphertext + encryptionMap.get(
					(""+clearText.charAt(i))).get((""+key.charAt(i)));
		}
		
		return ciphertext;
	}
	
	public static void main(String[] args) {
		Vigenere vigenere = new Vigenere();
		System.out.println("Vigenere加密法：\n请输入明文:");
		Scanner in = new Scanner(System.in);
		String clearText = in.next();
		System.out.println("Vigenere加密后密文为：\n" + vigenere.encipher(clearText));
	}
}
