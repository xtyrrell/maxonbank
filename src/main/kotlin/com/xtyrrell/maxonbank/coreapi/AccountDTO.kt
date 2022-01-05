package com.xtyrrell.maxonbank.coreapi

import java.util.UUID

data class AccountDTO(
    val accountId: UUID,
    var balance: Money,
)
