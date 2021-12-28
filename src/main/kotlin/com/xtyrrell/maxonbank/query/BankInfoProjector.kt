package com.xtyrrell.maxonbank.query

import com.xtyrrell.maxonbank.coreapi.*
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class BankInfoProjector {
    private var totalBalance = 0

    @EventHandler
    fun on(event: FundsDepositedEvent) {
        totalBalance += event.amount
    }

    @EventHandler
    fun on(event: FundsWithdrawnEvent) {
        totalBalance -= event.amount
    }

    @QueryHandler
    fun handle(query: GetTotalAvailableBalanceQuery): Money {
        return totalBalance
    }
}
