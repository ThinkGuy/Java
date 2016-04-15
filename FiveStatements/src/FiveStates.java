import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * FiveStates.
 * @author Liu, xinwei
 *
 */
public class FiveStates {
	private Scanner in = new Scanner(System.in);
	/** New */
	private Queue<State> newQueue = new LinkedList<State>();
	/** Ready*/
	private Queue<State> ready = new LinkedList<State>();
	/** Running*/
	private Queue<State> running = new LinkedList<State>();
	/** Blocked*/
	private Queue<State> blocked = new LinkedList<State>();
	/** Exit  */
	private Queue<State> exit = new LinkedList<State>();
	
	/**
	 * create a state.
	 */
	public void create() {
		System.out.println("please input demand:");
		State state = new State(in.next());
		newQueue.add(state);
		System.out.println("demand<" + state.getName() + ">has been created.");
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
		if (running.size() > 0) {
			System.out.println("there is a state running!");
			return;
		}
		
		State state = ready.poll();
		judge(running, state);
	}
	
	/**
	 * Running to Ready.
	 */
	public void timeout() {
		State state = running.poll();
		judge(ready, state);
		//dispatch automatic.
		dispatch();
		
	}
	
	/**
	 * Running to Blocked.
	 */
	public void eventWait() {
		State state = running.poll();
		judge(blocked, state);
		//dispatch automatic.
		dispatch();
	}
	
	/**
	 * Blocked to Ready.
	 */
	public void eventOccurs() {
		State state = blocked.poll();
		judge(ready, state);
		
		if (running.size() == 0 && ready.size() > 0) {
			dispatch();
		}
	}
	
	/**
	 * running to exit.
	 */
	public void release() {
		State state = running.poll();
		judge(exit, state);
		//dispatch automatic.
		dispatch();
	}
	
	/**
	 * Judge.
	 * @param queue
	 * @param state
	 */
	public void judge(Queue<State> queue, State state) {
		if (state != null) {
			queue.add(state);
		}
	}
	
	/**
	 * show all states.
	 */
	public void showStates() {
		System.out.println("The states Now:");
		System.out.println("New" + newQueue.toString());
		System.out.println("Ready" + ready.toString());
		System.out.println("Running" + running.toString());
		System.out.println("Blocked" + blocked.toString());
		System.out.println("Exit" + exit.toString());
	}
	
	/**
	 * show the window.
	 */
	public void showWindow() {
		System.out.println("***********************************FiveStates***********************************************");
		System.out.println("1.Create   2.Admit    3.Dispatch   4.TimeOut   5.EventOccurs  6.EventWait  7.release  0.Exit");
		System.out.println("********************************************************************************************");
	}
	
	/**
	 * controllor.
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
				case "0": break;
				default : {
					showWindow();
					System.out.println("Your demand is wrong, please input again:");
					continue;
				}
			}
			
			showStates();
			showWindow();
		}
		
		System.out.println("Thanks for use.");
	}
	public static void main(String[] args) {
		FiveStates fiveStates = new FiveStates();
		fiveStates.control();
		fiveStates.in.close();
	}
}
