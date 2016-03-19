
public class ToPo {
	//边编号。
	private int edge;
	//源点。
	private int source;
	//目标点。
	private int target;
	//权值。
	private int weight;
	
	public ToPo() {
		
	}

	/**
	 * 带参构造函数。
	 */
	public ToPo(int edge, int source, int target, int weight) {
		this.edge = edge;
		this.source = source;
		this.target = target;
		this.weight = weight;
	}

	/**
	 * @return the edge
	 */
	public int getEdge() {
		return edge;
	}

	/**
	 * @param edge the edge to set
	 */
	public void setEdge(int edge) {
		this.edge = edge;
	}

	/**
	 * @return the source
	 */
	public int getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(int source) {
		this.source = source;
	}

	/**
	 * @return the target
	 */
	public int getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(int target) {
		this.target = target;
	}

	/**
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ToPo [edge=" + edge + ", source=" + source + ", target="
				+ target + ", weight=" + weight + "]";
	}
	
}
