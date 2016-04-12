/**
 * 进程。
 * @author 刘鑫伟
 *
 */
public class State {
	/** 标识 */
	private String name;

	/*
	 * 默认构造函数。
	 */
	public State() {}
	
	/**
	 * 带参构造函数。
	 * @param name
	 */
	public State(String name) {
		super();
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
	
}
