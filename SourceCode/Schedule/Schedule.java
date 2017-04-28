package Schedule;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import Attendance.DateLabelFormatter;
import Main.CustomDialog;
import Main.LoginScreen;
import Main.MainScreen;
import javax.swing.JLabel;

/**
 * This is the screen where users can manage employees' schedules
 * @author Tejas
 *
 */

public class Schedule {

	public JFrame frame;
	private JScrollPane scrollPane;
	private DefaultTableModel model;
	private JTable table;
	
	private UtilDateModel dateModel;
	private Properties p;
	private JDatePanelImpl datePanel;
	private JDatePickerImpl datePicker;
	
	private JButton btnBack;
	private JButton btnEdit;
	private JButton btnPrint;
	private JButton btnGenerate;
	
	private final String[] columnNames = {"EID", "Date", "FirstName", "MI", "LastName", "9:00", "10:00", "11:00", "12:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00"};

	
	private String date;

	/**
	 * Create the application.
	 */
	Connection conn = null;
	private JLabel lblShowSchedulesFor;
	public Schedule() {
		conn = LoginScreen.conn;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		// Initial Setup
		
		frame = new JFrame();
		frame.setBounds(100, 100, 990, 493);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{84, 0, 0, 150};
		gridBagLayout.rowHeights = new int[]{55, 96, 235, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 0.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE, 0.0, 0.0};
		frame.getContentPane().setLayout(gridBagLayout);
		
		
		
		// Add elements to JFrame
		
		addElements();
		
		
		
		// Set default column widths for table
		
		/*table.getColumnModel().getColumn(1).setPreferredWidth(50); // Date
		table.getColumnModel().getColumn(2).setPreferredWidth(50); // First Name
		table.getColumnModel().getColumn(3).setPreferredWidth(50); // MI
		table.getColumnModel().getColumn(4).setPreferredWidth(50); // Last Name
*/		
		
		// Add the table to the scrollPane to show it
		
		scrollPane.setViewportView(table);
	    
	    
		
		// When back button clicked
		
	    btnBack.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		MainScreen mainScreen = new MainScreen();
				mainScreen.frame.setVisible(true);
				frame.setVisible(false);
	    	}
	    });
	   
	    

	    // When edit button clicked
	    
	    btnEdit.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		try {
					int eid = Integer.parseInt((String) table.getValueAt(table.getSelectedRow(), 0));
					EditSchedule editWindow = new EditSchedule(eid, date);
					editWindow.frame.setVisible(true);
					frame.setVisible(false);
				} catch (Exception e1) {
					new CustomDialog(frame, "Error", "Please select a row to edit.").showError();
				}
	    	}
	    });
	    
	    
	    
	    // When print button clicked
	    
	    btnPrint.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		printComponent();
	    	}
	    });
	    
	    
	    
	    // When generate button clicked
	    
	    btnGenerate.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		Date selectedDate = (Date) datePicker.getModel().getValue();
				SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
				try {
					date = format1.format(selectedDate);
					System.out.println(date);
					updateModel();
				} catch (Exception e1) {
					new CustomDialog(frame, "Error", "Please select a date.").showError();
				}
				
	    	}
	    });

	    
	    
	    // Initializes the date string to the current date in format
	    
	    date = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
	   
	    
	    
	    // Update the table model to match the database data
	    
	    updateModel();
	    
	}
	
	public void updateModel() {
		
		// Selects the employee name fields, and joins the schedule table
		
		String query = "select e.eid, sDate, e.firstname, e.mi, e.lastname, s900,s1000, s1100, s1200, s100, s200, s300, s400, s500, s600 "
				+ "from employees e left join schedule s on e.eid = s.eid and s.sDate = '" + date + "'";
		
		try {
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			int numOfColumns = columnNames.length;
			
			model = new DefaultTableModel(columnNames, 0); // Initializes table model
			
			while (rs.next()) { // While there is more data
				Object[] row = new Object[numOfColumns]; // Create a row Object[]
				for(int j = 1; j <= numOfColumns; j++) {
					row[j - 1] = rs.getString(j); // Put the data into the row Object[]
				}
				model.addRow(row); // Add the Object[] into the model to add it as a row
			}
			
			rs.close(); // Close to save memory
			pst.close(); // Close to save memory
			
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}

		table.setModel(model); // Set the table's model to the new model
	}
	
	/**
     * Prints the frame (table) by opening the computer's default print dialog.
     */
	public void printComponent(){
    	PrinterJob pj = PrinterJob.getPrinterJob();
    	pj.setJobName(" Print Component ");
    	
    	pj.setPrintable (new Printable() {    
	    	public int print(Graphics pg, PageFormat pf, int pageNum){
	    		if (pageNum > 0){
	    			return Printable.NO_SUCH_PAGE;
	    		}
	  	
				Graphics2D g2 = (Graphics2D) pg;
				g2.translate(pf.getImageableX(), pf.getImageableY());
				scrollPane.paint(g2); // paint the table
				return Printable.PAGE_EXISTS;
	    	}
    	});
    	
		if (pj.printDialog() == false) return;
		
		try {
		      pj.print();
		} catch (PrinterException ex) {
			new CustomDialog(frame, "Error", "Could not print. Please try again.").showError();
		}
	}
	
	/**
	 * Adds individual elements to frame
	 */
	
	private void addElements() {
		
		// Scroll pane for table

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 3;
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
		
		// Date model
		
		dateModel = new UtilDateModel();
		
		// Date picker properties
		
		p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		
		// Date picker panel
		
		datePanel = new JDatePanelImpl(dateModel, p);
		
		// Show Schedules label
		
		lblShowSchedulesFor = new JLabel("Show Schedules For");
		lblShowSchedulesFor.setFont(new Font("Lucida Handwriting", Font.PLAIN, 13));
		GridBagConstraints gbc_lblShowSchedulesFor = new GridBagConstraints();
		gbc_lblShowSchedulesFor.anchor = GridBagConstraints.SOUTH;
		gbc_lblShowSchedulesFor.insets = new Insets(0, 0, 5, 0);
		gbc_lblShowSchedulesFor.gridx = 3;
		gbc_lblShowSchedulesFor.gridy = 0;
		frame.getContentPane().add(lblShowSchedulesFor, gbc_lblShowSchedulesFor);
		
		// Date picker
		
		datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		datePicker.getJFormattedTextField().setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_datePicker_1 = new GridBagConstraints();
		gbc_datePicker_1.anchor = GridBagConstraints.NORTH;
		gbc_datePicker_1.insets = new Insets(0, 0, 5, 0);
		gbc_datePicker_1.gridx = 3;
		gbc_datePicker_1.gridy = 1;
		frame.getContentPane().add(datePicker, gbc_datePicker_1);
		
		// Back button
		
		btnBack = new JButton("Back");
	    btnBack.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
	    GridBagConstraints gbc_btnBack = new GridBagConstraints();
	    gbc_btnBack.anchor = GridBagConstraints.WEST;
	    gbc_btnBack.insets = new Insets(0, 0, 0, 5);
	    gbc_btnBack.gridx = 0;
	    gbc_btnBack.gridy = 3;
	    frame.getContentPane().add(btnBack, gbc_btnBack);
	    
	    // Edit button
	    
	    btnEdit = new JButton("Edit");
	    btnEdit.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
	    GridBagConstraints gbc_btnEdit = new GridBagConstraints();
	    gbc_btnEdit.insets = new Insets(0, 0, 0, 5);
	    gbc_btnEdit.gridx = 2;
	    gbc_btnEdit.gridy = 3;
	    frame.getContentPane().add(btnEdit, gbc_btnEdit);
	    
	    // Print button
	    
	    btnPrint = new JButton("Print");
	    btnPrint.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
	    GridBagConstraints gbc_btnPrint = new GridBagConstraints();
	    gbc_btnPrint.anchor = GridBagConstraints.WEST;
	    gbc_btnPrint.insets = new Insets(0, 0, 0, 5);
	    gbc_btnPrint.gridx = 1;
	    gbc_btnPrint.gridy = 3;
	    frame.getContentPane().add(btnPrint, gbc_btnPrint);
	    
	    // Go/Generate button
	    
	    btnGenerate = new JButton("Go");
	    btnGenerate.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
	    GridBagConstraints gbc_btnGenerate = new GridBagConstraints();
	    gbc_btnGenerate.anchor = GridBagConstraints.NORTH;
	    gbc_btnGenerate.insets = new Insets(0, 0, 5, 0);
	    gbc_btnGenerate.gridx = 3;
	    gbc_btnGenerate.gridy = 2;
	    frame.getContentPane().add(btnGenerate, gbc_btnGenerate);
	}

}
