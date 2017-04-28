package Employees;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Main.CustomDialog;
import Main.LoginScreen;
import Main.MainScreen;

/**
 * This is the screen where users can view employee data like name, date of birth, wage, etc.
 * @author Tejas
 *
 */

public class Employees {

	public JFrame frame;
	private JTable table;
	private DefaultTableModel model;
	
	JButton btnBack;
	JButton btnAdd;
	JButton btnEdit;
	
	
	/**
	 * Create the application.
	 */
	
	Connection conn = null;
	public Employees() {
		conn = LoginScreen.conn;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		// Initial Setup
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{1.0, 0.0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0};
		frame.getContentPane().setLayout(gridBagLayout);
		
		
		
		// Adds elements to JFrame
		
		addElements();
		
		
		
		// Queries data from database and adds it to TableModel
		
		String query = "select * from Employees"; // Select all of employees
		try {
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int numOfColumns = rsmd.getColumnCount(); // Get number of columns in employees
			String[] columnNames = new String[numOfColumns]; // Get list of column names
			for (int j = 1; j <= numOfColumns; j++) {
				columnNames[j - 1] = rsmd.getColumnName(j);
			}
			
			model = new DefaultTableModel(columnNames, 0);
			
			while (rs.next()) {
				Object[] row = new Object[numOfColumns];
				for(int j = 1; j <= numOfColumns; j++) {
					row[j - 1] = rs.getString(j);
				}
				model.addRow(row); // Add each row of database to DefaultTableModel
			}
			
			rs.close();
			pst.close();
			
			table.setModel(model); // Sets model of table to the newly made model
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		// When back clicked
		
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainScreen mainScreen = new MainScreen(); // Go back to main screen
				mainScreen.frame.setVisible(true);
				frame.setVisible(false);
			}
		});
		
		
		
		// When edit clicked
		
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int eid = Integer.parseInt((String) table.getValueAt(table.getSelectedRow(), 0)); // Get EID of currently selected row
					Edit editWindow = new Edit(eid); // Go to edit screen
					editWindow.frame.setVisible(true);	
					frame.setVisible(false);
				} catch (Exception e1) {
					new CustomDialog(frame, "Error", "Please select a row to edit").showError(); // If the user has not selected a row, prompt them
				}
			}
		});
		
		
		
		// When add clicked
		
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Add addWindow = new Add(); // Go to add screen
				addWindow.frame.setVisible(true);
				frame.setVisible(false);
			}
		});
		
	}
	
	/**
	 * Adds individual elements to frame
	 */
	
	private void addElements() {
		
		// Scroll pane for table
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);
		
		// Table
		
		table = new JTable() {
			private static final long serialVersionUID = 1L;

	        public boolean isCellEditable(int row, int column) {                
	                return false;               
	        };
		};
		table.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(table);
		
		// Back button
		
		btnBack = new JButton("Back");
		btnBack.setFont(new Font("Lucida Handwriting", Font.PLAIN, 13));
		GridBagConstraints gbc_btnBack = new GridBagConstraints();
		gbc_btnBack.anchor = GridBagConstraints.EAST;
		gbc_btnBack.insets = new Insets(0, 0, 0, 5);
		gbc_btnBack.gridx = 0;
		gbc_btnBack.gridy = 1;
		frame.getContentPane().add(btnBack, gbc_btnBack);
		
		// Edit button
		
		btnEdit = new JButton("Edit");
		btnEdit.setFont(new Font("Lucida Handwriting", Font.PLAIN, 13));
		GridBagConstraints gbc_btnEdit = new GridBagConstraints();
		gbc_btnEdit.insets = new Insets(0, 0, 0, 5);
		gbc_btnEdit.gridx = 1;
		gbc_btnEdit.gridy = 1;
		frame.getContentPane().add(btnEdit, gbc_btnEdit);
		
		// Add button
		
		btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Lucida Handwriting", Font.PLAIN, 13));
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.anchor = GridBagConstraints.WEST;
		gbc_btnAdd.insets = new Insets(0, 0, 0, 5);
		gbc_btnAdd.gridx = 2;
		gbc_btnAdd.gridy = 1;
		frame.getContentPane().add(btnAdd, gbc_btnAdd);
	}
}
