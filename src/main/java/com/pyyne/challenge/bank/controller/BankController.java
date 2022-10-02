package com.pyyne.challenge.bank.controller;

import com.bank1.integration.Bank1AccountSource;
import com.bank2.integration.Bank2AccountSource;
import com.pyyne.challenge.bank.integration.Bank1AdapterImpl;
import com.pyyne.challenge.bank.integration.Bank2AdapterImpl;
import com.pyyne.challenge.bank.integration.BankIntegration;

import java.util.Arrays;
import java.util.Date;

/**
 * Controller that pulls information form multiple bank integrations and prints them to the console.
 *
 * Created by Par Renyard on 5/12/21.
 */
public class BankController {

    private BankIntegration bankIntegration;

    public BankController() {
        Bank1AdapterImpl bank1Adapter = new Bank1AdapterImpl(new Bank1AccountSource());
        Bank2AdapterImpl bank2Adapter = new Bank2AdapterImpl(new Bank2AccountSource());
        this.bankIntegration = new BankIntegration(Arrays.asList(bank1Adapter, bank2Adapter));
    }

    public void printBalances(long accountNum) {
        System.out.println("Implement me to pull balance information from all available bank integrations and display them, one after the other.");

        System.out.println(bankIntegration.getBalances(accountNum));
    }

    public void printTransactions(long accountNum, Date fromDate, Date toDate) {
        System.out.println("Implement me to pull transactions from all available bank integrations and display them, one after the other.");
        System.out.println(bankIntegration.getTransactions(accountNum, fromDate, toDate));

    }
}
