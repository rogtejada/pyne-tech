package com.bank2.integration;

/**
 * Created by Par Renyard on 5/12/21.
 */
public class Bank2AccountTransaction {

    public static enum TRANSACTION_TYPES {
        DEBIT, CREDIT
    }

    private double amount;
    private TRANSACTION_TYPES type;
    private String text;

    public Bank2AccountTransaction(double amount, TRANSACTION_TYPES type, String text) {
        this.amount = amount;
        this.type = type;
        this.text = text;
    }

    public double getAmount() {
        return amount;
    }

    public TRANSACTION_TYPES getType() {
        return type;
    }

    public String getText() {
        return text;
    }


}
