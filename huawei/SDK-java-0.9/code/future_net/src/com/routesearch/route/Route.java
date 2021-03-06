package com.routesearch.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;


public final class Route {

	/**
	 * 深度。
	 */
	public static final int DEPTH = 8;
	
	public static Set<Integer> vE = new HashSet<Integer>();
	public static ArrayList<Integer> lines = new ArrayList<Integer>();
	
	
	static long startTime = System.currentTimeMillis();
	/**
	 * 图。
	 */
	private static HashMap<Integer, HashMap<Integer, Edge>> map = new HashMap<>();
	
	/*
	 * 符合条件的路线。
	 */
	private static ArrayList<ArrayList<Integer>> lineList = new ArrayList<>();
	
	/**
	 * 起点
	 */
	private static int source;
	/**
	 * 终点。
	 */
	private static int destination;
	/**
	 * 权值最小路径。
	 */
	private static String minWeightline = "";
	/**
	 * 必须经过的点V'。
	 */
	private static ArrayList<Integer> v = new ArrayList<>();
	/**
	 * 当前的深度。
	 */
	private static int currentDepth;
	
    /**
     * 你需要完成功能的入口
     * 
     * @author Liu,xinwei; Li,pengfei.
     * @since 2016-3-15
     * @version V2
     */
    public static String searchRoute(String graphContent, String condition)
    {
        readConditionAndGraph(graphContent, condition);
		lines.add(source);
		
		//改造深度优先。
//		dfs(source);
		//广度优先。
		bfs(source);
		
//        if (map.size() > 20) {
//            dfs(source);
//        } else {
//        	bfs(source);
//        }

        calculateShortestLine();
System.out.println("123");

        if (lineList.size() > 0) {
             minWeightline = minWeightline.substring(0,minWeightline.length()-1);
        return minWeightline.trim();
        }
        return "NA";
       
    }
    
    /**
     * read the condition and the Graph.
     * @param graphContent
     * @param condition
     */
    public static void readConditionAndGraph(String graphContent, String condition) {
    	//read condition.
        condition = condition.trim();
        String[] splits = condition.split(",");
        source = Integer.parseInt(splits[0]);
        destination = Integer.parseInt(splits[1]);
        String[] s = splits[2].split("\\|");
        
        for (String vertex : s) {
            v.add(Integer.parseInt(vertex));
        }
 
        //read graph.
        String[] reader = graphContent.split("\n");
        Route.Edge edge;
        
        for (String line : reader) {
            line = line.trim();
            String[] gSplits = line.split(",");
            edge = new Route.Edge(Integer.parseInt(gSplits[0]), Integer.parseInt(
                    gSplits[3]));
            Integer key = Integer.valueOf(gSplits[1]);
            
            if (map.containsKey(key)) {
                if (!gSplits[2].equals("" + source)) {
                    map.get(key).put(Integer.valueOf(gSplits[2]), edge);
                }
            } else if (!gSplits[1].equals("" + destination)){
                HashMap<Integer, Edge> insideMap = new HashMap<>();
                insideMap.put(Integer.valueOf(gSplits[2]), edge);
                map.put(Integer.valueOf(gSplits[1]), insideMap);
            }
        }
    }
    
    /**
	 * 深度遍历图。
	 */
	public static void dfs(Integer vertex) {
		if (map.containsKey(vertex) && (vE.size() < v.size() || !vertex.equals(destination))) {
			
			for (Integer pointer : map.get(vertex).keySet()) {
				
			    if (!lines.contains(pointer)) {
			    	if (v.contains(pointer)) {
						currentDepth = 0;
					} else {
						currentDepth++;
					}
					
					if (currentDepth > DEPTH) {
						currentDepth--;
						lines.remove(vertex);
						return;
					}
					
					if (v.contains(pointer)) {
						vE.add(pointer);
					}
					lines.add(pointer);
					dfs(pointer);
			    } 
			}
		} else if (vE.size() == v.size() && vertex.equals(destination)) {
			ArrayList<Integer> items = new ArrayList<Integer>();
			items = (ArrayList<Integer>) lines.clone();
			lineList.add(items);
			currentDepth--;
			lines.remove(vertex);
			return;
		} 
		
		if (vE.contains(vertex)) {
			vE.remove(vertex);
		}
		lines.remove(vertex);
		currentDepth--;
		
		if (System.currentTimeMillis()-startTime > 9000) {
            return;
        }
	}
    
    /**
	 * 广度遍历图。
	 */
	public static void bfs(Integer vertex) {
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
                        
                        if (vE.size()<v.size() && vertex.equals(destination)) {
                            continue;
                        }
                        lineQueue.add(items);
                    }
                }
            } 
            
            lines = lineQueue.poll();
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
            }

            if (System.currentTimeMillis()-startTime > 9000) {
                break;
            }
        }
	}
    
	/**
	 * Calculate the Shortest Line.
	 */
	public static void calculateShortestLine() {
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
			}
		}
	}

	/**
	 * Edge.
	 * @author 刘鑫伟
	 *
	 */
    public static class Edge {
        public int linkID;
        public int weight;
        private Integer vertex;
        
        public Edge() {
        }
        
        public Edge(int linkID, int weight) {
            super();
            this.linkID = linkID;
            this.weight = weight;
        }
        
        /**
         * @return the vertex
         */
        public Integer getVertex() {
            return vertex;
        }

        /**
         * @param vertex the vertex to set
         */
        public void setVertex(Integer vertex) {
            this.vertex = vertex;
        }
    }

}
