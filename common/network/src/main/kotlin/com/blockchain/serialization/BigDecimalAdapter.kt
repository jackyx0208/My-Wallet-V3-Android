package com.blockchain.serialization

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.math.BigDecimal

class BigDecimalAdapter {

    @FromJson
    fun fromJson(json: String): BigDecimal = json.toBigDecimal()

    @ToJson
    fun toJson(bigDecimal: BigDecimal): String = bigDecimal.toPlainString()
}
