package ink.qtum.org;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import autodagger.AutoComponent;
import ink.qtum.org.dependencies.AppModule;

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

    @Override
    public void onCreate() {
        super.onCreate();
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
}
