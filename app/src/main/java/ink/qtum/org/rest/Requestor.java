package ink.qtum.org.rest;

import android.util.Log;

import retrofit2.Call;

/**
 * Created by SV on 28.12.2017.
 */

public class Requestor {

    public static void getBalance(String address, ApiMethods.RequestListener listener) {
        Call call = ApiMethods.createInsightApi().getBalance(address);
        Log.d("svcom", "path " + call.request());
        ApiMethods.makeRequest(call, listener);
    }
}
