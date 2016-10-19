package configuration;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import utils.Report;

public class TestParameters {
	
	private static String projectID;
	private static String testID;
	private static Connection connection;

	final public void setProjectID(String projectID) {
		TestParameters.projectID = projectID;
	}
	
	final public void setTestId(String testID) {
		TestParameters.testID = testID;
	}
	
	final public void setConnection(Connection connection) {
		TestParameters.connection = connection;
	}
	
	final public JSONObject getTestParameters() {
        Statement st = null;
        ResultSet rs = null;
        
        try {
            
        	st = (Statement) connection.createStatement();
        	rs = st.executeQuery("select * from selenium_test_parameters where project = '"+projectID+"' and test = '" +testID+ "'");

        	if(rs.next()) {

        		String settingsEncoded = rs.getString("settings");
               
        		//System.out.println(settingsEncoded);

        		if (settingsEncoded.length()>0) {

        			try {
        				JSONParser parser = new JSONParser();
        				return (JSONObject) parser.parse(settingsEncoded);
                    } catch (Exception ex) {
                    	Report.LogLine("initialization","ParseException",ex.getMessage(),"",Report.LogLevel.FATAL);
                    }
        		 }
        	}

        	
        } catch (SQLException ex) {

        	Report.LogLine("initialization","SQLException",ex.getMessage(),"",Report.LogLevel.FATAL);        

        } finally {
         
            try {
                
                if (rs!=null) rs.close();
                if (st!=null) st.close();

            } catch (SQLException ex) {
                
            	Report.LogLine("initialization","SQLException",ex.getMessage(),"",Report.LogLevel.TRIVIAL);
            }
        }

		return null;
	}
	

}
