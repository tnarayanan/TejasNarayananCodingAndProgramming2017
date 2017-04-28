package Employees;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import Main.CustomDialog;
import Main.LoginScreen;

/**
 * This is the screen where users can add a new employee into the database.
 * @author Tejas
 *
 */

public class Add {

	

	/**
	 * Create the application.
	 */
	
	Connection conn = null;
	public JFrame frame;
	private JTextField textFieldEid;
	private JTextField textFieldFirstName;
	private JTextField textFieldLastName;
	private JTextField textFieldMiddle;
	private JTextField textFieldPhone3;
	private JTextField textFieldDob;
	private JTextField textFieldWage;
	private JTextField textFieldStartDate;
	private JTextField textFieldEndDate;
	private JTextField textFieldSsn;
	private JTextField textFieldPhone2;
	private JTextField textFieldPhone1;
	
	private JButton btnCancel;
	private JButton btnAdd;
	
	public Add() {
		conn = LoginScreen.conn;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		// Initial Setup
		
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		frame.setBounds(100, 100, 600, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{68, 85, 85, 56, 132, 168, 0};
		gridBagLayout.rowHeights = new int[]{51, 28, 28, 28, 28, 28, 29, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		
		
		// Add elements to JFrame
		
		addElements();
		
		
		
		// Limits number of characters for each phone number section
		
		textFieldPhone1.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { // When a key is typed in this field
		        if (textFieldPhone1.getText().length() >= 3 )
		            e.consume(); // If the number of characters is already the max, "consume"/suppress that keystroke
		    }  
		});
		
		textFieldPhone2.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { // When a key is typed in this field
		        if (textFieldPhone2.getText().length() >= 3 )
		            e.consume(); // If the number of characters is already the max, "consume"/suppress that keystroke
		    }  
		});
		
		textFieldPhone3.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { // When a key is typed in this field
		        if (textFieldPhone3.getText().length() >= 4 )
		            e.consume(); // If the number of characters is already the max, "consume"/suppress that keystroke
		    }  
		});
		
		
		
		// Limits number of characters in SSN field to 9
		
		textFieldSsn.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { // When a key is typed in this field
		        if (textFieldSsn.getText().length() >= 9 )
		            e.consume(); // If the number of characters is already the max, "consume"/suppress that keystroke
		    }  
		});
		
		
		
		// Find new EID by looking at number of rows in Employees table, then add 1
		
		String query = "select count(*) from Employees"; // Number of employees in table
		int numOfRows = 0;
		try {
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			while (rs.next()){
				numOfRows = rs.getInt(1); // Gets result
			}
			
			pst.close();
			rs.close();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		
		
		// Set EID text to number of employees + 1

		textFieldEid.setText(Integer.toString(numOfRows + 1));
		
		
		
		// When the user clicks cancel, prompts whether they want to lose changes
		
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = new CustomDialog(frame, "Warning", "Are you sure you want to exit and lose your changes?").showOptionPane();
				if (result == JOptionPane.YES_OPTION) {
					Employees mainScreen = new Employees(); // Goes back to main screen
					mainScreen.frame.setVisible(true);
					frame.setVisible(false);
				}
			}
		});
		
		
		
		/* When the user clicks the add button:
		 * 1. Check each field to make sure the input format is correct
		 * 		- If not, alert the user
		 * 2. If everything is correct, update the database
		 */
		
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (validFields()) { // Check each field
					try {
						// Add all the values from the fields into a new employee
						String query = "insert into Employees values (" 
								+ textFieldEid.getText()
								+ ", \"" + textFieldFirstName.getText() 
								+ "\", \"" + textFieldMiddle.getText() 
								+ "\", \"" + textFieldLastName.getText()
								+ "\", \"" + textFieldPhone1.getText() + "-" + textFieldPhone2.getText() + "-" + textFieldPhone3.getText()
								+ "\", \"" + textFieldDob.getText()
								+ "\", " + textFieldWage.getText()
								+ ", \"" + textFieldStartDate.getText()
								+ "\", \"" + textFieldEndDate.getText()
								+ "\", " + textFieldSsn.getText()
								+ ")";
						
						Statement statement = conn.createStatement();
						
						statement.executeUpdate(query);
						
						statement.close();
						
						Employees tableViewWindow = new Employees(); // Go back to employees screen
						tableViewWindow.frame.setVisible(true);
						frame.setVisible(false);
						
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		
		
		// Sets tab order
		
		frame.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{
				textFieldFirstName, 
				textFieldMiddle, 
				textFieldLastName, 
				textFieldPhone1, 
				textFieldPhone2, 
				textFieldPhone3, 
				textFieldDob, 
				textFieldWage, 
				textFieldStartDate, 
				textFieldEndDate, 
				textFieldSsn, 
				btnCancel, 
				btnAdd}));
		
	}
	
	/**
	 * Checks if input fields have valid values
	 * @return
	 */
	private boolean validFields() {
		if (textFieldFirstName.getText().length() > 0) { // First Name Valid
			
			// Middle name is allowed to be empty
			
			if (textFieldLastName.getText().length() > 0) { // Last Name Valid
				if (textFieldPhone1.getText().length() == 3) { // Phone Number Section 1 Valid
					if (textFieldPhone2.getText().length() == 3) { // Phone Number Section 2 Valid
						if (textFieldPhone3.getText().length() == 4) { // Phone Number Section 3 Valid
							if (isValidFormat("MM/dd/yyyy", textFieldDob.getText())) { // DOB Valid
								try {
									Double.parseDouble(textFieldWage.getText()); // Wage Valid
									if (isValidFormat("MM/dd/yyyy", textFieldStartDate.getText())) { // Start Date Valid
										if (textFieldEndDate.getText().length() == 0 || isValidFormat("MM/dd/yyyy", textFieldEndDate.getText())) { // If End Date is empty or, if not, fits the requirements (Valid)
											if (textFieldSsn.getText().length() == 9) { // SSN Valid
												
												return true; // All inputs are valid
												
											} else new CustomDialog(frame, "Error", "Please enter a valid Social Security number with 9 digits").showError(); // Bad SSN date
										} else new CustomDialog(frame, "Error", "Please enter a valid end date in the form MM/dd/yyyy.").showError(); // Bad end date
									} else new CustomDialog(frame, "Error", "Please enter a valid start date in the form MM/dd/yyyy.").showError(); // Bad start date
								} catch (NumberFormatException e1) {
									new CustomDialog(frame, "Error", "Please enter a valid wage without any dollar signs or other symbols").showError(); // Bad wage
								}
							} else new CustomDialog(frame, "Error", "Please enter a valid date of birth in the form MM/dd/yyyy.").showError(); // Bad DOB
						} else new CustomDialog(frame, "Error", "Please enter a valid phone number.").showError(); // Bad Phone Number
					} else new CustomDialog(frame, "Error", "Please enter a valid phone number.").showError(); // Bad Phone Number
				} else new CustomDialog(frame, "Error", "Please enter a valid phone number.").showError(); // Bad Phone Number
			} else new CustomDialog(frame, "Error", "Please enter a last name.").showError(); // Bad last name
		} else new CustomDialog(frame, "Error", "Please enter a first name.").showError(); // Bad first name
		
		return false;
	}
	

	
	/**
	 * Checks if date is in the correct format
	 * @param format
	 * @param value
	 * @return
	 */
	
	public static boolean isValidFormat(String format, String value) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format); // Creates date format with parameter
            sdf.setLenient(false); // Stricter rules on accepted dates
            Date date = sdf.parse(value); // Value parsed into date
            if (!value.equals(sdf.format(date))) { // If the value parameter does not equal the formatted date
                return false; // Invalid date
            }
        } catch (ParseException ex) {
        	return false; // Invalid date
        }
        return true;
    }
	
	/**
	 * Adds individual elements to frame
	 */
	
	private void addElements() {
		
		// Title
		
		JLabel lblTitle = new JLabel("Add Employee");
		lblTitle.setFont(new Font("Curlz MT", Font.PLAIN, 36));
		GridBagConstraints gbc_lblTitle = new GridBagConstraints();
		gbc_lblTitle.gridwidth = 6;
		gbc_lblTitle.insets = new Insets(0, 0, 5, 0);
		gbc_lblTitle.gridx = 0;
		gbc_lblTitle.gridy = 0;
		frame.getContentPane().add(lblTitle, gbc_lblTitle);
		
		// EID
		
		JLabel lblEid = new JLabel("EID");
		lblEid.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_lblEid = new GridBagConstraints();
		gbc_lblEid.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblEid.insets = new Insets(0, 0, 5, 5);
		gbc_lblEid.gridx = 0;
		gbc_lblEid.gridy = 1;
		frame.getContentPane().add(lblEid, gbc_lblEid);
		
		textFieldEid = new JTextField();
		textFieldEid.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_textFieldEid = new GridBagConstraints();
		gbc_textFieldEid.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldEid.gridwidth = 3;
		gbc_textFieldEid.anchor = GridBagConstraints.NORTH;
		gbc_textFieldEid.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldEid.gridx = 1;
		gbc_textFieldEid.gridy = 1;
		frame.getContentPane().add(textFieldEid, gbc_textFieldEid);
		textFieldEid.setColumns(10);
		textFieldEid.setEditable(false);
		textFieldEid.setEnabled(false);
		
		// First Name
		
		JLabel lblFirstName = new JLabel("First Name *");
		lblFirstName.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_lblFirstName = new GridBagConstraints();
		gbc_lblFirstName.anchor = GridBagConstraints.WEST;
		gbc_lblFirstName.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirstName.gridx = 0;
		gbc_lblFirstName.gridy = 2;
		frame.getContentPane().add(lblFirstName, gbc_lblFirstName);
		
		textFieldFirstName = new JTextField();
		textFieldFirstName.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_textFieldFirstName = new GridBagConstraints();
		gbc_textFieldFirstName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldFirstName.gridwidth = 3;
		gbc_textFieldFirstName.anchor = GridBagConstraints.NORTH;
		gbc_textFieldFirstName.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldFirstName.gridx = 1;
		gbc_textFieldFirstName.gridy = 2;
		frame.getContentPane().add(textFieldFirstName, gbc_textFieldFirstName);
		textFieldFirstName.setColumns(10);
		
		// Middle Name
		
		JLabel lblMiddle = new JLabel("Middle");
		lblMiddle.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_lblMiddle = new GridBagConstraints();
		gbc_lblMiddle.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblMiddle.insets = new Insets(0, 0, 5, 5);
		gbc_lblMiddle.gridx = 0;
		gbc_lblMiddle.gridy = 3;
		frame.getContentPane().add(lblMiddle, gbc_lblMiddle);
		
		textFieldMiddle = new JTextField();
		textFieldMiddle.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_textFieldMiddle = new GridBagConstraints();
		gbc_textFieldMiddle.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldMiddle.gridwidth = 3;
		gbc_textFieldMiddle.anchor = GridBagConstraints.NORTH;
		gbc_textFieldMiddle.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldMiddle.gridx = 1;
		gbc_textFieldMiddle.gridy = 3;
		frame.getContentPane().add(textFieldMiddle, gbc_textFieldMiddle);
		textFieldMiddle.setColumns(10);
		
		// Last Name
		
		JLabel lblLastName = new JLabel("Last Name *");
		lblLastName.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_lblLastName = new GridBagConstraints();
		gbc_lblLastName.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblLastName.insets = new Insets(0, 0, 5, 5);
		gbc_lblLastName.gridx = 0;
		gbc_lblLastName.gridy = 4;
		frame.getContentPane().add(lblLastName, gbc_lblLastName);
		
		textFieldLastName = new JTextField();
		textFieldLastName.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_textFieldLastName = new GridBagConstraints();
		gbc_textFieldLastName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldLastName.gridwidth = 3;
		gbc_textFieldLastName.anchor = GridBagConstraints.NORTH;
		gbc_textFieldLastName.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldLastName.gridx = 1;
		gbc_textFieldLastName.gridy = 4;
		frame.getContentPane().add(textFieldLastName, gbc_textFieldLastName);
		textFieldLastName.setColumns(10);
		
		// Phone Number
		
		JLabel lblPhone = new JLabel("Phone # *");
		lblPhone.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_lblPhone = new GridBagConstraints();
		gbc_lblPhone.anchor = GridBagConstraints.WEST;
		gbc_lblPhone.insets = new Insets(0, 0, 5, 5);
		gbc_lblPhone.gridx = 0;
		gbc_lblPhone.gridy = 5;
		frame.getContentPane().add(lblPhone, gbc_lblPhone);
		
		textFieldPhone1 = new JTextField();
		textFieldPhone1.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		textFieldPhone1.setColumns(10);
		GridBagConstraints gbc_textFieldPhone1 = new GridBagConstraints();
		gbc_textFieldPhone1.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldPhone1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldPhone1.gridx = 1;
		gbc_textFieldPhone1.gridy = 5;
		frame.getContentPane().add(textFieldPhone1, gbc_textFieldPhone1);
		
		textFieldPhone2 = new JTextField();
		textFieldPhone2.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		textFieldPhone2.setColumns(10);
		GridBagConstraints gbc_textFieldPhone2 = new GridBagConstraints();
		gbc_textFieldPhone2.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldPhone2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldPhone2.gridx = 2;
		gbc_textFieldPhone2.gridy = 5;
		frame.getContentPane().add(textFieldPhone2, gbc_textFieldPhone2);
		
		textFieldPhone3 = new JTextField();
		textFieldPhone3.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_textFieldPhone3 = new GridBagConstraints();
		gbc_textFieldPhone3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldPhone3.anchor = GridBagConstraints.NORTH;
		gbc_textFieldPhone3.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldPhone3.gridx = 3;
		gbc_textFieldPhone3.gridy = 5;
		frame.getContentPane().add(textFieldPhone3, gbc_textFieldPhone3);
		textFieldPhone3.setColumns(10);
		
		// Date of Birth
		
		JLabel lblDob = new JLabel("DOB *");
		lblDob.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_lblDob = new GridBagConstraints();
		gbc_lblDob.anchor = GridBagConstraints.WEST;
		gbc_lblDob.insets = new Insets(0, 0, 5, 5);
		gbc_lblDob.gridx = 4;
		gbc_lblDob.gridy = 1;
		frame.getContentPane().add(lblDob, gbc_lblDob);
		
		textFieldDob = new JTextField();
		textFieldDob.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_textFieldDob = new GridBagConstraints();
		gbc_textFieldDob.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldDob.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldDob.gridx = 5;
		gbc_textFieldDob.gridy = 1;
		frame.getContentPane().add(textFieldDob, gbc_textFieldDob);
		textFieldDob.setColumns(10);
		
		// Hourly Wage
		
		JLabel lblHourlyWage = new JLabel("Hourly Wage *        $");
		lblHourlyWage.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_lblHourlyWage = new GridBagConstraints();
		gbc_lblHourlyWage.anchor = GridBagConstraints.WEST;
		gbc_lblHourlyWage.insets = new Insets(0, 0, 5, 5);
		gbc_lblHourlyWage.gridx = 4;
		gbc_lblHourlyWage.gridy = 2;
		frame.getContentPane().add(lblHourlyWage, gbc_lblHourlyWage);
		
		textFieldWage = new JTextField();
		textFieldWage.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_textFieldWage = new GridBagConstraints();
		gbc_textFieldWage.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldWage.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldWage.gridx = 5;
		gbc_textFieldWage.gridy = 2;
		frame.getContentPane().add(textFieldWage, gbc_textFieldWage);
		textFieldWage.setColumns(10);
		
		// Start Date
		
		JLabel lblStartDate = new JLabel("Start Date *");
		lblStartDate.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_lblStartDate = new GridBagConstraints();
		gbc_lblStartDate.anchor = GridBagConstraints.WEST;
		gbc_lblStartDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblStartDate.gridx = 4;
		gbc_lblStartDate.gridy = 3;
		frame.getContentPane().add(lblStartDate, gbc_lblStartDate);
		
		textFieldStartDate = new JTextField();
		textFieldStartDate.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_textFieldStartDate = new GridBagConstraints();
		gbc_textFieldStartDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldStartDate.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldStartDate.gridx = 5;
		gbc_textFieldStartDate.gridy = 3;
		frame.getContentPane().add(textFieldStartDate, gbc_textFieldStartDate);
		textFieldStartDate.setColumns(10);
		
		// End Date
		
		JLabel lblEndDate = new JLabel("End Date");
		lblEndDate.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_lblEndDate = new GridBagConstraints();
		gbc_lblEndDate.anchor = GridBagConstraints.WEST;
		gbc_lblEndDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblEndDate.gridx = 4;
		gbc_lblEndDate.gridy = 4;
		frame.getContentPane().add(lblEndDate, gbc_lblEndDate);
		
		textFieldEndDate = new JTextField();
		textFieldEndDate.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_textFieldEndDate = new GridBagConstraints();
		gbc_textFieldEndDate.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldEndDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldEndDate.gridx = 5;
		gbc_textFieldEndDate.gridy = 4;
		frame.getContentPane().add(textFieldEndDate, gbc_textFieldEndDate);
		textFieldEndDate.setColumns(10);
		
		// SSN
		
		JLabel lblSsn = new JLabel("SSN *");
		lblSsn.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_lblSsn = new GridBagConstraints();
		gbc_lblSsn.anchor = GridBagConstraints.WEST;
		gbc_lblSsn.insets = new Insets(0, 0, 5, 5);
		gbc_lblSsn.gridx = 4;
		gbc_lblSsn.gridy = 5;
		frame.getContentPane().add(lblSsn, gbc_lblSsn);
		
		textFieldSsn = new JTextField();
		textFieldSsn.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_textFieldSsn = new GridBagConstraints();
		gbc_textFieldSsn.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldSsn.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldSsn.gridx = 5;
		gbc_textFieldSsn.gridy = 5;
		frame.getContentPane().add(textFieldSsn, gbc_textFieldSsn);
		textFieldSsn.setColumns(10);
		
		// Cancel button
		
		btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.anchor = GridBagConstraints.EAST;
		gbc_btnCancel.insets = new Insets(0, 0, 5, 5);
		gbc_btnCancel.gridx = 3;
		gbc_btnCancel.gridy = 6;
		frame.getContentPane().add(btnCancel, gbc_btnCancel);
		
		// Add Button
		
		btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 5, 5);
		gbc_btnAdd.anchor = GridBagConstraints.NORTH;
		gbc_btnAdd.gridx = 4;
		gbc_btnAdd.gridy = 6;
		frame.getContentPane().add(btnAdd, gbc_btnAdd);
	}
}
