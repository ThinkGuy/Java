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
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 华为软件精英挑战赛。 最短路径。
 * 
 * @author Li,Pengfei; Liu,xinwei
 *
 */
public class WebOfFuture {
	
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
	 * 图。
	 */
	private HashMap<Integer, HashMap<Integer, Edge>> map = new HashMap<>();
	
	/*
	 * 符合条件的路线。
	 */
	private ArrayList<ArrayList<Integer>> lineList = new ArrayList<>();
	
	/**
	 * 起点
	 */
	private int source;
	/**
	 * 终点。
	 */
	private int destination;
	/**
	 * 权值最小路径。
	 */
	private String minWeightline = "";
	/**
	 * 必须经过的点V'。
	 */
	private ArrayList<Integer> v = new ArrayList<>();
	
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
			Edge edge;
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
	
	public void traverseMap() {
		Integer vertex = source;
		String line = "";
		long minWeight = 100000;
		Queue<ArrayList<Integer>> lineQueue = new LinkedBlockingQueue<>();
		
		ArrayList<Integer> lines = new ArrayList<Integer>();
		lines.add(vertex);
		
		Set<Integer> vE = new HashSet<Integer>();
		//当前点不是终点且V'全部走完。
		while (true) {
			vE = new HashSet<Integer>();
			
			if (map.containsKey(vertex)) {
				for (Integer target : map.get(vertex).keySet()) {
					
					//没有重复。
					if (!lines.contains(target)) {
						ArrayList<Integer> items = new ArrayList<Integer>();
						items = (ArrayList<Integer>) lines.clone();
						items.add(target);
						//***********
						
						if (vE.size()<v.size() && vertex.equals(destination)) {
							continue;
						}
						lineQueue.add(items);
					}
				}
			} 
			
			lines = lineQueue.poll();
//System.out.println(lines);
			if (lines == null) {
				break;
			}
			
			for (Integer integer : lines) {
				if (v.contains(integer)) {
					vE.add(integer);
				}
			}
			
			vertex = lines.get(lines.size()-1);
			
			if (!(vE.size() < v.size()) && vertex.equals(destination)) {
				lineList.add(lines);
//				break;
			}
		}
		
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
		long startTime = System.currentTimeMillis();
		
		WebOfFuture webOfFuture = new WebOfFuture();
		webOfFuture.readDemand();
		webOfFuture.readMap();
		webOfFuture.traverseMap();
		webOfFuture.writeLine();
		
		System.out.println("共耗时"+ (System.currentTimeMillis()-startTime) + "ms");
		
	}
}
