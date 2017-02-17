package Server;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MySQLBridge {

	static final String WIN_URL = "jdbc:sqlite:C:/5-Java/JettyServer/AMFC-Server/db/amfc.db";
	static final String LINUX_URL = "jdbc:sqlite:/home/bnabaei/AMFC-Server/db/amfc.db";

	static int userID = 1;
	Connection conn;
	java.sql.Statement stmt;
	ResultSet rs;
	public static MySQLBridge msql = new MySQLBridge();
	// TODO relative address

	// Establishing connection to the database
	public MySQLBridge() {
		try {
			conn = DriverManager.getConnection(WIN_URL);
			stmt = conn.createStatement();
			setUserId();
			System.out.println(userID+"****");
		} catch (SQLException e) {
			System.out.println("NO Connection");
		}
		// TODO Handle errors if the database doesn't exist
	}

	// Check the username and password
	public synchronized boolean authenticate(String username, String password) {
		boolean isValid = false;
		String sql = "SELECT COUNT(*) FROM admins WHERE username = \'" + username + "\' AND password = \'" + password
				+ "\';";
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

	// To change the password
	public boolean changePassword(String username, String oldPassword, String newPassword) {
		String sql = "UPDATE admins SET password = \'" + newPassword + "\' WHERE username = \'" + username
				+ "\' AND password = \'" + oldPassword + "\';";
		try {
			int rowChanged = stmt.executeUpdate(sql);
			if (rowChanged > 0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public synchronized boolean addApplicant(Applicant applicant) {

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calToday = Calendar.getInstance();

		applicant.age = calculateAge(applicant.birthYear);
		if(applicant.prefAgeMax!=0)
			applicant.prefAgeMax = Math.abs(applicant.age - applicant.prefAgeMax);
		if(applicant.prefAgeMin!=0)
		applicant.prefAgeMin = Math.abs(applicant.age - applicant.prefAgeMin);

		userID++;
		
		String sql = "INSERT INTO applicants VALUES(" + userID + ",\'" + applicant.firstName + "\',\'"
				+ applicant.lastName + "\',\'" + applicant.birthYear + "\'," + applicant.gender + ",\'"
				+ applicant.ethnicity + "\',\'" + applicant.citizenship + "\',\'" + applicant.maritalStatus + "\',"
				+ applicant.children + "," + applicant.smoke + ",\'" + applicant.hasORwantsHijab + "\',"
				+ applicant.relocate + ",\'" + applicant.relocateWhere + "\',\'" + applicant.education + "\',\'"
				+ applicant.occupation + "\',\'" + applicant.comments + "\',\'" + applicant.email + "\',\'"
				+ applicant.mobilePhoneNumber + "\',\'" + applicant.homePhoneNumber + "\',\'" + applicant.pointOfContact
				+ "\',\'" + applicant.city + "\',\'" + applicant.province + "\',\'" + applicant.country + "\',\'"
				+ applicant.prefMaritalStatus + "\'," + applicant.prefAgeMin + "," + applicant.prefAgeMax + ",\'"
				+ applicant.prefEthnicity + "\',\'" + applicant.prefEducation + "\',\'" + applicant.prefCountry + "\',\'"
				+ applicant.prefComments + "\',\'" + applicant.amfcPointOfContact + "\',\'approved\',\'free\',\'"
				+ dateFormatter.format(calToday.getTime()) + "\')";

		System.out.println(sql);

		try {
			int rowChanged = stmt.executeUpdate(sql);
			if (rowChanged > 0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public synchronized Applicant[] getApplicants() {

		Applicant[] applicants = null;

		try {
			String sql = "SELECT COUNT(*) FROM applicants;";
			rs = stmt.executeQuery(sql);
			applicants = new Applicant[rs.getInt("COUNT(*)")];

			sql = "SELECT * FROM applicants;";
			rs = stmt.executeQuery(sql);
			rs.next();
			for (int i = 0; i < applicants.length; i++) {
				applicants[i] = new Applicant();
				applicants[i].firstName = rs.getString("firstName");
				applicants[i].lastName = rs.getString("lastName");
				applicants[i].birthYear = rs.getString("birthYear");
				applicants[i].age = calculateAge(applicants[i].birthYear);
				applicants[i].status = rs.getString("status");
				applicants[i].dateAdded = rs.getString("dateAdded");
				applicants[i].children = rs.getInt("children");
				applicants[i].citizenship = rs.getString("citizenship");
				applicants[i].city = rs.getString("city");
				applicants[i].prefComments = rs.getString("prefComments");
				applicants[i].country = rs.getString("country");
				applicants[i].education = rs.getString("education");
				applicants[i].email = rs.getString("email");
				applicants[i].ethnicity = rs.getString("ethnicity");
				applicants[i].gender = rs.getInt("gender");
				rs.next();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return applicants;
	}

	public synchronized void setUserId() {

		try {
			String sql = "SELECT max(userId) FROM applicants;";
			rs = stmt.executeQuery(sql);
			rs.next();
			userID = rs.getInt("max(userId)");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	int calculateAge(String dateOfBirth) {
		int age = 0;
		DateFormat dateFormatter = new SimpleDateFormat("yyyy");
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

// try {
// System.out.println("Creating the database...");
// conn = DriverManager.getConnection(DB_URL, USER, PASS);
// String sql = "CREATE DATABASE AMFC";
// stmt.executeUpdate(sql);
// sql = "CREATE TABLE admins (username VARCHAR(255), password VARCHAR(255),
// firstName VARCHAR(255), lastName VARCHAR(255), email VARCHAR(255),
// phoneNumber VARCHAR(255))";

// stmt.executeUpdate(sql);
//
// } catch (SQLException e1) {
// e1.printStackTrace();
// }
// try {
// stmt = conn.createStatement();
// } catch (SQLException e) {
// e.printStackTrace();
// }

/*
 * SQLite commands: .tables => show all tables drop table table_name => delete
 * table
 */