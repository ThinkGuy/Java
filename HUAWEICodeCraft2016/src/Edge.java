
public class Edge {
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[linkID=" + linkID + ", weight=" + weight + ", vertex="
				+ vertex + "]";
	}
	
}
