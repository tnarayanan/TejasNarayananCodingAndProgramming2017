package Attendance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFormattedTextField.AbstractFormatter;

/**
 * Formats date for DatePicker
 * @author Tejas
 *
 */

public class DateLabelFormatter extends AbstractFormatter {

	private static final long serialVersionUID = 1L;
	private String datePattern = "MM/dd/yyyy";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern); // Sets format

    
    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parseObject(text); // Returns parsed version of input
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime()); // Returns string version of input
        }

        return "";
    }

}
