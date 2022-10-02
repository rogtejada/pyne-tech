package com.pyyne.challenge.bank.integration;

import com.bank2.integration.Bank2AccountBalance;
import com.bank2.integration.Bank2AccountSource;
import com.bank2.integration.Bank2AccountTransaction;
import com.pyyne.challenge.bank.domain.Balance;
import com.pyyne.challenge.bank.domain.Transaction;
import com.pyyne.challenge.bank.domain.TransactionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class Bank1AdapterImplTest {

    private Bank2AdapterImpl bankAdapter;
    private Bank2AccountSource mockAccountSource;
    private final Date firstMonth = new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime();
    private final Date lastMonth = new GregorianCalendar(2020, Calendar.DECEMBER, 1).getTime();


    @BeforeEach
    void setUp() {
        mockAccountSource = mock(Bank2AccountSource.class);
        bankAdapter = new Bank2AdapterImpl(mockAccountSource);
    }

    @Test
    public void shouldGetAllTransactionsSuccessfully() {
        when(mockAccountSource.getTransactions(1, firstMonth, lastMonth)).thenReturn(Arrays.asList(
                new Bank2AccountTransaction(125d, Bank2AccountTransaction.TRANSACTION_TYPES.DEBIT, "Amazon.com"),
                new Bank2AccountTransaction(500d, Bank2AccountTransaction.TRANSACTION_TYPES.DEBIT, "Car insurance"),
                new Bank2AccountTransaction(800d, Bank2AccountTransaction.TRANSACTION_TYPES.CREDIT, "Salary")
        ));

        List<Transaction> result = bankAdapter.getTransactions(1, firstMonth, lastMonth);


        Transaction firstTransaction = new Transaction(125d, TransactionType.DEBIT, "Amazon.com");
        Transaction secondTransaction = new Transaction(500d,TransactionType.DEBIT, "Car insurance");
        Transaction thirdTransaction = new Transaction(800d, TransactionType.CREDIT, "Salary");

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
                new Bank2AccountTransaction(125d, Bank2AccountTransaction.TRANSACTION_TYPES.DEBIT, "Amazon.com"),
                new Bank2AccountTransaction(-500d, Bank2AccountTransaction.TRANSACTION_TYPES.DEBIT, "Car insurance"),
                new Bank2AccountTransaction(800d, Bank2AccountTransaction.TRANSACTION_TYPES.CREDIT, "Salary")
        ));

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> bankAdapter.getTransactions(1, firstMonth, lastMonth));
        Assertions.assertEquals("Cannot have negative amount transactions", exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenInvalidTransactionType() {
        when(mockAccountSource.getTransactions(1, firstMonth, lastMonth)).thenReturn(Arrays.asList(
                new Bank2AccountTransaction(125d, Bank2AccountTransaction.TRANSACTION_TYPES.DEBIT, "Amazon.com"),
                new Bank2AccountTransaction(500d, null, "Car insurance"),
                new Bank2AccountTransaction(800d, Bank2AccountTransaction.TRANSACTION_TYPES.CREDIT, "Salary")
        ));

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> bankAdapter.getTransactions(1, firstMonth, lastMonth));
        Assertions.assertEquals("Invalid transaction type", exception.getMessage());
    }

    @Test
    public void shouldGetBalanceSuccessfully() {
        when(mockAccountSource.getBalance(1)).thenReturn(new Bank2AccountBalance(512.5d, "USD"));

        Balance result = bankAdapter.getBalance(1);

        Balance expectedBalance = new Balance(512.5d, "USD");

        Assertions.assertEquals(expectedBalance, result);
    }

    @Test
    public void shouldGetBalanceSuccessfullyEvenWithNegativeNumber() {
        when(mockAccountSource.getBalance(1)).thenReturn(new Bank2AccountBalance(-500d, "USD"));

        Balance result = bankAdapter.getBalance(1);

        Balance expectedBalance = new Balance(-500d, "USD");

        Assertions.assertEquals(expectedBalance, result);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenNullCurrency() {
        when(mockAccountSource.getBalance(1)).thenReturn(new Bank2AccountBalance(500d, null));

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> bankAdapter.getBalance(1));
        Assertions.assertEquals("Invalid Currency", exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenEmptyCurrency() {
        when(mockAccountSource.getBalance(1)).thenReturn(new Bank2AccountBalance(500d, ""));

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> bankAdapter.getBalance(1));
        Assertions.assertEquals("Invalid Currency", exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenBalnkCurrency() {
        when(mockAccountSource.getBalance(1)).thenReturn(new Bank2AccountBalance(500d, " "));

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> bankAdapter.getBalance(1));
        Assertions.assertEquals("Invalid Currency", exception.getMessage());
    }


}