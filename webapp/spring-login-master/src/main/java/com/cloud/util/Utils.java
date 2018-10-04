package com.cloud.util;

public class Utils {

	/**
	 * Added to validate the date format
	 * 
	 * @return
	 */
	public static boolean validateDate(String date) {
		boolean isValid = false;

		if (date.matches("\\d{2}/\\d{2}/\\d{4}")) {
			isValid = true;
		}

		return isValid;
	}
}
