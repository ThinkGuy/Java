package InformationSafty;

import java.util.Arrays;
import java.util.HashMap;

/**
 * 钥控序列加密法。
 * @author 刘鑫伟
 *
 */
public class ControlKey {
	
	HashMap<String, String> plaintextMap = new HashMap<>();
	
	//密钥。
	String key = "computer";
	
	/**
	 * 加密。
	 * @param plaintext 明文。
	 */
	public void encipher(String plaintext) {
		System.out.println("钥控序列加密法：\n明文：\n" + plaintext + "\n密文：");
		//对明文进行去空格处理及转换为数组。
		plaintext = plaintext.replace(" ", "");
		char[] plaintexts = plaintext.toCharArray();
		
		char[] keys = key.toCharArray();
		int col = keys.length;
		String[] results = new String[col];
		
		//初始化列字符串矩阵。
		for (int i=0; i<results.length; i++) {
			results[i] = "";
		}
		
		//获得矩阵列字符串。
		int n = 0;
		for (int i=0; i<plaintexts.length; i++) {
			n = i%col;
			results[n] = results[n] + plaintexts[i]; 
		}
		
		//匹配密钥与矩阵列字符串。
		for (int i=0; i<keys.length; i++) {
			plaintextMap.put("" + keys[i], results[i]);
		}
		
		//对密钥安字母顺序进行排序。
		Arrays.sort(keys);
		
		//输出密文。
		for (char key : keys) {
			System.out.println(plaintextMap.get("" + key));
		}
	}
	
	public static void main(String[] args) {
		//明文。
		String plaintext = "The normal deision table representation has four "
				+ "separate parts in a specific format";
		
		ControlKey controlKey = new ControlKey();
		//加密。
		controlKey.encipher(plaintext);
	}
}