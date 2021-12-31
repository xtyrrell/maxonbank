package com.xtyrrell.maxonbank.coreapi

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class AccountOpenedEvent(
    @TargetAggregateIdentifier val accountId: UUID,
)

data class FundsDepositedEvent(
    @TargetAggregateIdentifier val accountId: UUID,
    val amount: Money
)

data class FundsWithdrawnEvent(
    @TargetAggregateIdentifier val accountId: UUID,
    val amount: Money
)
