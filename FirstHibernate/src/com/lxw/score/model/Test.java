package com.lxw.score.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.lxw.score.model.Student;

public class Test {

	private static SessionFactory sf;

	/**
	 * 插入功能。
	 * 
	 * @param student
	 */
	public void insert(Student student) {
		Session session = sf.openSession();
		try {
			session.beginTransaction();
			session.save(student);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	/**
	 * 删除功能。
	 * 
	 * @param student
	 */
	public void delete(Student student) {
		Session session = sf.openSession();
		try {
			session.beginTransaction();
			session.delete(student);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	/**
	 * 更新功能。
	 * 
	 * @param student
	 */
	public void update(Student student) {
		Session session = sf.openSession();
		try {
			session.beginTransaction();
			session.delete(student);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	public static void main(String[] args) {
		Test test = new Test();

		sf = new AnnotationConfiguration().configure().buildSessionFactory();

		Student student = new Student();
		student.setStudentId(20134019);
		student.setName("刘鑫伟");

		test.insert(student);

		sf.close();
	}
}
