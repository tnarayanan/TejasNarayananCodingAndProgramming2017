package Main;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Custom class to generate dialogs easily - either error/message dialogs (ok), or option panes (yes/no).
 * Each dialog contains the House of Fun logo, a title, a message, and buttons.
 * @author Tejas
 *
 */
public class CustomDialog {
	private JFrame frame;
	private String title;
	private String message;
	
	private ImageIcon houseIcon;
	
	/**
	 * Constructor for CustomDialog. Contains JFrame for displaying the message, a title, and a message
	 * @param frame
	 * @param title
	 * @param message
	 */
	public CustomDialog(JFrame frame, String title, String message) {
		this.title = title;
		this.message = message;
		this.frame = frame;
	}
	
	/**
	 * Initializes the icon to add to each dialog
	 */
	
	private void initializeImage() {
		Image houseImg = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/house.png"));
		
		Image resizedHouseImage = houseImg.getScaledInstance(75, 75, 0);
		
		houseIcon = new ImageIcon(resizedHouseImage);
	}

	/**
	 * Show an error message
	 */
	
	public void showError() {
		initializeImage();
		JOptionPane.showMessageDialog(frame, message, title, JOptionPane.ERROR_MESSAGE, houseIcon);
	}
	
	/**
	 * Show a yes/no dialog
	 * @return JOptionPane.YES_OPTION or JOptionPane.NO_OPTION
	 */
	
	public int showOptionPane() {
		initializeImage();
		return JOptionPane.showConfirmDialog(frame, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, houseIcon);
	}
}
