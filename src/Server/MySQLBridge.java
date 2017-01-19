package Server;

import java.sql.*;

public class MySQLBridge {

	Connection conn;
	java.sql.Statement stmt;
	ResultSet rs;
	public static MySQLBridge msql = new MySQLBridge();
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/";
	static final String USER = "root";
	static final String PASS = "rouhiFedak14";

	// Establishing connection to the database
	public MySQLBridge() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL + "AMFC", USER, PASS);
		} catch (ClassNotFoundException | SQLException e) {
			// if the database doesn't exist
			try {
				System.out.println("Creating the database...");
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				String sql = "CREATE DATABASE AMFC";
				stmt.executeUpdate(sql);
				sql = "CREATE TABLE AMFC.admins (username VARCHAR(255), password VARCHAR(255), firstName VARCHAR(255), lastName VARCHAR(255), email VARCHAR(255), phoneNumber VARCHAR(255))";
				stmt.executeUpdate(sql);

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean authenticate(String username, String password) {
		boolean isValid = false;
		String sql = "SELECT COUNT(*) FROM AMFC.admins WHERE username = \'" + username + "\' AND password = \""
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

	public boolean changePassword(String username, String oldPassword, String newPassword) {
		String sql = "UPDATE AMFC.admins SET password = \'"	+ newPassword + 
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
