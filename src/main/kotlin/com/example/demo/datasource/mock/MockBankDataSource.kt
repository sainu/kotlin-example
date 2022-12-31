package com.example.demo.datasource.mock

import com.example.demo.datasource.BankDataSource
import com.example.demo.model.Bank
import org.springframework.stereotype.Repository

@Repository
class MockBankDataSource : BankDataSource {
    val banks =  listOf(
        Bank(accountNumber = "1234", trust = 3.14, transactionFree = 17),
        Bank(accountNumber = "1010", trust = 17.0, transactionFree = 0),
        Bank(accountNumber = "5678", trust = 0.0, transactionFree = 100),
    )

    override fun retrieveBanks(): Collection<Bank> = banks

    override fun retrieveBank(accountNumber: String): Bank =
        banks.firstOrNull() { it.accountNumber == accountNumber }
            ?: throw NoSuchElementException("Could not find a bank with account number $accountNumber")
}