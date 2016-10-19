package configuration;

public class database {
	
	public final static String getUsername() {
		return "selenium_user";
	}

	public final static String getPassword() {
		return "7Adq5Kw3GnCCQB3s0CKd";
	}

	public final static String getDatabase() {
		return "selenium";
	}

	public final static String getUrl() {
		return "jdbc:mysql://localhost:3306/" + getDatabase();
	}

}
