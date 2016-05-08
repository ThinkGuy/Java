/**
 * State.
 * @author Liu, xinwei
 *
 */
public class State {
	/** name */
	private String name;

	/*
	 * default construct.
	 */
	public State() {}
	
	/**
	 * construct.
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
