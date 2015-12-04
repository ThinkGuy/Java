package com.lxw.syntaxAnalysis;

import java.util.Scanner;

/**
 * 递归下降语法分析器。
 * 
 * @author liuxinwei
 *
 */
public class RecursiveWay {

	// 待匹配语句。
	private String sentence;
	// 是否匹配。
	private boolean ok = true;

	/**
	 * 入口函数。
	 */
	public void entry() {
		Scanner in = new Scanner(System.in);
		setSentence(in.nextLine());
		//词法分析。
		new WordScanner().scan(getSentence());
		dealWithSentence(getSentence());
		
		//递归下降正式开始。
		String w = read(getSentence());
		w = partE(w);

		if ("#".equals(w)) {
			if (isOk() == true) {
				System.out.println("成功匹配");
			}
		} else {
			System.err.println("err");
		}
	}

	/**
	 * 子程序E。
	 * 
	 * @param w
	 *            待判断的第一个字。
	 * @return w。
	 */
	public String partE(String w) {
		w = partT(w);

		while ("w0".equals(w)) {
			w = read(sentence);
			w = partT(w);
		}

		return w;
	}

	/**
	 * 子程序T。
	 * 
	 * @param w
	 *            待判断的第一个字。
	 * @return w。
	 */
	public String partT(String w) {
		w = partF(w);

		while ("w1".equals(w)) {
			w = read(sentence);
			w = partF(w);
		}

		return w;
	}

	/**
	 * 子程序F。
	 * 
	 * @param w
	 *            待判断的第一个字。
	 * @return w。
	 */
	public String partF(String w) {

		if ("i".equals(w)) {
			w = read(sentence);
		} else if ("(".equals(w)) {
			w = read(sentence);
			w = partE(w);

			if (")".equals(w)) {
				w = read(getSentence());
			} else {
				setOk(false);
				System.err.println("err");
			}
		} else {
			setOk(false);
			System.err.println("err");
		}
		return w;
	}

	/**
	 * 字符串处理。
	 * 
	 * @param sentence
	 *            待处理的字符串。
	 */
	public void dealWithSentence(String sentence) {
		setSentence(getSentence() + "#");
		setSentence(getSentence().toLowerCase());
		setSentence(getSentence().replaceAll("[a-z]+", "a"));
		setSentence(getSentence().replaceAll("[0-9]+\\.{0,1}[0,9]*?", "2"));
		setSentence(getSentence().replaceAll("\\d+", "2"));
	}

	/**
	 * 读第一个字。
	 * 
	 * @param sentence
	 *            待处理的字符串。
	 * @return 第一个经过处理的字。
	 */
	public String read(String sentence) {
		String begin = String.valueOf(sentence.charAt(0));
		setSentence(sentence.substring(1));

		if (begin.matches("[0-9A-Za-z]")) {
			return "i";
		} else if ("+".equals(begin) || "-".equals(begin)) {
			return "w0";
		} else if ("*".equals(begin) || "/".equals(begin)) {
			return "w1";
		}
		return begin;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public static void main(String[] args) {
		RecursiveWay rw = new RecursiveWay();
		rw.entry();
	}
}
