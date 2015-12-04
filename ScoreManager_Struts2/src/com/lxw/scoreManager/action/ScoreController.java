package com.lxw.scoreManager.action;



import com.lxw.scoreManager.model.DataBase;
import com.lxw.scoreManager.model.Score;
import com.lxw.scoreManager.model.Student;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class ScoreController extends ActionSupport{
	
	private Student student;
	
	DataBase dataBase = new DataBase();
	
	
	public String judge(String method) {
		if (student.getStudentId() == null ||
				"".equals(student.getStudentId())) {
			this.addFieldError("studentId", "学号为空");
		} else if (!dataBase.isExist(student.getStudentId().toString()).
				getStudentId().equals(student.getStudentId())) {
			this.addFieldError("studentId", "数据库里无此人");
		} else { 
			return method;
		}
		
		return method + "Input";
	}
	
	public String search() {
		String judge = judge("search");
		
		if ("search".equals(judge)) {
			student = dataBase.searchInfo(student.getStudentId().toString());
		} 
		
		return judge;
	}
			
	public String update() {
		String judge = judge("update");
		
		if ("update".equals(judge)) {
			dataBase.updateInfo(student.getStudentId().toString(), 
					student.getScore().getChinese(), student.getScore().getEnglish(),
					student.getScore().getMath());
			student = dataBase.searchInfo(student.getStudentId().toString());
			
		} 
		
		return judge;
		
	}
	
	public String delete() {
		String judge = judge("delete");
		
		if ("delete".equals(judge)) {
			student = dataBase.searchInfo(student.getStudentId().toString());
			dataBase.deleteInfo(student.getStudentId().toString());
		} 
		
		return judge;
	}
	
	public String insert() {
		if (student.getStudentId() == null ||
				"".equals(student.getStudentId())) {
			this.addFieldError("studentId", "学号为空");
		} else {
			dataBase.insertInfo(student.getStudentId().toString(),student.getName(), 
					student.getScore().getChinese(), student.getScore().getEnglish(),
					student.getScore().getMath());
			student = dataBase.searchInfo(student.getStudentId().toString());
			
			return "insert";
		}
		
		return "insertInput";
	}

	public Student getStudent() {
		return student;
	}


	public void setStudent(Student student) {
		this.student = student;
	}
	
}
