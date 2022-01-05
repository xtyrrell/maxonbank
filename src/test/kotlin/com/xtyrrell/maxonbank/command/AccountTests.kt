package com.xtyrrell.maxonbank.command

import com.xtyrrell.maxonbank.coreapi.*
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.*
import java.util.*


class AccountTests {
    private lateinit var fixture: FixtureConfiguration<Account>

    @BeforeEach
    fun setUp() {
        fixture = AggregateTestFixture(Account::class.java)
    }

    @Nested
    @DisplayName("Commands.OpenAccount")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class OpenAccountCommandTests {
        @Test
        fun `should open an account`() {
            val accountId = UUID.randomUUID()

            fixture.givenNoPriorActivity()
                .`when`(Commands.OpenAccount(accountId))
                .expectSuccessfulHandlerExecution()
                .expectEvents(Events.AccountOpened(accountId))
        }
    }

    @Nested
    @DisplayName("Commands.DepositFunds")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DepositFundsCommandTests {
        @Test
        fun `should allow depositing funds for a non-zero positive amount`() {
            val accountId = UUID.randomUUID()
            val ledgerEntryId = UUID.randomUUID()

            fixture
                .given(Events.AccountOpened(accountId))
                .`when`(Commands.DepositFunds(ledgerEntryId, accountId, 500,))
                .expectSuccessfulHandlerExecution()
                .expectEvents(Events.FundsDeposited(ledgerEntryId, accountId, 500))
        }

        @Test
        fun `should prevent depositing funds for a negative amount`() {
            val accountId = UUID.randomUUID()
            val ledgerEntryId = UUID.randomUUID()

            fixture
                .given(Events.AccountOpened(accountId))
                .`when`(Commands.DepositFunds(ledgerEntryId, accountId, -500))
                .expectException(FundsDepositException::class.java)
        }

        @Test
        fun `should prevent depositing funds for a zero amount`() {
            val accountId = UUID.randomUUID()
            val ledgerEntryId = UUID.randomUUID()

            fixture
                .given(Events.AccountOpened(accountId))
                .`when`(Commands.DepositFunds(ledgerEntryId, accountId, 0))
                .expectException(FundsDepositException::class.java)
        }
    }

    @Nested
    @DisplayName("Commands.WithdrawFunds")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class WithdrawFundsCommandTests {
        @Test
        fun `should allow withdrawing funds for a non-zero positive amount`() {
            val accountId = UUID.randomUUID()
            val depositLedgerEntryId = UUID.randomUUID()
            val withdrawLedgerEntryId = UUID.randomUUID()

            fixture
                .given(Events.AccountOpened(accountId), Events.FundsDeposited(depositLedgerEntryId, accountId, 1000))
                .`when`(Commands.WithdrawFunds(withdrawLedgerEntryId, accountId, 500))
                .expectSuccessfulHandlerExecution()
                .expectEvents(Events.FundsWithdrawn(withdrawLedgerEntryId, accountId, 500))
        }

        @Test
        fun `should prevent withdrawing funds for a zero amount`() {
            val accountId = UUID.randomUUID()
            val depositLedgerEntryId = UUID.randomUUID()
            val withdrawLedgerEntryId = UUID.randomUUID()

            fixture
                .given(Events.AccountOpened(accountId), Events.FundsDeposited(depositLedgerEntryId, accountId, 1000))
                .`when`(Commands.WithdrawFunds(withdrawLedgerEntryId, accountId, 0))
                .expectException(FundsWithdrawalException::class.java)
        }

        @Test
        fun `should prevent withdrawing funds for a negative amount`() {
            val accountId = UUID.randomUUID()
            val depositLedgerEntryId = UUID.randomUUID()
            val withdrawLedgerEntryId = UUID.randomUUID()

            fixture
                .given(Events.AccountOpened(accountId), Events.FundsDeposited(depositLedgerEntryId, accountId, 1000))
                .`when`(Commands.WithdrawFunds(withdrawLedgerEntryId, accountId, -500))
                .expectException(FundsWithdrawalException::class.java)
        }

        @Test
        fun `should prevent withdrawing funds greater than account balance`() {
            val accountId = UUID.randomUUID()
            val withdrawLedgerEntryId = UUID.randomUUID()
            val depositLedgerEntryId = UUID.randomUUID()

            fixture
                .given(Events.AccountOpened(accountId), Events.FundsDeposited(depositLedgerEntryId, accountId, 500))
                .`when`(Commands.WithdrawFunds(withdrawLedgerEntryId, accountId, 600))
                .expectException(FundsWithdrawalException::class.java)
        }
    }
}