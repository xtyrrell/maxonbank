package com.xtyrrell.maxonbank.command

import com.xtyrrell.maxonbank.coreapi.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.spring.stereotype.Aggregate
import java.util.*

@Aggregate
class Account() {

    // Question: Is my use of lateinit here correct? I am using it because
    // `aggregateId` is initialized in the AccountOpenedEvent handler.
    // So I know it's not null whenever its used, but I don't want to have to
    // manually handle nullability.
    @AggregateIdentifier
    private lateinit var accountId: UUID

    private var balance = 0

    @CommandHandler
    constructor(command: OpenAccountCommand) : this() {
        val aggregateId = UUID.randomUUID()
        apply(AccountOpenedEvent(aggregateId))
    }

    @CommandHandler
    fun handle(command: DepositFundsCommand) {
        // Prohibit deposits of zero or negative amounts
        if (command.amount <= 0) {
            throw FundsDepositException("The amount you want to deposit cannot be zero or negative")
        }

        apply(FundsDepositedEvent(accountId, command.amount))
    }

    @CommandHandler
    fun handle(command: WithdrawFundsCommand) {
        // Only allow withdrawals of amounts >= current balance
        if (command.amount > balance) {
            throw FundsWithdrawalException("The amount you want to withdraw cannot exceed your balance")
        }

        // Prohibit withdrawals of zero or negative amounts
        if (command.amount <= 0) {
            throw FundsWithdrawalException("The amount you want to withdraw cannot be zero or negative")
        }

        apply(FundsWithdrawnEvent(accountId, command.amount))
    }

    @EventSourcingHandler
    fun on(event: AccountOpenedEvent) {
        accountId = event.accountId
    }

    @EventSourcingHandler
    fun on(event: FundsDepositedEvent) {
        balance += event.amount
    }

    @EventSourcingHandler
    fun on(event: FundsWithdrawnEvent) {
        balance -= event.amount
    }
}