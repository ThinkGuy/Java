import java.util.LinkedList;
import java.util.Queue;

/**
 * 五状态。
 * @author 刘鑫伟
 *
 */
public class FiveStatements {
	/** 就绪状态。*/
	Queue<Statement> ready = new LinkedList<Statement>();
	/** 运行状态。*/
	Queue<Statement> running = new LinkedList<Statement>();
	/** 阻塞状态。*/
	Queue<Statement> blocked = new LinkedList<Statement>();
	
	/**
	 * Ready to Running.
	 */
	public void dispatch() {
		
	}
	
	/**
	 * Running to Ready.
	 */
	public void timeout() {
		
	}
	
	/**
	 * Running to Blocked.
	 */
	public void eventWait() {
		
	}
	
	/**
	 * Blocked to Running.
	 */
	public void eventOccurs() {
		
	}
}
