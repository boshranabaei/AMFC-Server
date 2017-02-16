package Server;

public class Applicant {
	long userId;
	int age;
	String lastName;
	String firstName; 
	String dateOfBirth;
	int gender;
	String ethnicity;
	String citizenship;
	String maritalStatus;
	int children;
	int smoke;
	String hasORwantsHijab;
	int relocate;
	String relocateWhere;
	String education;
	String occupation;
	String comments;
	String email;
	String mobilePhoneNumber;
	String homePhoneNumber;
	String pointOfContact;
	String city;
	String province;
	String country;
	String prefMaritalStatus;
	int prefAgeMin;
	int prefAgeMax;
	String prefEthnicity;
	String prefEducation;
	String prefComments;
	String prefCountry;
	String amfcPointOfContact;
	String approvalStatus;
	String status;
	String dateAdded;
}

/*	
CREATE TABLE applicants (	
userId LONG,
firstName VARCHAR(255), 
lastName VARCHAR(255),
dateOfBirth DATE,
gender VARCHAR(1),
ethnicity VARCHAR(100),
citizenship VARCHAR(100),
maritalStatus VARCHAR(100),
children INTEGER,
smoke INTEGER,
hasORwantsHijab VARCHAR(100),
relocate INTEGER,
relocateWhere VARCHAR(100),
education VARCHAR(100),
occupation VARCHAR(255),
comments VARCHAR(800),
email VARCHAR(255),
mobilePhoneNumber  VARCHAR(100),
homePhoneNumber  VARCHAR(100),
pointOfContact VARCHAR(100),
city VARCHAR(100),
province VARCHAR(100),
country VARCHAR(100),
prefMaritalStatus VARCHAR(100),
prefAgeMin INTEGER,
prefAgeMax INTEGER,
prefEthnicity VARCHAR(100),
prefEducation VARCHAR(100),
prefCountry VARCHAR(100),
prefComments VARCHAR(1000),
amfcPointOfContact VARCHAR(100),
approvalStatus VARCHAR(10),
status VARCHAR(10),
dateAdded VARCHAR(10));
*/