package com.bank1.integration;

import java.util.Date;

/**
 * Created by Par Renyard on 5/12/21.
 */
public class Bank1Transaction {

    public static int TYPE_CREDIT = 1;
    public static int TYPE_DEBIT = 2;

    private double amount;
    private int type;
    private String text;

    public Bank1Transaction(double amount, int type, String text) {
        this.amount = amount;
        this.type = type;
        this.text = text;
    }

    public double getAmount() {
        return amount;
    }

    public int getType() {
        return type;
    }

    public String getText() {
        return text;
    }


}
