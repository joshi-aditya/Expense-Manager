package com.cloud.util;

import org.springframework.web.multipart.MultipartFile;

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
	
	/**
	 * Added to generate a file name without spaces
	 * @param multiPart
	 * @return
	 */
	 public static String generateFileName(MultipartFile multiPart) {
	        return multiPart.getOriginalFilename().replace(" ", "_");
	    }
}
