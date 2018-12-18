package id.co.kynga.app.model;

import java.util.ArrayList;

public class User {
	private static User instance;
	private String phone = "",
			name = "",
			userId = "",
			email = "",
			password = "",
			session = "",
			gender = "",
			birthdate = "",
			address = "", balance = "";
	private ArrayList<PackageModel> packages;

	public void setUserId(String id) {
		userId = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setName(String n) {
		this.name = n;
	}

	public String getName() {
		return name;
	}

	public void setPhoneNumber(String number) {
		this.phone = number;
	}

	public String getPhoneNumber() {
		return phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setPassword(String pass) {
		this.password = pass;
	}

	public String getPassword() {
		return this.password;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getSession() {
		return this.session;
	}

	public void setGender(String g) {
		this.gender = g;
	}

	public String getGender() {
		return gender;
	}

	public void setBirthdate(String b) {
		this.birthdate = b;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setAddress(String a) {
		this.address = a;
	}

	public String getAddress() {
		return address;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getBalance() {
		return this.balance;
	}

	public void setPackages(ArrayList<PackageModel> pack) {
		this.packages = pack;
	}

	public ArrayList<PackageModel> getPackage() {
		return packages;
	}

	public void clearData() {
		phone = "";
		name = "";
		email = "";
		password = "";
		session = "";
		gender = "";
		birthdate = "";
		address = "";
		balance = "";
		packages = null;
	}

	public static synchronized User getInstance() {
		if (instance == null) {
			instance = new User();
		}
		return instance;
	}
}