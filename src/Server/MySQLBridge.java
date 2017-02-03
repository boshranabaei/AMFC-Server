package Server;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MySQLBridge {

	static int lastID=1;
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
		String sql = "SELECT COUNT(*) FROM admins WHERE username = \'" + username + "\' AND password = \'"
				+ password + "\';";
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

	public synchronized boolean addApplicant(Applicant applicant){
		
		DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy");
		Calendar calToday = Calendar.getInstance();
		
		applicant.age = calculateAge( applicant.dateOfBirth ); 
		applicant.prefAgeMax = Math.abs(applicant.age - applicant.prefAgeMax);
		applicant.prefAgeMin = Math.abs(applicant.age - applicant.prefAgeMin);
		
		lastID ++;
		
		String sql = "INSERT INTO applicants VALUES("+lastID+",\'"+applicant.firstName+"\',\'"+applicant.lastName+"\',\'"+
		applicant.dateOfBirth+"\',"+applicant.gender+",\'"+applicant.maritalStatus+"\',"+applicant.children
		+",\'"+applicant.ethnicity+"\',\'"+applicant.citizenship+"\',"+applicant.relocate+",\'"+
		applicant.hasORwantsHijab+"\',"+applicant.smoke+",\'"+applicant.education+"\',\'"+applicant.occupation+"\',\'"+
		applicant.occupationComments+"\',\'"+applicant.email+"\',\'"+applicant.mobilePhoneNumber+"\',\'"+
		applicant.homePhoneNumber+"\',\'"+applicant.city+"\',\'"+applicant.country+"\',\'"+applicant.province
		+"\',\'"+applicant.country+"\',\'"+applicant.prefMaritalStatus+"\',\'"+applicant.ethnicity+"\',\'"+
		applicant.prefEducation+"\',"+applicant.prefAgeMin+","+applicant.prefAgeMax+",\'"+applicant.pointOfContact
		+"\',\'"+applicant.comments+"\',\'approved\',\'free\',\'"+dateFormatter.format(calToday.getTime())+"\')";
		
		System.out.println(sql);
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
	
	public Applicant [] getApplicants(){
		
		Applicant[] applicants = null;
		
		try {
			String sql = "SELECT COUNT(*) FROM applicants;";
			rs = stmt.executeQuery(sql);
			applicants = new Applicant [rs.getInt("COUNT(*)")];
			
			sql = "SELECT * FROM applicants;";
			rs = stmt.executeQuery(sql);
			rs.next();
			for (int i=0;i<applicants.length;i++){
				applicants[i] = new Applicant();
				applicants[i].firstName = rs.getString("firstName");
				applicants[i].lastName = rs.getString("lastName");
				applicants[i].dateOfBirth = rs.getString("dateOfBirth");
				applicants[i].age = calculateAge(applicants[i].dateOfBirth);
				System.out.println(applicants[i].age);
				rs.next();
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return applicants;
	}
	
	int calculateAge(String dateOfBirth){
		int age=0;
		DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy");
		Calendar calToday = Calendar.getInstance();
	    Calendar calBirth = Calendar.getInstance();
		Date birthdate = null;
		try {
			birthdate = dateFormatter.parse(dateOfBirth);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		calBirth.setTime(birthdate);
		age = calToday.get(Calendar.YEAR) - (calBirth.get(Calendar.YEAR));
		return age;
	}
		
}


//try {
//System.out.println("Creating the database...");
//conn = DriverManager.getConnection(DB_URL, USER, PASS);
//String sql = "CREATE DATABASE AMFC";
//stmt.executeUpdate(sql);
//sql = "CREATE TABLE admins (username VARCHAR(255), password VARCHAR(255), firstName VARCHAR(255), lastName VARCHAR(255), email VARCHAR(255), phoneNumber VARCHAR(255))";
/*	
CREATE TABLE applicants (	
userId LONG,
firstName VARCHAR(255), 
lastName VARCHAR(255),
dateOfBirth DATE,
gender VARCHAR(1),
maritalStatus VARCHAR(100),
children INTEGER,
ethnicity VARCHAR(100),
citizenship VARCHAR(100),
relocate INTEGER,
hasORwantsHijab VARCHAR(100),
smoke INTEGER,
education VARCHAR(100),
occupation VARCHAR(255),
occupationComments VARCHAR(800),
email VARCHAR(255),
mobilePhoneNumber  VARCHAR(100),
homePhoneNumber  VARCHAR(100),
city VARCHAR(100),
province VARCHAR(100),
country VARCHAR(100),
prefMaritalStatus VARCHAR(100),
prefEthnicity VARCHAR(100),
prefEducation VARCHAR(100),
prefCountry VARCHAR(100),
prefAgeMin INTEGER,
prefAgeMax INTEGER,
pointOfContact VARCHAR(100),
comments VARCHAR(1000),
approvalStatus VARCHAR(10),
status VARCHAR(10),
dateAdded VARCHAR(10));
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
