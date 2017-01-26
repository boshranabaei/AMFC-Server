package Server;

import java.sql.*;

public class MySQLBridge {

	Connection conn;
	java.sql.Statement stmt;
	ResultSet rs;
	public static MySQLBridge msql = new MySQLBridge();
	static final String DB_URL = "jdbc:sqlite:C:/5-Java/JettyServer/AMFC-Server/db/amfc.db";
	//TODO relative address

	
	// Establishing connection to the database
	public MySQLBridge() {
		try {
			conn = DriverManager.getConnection(DB_URL);
			stmt = conn.createStatement();
		} catch (SQLException e) {
			System.out.println("NO Connection");
		}
		//TODO Handle errors if the database doesn't exist
	}

	//Check the username and password
	public boolean authenticate(String username, String password) {
		boolean isValid = false;
		String sql = "SELECT COUNT(*) FROM admins WHERE username = \'" + username + "\' AND password = \""
				+ password + "\";";
		try {
			rs = stmt.executeQuery(sql);
			rs.next();
			if (rs.getInt("COUNT(*)") > 0)
				isValid = true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isValid;
	}

	//To change the password
	public boolean changePassword(String username, String oldPassword, String newPassword) {
		String sql = "UPDATE admins SET password = \'"	+ newPassword + 
				"\' WHERE username = \'" + username +"\' AND password = \'"+oldPassword+"\';";
		try {
			int rowChanged = stmt.executeUpdate(sql);
			if(rowChanged>0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}


//try {
//System.out.println("Creating the database...");
//conn = DriverManager.getConnection(DB_URL, USER, PASS);
//String sql = "CREATE DATABASE AMFC";
//stmt.executeUpdate(sql);
//sql = "CREATE TABLE admins (username VARCHAR(255), password VARCHAR(255), firstName VARCHAR(255), lastName VARCHAR(255), email VARCHAR(255), phoneNumber VARCHAR(255))";
/*	CREATE TABLE applicants (	
userId LONG,
firstName VARCHAR(255), 
lastName VARCHAR(255),
gender VARCHAR(1),
dateOfBirth DATE,
hasORwantsHijab VARCHAR(100),
city VARCHAR(100),
province VARCHAR(100),
country VARCHAR(100),
ethnicity VARCHAR(100),
citizenship VARCHAR(100),
maritalStatus VARCHAR(100),
children INTEGER,
occupation VARCHAR(255),
occupationComments VARCHAR(800),
education VARCHAR(100),
smoke Boolean,
relocate Boolean,
prefMaritalStatus VARCHAR(100),
prefEducation VARCHAR(100),
prefCitizenship VARCHAR(100),
prefCountry VARCHAR(100),
prefEthnicity VARCHAR(100),
prefAgeMin INTEGER,
prefAgeMax INTEGER,
homePhoneNumber  VARCHAR(100),
mobilePhoneNumber  VARCHAR(100),
email VARCHAR(255),
pointOfContact VARCHAR(100),
comments VARCHAR(1000),
approvalStatus VARCHAR(10),
status VARCHAR(10));
*/	

//stmt.executeUpdate(sql);
//
//} catch (SQLException e1) {
//e1.printStackTrace();
//}
//try {
//stmt = conn.createStatement();
//} catch (SQLException e) {
//e.printStackTrace();
//}
