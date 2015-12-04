package com.lxw.scoreManager.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataBase {

	public static final String STUDENT_TABLE = "studentInfo";
	public static final String MANAGER_TABLE = "managerList";

	private Connection connection;
	private Statement statement;
	private String driver = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/studentmanager";
	private String username = "root";
	private String password = "root";
	private Student student;

	public DataBase() {
		try {
			// 建立数据库的连接
			Class.forName(driver);
			connection = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private ResultSet excuteSqlReturnSet(String sql) {
		ResultSet resultSet;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return resultSet;
	}

	private boolean excuteSql(String sql) {

		try {
			int result = statement.executeUpdate(sql);
			if (result > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 查询登录状态。
	 * 
	 * @param studentId
	 * @return
	 */
	public Student isExist(String studentId) {

		student = new Student();
		String sql = "select studentId, password from " + STUDENT_TABLE
				+ " where studentId=" + studentId + ";";
		ResultSet resultSet = excuteSqlReturnSet(sql);

		student.setStudentId(0);
		student.setPassword(null);
		try {
			while (resultSet.next()) {
				student.setStudentId(Integer.valueOf(resultSet
						.getString("studentId")));
				student.setPassword(resultSet.getString("password"));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return student;
	}

	/**
	 * 根据学生学号查询成绩。
	 * 
	 * @param studentId
	 * @return
	 */
	public Student searchInfo(String studentId) {
		student = new Student();
		Score score = new Score();
		String sql = "select studentId, name, chinese, english, math from "
				+ STUDENT_TABLE + " where studentId=" + studentId + ";";

		try {
			ResultSet resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				student.setStudentId(Integer.valueOf(resultSet
						.getString("studentId")));
				student.setName(resultSet.getString("name"));
				score.setChinese(Integer.parseInt(resultSet
						.getString("chinese")));
				score.setEnglish(Integer.parseInt(resultSet
						.getString("english")));
				score.setMath(Integer.parseInt(resultSet.getString("math")));
				student.setScore(score);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return student;
	}

	/**
	 * 根据学生姓名删除学生成绩。
	 * 
	 * @param studentId
	 *            学号。
	 * @return
	 */
	public boolean deleteInfo(String studentId) {
		String sql = "delete from " + STUDENT_TABLE + " where studentId="
				+ studentId + ";";

		return excuteSql(sql);
	}

	/**
	 * 更新成绩。
	 * 
	 * @param studentId
	 *            学号。
	 * @param chinese
	 *            语文。
	 * @param english
	 *            英语。
	 * @param math
	 *            数学。
	 * @return
	 */
	public boolean updateInfo(String studentId, int chinese, int english,
			int math) {
		String sql = "update " + STUDENT_TABLE + " set chinese=" + chinese
				+ ",english=" + english + ",math=" + math + " where studentId="
				+ studentId + ";";

		return excuteSql(sql);
	}

	/**
	 * 
	 * @param studentId
	 * @param name
	 * @param chinese
	 * @param english
	 * @param math
	 * @return
	 */
	public boolean insertInfo(String studentId, String name, int chinese,
			int english, int math) {
		String sql = "insert into " + STUDENT_TABLE
				+ " (studentId, name, chinese, " + "english, math) values("
				+ studentId + ",'" + name + "'," + chinese + "," + english
				+ "," + math + ");";

		return excuteSql(sql);
	}

	public static void main(String[] args) {
		DataBase dateBase = new DataBase();
		System.out.println(dateBase.insertInfo("20134010", "刘鑫伟", 88, 99, 77));
		System.out.println(dateBase.isExist("20134019").getStudentId());
		System.out.println(dateBase.searchInfo("20134019").getScore()
				.getChinese());
		System.out.println(dateBase.deleteInfo("20134010"));
		System.out.println(dateBase.updateInfo("20134019", 99, 88, 77));

	}
}
