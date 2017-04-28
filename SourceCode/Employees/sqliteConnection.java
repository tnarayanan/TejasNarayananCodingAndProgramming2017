package Employees;
import java.io.File;
import java.net.URISyntaxException;
import java.sql.*;
import javax.swing.*;

import Main.LoginScreen;

/**
 * This class establishes a connection to the SQLite database for references to the data.
 * @author Tejas
 *
 */

public class sqliteConnection {
	
	Connection conn = null;
	public static Connection dbConnector() {
		try {
			Class.forName("org.sqlite.JDBC");
			String path = "";
			try {
				// This gets the path of the JAR file running, regardless of where the user runs from
				path = new File(LoginScreen.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath();
			} catch (URISyntaxException e2) {
				e2.printStackTrace();
			}
			
			//System.out.println(path);
			
			/*
			 * Gets a connection to the database located in the same directory as the JAR file. If the database does not
			 * exist, it will automatically be created.
			 */
			
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + path + "/FEC_Database.sqlite");
			System.out.println("Connection successful");
			return conn; // Returns the connection for other classes to use
		} 
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, e);
			return null;
		}
	}
	
}
