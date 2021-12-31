package com.xtyrrell.maxonbank.coreapi

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

class OpenAccountCommand

data class DepositFundsCommand(
    @TargetAggregateIdentifier val accountId: UUID,
    /**
     * The amount to deposit, in rands.
     */
    val amount: Money
)

data class WithdrawFundsCommand(
    @TargetAggregateIdentifier val accountId: UUID,
    val amount: Money
)
