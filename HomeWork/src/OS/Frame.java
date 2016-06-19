package OS;

public class Frame {
	int pageNo;
	char flag;
	
	public Frame() {
		
	}
	
	public Frame(int pageno) {
		this.pageNo = pageno;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Frame [pageNo=" + pageNo + ", flag=" + flag + "]";
	}
	
}
