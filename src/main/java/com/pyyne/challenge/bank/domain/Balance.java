package com.pyyne.challenge.bank.domain;

import java.util.Objects;

public class Balance {

    private double balance;
    private String currency;

    public Balance(double balance, String currency) {
        this.balance = balance;
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Balance balance1 = (Balance) o;
        return Double.compare(balance1.balance, balance) == 0 && Objects.equals(currency, balance1.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance, currency);
    }
}
