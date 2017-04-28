package Main;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Key;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Employees.sqliteConnection;

/**
 * This is the initial screen, where users can login to the program to gain access.
 * @author Tejas
 *
 */

public class LoginScreen {

	private JFrame frame;
	private JTextField nameField;
	private JPasswordField passwordField;
	private JButton btnLogIn;
	
	public static ImageIcon houseIcon;
	
	public static final Connection conn = sqliteConnection.dbConnector();
	

	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginScreen window = new LoginScreen();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	
	/**
	 * Create the application.
	 */
	
	public LoginScreen() {
		initialize();
	}

	
	
	/**
	 * Initialize the contents of the frame.
	 */
	
	private void initialize() {
		
		// Initial setup
		
		frame = new JFrame("House of Fun!");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{42, 34, 126, 0, 0};
		gridBagLayout.rowHeights = new int[]{31, 36, 0, 28, 28, 28, 0, 29, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, 0.0, 1.0, 1.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		
		
		// Create house image
		
		Image houseImg = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/house.png")); // Load Image
		Image resizedHouseImage = houseImg.getScaledInstance(75, 75, 0); // Resize to 75x75
		houseIcon = new ImageIcon(resizedHouseImage); // Set icon to image
		
		
		
		// Add elements to JFrame
		
		addElements();
		
		
		
		// When Login button clicked
		
		btnLogIn.addActionListener(new ActionListener() {
			
			/*
			 * This is where the username is checked and the password is encryption-checked.
			 * Without the encryption key for that password, the password cannot be retrieved.
			 */
			
			public void actionPerformed(ActionEvent e) {
				// Checks if username, password, and key are all eligible
				
				boolean nameOk = !(nameField.getText().toString().isEmpty());
				boolean passOk = !(passwordField.getPassword().toString().isEmpty());
				
				if (nameOk && passOk) {
					
					// Checks usernames
					
					try {
						
						// Looks through the Username file
						
						BufferedReader userFin = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("usernames")));
						
						String userLine;
						boolean foundUser = false;
						while((userLine = userFin.readLine()) != null) {
							if (userLine.equals(nameField.getText().toString())) { // If the current username in the file equals the one entered in the field
								foundUser = true;
								break;
							}
						}
						
						userFin.close(); // Close file to save memory
						
						
						
						
						if (foundUser == false) {
							new CustomDialog(frame, "Error", "Username is incorrect.").showError(); 
						} else {
							
							// check passcode through encryption
							
							String password = new String(passwordField.getPassword());
							String key = "abcdefgh12345678";
							String encryptedPassword = encrypt(password, key); // Gets the encrypted version of the entered password
							
							// Looks through the Encrypted Password File
							
							BufferedReader passFin = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("passwords")));
							String passLine;
							boolean foundPass = false;
							while((passLine = passFin.readLine()) != null) {
								if (passLine.equals(encryptedPassword)) { // If the current encrypted password in the file equals the encrypted version of the password entered in the field
									foundPass = true;
									break;
								}
							}
							
							passFin.close(); // Close file to save memory
							
							
							
							
							if (foundPass == false) {
								new CustomDialog(frame, "Error", "Password is incorrect.").showError();
							} else {
								// Username and password are correct --> Enter into system
								
								
								
								// If the Employees table does not exist, create it with the correct fields
								String query = "create table if not exists 'Employees' ('EID' INTEGER,'FirstName' TEXT,'MI' TEXT,'LastName' TEXT,'PhoneNumber' TEXT,'DOB' TEXT,'HourlyWage' DOUBLE,'StartDate' TEXT NOT NULL ,'EndDate' TEXT,'SSN' NUMERIC)";
								try {
									Statement stmt = conn.createStatement();
									stmt.execute(query);
									System.out.println("Created Employees if didn't exist");
								} catch (SQLException e1) {
									e1.printStackTrace();
								}
								
								
								// If the Attendance table does not exist, create it with the correct fields
								query = "create table if not exists 'Attendance' ('Date' TEXT, 'Time' INTEGER, 'Category' TEXT)";
								try {
									Statement stmt = conn.createStatement();
									stmt.execute(query);
									System.out.println("Created Attendance if didn't exist");
								} catch (SQLException e1) {
									e1.printStackTrace();
								}
								
								
								// If the Schedule table does not exist, create it with the correct fields
								query = "create table if not exists 'Schedule' ('EID' INTEGER, 'sDate' , 's900' , 's1000' , 's1100' , 's1200' , 's100' , 's200' , 's300' , 's400' , 's500' , 's600' )";
								try {
									Statement stmt = conn.createStatement();
									stmt.execute(query);
									System.out.println("Created Schedule if didn't exist");
								} catch (SQLException e1) {
									e1.printStackTrace();
								}
								
								
								
								// Loads main screen
								
								MainScreen ms = new MainScreen();
								ms.frame.setVisible(true);
								frame.setVisible(false);
							}
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					new CustomDialog(frame, "Error", "Username and Password must contain text").showError();
				}
			}
		});
		
