package com.xtyrrell.maxonbank.query

import com.xtyrrell.maxonbank.coreapi.*
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class BankInfoProjector {
    private var totalBalance = 0

    @EventHandler
    fun on(event: Events.FundsDeposited) {
        totalBalance += event.amount
    }

    @EventHandler
    fun on(event: Events.FundsWithdrawn) {
        totalBalance -= event.amount
    }

    @QueryHandler
    fun handle(query: Queries.TotalBalance): Money {
        return totalBalance
    }
}
