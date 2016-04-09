/**
 * 希尔排序。
 * @author 刘鑫伟
 *
 */
public class ShellSort {
	public static void main(String[] args) {
		int[] a = {5,3,8,6,1,9,2,7,4};
		int l = a.length;
		while(true){
		    for(int i=0;i<l;i++){
		        for(int j=i;j+l<a.length;j+=l){
		        int temp;
		        if(a[j]>a[j+l]){
		            temp=a[j];
		            a[j]=a[j+l];
		            a[j+l]=temp;
		            }
		        }
		    }
		     
		     
		    if(l==1){break;}
		    l--;
		}
		
		for (int i=0; i<a.length; i++) {
			System.out.print(a[i] + " ");
		}
	}
}
