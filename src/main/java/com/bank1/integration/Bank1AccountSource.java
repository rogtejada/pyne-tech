package com.bank1.integration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Par Renyard on 5/12/21.
 */
public class Bank1AccountSource {

    public Double getAccountBalance(long accountId) {
        return 215.5d;
    }
    public String getAccountCurrency(long accountId) {
        return "USD";
    }

    public List<Bank1Transaction> getTransactions(long accountId, Date fromDate, Date toDate) {
        return Arrays.asList(
                new Bank1Transaction(100d, Bank1Transaction.TYPE_CREDIT, "Check deposit"),
                new Bank1Transaction(25.5d, Bank1Transaction.TYPE_DEBIT, "Debit card purchase"),
                new Bank1Transaction(225d, Bank1Transaction.TYPE_DEBIT, "Rent payment")
        );
    }


}
