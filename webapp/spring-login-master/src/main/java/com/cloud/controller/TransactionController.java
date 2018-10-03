package com.cloud.controller;

import com.cloud.constants.CommonConstants;
import com.cloud.model.Transaction;
import com.cloud.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    /**
     * Created the transaction
     * @return String
     */
    @RequestMapping(value = "/transaction", method = RequestMethod.POST)
    @ResponseBody
    public String createNewTransaction(@RequestBody Transaction transaction) {

        transactionService.saveTransaction(transaction);
        return CommonConstants.TRANSACTION_CREATED;

    }

}
