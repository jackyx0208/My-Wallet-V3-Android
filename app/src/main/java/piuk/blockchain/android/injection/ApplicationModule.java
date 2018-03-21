package piuk.blockchain.android.injection;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

import info.blockchain.wallet.util.PrivateKeyFactory;

import java.util.Locale;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import piuk.blockchain.android.data.access.AccessState;
import piuk.blockchain.android.data.api.EnvironmentSettings;
import piuk.blockchain.android.data.cache.DynamicFeeCache;
import piuk.blockchain.androidcore.utils.AESUtilWrapper;
import piuk.blockchain.android.util.AppUtil;
import piuk.blockchain.androidcore.data.currency.CurrencyFormatUtil;
import piuk.blockchain.androidcore.data.currency.CurrencyState;
import piuk.blockchain.androidcore.data.ethereum.EthereumAccountWrapper;
import piuk.blockchain.androidcore.data.rxjava.RxBus;
import piuk.blockchain.androidcore.utils.MetadataUtils;
import piuk.blockchain.androidcore.utils.PrefsUtil;


@SuppressWarnings("WeakerAccess")
@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    protected Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    protected PrefsUtil providePrefsUtil() {
        return new PrefsUtil(application);
    }

    @Provides
    @Singleton
    protected AppUtil provideAppUtil() {
        return new AppUtil(application);
    }

    @Provides
    protected AccessState provideAccessState() {
        return AccessState.getInstance();
    }

    @Provides
    protected AESUtilWrapper provideAesUtils() {
        return new AESUtilWrapper();
    }

    @Provides
    @Singleton
    protected DynamicFeeCache provideDynamicFeeCache() {
        return new DynamicFeeCache();
    }

    @Provides
    protected PrivateKeyFactory privateKeyFactory() {
        return new PrivateKeyFactory();
    }

    @Provides
    @Singleton
    protected NotificationManager provideNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Provides
    @Singleton
    protected RxBus provideRxBus() {
        return new RxBus();
    }

    @Provides
    @Singleton
    protected EnvironmentSettings provideDebugSettings() {
        return new EnvironmentSettings();
    }

    @Provides
    protected CurrencyState provideCurrencyState() {
        return CurrencyState.getInstance();
    }

    @Provides
    protected MetadataUtils provideMetadataUtils() {
        return new MetadataUtils();
    }

    @Provides
    protected EthereumAccountWrapper provideEthereumAccountWrapper() {
        return new EthereumAccountWrapper();
    }

    @Provides
    protected CurrencyFormatUtil provideCurrencyFormatUtil() {
        return new CurrencyFormatUtil();
    }

    @Provides
    protected Locale provideLocale() {
        return Locale.getDefault();
    }
}
