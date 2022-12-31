package com.example.demo.datasource.mock

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

internal class MockBankDataSourceTest {
    private val mockDataSource = MockBankDataSource()

    @Nested
    @DisplayName("retrieveBanks()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class RetrieveBanks {
        @Test
        fun `should provide a collection of banks`() {
            // when
            val banks = mockDataSource.retrieveBanks()

            // then
            assertThat(banks.size).isGreaterThanOrEqualTo(3)
        }

        @Test
        fun `should provide some mock data`() {
            // when
            val banks = mockDataSource.retrieveBanks()

            // then
            assertThat(banks).anyMatch { it.accountNumber.isNotBlank() }
            assertThat(banks).anyMatch { it.trust != 0.0 }
            assertThat(banks).anyMatch { it.transactionFree != 0 }
        }
    }

    @Nested
    @DisplayName("retrieveBank()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class RetrieveBank {
        @Test
        fun `should return the bank with the given account number`() {
            // given
            val accountNumber = "1234"

            // when
            val bank = mockDataSource.retrieveBank(accountNumber)

            // then
            assertThat(bank).isNotNull
            assertThat(bank.accountNumber).isEqualTo(accountNumber)
            assertThat(bank.trust).isEqualTo(3.14)
            assertThat(bank.transactionFree).isEqualTo(17)
        }

        @Test
        fun `should throw NoSuchElementException if the account number does not exist`() {
            // given
            val accountNumber = "does_not_exist"

            // when
            val error = assertThrows<NoSuchElementException> {
                mockDataSource.retrieveBank(accountNumber)
            }

            // then
            assertThat(error.message).isEqualTo("Could not find a bank with account number $accountNumber")
        }
    }
}