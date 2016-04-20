package InformationSafty;

/**
 * 双轨密码。
 * @author Liu,xinwei。
 *
 */
public class DualTrack {
	
	/**
	 * 加密。
	 * @param plaintext 明文。
	 */
	public void encipher(String plaintext) {
		//密文。
		String ciphertext = "";
		//奇数串。
		String oddString = "";
		//偶数串。
		String evenString = "";
		String[] splits = plaintext.split("");
		
		for (int i=0; i<splits.length; i++) {
			if (i%2 == 0) {
				oddString = oddString + splits[i];
			} else {
				evenString = evenString + splits[i];
			}
		}
		
		ciphertext = oddString + evenString;
		System.out.println(plaintext + "\n经双规加密后为：\n" + ciphertext);
	}
	
	/**
	 * 解密。
	 * @param ciphertext 密文。
	 */
	public void decrypt(String ciphertext) {
		//明文。
		StringBuilder plaintext = new StringBuilder();
		String[] oddString = ciphertext.substring(0, (ciphertext.length()+1)/2).split("");
		String[] evenString = ciphertext.substring((ciphertext.length()+1)/2, 
				ciphertext.length()).split("");
		
		for (int i=0; i<ciphertext.length()/2; i++) {
			plaintext.append(oddString[i]).append(evenString[i]);
		}
		
		//当奇数串长于偶数串时。
		if (oddString.length > evenString.length) {
			plaintext.append(oddString[oddString.length-1]);
		}
		
		System.out.println(ciphertext + "\n经双规解密后为：\n" + plaintext);
		
	}
	
	public static void main(String[] args) {
		//明文。
		String plaintext = "DiscreteAndSystem";
		//密文。
		String ciphertext = "DsrtAdytmiceenSse";
		
		System.out.println("双规加密方式：");
		
		DualTrack dualTrack = new DualTrack();
		//双轨加密。
		dualTrack.encipher(plaintext);
		//双轨解密。
		dualTrack.decrypt(ciphertext);
		
		System.out.println("\n姓名：刘鑫伟 学号：20134019");
	}
}