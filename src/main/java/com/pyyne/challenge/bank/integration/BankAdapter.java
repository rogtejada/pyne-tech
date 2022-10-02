package com.pyyne.challenge.bank.integration;

import com.pyyne.challenge.bank.domain.Balance;
import com.pyyne.challenge.bank.domain.Transaction;

import java.util.Date;
import java.util.List;

public interface BankAdapter {

    List<Transaction> getTransactions(long accountNum, Date fromDate, Date toDate);

    Balance  getBalance(long accountId);
}
