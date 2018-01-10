package ink.qtum.org.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import javax.inject.Inject;

import autodagger.AutoInjector;
import de.adorsys.android.securestoragelibrary.SecurePreferences;
import ink.qtum.org.QtumApp;
import ink.qtum.org.inkqtum.R;

import static ink.qtum.org.models.Constants.LAST_SYNCED_BLOCK;
import static ink.qtum.org.models.Constants.QTUM_SHARED_PREFS;

/**
 * Created by SV on 06.01.2018.
 */
@AutoInjector(QtumApp.class)
public class SharedManager {

    @Inject
    Context context;

    private SharedPreferences settings;

    public SharedManager() {
        QtumApp.getAppComponent().inject(this);
        settings = context.getSharedPreferences(QTUM_SHARED_PREFS, Context.MODE_PRIVATE);
    }

    public String getLastSyncedBlock() {
        return SecurePreferences.getStringValue(LAST_SYNCED_BLOCK, "");
    }

    public void setLastSyncedBlock(String wallet) {
        try {
            SecurePreferences.setValue(LAST_SYNCED_BLOCK, wallet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearLastSyncedBlock(){
        try {
            SecurePreferences.clearAllValues();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
