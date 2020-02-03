package Chat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Baza {		
		   
		static String url="jdbc:mysql://localhost:3306/lab7?autoReconnect=true&useSSL=false";
		static String user="root";
		static String pass="mysql";
	
	public static Connection connect() throws SQLException {
	return DriverManager.getConnection(url, user, pass);
	}	
	
	public static void insertMessage(String korisnik, String textField) {
	
	 String SQL = "INSERT INTO chatporuke(userName,poruka) VALUES(?,?)";
			
	    try {	    		
	            
	    	PreparedStatement pstmt = connect().prepareStatement(SQL);	    	
	        pstmt.setString(1, korisnik);
	        pstmt.setString(2, textField);
	        pstmt.executeUpdate();	        
	        
	    } catch (SQLException ex) {
	        System.out.println(ex.getMessage());
	    }
	   			
	}
	
	public static String showPoruke() {
		Statement stmt=null;
	
	String query = "SELECT userName,poruka FROM chatporuke";
	String string="";
	try {		
		stmt = connect().createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		while (rs.next()) {
			string+=rs.getString(1)+": "+rs.getString(2)+"\n";	
	    }
	
	} catch (SQLException ex) {
	
	ex.printStackTrace();
	
	}
	
	return string;					
	}	
}
