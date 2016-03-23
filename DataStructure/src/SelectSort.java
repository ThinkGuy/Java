/**
 * 选择排序。
 * @author 刘鑫伟
 *
 */
public class SelectSort {

	public static void main(String[] args) {
		int[] a = {5,3,8,6,1,9,2,7,4};
		int min = 0;
		int temp = 0;
		
		for (int i=0; i<a.length-1; i++) {
			min = i;
			for (int j=i+1; j<a.length; j++) {
				
				if (a[min] > a[j]) {
					min = j;
				}
			}
			
			if (min != i) {
				temp = a[min];
				a[min] = a[i];
				a[i] = temp;
			}
		}
		
		for (int i=0; i<a.length; i++) {
			System.out.print(a[i] + " ");
		}
	}
}
