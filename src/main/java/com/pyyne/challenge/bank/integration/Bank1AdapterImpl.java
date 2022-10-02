package com.pyyne.challenge.bank.integration;

import com.bank1.integration.Bank1AccountSource;
import com.bank1.integration.Bank1Transaction;
import com.pyyne.challenge.bank.domain.Balance;
import com.pyyne.challenge.bank.domain.Transaction;
import com.pyyne.challenge.bank.domain.TransactionType;

import java.util.*;
import java.util.stream.Collectors;

public class Bank1AdapterImpl implements BankAdapter {

    private Bank1AccountSource bank1AccountSource;
    private Map<Integer, TransactionType> transactionsTypeConverter;

    public Bank1AdapterImpl(Bank1AccountSource bank1AccountSource) {
        this.bank1AccountSource = bank1AccountSource;
        transactionsTypeConverter = new HashMap<>();
        transactionsTypeConverter.put(1, TransactionType.CREDIT);
        transactionsTypeConverter.put(2, TransactionType.DEBIT);
    }

    @Override
    public List<Transaction> getTransactions(long accountNum, Date fromDate, Date toDate) {

        return bank1AccountSource.getTransactions(accountNum, fromDate, toDate).stream()
                .map(this::convertTransaction)
                .collect(Collectors.toList());
    }

    private Transaction convertTransaction(Bank1Transaction transaction) {
        if (transaction.getAmount() < 0) {
            throw new IllegalArgumentException("Cannot have negative amount transactions");
        }

        TransactionType transactionType = Optional.ofNullable(transactionsTypeConverter.get(transaction.getType()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid transaction type"));
        return new Transaction(transaction.getAmount(), transactionType, transaction.getText());
    }

    @Override

    public Balance getBalance(long accountId) {
        String currency = bank1AccountSource.getAccountCurrency(accountId);

        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Invalid Currency");
        }

        Double balance = Optional.ofNullable(bank1AccountSource.getAccountBalance(accountId))
                .orElseThrow(() -> new IllegalArgumentException("Invalid Currency"));

        return new Balance(balance, currency);
    }
}
