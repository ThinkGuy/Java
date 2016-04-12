import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import com.sun.org.apache.xerces.internal.impl.xpath.XPath.Step;

import sun.java2d.Disposer.PollDisposable;


/**
 * 五状态。
 * @author 刘鑫伟
 *
 */
public class FiveStatements {
	/** 创建状态 */
	private Queue<Statement> newQueue = new LinkedList<Statement>();
	/** 就绪状态。*/
	private Queue<Statement> ready = new LinkedList<Statement>();
	/** 运行状态。*/
	private Queue<Statement> running = new LinkedList<Statement>();
	/** 阻塞状态。*/
	private Queue<Statement> blocked = new LinkedList<Statement>();
	/** 退出状态  */
	private Queue<Statement> exit = new LinkedList<Statement>();
	
	/**
	 * create a statement.
	 * @param statement statement.
	 */
	public void create(Statement statement) {
		newQueue.add(statement);
	}
	
	/**
	 * Ready to Running.
	 */
	public void dispatch() {
		//poll可以返回null.
		running.add(ready.poll());
	}
	
	/**
	 * new to ready.
	 */
	public void admit() {
		ready.add(newQueue.poll());
	}
	
	/**
	 * Running to Ready.
	 */
	public void timeout() {
		ready.add(running.poll());
	}
	
	/**
	 * Running to Blocked.
	 */
	public void eventWait() {
		blocked.add(running.poll());
	}
	
	/**
	 * Blocked to Running.
	 */
	public void eventOccurs() {
		running.add(blocked.poll());
	}
	
	/**
	 * running to exit.
	 */
	public void release() {
		exit.add(running.poll());
	}
	
	/**
	 * 显示窗口。
	 */
	public void showWindow() {
		System.out.println("********************五状态系统*********************");
		System.out.println("1.创建                                         2.提交                                       3.调度");
		System.out.println("4.超时                                         5.等待事件                               6.事件发生");
		System.out.println("7.释放                                         8.显示各状态                           0.退出系统");
		System.out.println("************************************************");
		
		
	}
	
	/**
	 * 控制器。
	 */
	public void control() {
		showWindow();
		//显示加入编号
		
		//1 create+1
		//1admit new to ready
		//2dispatch
		//3timeout
		//4eventwait
		//5eventoccurs
		//6release runnintoexit
		
		Scanner in = new Scanner(System.in);
		String temp;
		Statement statement;
		while (!"exit".equals((temp = in.next()))) {
			System.out.println(temp);
			
		}
	}
	public static void main(String[] args) {
		FiveStatements fiveStatements = new FiveStatements();
		fiveStatements.control();
	}
}
