package com.mashiverse.mashit.utils.helpers.web3

import com.mashiverse.mashit.utils.ALCHEMY_BASE_URL
import com.mashiverse.mashit.utils.ALCHEMY_KEY
import com.mashiverse.mashit.utils.CONTRACT_ADDRESS
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.generated.Bytes32
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.abi.datatypes.generated.Uint32
import org.web3j.abi.datatypes.generated.Uint64
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.http.HttpService
import java.math.BigInteger

/**
 * Singleton helper:
 * Reads marketplace.listings(listingId).totalSold using Alchemy Polygon RPC.
 */
object SoldHelper {
    private const val RPC_URL = "${ALCHEMY_BASE_URL}v2/${ALCHEMY_KEY}"
    private val web3j: Web3j by lazy { Web3j.build(HttpService(RPC_URL)) }

    private const val TOTAL_SOLD_INDEX = 4
    private val OUTPUT_TYPES: List<TypeReference<out Type<*>>> = listOf(
        object : TypeReference<Address>() {},
        object : TypeReference<Address>() {},
        object : TypeReference<Uint256>() {},
        object : TypeReference<Uint64>() {},
        object : TypeReference<Uint64>() {},
        object : TypeReference<Uint64>() {},
        object : TypeReference<Uint64>() {},
        object : TypeReference<Uint32>() {},
        object : TypeReference<Uint64>() {},
        object : TypeReference<Bytes32>() {},
        object : TypeReference<Bool>() {}
    )

    @Throws(Exception::class)
    fun getTotalSold(listingId: Long): BigInteger = getTotalSold(BigInteger.valueOf(listingId))

    @Throws(Exception::class)
    fun getTotalSold(listingId: BigInteger): BigInteger {
        val function = Function(
            "listings",
            listOf(Uint256(listingId)),
            OUTPUT_TYPES
        )

        val data = FunctionEncoder.encode(function)
        val tx = Transaction.createEthCallTransaction(null, CONTRACT_ADDRESS, data)
        val resp = web3j.ethCall(tx, DefaultBlockParameterName.LATEST).send()

        if (resp.hasError()) throw RuntimeException("RPC error: ${resp.error.message}")

        val decoded = FunctionReturnDecoder.decode(resp.value, function.outputParameters)
        if (decoded.isEmpty()) throw RuntimeException("Decoded empty output. Wrong ABI output types/order.")

        return when (val totalSoldField = decoded[TOTAL_SOLD_INDEX]) {
            is Uint64 -> totalSoldField.value
            is Uint256 -> totalSoldField.value
            else -> throw IllegalStateException("totalSold field is not Uint64/Uint256. Got: ${totalSoldField.typeAsString}")
        }
    }
}