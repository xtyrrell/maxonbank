package com.xtyrrell.maxonbank.command

import com.xtyrrell.maxonbank.coreapi.*
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.axonframework.test.matchers.Matchers
import org.junit.jupiter.api.*
import java.util.*


class AccountTests {
    private lateinit var fixture: FixtureConfiguration<Account>

    @BeforeEach
    fun setUp() {
        fixture = AggregateTestFixture(Account::class.java)
    }

    @Nested
    @DisplayName("OpenAccountCommand")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class OpenAccountCommandTests {
        @Test
        fun `should open an account`() {
            fixture.givenNoPriorActivity()
                .`when`(OpenAccountCommand())
                .expectSuccessfulHandlerExecution()
                .expectEventsMatching(
                    Matchers.exactSequenceOf(
                        Matchers.messageWithPayload(
                            Matchers.matches { payload: AccountOpenedEvent ->
                                // This is weird, but I think it works. If we can cast [payload] to an
                                // AccountOpenedEvent, then it must be one. If [payload] is any other type, I believe
                                // this will throw an exception.
                                // Still, it would be nicer to rather do this check at runtime, something like
                                // `return payload is AccountOpenedEvent`
                                true
                            }
                        )
                    )
                )
        }
    }

    @Nested
    @DisplayName("DepositFundsCommand")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DepositFundsCommandTests {
        @Test
        fun `should allow depositing funds for a non-zero positive amount`() {
            val accountId = UUID.randomUUID()

            fixture
                .given(AccountOpenedEvent(accountId))
                .`when`(DepositFundsCommand(accountId, 500))
                .expectSuccessfulHandlerExecution()
                .expectEvents(FundsDepositedEvent(accountId, 500))
        }

        @Test
        fun `should prevent depositing funds for zero or negative amount`() {
            val accountId = UUID.randomUUID()

            fixture
                .given(AccountOpenedEvent(accountId))
                .`when`(DepositFundsCommand(accountId, -500))
                .expectException(FundsDepositException::class.java)

            fixture
                .given(AccountOpenedEvent(accountId))
                .`when`(DepositFundsCommand(accountId, 0))
                .expectException(FundsDepositException::class.java)
        }
    }

    @Nested
    @DisplayName("WithdrawFundsCommand")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class WithdrawFundsCommandTests {
        @Test
        fun `should allow withdrawing funds for a non-zero positive amount`() {
            val accountId = UUID.randomUUID()

            fixture
                .given(AccountOpenedEvent(accountId), FundsDepositedEvent(accountId, 500))
                .`when`(WithdrawFundsCommand(accountId, 500))
                .expectSuccessfulHandlerExecution()
                .expectEvents(FundsWithdrawnEvent(accountId, 500))
        }

        @Test
        fun `should prevent withdrawing funds for a zero or negative amount`() {
            val accountId = UUID.randomUUID()

            fixture
                .given(AccountOpenedEvent(accountId), FundsDepositedEvent(accountId, 500))
                .`when`(WithdrawFundsCommand(accountId, -500))
                .expectException(FundsWithdrawalException::class.java)

            fixture
                .given(AccountOpenedEvent(accountId), FundsDepositedEvent(accountId, 500))
                .`when`(WithdrawFundsCommand(accountId, 0))
                .expectException(FundsWithdrawalException::class.java)
        }

        @Test
        fun `should prevent withdrawing funds greater than account balance`() {
            val accountId = UUID.randomUUID()

            fixture
                .given(AccountOpenedEvent(accountId), FundsDepositedEvent(accountId, 500))
                .`when`(WithdrawFundsCommand(accountId, 600))
                .expectException(FundsWithdrawalException::class.java)
        }
    }
}