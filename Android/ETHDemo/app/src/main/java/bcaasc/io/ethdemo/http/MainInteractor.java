package bcaasc.io.ethdemo.http;

import bcaasc.io.ethdemo.bean.ETHTXListResponse;
import bcaasc.io.ethdemo.http.retrofit.RetrofitFactory;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * @author catherine.brainwilliam
 * @since 2018/11/13
 */
public class MainInteractor {

    public void getTXList(String module, String action, String address, String startblock, String endblock,
                          String sort, String apikey, Callback<ETHTXListResponse> callBackListener) {
        HttpApi httpApi = RetrofitFactory.getInstance().create(HttpApi.class);
        Call<ETHTXListResponse> call = httpApi.getTransactionList(module, action, address, startblock, endblock, sort, apikey);
        call.enqueue(callBackListener);
    }
}
