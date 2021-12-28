package com.xtyrrell.maxonbank.web

import com.xtyrrell.maxonbank.query.LedgerEntryView
import com.xtyrrell.maxonbank.query.AccountView
import com.xtyrrell.maxonbank.query.AccountViewRepository
import com.xtyrrell.maxonbank.query.LedgerEntryType
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * This is just an example controller to use to play with our Account entities and should be deleted
 * pretty soon.
 */

@RequestMapping("/test")
@RestController
class AccountsTestController(
    val accountRepository: AccountViewRepository
) {
    @GetMapping("/one-ac") fun addA(): AccountView {
        accountRepository.deleteAll()

        val account = AccountView(
            UUID.randomUUID()
        )

        accountRepository.save(account)

        return account
    }

    @GetMapping("/deposit") fun deposit(): AccountView {
        val account = accountRepository.findAll().first()

        val ledgerEntry = LedgerEntryView(account, UUID.randomUUID(), LedgerEntryType.DEPOSIT, 123)
        account.addLedgerEntry(ledgerEntry)

        accountRepository.save(account)

        return account
    }

    @GetMapping fun index() = accountRepository.findAll()
}
