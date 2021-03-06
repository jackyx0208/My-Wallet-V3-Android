package com.blockchain.nabu.service.nabu

import com.blockchain.android.testutils.rxInit
import com.blockchain.nabu.FakeAuthenticator
import com.blockchain.nabu.USD
import com.blockchain.nabu.api.nabu.Nabu
import com.blockchain.nabu.models.responses.nabu.KycTierLevel
import com.blockchain.nabu.models.responses.nabu.KycTierState
import com.blockchain.nabu.models.responses.nabu.KycTiers
import com.blockchain.nabu.models.responses.nabu.Limits
import com.blockchain.nabu.models.responses.nabu.Tier
import com.blockchain.nabu.models.responses.nabu.TierUpdateJson
import com.blockchain.nabu.models.responses.nabu.Tiers
import com.blockchain.nabu.service.NabuTierService
import com.blockchain.nabu.util.fakefactory.nabu.FakeKycTiersFactory
import com.blockchain.nabu.util.fakefactory.nabu.FakeNabuSessionTokenFactory
import com.blockchain.testutils.waitForCompletionWithoutErrors
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import info.blockchain.balance.AssetCatalogue
import info.blockchain.balance.Money
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Rule
import org.junit.Test

class NabuTiersServiceTest {

    private val sessionToken = FakeNabuSessionTokenFactory.any
    private val nabu: Nabu = mock()
    private val assetCatalogues: AssetCatalogue = mock {
        on { fromNetworkTicker("USD") }.thenReturn(USD)
    }
    private val authenticator: FakeAuthenticator = FakeAuthenticator(sessionToken)

    private val subject: NabuTierService = NabuTierService(nabu, assetCatalogues, authenticator)

    @get:Rule
    val rx = rxInit {
        ioTrampoline()
    }

    @Test
    fun `get tiers`() {

        whenever(
            nabu.getTiers(sessionToken.authHeader)
        ).thenReturn(
            Single.just(FakeKycTiersFactory.any)
        )

        subject.tiers()
            .test()
            .waitForCompletionWithoutErrors()
            .assertValue {
                it == KycTiers(
                    Tiers(
                        mapOf(
                            KycTierLevel.BRONZE to
                                Tier(
                                    KycTierState.Verified,
                                    Limits(null, null)
                                ),
                            KycTierLevel.SILVER to
                                Tier(
                                    KycTierState.Pending,
                                    Limits(null, Money.fromMajor(USD, 1000.0.toBigDecimal()))
                                ),
                            KycTierLevel.GOLD to
                                Tier(
                                    KycTierState.None,
                                    Limits(
                                        Money.fromMajor(USD, 25000.0.toBigDecimal()), null
                                    )
                                )
                        )
                    )
                )
            }
    }

    @Test
    fun `set tier`() {
        val selectedTier = 1

        whenever(
            nabu.setTier(TierUpdateJson(selectedTier), sessionToken.authHeader)
        ).thenReturn(
            Completable.complete()
        )

        subject.setUserTier(selectedTier)
            .test()
            .waitForCompletionWithoutErrors()
    }
}
