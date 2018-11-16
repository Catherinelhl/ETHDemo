package bcaasc.io.ethdemo.contract;

import java.io.File;

/**
 * @author catherine.brainwilliam
 * @since 2018/11/15
 */
public interface MainContract {

    interface View {
        void success(String info);
        void failure(String info);
    }

    interface Presenter {
        void createAccount();
        void loadWallet(File file);
        void connectETHClient();
        void getBalance();
        void getTXList();
        void publishTX(String gas,String addressTo,String amountString);
        void checkTXInfo();
    }
}
