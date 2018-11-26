package com.cloud.service;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cloud.model.Attachment;
import com.cloud.model.Transaction;
import com.cloud.model.User;
import com.cloud.repository.AttachmentRepository;
import com.cloud.repository.TransactionRepository;
import com.cloud.repository.UserRepository;

public class TransactionServiceTest {

	//User details
	private int userId  		= 1;
	private String email  		= "test@neu.com";
	private String lastName  	= "lastName";
	private String name  		= "name";
	private String password 	= "1234";
	
	//Transaction details
	private String id 			= "38400000-8cf0-11bd-b23e-10b96e4ef00d";
	private UUID transactionId	= UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
	private String description  = "coffee";
	private String merchant		= "starbucks";
	private Float amount		= 2.69f;
	private String date			= "09/25/2018";
	private String category		= "food";
	
	//Attachment details
	private UUID attachmentId	= UUID.fromString("12300000-8cf0-11bd-b23e-10b96e4ef00d");
	private String uri 			= "/image.jpeg";
	
	@InjectMocks
	UserService userService;

	@Mock
	UserRepository userRepository;
	
	@InjectMocks
	TransactionService transactionService;

	@Mock
	TransactionRepository transactionRepository;
	
	@Mock
	AttachmentRepository attachmentRepository;
	
	private User userAccount;
	
	private List<Transaction> transactions;
	
	private Transaction transaction;
	
	private List<Attachment> attachments;
	
	private Attachment attachment;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockUserCreation();
		mockFindUser();
		mockTransactionCreation();
		mockAttachmentCreation();
	}

	/**
	 * Added to mock the user creation
	 */
	private void mockUserCreation()
	{
		userAccount = new User();
		userAccount.setId(userId);
		userAccount.setName(name);
		userAccount.setLastName(lastName);
		userAccount.setEmail(email);
		userAccount.setPassword(password);
		userAccount.setActive(1);
		when(userRepository.save(userAccount)).thenReturn(userAccount);
	}
	
	/**
	 * Added to mock the retrieve user
	 */
	private void mockFindUser()
	{
		when(userRepository.findByEmail(email)).thenReturn(userAccount);
	}
	
	/**
	 * Added to mock the transaction creation for a users
	 */
	private void mockTransactionCreation()
	{
		transactions = new ArrayList<Transaction>();
		transaction = new Transaction(description, merchant, amount, date, category, userAccount, null);
		transactions.add(transaction);
		when(transactionRepository.findByUserId(userId)).thenReturn(transactions);
		when(transactionRepository.findByTransactionId(transactionId)).thenReturn(transaction);
	}
	
	/**
	 * Added to mock the attachment creation for a transaction
	 */
	private void mockAttachmentCreation()
	{
		attachments = new ArrayList<Attachment>();
		attachment = new Attachment(uri, transaction);
		attachments.add(attachment);
		when(attachmentRepository.findByid(attachmentId)).thenReturn(attachment);
	}
	
	@Test
	public void findTransactionsByUserId() {

		List<Transaction> transactionList = transactionService.findByUserId(userAccount.getId());
		Assert.assertFalse(transactionList.isEmpty());
		Assert.assertEquals("UserId matches", transactionList.get(0).getUser().getId(), userAccount.getId());
		Assert.assertEquals("User email matches", transactionList.get(0).getUser().getEmail(), userAccount.getEmail());
		Assert.assertEquals("Description matches", transactionList.get(0).getDescription(), transaction.getDescription());
		Assert.assertEquals("Merchant matches", transactionList.get(0).getMerchant(), transaction.getMerchant());
		Assert.assertEquals("Amount matches", transactionList.get(0).getAmount(), transaction.getAmount());
		Assert.assertEquals("Date matches", transactionList.get(0).getDate(), transaction.getDate());
		Assert.assertEquals("Category matches", transactionList.get(0).getCategory(), transaction.getCategory());
		
	}
	
	
	@Test
	public void findAttachmentsForTransaction()
	{
		//mock add attachment to transaction
		transaction.setAttachments(attachments);
		
		try {
			
			Transaction transaction = transactionService.find(id);
			Assert.assertFalse(transaction.getAttachments().isEmpty());
			Assert.assertEquals("Attachment uri matches", transaction.getAttachments().get(0).getUri(), attachment.getUri());
			Assert.assertEquals("Attachement Id matches", transaction.getAttachments().get(0).getId(), attachment.getId());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}