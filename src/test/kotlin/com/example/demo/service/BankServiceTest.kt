package com.example.demo.service

import com.example.demo.datasource.BankDataSource
import com.example.demo.model.Bank
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

class BankServiceTest {
    private val dataSource: BankDataSource = mockk(relaxed = true)
    private val bankService = BankService(dataSource = dataSource)

    @Nested
    @DisplayName("getBanks()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBanks {
        @Test
        fun `should call its data source to retrieve banks`() {
            // when
            bankService.getBanks()

            // then
            verify(exactly = 1) { dataSource.retrieveBanks() }
        }
    }

    @Nested
    @DisplayName("getBank()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBank {
        @Test
        fun `should call its data source to retrieve bank`() {
            // given
            val accountNumber = "1234"

            // when
            bankService.getBank(accountNumber)

            // then
            verify(exactly = 1) { dataSource.retrieveBank(accountNumber) }
        }
    }

    @Nested
    @DisplayName("addBank()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class AddBank {
        @Test
        fun `should call its data source to add bank`() {
            // given
            val bank = Bank(
                accountNumber = "foo",
                trust = 14.2,
                transactionFee = 1
            )

            // when
            bankService.addBank(bank)

            // then
            verify(exactly = 1) { dataSource.createBank(bank) }
        }
    }

    @Nested
    @DisplayName("updateBank()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class UpdateBank {
        @Test
        fun `should call its data source to update bank`() {
            // given
            val bank = Bank(
                accountNumber = "foo",
                trust = 12.2,
                transactionFee = 1
            )

            // when
            bankService.updateBank(bank)

            // then
            verify(exactly = 1) { dataSource.updateBank(bank) }
        }
    }
}