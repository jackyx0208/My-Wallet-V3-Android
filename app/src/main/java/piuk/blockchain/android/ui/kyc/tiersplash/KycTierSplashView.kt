package piuk.blockchain.android.ui.kyc.tiersplash

import androidx.annotation.StringRes
import androidx.navigation.NavDirections
import com.blockchain.nabu.models.responses.nabu.KycTiers
import piuk.blockchain.android.ui.base.View

interface KycTierSplashView : View {

    fun navigateTo(directions: NavDirections, tier: Int)

    fun showError(@StringRes message: Int)

    fun renderTiersList(tiers: KycTiers)
}