		frame.getRootPane().setDefaultButton(btnLogIn); // Allows enter to submit
	}
	
	/**
	 * Encrypts a String into a seemingly random 16-digit character String
	 */
	private String encrypt(String text, String key) {
		
		// Uses AES Cypher
		
		Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, aesKey); // Initialize cipher
			byte[] encrypted = cipher.doFinal(text.getBytes()); // Encrypted bytes
			
			Base64.Encoder encoder = Base64.getEncoder(); // Encodes from byte array to String
			String encryptedString = encoder.encodeToString(encrypted); // Encodes array to String
			return encryptedString;
		} catch (Exception e2) {
			e2.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Adds individual elements to frame
	 */
	
	private void addElements() {
		
		// Image Logo
		
		JLabel houseImgLabel = new JLabel(houseIcon);
		GridBagConstraints gbc_houseImg = new GridBagConstraints();
		gbc_houseImg.gridwidth = 2;
		gbc_houseImg.insets = new Insets(0, 0, 5, 5);
		gbc_houseImg.gridx = 0;
		gbc_houseImg.gridy = 0;
		frame.getContentPane().add(houseImgLabel, gbc_houseImg);
		
		// Welcome to FEC label
		
		JLabel lblWelcomeToFec = new JLabel("House of Fun!");
		lblWelcomeToFec.setForeground(new Color(0, 0, 128));
		lblWelcomeToFec.setFont(new Font("Curlz MT", Font.BOLD, 54));
		GridBagConstraints gbc_lblWelcomeToFec = new GridBagConstraints();
		gbc_lblWelcomeToFec.anchor = GridBagConstraints.WEST;
		gbc_lblWelcomeToFec.insets = new Insets(0, 0, 5, 0);
		gbc_lblWelcomeToFec.gridwidth = 2;
		gbc_lblWelcomeToFec.gridx = 2;
		gbc_lblWelcomeToFec.gridy = 0;
		frame.getContentPane().add(lblWelcomeToFec, gbc_lblWelcomeToFec);
		
		// Please Log In label
		
		JLabel lblPleaseLogIn = new JLabel("Please log in");
		lblPleaseLogIn.setFont(new Font("Lucida Handwriting", Font.PLAIN, 21));
		GridBagConstraints gbc_lblPleaseLogIn = new GridBagConstraints();
		gbc_lblPleaseLogIn.gridheight = 2;
		gbc_lblPleaseLogIn.anchor = GridBagConstraints.NORTH;
		gbc_lblPleaseLogIn.insets = new Insets(0, 0, 5, 0);
		gbc_lblPleaseLogIn.gridwidth = 4;
		gbc_lblPleaseLogIn.gridx = 0;
		gbc_lblPleaseLogIn.gridy = 1;
		frame.getContentPane().add(lblPleaseLogIn, gbc_lblPleaseLogIn);
		
		// Username
		
		JLabel lblName = new JLabel("Username");
		lblName.setFont(new Font("Lucida Handwriting", Font.PLAIN, 13));
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 1;
		gbc_lblName.gridy = 3;
		frame.getContentPane().add(lblName, gbc_lblName);
		
		nameField = new JTextField();
		nameField.setFont(new Font("Lucida Handwriting", Font.PLAIN, 13));
		nameField.setToolTipText("Name");
		GridBagConstraints gbc_nameField = new GridBagConstraints();
		gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nameField.insets = new Insets(0, 0, 5, 5);
		gbc_nameField.gridx = 2;
		gbc_nameField.gridy = 3;
		frame.getContentPane().add(nameField, gbc_nameField);
		nameField.setColumns(10);
		
		// Password
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Lucida Handwriting", Font.PLAIN, 13));
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.WEST;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 1;
		gbc_lblPassword.gridy = 4;
		frame.getContentPane().add(lblPassword, gbc_lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Lucida Handwriting", Font.PLAIN, 13));
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.gridx = 2;
		gbc_passwordField.gridy = 4;
		frame.getContentPane().add(passwordField, gbc_passwordField);
		
		// Login Button
		
		btnLogIn = new JButton("Log In");
		btnLogIn.setFont(new Font("Lucida Handwriting", Font.PLAIN, 13));
		GridBagConstraints gbc_btnLogIn = new GridBagConstraints();
		gbc_btnLogIn.insets = new Insets(0, 0, 5, 5);
		gbc_btnLogIn.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnLogIn.gridx = 2;
		gbc_btnLogIn.gridy = 6;
		frame.getContentPane().add(btnLogIn, gbc_btnLogIn);
	}
}
