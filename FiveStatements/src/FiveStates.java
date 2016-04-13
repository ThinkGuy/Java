import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * 五状态。
 * @author 刘鑫伟
 *
 */
public class FiveStates {
	private Scanner in = new Scanner(System.in);
	/** 创建状态 */
	private Queue<State> newQueue = new LinkedList<State>();
	/** 就绪状态。*/
	private Queue<State> ready = new LinkedList<State>();
	/** 运行状态。*/
	private Queue<State> running = new LinkedList<State>();
	/** 阻塞状态。*/
	private Queue<State> blocked = new LinkedList<State>();
	/** 退出状态  */
	private Queue<State> exit = new LinkedList<State>();
	
	/**
	 * create a state.
	 */
	public void create() {
		System.out.println("请输入您要创建的命令:");
		State state = new State(in.next());
		newQueue.add(state);
		System.out.println("命令<" + state.getName() + ">已经被创建。");
	}
	
	/**
	 * new to ready.
	 */
	public void admit() {
		State state = newQueue.poll();
		judge(ready, state);
	}
	
	/**
	 * Ready to Running.
	 */
	public void dispatch() {
		State state = ready.poll();
		judge(running, state);
	}
	
	/**
	 * Running to Ready.
	 */
	public void timeout() {
		State state = running.poll();
		judge(ready, state);
		
	}
	
	/**
	 * Running to Blocked.
	 */
	public void eventWait() {
		State state = running.poll();
		judge(blocked, state);
		
	}
	
	/**
	 * Blocked to Running.
	 */
	public void eventOccurs() {
		State state = blocked.poll();
		judge(running, state);
	}
	
	/**
	 * running to exit.
	 */
	public void release() {
		State state = running.poll();
		judge(exit, state);
	}
	
	/**
	 * 判断处理。
	 * @param queue
	 * @param state
	 */
	public void judge(Queue<State> queue, State state) {
		if (state != null) {
			queue.add(state);
		}
	}
	
	/**
	 * 显示各个状态。
	 */
	public void showStates() {
		System.out.println("目前各状态内容如下:");
		System.out.println("New" + newQueue.toString());
		System.out.println("Ready" + ready.toString());
		System.out.println("Running" + running.toString());
		System.out.println("Blocked" + blocked.toString());
		System.out.println("Exit" + exit.toString());
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
		
		String temp;
		
		while (!"0".equals((temp = in.next()))) {
			switch (temp) {
				case "1": create(); break;
				case "2": admit(); break;
				case "3": dispatch(); break;
				case "4": timeout(); break;
				case "5": eventWait(); break;
				case "6": eventOccurs(); break;
				case "7": release(); break;
				case "8": showStates(); break;
				case "0": break;
				default : {
					showWindow();
					System.out.println("您的命令有误，请重新输入:");
					continue;
				}
			}
			
			showWindow();
		}
		
		System.out.println("欢迎再次使用，谢谢。");
	}
	public static void main(String[] args) {
		FiveStates fiveStates = new FiveStates();
		fiveStates.control();
		fiveStates.in.close();
	}
}
