package com.cloud.controller;

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
	 */
	@RequestMapping(value = "/transaction", method = RequestMethod.POST)
	@ResponseBody
	public String create(@RequestBody Transaction transaction) {

		String status = CommonConstants.TRANSACTION_CREATED;

		if (Utils.validateDate(transaction.getDate())) {
			// Fetches the current user name who is logged in
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			try {
				User user = userService.findUserByEmail(auth.getName());
				transaction.setUser(user);

				transaction.setDate(transaction.getDate());
				transactionService.save(transaction);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				status = CommonConstants.TRANSACTION_FAILURE + " : " + e.getMessage();
			}
		} else {
			status = CommonConstants.INVALID_DATE_FORMAT;
		}

		return status;

	}

	/**
	 * Create the transaction for the logged in user
	 * 
	 * @return String
	 */
	@RequestMapping(value = "/transaction/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public String update(@PathVariable Integer id, @RequestBody Transaction transaction) {

		String status = CommonConstants.TRANSACTION_CREATED;
		// Fetches the current user name who is logged in
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		try {
			Transaction actualTransaction = transactionService.find(id);

			// TODO
			// Update the transaction with the new values

			// Check if the user owns the transaction
			if (transaction.getUser().getEmail().equalsIgnoreCase(auth.getName())) {
				transactionService.save(transaction);
			} else {
				status = CommonConstants.UNAUTHORIZED;
			}
		} catch (Exception e) {
			status = CommonConstants.TRANSACTION_FAILURE + " : " + e.getMessage();
		}

		return status;

	}
	
	/**
	 * Deletes the transaction
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/transaction/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public String delete(@PathVariable Integer id) {
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

		return status;

	}

}
