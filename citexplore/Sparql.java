package lxw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import citexplore.foundation.util.Net;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;

import lxw.Vertex;
import lxw.Arc;
import lxw.Runner;

/**
 * 
 * @author Liu, Xinwei
 *
 */
public class Sparql {
	HashMap<String, ArrayList<String>> treeMap = new HashMap<>();
public static int j = 0;
		
	
	/**
	 * 读取RDF中的节点,调用递归函数
	 */
	public void readRDF() {
		ArrayList<String> childList = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					new File("D:/本体.txt")));
			String line;
			String replaceString;
			
			while((line = reader.readLine()) != null) {
				if (line.equals("")) {
					continue;
				}
				
				String[] splits = line.split("> <");
				replaceString = splits[2].substring(0, splits[2].indexOf(">"));
				replaceString = replaceString.replace(" ", "_");
				childList.add(replaceString);
			}
			reader.close();
			
			getParent(childList);
			createRDF();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
		
	/**
	 * 递归函数,获得父亲节点
	 * 
	 * @param childList 子节点集合
	 */
	public void getParent(ArrayList<String> childList) {
		ArrayList<String> uriList = null;
		
		for (String childUri : childList) {
			uriList = getSparqlAndFormatResults(childUri);

			if (treeMap.containsKey(childUri)) {
				System.out.println("**********************************************************");
				return;
			}
			
			treeMap.put(childUri, uriList);
			getParent(uriList);
		}
		
	}
	
	/**
	 * 获得Sparql查询结果，同时格式化uri
	 * @param childUri 子Uri
	 * @return 查询处理后得到的标准uri集合
	 */
	public ArrayList<String> getSparqlAndFormatResults(String childUri) {
		ArrayList<String> uriList = new ArrayList<>();
		ResultSet set;
		String uri;

		set = ResultSetFactory
				.fromXML(Net.fetchStringFromUrlSafely(buildUrl(childUri)));
		
		while (set.hasNext()) {
			QuerySolution solution = set.next();
			uri = solution.get("l").toString();
			//将Sparql查询得到的简单名称处理成标准uri形式
			uri = uri.substring(0, uri.indexOf("@en"));
			uri = uri.replace(" ", "_");
			uri = "http://dbpedia.org/resource/Category:" + uri;
System.out.println(j++  + " " + uri);			
			uriList.add(uri);
		}
		
		return uriList;
	}
	
	/**
	 * 构造RDF关系文档
	 */
	public void createRDF() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					new File("D:/树.txt")));
			
			for (String childUri : treeMap.keySet()) {
				for (String parentUri : treeMap.get(childUri)) {
					String line = "<" + childUri + "> " + "<http://www.w3"
							+ ".org/2004/02/skos/core#broader> "
							+ "<" + parentUri + "> .";
					writer.write(line.trim() + "\n");
				}
			}
			
			writer.close();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * @param childUri 需要查询的Uri
	 * @return 相应的sparql查询语句的URL
	 */
	public String buildUrl(String childUri) {
		String url;
		
		url = "http://202.118.18.234:8890/sparql?default-graph-uri=&query=select+%3Fl+where+%7B%0D%0A%3C"
				+ URLEncoder.encode(childUri) + "%3E+%3Chttp%3A%2F%2Fwww.w3.org%2F2004%2F02%2Fskos%2Fcore"
						+ "%23broader%3E+%3Fc.%0D%0A%3Fc+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-sch"
						+ "ema%23label%3E+%3Fl.%0D%0A+%7D&format=application%2Fsparql-results%2Bxml&debug=on";
		return url;
	}
	
	public static void main(String[] args) {
		new Sparql().readRDF();
	}
}
