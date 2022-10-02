package com.pyyne.challenge.bank.integration;

import com.pyyne.challenge.bank.domain.Balance;
import com.pyyne.challenge.bank.domain.Transaction;
import com.pyyne.challenge.bank.domain.TransactionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mockito.Mockito.*;

class BankIntegrationTest {

    private BankIntegration bank;
    private Bank1AdapterImpl mockAdapter1;
    private Bank2AdapterImpl mockAdapter2;
    private final Integer accountNumber = 1;
    private final Date firstMonth = new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime();
    private final Date lastMonth = new GregorianCalendar(2020, Calendar.DECEMBER, 1).getTime();

    @BeforeEach
    void setUp() {
        mockAdapter1 = mock(Bank1AdapterImpl.class);
        mockAdapter2 = mock(Bank2AdapterImpl.class);

        bank = new BankIntegration(Arrays.asList(mockAdapter1, mockAdapter2));
    }


    @Test
    public void shouldGetAllTransactionsSuccessfully() {
        when(mockAdapter1.getTransactions(accountNumber, firstMonth, lastMonth)).thenReturn(Arrays.asList(
                new Transaction(125d, TransactionType.DEBIT, "Amazon.com"),
                new Transaction(500d, TransactionType.DEBIT, "Car insurance"),
                new Transaction(800d, TransactionType.CREDIT, "Salary")
        ));

        when(mockAdapter2.getTransactions(accountNumber, firstMonth, lastMonth)).thenReturn(Arrays.asList(
                new Transaction(100d, TransactionType.CREDIT, "Check deposit"),
                new Transaction(25.5d, TransactionType.DEBIT, "Debit card purchase"),
                new Transaction(225d, TransactionType.DEBIT, "Rent payment")
        ));

        Transaction firstTransaction = new Transaction(125d, TransactionType.DEBIT, "Amazon.com");
        Transaction secondTransaction = new Transaction(500d, TransactionType.DEBIT, "Car insurance");
        Transaction thirdTransaction = new Transaction(800d, TransactionType.CREDIT, "Salary");
        Transaction fourthTransaction = new Transaction(100d, TransactionType.CREDIT, "Check deposit");
        Transaction fifthTransaction = new Transaction(25.5d, TransactionType.DEBIT, "Debit card purchase");
        Transaction sixthTransaction = new Transaction(225d, TransactionType.DEBIT, "Rent payment");

        List<Transaction> result = bank.getTransactions(accountNumber, firstMonth, lastMonth);

        Assertions.assertEquals(6, result.size());
        Assertions.assertEquals(firstTransaction, result.get(0));
        Assertions.assertEquals(secondTransaction, result.get(1));
        Assertions.assertEquals(thirdTransaction, result.get(2));
        Assertions.assertEquals(fourthTransaction, result.get(3));
        Assertions.assertEquals(fifthTransaction, result.get(4));
        Assertions.assertEquals(sixthTransaction, result.get(5));

        verify(mockAdapter1, times(1)).getTransactions(accountNumber, firstMonth, lastMonth);
        verify(mockAdapter2, times(1)).getTransactions(accountNumber, firstMonth, lastMonth);
    }

    @Test
    public void shouldGetAllTransactionsSuccessfullyWithOnlyFirstAdapter() {
        when(mockAdapter1.getTransactions(accountNumber, firstMonth, lastMonth)).thenReturn(Arrays.asList(
                new Transaction(125d, TransactionType.DEBIT, "Amazon.com"),
                new Transaction(500d, TransactionType.DEBIT, "Car insurance"),
                new Transaction(800d, TransactionType.CREDIT, "Salary")
        ));

        when(mockAdapter2.getTransactions(accountNumber, firstMonth, lastMonth)).thenReturn(Collections.emptyList());

        Transaction firstTransaction = new Transaction(125d, TransactionType.DEBIT, "Amazon.com");
        Transaction secondTransaction = new Transaction(500d, TransactionType.DEBIT, "Car insurance");
        Transaction thirdTransaction = new Transaction(800d, TransactionType.CREDIT, "Salary");


        List<Transaction> result = bank.getTransactions(accountNumber, firstMonth, lastMonth);

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(firstTransaction, result.get(0));
        Assertions.assertEquals(secondTransaction, result.get(1));
        Assertions.assertEquals(thirdTransaction, result.get(2));

        verify(mockAdapter1, times(1)).getTransactions(accountNumber, firstMonth, lastMonth);
        verify(mockAdapter2, times(1)).getTransactions(accountNumber, firstMonth, lastMonth);
    }

