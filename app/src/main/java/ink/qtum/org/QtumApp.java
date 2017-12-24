package ink.qtum.org;

import android.app.Application;

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

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = buildAppComponent();
    }

    public static QtumAppComponent getAppComponent() {
        return appComponent;
    }

    private QtumAppComponent buildAppComponent() {
        return DaggerQtumAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
