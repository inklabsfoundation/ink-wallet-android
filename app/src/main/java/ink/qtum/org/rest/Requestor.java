package ink.qtum.org.rest;

import android.util.Log;

import ink.qtum.org.models.Constants;
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

    public static void getTransactions(String address, ApiMethods.RequestListener listener) {
        Call call = ApiMethods.createInsightApi().getTransactions(address);
        Log.d("svcom", "path " + call.request());
        ApiMethods.makeRequest(call, listener);
    }

    public static void getUTXOList(String address, ApiMethods.RequestListener listener) {
        Call call = ApiMethods.createInsightApi().getUTXOByAddress(address);
        Log.d("Request", "path " + call.request());
        ApiMethods.makeRequest(call, listener);
    }

    public static void getUTXOListForToken(String address, ApiMethods.RequestListener listener) {
        Call call = ApiMethods.createInsightApi().getUTXOByAddressForToken(address);
        Log.d("Request", "path " + call.request());
        ApiMethods.makeRequest(call, listener);
    }

    public static void sendRawTx(String rawTx, ApiMethods.RequestListener listener){
        Call call = ApiMethods.createInsightApi().sendRawTx(rawTx);
        Log.d("Request", "path " + call.request());
        ApiMethods.makeRequest(call, listener);
    }

    public static void getInkBalance(String address, ApiMethods.RequestListener listener) {
        Call call = ApiMethods.createInsightApi().getTokenBalance(Constants.INK_CONTRACT_ADDRESS_BASE58, address);
        Log.d("svcom", "path " + call.request());
        ApiMethods.makeRequest(call, listener);
    }

    public static void getInkTransactions(String address, ApiMethods.RequestListener listener) {
        Call call = ApiMethods.createInsightApi().getTokenTransactions(Constants.INK_CONTRACT_ADDRESS_BASE58, address);
        Log.d("svcom", "path " + call.request());
        ApiMethods.makeRequest(call, listener);
    }

}
