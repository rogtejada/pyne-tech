package com.pyyne.challenge.bank.integration;

import com.bank1.integration.Bank1AccountSource;
import com.bank1.integration.Bank1Transaction;
import com.pyyne.challenge.bank.domain.Balance;
import com.pyyne.challenge.bank.domain.Transaction;
import com.pyyne.challenge.bank.domain.TransactionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class Bank2AdapterImplTest {

    private Bank1AdapterImpl bankAdapter;
    private Bank1AccountSource mockAccountSource;
    private final Date firstMonth = new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime();
    private final Date lastMonth = new GregorianCalendar(2020, Calendar.DECEMBER, 1).getTime();


    @BeforeEach
    void setUp() {
        mockAccountSource = mock(Bank1AccountSource.class);
        bankAdapter = new Bank1AdapterImpl(mockAccountSource);
    }

    @Test
    public void shouldGetAllTransactionsSuccessfully() {
        when(mockAccountSource.getTransactions(1, firstMonth, lastMonth)).thenReturn(Arrays.asList(
                new Bank1Transaction(100d, Bank1Transaction.TYPE_CREDIT, "Check deposit"),
                new Bank1Transaction(25.5d, Bank1Transaction.TYPE_DEBIT, "Debit card purchase"),
                new Bank1Transaction(225d, Bank1Transaction.TYPE_DEBIT, "Rent payment")
        ));

        List<Transaction> result = bankAdapter.getTransactions(1, firstMonth, lastMonth);


        Transaction firstTransaction = new Transaction(100d, TransactionType.CREDIT, "Check deposit");
        Transaction secondTransaction = new Transaction(25.5d,TransactionType.DEBIT, "Debit card purchase");
        Transaction thirdTransaction = new Transaction(225d, TransactionType.DEBIT, "Rent payment");

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(firstTransaction, result.get(0), "First transaction");
        Assertions.assertEquals(secondTransaction, result.get(1), "Second Transaction");
        Assertions.assertEquals(thirdTransaction, result.get(2), "Third transaction");
    }

    @Test
    public void shouldGetAllTransactionsWhenNoTransactionIsFound() {
        when(mockAccountSource.getTransactions(1, firstMonth, lastMonth)).thenReturn(Collections.emptyList());

        List<Transaction> result = bankAdapter.getTransactions(1, firstMonth, lastMonth);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenInvalidBalanceIsFound() {
        when(mockAccountSource.getTransactions(1, firstMonth, lastMonth)).thenReturn(Arrays.asList(
                new Bank1Transaction(100d, Bank1Transaction.TYPE_CREDIT, "Check deposit"),
                new Bank1Transaction(-25.5d, Bank1Transaction.TYPE_DEBIT, "Debit card purchase"),
                new Bank1Transaction(225d, Bank1Transaction.TYPE_DEBIT, "Rent payment")
        ));


        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> bankAdapter.getTransactions(1, firstMonth, lastMonth));
        Assertions.assertEquals("Cannot have negative amount transactions", exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenInvalidTransactionType() {
        when(mockAccountSource.getTransactions(1, firstMonth, lastMonth)).thenReturn(Arrays.asList(
                new Bank1Transaction(100d, Bank1Transaction.TYPE_CREDIT, "Check deposit"),
                new Bank1Transaction(25.5d, 5, "Debit card purchase"),
                new Bank1Transaction(225d, Bank1Transaction.TYPE_DEBIT, "Rent payment")
        ));

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> bankAdapter.getTransactions(1, firstMonth, lastMonth));
        Assertions.assertEquals("Invalid transaction type", exception.getMessage());
    }

    @Test
    public void shouldGetBalanceSuccessfully() {
        when(mockAccountSource.getAccountBalance(1)).thenReturn(215.5d);
        when(mockAccountSource.getAccountCurrency(1)).thenReturn("USD");

        Balance result = bankAdapter.getBalance(1);

        Balance expectedBalance = new Balance(215.5d, "USD");

        Assertions.assertEquals(expectedBalance, result);
    }

    @Test
    public void shouldGetBalanceSuccessfullyEvenWithNegativeNumber() {
        when(mockAccountSource.getAccountBalance(1)).thenReturn(-215.5d);
        when(mockAccountSource.getAccountCurrency(1)).thenReturn("USD");

        Balance result = bankAdapter.getBalance(1);

        Balance expectedBalance = new Balance(-215.5d, "USD");

        Assertions.assertEquals(expectedBalance, result);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenNullCurrency() {
        when(mockAccountSource.getAccountBalance(1)).thenReturn(-215.5d);
        when(mockAccountSource.getAccountCurrency(1)).thenReturn(null);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> bankAdapter.getBalance(1));
        Assertions.assertEquals("Invalid Currency", exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenEmptyCurrency() {
        when(mockAccountSource.getAccountBalance(1)).thenReturn(-215.5d);
        when(mockAccountSource.getAccountCurrency(1)).thenReturn("");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> bankAdapter.getBalance(1));
        Assertions.assertEquals("Invalid Currency", exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenBalnkCurrency() {
        when(mockAccountSource.getAccountBalance(1)).thenReturn(-215.5d);
        when(mockAccountSource.getAccountCurrency(1)).thenReturn("  ");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> bankAdapter.getBalance(1));
        Assertions.assertEquals("Invalid Currency", exception.getMessage());
    }
}