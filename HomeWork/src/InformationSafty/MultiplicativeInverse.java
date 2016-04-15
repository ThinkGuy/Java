package InformationSafty;
import java.util.Scanner;

/**
 * 乘法逆元。
 * @author 刘鑫伟
 *
 */
public class MultiplicativeInverse {
	// e * d % f = 1 求e对f的乘法逆元
	public int mI(int e, int f) {
		int x2 = 0, x3 = f, y2 = 1, y3 = e, q, t2, t3;
		while (true) {
			if (y3 == 0) return 0;
			if (y3 == 1) {
				if (y2 < 0) y2 += f;
				System.out.println(e + " * " + y2 + " % " + f + " = " + e * y2 % f);		
				return y2;
			}
			q = x3 / y3;
			t2 = x2 - q * y2;
			t3 = x3 - q * y3;
			x2 = y2; x3 = y3;
			y2 = t2; y3 = t3;

		}
	}

	public static void main(String[] args) {
		MultiplicativeInverse mI = new MultiplicativeInverse();
		Scanner in = new Scanner(System.in);
		int e = Integer.parseInt(in.next());
		int f = Integer.parseInt(in.next());
		
		System.out.println("所以" + e + "关于模" + f + "的乘法逆元为：" + mI.mI(e, f));
		
	}
}
