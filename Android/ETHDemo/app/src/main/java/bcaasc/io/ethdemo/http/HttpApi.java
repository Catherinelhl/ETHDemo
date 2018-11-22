package bcaasc.io.ethdemo.http;

import bcaasc.io.ethdemo.bean.ETHTXListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HttpApi {


    /**
     * asc ：
     * desc：
     * /api?module=account
     * &action=txlist
     * &address=0xddbd2b932c763ba5b1b7ae3b362eac3e8d40121a
     * &startblock=0
     * &endblock=99999999
     * &sort=asc
     * &apikey=YourApiKeyToken
     *
     * @return
     */
    /*获取交易记录*/
    @GET("/api")
    Call<ETHTXListResponse> getTransactionList(@Query("module") String module,
                                               @Query("action") String action,
                                               @Query("address") String address,
                                               @Query("startblock") String startblock,
                                               @Query("endblock") String endblock,
                                               @Query("sort") String sort,
                                               @Query("apikey") String apikey);
}
