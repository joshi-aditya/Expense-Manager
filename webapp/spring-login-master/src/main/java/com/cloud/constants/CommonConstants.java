package com.cloud.constants;

public class CommonConstants {

	/* Success */
	public static String SUCCESS = "Success";
	
	/* User already exists in the database */
	public static String USER_ALREADY_EXISTS = "There is already a user registered with the email provided";
	
	/* User registered successfully */
	public static String USER_REGISTERATION_SUCCESS = "User has been registered successfully";

	/* Transaction created successfully */
	public static String TRANSACTION_CREATED = "Transaction has been created successfully";

	/* Transaction updated successfully */
	public static String TRANSACTION_UPDATED = "Transaction has been modified successfully";
	
	/* Transaction failure */
	public static String TRANSACTION_FAILURE = "Transaction creation/updation failed";
	
	/* Invalid Date format */
	public static String INVALID_DATE_FORMAT = "Invalid date format. Please enter the date in the format : MM/DD/YYYY";
	
	/* Unauthorized update of transaction */
	public static String UNAUTHORIZED = "User is not authorized to update the transaction";
	
	/* Transaction deleted successfully */
	public static String TRANSACTION_DELETED = "Transaction has been deleted successfully";
	
	/* Transaction deletion failure */
	public static String TRANSACTION_DELETION_FAILURE = "Transaction deletion failed";
	
	/* Get transaction failure */
	public static String GET_ALL_TRANSACTION_FAILURE = "Get transaction failed";
	
	/* Get transaction failure */
	public static String GET_ATTACHMENTS_FAILURE = "Get attachments for transaction failed";
	
	/* Upload attachments failure */
	public static String UPLOAD_ATTACHMENTS_FAILURE = "Upload attachment for transaction failed";
	
	/* Upload attachments failure */
	public static String INVALID_ATTACHMENT = "Invalid attachment extension";
	
	/* Delete attachments failure */
	public static String DELETE_ATTACHMENTS_FAILURE = "Delete attachment for transaction failed";
	
	/* Attachment deleted successfully */
	public static String DELETE_ATTACHMENTS_SUCCESS = "Deleted Successfully";
	
	/* Attachment not present */
	public static String ATTACHMENTS_NOT_PRESENT = "Receipt not present for the transaction";
	
	public static interface StatusCodes
	{
		public static String SUCCESS = "200";
		
		public static String FAILURE = "500";
		
		//User Failures
		
		public static String USER_REGISTRATION_FAILURE = "601";
		
		public static String UNAUTHORIZED = "401";
		
		public static String DELETION_SUCCESS = "204";
		
		//Transaction Failures
		
		public static String TRANSACTION_SUCCESS = "201";
		
		public static String TRANSACTION_CREATION_FAILURE = "701";
		
		public static String TRANSACTION_UPDATION_FAILURE = "702";
		
		public static String TRANSACTION_DELETION_FAILURE = "703";
		
		public static String GET_ALL_TRANSACTIONS_FAILURE = "704";
		
		//Attachment Failure
		
		public static String GET_ATTACHMENT_FAILURE = "801";
		
		public static String UPLOAD_ATTACHMENT_FAILURE = "802";
		
		public static String ATTACHMENT_DELETION_FAILURE = "803";
		
		public static String ATTACHMENT_NOT_PRESENT = "804";
		
		public static String INVALID_ATTACHMENT = "805";
		
		//Common Failure
		
		public static String INVALID_DATE_FORMAT = "901";
		
		public static String INVALID_EMAIL = "902";
		
		public static String INVALID_AMOUNT = "903";
		
		
	}
}