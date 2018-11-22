package bcaasc.io.ethdemo.contract;

import java.io.File;
import java.math.BigInteger;

/**
 * @author catherine.brainwilliam
 * @since 2018/11/15
 */
public interface MainContract {

    interface View {
        void success(String info);
        void failure(String info);

        void gasPriceSuccess(BigInteger bigInteger);
        void gasPriceFailure(String bigInteger);
    }

    interface Presenter {
        void createAccount();
        void getGasPrice();
        void loadWallet(File file);
        void connectETHClient();
        void getBalance();
        void getTXList();
        void publishTX(BigInteger bigInteger,String addressTo,String amountString);
        void checkTXInfo();
        void cancelSubscribe();
    }
}
