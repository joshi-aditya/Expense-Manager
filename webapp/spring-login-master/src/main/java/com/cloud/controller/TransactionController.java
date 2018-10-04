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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.constants.CommonConstants;
import com.cloud.model.Transaction;
import com.cloud.model.User;
import com.cloud.service.TransactionService;
import com.cloud.service.UserService;
import com.cloud.util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RestController
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private UserService userService;

	/**
	 * Create the transaction for the logged in user
	 * 
	 * @return String
	 * @throws IOException
	 */
	@RequestMapping(value = "/transaction", method = RequestMethod.POST)
	@ResponseBody
	public void create(@RequestBody Transaction transaction, HttpServletResponse response) throws IOException {

		String status = CommonConstants.TRANSACTION_CREATED;

		if (Utils.validateDate(transaction.getDate().toString())) {
			// Fetches the current user name who is logged in
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			try {
				User user = userService.findUserByEmail(auth.getName());
				transaction.setUser(user);
				SimpleDateFormat sf = new SimpleDateFormat(transaction.getDate().toString());
				transaction.setDate(sf.format(new Date()));
				transactionService.save(transaction);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				status = CommonConstants.TRANSACTION_FAILURE + " : " + e.getMessage();
			}
		} else {
			status = CommonConstants.INVALID_DATE_FORMAT;
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(status);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}

	/**
	 * Create the transaction for the logged in user
	 * 
	 * @return String
	 * @throws IOException
	 */
	@RequestMapping(value = "/transaction/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public void update(@PathVariable String id, @RequestBody Transaction transaction, HttpServletResponse response)
			throws IOException {
		String status = CommonConstants.TRANSACTION_UPDATED;
		// Fetches the current user name who is logged in
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		try {
			Transaction actualTransaction = transactionService.find(id);

			// Update the transaction with the new values
			actualTransaction = this.setTransactionData(transaction, actualTransaction);
			// Check if the user owns the transaction
			if (actualTransaction.getUser().getEmail().equalsIgnoreCase(auth.getName())) {
				transactionService.save(actualTransaction);
			} else {
				status = CommonConstants.UNAUTHORIZED;
			}
		} catch (Exception e) {
			status = CommonConstants.TRANSACTION_FAILURE + " : " + e.getMessage();
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(status);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}

	private Transaction setTransactionData(Transaction transaction, Transaction actualTransaction) {

		actualTransaction.setAmount(transaction.getAmount());
		actualTransaction.setDate(transaction.getDate());
		actualTransaction.setCategory(transaction.getCategory());
		actualTransaction.setDescription(transaction.getDescription());
		actualTransaction.setMerchant(transaction.getMerchant());

		return actualTransaction;
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
	public void delete(@PathVariable String id, HttpServletResponse response) throws IOException {
		String status = CommonConstants.TRANSACTION_DELETED;
		// Fetches the current user name who is logged in
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		try {
			Transaction transaction = transactionService.find(id);

			if (transaction.getUser().getEmail().equalsIgnoreCase(auth.getName())) {
				transactionService.deleteById(id);
			} else {
				status = CommonConstants.UNAUTHORIZED;
			}
		} catch (Exception e) {
			status = CommonConstants.TRANSACTION_DELETION_FAILURE + ":" + e.getMessage();
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(status);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);

	}

	/**
	 * Gets the user's transaction
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/transaction", method = RequestMethod.GET)
	@ResponseBody
	public void findByUserId(HttpServletResponse response) throws IOException {
		// Fetches the current user name who is logged in
		String status = CommonConstants.GET_ALL_TRANSACTION_FAILURE;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			User user = userService.findUserByEmail(auth.getName());
			List<Transaction> getAllTransaction = transactionService.findByUserId(user.getId());
			status = new Gson().toJson(getAllTransaction);

		} catch (Exception e) {
			status = CommonConstants.GET_ALL_TRANSACTION_FAILURE + ":" + e.getMessage();
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(status);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}

}
