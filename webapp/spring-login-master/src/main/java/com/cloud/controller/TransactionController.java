package com.cloud.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloud.constants.CommonConstants;
import com.cloud.model.Attachment;
import com.cloud.model.AttachmentWrapper;
import com.cloud.model.Status;
import com.cloud.model.Transaction;
import com.cloud.model.TransactionWrapper;
import com.cloud.model.User;
import com.cloud.service.BaseClient;
import com.cloud.service.TransactionService;
import com.cloud.service.UserService;
import com.cloud.util.Utils;

@RestController
public class TransactionController {

	//private static final //logger //logger = LogManager.get//logger(TransactionController.class);
	
	@Autowired
	private TransactionService transactionService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private BaseClient baseClient;


	/**
	 * Gets the user's transaction
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/transaction", method = RequestMethod.GET)
	@ResponseBody
	public TransactionWrapper findByUserId(HttpServletResponse response) throws IOException {
		
		//logger.info("Find Transactions by User : Start");
		
		TransactionWrapper transactions = new TransactionWrapper();
		
		// Fetches the current user name who is logged in
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			User user = userService.findUserByEmail(auth.getName());
			List<Transaction> transactionList = transactionService.findByUserId(user.getId());
			transactions.setTransactions(transactionList);
			transactions.setStatusCode(CommonConstants.StatusCodes.SUCCESS);
			transactions.setMessage(CommonConstants.SUCCESS);
		} catch (Exception e) {
			//logger.error("Get all transactions for user failed");
			transactions.setStatusCode(CommonConstants.StatusCodes.GET_ALL_TRANSACTIONS_FAILURE);
			transactions.setMessage(CommonConstants.GET_ALL_TRANSACTION_FAILURE + ":" + e.getMessage());
		}

		//logger.info("Find Transactions by User : End");
		
		return transactions;
	}
	
	/**
	 * Create the transaction for the logged in user
	 * 
	 * @return String
	 * @throws IOException
	 */
	@RequestMapping(value = "/transaction", method = RequestMethod.POST)
	@ResponseBody
	public Status create(@RequestBody Transaction transaction, HttpServletResponse response) throws IOException {

		//logger.info("Create Transaction - Start");

		Status status = new Status();

		try {
			if (Utils.validateDate(transaction.getDate().toString())) {
				// Fetches the current user name who is logged in
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();

				User user = userService.findUserByEmail(auth.getName());
				transaction.setUser(user);
				// Setting an empty attachment
				transaction.setAttachments(null);
				SimpleDateFormat sf = new SimpleDateFormat(transaction.getDate().toString());
				transaction.setDate(sf.format(new Date()));
				transactionService.save(transaction);
				status.setStatusCode(CommonConstants.StatusCodes.TRANSACTION_SUCCESS);
				status.setMessage(CommonConstants.SUCCESS);

			} else {
				//logger.info("Invalid Date format");
				status.setStatusCode(CommonConstants.StatusCodes.INVALID_DATE_FORMAT);
				status.setMessage(CommonConstants.INVALID_DATE_FORMAT);
			}
		} catch (Exception e) {
			//logger.error("Create transaction failed");
			status.setStatusCode(CommonConstants.StatusCodes.TRANSACTION_CREATION_FAILURE);
			status.setMessage(CommonConstants.TRANSACTION_FAILURE + ":" + e.getMessage());
		}

		//logger.info("Create Transaction - End");

		return status;
	}

