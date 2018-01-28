package ink.qtum.org.rest;

import java.util.List;

import ink.qtum.org.models.contract.UnspentOutput;
import ink.qtum.org.models.response.TransactionsInkListResponse;
import ink.qtum.org.models.response.TransactionsQtumListResponse;
import ink.qtum.org.models.response.SendTxResponse;
import ink.qtum.org.models.response.UtxoItemResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    Call<TransactionsQtumListResponse> getTransactions(@Path("address") String address);

    @GET("/insight-api/addr/{address}/utxo")
    Call<List<UtxoItemResponse>> getUTXOByAddress(@Path("address") String address);

    @GET("/insight-api/addr/{address}/utxo")
    Call<List<UnspentOutput>> getUTXOByAddressForToken(@Path("address") String address);

    @POST("/insight-api/tx/send")
    @FormUrlEncoded
    Call<SendTxResponse> sendRawTx(@Field("rawtx") String rawTx);

    @GET("/insight-api/tokens/{token}/addresses/{address}/balance")
    Call <ResponseBody> getTokenBalance(@Path("token") String token, @Path("address") String address);

    @GET("/insight-api/tokens/{token}/transactions")
    Call <TransactionsInkListResponse> getTokenTransactions(@Path("token") String token, @Query("addresses") String... addresses);
}
