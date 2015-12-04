package lxw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;

import citexplore.foundation.util.Config;
import citexplore.foundation.util.Net;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;

public class LabelBuilder {
	// **************** 公开变量
	
	/**
     * Sparql endpont地址。
     */
    public static final String SPARQL_ENDPOINT = "cx.exp" + "" +
            ".dbpediacategorybrodergraphbuilder.sparqlendpoint";
    
    // **************** 私有变量
    
    /**
     * uri节点表。
     */
    private ArrayList<String> uris = new ArrayList<>();
    
    /**
     * label节点表。
     */
    private Hashtable<String, String> labelTable = new Hashtable<>(); 

    // **************** 继承方法

    // **************** 公开方法
	
	public void buildLabelDocument(String graphPath, String labelPath) {
		try {
			//从graphml图里读取uri。
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					graphPath)));
			String line; 
			while ((line = reader.readLine()) != null) {
				if (line.indexOf("<data key=\"dbpediaUri\">") != -1) {
					uris.add(line.substring(23, line.indexOf("</data>")));
				}
			}
			reader.close();
			
			//生成label节点表。
			for (String uri : uris) {
				ResultSet set = ResultSetFactory.fromXML
						(Net.fetchStringFromUrlSafely(buildUrl(uri)));
				
				while (set.hasNext()) {
					String label = set.next().get("l").toString();
					labelTable.put(label, "");
				}
			}
		
			//生成label文档。
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File
					(labelPath)));
			for (String label : labelTable.keySet()) {
				writer.write(label.trim() + "\n");
			}
			writer.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	//***************** 私有方法
	
	/**
	 * 生成查询url。
	 * 
     * @param uri uri。
	 * @return 生成查询url。
	 */
	private String buildUrl(String uri) {
		return Config.get(SPARQL_ENDPOINT) + "default-graph-uri=&query=select+"
				+ "%3Fl+where+%7B%3Fc+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2F"
				+ "rdf-schema%23label%3E+%3Fl.%0D%0A%3Fc+%3Chttp%3A%2F%2Fpurl.o"
				+ "rg%2Fdc%2Fterms%2Fsubject%3E+%3C" + URLEncoder.encode(uri) + 
				"%3E%7D%0D%0A&format=application%2Fsparql-results%2Bxml&debug=on";
	}
	
	public static void main(String[] args) {
		new LabelBuilder().buildLabelDocument("D:/tree.graphml", "D:/lable.txt");
	}

}
