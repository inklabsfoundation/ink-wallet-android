package ink.qtum.org.rest;

import ink.qtum.org.models.response.TransactionsListResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by SV on 28.12.2017.
 */

public interface InsightApi {
    @GET("insight-api/addr/{address}/balance")
    Call<ResponseBody> getBalance(@Path("address") String address);

    //TODO: implement per-page history loading
    @GET("/insight-api/addrs/{address}/txs?from=0&to=50")
    Call<TransactionsListResponse> getTransactions(@Path("address") String address);
}
