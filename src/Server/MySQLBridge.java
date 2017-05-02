package Server;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.simple.JSONArray;

public class MySQLBridge {

	static final String WIN_URL = "jdbc:sqlite:C:/5-Java/JettyServer/AMFC-Server/db/amfc.db";
	static final String LINUX_URL = "jdbc:sqlite:/root/AMFC-Server/db/amfc.db";

	static int userID = 1;
	Connection conn;
	java.sql.Statement stmt;
	ResultSet rs;
	public static MySQLBridge msql = new MySQLBridge();

	// Establishing connection to the database
	public MySQLBridge() {
		try {
			conn = DriverManager.getConnection(LINUX_URL);
			stmt = conn.createStatement();
			setUserId();
		} catch (SQLException e) {
			System.out.println("NO Connection");
		}
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

	// Update profile info
	public synchronized boolean updateProfile(String username, String firstName, String lastName, String email,
			String phoneNumber) {
		String sql = "UPDATE admins SET firstName = \'" + firstName + "\', lastName = \'" + lastName + "\', email=\'"
				+ email + "\', phoneNumber=\'" + phoneNumber + "\'" + " WHERE username = \'" + username + "\';";
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

	public synchronized Admin[] getAdmins() {

		Admin[] admins = null;

		try {
			String sql = "SELECT COUNT(*) FROM admins;";
			rs = stmt.executeQuery(sql);
			admins = new Admin[rs.getInt("COUNT(*)")];

			sql = "SELECT * FROM Admins;";
			rs = stmt.executeQuery(sql);
			rs.next();
			for (int i = 0; i < admins.length; i++) {
				admins[i] = new Admin();
				admins[i].username = rs.getString("username");
				admins[i].firstName = rs.getString("firstName");
				admins[i].lastName = rs.getString("lastName");
				admins[i].email = rs.getString("email");
				admins[i].phoneNumber = rs.getString("phoneNumber");
				rs.next();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return admins;
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
		if (applicant.prefAgeMax != 0)
			applicant.prefAgeMax = applicant.age - applicant.prefAgeMax;
		if (applicant.prefAgeMin != 0)
			applicant.prefAgeMin = applicant.age - applicant.prefAgeMin;

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
				+ applicant.prefEthnicity + "\',\'" + applicant.prefEducation + "\',\'" + applicant.prefCountry
				+ "\',\'" + applicant.prefComments + "\',\'" + applicant.amfcPointOfContact + "\',\'"
				+ applicant.approvalStatus + "\',\'free\',\'" + dateFormatter.format(calToday.getTime()) + "\',0,"
				+ applicant.approximateAge + ", \"" + applicant.photo + "\")";

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

	public synchronized Applicant[] getApplicants(String approvalStatus) {

		Applicant[] applicants = null;

		try {
			String sql = "SELECT COUNT(*) FROM applicants WHERE approvalStatus==\'" + approvalStatus + "\';";
			rs = stmt.executeQuery(sql);
			applicants = new Applicant[rs.getInt("COUNT(*)")];

			sql = "SELECT * FROM applicants WHERE approvalStatus==\'" + approvalStatus + "\';";
			rs = stmt.executeQuery(sql);
			rs.next();
			applicants = fillUpFromQuery(applicants, rs);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return applicants;
	}

	public synchronized Applicant[] getApplicantById(int userId) {

		Applicant[] applicants = null;

		try {
			String sql = "SELECT * FROM applicants where userId==" + userId + ";";
			rs = stmt.executeQuery(sql);
			rs.next();
			applicants = new Applicant[1];

			applicants = fillUpFromQuery(applicants, rs);

		} catch (

		SQLException e) {
			e.printStackTrace();
		}

		return applicants;
	}

	public synchronized boolean updateApplicant(Applicant applicant) {

		applicant.age = calculateAge(applicant.birthYear);
		if (applicant.prefAgeMax != 0)
			applicant.prefAgeMax = applicant.age - applicant.prefAgeMax;
		if (applicant.prefAgeMin != 0)
			applicant.prefAgeMin = applicant.age - applicant.prefAgeMin;

		String sql = "DELETE FROM applicants WHERE userId==" + applicant.userId + ";";
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sql = "INSERT INTO applicants VALUES(" + applicant.userId + ",\'" + applicant.firstName + "\',\'"
				+ applicant.lastName + "\',\'" + applicant.birthYear + "\'," + applicant.gender + ",\'"
				+ applicant.ethnicity + "\',\'" + applicant.citizenship + "\',\'" + applicant.maritalStatus + "\',"
				+ applicant.children + "," + applicant.smoke + ",\'" + applicant.hasORwantsHijab + "\',"
				+ applicant.relocate + ",\'" + applicant.relocateWhere + "\',\'" + applicant.education + "\',\'"
				+ applicant.occupation + "\',\'" + applicant.comments + "\',\'" + applicant.email + "\',\'"
				+ applicant.mobilePhoneNumber + "\',\'" + applicant.homePhoneNumber + "\',\'" + applicant.pointOfContact
				+ "\',\'" + applicant.city + "\',\'" + applicant.province + "\',\'" + applicant.country + "\',\'"
				+ applicant.prefMaritalStatus + "\'," + applicant.prefAgeMin + "," + applicant.prefAgeMax + ",\'"
				+ applicant.prefEthnicity + "\',\'" + applicant.prefEducation + "\',\'" + applicant.prefCountry
				+ "\',\'" + applicant.prefComments + "\',\'" + applicant.amfcPointOfContact + "\',\'approved\',\'"
				+ applicant.status + "\',\'" + applicant.dateAdded + "\',0," + applicant.approximateAge + ",\""
				+ applicant.photo + "\")";
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

	public synchronized Pairing[] getPairingsById(long userId, int gender) {
		Pairing[] pairings = null;
		try {

			String sql = "SELECT COUNT(*) FROM pairings where ";
			if (gender == 0)
				sql += "MUserId==" + userId + ";";
			else
				sql += "FUserId==" + userId + ";";
			rs = stmt.executeQuery(sql);
			pairings = new Pairing[rs.getInt("COUNT(*)")];

			sql = "SELECT * FROM pairings where ";
			if (gender == 0)
				sql += "MUserId==" + userId + ";";
			else
				sql += "FUserId==" + userId + ";";
			rs = stmt.executeQuery(sql);
			rs.next();
			for (int i = 0; i < pairings.length; i++) {
				pairings[i] = new Pairing();
				pairings[i].MUserId = rs.getInt("MUserId");
				pairings[i].FUserId = rs.getInt("FUserId");
				pairings[i].director = rs.getString("director");
				pairings[i].pairingStatus = rs.getString("pairingStatus");
				pairings[i].pairingDate = rs.getString("pairingDate");
				pairings[i].note = rs.getString("note");
				rs.next();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return pairings;
	}

	public synchronized Applicant[] getCandidates(int gender, int userId) {
		Applicant[] candidates = null;
		try {

			String sql = "SELECT ( SELECT COUNT(*) from archivedApplicants where userId in ";
			if (gender == 1)
				sql += "(SELECT MUserId FROM pairings WHERE FUserId==" + userId + "))";
			else
				sql += "(SELECT FUserId FROM pairings WHERE MUserId==" + userId + "))";

			sql += "+ (SELECT COUNT(*) FROM applicants WHERE gender !=" + gender + ");";

			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			rs.next();
			candidates = new Applicant[rs.getInt(rsmd.getColumnName(1))];

			sql = "SELECT * from archivedApplicants where userId in ";
			if (gender == 1)
				sql += "(SELECT MUserId FROM pairings WHERE FUserId==" + userId + ")";
			else
				sql += "(SELECT FUserId FROM pairings WHERE MUserId==" + userId + ")";

			sql += " UNION SELECT * FROM applicants WHERE gender !=" + gender + ";";

			rs = stmt.executeQuery(sql);
			rs.next();
			candidates = fillUpFromQuery(candidates, rs);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return candidates;
	}

	public synchronized boolean addPairing(int MUserId, int FUserId, String director) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calToday = Calendar.getInstance();

		try {
			String sql = "INSERT INTO pairings VALUES(" + MUserId + "," + FUserId + ",\"" + director
					+ "\",\"on going\",\"" + dateFormatter.format(calToday.getTime()) + "\",\"Add comments here\");";
			int rowChanged = stmt.executeUpdate(sql);
			sql = "Update applicants SET status=\"busy\" WHERE userId==" + MUserId + " OR userId==" + FUserId + ";";
			stmt.executeUpdate(sql);

			if (rowChanged > 0)
				return true;
			else
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public synchronized boolean removePairing(int MUserId, int FUserId) {
		try {
			String sql = "DELETE FROM pairings WHERE MUserId==" + MUserId + " and FUserId==" + FUserId + ";";
			int rowChanged = stmt.executeUpdate(sql);
			sql = "SELECT COUNT(*) FROM pairings WHERE MUserId==" + MUserId + " AND pairingStatus==\"on going\";";
			rs = stmt.executeQuery(sql);
			if (rs.getInt("COUNT(*)") == 0) {
				sql = "Update applicants SET status=\"free\" WHERE userId==" + MUserId + ";";
				stmt.executeUpdate(sql);
			}

			sql = "SELECT COUNT(*) FROM pairings WHERE FUserId==" + FUserId + " AND pairingStatus==\"on going\";";
			rs = stmt.executeQuery(sql);
			if (rs.getInt("COUNT(*)") == 0) {
				sql = "Update applicants SET status=\"free\" WHERE userId==" + FUserId + ";";
				stmt.executeUpdate(sql);
			}

			stmt.executeUpdate(sql);

			if (rowChanged > 0)
				return true;
			else
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public synchronized boolean updatePairingStatus(int MUserId, int FUserId, String pairingStatus) {
		try {
			String sql = "UPDATE pairings SET pairingStatus=\"" + pairingStatus + "\" WHERE " + "MUserId==" + MUserId
					+ " AND FUserId==" + FUserId + ";";
			int rowChanged = stmt.executeUpdate(sql);
			if (!pairingStatus.equals("on going")) {
				sql = "SELECT COUNT(*) FROM pairings WHERE MUserId==" + MUserId + " AND pairingStatus==\"on going\";";
				rs = stmt.executeQuery(sql);
				if (rs.getInt("COUNT(*)") == 0) {
					sql = "Update applicants SET status=\"free\" WHERE userId==" + MUserId + ";";
					stmt.executeUpdate(sql);
				}

				sql = "SELECT COUNT(*) FROM pairings WHERE FUserId==" + FUserId + " AND pairingStatus==\"on going\";";
				rs = stmt.executeQuery(sql);
				if (rs.getInt("COUNT(*)") == 0) {
					sql = "Update applicants SET status=\"free\" WHERE userId==" + FUserId + ";";
					stmt.executeUpdate(sql);
				}

			} else {
				sql = "Update applicants SET status=\"busy\" WHERE userId==" + MUserId + " OR userId==" + FUserId + ";";
				stmt.executeUpdate(sql);
			}

			if (rowChanged > 0)
				return true;
			else
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public synchronized boolean addNote(int MUserId, int FUserId, String note) {
		try {
			String sql = "UPDATE pairings SET note=\"" + note + "\" WHERE " + "MUserId==" + MUserId + " AND FUserId=="
					+ FUserId + ";";
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

	public synchronized boolean archiveApplicant(int userId) {
		try {
			String sql = "SELECT COUNT(*) FROM pairings WHERE (FUserId ==" + userId + " OR MUserId ==" + userId + ")"
					+ "AND pairingStatus==\"on going\";";
			rs = stmt.executeQuery(sql);

			if (rs.getInt("COUNT(*)") > 0)
				return false;

			sql = "INSERT INTO archivedApplicants SELECT * FROM applicants WHERE userId==" + userId + ";";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM applicants WHERE userId==" + userId + ";";
			stmt.executeUpdate(sql);
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public synchronized boolean acceptApplicant(JSONArray userIdsArray) {
		String sql = "UPDATE applicants SET approvalStatus=\'approved\' WHERE ";
		for (int i = 0; i < userIdsArray.size(); i++) {
			 sql+=  "userId==" + userIdsArray.get(i).toString();
			 if(i<userIdsArray.size()-1)
				 sql+=" OR ";
		}
		sql+= ";";
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
	
	public synchronized boolean rejectApplicant(JSONArray userIdsArray) {
		try {
			String sql = "INSERT INTO archivedApplicants SELECT * FROM applicants WHERE ";

			for (int i = 0; i < userIdsArray.size(); i++) {
				 sql+=  "userId==" + userIdsArray.get(i).toString();
				 if(i<userIdsArray.size()-1)
					 sql+=" OR ";
			}
			sql+= ";";
			
			stmt.executeUpdate(sql);
			sql = "DELETE FROM applicants WHERE ";
			
			for (int i = 0; i < userIdsArray.size(); i++) {
				 sql+=  "userId==" + userIdsArray.get(i).toString();
				 if(i<userIdsArray.size()-1)
					 sql+=" OR ";
			}
			sql+= ";";
			
			
			stmt.executeUpdate(sql);
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	
	public synchronized void setUserId() {
		try {
			String sql = "SELECT max(userId) FROM " + "(SELECT userId FROM applicants UNION ALL"
					+ " SELECT userId FROM archivedApplicants);";

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

	Applicant[] fillUpFromQuery(Applicant[] applicants, ResultSet rs) {
		for (int i = 0; i < applicants.length; i++) {
			applicants[i] = new Applicant();
			try {
				applicants[i].userId = rs.getInt("userId");
				applicants[i].firstName = rs.getString("firstName");
				applicants[i].lastName = rs.getString("lastName");
				applicants[i].birthYear = rs.getString("birthYear");
				applicants[i].gender = rs.getInt("gender");
				applicants[i].hasORwantsHijab = rs.getString("hasORwantsHijab");
				applicants[i].age = calculateAge(applicants[i].birthYear);
				applicants[i].smoke = rs.getInt("smoke");
				applicants[i].citizenship = rs.getString("citizenship");
				applicants[i].ethnicity = rs.getString("ethnicity");
				applicants[i].maritalStatus = rs.getString("maritalStatus");
				applicants[i].children = rs.getInt("children");
				applicants[i].relocate = rs.getInt("relocate");
				applicants[i].relocateWhere = rs.getString("relocateWhere");
				applicants[i].city = rs.getString("city");
				applicants[i].province = rs.getString("province");
				applicants[i].country = rs.getString("country");
				applicants[i].education = rs.getString("education");
				applicants[i].occupation = rs.getString("occupation");
				applicants[i].comments = rs.getString("comments");
				applicants[i].prefMaritalStatus = rs.getString("prefMaritalStatus");
				applicants[i].prefAgeMax = rs.getInt("prefAgeMax");
				applicants[i].prefAgeMin = rs.getInt("prefAgeMin");
				applicants[i].prefEthnicity = rs.getString("prefEthnicity");
				applicants[i].prefEducation = rs.getString("prefEducation");
				applicants[i].prefCountry = rs.getString("prefCountry");
				applicants[i].prefComments = rs.getString("prefComments");
				applicants[i].email = rs.getString("email");
				applicants[i].mobilePhoneNumber = rs.getString("mobilePhoneNumber");
				applicants[i].homePhoneNumber = rs.getString("homePhoneNumber");
				applicants[i].pointOfContact = rs.getString("pointOfContact");
				applicants[i].dateAdded = rs.getString("dateAdded");
				applicants[i].status = rs.getString("status");
				applicants[i].amfcPointOfContact = rs.getString("amfcPointOfContact");
				applicants[i].archived = rs.getInt("archived");
				applicants[i].approximateAge = rs.getInt("approximateAge");
				applicants[i].photo = rs.getString("photo");
				rs.next();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return applicants;
	}
}

// try {
// System.out.println("Creating the database...");
// conn = DriverManager.getConnection(DB_URL, USER, PASS);
// String sql = "CREATE DATABASE AMFC";
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
 * alter table x add column y integer; => add column
 */

/*
 * create table sessionUserInfo ( sessionId VARCHAR(200),username
 * VARCHAR(20),expireDate VARCHAR(10));
 */
