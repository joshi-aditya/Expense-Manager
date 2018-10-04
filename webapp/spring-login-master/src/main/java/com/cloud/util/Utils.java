package com.cloud.util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	/**
	 * Added to validate the date format 
	 * @return
	 */
	public static boolean validateDate(String date)
	{
		boolean isValid = false;

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String dateInString = "07/06/2013";

		try {

			Date formattedDate = formatter.parse(dateInString);
			formatter.format(formattedDate);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return isValid;
	}
}
