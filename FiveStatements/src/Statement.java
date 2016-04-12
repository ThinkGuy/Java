/**
 * 进程。
 * @author 刘鑫伟
 *
 */
public class Statement {
	/** 标识 */
	private String name;

	/*
	 * 默认构造函数。
	 */
	public Statement() {}
	
	/**
	 * 带参构造函数。
	 * @param name
	 */
	public Statement(String name) {
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
	
	
}
