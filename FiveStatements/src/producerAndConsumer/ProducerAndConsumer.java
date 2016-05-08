package producerAndConsumer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;


/**
 * Producer and consumer problem.
 * 
 * @author Liu,xinwei.
 *
 */
public class ProducerAndConsumer {
	public Scanner in = new Scanner(System.in);
	
	/**
	 * block queue.
	 */
	private Queue<String> blockQueue = new LinkedList<>();
	
	/**
	 * wait queue.
	 */
	private Queue<String> waitQueue = new LinkedList<>();
	
	/**
	 * The waiting consumer counter.
	 */
	private Integer counter = 0;
	
	/**
	 * product number.
	 */
	private int proNum = 0;
	
	/**
	 * is block queue full.
	 * @return is block queue full.
	 */
	public boolean isQueueFull() {
//		blockQueue.remove(0);
		return true;
	}
	
	/**
	 * produce something.
	 */
	public void produce() {
		proNum ++;
		
		//if blockQueueOverFlow.
		if (blockQueue.size() >= 5) {
			dealWithOverflow();
		} else {
			blockQueue.add("" + proNum);
			
			if (counter > 0) {
				consume();
				counter --;
			} 
		}
		
		System.out.println("produce : " + proNum);
	}
	
	/**
	 * consume something.
	 */
	public void consume() {
		int product = 0;
		if (blockQueue.size() == 0) {
			counter ++;
		} else {
			product = Integer.parseInt(blockQueue.poll());
			
			if (waitQueue.size() > 0) {
				blockQueue.add(waitQueue.poll());
			}
		}
	
		System.out.println("consume: " + product);
	}
	
	/**
	 * deal with the block queue overflow.
	 */
	public void dealWithOverflow() {
		waitQueue.add("" + proNum);
	}
	
	/**
	 * display the present state.
	 */
	public void display() {
		System.out.println("Block Queue: " + blockQueue.toString());
		System.out.println("Wait Queue: " + waitQueue.toString());
		System.out.println("consumer counter:" + counter);
	}
	
	/**
	 *control.
	 */
	public void control(String command) {
		
		while (!"0".equals(command)) {
			command = in.next();
			if ("p".equals(command)) {
				produce();
				display();
			} else if ("c".equals(command)) {
				consume();
				display();
			} else if ("0".equals(command)){ 
				System.out.println("Thank for using.");
			} else {
				System.out.println("Your command is wrong, please input again:");
			}
		}
	}
	
	public static void main(String[] args) {
		Random random = new Random();
		ProducerAndConsumer pAC = new ProducerAndConsumer();
		System.out.println("p: produce c: consume 0:exit");
		
		while (true) {
			if (random.nextInt(20000000)%1999998 == 0) {
				pAC.produce();
				pAC.display();
			} else if (random.nextInt(20000000)%1999999 == 0) {
				pAC.consume();
				pAC.display();
			}
		}
		// pAC.control("1");
	}
}
