package citexplore.experiment.dataset;

import citexplore.foundation.util.Config;
import citexplore.foundation.util.Net;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * CCS-DBPediaӳ�����Broder����ͼ�������ߡ�
 *
 * @author Liu, Xinwei; ZHang, Yin
 */
public class DbpediaCategoryBroderGraphBuilder {

    // **************** ��������

    /**
     * Sparql endpont��ַ����ֹ��?��
     */
    public static final String SPARQL_ENDPOINT = "cx.exp" + "" +
            ".dbpediacategorybrodergraphbuilder.sparqlendpoint";

    /**
     * �����͡�
     */
    public static class Arc {
    }

    // **************** ˽�б���

    /**
     * �ϲ�����
     */
    private Hashtable<String, ArrayList<String>> broderCategoryTable = new
            Hashtable<>();

    /**
     * Uri�ڵ��
     */
    private Hashtable<String, Vertex> uriVertexTable = new Hashtable<>();

    /**
     * �ڵ��������
     */
    private int i = 1;
    
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
         * Label��
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
         * @param uri   uri��
         * @param label label��
         * @param id    id��
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
        // ��Mapping�ж�ȡ���еĸ��
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

        // ��ȡÿ�������label��
        for (String uri : uriList) {
            ResultSet set = ResultSetFactory.fromXML(Net
                    .fetchStringFromUrlSafely(buildLabelUrl(uri)));
            String label = "";

            while (set.hasNext()) {
                label = set.next().get("l").toString();
                Vertex vertex = new Vertex(uri, label, i);
                i = i + 1;
                uriVertexTable.put(uri, vertex);
                break;
            }
        }

        // �ݹ��ȡ����broder���
        broder(uriList);

        // ����ͼ��
        DefaultDirectedGraph<Vertex, Arc> graph = new DefaultDirectedGraph<>(Arc.class);
        for (String uri : broderCategoryTable.keySet()) {
            if (uriVertexTable.containsKey(uri)) {
                graph.addVertex(uriVertexTable.get(uri));
                for (String childUri : broderCategoryTable.get(uri)) {
                    if (uriVertexTable.containsKey(childUri)) {
                        if (!graph.containsVertex(uriVertexTable.get
                                (childUri))) {
                            graph.addVertex(uriVertexTable.get(childUri));
                        }
                        graph.addEdge(uriVertexTable.get(uri), uriVertexTable.get(childUri));
                    }
                }
            }
        }
        
        // ����graphml�ļ���
        try {
            BufferedWriter graphWriter = new BufferedWriter(new
                    OutputStreamWriter(new FileOutputStream(new File
                    (graphPath))));

            graphWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<graphml xmlns=\"http://graphml.graphdrawing.org/xmln"
                    + "s\">\n<key attr.name=\"label\" attr.type=\"string\" " 
                    +"for=\"node\" id=\"label\"/>\n<key attr.name=\"dbpediaUri\""
                    + "  attr.type=\"string\" for=\"node\" id=\"dbpediaUri\"/>\n"
                    + "<key attr.name=\"wikipediaUrl\"  attr.type=\"string\" for="
                    + "\"node\" id=\"wikipediaUrl\"/>\n<broderCategoryTable edged"
                    + "efault=\"directed\">\n");

            for (Vertex vertex : graph.vertexSet()) {
                int id = vertex.id;
                String label = vertex.label.replace("&", "&amp;");
                String dbpediaUri = vertex.uri.replace("&", "&amp;");
                String wikipediaUrl = "http://en.wikipedia.org/wiki/" 
                		+ dbpediaUri.substring(dbpediaUri.indexOf("Category"));
                graphWriter.write(String.format("<node id=\"%d\">\n<data key"
                        + "=\"label\">%s</data>\n<data key=\"dbpediaUri\">%s"
                        + "</data>\n<data key=\"wikipediaUrl\">%s</data>\n"
                        + "</node>\n", id, label, dbpediaUri, wikipediaUrl));
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
    public void broder(ArrayList<String> uriList) {
        for (String uri : uriList) {
            if (!broderCategoryTable.containsKey(uri)) {
                ArrayList<String> broderUris = broderUri(uri);
                broderCategoryTable.put(uri, broderUris);
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
    public ArrayList<String> broderUri(String uri) {
        ResultSet set = ResultSetFactory.fromXML(Net.fetchStringFromUrlSafely
        		(buildBroderUrl(uri)));
        ArrayList<String> broderUris = new ArrayList<>();
        while (set.hasNext()) {
            QuerySolution solution = set.next();
            String broderUri = solution.get("c").toString();
            String broderLabel = solution.get("l").toString();

            if (!uriVertexTable.containsKey(broderUri)) {
                Vertex vertex = new Vertex(broderUri, broderLabel, i);
                i = i + 1;
                uriVertexTable.put(uri, vertex);
            }

            broderUris.add(broderUri);
        }

        return broderUris;
    }

    /**
     * ����broder��ѯurl��
     *
     * @param uri uri��
     * @return ���ɵĲ�ѯurl��
     */
    private String buildBroderUrl(String uri) {
        return Config.get(SPARQL_ENDPOINT) + "default-broderCategory" +
                "Table-uri=&query=select+%3Fc%2C%3Fl+where+%7B%0D%0A%3C" +
                URLEncoder.encode(uri) + "%3E+%3Chttp%3A%2F%2Fwww.w3" +
                ".org%2F2004" + "%2F02%2Fskos%2Fcore%23broader%3E+%3Fc" +
                ".%0D%0A%3Fc+%3Chttp%3A%2F" + "%2Fwww.w3" +
                ".org%2F2000%2F01%2Frdf-sch" + "ema%23label%3E+%3Fl.%0D" +
                "%0A+%7D&format=application%2Fsparql-results%2Bxml" +
                "&debug=on";
    }

    /**
     * ����label��ѯurl��
     * 
     * @param uri uri��
     * @return ���ɵĲ�ѯurl��
     */
    private String buildLabelUrl(String uri) {
        return Config.get(SPARQL_ENDPOINT) + "default-graph-uri=&que" +
                "ry=select+%3Fl+where+%7B%0D%0A%3C" + URLEncoder.encode(uri)
                + "%3E+%3Chttp%3A%2F%2Fwww.w3" +
                ".org%2F2000%2F01%2Frdf-schema%23" + "label%3E+%3Fl" +
                ".%0D%0A+%7D&format=application%2Fsparql-results%2Bxml" +
                "&debug" + "=on";
    }

    public static void main(String[] args) {
        new DbpediaCategoryBroderGraphBuilder().build("D:/��.txt", "D:/tree"
                + ".graphml");
    }
}
