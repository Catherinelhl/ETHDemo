package bcaasc.io.ethdemo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import bcaasc.io.ethdemo.R;
import bcaasc.io.ethdemo.constants.Constants;
import bcaasc.io.ethdemo.constants.ETHParamConstants;
import bcaasc.io.ethdemo.constants.MessageConstants;
import bcaasc.io.ethdemo.contract.MainContract;
import bcaasc.io.ethdemo.presenter.MainPresenterImp;
import bcaasc.io.ethdemo.tool.LogTool;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding2.view.RxView;
import com.obt.qrcode.activity.CaptureActivity;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/11/15
 */
public class MainNewActivity extends AppCompatActivity implements MainContract.View {
    private String TAG = MainNewActivity.class.getSimpleName();
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.ll_my_address)
    LinearLayout llMyAddress;
    @BindView(R.id.tv_get_balance)
    TextView tvGetBalance;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.ll_my_balance)
    LinearLayout llMyBalance;
    @BindView(R.id.et_amount)
    EditText etAmount;
    @BindView(R.id.et_to_address)
    EditText etToAddress;
    @BindView(R.id.ib_scan)
    ImageButton ibScan;
    @BindView(R.id.ib_scan_address)
    ImageButton ibScanAddress;
    @BindView(R.id.ll_to_send_address)
    LinearLayout llToSendAddress;
    @BindView(R.id.btn_push)
    Button btnPush;
    @BindView(R.id.btn_get_tx_list)
    Button btnGetTxList;
    @BindView(R.id.ll_send_action)
    LinearLayout llSendAction;
    @BindView(R.id.tv_get_tx_status)
    TextView tvGetTxStatus;
    @BindView(R.id.tv_tx_hash)
    TextView tvTxHash;
    @BindView(R.id.ib_scan_hash)
    ImageButton ibScanHash;
    @BindView(R.id.ll_query)
    LinearLayout llQuery;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.et_fee)
    EditText etFee;
    @BindView(R.id.switch_net)
    Switch switchNet;
    @BindView(R.id.et_private_key)
    EditText etPrivateKey;
    @BindView(R.id.ib_scan_private_key)
    ImageButton ibScanPrivateKey;
    @BindView(R.id.ll_my_private_key)
    LinearLayout llMyPrivateKey;
    @BindView(R.id.ll_send_amount)
    LinearLayout llSendAmount;


    private MainContract.Presenter presenter;

    //获取当前动态获取的gasPrice
    private BigInteger gasPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initListener();
        getCameraPermission();

    }

    /*獲得照相機權限*/
    private void getCameraPermission() {
        if (Build.VERSION.SDK_INT > 22) { //这个说明系统版本在6.0之下，不需要动态获取权限。
            if (ContextCompat.checkSelfPermission(MainNewActivity.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //先判断有没有权限 ，没有就在这里进行权限的申请,否则说明已经获取到摄像头权限了 想干嘛干嘛
                ActivityCompat.requestPermissions(MainNewActivity.this,
                        new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_Permission_OK);

            }
        }
    }

    public static final int REQUEST_CODE_SCAN_RECEIVE_ADDRESS_OK = 0x001;
    public static final int REQUEST_CODE_SCAN_HASH_OK = 0x003;
    public static final int REQUEST_CODE_CAMERA_Permission_OK = 0x002;
    public static final int REQUEST_CODE_SCAN_ADDRESS_OK = 0x004;
    public static final int REQUEST_CODE_SCAN_PRIVATE_KEY_OK = 0x005;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                return;
            }
            String result = bundle.getString("result");
            switch (requestCode) {
                case REQUEST_CODE_SCAN_ADDRESS_OK:
                    if (etAddress != null) {
                        etAddress.setText(result);
                    }
                    presenter.getBalance(result);
                    break;
                case REQUEST_CODE_SCAN_RECEIVE_ADDRESS_OK:
                    if (etToAddress != null) {
                        etToAddress.setText(result);
                    }
                    break;
                case REQUEST_CODE_SCAN_HASH_OK:
                    if (tvTxHash != null) {
                        tvTxHash.setText(result);
                    }
                    break;
                case REQUEST_CODE_SCAN_PRIVATE_KEY_OK:
                    if (etPrivateKey != null) {
                        etPrivateKey.setText(result);
                    }
                    break;
            }

        }
    }


    private void initView() {
        etAmount.setText("0.0003");
        etFee.setText(Constants.feeString);
        presenter = new MainPresenterImp(this);
        //连接ETH客户端
        presenter.connectETHClient();
        presenter.getGasPrice();
    }

    @Deprecated
    private void loadingWalletByFile() {
        //导入钱包
        getExternalFile();
        LogTool.d(TAG, file);
        if (file != null) {
            checkWriteStoragePermission(MainNewActivity.this);
        }

    }

    private void initListener() {
        switchNet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchNet.setText(isChecked ? "ETH测试网络" : "ETH主网络");
                //刷新当前界面
                etAddress.setText(MessageConstants.EMPTY);
                etToAddress.setText(MessageConstants.EMPTY);
                etPrivateKey.setText(MessageConstants.EMPTY);
                tvTxHash.setText(MessageConstants.EMPTY);
                tvContent.setText(MessageConstants.EMPTY);
                //切换网络
                ETHParamConstants.isTest = isChecked;
            }
        });

        RxView.clicks(ibScan).throttleFirst(Constants.SleepTime800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        startActivityForResult(new Intent(MainNewActivity.this, CaptureActivity.class), REQUEST_CODE_SCAN_RECEIVE_ADDRESS_OK);

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(ibScanHash).throttleFirst(Constants.SleepTime800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        startActivityForResult(new Intent(MainNewActivity.this, CaptureActivity.class), REQUEST_CODE_SCAN_HASH_OK);

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(tvGetBalance).throttleFirst(Constants.SleepTime800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        String address = etAddress.getText().toString();
                        if (TextUtils.isEmpty(address)) {
                            Toast.makeText(MainNewActivity.this, "请先输入地址", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String privateKey = etPrivateKey.getText().toString();
                        if (TextUtils.isEmpty(privateKey)) {
                            Toast.makeText(MainNewActivity.this, "请先输入私钥", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        presenter.getBalance(address);

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());

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
                        String address = etAddress.getText().toString();
                        if (TextUtils.isEmpty(address)) {
                            Toast.makeText(MainNewActivity.this, "请先输入地址", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String privateKey = etPrivateKey.getText().toString();
                        if (TextUtils.isEmpty(privateKey)) {
                            Toast.makeText(MainNewActivity.this, "请先输入私钥", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        presenter.getTXList(address);

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());

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
                        String address = etAddress.getText().toString();
                        String addressTo = etToAddress.getText().toString();
                        String amountString = etAmount.getText().toString();
                        String privateKey = etPrivateKey.getText().toString();

                        if (TextUtils.isEmpty(address)) {
                            Toast.makeText(MainNewActivity.this, "请先输入地址", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(privateKey)) {
                            Toast.makeText(MainNewActivity.this, "请先输入私钥", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (TextUtils.isEmpty(amountString)) {
                            amountString = Constants.amountString;
                            Toast.makeText(MainNewActivity.this, "请先输入金额", Toast.LENGTH_SHORT).show();

                            return;
                        }
                        if (TextUtils.isEmpty(addressTo)) {
                            Toast.makeText(MainNewActivity.this, "请先输入接收方地址", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        btnPush.setText("Pushing");
                        //开始交易
                        presenter.publishTX(gasPrice, addressTo, amountString, privateKey);

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(tvGetTxStatus).throttleFirst(Constants.SleepTime800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        String txHash = tvTxHash.getText().toString();
                        if (TextUtils.isEmpty(txHash)) {
                            Toast.makeText(MainNewActivity.this, "txHash is Empty!", Toast.LENGTH_SHORT).show();
                        } else {

                            presenter.checkTXInfo(txHash);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        ibScanAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainNewActivity.this, CaptureActivity.class), REQUEST_CODE_SCAN_ADDRESS_OK);

            }
        });
        ibScanPrivateKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainNewActivity.this, CaptureActivity.class), REQUEST_CODE_SCAN_PRIVATE_KEY_OK);

            }
        });

        ibScanAddress.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String address = etAddress.getText().toString();
                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(MainNewActivity.this, "请先输入地址", Toast.LENGTH_SHORT).show();
                    return false;
                }
                Intent intent = new Intent();
                intent.putExtra("address", address);
                intent.setClass(MainNewActivity.this, QrCodeActivity.class);
                startActivity(intent);
                return false;
            }
        });

    }

    @Override
    public void success(String info) {
        LogTool.d(TAG, info);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvContent.setText(info);
            }
        });
    }

    @Override
    public void failure(String info) {
        LogTool.e(TAG, info);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvContent.setText(info);
                if (btnPush != null) {
                    btnPush.setText("Push TX");
                }
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
//                presenter.loadWallet(file);
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
//                    presenter.loadWallet(file);
                } else {
                    //这里是拒绝给APP摄像头权限，给个提示什么的说明一下都可以。
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //When you no longer need a Web3j instance you need to call the shutdown method to close resources used by it.
        presenter.cancelSubscribe();
    }

    @Override
    public void gasPriceFailure(String bigInteger) {
        LogTool.e(TAG, "gasPrice failure:" + bigInteger);
    }

    @Override
    public void gasPriceSuccess(BigInteger bigInteger) {
        gasPrice = bigInteger;
        etFee.setText(String.valueOf(Convert.fromWei(String.valueOf(gasPrice.multiply(BigInteger.valueOf(21000))), Convert.Unit.ETHER)));
    }

    @Override
    public void getBalanceSuccess(String balance) {
        tvBalance.setText(balance);
    }

    @Override
    public void getBalanceSFailure(String balance) {

    }

    @Override
    public void getHashRaw(String hashRaw) {
        tvTxHash.setText(hashRaw);
        if (btnPush != null) {
            btnPush.setText("Push TX");
        }
    }

    @Override
    public void getAddressSuccess(String address) {
        if (etAddress != null) {
            etAddress.setText(address);
        }
    }
}
