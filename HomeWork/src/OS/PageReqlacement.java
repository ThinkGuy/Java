package OS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class PageReqlacement {
	public static final int MEMORYSIZE = 3;
	
	/**
	 * random sequence.
	 */
	private int[] accessSeries = new int[12];
	private int lru;
	private int fifo;
	private int totalInstruction = accessSeries.length;
	/**
	 * disaffectLRU.
	 */
	private int disaffectLRU;
	
	/**
	 * disaffectfifo;
	 */
	private int disaffectfifo;
	
	ArrayList<Frame> frames = new ArrayList<Frame>(MEMORYSIZE);
	
	HashMap<Integer, Frame> framMap = new HashMap<Integer, Frame>();
	
	/**
	 * initial the random sequence.
	 */
	public void initSequence() {
		Random rd = new Random();
		for (int i=0; i<accessSeries.length; i++) {
			accessSeries[i] = rd.nextInt(5) + 1;
			System.out.print(accessSeries[i]);
		}
	}
	
	/**
	 * initial FramMap.
	 */
	public void initFramMap() {
		for (int i=1; i<6; i++) {
			framMap.put(i, new Frame(i));
		}
		System.out.println(framMap.toString());
	}
	
	/**
	 * imitate stack by LRU.
	 * @param pageNo pageNo.
	 */
	public void imitateStackByLRU(int pageNo) {
		
		if (!frames.contains(framMap.get(pageNo))) {
			
			if (lru < MEMORYSIZE) {
				frames.add(framMap.get(pageNo));
				lru ++;
			} else {
				frames.remove(0);
				frames.add(framMap.get(pageNo));
			}
			disaffectLRU ++;
		} else {
			
			if (lru < MEMORYSIZE) {
				return;
			} else {
				Frame frame = frames.get(0);
				frames.remove(0);
				frames.add(frame);
			}
		}
		
	}
	
	/**
	 * imitate stack by FIFO.
	 * @param pageNO pageNo.
	 */
	public void imitateStackByFIFO(int pageNo) {
		if (!frames.contains(framMap.get(pageNo))) {
			
			if (fifo < MEMORYSIZE) {
				frames.add(framMap.get(pageNo));
				fifo ++;
			} else {
				frames.remove(0);
				frames.add(framMap.get(pageNo));
			}
			
			disaffectfifo ++;
		}
	}
	
	/**
	 * page replacement by lru.
	 * @return
	 */
	public float lru() {
		for (int pageNo : accessSeries) {
			imitateStackByLRU(pageNo);
			System.out.println(frames);
		}
		
		return (float)disaffectLRU / totalInstruction;
	}
	
	/**
	 * page replacement by fifo.
	 * @return 
	 */
	public float fifo() {
		for (int pageNo : accessSeries) {
			imitateStackByFIFO(pageNo);
		}
		
		return (float)disaffectfifo / totalInstruction;
	}
	
	public static void main(String[] args) {
		PageReqlacement pageReqlacement =  new PageReqlacement();
		pageReqlacement.initSequence();
		pageReqlacement.initFramMap();
		System.out.println(pageReqlacement.lru());
		System.out.println(pageReqlacement.fifo());
	}
}
