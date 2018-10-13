package com.cloud.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.model.Attachment;
import com.cloud.model.Transaction;
import com.cloud.repository.AttachmentRepository;
import com.cloud.repository.TransactionRepository;


@Service("transactionService")
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired 
	private AttachmentRepository attachmentRepository;

	/**
	 * Create a new transaction for a User
	 * 
	 * @param transaction
	 * @throws Exception
	 */
	public void save(Transaction transaction) throws Exception {

		transactionRepository.save(transaction);
	}

	/**
	 * Find a transaction for a User
	 * 
	 * @param id
	 * @throws Exception
	 */
	public Transaction find(String id) throws Exception {

		return transactionRepository.findByTransactionId(UUID.fromString(id));

	}

	/**
	 * Deletes the transaction by transaction Id
	 * @param id
	 */
	public void deleteById(String id) {
		transactionRepository.deleteById(UUID.fromString(id));

	}

	/**
	 * Fetches all the transaction for the logged in user
	 * 
	 * @param userId
	 * @return
	 */
	public List<Transaction> findByUserId(int userId) {
		List<Transaction> transactions = transactionRepository.findByUserId(userId);
		return transactions;
	}
	
	/**
	 * Saves the attachment to the given transaction
	 * @param transactionId
	 * @param uri
	 * @throws Exception 
	 */
	public void saveAttachment(String transactionId, String uri) throws Exception
	{
		//Update the attach with the attachment
		Transaction transaction = this.find(transactionId);
		attachmentRepository.save(new Attachment(uri, transaction));	
	}
	
	/**
	 * Save the attachment for a transaction
	 * 
	 * @param transaction
	 * @throws Exception
	 */
	public void save(Attachment attachment) throws Exception {

		attachmentRepository.save(attachment);
	}
	
	/**
	 * Deletes the attachments attached to the transaction and updates the transaction receipts
	 * @param transactionId
	 * @param fileUrl
	 * @throws Exception
	 */
	public void deleteAttachment(String transactionId, String fileUrl) throws Exception
	{
		//Delete the attachment
		Transaction transaction = this.find(transactionId);
		List<Attachment> attachments = transaction.getAttachments();
		if(null != attachments)
		{
			for(Attachment attachment : attachments)
			{
				if(attachment.getUri().equals(fileUrl))
				{
					attachmentRepository.delete(attachment);
					return;
				}
			}
		}
	}
}

