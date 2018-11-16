package bcaasc.io.ethdemo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import bcaasc.io.ethdemo.R;
import bcaasc.io.ethdemo.constants.Constants;
import bcaasc.io.ethdemo.contract.MainContract;
import bcaasc.io.ethdemo.presenter.MainPresenterImp;
import bcaasc.io.ethdemo.tool.LogTool;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/11/15
 */
public class MainNewActivity extends Activity implements MainContract.View {

    private String TAG = MainNewActivity.class.getSimpleName();

    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.btn_get_balance)
    Button btnGetBalance;
    @BindView(R.id.btn_get_tx_list)
    Button btnGetTxList;
    @BindView(R.id.btn_push)
    Button btnPush;
    @BindView(R.id.btn_get_tx_status)
    Button btnGetTxStatus;
    @BindView(R.id.et_content)
    EditText etContent;

    private MainContract.Presenter presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initListener();

    }

    private void initView() {
        tvAddress.setText(Constants.address);
        presenter = new MainPresenterImp(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                presenter.connectETHClient();
            }
        }).start();
    }

    private void initListener() {
        RxView.clicks(btnGetBalance).throttleFirst(Constants.SleepTime800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                presenter.getBalance();

                            }
                        }).start();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(btnGetTxList).throttleFirst(Constants.SleepTime800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                presenter.getTXList();

                            }
                        }).start();


                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(btnPush).throttleFirst(Constants.SleepTime800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                presenter.publishTX();

                            }
                        }).start();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(btnGetTxStatus).throttleFirst(Constants.SleepTime800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        getExternalFile();
                        LogTool.d(TAG, file);
                        if (file != null) {
                            checkWriteStoragePermission(MainNewActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void success(String info) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                etContent.setText(info);
            }
        });
    }

    @Override
    public void failure(String info) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                etContent.setText(info);
            }
        });
    }

    //得到需要存储当前keystore信息的文件
    private File file;

    /*得到一个存储当前keystore的文件地址*/
    private void getExternalFile() {
        //得到需要创建的文件夹
        File rootFile = new File(getExternalFilesDir("bcaas").getAbsolutePath());
        if (!rootFile.exists()) {
            rootFile.mkdir();
        }
        //得到需要创建用来存储信息的文件,文件夹是一个以当前钱包地址命名的txt
        file = new File(rootFile, "/keystore");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                LogTool.e(TAG, e.getMessage());
            }
        }
    }


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    /**
     * 检查当前读写权限
     *
     * @param activity
     */
    public void checkWriteStoragePermission(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            } else {
                presenter.loadWallet(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogTool.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //这里已经获取到了摄像头的权限，想干嘛干嘛了可以
                    presenter.loadWallet(file);
                } else {
                    //这里是拒绝给APP摄像头权限，给个提示什么的说明一下都可以。
                }
                break;
        }
    }

}
