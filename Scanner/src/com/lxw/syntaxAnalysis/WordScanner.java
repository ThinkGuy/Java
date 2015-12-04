package com.lxw.syntaxAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 词法扫描器。
 * 
 * @author liuxinwei。
 *
 */
public class WordScanner {

	// 词表路径。
	public static final String TABLE_PATH = "resource/table.txt";

	// 代码路径。
	public static final String CODE_PATH = "resource/code.txt";

	// 待扫描代码。
	private String code;
	// token。
	private String token = "";

	// iT标识符表。
	private Map<String, String> iTIdMap = new HashMap<>();
	// cT字符表。
	private Map<String, String> cTCharMap = new HashMap<>();
	// sT字符串。
	private Map<String, String> sTStringMap = new HashMap<>();
	// CT常数。
	private Map<String, String> cTConstantMap = new HashMap<>();
	// KT关键字。
	private Map<String, String> kTKeyMap = new HashMap<>();
	// PT界符。
	private Map<String, String> pTDeliMap = new HashMap<>();

	/**
	 * 将词表写入Map。
	 */
	public void writeToMap() {
		// 词表的种类。
		String kind = null;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					TABLE_PATH)));

			String line;
			while ((line = reader.readLine()) != null) {

				if ("iTId".equals(line) || "cTChar".equals(line)
						|| "sTString".equals(line) || "cTConstant".equals(line)
						|| "kTKey".equals(line) || "ptDeli".equals(line)) {
					kind = line;
				} else {
					String[] splits = line.split(" ");

					if ("iTId".equals(kind)) {
						iTIdMap.put(splits[0], splits[1]);
					} else if ("cTChar".equals(kind)) {
						cTCharMap.put(splits[0], splits[1]);
					} else if ("sTString".equals(kind)) {
						sTStringMap.put(splits[0], splits[1]);
					} else if ("cTConstant".equals(kind)) {
						cTConstantMap.put(splits[0], splits[1]);
					} else if ("kTKey".equals(kind)) {
						kTKeyMap.put(splits[0], splits[1]);
					} else if ("ptDeli".equals(kind)) {
						pTDeliMap.put(splits[0], splits[1]);
					}
				}
			}

			reader.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 读代码。
	 */
	public void readCode() {
		String code = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					CODE_PATH)));

			String line;
			while ((line = reader.readLine()) != null) {
				code = code + line + " ";
			}

			reader.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		this.code = code;
	}

	/**
	 * 扫描词法。
	 */
	public void scan(String code) {
		String word; // 当前单词。
		char start; // 开始字符。
		char ch; // 当前字符。

		writeToMap();
		while (!"".equals(code)) {
			int i = 0;
			start = code.charAt(0);

			// 字母开头。
			if ((start >= 'A' && start <= 'Z')
					|| (start >= 'a' && start <= 'z')) {
				ch = code.charAt(0);

				while ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')) {
					i = i + 1;
					if (i >= code.length()) {
						break;
					}
					ch = code.charAt(i);
				}

				word = code.substring(0, i);
				// 先到关键字表里查找，然后到标识符表里查找，再到字符串表里查找，最后到字符表里查找。
				if (kTKeyMap.containsKey(word)) {
					token = token + "<" + kTKeyMap.get(word) + ">, ";
				} else if (iTIdMap.containsKey(word)) {
					token = token + "<" + iTIdMap.get(word) + ">, ";
				} else if (sTStringMap.containsKey(word)) {
					token = token + "<" + sTStringMap.get(word) + ">, ";
				} else {
					token = token + "<" + cTCharMap.get(word) + ">, ";
				}

			} else if (Character.isDigit(start)) {
				// 数字开头。
				while (Character.isDigit(code.charAt(i))) {
					i = i + 1;
					if (i >= code.length()) {
						break;
					}
				}

				word = code.substring(0, i);
				token = token + "<" + cTConstantMap.get(word) + ">, ";

			} else {
				// 其他字符开头。
				ch = code.charAt(i);
				while (!((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || Character
						.isDigit(ch))) {
					i = i + 1;
					if (i >= code.length()) {
						break;
					}
					ch = code.charAt(i);
				}

				word = code.substring(0, i).trim();

				if (!(word.contains(">=") || word.contains("<=") || word
						.contains("=="))) {

					char[] list = word.toCharArray();

					for (int j = 0; j < list.length; j++) {
						String s = "" + word.charAt(j);

						if (!" ".equals(s)) {

							if ("\\".equals(s)) {
								s = "\\" + s;
								token = token + "<" + pTDeliMap.get(s) + ">, ";
							} else {
								token = token + "<" + pTDeliMap.get(s) + ">, ";
							}
						}
					}

				} else {
					token = token + pTDeliMap.get(word) + ">, ";
				}
			}
			code = code.substring(i);

			while (code.indexOf(" ") == 0) {
				code = code.substring(1);
			}
		}

		token = token.substring(0, token.length() - 2);
		System.out.println(token);
	}

	public static void main(String[] args) {
		WordScanner scanner = new WordScanner();
		scanner.readCode();
		scanner.scan(scanner.code);
	}

}
