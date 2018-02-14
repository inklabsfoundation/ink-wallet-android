package ink.qtum.org;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;

import javax.inject.Singleton;

import autodagger.AutoComponent;
import ink.qtum.org.dependencies.AppModule;
import io.fabric.sdk.android.Fabric;

/**
 * Created by SV on 21.12.2017.
 */

@AutoComponent(
        modules = {AppModule.class}
)
@Singleton
public class QtumApp extends Application {


    private static QtumAppComponent appComponent;
    private static Context appContext;
    private static boolean isAccessAllowed = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        appComponent = buildAppComponent();
        appContext = this;
    }

    public static QtumAppComponent getAppComponent() {
        return appComponent;
    }

    private QtumAppComponent buildAppComponent() {
        return DaggerQtumAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static boolean isIsAccessAllowed() {
        return isAccessAllowed;
    }

    public static void setIsAccessAllowed(boolean isAccessAllowed) {
        QtumApp.isAccessAllowed = isAccessAllowed;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        setIsAccessAllowed(false);
    }
}
