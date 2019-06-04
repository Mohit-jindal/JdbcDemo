package org.koushik.javabrains;

import org.koushik.javabrains.dao.JdbcDaoImpl;
import org.koushik.javabrains.model.Circle;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JdbcDemo {

	public static void main(String[] args) {
		
		//Circle circle = new JdbcDaoImpl().getCircle(1);
		ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
		JdbcDaoImpl dao = context.getBean("jdbcDaoImpl",JdbcDaoImpl.class);
		//Circle circle = dao.getCircle(1);
		//System.out.println(circle.getName());
		//System.out.println(dao.getCircleCount());
//		System.out.println(dao.getCircleName(1));
		//System.out.println(dao.getCircleForId(1).getName());
		dao.insertCircle(new Circle(4, "Four Circle"));
		System.out.println(dao.getAllCircles().size());
		//dao.createTriangleTable();
	}

}
