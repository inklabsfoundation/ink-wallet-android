package ink.qtum.org.rest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by SV on 28.12.2017.
 */

public interface InsightApi {
    @GET("insight-api/addr/{address}/balance")
    Call<ResponseBody> getBalance(@Path("address") String address);
}
