package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Attendance.Attendance;
import Employees.Employees;
import Schedule.Schedule;

/**
 * This class is the screen that gives access to each of the three main sections.
 * @author Tejas
 *
 */

public class MainScreen {

	public JFrame frame;
	private JButton btnEmployees;
	private JButton btnAttendance;
	private JButton btnSchedule;
	
	
	/**
	 * Create the application.
	 */
	public MainScreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		// Initial setup
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{117, 309};
		gridBagLayout.rowHeights = new int[]{24, 29, 31, 29, 31, 29, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		
		
		// Add elements to JFrame
		
		addElements();
		
		
		
		// When Employees button clicked
		
		btnEmployees.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Employees employeesScreen = new Employees();
				employeesScreen.frame.setVisible(true);
				frame.setVisible(false);
			}
		});
		
		
		
		// When Attendance button clicked
		
		btnAttendance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Attendance attendanceScreen = new Attendance();
				attendanceScreen.frame.setVisible(true);
				frame.setVisible(false);
			}
		});
		
		
		
		// When Schedule button clicked
		
		btnSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Schedule scheduleScreen = new Schedule();
				scheduleScreen.frame.setVisible(true);
				frame.setVisible(false);
			}
		});
	}
	
	/**
	 * Adds individual elements to frame
	 */
	
	private void addElements() {
		
		// Image Logo
		
		JLabel houseImgLabel = new JLabel(LoginScreen.houseIcon);
		GridBagConstraints gbc_houseImgLabel = new GridBagConstraints();
		gbc_houseImgLabel.insets = new Insets(0, 0, 5, 5);
		gbc_houseImgLabel.gridx = 0;
		gbc_houseImgLabel.gridy = 0;
		frame.getContentPane().add(houseImgLabel, gbc_houseImgLabel);
		
		// Title
		
		JLabel lblNewLabel = new JLabel("House of Fun!");
		lblNewLabel.setForeground(new Color(0, 0, 128));
		lblNewLabel.setFont(new Font("Curlz MT", Font.PLAIN, 54));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		frame.getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		// Employees
		
		JLabel lblEmployeesText = new JLabel("Add, edit, or change work status");
		lblEmployeesText.setToolTipText("");
		lblEmployeesText.setFont(new Font("Lucida Handwriting", Font.PLAIN, 13));
		GridBagConstraints gbc_lblEmployeesText = new GridBagConstraints();
		gbc_lblEmployeesText.anchor = GridBagConstraints.WEST;
		gbc_lblEmployeesText.insets = new Insets(0, 0, 5, 0);
		gbc_lblEmployeesText.gridx = 1;
		gbc_lblEmployeesText.gridy = 2;
		frame.getContentPane().add(lblEmployeesText, gbc_lblEmployeesText);
		
		btnEmployees = new JButton("Employees");
		btnEmployees.setToolTipText("Add, edit, or change work status of employees");
		btnEmployees.setFont(new Font("Lucida Handwriting", Font.PLAIN, 13));
		GridBagConstraints gbc_btnEmployees = new GridBagConstraints();
		gbc_btnEmployees.anchor = GridBagConstraints.WEST;
		gbc_btnEmployees.insets = new Insets(0, 0, 5, 5);
		gbc_btnEmployees.gridx = 0;
		gbc_btnEmployees.gridy = 2;
		frame.getContentPane().add(btnEmployees, gbc_btnEmployees);
		
		// Attendance
		
		JLabel lblAttendanceText = new JLabel("Modify & view graphs");
		lblAttendanceText.setToolTipText("");
		lblAttendanceText.setFont(new Font("Lucida Handwriting", Font.PLAIN, 13));
		GridBagConstraints gbc_lblAttendanceText = new GridBagConstraints();
		gbc_lblAttendanceText.anchor = GridBagConstraints.WEST;
		gbc_lblAttendanceText.insets = new Insets(0, 0, 5, 0);
		gbc_lblAttendanceText.gridx = 1;
		gbc_lblAttendanceText.gridy = 4;
		frame.getContentPane().add(lblAttendanceText, gbc_lblAttendanceText);
		
		btnAttendance = new JButton("Attendance");
		btnAttendance.setToolTipText("Modify attendance records and view graphs");
		btnAttendance.setFont(new Font("Lucida Handwriting", Font.PLAIN, 13));
		GridBagConstraints gbc_btnAttendance = new GridBagConstraints();
		gbc_btnAttendance.anchor = GridBagConstraints.WEST;
		gbc_btnAttendance.insets = new Insets(0, 0, 5, 5);
		gbc_btnAttendance.gridx = 0;
		gbc_btnAttendance.gridy = 4;
		frame.getContentPane().add(btnAttendance, gbc_btnAttendance);
		
		// Schedule
		
		JLabel lblModifyEmployeeSchedules = new JLabel("Modify employee schedules");
		lblModifyEmployeeSchedules.setFont(new Font("Lucida Handwriting", Font.PLAIN, 13));
		GridBagConstraints gbc_lblModifyEmployeeSchedules = new GridBagConstraints();
		gbc_lblModifyEmployeeSchedules.anchor = GridBagConstraints.WEST;
		gbc_lblModifyEmployeeSchedules.gridx = 1;
		gbc_lblModifyEmployeeSchedules.gridy = 6;
		frame.getContentPane().add(lblModifyEmployeeSchedules, gbc_lblModifyEmployeeSchedules);
		
		btnSchedule = new JButton("Schedule");
		btnSchedule.setToolTipText("Modify employee schedules");
		btnSchedule.setFont(new Font("Lucida Handwriting", Font.PLAIN, 13));
		GridBagConstraints gbc_btnSchedule = new GridBagConstraints();
		gbc_btnSchedule.anchor = GridBagConstraints.WEST;
		gbc_btnSchedule.insets = new Insets(0, 0, 0, 5);
		gbc_btnSchedule.gridx = 0;
		gbc_btnSchedule.gridy = 6;
		frame.getContentPane().add(btnSchedule, gbc_btnSchedule);
		
		
	}
}
