package com.r3.corda.lib.obligation.types

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.r3.corda.lib.tokens.contracts.types.TokenType
import net.corda.core.serialization.CordaSerializable
import java.security.PublicKey

/** All settlement methods require some key or account that a payment must be made to. */
@CordaSerializable
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "_type")
interface SettlementMethod {
    /** The public key, account number or whatever, that payment should be made to. */
    val accountToPay: Any
}