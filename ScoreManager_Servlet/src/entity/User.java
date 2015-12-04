package entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 用户类。
 * @author 刘鑫伟
 *
 */
@Entity
public class User {

	@Id
	@GeneratedValue
	private int uid;
	private String usename;
	private String password;

	public User() {

	}

	public User(int uId, String usename, String password) {
		// super();
		this.uid = uId;
		this.usename = usename;
		this.password = password;
	}

	public int getuId() {
		return uid;
	}

	public void setuId(int uId) {
		this.uid = uId;
	}

	public String getUsename() {
		return usename;
	}

	public void setUsename(String usename) {
		this.usename = usename;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [uId=" + uid + ", usename=" + usename + ", password="
				+ password + "]";
	}
	

}
