package com.pyyne.challenge.bank.integration;

import com.bank2.integration.Bank2AccountBalance;
import com.bank2.integration.Bank2AccountSource;
import com.bank2.integration.Bank2AccountTransaction;
import com.pyyne.challenge.bank.domain.Balance;
import com.pyyne.challenge.bank.domain.Transaction;
import com.pyyne.challenge.bank.domain.TransactionType;

import java.util.*;
import java.util.stream.Collectors;

import static com.bank2.integration.Bank2AccountTransaction.TRANSACTION_TYPES.CREDIT;
import static com.bank2.integration.Bank2AccountTransaction.TRANSACTION_TYPES.DEBIT;

public class Bank2AdapterImpl implements BankAdapter {

    private Bank2AccountSource bank2AccountSource;
    private Map<Bank2AccountTransaction.TRANSACTION_TYPES, TransactionType> transactionsTypeConverter;

    public Bank2AdapterImpl(Bank2AccountSource bank2AccountSource) {
        this.bank2AccountSource = bank2AccountSource;
        transactionsTypeConverter = new HashMap<>();
        transactionsTypeConverter.put(CREDIT, TransactionType.CREDIT);
        transactionsTypeConverter.put(DEBIT, TransactionType.DEBIT);
    }

    @Override
    public List<Transaction> getTransactions(long accountNum, Date fromDate, Date toDate) {
        return bank2AccountSource.getTransactions(accountNum, fromDate, toDate)
                .stream()
                .map(this::convertTransaction)
                .collect(Collectors.toList());
    }

    private Transaction convertTransaction(Bank2AccountTransaction transaction) {
        if (transaction.getAmount() < 0) {
            throw new IllegalArgumentException("Cannot have negative amount transactions");
        }

        TransactionType transactionType = Optional.ofNullable(transactionsTypeConverter.get(transaction.getType()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid transaction type"));
        return new Transaction(transaction.getAmount(), transactionType, transaction.getText());
    }

    @Override
    public Balance getBalance(long accountId) {
        return convertBalance(bank2AccountSource.getBalance(accountId));
    }

    private Balance convertBalance(Bank2AccountBalance balance) {
        if (balance.getCurrency() == null || balance.getCurrency().isBlank()) {
            throw new IllegalArgumentException("Invalid Currency");
        }

        return new Balance(balance.getBalance(), balance.getCurrency());
    }

}