	/**
	 * Create the transaction for the logged in user
	 * 
	 * @return String
	 * @throws IOException
	 */
	@RequestMapping(value = "/transaction/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public Status update(@PathVariable String id, @RequestBody Transaction transaction, HttpServletResponse response)
			throws IOException {
		
		//logger.info("Update Transaction - Start");
		
		Status status = new Status();
		// Fetches the current user name who is logged in
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		try {
			Transaction actualTransaction = transactionService.find(id);

			// Check if the user owns the transaction
			if (actualTransaction.getUser().getEmail().equalsIgnoreCase(auth.getName())) {
				// Update the transaction with the new values
				actualTransaction = this.setTransactionData(transaction, actualTransaction);
				transactionService.save(actualTransaction);
				status.setStatusCode(CommonConstants.StatusCodes.TRANSACTION_SUCCESS);
				status.setMessage(CommonConstants.SUCCESS);
			} else {
				//logger.info("Unauthorized user");
				status.setStatusCode(CommonConstants.StatusCodes.UNAUTHORIZED);
				status.setMessage(CommonConstants.UNAUTHORIZED);
			}
		} catch (Exception e) {
			//logger.error("Update transaction failed");
			status.setStatusCode(CommonConstants.StatusCodes.TRANSACTION_UPDATION_FAILURE);
			status.setMessage(CommonConstants.TRANSACTION_FAILURE + " : " + e.getMessage());
		}

		//logger.info("Update Transaction - End");
		
		return status;
	}

	/**
	 * Deletes the transaction
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/transaction/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Status delete(@PathVariable String id, HttpServletResponse response) throws IOException {
		
		//logger.info("Delete Transaction - Start");
		
		Status status = new Status();
		// Fetches the current user name who is logged in
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		try {
			Transaction transaction = transactionService.find(id);

			if (transaction.getUser().getEmail().equalsIgnoreCase(auth.getName())) {
				transactionService.deleteById(id);
				status.setStatusCode(CommonConstants.StatusCodes.DELETION_SUCCESS);
				status.setMessage(CommonConstants.SUCCESS);
			} else {
				//logger.info("Unauthorized user");
				status.setStatusCode(CommonConstants.StatusCodes.UNAUTHORIZED);
				status.setMessage(CommonConstants.UNAUTHORIZED);
			}
		} catch (Exception e) {
			//logger.error("Delete transactions failed");
			status.setStatusCode(CommonConstants.StatusCodes.TRANSACTION_DELETION_FAILURE);
			status.setMessage(CommonConstants.TRANSACTION_DELETION_FAILURE + ":" + e.getMessage());
		}

		//logger.info("Delete Transaction - End");
		
		return status;

	}
	
	/**
	 * Added to upload a receipt to a transaction
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/transaction/{id}/attachments", method = RequestMethod.GET)
	public AttachmentWrapper getReceipt(@PathVariable String id, HttpServletResponse response) throws IOException {

		//logger.info("Get Transaction Receipt with id : " + id + "Start");

		// Fetches the current user name who is logged in
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		AttachmentWrapper attachmentWrapper = new AttachmentWrapper();
		List<Attachment> attachmentList = null;

		// Fetches all the attachments in the transaction
		try {
			Transaction transaction = transactionService.find(id);
			if (transaction.getUser().getEmail().equalsIgnoreCase(auth.getName())) {
				attachmentList = transaction.getAttachments();
				attachmentWrapper.setAttachments(attachmentList);
				attachmentWrapper.setMessage(CommonConstants.SUCCESS);
				attachmentWrapper.setStatusCode(CommonConstants.StatusCodes.SUCCESS);
			} else {
				//logger.info("Unauthorized user");
				attachmentWrapper.setStatusCode(CommonConstants.StatusCodes.UNAUTHORIZED);
				attachmentWrapper.setMessage(CommonConstants.UNAUTHORIZED);
			}

		} catch (Exception e) {
			//logger.error("Get transaction receipts failed");
			attachmentWrapper.setStatusCode(CommonConstants.StatusCodes.GET_ATTACHMENT_FAILURE);
			attachmentWrapper.setMessage(CommonConstants.GET_ATTACHMENTS_FAILURE + ":" + e.getMessage());
		}

		//logger.info("Get Transaction Receipt with id : " + id + "- End");

		return attachmentWrapper;
	}
	
	/**
	 * Added to upload a receipt to a transaction
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/transaction/{id}/attachments", method = RequestMethod.POST)
	public Status uploadReceipt(@PathVariable String id, @RequestPart(value = "file") MultipartFile file) {

		//logger.info("Attach Transaction Receipt with id : " + id + " - Start");

		// Fetches the current user name who is logged in
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		Status status = new Status();

		try {
			if (Utils.isValidExt(file)) {
				Transaction transaction = transactionService.find(id);
				if (transaction.getUser().getEmail().equalsIgnoreCase(auth.getName())) {
					// Upload the receipt
					String uri = baseClient.uploadFile(file);

					// Save the metadata of the receipt in the database attachment table
					transactionService.saveAttachment(id, uri);
					status.setMessage(uri);
					status.setStatusCode(CommonConstants.StatusCodes.SUCCESS);
				} else {
					//logger.info("Unauthorized user");
					status.setStatusCode(CommonConstants.StatusCodes.UNAUTHORIZED);
					status.setMessage(CommonConstants.UNAUTHORIZED);
				}

			} else {
				status.setStatusCode(CommonConstants.StatusCodes.INVALID_ATTACHMENT);
				status.setMessage(CommonConstants.INVALID_ATTACHMENT);
				//logger.error("Invlaid file extension");
			}

		} catch (Exception e) {

			status.setStatusCode(CommonConstants.StatusCodes.INVALID_ATTACHMENT);
			status.setMessage(CommonConstants.UPLOAD_ATTACHMENTS_FAILURE + e.getMessage());
			//logger.error("Error while attaching the receipt");
		}

		//logger.info("Attach Transaction Receipt with id : " + id + " - End");

		return status;
	}
	
	/**
	 * Added to update a receipt to a transaction
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/transaction/{id}/attachments/{attachmentId}", method = RequestMethod.PUT)
	public Status updateReceipt(@PathVariable String id, @PathVariable String attachmentId,
			@RequestPart(value = "file") MultipartFile file) {

		//logger.info("Attach Transaction Receipt with id : " + id + " - Start");

		// Fetches the current user name who is logged in
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		Status status = new Status();
		boolean receiptPresent = false;

		try {
			if (Utils.isValidExt(file)) {
				// Check if attachment is present
				Transaction transaction = transactionService.find(id);
				if (transaction.getUser().getEmail().equalsIgnoreCase(auth.getName())) {

					Attachment oldAttachment = null;
					for (Attachment attachment : transaction.getAttachments()) {
						if (attachment.getId().toString().equals(attachmentId)) {
							receiptPresent = true;
							oldAttachment = attachment;
							break;
						}
					}

					if (receiptPresent) {
						// Delete the existing file
						baseClient.deleteFile(oldAttachment.getUri());
						// Upload the receipt
						String uri = baseClient.uploadFile(file);
						oldAttachment.setUri(uri);
						// Save the metadata of the receipt in the database attachment table
						transactionService.save(oldAttachment);
						status.setMessage(uri);
						status.setStatusCode(CommonConstants.StatusCodes.SUCCESS);

					} else {
						//logger.info("Receipt not present for the transaction");
						status.setMessage(CommonConstants.ATTACHMENTS_NOT_PRESENT);
						status.setStatusCode(CommonConstants.StatusCodes.ATTACHMENT_NOT_PRESENT);
					}
				} else {
					//logger.info("Unauthorized user");
					status.setStatusCode(CommonConstants.StatusCodes.UNAUTHORIZED);
					status.setMessage(CommonConstants.UNAUTHORIZED);
				}
			} else {
				status.setStatusCode(CommonConstants.StatusCodes.INVALID_ATTACHMENT);
				status.setMessage(CommonConstants.INVALID_ATTACHMENT);
				//logger.error("Invlaid file extension");
			}

		} catch (Exception e) {

			status.setStatusCode(CommonConstants.StatusCodes.UPLOAD_ATTACHMENT_FAILURE);
			status.setMessage(CommonConstants.UPLOAD_ATTACHMENTS_FAILURE + e.getMessage());
			//logger.error("Error while attaching the receipt");
		}

		//logger.info("Attach Transaction Receipt with id : " + id + " - End");

		return status;
	}

	/**
	 * Added to delete a receipt to a transaction
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/transaction/{id}/attachments/{attachmentId}", method = RequestMethod.DELETE)
	public Status deleteAttachment(@PathVariable String id, @PathVariable String attachmentId,
			HttpServletResponse response) throws IOException {

		//logger.info("Delete Transaction Receipt with id : " + id + "- Start");

		// Fetches the current user name who is logged in
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		Status status = new Status();
		boolean receiptPresent = false;

		// Save the metadata of the receipt in the database attachment table
		try {

			Transaction transaction = transactionService.find(id);
			if (transaction.getUser().getEmail().equalsIgnoreCase(auth.getName())) {
				Attachment oldAttachment = null;
				for (Attachment attachment : transaction.getAttachments()) {
					if (attachment.getId().toString().equals(attachmentId)) {
						receiptPresent = true;
						oldAttachment = attachment;
						break;
					}
				}

				if (receiptPresent) {
					String result = baseClient.deleteFile(oldAttachment.getUri());
					transactionService.deleteAttachment(id, oldAttachment.getUri());
					status.setMessage(result);
					status.setStatusCode(CommonConstants.StatusCodes.DELETION_SUCCESS);
				} else {
					//logger.info("Receipt not present for the transaction");
					status.setMessage(CommonConstants.ATTACHMENTS_NOT_PRESENT);
					status.setStatusCode(CommonConstants.StatusCodes.ATTACHMENT_NOT_PRESENT);
				}
			} else {
				//logger.info("Unauthorized user");
				status.setStatusCode(CommonConstants.StatusCodes.UNAUTHORIZED);
				status.setMessage(CommonConstants.UNAUTHORIZED);
			}

		} catch (Exception e) {

			status.setStatusCode(CommonConstants.StatusCodes.ATTACHMENT_DELETION_FAILURE);
			status.setMessage(CommonConstants.DELETE_ATTACHMENTS_FAILURE + e.getMessage());
			//logger.error("Error while deleting a receipt");
		}

		//logger.info("Delete Transaction Receipt with id : " + id + "- End");

		return status;

	}
	
	/**
	 * Added to set the Transaction data
	 * @param transaction
	 * @param actualTransaction
	 * @return
	 */
	private Transaction setTransactionData(Transaction transaction, Transaction actualTransaction) {

		//logger.info("Set Transaction Data - Start");
		
		actualTransaction.setAmount(transaction.getAmount());
		actualTransaction.setDate(transaction.getDate());
		actualTransaction.setCategory(transaction.getCategory());
		actualTransaction.setDescription(transaction.getDescription());
		actualTransaction.setMerchant(transaction.getMerchant());

		//logger.info("Set Transaction Data - End");
		
		return actualTransaction;
	}


}