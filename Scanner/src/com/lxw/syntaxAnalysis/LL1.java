package com.lxw.syntaxAnalysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 * LL1语法分析器。
 * 
 * @author liuxinwei。
 * 
 */
public class LL1 {
	// 表内元素。
	public static final String FIRST = "{i,(}";
	public static final String SECOND = "{w0}";
	public static final String THIRD = "{i,#}";
	public static final String FOURTH = "{i,(}";
	public static final String FIFTH = "{w1}";
	public static final String SIXTH = "{w0,),#}";
	public static final String SEVENTH = "{i}";
	public static final String EIGHTH = "{(}";

	// 非终结符。
	public static final String VT = "abcdefghijklmnopqrstuvwxyz+-*/w0w1()";
	// 终结符。
	public static final String VN = "ABCDE1F1GHIJKLMNOPQRST1UVWXYZ";

	// 文法字符串形式集合。
	private List<String> grammarList = new ArrayList<>();
	// 文法键值对形式集合。
	private HashMap<String, List<String>> grammarMap = new HashMap<>();

	// LL(1)分析表。
	private HashMap<String, HashMap<String, String>> table = new HashMap<>();
	// LL(1)表行。
	private List<String> rowList = new ArrayList<>();
	// LL(1)表列。
	private List<String> colList = new ArrayList<>();
	// 语法栈。
	Stack<String> stack = new Stack<>();

	// 待匹配语句。
	private String sentence;
	// 是否匹配。
	private boolean ok = true;

	/**
	 * 文法和表的初始化。
	 */
	public void initialize() {
		// 文法的初始化。
		String row = "E,E1,T,T1,F";
		String col = "i,w0,w1,(,),#";
		String head;

		grammarList.add("E->TE1");
		grammarList.add("E1->w0TE1|&");
		grammarList.add("T->FT1");
		grammarList.add("T1->w1FT1|&");
		grammarList.add("F->i|(E)");

		String[] splits = row.split(",");
		rowList = Arrays.asList(splits);
		splits = col.split(",");
		colList = Arrays.asList(splits);

		List<String> list;
		for (String grammar : grammarList) {
			head = grammar.substring(0, grammar.indexOf("->"));
			grammar = grammar.substring(grammar.indexOf("->") + 2);
			splits = grammar.split("\\|");
			list = Arrays.asList(splits);
			grammarMap.put(head, list);
		}

		// 表的初始化
		HashMap<String, String> map;
		for (String r : rowList) {
			map = new HashMap<>();
			for (String c : colList) {
				if ("E".equals(r)) {
					if ("i".equals(c) || "(".equals(c)) {
						map.put(c, FIRST);
					} else {
						map.put(c, null);
					}
				} else if ("E1".equals(r)) {
					if ("w0".equals(c)) {
						map.put(c, SECOND);
					} else if (")".equals(c) || "#".equals(c)) {
						map.put(c, THIRD);
					} else {
						map.put(c, null);
					}
				} else if ("T".equals(r)) {
					if ("i".equals(c) || "(".equals(c)) {
						map.put(c, FOURTH);
					} else {
						map.put(c, null);
					}
				} else if ("T1".equals(r)) {
					if ("w1".equals(c)) {
						map.put(c, FIFTH);
					} else if ("w0".equals(c) || ")".equals(c) || "#".equals(c)) {
						map.put(c, SIXTH);
					} else {
						map.put(c, null);
					}
				} else if ("F".equals(r)) {
					if ("i".equals(c)) {
						map.put(c, SEVENTH);
					} else if ("(".equals(c)) {
						map.put(c, EIGHTH);
					} else {
						map.put(c, null);
					}
				} else {
					map.put(c, null);
				}
			}
			table.put(r, map);
		}

		// 标准输入带扫描表达式。
		Scanner in = new Scanner(System.in);
		setSentence(in.nextLine());
		//词法分析。
		new WordScanner().scan(getSentence());
		dealWithSentence(getSentence());
	}

	/**
	 * 语义分析。
	 * 
	 */
	public void analysis() {
		String w = read(getSentence());
		String x = "";
		
		stack.push("#");
		stack.push("E");
		while (!"#".equals(w)) {

			if (stack.size() == 0) {
				System.err.println("err");
				return;
			}
			x = stack.pop();

			// 不是终结符。
			if (VT.indexOf(x) == -1) {
				// 是非终结符。
				if (VN.indexOf(x) != -1) {

					if (table.get(x).get(w) != null) {
						List<String> list = grammarMap.get(x);

						if (list.size() > 1) {
							for (String item : list) {
								if (item.indexOf(w) == 0) {
									// 逆序入栈。
									String[] splits = item.split("");
									for (int i = splits.length; i > 0; i--) {
										if (splits[i - 1].matches("[0-9]")) {
											stack.push(splits[i - 2]
													+ splits[i - 1]);
											i--;
										} else {
											stack.push(splits[i - 1]);
										}
									}
								}
							}
						} else {

							for (String word : list) {
								// 逆序入栈。
								String[] splits = word.split("");
								for (int i = splits.length; i > 0; i--) {
									if (splits[i - 1].matches("[0-9]")) {
										stack.push(splits[i - 2]
												+ splits[i - 1]);
										i--;
									} else {
										stack.push(splits[i - 1]);
									}
								}
							}
						}

					} else {
						System.err.println("err");
						setOk(false);
						return;
					}
				}
			} else {
				if (x.equals(w)) {
					w = read(getSentence());
					continue;
				} else {
					System.err.println("err");
					setOk(false);
					return;
				}
			}
		}

		if (isOk() == true) {
			// 按照PPT上算法，stack中必定包含#,T1,F1。
			if (stack.size() == 3) {
				System.out.println("成功匹配");
			} else {
				System.err.println("err");
			}
		}
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
		LL1 ll1 = new LL1();
		ll1.initialize();
		ll1.analysis();
	}
}
