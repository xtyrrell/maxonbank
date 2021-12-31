package com.xtyrrell.maxonbank.coreapi

/*
 * We are being very naive about how we handle money.
 *
 * This is a placeholder for a money type that:
 * - precisely represents very large amounts (to at least the sum of all minted
 *      Rands).
 * - precisely stores cents, at least to two decimal places, and possibly more
 *      depending on the requirements of regulation (eg to support
 *      calculations on interest we might need to store amounts to eg 5
 *      decimal places).
 */
typealias Money = Int
