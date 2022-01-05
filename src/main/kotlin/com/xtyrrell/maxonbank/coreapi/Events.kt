package com.xtyrrell.maxonbank.coreapi

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.UUID

interface Event

sealed class Events {
    data class AccountOpened(
        @TargetAggregateIdentifier val accountId: UUID,
    ) : Event

    data class FundsDeposited(
        val ledgerEntryId: UUID,
        @TargetAggregateIdentifier val accountId: UUID,
        val amount: Money
    ) : Event

    data class FundsWithdrawn(
        val ledgerEntryId: UUID,
        @TargetAggregateIdentifier val accountId: UUID,
        val amount: Money
    ) : Event
}

