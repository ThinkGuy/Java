import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 华为软件精英挑战赛。 最短路径。
 * 
 * @author Li,Pengfei; Liu,xinwei
 *
 */
public class DirectFind {
	// **************** 公开变量
	Set<Integer> vE = new HashSet<Integer>();
	ArrayList<Integer> lines = new ArrayList<Integer>();
	
long startTime = System.currentTimeMillis();
	
	/**
	 * 图文件路径。
	 */
	public static final String PATHOFMAP = "E:/HUAWEISoftCraft2016/test-case/ca"
			+ "se/topo.csv";
	 
	public static final String PATHOFDEMAND = "E:/HUAWEISoftCraft2016/test-case"
			+ "/case/demand.csv";
	public static final String PATHOFRESULT = "E:/HUAWEISoftCraft2016/test-case"
			+ "/case/sample_result.csv";
	
	/**
	 * 深度。
	 */
	public static final int DEPTH = 9;
	

    // **************** 私有变量
	
	/**
	 * 图。
	 */
	private HashMap<Integer, HashMap<Integer, Edge>> map = new HashMap<>();
	/*
	 * 符合条件的路线。
	 */
	private ArrayList<ArrayList<Integer>> lineList = new ArrayList<>();
	/**
	 * 必须经过的点V'。
	 */
	private ArrayList<Integer> v = new ArrayList<>();
	
	/**
	 * 起点
	 */
	private int source;
	/**
	 * 终点。
	 */
	private int destination;
	/**
	 * 当前的深度。
	 */
	private int currentDepth;
	/**
	 * 权值最小路径。
	 */
	private String minWeightline;
	

    // **************** 公开方法
	
	/**
	 * 读取图。
	 */
	public void readMap() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					PATHOFMAP)));

			String line;
			Edge edge;
			while ((line = reader.readLine()) != null) {
				String[] splits = line.split(",");
				edge = new Edge(Integer.parseInt(splits[0]), Integer.parseInt(
						splits[3]));
				
				if (v.contains(Integer.valueOf(splits[2]))) {
					edge.setVertex(Integer.valueOf(splits[2]));
				}
				Integer key = Integer.valueOf(splits[1]);
				
				if (map.containsKey(key)) {
					if (!splits[2].equals("" + source)) {
						map.get(key).put(Integer.valueOf(splits[2]), edge);
					}
				} else if (!splits[1].equals("" + destination)){
					HashMap<Integer, Edge> insideMap = new HashMap<>();
					insideMap.put(Integer.valueOf(splits[2]), edge);
					map.put(Integer.valueOf(splits[1]), insideMap);
				}
				
			}
			reader.close();
System.out.println(map);
		} catch (FileNotFoundException e) {
			System.out.println("文件未找到！");
		} catch (IOException e) {
			System.out.println("读写文件出错！");
		}
		
System.out.println(map.size());
	}
	
	/**
	 * 读取需要。
	 */
	public void readDemand() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					PATHOFDEMAND)));

			String line;
			while ((line = reader.readLine()) != null) {
				String[] splits = line.split(",");
				source = Integer.parseInt(splits[0]);
				destination = Integer.parseInt(splits[1]);
				
				String[] s = splits[2].split("\\|");
				
				for (String vertex : s) {
					v.add(Integer.parseInt(vertex));
				}
			}
System.out.println(v);
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("文件未找到！");
		} catch (IOException e) {
			System.out.println("读写文件出错！");
		}
	}
	
	/**
	 * 深度遍历图。
	 */
	public void dfs(Integer vertex) {
		
		while (map.containsKey(vertex) && (vE.size() < v.size() || !vertex.
				equals(destination))) {
			for (Integer pointer : map.get(vertex).keySet()) {
			
			    if (!lines.contains(pointer) && (vE.size() < v.size() && !pointer
			    		.equals(destination) || (!(vE.size() < v.size()) && 
			    				(pointer.equals(destination))))) {
					
					if (v.contains(pointer)) {
						vE.add(pointer);
					}
					
					lines.add(pointer);
					vertex = pointer;
					break;
			    } 
			}
		}
		
		if (vE.size() == v.size() && vertex.equals(destination)) {
			ArrayList<Integer> items = new ArrayList<Integer>();
			items = (ArrayList<Integer>) lines.clone();
			lineList.add(items);
System.out.println(items);
System.out.println("找到了共耗时"+ (System.currentTimeMillis()-startTime) + "ms");
//System.exit(1);
		} 
		
	}
	
	/**
	 * 广度遍历图。
	 */
	public void bfs(Integer vertex) {
		
	}
	
	public void calculateShortestLine() {
		String line;
		int minWeight = 10000;
		for (ArrayList<Integer> pointLine : lineList) {
			int weight = 0;
			minWeightline = "";
			for (int i=0; i<pointLine.size()-1; i++) {
				Integer integer = pointLine.get(i);
				Integer sInteger = pointLine.get(i+1);
				minWeightline = minWeightline + map.get(integer).get(sInteger).linkID + "|";
				weight = weight + map.get(integer).get(sInteger).weight;
			}
			
			if (minWeight > weight) {
				minWeight = weight;
System.out.println(pointLine);
			}
		}
System.out.println(minWeightline);
System.out.println(minWeight);
	}
	
	/**
	 * 最终路线写入文件。
	 */
	public void writeLine() {
		if (minWeightline == null) {
			return;
		}
		minWeightline = minWeightline.substring(0,minWeightline.length()-1);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					PATHOFRESULT)));
			writer.write(minWeightline.trim());
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("读写文件出错！");
		}
	}
	
	public static void main(String[] args) {
		
		DirectFind directFind = new DirectFind();
		directFind.readDemand();
		directFind.readMap();
		directFind.lines.add(directFind.source);
		directFind.dfs(directFind.source);
		directFind.calculateShortestLine();
		directFind.writeLine();
		
		System.out.println("共" + directFind.lineList.size() + "条");
		System.out.println("共耗时"+ (System.currentTimeMillis()-directFind.startTime) + "ms");
		
	}
}
