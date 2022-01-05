package com.xtyrrell.maxonbank.command

import com.xtyrrell.maxonbank.coreapi.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.spring.stereotype.Aggregate
import java.util.UUID

@Aggregate
class Account() {

    @AggregateIdentifier
    private lateinit var accountId: UUID

    private var balance = 0

    @CommandHandler
    constructor(command: Commands.OpenAccount) : this() {
        apply(Events.AccountOpened(command.accountId))
    }

    @CommandHandler
    fun handle(command: Commands.DepositFunds) {
        // Prohibit deposits of zero or negative amounts
        if (command.amount <= 0) {
            throw FundsDepositException("The amount you want to deposit cannot be zero or negative")
        }

        apply(Events.FundsDeposited(command.ledgerEntryId, accountId, command.amount))
    }

    @CommandHandler
    fun handle(command: Commands.WithdrawFunds) {
        // Only allow withdrawals of amounts >= current balance
        if (command.amount > balance) {
            throw FundsWithdrawalException("The amount you want to withdraw cannot exceed your balance")
        }

        // Prohibit withdrawals of zero or negative amounts
        if (command.amount <= 0) {
            throw FundsWithdrawalException("The amount you want to withdraw cannot be zero or negative")
        }

        apply(Events.FundsWithdrawn(command.ledgerEntryId, accountId, command.amount))
    }

    @EventSourcingHandler
    fun on(event: Events.AccountOpened) {
        accountId = event.accountId
    }

    @EventSourcingHandler
    fun on(event: Events.FundsDeposited) {
        balance += event.amount
    }

    @EventSourcingHandler
    fun on(event: Events.FundsWithdrawn) {
        balance -= event.amount
    }
}