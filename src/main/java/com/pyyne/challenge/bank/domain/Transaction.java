package com.pyyne.challenge.bank.domain;

import java.util.Objects;

public class Transaction {

    private double amount;
    private TransactionType type;
    private String text;

    public Transaction(double amount, TransactionType type, String text) {
        this.amount = amount;
        this.type = type;
        this.text = text;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(that.amount, amount) == 0 && type == that.type && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, type, text);
    }
}
