package com.pyyne.challenge.bank.integration;

import com.pyyne.challenge.bank.domain.Balance;
import com.pyyne.challenge.bank.domain.Transaction;

import java.util.*;
import java.util.stream.Collectors;

public class BankIntegration {

    private List<BankAdapter> banks;

    public BankIntegration(List<BankAdapter> banks) {
        this.banks = banks;
    }

    public List<Transaction> getTransactions(long accountNum, Date fromDate, Date toDate) {
       return banks.stream()
               .flatMap(bankAdapter -> bankAdapter.getTransactions(accountNum, fromDate, toDate).stream())
               .collect(Collectors.toList());
    }

    public List<Balance> getBalances(long accountId) {
        final Map<String, Double> balanceByCurrency = new HashMap<>();


        banks.forEach(bankAdapter -> {
            Balance balance = bankAdapter.getBalance(accountId);
            Double balanceAgg = balanceByCurrency.get(balance.getCurrency());

            if (balanceAgg == null) {
                balanceByCurrency.put(balance.getCurrency(), balance.getBalance());
            } else {
                balanceAgg += balance.getBalance();
                balanceByCurrency.put(balance.getCurrency(), balanceAgg);
            }
        });

        return balanceByCurrency.entrySet()
                .stream()
                .map(entry -> new Balance(entry.getValue(), entry.getKey()))
                .collect(Collectors.toList());
    }
}
