package Attendance;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import Main.LoginScreen;
import Main.MainScreen;

/**
 * This is the screen where users can track visitor attendance.
 * @author Tejas
 *
 */

public class Attendance {

	public JFrame frame;
	private JTable table;
	private JButton btnAdult;
	private JButton btnChild;
	private JButton btnGenerate;
	private JButton btnBack;
	private JDatePickerImpl datePicker;
	private DefaultTableModel dtm;

	/**
	 * Create the application.
	 */
	Connection conn = null;
	public Attendance() {
		conn = LoginScreen.conn;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		// Initial setup
		
		frame = new JFrame();
		frame.setBounds(100, 100, 900, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{23, 266, 177, 114, 0};
		gridBagLayout.rowHeights = new int[]{5, 48, 38, 43, 38, 0, 41, 36, 47, 5, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		
		
		// Add elements to JFrame
		
		addElements();
		
		
		
		// Initialize table with existing data
		
		String query = "select * from Attendance";
		try {
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int numOfColumns = rsmd.getColumnCount();
			String[] columnNames = new String[numOfColumns];
			for (int i = 1; i <= numOfColumns; i++) {
				columnNames[i - 1] = rsmd.getColumnName(i);
			}
			
			dtm = new DefaultTableModel(columnNames, 0);
			
			while (rs.next()) {
				Object[] row = new Object[numOfColumns];
				for (int j = 1; j <= numOfColumns; j++) {
					row[j - 1] = rs.getString(j);
				}
				int fullSecs = Integer.parseInt((String) row[1]);
				int hourRem = fullSecs % 3600;
				int hours = (fullSecs - hourRem) / 3600;
				String hourStr = ((hours < 10) ? "0" : "") + String.valueOf(hours);
				int minRem = hourRem % 60;
				int mins = (hourRem - minRem) / 60;
				String minStr = ((mins < 10) ? "0" : "") + String.valueOf(mins);
				int secs = minRem;
				String secStr = ((secs < 10) ? "0" : "") + String.valueOf(secs);
				
				row[1] = hourStr + ":" + minStr + ":" + secStr;
				dtm.addRow(row);
			}
			
			rs.close();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		// Sets table model to database data model

		table.setModel(dtm);
		
		
		
		// When +1 Adult button clicked
		
		btnAdult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addVisitor("Adult");
			}
		});
		
		
		
		// When +1 Child button clicked
		
		btnChild.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addVisitor("Child");
			}
		});
		
		
		
		// When Generate button clicked
		
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Date selectedDate = (Date) datePicker.getModel().getValue(); // Gets selected date from datePicker
					
					SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
					String formatted = format1.format(selectedDate); // Converts date to MM/dd/yyyy and parses to string
					GraphCalendar graph = new GraphCalendar(formatted); // Passes date to Graph class
					graph.frame.setVisible(true);
					frame.setVisible(false);
				} catch (NullPointerException e1) {
					JOptionPane.showMessageDialog(null, "Please select a date in the calendar.");
				}
				
				
			}
		});
		
		
		
		// When back clicked
		
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainScreen mr = new MainScreen(); // Return to main screen
				mr.frame.setVisible(true);
				frame.setVisible(false);
			}
		});	
	}
	
	/**
	 * Adds either adult or child visitor, depending on parameter
	 * @param category
	 */
	
	private void addVisitor(String category) {
		String currDate = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
		
		// Gets time in seconds since midnight
		Calendar c = Calendar.getInstance();
		long now = c.getTimeInMillis();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		long passed = now - c.getTimeInMillis();
		long secondsPassed = passed / 1000;
		
		String currTime = String.valueOf(secondsPassed);
		
		String queryAddChild = "insert into Attendance values (\""
				+ currDate
				+ "\", "
				+ currTime
				+ ", \"" + category + "\")";
		
		try {
			Statement statement = conn.createStatement();
			statement.executeUpdate(queryAddChild);
			
		} catch(SQLException e1) {
			e1.printStackTrace();
		}
		
		Attendance attendance = new Attendance();
		attendance.frame.setVisible(true);
		frame.setVisible(false);
	}
	
	/**
	 * Adds individual elements to frame
	 */
	
	private void addElements() {
		
		// Main Label
		
		JLabel lblEditAttendance = new JLabel("Visitor Attendance");
		lblEditAttendance.setFont(new Font("Curlz MT", Font.PLAIN, 36));
		GridBagConstraints gbc_lblEditAttendance = new GridBagConstraints();
		gbc_lblEditAttendance.gridwidth = 2;
		gbc_lblEditAttendance.insets = new Insets(0, 0, 5, 5);
		gbc_lblEditAttendance.gridx = 2;
		gbc_lblEditAttendance.gridy = 1;
		frame.getContentPane().add(lblEditAttendance, gbc_lblEditAttendance);
		
		// Attendance Label
		
		JLabel lblAttendance = new JLabel("Attendance");
		lblAttendance.setFont(new Font("Lucida Handwriting", Font.PLAIN, 18));
		GridBagConstraints gbc_lblAttendance = new GridBagConstraints();
		gbc_lblAttendance.insets = new Insets(0, 0, 5, 5);
		gbc_lblAttendance.gridx = 2;
		gbc_lblAttendance.gridy = 4;
		frame.getContentPane().add(lblAttendance, gbc_lblAttendance);
		
		// Scroll pane for table
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 8;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 1;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);
		
		// Table
		
		table = new JTable() {
			private static final long serialVersionUID = 1L;

	        public boolean isCellEditable(int row, int column) {                
	                return false;               
	        };
		};
		scrollPane.setViewportView(table);
		
		// Calendar Picker
		
		UtilDateModel model = new UtilDateModel();
		
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		
		datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		datePicker.getJFormattedTextField().setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_datePicker_1 = new GridBagConstraints();
		gbc_datePicker_1.anchor = GridBagConstraints.NORTH;
		gbc_datePicker_1.insets = new Insets(0, 0, 5, 5);
		gbc_datePicker_1.gridx = 2;
		gbc_datePicker_1.gridy = 8;
		frame.getContentPane().add(datePicker, gbc_datePicker_1);
		
		// +1 Adult
		
		btnAdult = new JButton("+1 Adult");
		btnAdult.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_btnAdult = new GridBagConstraints();
		gbc_btnAdult.insets = new Insets(0, 0, 5, 0);
		gbc_btnAdult.gridx = 3;
		gbc_btnAdult.gridy = 5;
		frame.getContentPane().add(btnAdult, gbc_btnAdult);
		
		// +1 Child
		
		btnChild = new JButton("+1 Child");
		btnChild.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_btnChild = new GridBagConstraints();
		gbc_btnChild.insets = new Insets(0, 0, 5, 0);
		gbc_btnChild.gridx = 3;
		gbc_btnChild.gridy = 4;
		frame.getContentPane().add(btnChild, gbc_btnChild);
		
		// Generate
		
		JLabel lblGenerateGraph = new JLabel("Generate Attendance Graph");
		lblGenerateGraph.setFont(new Font("Lucida Handwriting", Font.PLAIN, 18));
		GridBagConstraints gbc_lblGenerateGraph = new GridBagConstraints();
		gbc_lblGenerateGraph.anchor = GridBagConstraints.SOUTH;
		gbc_lblGenerateGraph.gridwidth = 2;
		gbc_lblGenerateGraph.insets = new Insets(0, 0, 5, 0);
		gbc_lblGenerateGraph.gridx = 2;
		gbc_lblGenerateGraph.gridy = 6;
		frame.getContentPane().add(lblGenerateGraph, gbc_lblGenerateGraph);
		
		btnGenerate = new JButton("Generate");
		btnGenerate.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_btnGenerateCalendar = new GridBagConstraints();
		gbc_btnGenerateCalendar.anchor = GridBagConstraints.NORTH;
		gbc_btnGenerateCalendar.insets = new Insets(0, 0, 5, 0);
		gbc_btnGenerateCalendar.gridx = 3;
		gbc_btnGenerateCalendar.gridy = 8;
		frame.getContentPane().add(btnGenerate, gbc_btnGenerateCalendar);
		
		// Back Button
		
		btnBack = new JButton("Back");
		btnBack.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_btnBack = new GridBagConstraints();
		gbc_btnBack.insets = new Insets(0, 0, 0, 5);
		gbc_btnBack.gridx = 1;
		gbc_btnBack.gridy = 9;
		frame.getContentPane().add(btnBack, gbc_btnBack);
		
	}

}
