package com.http.testtransaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class TestTransaction {

	public static void main(String[] args) throws Exception {

		Class.forName("com.mysql.jdbc.Driver"); 
		Connection conn = DriverManager.getConnection
		("jdbc:mysql://localhost:3306/hhuser", "root", "123456");
		conn.setAutoCommit(false);    

		try{
			Statement stmt = conn.createStatement();   
			int insertResult = stmt.executeUpdate
			("insert into hhuser set userid=125,nick = 'chenkangxian'");    
			int updateResult = stmt.executeUpdate
			("update hhuser set nick='chenkangxian@abc.com' where userid = 125");    
			if(insertResult > 0 && updateResult > 0){
				conn.commit();
			}else{
				conn.rollback();
			}
		}catch(Exception e){
			conn.rollback();
		}
	}

}
