package InformationSafty;
import java.util.Scanner;

/**
 * 列换位算法。 3-1-4-2.
 * @author 刘鑫伟
 *
 */
public class ChangeLocation {
	/**
	 * 明文。
	 */
	public static final String CLEARTEXT = "DATA SECU RITY";
	
	/**
	 * 密文。
	 */
	private String ciphertext;
	
	/**
	 * 加密。3-1-4-2。
	 * @param clearText 明文。
	 */
	public void encipher(String clearText) {
		clearText = clearText.replace(" ", "").trim();
		char[] list = clearText.toCharArray();
		StringBuilder first = new StringBuilder();
		StringBuilder second = new StringBuilder();
		StringBuilder third = new StringBuilder();
		StringBuilder fouth = new StringBuilder();
		
		for (int i=0; i<list.length; i++) {
			if (i%4 == 0) {
				third.append(list[i]);
			} else if (i%4 == 1) {
				first.append(list[i]);
			} else if (i%4 == 2) {
				fouth.append(list[i]);
			} else {
				second.append(list[i]);
			}
		}
		
		ciphertext = first.append(second).append(third).append(fouth).toString();
		System.out.println("密文：" + ciphertext);
		
	}
	
	public static void main(String[] args) {
		System.out.println("列换位算法：\n明文：" + CLEARTEXT);
		new ChangeLocation().encipher(CLEARTEXT);
	}
}
