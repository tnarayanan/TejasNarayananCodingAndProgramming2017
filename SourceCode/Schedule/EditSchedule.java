package Schedule;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Main.LoginScreen;

/**
 * This is the screen where users can edit employee schedules.
 * @author Tejas
 *
 */
public class EditSchedule {

	public JFrame frame;
	private final String[] jobs = {"-----------------", "Cleaning", "Cooking", "Entrance Desk", "Gift Counter", "Entertainer", "Off Duty"};
	
	// Dropdown boxes (ComboBoxes) for each hour
	
	private JComboBox<?> comboBox9;
	private JComboBox<?> comboBox10;
	private JComboBox<?> comboBox11;
	private JComboBox<?> comboBox12;
	private JComboBox<?> comboBox1;
	private JComboBox<?> comboBox2;
	private JComboBox<?> comboBox3;
	private JComboBox<?> comboBox4;
	private JComboBox<?> comboBox5;
	private JComboBox<?> comboBox6;
	
	private JButton btnCancel;
	private JButton btnDone;
	

	/**
	 * Create the application.
	 */
	Connection conn = null;
	public EditSchedule(int eid, String date) {
		conn = LoginScreen.conn;
		initialize(eid, date);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(int eid, String date) {
		
		// Initial setup
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 330);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{153, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		
		
		// Add elements to JFrame
		
		addElements();
		
		
		
		// Add the existing schedule entries from the database into the fields
		
		String getQuery = "select * from Schedule where Schedule.eid = " + eid + " and Schedule.date = '" + date + "'";
		
		try {
			PreparedStatement pst =  conn.prepareStatement(getQuery);
			ResultSet rs = pst.executeQuery();
			
			comboBox9.setSelectedItem(rs.getString(3));
			comboBox10.setSelectedItem(rs.getString(4));
			comboBox11.setSelectedItem(rs.getString(5));
			comboBox12.setSelectedItem(rs.getString(6));
			comboBox1.setSelectedItem(rs.getString(7));
			comboBox2.setSelectedItem(rs.getString(8));
			comboBox3.setSelectedItem(rs.getString(9));
			comboBox4.setSelectedItem(rs.getString(10));
			comboBox5.setSelectedItem(rs.getString(11));
			comboBox6.setSelectedItem(rs.getString(12));

			pst.close();
			rs.close();
		} catch (SQLException e) {
			/*
			 * If the program reaches this empty catch, the record being edited does not yet exist in the
			 * database. It will be added, rather than updated. One way of addressing this would be to have
			 * the program check whether the record exists, then update the fields if it does. However, having 
			 * an empty catch block is more time efficient, as the program does not need to execute another
			 * SQL query.
			 */
		}

		// When cancel button clicked
		
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Schedule sc = new Schedule();
				sc.frame.setVisible(true);
				frame.setVisible(false);
			}
		});
		

		
		// When done button clicked
		
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				/*
				 * The unique key for the database is the combination of the EID and the date.
				 * There can only be one row for a certain date and EID, as there can only be 
				 * one schedule per person per day.
				 * 
				 * Insert or replace adds a row to the database if the EID-date pairing does not exist,
				 * and replaces the current entry if it does. This is more efficient than manually
				 * checking to see if the pairing exists or not, then replacing or adding a row respectively.
				 */
				
				String querySubmit = "insert or replace into Schedule values (" + eid
						+ ", '" + date
						+ "', '" + comboBox9.getSelectedItem().toString() 
						+ "', '" + comboBox10.getSelectedItem().toString()
						+ "', '" + comboBox11.getSelectedItem().toString()
						+ "', '" + comboBox12.getSelectedItem().toString()
						+ "', '" + comboBox1.getSelectedItem().toString() 
						+ "', '" + comboBox2.getSelectedItem().toString() 
						+ "', '" + comboBox3.getSelectedItem().toString() 
						+ "', '" + comboBox4.getSelectedItem().toString() 
						+ "', '" + comboBox5.getSelectedItem().toString() 
						+ "', '" + comboBox6.getSelectedItem().toString() + "')";
				System.out.println(date);
				
				try {
					PreparedStatement pst = conn.prepareStatement(querySubmit);
					pst.executeUpdate();
					pst.close();

				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				Schedule schedule = new Schedule(); // Goes back to the schedule screen
				schedule.frame.setVisible(true);
				frame.setVisible(false);
			}
		});
		
	}
	
	private void addElements() {
		
		// 9:00
		
		JLabel lbl9 = new JLabel("9:00 AM");
		lbl9.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_lbl9 = new GridBagConstraints();
		gbc_lbl9.insets = new Insets(0, 0, 5, 5);
		gbc_lbl9.gridx = 0;
		gbc_lbl9.gridy = 0;
		frame.getContentPane().add(lbl9, gbc_lbl9);
		
		comboBox9 = new JComboBox<Object>(jobs);
		comboBox9.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_comboBox9 = new GridBagConstraints();
		gbc_comboBox9.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox9.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox9.gridx = 0;
		gbc_comboBox9.gridy = 1;
		frame.getContentPane().add(comboBox9, gbc_comboBox9);
		
		// 10:00
		
		JLabel label10 = new JLabel("10:00 AM");
		label10.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_label10 = new GridBagConstraints();
		gbc_label10.insets = new Insets(0, 0, 5, 5);
		gbc_label10.gridx = 0;
		gbc_label10.gridy = 2;
		frame.getContentPane().add(label10, gbc_label10);
		
		comboBox10 = new JComboBox<Object>(jobs);
		comboBox10.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_comboBox10 = new GridBagConstraints();
		gbc_comboBox10.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox10.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox10.gridx = 0;
		gbc_comboBox10.gridy = 3;
		frame.getContentPane().add(comboBox10, gbc_comboBox10);
		
		// 11:00
		
		JLabel label11 = new JLabel("11:00 AM");
		label11.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_label11 = new GridBagConstraints();
		gbc_label11.insets = new Insets(0, 0, 5, 5);
		gbc_label11.gridx = 0;
		gbc_label11.gridy = 4;
		frame.getContentPane().add(label11, gbc_label11);
		
		comboBox11 = new JComboBox<Object>(jobs);
		comboBox11.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_comboBox11 = new GridBagConstraints();
		gbc_comboBox11.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox11.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox11.gridx = 0;
		gbc_comboBox11.gridy = 5;
		frame.getContentPane().add(comboBox11, gbc_comboBox11);
		
		// 12:00
		
		JLabel label12 = new JLabel("12:00 PM");
		label12.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_label12 = new GridBagConstraints();
		gbc_label12.insets = new Insets(0, 0, 5, 5);
		gbc_label12.gridx = 0;
		gbc_label12.gridy = 6;
		frame.getContentPane().add(label12, gbc_label12);
		
		comboBox12 = new JComboBox<Object>(jobs);
		comboBox12.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_comboBox12 = new GridBagConstraints();
		gbc_comboBox12.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox12.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox12.gridx = 0;
		gbc_comboBox12.gridy = 7;
		frame.getContentPane().add(comboBox12, gbc_comboBox12);
		
		// 1:00
		
		JLabel lbl1 = new JLabel("1:00 PM");
		lbl1.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_lbl1 = new GridBagConstraints();
		gbc_lbl1.insets = new Insets(0, 0, 5, 5);
		gbc_lbl1.gridx = 0;
		gbc_lbl1.gridy = 8;
		frame.getContentPane().add(lbl1, gbc_lbl1);
		
		comboBox1 = new JComboBox<Object>(jobs);
		comboBox1.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_comboBox1 = new GridBagConstraints();
		gbc_comboBox1.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox1.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox1.gridx = 0;
		gbc_comboBox1.gridy = 9;
		frame.getContentPane().add(comboBox1, gbc_comboBox1);
		
		// 2:00
		
		JLabel lbl2 = new JLabel("2:00 PM");
		lbl2.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_lbl2 = new GridBagConstraints();
		gbc_lbl2.insets = new Insets(0, 0, 5, 0);
		gbc_lbl2.gridx = 1;
		gbc_lbl2.gridy = 0;
		frame.getContentPane().add(lbl2, gbc_lbl2);
		
		comboBox2 = new JComboBox<Object>(jobs);
		comboBox2.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_comboBox2 = new GridBagConstraints();
		gbc_comboBox2.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox2.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox2.gridx = 1;
		gbc_comboBox2.gridy = 1;
		frame.getContentPane().add(comboBox2, gbc_comboBox2);
		
		// 3:00
		
		JLabel lbl3 = new JLabel("3:00 PM");
		lbl3.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_lbl3 = new GridBagConstraints();
		gbc_lbl3.insets = new Insets(0, 0, 5, 0);
		gbc_lbl3.gridx = 1;
		gbc_lbl3.gridy = 2;
		frame.getContentPane().add(lbl3, gbc_lbl3);
		
		comboBox3 = new JComboBox<Object>(jobs);
		comboBox3.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_comboBox3 = new GridBagConstraints();
		gbc_comboBox3.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox3.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox3.gridx = 1;
		gbc_comboBox3.gridy = 3;
		frame.getContentPane().add(comboBox3, gbc_comboBox3);
		
		// 4:00
		
		JLabel lbl4 = new JLabel("4:00 PM");
		lbl4.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_lbl4 = new GridBagConstraints();
		gbc_lbl4.insets = new Insets(0, 0, 5, 0);
		gbc_lbl4.gridx = 1;
		gbc_lbl4.gridy = 4;
		frame.getContentPane().add(lbl4, gbc_lbl4);
		
		comboBox4 = new JComboBox<Object>(jobs);
		comboBox4.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_comboBox4 = new GridBagConstraints();
		gbc_comboBox4.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox4.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox4.gridx = 1;
		gbc_comboBox4.gridy = 5;
		frame.getContentPane().add(comboBox4, gbc_comboBox4);
		
		// 5:00
		
		JLabel lbl5 = new JLabel("5:00 PM");
		lbl5.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_lbl5 = new GridBagConstraints();
		gbc_lbl5.insets = new Insets(0, 0, 5, 0);
		gbc_lbl5.gridx = 1;
		gbc_lbl5.gridy = 6;
		frame.getContentPane().add(lbl5, gbc_lbl5);
		
		comboBox5 = new JComboBox<Object>(jobs);
		comboBox5.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_comboBox5 = new GridBagConstraints();
		gbc_comboBox5.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox5.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox5.gridx = 1;
		gbc_comboBox5.gridy = 7;
		frame.getContentPane().add(comboBox5, gbc_comboBox5);
		
		// 6:00
		
		JLabel lbl6 = new JLabel("6:00 PM");
		lbl6.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_lbl6 = new GridBagConstraints();
		gbc_lbl6.insets = new Insets(0, 0, 5, 0);
		gbc_lbl6.gridx = 1;
		gbc_lbl6.gridy = 8;
		frame.getContentPane().add(lbl6, gbc_lbl6);
		
		comboBox6 = new JComboBox<Object>(jobs);
		comboBox6.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_comboBox6 = new GridBagConstraints();
		gbc_comboBox6.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox6.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox6.gridx = 1;
		gbc_comboBox6.gridy = 9;
		frame.getContentPane().add(comboBox6, gbc_comboBox6);
		
		// Cancel button
		
		btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.insets = new Insets(0, 0, 0, 5);
		gbc_btnCancel.gridx = 0;
		gbc_btnCancel.gridy = 10;
		frame.getContentPane().add(btnCancel, gbc_btnCancel);
		
		// Done button
		
		btnDone = new JButton("Done");
		btnDone.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_btnDone = new GridBagConstraints();
		gbc_btnDone.gridx = 1;
		gbc_btnDone.gridy = 10;
		frame.getContentPane().add(btnDone, gbc_btnDone);
	}
	
	

}
