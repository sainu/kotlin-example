package com.example.demo.datasource.mock

import com.example.demo.model.Bank
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
            assertThat(banks).anyMatch { it.transactionFee != 0 }
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
            assertThat(bank.transactionFee).isEqualTo(17)
        }

        @Test
        fun `should throw NoSuchElementException if the account number does not exist`() {
            // given
            val accountNumber = "does_not_exist"

            // when/then
            assertThrows<NoSuchElementException> {
                mockDataSource.retrieveBank(accountNumber)
            }
        }
    }

    @Nested
    @DisplayName("createBank()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class CreateBank {
        @Test
        fun `should return the bank same as input`() {
            // given
            val newBank = Bank("0000", 13.0, 3)

            // when
            val bank = mockDataSource.createBank(newBank)

            // then
            assertThat(bank).isEqualTo(newBank)
        }

        @Test
        fun `should append the bank to banks`() {
            // given
            val newBank = Bank("0101", 13.0, 3)

            // when
            mockDataSource.createBank(newBank)

            // then
            assertThat(mockDataSource.retrieveBanks().last()).isEqualTo(newBank)
        }

        @Test
        fun `should throw IllegalArgumentException if bank with given account name already exists`() {
            // given
            val newBank = Bank("1234", 13.0, 3)

            // when/then
            assertThrows<IllegalArgumentException> {
                mockDataSource.createBank(newBank)
            }
        }

        @Test
        fun `should not append the bank if bank with given account name already exists`() {
            // given
            val newBank = Bank("1234", 13.0, 3)

            // when
            assertThrows<IllegalArgumentException> {
                mockDataSource.createBank(newBank)
            }

            // then
            assertThat(mockDataSource.retrieveBanks().last()).isNotEqualTo(newBank)
        }
    }

    @Nested
    @DisplayName("updateBank()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class UpdateBank {
        @Test
        fun `should return the bank same as input`() {
            // given
            val currentBank = mockDataSource.retrieveBanks().first()
            val updatedBank = Bank(
                accountNumber = currentBank.accountNumber,
                trust = currentBank.trust + 1.0,
                transactionFee = currentBank.transactionFee + 1
            )

            // when/then
            assertThat(
                mockDataSource.updateBank(updatedBank)
            ).isEqualTo(updatedBank)
        }

        @Test
        fun `should update the bank with given account number`() {
            // given
            val currentBank = mockDataSource.retrieveBanks().first()
            val updatedBank = Bank(
                accountNumber = currentBank.accountNumber,
                trust = currentBank.trust + 1.0,
                transactionFee = currentBank.transactionFee + 1
            )

            // when
            mockDataSource.updateBank(updatedBank)

            // then
            assertThat(
                mockDataSource.retrieveBank(updatedBank.accountNumber)
            ).isEqualTo(updatedBank)
        }

        @Test
        fun `should throw NoSuchElementException if no bank with given account number exists`() {
            // given
            val bank = Bank(
                accountNumber = "does_not_exist",
                trust = 1.0,
                transactionFee = 1
            )

            // when/then
            assertThrows<NoSuchElementException> {
                mockDataSource.updateBank(bank)
            }
        }

        @Test
        fun `should not update banks if no bank with given account number exists`() {
            // given
            val beforeBanks = mockDataSource.retrieveBanks()
            val bank = Bank(
                accountNumber = "does_not_exist",
                trust = 1.0,
                transactionFee = 1
            )

            // when
            assertThrows<NoSuchElementException> {
                mockDataSource.updateBank(bank)
            }

            // then
            assertThat(
                mockDataSource.retrieveBanks()
            ).isEqualTo(beforeBanks)
        }
    }

    @Nested
    @DisplayName("deleteBank()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteBank {
        @Test
        fun `should delete the bank with the given account number`() {
            // given
            val accountNumber = "1234"
            assertThat(
                mockDataSource.retrieveBank(accountNumber)
            ).isNotNull

            // when
            mockDataSource.deleteBank(accountNumber)

            // then
            assertThrows<NoSuchElementException> {
                mockDataSource.retrieveBank(accountNumber)
            }
        }

        @Test
        fun `should throw NoSuchElementException if no bank with the given account number exists`() {
            // given
            val invalidAccountNumber = "does_not_exist"

            // when/then
            assertThrows<NoSuchElementException> {
                mockDataSource.deleteBank(invalidAccountNumber)
            }
        }
    }
}