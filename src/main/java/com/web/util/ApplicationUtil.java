package com.web.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ApplicationUtil {

	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://localhost:3306/probmng3";

	   //  Database credentials
	   static final String USER = "root";
	   static final String PASS = "";
	   
	public static int getConnection(String email){

		Connection conn = null;
		Statement stmt = null;
		Long id=0l;
	   try{
		      //STEP 2: Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");

		      //STEP 3: Open a connection
		      System.out.println("Connecting to a selected database...");
		      conn = DriverManager.getConnection(DB_URL, USER, PASS);
		      System.out.println("Connected database successfully...");
		      
		      //STEP 4: Execute a query
		      System.out.println("Creating statement...");
		      stmt = conn.createStatement();

		      String sql = "SELECT user_id from pm_users where email_id='"+email+"'";
		      ResultSet rs = stmt.executeQuery(sql);
		      //STEP 5: Extract data from result set
		      while(rs.next()){
		         id  = rs.getLong("user_id");
		         if(id==0){
		        	 
		         }else{
		        	 System.out.println(id);
		         }
		      }
		      rs.close();
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            conn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
	return id.intValue();
		}//end main
}
