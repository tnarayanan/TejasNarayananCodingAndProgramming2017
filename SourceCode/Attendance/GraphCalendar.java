package Attendance;

import java.awt.Color;
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

import javax.swing.JButton;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import Main.CustomDialog;
import Main.LoginScreen;

/**
 * This is the screen where users can view Attendance Graphs.
 * @author Tejas
 *
 */
public class GraphCalendar {

	public JFrame frame;
	public ChartPanel chartPanel;
	
	private JButton btnPrint;
	private JButton btnBack;
	
	private CategoryDataset dataset;
	private JFreeChart chart;
	


	/**
	 * Create the application.
	 */
	Connection conn = null;
	public GraphCalendar(String dateStr) {
		conn = LoginScreen.conn;
		initialize(dateStr);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String dateStr) {
		
		// Initial setup
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{279, 294, 0};
		gridBagLayout.rowHeights = new int[]{220, 29, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);

		
		
		// Creates the dataset for the graph
		
		dataset = createDataset(dateStr);
		
		
		
		// Creates the graph using the dataset
		
		chart = createChart(dataset, dateStr);
		
		
		
		// Add elements to JFrame, including adding the chart to the ChartPanel
		
		addElements();
		
		
		
		// When back button clicked
		
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Attendance attendance = new Attendance();
				attendance.frame.setVisible(true);
				frame.setVisible(false);
			}
		});
		
		
		
		// When print button clicked
		
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printComponent();
			}
		});
	}
	
	/**
	 * Creates the dataset for the given date by accessing the database
	 * @param dateStr
	 * @return
	 */
	
	private CategoryDataset createDataset(String dateStr) {
        
        // row keys
        final String series1 = "Attendance";

        // column keys
        final String category1 = "9:00";
        final String category2 = "10:00";
        final String category3 = "11:00";
        final String category4 = "12:00";
        final String category5 = "1:00";
        final String category6 = "2:00";
        final String category7 = "3:00";
        final String category8 = "4:00";
        final String category9 = "5:00";
        final String category10 = "6:00";

        // create the dataset by selecting the times where the date values match
        
        String query = "select Time from Attendance where Attendance.Date = \"" + dateStr + "\"";
        
        int[] counter = new int[10]; // How many records in each hour
        PreparedStatement pst;
        ResultSet rs;
		try {
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			
			while(rs.next()) { // While there are more entries
				int time = Integer.parseInt(rs.getString(1)); // Get the time in seconds
				
				/*
				 * Gets the index (hour) that the time would fall into,
				 * and account for the 9 hours before 9:00 AM by subtracting 9
				 */
				int index = ((time - (time % 3600)) / 3600) - 9;
				if (index >= 0 && index <= 9) { // If the time falls between 9:00 AM and 6:00 PM
					counter[index] ++; // Add to the data
				}
				//i++;
			}
			
			pst.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Adds the final values to the dataset
		
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(counter[0], series1, category1);
        dataset.addValue(counter[1], series1, category2);
        dataset.addValue(counter[2], series1, category3);
        dataset.addValue(counter[3], series1, category4);
        dataset.addValue(counter[4], series1, category5);
        dataset.addValue(counter[5], series1, category6);
        dataset.addValue(counter[6], series1, category7);
        dataset.addValue(counter[7], series1, category8);
        dataset.addValue(counter[8], series1, category9);
        dataset.addValue(counter[9], series1, category10);
        
        return dataset;
        
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset
     * 
     * @return The chart.
     */
    private JFreeChart createChart(final CategoryDataset dataset, String dateStr) {
        
        // create the chart
        final JFreeChart chart = ChartFactory.createBarChart(
	        "Visitor Attendance for " + dateStr,	// chart title
	        "Time",               					// domain axis label
	        "Visitor Attendance",              		// range axis label
	        dataset,                  				// data
	        PlotOrientation.VERTICAL, 				// orientation
	        false,                     				// include legend
	        true,                     				// tooltips?
	        false                     				// URLs?
        );


        // set the background color for the chart
        chart.setBackgroundPaint(Color.WHITE);

        // Set colors of the plot
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        // set the range axis to display integers only
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        
        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        
        return chart; // returns the JFreeChart to display
        
    }
    
    /**
     * Prints the frame (graph) by opening the computer's default print dialog.
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
				chartPanel.paint(g2); // paint the chartPanel
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
    	
    	// Panel for the Chart
    	
    	chartPanel = new ChartPanel(chart);
		GridBagConstraints gbc_chartPanel = new GridBagConstraints();
		gbc_chartPanel.gridwidth = 2;
		gbc_chartPanel.fill = GridBagConstraints.BOTH;
		gbc_chartPanel.insets = new Insets(0, 0, 5, 0);
		gbc_chartPanel.gridx = 0;
		gbc_chartPanel.gridy = 0;
		frame.getContentPane().add(chartPanel, gbc_chartPanel);
		
		// GridBagLayout of the panel
		
		GridBagLayout gbl_chartPanel = new GridBagLayout();
		gbl_chartPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_chartPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_chartPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_chartPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		chartPanel.setLayout(gbl_chartPanel);
		
		// Back button
		
		btnBack = new JButton("Back");
		btnBack.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_btnBack = new GridBagConstraints();
		gbc_btnBack.anchor = GridBagConstraints.NORTH;
		gbc_btnBack.insets = new Insets(0, 0, 0, 5);
		gbc_btnBack.gridx = 0;
		gbc_btnBack.gridy = 1;
		frame.getContentPane().add(btnBack, gbc_btnBack);
		
		// Print button
		
		btnPrint = new JButton("Print");
		btnPrint.setFont(new Font("Lucida Handwriting", Font.PLAIN, 11));
		GridBagConstraints gbc_btnPrint = new GridBagConstraints();
		gbc_btnPrint.anchor = GridBagConstraints.NORTH;
		gbc_btnPrint.gridx = 1;
		gbc_btnPrint.gridy = 1;
		frame.getContentPane().add(btnPrint, gbc_btnPrint);
    }

}
