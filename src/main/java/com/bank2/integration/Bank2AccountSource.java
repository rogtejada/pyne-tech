package com.bank2.integration;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Par Renyard on 5/12/21.
 */
public class Bank2AccountSource {

    public Bank2AccountBalance getBalance(long accountNum) {
        return new Bank2AccountBalance(512.5d, "USD");
    }

    public List<Bank2AccountTransaction> getTransactions(long accountNum, Date fromDate, Date toDate) {
        return Arrays.asList(
                new Bank2AccountTransaction(125d, Bank2AccountTransaction.TRANSACTION_TYPES.DEBIT, "Amazon.com"),
                new Bank2AccountTransaction(500d, Bank2AccountTransaction.TRANSACTION_TYPES.DEBIT, "Car insurance"),
                new Bank2AccountTransaction(800d, Bank2AccountTransaction.TRANSACTION_TYPES.CREDIT, "Salary")
        );
    }
}
