package piuk.blockchain.androidcore.data.ethereum.models

import info.blockchain.wallet.ethereum.data.EthAddressResponse
import info.blockchain.wallet.ethereum.data.EthAddressResponseMap
import info.blockchain.wallet.ethereum.data.EthTransaction
import java.math.BigInteger

/**
 * A model that merges the transactions and balances of multiple ETH responses into a single object.
 */
class CombinedEthModel(private val ethAddressResponseMap: EthAddressResponseMap) {

    fun getTotalBalance(): BigInteger {
        val values = ethAddressResponseMap.ethAddressResponseMap.values
        var total = BigInteger.ZERO
        for (it in values) {
            total += total.add(it?.balance ?: BigInteger.ZERO)
        }
        return total
    }

    fun getTransactions(): List<EthTransaction> {
        val values = ethAddressResponseMap.ethAddressResponseMap.values
        val transactions = mutableListOf<EthTransaction>()
        for (it in values) {
            transactions.addAll(it.transactions)
        }
        return transactions.toList()
    }

    /**
     * Main eth account
     */
    fun getAddressResponse(): EthAddressResponse? =
        ethAddressResponseMap.ethAddressResponseMap.values.first()

    fun getNonce(): BigInteger {
        return BigInteger.valueOf(getAddressResponse()!!.nonce.toLong())
    }
}