    @Test
    public void shouldGetAllTransactionsSuccessfullyWithOnlySecondAdapter() {
        when(mockAdapter1.getTransactions(accountNumber, firstMonth, lastMonth)).thenReturn(Collections.emptyList());

        when(mockAdapter2.getTransactions(accountNumber, firstMonth, lastMonth)).thenReturn(Arrays.asList(
                new Transaction(100d, TransactionType.CREDIT, "Check deposit"),
                new Transaction(25.5d, TransactionType.DEBIT, "Debit card purchase"),
                new Transaction(225d, TransactionType.DEBIT, "Rent payment")
        ));

        Transaction firstTransaction = new Transaction(100d, TransactionType.CREDIT, "Check deposit");
        Transaction secondTransaction = new Transaction(25.5d, TransactionType.DEBIT, "Debit card purchase");
        Transaction thirdTransaction = new Transaction(225d, TransactionType.DEBIT, "Rent payment");

        List<Transaction> result = bank.getTransactions(accountNumber, firstMonth, lastMonth);

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(firstTransaction, result.get(0));
        Assertions.assertEquals(secondTransaction, result.get(1));
        Assertions.assertEquals(thirdTransaction, result.get(2));

        verify(mockAdapter1, times(1)).getTransactions(accountNumber, firstMonth, lastMonth);
        verify(mockAdapter2, times(1)).getTransactions(accountNumber, firstMonth, lastMonth);
    }

    @Test
    public void shouldGetAllTransactionsSuccessfullyWithEmptyTransactions() {
        when(mockAdapter1.getTransactions(accountNumber, firstMonth, lastMonth)).thenReturn(Collections.emptyList());

        when(mockAdapter2.getTransactions(accountNumber, firstMonth, lastMonth)).thenReturn(Collections.emptyList());

        List<Transaction> result = bank.getTransactions(accountNumber, firstMonth, lastMonth);

        Assertions.assertTrue(result.isEmpty());

        verify(mockAdapter1, times(1)).getTransactions(accountNumber, firstMonth, lastMonth);
        verify(mockAdapter2, times(1)).getTransactions(accountNumber, firstMonth, lastMonth);
    }

    @Test
    public void shouldSumBalanceSuccessfully() {
        when(mockAdapter1.getBalance(accountNumber)).thenReturn(new Balance(150d, "USD"));
        when(mockAdapter2.getBalance(accountNumber)).thenReturn(new Balance(300d, "USD"));

        List<Balance> result = bank.getBalances(accountNumber);

        Balance expectedBalance = new Balance(450d, "USD");

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(expectedBalance, result.get(0));

        verify(mockAdapter1, times(1)).getBalance(accountNumber);
        verify(mockAdapter2, times(1)).getBalance(accountNumber);

    }

    @Test
    public void shouldSumBalanceSuccessfullyWithDifferentCurrencies() {
        when(mockAdapter1.getBalance(accountNumber)).thenReturn(new Balance(150d, "USD"));
        when(mockAdapter2.getBalance(accountNumber)).thenReturn(new Balance(300d, "BRL"));

        List<Balance> result = bank.getBalances(accountNumber);

        Balance expectedDolarBalance = new Balance(150d, "USD");
        Balance expectedRealBalance = new Balance(300d, "BRL");

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(expectedDolarBalance, result.get(0));
        Assertions.assertEquals(expectedRealBalance, result.get(1));

        verify(mockAdapter1, times(1)).getBalance(accountNumber);
        verify(mockAdapter2, times(1)).getBalance(accountNumber);
    }

}