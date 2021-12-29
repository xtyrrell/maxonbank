package com.xtyrrell.maxonbank.query

import com.xtyrrell.maxonbank.coreapi.*
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.axonframework.test.matchers.Matchers
import org.junit.jupiter.api.*
import java.util.*

// TODO: Write tests for the query model

class AccountTests {
    @BeforeEach
    fun setUp() {
        //fixture = AggregateTestFixture(Account::class.java)
    }

    @Nested
    @DisplayName("OpenAccountCommand")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class OpenAccountCommandTests {
        @Test
        fun `should open an account`() {
            //fixture.givenNoPriorActivity()
            //    .`when`(OpenAccountCommand())
            //    .expectSuccessfulHandlerExecution()
            //    .expectEventsMatching(
            //        Matchers.exactSequenceOf(
            //            Matchers.messageWithPayload(
            //                Matchers.matches { payload: AccountOpenedEvent ->
            //                    // This is weird, but I think it works. If we can cast [payload] to an
            //                    // AccountOpenedEvent, then it must be one. If [payload] is any other type, I believe
            //                    // this will throw an exception.
            //                    // Still, it would be nicer to rather do this check at runtime, something like
            //                    // `return payload is AccountOpenedEvent`
            //                    true
            //                }
            //            )
            //        )
            //    )
        }
    }
}