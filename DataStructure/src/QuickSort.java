/**
 * QuickSort
 * @author 刘鑫伟
 *
 */
public class QuickSort {
	
	/*
	 * Method of quick Sort.
	 */
	public void quickSort(int[] a, int l, int r) {
		if (l < r) {
			int i=l, j=r;
			int base = a[i];
			
			while (i < j) {
				
				while (i < j && a[j] > base) {
					j--;
				}
				
				if (i < j) {
					a[i++] = a[j];
				}
				
				while (i < j && a[i] < base) {
					i++;
				}
				
				if (i < j) {
					a[j--] = a[i];
				}
			}
			a[i] = base;
			
			quickSort(a, l, i-1);
			quickSort(a, i+1, r);
		}
	}
	
	public static void main(String[] args) {
		int[] a = {5,3,8,6,1,9,2,7,4};
		new QuickSort().quickSort(a, 0, a.length-1);
		
		for (int i=0; i<a.length; i++) {
			System.out.print(a[i] + " ");
		}
		
	}
}
