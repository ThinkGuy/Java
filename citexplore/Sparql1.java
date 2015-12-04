package lxw;

import citexplore.foundation.util.Config;
import citexplore.foundation.util.Net;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.sun.org.apache.regexp.internal.recompile;

import org.jgrapht.graph.DefaultDirectedGraph;

import java.beans.Statement;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * CCS-DBPediaӳ�����Broder����ͼ�������ߡ�
 *
 * @author Liu, Xinwei
 */
public class DbpediaCategoryBroderGraphBuilder {

    // **************** ��������
	
	/**
	 * sparqlǰ׺��
	 */
	public static final String SPARQL_PREFIX = "cx.exp.sparql";
	
	 /**
     * graph��
     */
    public DefaultDirectedGraph<Vertex, Arc> graph = new DefaultDirectedGraph<>
    (Arc.class);
	
    // **************** ˽�б���
	
	/**
	 * id������
	 */
    private static int i = 1;
	
	/**
	 * Uri������
	 * 
	 * @author liuxinwei
	 *
	 */
	private enum KindOfUrl {
		Label, UriAndLabel;
	}
	
    /**
     * �ϲ�����
     */
    private Hashtable<Vertex, ArrayList<Vertex>> broderCategoryTable = 
    		new Hashtable<>();
    
//    /**
//     * Uri�ڵ��
//     */
//    private Hashtable<String, Vertex> uriVertexTable = new Hashtable<>();

    /**
     * �����͡�
     */
    private static class Arc {
    }

    /**
     * �ڵ����͡�
     */
    private static class Vertex {

        // **************** ��������

        /**
         * Uri��
         */
        public String uri = "";
        
        /**
         * label��
         */
        public String label = "";

        /**
         * Id��
         */
        public int id = 0;
        
        // **************** ˽�б���

        // **************** �̳з���

        // **************** ��������

        /**
         * �ڵ����͹��캯����
         *
         * @param uri uri��
         * @param id  id��
         */
        public Vertex(String uri, String label, int id) {
            this.uri = uri;
            this.label = label;
            this.id = id;
        }

        // **************** ˽�з���
        
    }

    // **************** �̳з���

    // **************** ��������

    public void build(String mappingPath, String graphPath) {
        // asdfasdfasdf
        ArrayList<String> uriList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new
                    File(mappingPath)));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splits = line.trim().split("> <");
                uriList.add(splits[2].substring(0, splits[2].indexOf(">")));
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // �ݹ����
        broder(createMapping(uriList));

        // asdfasdfasdfasdf

        Hashtable<String, Vertex> vertexTable = new Hashtable<>();
        
        for (Vertex vertex : broderCategoryTable.keySet()) {
            for (Vertex ver : broderCategoryTable.get(vertex)) {
                graph.addEdge(vertex, ver);
            }
        }
        
        // asdfasdfasdf
        try {
            BufferedWriter graphWriter = new BufferedWriter(new
                    OutputStreamWriter(new FileOutputStream(new File
                    ("D:/tree" + ".graphml"))));

            graphWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<graphml xmlns=\"http://graphml.graphdrawing.org/xmln"
                    + "s\">\n<key attr.name=\"label\" attr.type=\"string\" " +
                    "for=\"node\" id=\"label\"/>\n<broderCategoryTable " +
                    "edgedefault=\"dir" +
                    "ected\">\n");

            for (Vertex vertex : graph.vertexSet()) {
                int id = vertex.id;
                String label = vertex.uri;
                graphWriter.write(String.format("<node id=\"%d\">\n<data key"
                        + "=\"label\">%s</data>\n</node>\n", id, label));
            }

            for (Arc arc : graph.edgeSet()) {
                int source = graph.getEdgeSource(arc).id;
                int target = graph.getEdgeTarget(arc).id;

                graphWriter.write(String.format("<Edge source=\"%d\" " +
                        "target=\"%d\">\n</Edge>" + "\n", source, target));
            }

            graphWriter.write("</broderCategoryTable>\n</graphml>\n");
            graphWriter.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // **************** ˽�з���

    /**
     * �ݹ��ø��׽ڵ㡣
     *
     * @param uriList �ӽڵ㼯�ϡ�
     */
    private void broder(ArrayList<Vertex> uriList) {
        for (Vertex vertex : uriList) {
        	String uri = vertex.uri;
            if (broderCategoryTable.get(vertex) == null) {
            	Hashtable<Vertex, ArrayList<Vertex>> broderUrisTable = 
            			new Hashtable<>();
                ArrayList<Vertex> broderUris = broderUri(uri);
                broderCategoryTable.put(vertex, broderUris);
                broder(broderUris);
            }
        }
    }

    /**
     * ����ϲ�uri��
     *
     * @param uri uri��
     * @return ����ϲ�uri��
     */
    private ArrayList<Vertex> broderUri(String uri) {
        ResultSet set = ResultSetFactory.fromXML(Net.fetchStringFromUrlSafely
                (buildUrl(KindOfUrl.UriAndLabel, uri)));
        ArrayList<Vertex> broderUris = new ArrayList<>();
        while (set.hasNext()) {
            String uriString = set.next().get("c").toString();
            String label = set.next().get("l").toString();
            Vertex vertex = new Vertex(uriString, label, i);
            i = i + 1;
            graph.addVertex(vertex);
            broderUris.add(vertex);
        }

        return broderUris;
    }
    
    /**
     * ����
     * 
     * @param uriList  
     * @return
     */
    private ArrayList<Vertex> createMapping(ArrayList<String> uriList) {
    	ArrayList<Vertex> mappingList = new ArrayList<>();
    	
    	for (String uri : uriList) {
    		ResultSet set = ResultSetFactory.fromXML(Net.fetchStringFromUrlSafely
        			(buildUrl(KindOfUrl.Label, uri)));
    		
    			String label = set.next().get("l").toString();
    			Vertex vertex = new Vertex(uri, label, i);
    			i = i + 1;
    			graph.addVertex(vertex);
    			mappingList.add(vertex);
    	}
    	
    	return mappingList;
    }

    /**
     * ����sqarql��ѯurl��
     *
     * @param uri uri��
     * @return ���ɵĲ�ѯurl��
     */
    private String buildUrl(KindOfUrl kind, String uri) {
        String url = null;
        
        if (kind == KindOfUrl.UriAndLabel)
        	url = Config.getPathForRead(SPARQL_PREFIX) + "default-broderCategory"
        			+ "Table-uri=&query=select+%3Fc%2C%3Fl+where+%7B%0D%0A%3C"
        		+ URLEncoder.encode(uri) + "%3E+%3Chttp%3A%2F%2Fwww.w3.org%2F2004"
        		+ "%2F02%2Fskos%2Fcore%23broader%3E+%3Fc.%0D%0A%3Fc+%3Chttp%3A%2F"
        		+ "%2Fwww.w3.org%2F2000%2F01%2Frdf-sch" + "ema%23label%3E+%3Fl.%0D"
        		+ "%0A+%7D&format=application%2Fsparql-results%2Bxml&debug=on";
        if (kind == KindOfUrl.Label) {
            url =  Config.getPathForRead(SPARQL_PREFIX) + "default-graph-uri=&que"
            		+ "ry=select+%3Fl+where+%7B%0D%0A%3C" + URLEncoder.encode(uri) 
            		+ "%3E+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23"
            		+ "label%3E+%3Fl.%0D%0A+%7D&format=text%2Fhtml&timeout=0&debug"
            		+ "=on";
        }
        return url;
    }
}
