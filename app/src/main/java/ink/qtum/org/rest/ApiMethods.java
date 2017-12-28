package ink.qtum.org.rest;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import ink.qtum.org.models.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SV on 28.12.2017.
 */

public class ApiMethods {
    public interface RequestListener {
        void onSuccess(Object response);

        void onFailure(String msg);
    }



    static InsightApi createInsightApi() {
        return getBaseApi(Constants.INSIGHT_API_BASE_URL).create(InsightApi.class);
    }



    private static Retrofit getBaseApi(String baseUrl) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        OkHttpClient client = httpClient
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

    static void makeRequest(Call call, @Nullable final RequestListener listener) {
        call.enqueue(new Callback<Object>() {
                         @Override
                         public void onResponse(Call<Object> call, Response<Object> response) {
                             if (response.isSuccessful()) {
                                 //getting response from server
                                 Object serverResponse = response.body();
                                 if (listener != null) {
                                     listener.onSuccess(serverResponse);
                                 }
                             } else {
                                 try {
                                     Log.d("svcom ", "clearText body " + response.errorBody().string());
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                                 if (listener != null) {
                                     try {
                                         listener.onFailure(response.errorBody().string());
                                     } catch (IOException e) {
                                         e.printStackTrace();
                                         listener.onFailure("Request clearText");
                                     }
                                 }
                             }
                         }

                         @Override
                         public void onFailure(Call<Object> call, Throwable t) {
                             Log.d("svcom", "failure " + t.getMessage() + " " + t.toString());
                             if (listener != null) {
                                 listener.onFailure(t.getMessage() + " " + t.toString());
                             }
                         }
                     }
        );
    }
}
