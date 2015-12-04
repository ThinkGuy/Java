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
 * CCS-DBPedia映射分类Broder分类图构建工具。
 *
 * @author Liu, Xinwei; ZHang, Yin
 */
public class DbpediaCategoryBroderGraphBuilder {

    // **************** 公开变量

    /**
     * Sparql endpont地址，截止到?。
     */
    public static final String SPARQL_ENDPOINT = "cx.exp" + "" +
            ".dbpediacategorybrodergraphbuilder.sparqlendpoint";

    /**
     * 边类型。
     */
    public static class Arc {
    }

    // **************** 私有变量

    /**
     * 上层分类表。
     */
    private Hashtable<String, ArrayList<String>> broderCategoryTable = new
            Hashtable<>();

    /**
     * Uri节点表。
     */
    private Hashtable<String, Vertex> uriVertexTable = new Hashtable<>();

    /**
     * 节点计数器。
     */
    private int i = 1;
    
    /**
     * 节点类型。
     */
    private static class Vertex {

        // **************** 公开变量

        /**
         * Uri。
         */
        public String uri = "";

        /**
         * Label。
         */
        public String label = "";

        /**
         * Id。
         */
        public int id = 0;

        // **************** 私有变量

        // **************** 继承方法

        // **************** 公开方法

        /**
         * 节点类型构造函数。
         *
         * @param uri   uri。
         * @param label label。
         * @param id    id。
         */
        public Vertex(String uri, String label, int id) {
            this.uri = uri;
            this.label = label;
            this.id = id;
        }

        // **************** 私有方法

    }

    // **************** 继承方法

    // **************** 公开方法

    public void build(String mappingPath, String graphPath) {
        // 从Mapping中读取所有的概念。
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

        // 读取每个概念的label。
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

        // 递归获取所有broder概念。
        broder(uriList);

        // 构建图。
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
        
        // 生成graphml文件。
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

    // **************** 私有方法

    /**
     * 递归获得父亲节点。
     *
     * @param uriList 子节点集合。
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
     * 获得上层uri。
     *
     * @param uri uri。
     * @return 获得上层uri。
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
     * 生成broder查询url。
     *
     * @param uri uri。
     * @return 生成的查询url。
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
     * 生成label查询url。
     * 
     * @param uri uri。
     * @return 生成的查询url。
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
        new DbpediaCategoryBroderGraphBuilder().build("D:/树.txt", "D:/tree"
                + ".graphml");
    }
}
