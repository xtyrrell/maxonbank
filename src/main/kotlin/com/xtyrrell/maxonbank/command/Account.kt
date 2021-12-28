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
    constructor(command: OpenAccountCommand): this() {
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

        apply(FundsWithdrawnEvent(accountId, command.amount))
    }

    @EventSourcingHandler
    fun on(event: AccountOpenedEvent) {
        // Is this right? In the example I'm watching https://youtu.be/7oy4w5THFEU?t=317
        // they use event.getAccountId() -- I thought this should be autogenerated because
        // we are using Kotlin and have declared `val ` AccountOpenedEvent.
        accountId = event.accountId

        // Question: Instead of setting balance = 0 at the top of this class, we could set it here
//        balance = 0
        // Is this preferred? If so, do we make balance a lateinit var (we can't,
        // so do we instead use a NotNull Delegate like IntelliJ suggests:
//        var balance by Delegates.notNull<Int>()
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