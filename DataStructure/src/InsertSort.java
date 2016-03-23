/**
 * 插入排序。
 * @author 刘鑫伟
 *
 */

public class InsertSort {
	public int[] insertSort(int[] a) {
		if (a == null || a.length < 2) {
			return a;
		}
		for (int i = 1; i < a.length; i++) {
			for (int j = i; j > 0; j--) {
				if (a[j] < a[j - 1]) {
					int temp = a[j];
					a[j] = a[j - 1];
					a[j - 1] = temp;
				} else {
					// 接下来是无用功
					break;
				}
			}
		}
		return a;
	}

	public static void main(String[] args) {
		int[] a = { 5, 3, 8, 6, 1, 9, 2, 7, 4 };
		new InsertSort().insertSort(a);

		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i] + " ");
		}

	}
}
