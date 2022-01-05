package com.xtyrrell.maxonbank.coreapi

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.UUID

interface Command

sealed class Commands {
    data class OpenAccount(
        @TargetAggregateIdentifier val accountId: UUID
    ) : Command

    data class DepositFunds(
        val ledgerEntryId: UUID,
        @TargetAggregateIdentifier val accountId: UUID,
        val amount: Money
    ) : Command

    data class WithdrawFunds(
        val ledgerEntryId: UUID,
        @TargetAggregateIdentifier val accountId: UUID,
        val amount: Money
    ) : Command
}

