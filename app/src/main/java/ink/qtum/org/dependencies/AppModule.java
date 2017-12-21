package ink.qtum.org.dependencies;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ink.qtum.org.managers.WalletManager;

/**
 * Created by SV on 21.12.2017.
 */

@Module
public class AppModule {

    private Context mContext;

    public AppModule(Context mContext) {
        this.mContext = mContext;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mContext;
    }

    @Provides
    @Singleton
    WalletManager provideWalletManager(Context context) {
        return new WalletManager(context);
    }
}

