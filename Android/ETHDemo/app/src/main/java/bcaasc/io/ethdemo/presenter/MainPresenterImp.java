package bcaasc.io.ethdemo.presenter;

import android.text.TextUtils;
import bcaasc.io.ethdemo.bean.ETHTXListResponse;
import bcaasc.io.ethdemo.bean.TXListBean;
import bcaasc.io.ethdemo.constants.Constants;
import bcaasc.io.ethdemo.contract.MainContract;
import bcaasc.io.ethdemo.http.MainInteractor;
import bcaasc.io.ethdemo.tool.LogTool;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import kotlin.Deprecated;
import org.spongycastle.util.encoders.Hex;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author catherine.brainwilliam
 * @since 2018/11/15
 */
public class MainPresenterImp implements MainContract.Presenter {

    private String TAG = MainPresenterImp.class.getSimpleName();

    private Web3j web3j;
    private Credentials credentials;

    private MainContract.View view;
    private String transactionHash;//得到此次交易的hash
    private MainInteractor interactor;

    public MainPresenterImp(MainContract.View view) {

        this.view = view;
        interactor = new MainInteractor();
    }

    /**
     * 创建一个钱包文件
     */
    @Override
    public void createAccount() {
        String walletFileName = "";//文件名
        String walletFilePath = "/Users/catherine.brainwilliam/AndroidOBTExample/eth_wallet_keystore";
        //钱包文件保持路径，请替换位自己的某文件夹路径

        try {
            //useFullScrypt：是否使用全加密
            walletFileName = WalletUtils.generateNewWalletFile(Constants.password, new File(walletFilePath), false);
        } catch (CipherException
                | IOException
                | InvalidAlgorithmParameterException
                | NoSuchAlgorithmException
                | NoSuchProviderException e) {
            e.printStackTrace();
            LogTool.e(TAG, e.getMessage());
        }
        //WalletUtils.generateFullNewWalletFile("password1",new File(walleFilePath1));
        //WalletUtils.generateLightNewWalletFile("password2",new File(walleFilePath2));
        System.out.println("walletName: " + walletFileName);
    }

    /**
     * 根据keystore信息加载钱包文件
     */
    @Override
    public void loadWallet(File file) {
//        loadWalletByKeystore(file);
        loadWalletByPrivateKey();


    }

    /**
     * 通过加载Keystore文件来得到当前钱包的元数据
     *
     * @param file
     */
    private void loadWalletByKeystore(File file) {
        String keystore = "{\"address\":\"07757733653a6670a4f7b8d30704378cb4cf89b2\",\"id\":\"428e5938-f0b3-41be-9957-de7c014581fb\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"275e9e1c3ad350b5ac9b85d1cb826ea1a786817ee58bc096016f1f91e8be6d81\",\"cipherparams\":{\"iv\":\"cd8c8e0089f105a2575699be756b9a01\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":4096,\"p\":6,\"r\":8,\"salt\":\"6dc98ae0c90c575529417ac932f6a91edaa2f7bdb0a7827edec7843803b953be\"},\"mac\":\"1b2062c587a2ba1444dc574d1f8d2c65c40ef36ad45f8cda851cf142e79620ac\"}}";

        writeKeyStoreToFile(keystore, file);
        String walletFilePath = "/Users/catherine.brainwilliam/AndroidOBTExample/eth_wallet_keystore/UTC--2018-11-15T11-31-38.809--07757733653a6670a4f7b8d30704378cb4cf89b2.json";
        LogTool.d(TAG, file);
        try {
            credentials = WalletUtils.loadCredentials(Constants.password, file);
        } catch (IOException | CipherException e) {
            e.printStackTrace();
            LogTool.e(TAG, e.getMessage());
        }
        String address = credentials.getAddress();
        BigInteger publicKey = credentials.getEcKeyPair().getPublicKey();
        BigInteger privateKey = credentials.getEcKeyPair().getPrivateKey();
        LogTool.d(TAG, "address=" + address);
        LogTool.d(TAG, "hex public key=" + Hex.toHexString(publicKey.toByteArray()));
        LogTool.d(TAG, "hex public key=" + privateKey.toByteArray().length);
        LogTool.d(TAG, "hex private key=" + Hex.toHexString(privateKey.toByteArray(), 1, 32));
    }


    /**
     * 通过加载私钥来得到当前钱包的元数据
     */
    private void loadWalletByPrivateKey() {

        byte[] array = new BigInteger(Constants.privateKey).toByteArray();
        if (array[0] == 0) {
            byte[] tmp = new byte[array.length - 1];
            System.arraycopy(array, 1, tmp, 0, tmp.length);
            array = tmp;
        }
        String privateKeyConvert = Hex.toHexString(array);
        credentials = Credentials.create(privateKeyConvert);
        LogTool.d(TAG, "address=" + credentials.getAddress());
        LogTool.d(TAG, " public key=" + credentials.getEcKeyPair().getPublicKey());
        LogTool.d(TAG, " private key=" + credentials.getEcKeyPair().getPrivateKey());
    }

    /*存储钱包信息*/
    private boolean writeKeyStoreToFile(String keystore, File file) {
        boolean status = false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(keystore.getBytes());
            status = true;
        } catch (IOException e) {
            LogTool.d(TAG, e.getMessage());
            e.printStackTrace();
            status = false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LogTool.d(TAG, e.getMessage());
                    e.printStackTrace();
                }
            }
            return status;
        }
    }


    /**
     * 连接以太坊客户端
     */

    @Override
    public void connectETHClient() {
        LogTool.d(TAG, "connectETHClient");
        //连接方式:使用infura 提供的客户端
        //test net
//        web3j = Web3jFactory.build(new HttpService("https://rinkeby.infura.io/v3/02d45666497a41f9b9dce844f03b4457"));
        //main net
        web3j = Web3jFactory.build(new HttpService("https://mainnet.infura.io/v3/7c4ebc9898924f0aa003ab19df9c36eb"));

        Disposable subscribe = Observable.just(web3j.web3ClientVersion())
                .map(new Function<Request<?, Web3ClientVersion>, String>() {
                    @Override
                    public String apply(Request<?, Web3ClientVersion> web3ClientVersionRequest) throws Exception {
                        return web3ClientVersionRequest.send().getWeb3ClientVersion();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String web3ClientVersion) throws Exception {
                        //测试是否连接成功
                        view.success("version:" + web3ClientVersion);
                        getBalance();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.failure(throwable.getMessage());
                    }
                });
    }


    /***********查询指定地址的余额***********/
    /**
     * 其中核心方法 web3j.ethGetBalance(address, defaultBlockParameter）
     * 中的第二个参数比较特殊，指默认的区块参数。当请求余额的方法作用与以太坊的区块网络时，
     * 这个参数决定了查询区块的高度。
     * <p>
     * <p>
     * 一般情况下，选择“latest”即可。
     * <p>
     * 以太坊中，如果没有特殊标示，数字的单位都是小数点后18位，因此查询账户余额有必要将wei转化成ether。
     * <p>
     * HEX String - 一个整数块号
     * String "earliest" 为最早/起源块
     * String "latest" - 为最新的采矿块
     * String "pending" - 待处理状态/交易
     *
     * @throws IOException
     */

    @Override
    public void getBalance() {
        if (web3j == null) return;
        //第二个参数：区块的参数，建议选最新区块
        Disposable subscribe = Observable.just(web3j.ethGetBalance(Constants.address, DefaultBlockParameterName.LATEST))
                .map(new Function<Request<?, EthGetBalance>, Future<EthGetBalance>>() {
                    @Override
                    public Future<EthGetBalance> apply(Request<?, EthGetBalance> ethGetBalanceRequest) throws Exception {
                        //这里可以有异步，也可以同步：send()
                        return ethGetBalanceRequest.sendAsync();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Future<EthGetBalance>>() {
                    @Override
                    public void accept(Future<EthGetBalance> ethGetBalanceFuture) throws Exception {
                        EthGetBalance ethGetBalance = ethGetBalanceFuture.get();
                        //格式转化 wei-ether
                        String balanceETH = Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER).toPlainString().concat(" ether");
                        view.getBalanceSuccess(balanceETH);
                        LogTool.d(TAG, balanceETH);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogTool.e(TAG, throwable.getMessage());
                        view.getBalanceSFailure(throwable.getMessage());
                    }
                });
    }

    @Override
    public void getGasPrice() {
        if (web3j == null) return;
        Disposable subscribe = Observable.just(web3j.ethGasPrice()).map(new Function<Request<?, EthGasPrice>, Future<EthGasPrice>>() {
            @Override
            public Future<EthGasPrice> apply(Request<?, EthGasPrice> ethGasPriceRequest) throws Exception {
                return ethGasPriceRequest.sendAsync();
            }
        }).subscribe(new Consumer<Future<EthGasPrice>>() {
            @Override
            public void accept(Future<EthGasPrice> ethGasPriceFuture) throws Exception {
                BigInteger bigInteger = ethGasPriceFuture.get().getGasPrice();
                view.gasPriceSuccess(bigInteger);
                LogTool.d(TAG, "gasPrice:" + bigInteger);

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                view.gasPriceFailure(throwable.getMessage());
            }
        });
    }

    @Override
    public void getTXList() {
        if (web3j == null) return;
        getTxListByEtherscan();


    }

    /**
     * 通过Etherscan直接网络请求进行查询
     * <p>
     * module=account
     * * &action=txlist
     * * &address=0xddbd2b932c763ba5b1b7ae3b362eac3e8d40121a
     * * &startblock=0
     * * &endblock=99999999
     * * &sort=asc
     * * &apikey=YourApiKeyToken
     * <p>
     * apikey 这里为null也行
     */
    private void getTxListByEtherscan() {
        //desc 从高到低
        //asc 从低到高
        interactor.getTXList("account", "txlist", Constants.address, "0",
                "99999999", "desc", Constants.APIKEY, new Callback<ETHTXListResponse>() {
                    @Override
                    public void onResponse(Call<ETHTXListResponse> call, Response<ETHTXListResponse> response) {
                        if (response != null) {
                            ETHTXListResponse ethtxListResponse = response.body();
                            view.success(ethtxListResponse.toString());
                            if (ethtxListResponse != null) {
                                List<TXListBean> txListBeans = ethtxListResponse.getResult();
                                if (txListBeans != null && txListBeans.size() > 0) {
                                    for (TXListBean txListBean : txListBeans) {
//                                        LogTool.d(TAG, response.body().getResult());
                                        LogTool.d(TAG, "Hash:" + txListBean.getHash() + ";status:" + txListBean.getConfirmations());
                                    }
                                }

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ETHTXListResponse> call, Throwable t) {
                        LogTool.e(TAG, t.getMessage());

                    }
                });

    }

    @Deprecated(message = "unuse")
    private void getTXListByWeb3j() {
        Disposable subscribe = Observable.just(web3j.ethGetTransactionReceipt(transactionHash)).
                map(new Function<Request<?, EthGetTransactionReceipt>, EthGetTransactionReceipt>() {
                    @Override
                    public EthGetTransactionReceipt apply(Request<?, EthGetTransactionReceipt> ethGetTransactionReceiptRequest) throws Exception {
                        return ethGetTransactionReceiptRequest.send();
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EthGetTransactionReceipt>() {
                    @Override
                    public void accept(EthGetTransactionReceipt ethGetTransactionReceipt) throws Exception {
                        LogTool.d(TAG, ethGetTransactionReceipt.getError());
                        LogTool.d(TAG, ethGetTransactionReceipt.getId());
                        LogTool.d(TAG, ethGetTransactionReceipt.getResult());
                        view.success(ethGetTransactionReceipt.getRawResponse());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.failure(throwable.getMessage());
                    }
                });
    }

    /****************交易*****************/
    /**
     * 作为一个钱包，除了保存账户资产外，最重要的就是转账或交易了，利用web3j可以便捷的实现eth的转移。
     * 核心方法需要提供4个参数：
     * <p>
     * web3j实体
     * Credentials 源账户所有信息
     * address 转出地址
     * value 数量
     * unit 单位
     * 等待片刻后，会返回转账结果
     *
     * @throws Exception
     */
    @Override
    public void publishTX(BigInteger gasPrice, String addressTo, String amountString) {
        if (web3j == null) return;
        if (credentials == null) {
            return;
        }

        BigDecimal amount = new BigDecimal(amountString);
        LogTool.d(TAG, "addressTo:" + addressTo);
        LogTool.d(TAG, "amount:" + amount);
        LogTool.d(TAG, "gasPrice:" + gasPrice);
        LogTool.d(TAG, "privateKey:" + credentials.getEcKeyPair().getPrivateKey());
        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials);
        Transfer transfer = new Transfer(web3j, transactionManager);
        Disposable disposable = Observable.just(transfer.sendFunds(addressTo, amount, Convert.Unit.ETHER, gasPrice, BigInteger.valueOf(21000)))
                .map(new Function<RemoteCall<TransactionReceipt>, TransactionReceipt>() {
                    @Override
                    public TransactionReceipt apply(RemoteCall<TransactionReceipt> transactionReceiptRemoteCall) throws Exception {
                        LogTool.d(TAG, "pushing");
                        return transactionReceiptRemoteCall.send();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TransactionReceipt>() {
                    @Override
                    public void accept(TransactionReceipt send) throws Exception {
                        if (send == null) {
                            view.failure("failure send is null");
                        } else {
                            LogTool.d(TAG, "Transaction complete:");
                            LogTool.d(TAG, "trans hash=" + send.getTransactionHash());
                            LogTool.d(TAG, "block hash" + send.getBlockHash());
                            LogTool.d(TAG, "from :" + send.getFrom());
                            LogTool.d(TAG, "to:" + send.getTo());
                            LogTool.d(TAG, "gas used=" + send.getGasUsed());
                            LogTool.d(TAG, "status: " + send.getStatus());
                            transactionHash = send.getTransactionHash();
                            view.success(transactionHash);
                            view.getHashRaw(transactionHash);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.failure(throwable.getMessage());
                        LogTool.e(TAG, throwable.getMessage());
                    }
                });


    }

    @Override
    public void checkTXInfo(String txHash) {
        if (TextUtils.isEmpty(txHash)) {
            txHash = "0x0504eceb6e80d5a5d510e25d3327db5ad1d7b53968466544e4783190758b07b1";
        }
        if (web3j == null) return;
        if (TextUtils.isEmpty(txHash)) {
            view.failure("transactionHash is empty!!");
            return;
        }
        Disposable subscribe = Observable.just(web3j.ethGetTransactionByHash(txHash))
                .map(new Function<Request<?, EthTransaction>, Future<EthTransaction>>() {

                    @Override
                    public Future<EthTransaction> apply(Request<?, EthTransaction> ethTransactionRequest) throws Exception {
                        return ethTransactionRequest.sendAsync();
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Future<EthTransaction>>() {
                    @Override
                    public void accept(Future<EthTransaction> ethTransactionFuture) throws Exception {
                        LogTool.d(TAG, "ethTransactionFuture:" + ethTransactionFuture);
                        if (ethTransactionFuture == null) {
                            return;

                        }
                        EthTransaction ethTransaction = ethTransactionFuture.get();
                        LogTool.d(TAG, "ethTransaction:" + ethTransaction);
                        if (ethTransaction == null) {
                            return;
                        }
                        Transaction transaction = ethTransaction.getTransaction();
                        LogTool.d(TAG, "transaction:" + transaction);
                        if (transaction == null) {
                            return;
                        }
                        LogTool.d(TAG, "getBlockNumber:" + transaction.getBlockNumber());
                        LogTool.d(TAG, "getBlockHash:" + transaction.getBlockHash());
                        LogTool.d(TAG, "getBlockNumberRaw:" + transaction.getBlockNumberRaw());
                        LogTool.d(TAG, "getGasPriceRaw:" + transaction.getGasPriceRaw());
                        LogTool.d(TAG, "getGasPrice:" + transaction.getGasPrice());
                        LogTool.d(TAG, "getGasRaw:" + transaction.getGasRaw());
                        LogTool.d(TAG, "getHash:" + transaction.getHash());
                        LogTool.d(TAG, "getInput:" + transaction.getInput());
                        LogTool.d(TAG, "getNonceRaw:" + transaction.getNonceRaw());
                        LogTool.d(TAG, "getNonce:" + transaction.getNonce());
                        LogTool.d(TAG, "getS:" + transaction.getS());
                        LogTool.d(TAG, "getR:" + transaction.getR());
                        LogTool.d(TAG, "getRaw:" + transaction.getRaw());
                        LogTool.d(TAG, "getFrom:" + transaction.getFrom());
                        LogTool.d(TAG, "getTo:" + transaction.getTo());
                        LogTool.d(TAG, "getTransactionIndexRaw:" + transaction.getTransactionIndexRaw());
                        LogTool.d(TAG, "getTransactionIndex:" + transaction.getTransactionIndex());
                        LogTool.d(TAG, "getValue:" + transaction.getValue());
                        LogTool.d(TAG, "getValueRaw:" + transaction.getValueRaw());
                        LogTool.d(TAG, "getCreates:" + transaction.getCreates());
                        if (transaction == null) {
                            view.failure("transaction is empty!!");
                            return;
                        }
                        view.success("getBlockNumber:" + transaction.getBlockNumber().toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogTool.e(TAG, throwable.getMessage());
                        view.failure(throwable.getMessage());
                    }
                });

    }

    @Override
    public void cancelSubscribe() {
    }

//    /*** 查询代币余额 */
//    public static BigInteger getTokenBalance(Web3j web3j, String fromAddress, String contractAddress) {
//
//        String methodName = "balanceOf";
//        List inputParameters = new ArrayList<>();
//        List outputParameters = new ArrayList<>();
//        Address address = new Address(fromAddress);
//        inputParameters.add(address);

//        TypeReference typeReference = new TypeReference() {
//        };
//        outputParameters.add(typeReference);
//        Function function = new Function(methodName, inputParameters, outputParameters);
//        String data = FunctionEncoder.encode(function);
//        Transaction transaction = Transaction.createEthCallTransaction(fromAddress, contractAddress, data);
//
//        EthCall ethCall;
//        BigInteger balanceValue = BigInteger.ZERO;
//        try {
//            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
//            List results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
//            balanceValue = (BigInteger) results.get(0).getValue();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return balanceValue;
//    }
//
//    /**
//     * 查询代币名称
//     *
//     * @param web3j
//     * @param contractAddress
//     * @return
//     */
//    public  String getTokenName(Web3j web3j, String contractAddress) {
//        String methodName = "name";
//        String name = null;
//        String fromAddr = emptyAddress;
//        List inputParameters = new ArrayList<>();
//        List outputParameters = new ArrayList<>();
//
//        TypeReference typeReference = new TypeReference() {
//        };
//        outputParameters.add(typeReference);
//
//        Function function = new Function(methodName, inputParameters, outputParameters);
//
//        String data = FunctionEncoder.encode(function);
//        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);
//
//        EthCall ethCall;
//        try {
//            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
//            List results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
//            name = results.get(0).getValue().toString();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//        return name;
//    }
//
//    /**
//     * 查询代币符号
//     *
//     * @param web3j
//     * @param contractAddress
//     * @return
//     */
//    public  String getTokenSymbol(Web3j web3j, String contractAddress) {
//        String methodName = "symbol";
//        String symbol = null;
//        String fromAddr = emptyAddress;
//        List inputParameters = new ArrayList<>();
//        List outputParameters = new ArrayList<>();
//
//        TypeReference typeReference = new TypeReference() {
//        };
//        outputParameters.add(typeReference);
//
//        Function function = new Function(methodName, inputParameters, outputParameters);
//
//        String data = FunctionEncoder.encode(function);
//        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);
//
//        EthCall ethCall;
//        try {
//            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
//            List results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
//            symbol = results.get(0).getValue().toString();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//        return symbol;
//    }
//
//    /**
//     * 查询代币精度
//     *
//     * @param web3j
//     * @param contractAddress
//     * @return
//     */
//    public  int getTokenDecimals(Web3j web3j, String contractAddress) {
//        String methodName = "decimals";
//        String fromAddr = emptyAddress;
//        int decimal = 0;
//        List inputParameters = new ArrayList<>();
//        List outputParameters = new ArrayList<>();
//
//        TypeReference typeReference = new TypeReference() {
//        };
//        outputParameters.add(typeReference);
//
//        Function function = new Function(methodName, inputParameters, outputParameters);
//
//        String data = FunctionEncoder.encode(function);
//        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);
//
//        EthCall ethCall;
//        try {
//            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
//            List results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
//            decimal = Integer.parseInt(results.get(0).getValue().toString());
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//        return decimal;
//    }
//
//    /**
//     * 查询代币发行总量
//     *
//     * @param web3j
//     * @param contractAddress
//     * @return
//     */
//    public  BigInteger getTokenTotalSupply(Web3j web3j, String contractAddress) {
//        String methodName = "totalSupply";
//        String fromAddr = emptyAddress;
//        BigInteger totalSupply = BigInteger.ZERO;
//        List inputParameters = new ArrayList<>();
//        List outputParameters = new ArrayList<>();
//
//        TypeReference typeReference = new TypeReference() {
//        };
//        outputParameters.add(typeReference);
//
//        Function function = new Function(methodName, inputParameters, outputParameters);
//
//        String data = FunctionEncoder.encode(function);
//        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);
//
//        EthCall ethCall;
//        try {
//            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
//            List results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
//            totalSupply = (BigInteger) results.get(0).getValue();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//        return totalSupply;
//    }
//
//    /**
//     * 代币转账
//     */
//    public  String sendTokenTransaction(String fromAddress, String password, String toAddress, String contractAddress, BigInteger amount) {
//        String txHash = null;
//
//        try {
//            PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(
//                    fromAddress, password, BigInteger.valueOf(10)).send();
//            if (personalUnlockAccount.accountUnlocked()) {
//                String methodName = "transfer";
//                List inputParameters = new ArrayList<>();
//                List outputParameters = new ArrayList<>();
//
//                Address tAddress = new Address(toAddress);
//
//                Uint256 value = new Uint256(amount);
//                inputParameters.add(tAddress);
//                inputParameters.add(value);
//
//                TypeReference typeReference = new TypeReference() {
//                };
//                outputParameters.add(typeReference);
//
//                Function function = new Function(methodName, inputParameters, outputParameters);
//
//                String data = FunctionEncoder.encode(function);
//
//                EthGetTransactionCount ethGetTransactionCount = web3j
//                        .ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).sendAsync().get();
//                BigInteger nonce = ethGetTransactionCount.getTransactionCount();
//                BigInteger gasPrice = Convert.toWei(BigDecimal.valueOf(5), Convert.Unit.GWEI).toBigInteger();
//
//                Transaction transaction = Transaction.createFunctionCallTransaction(fromAddress, nonce, gasPrice,
//                        BigInteger.valueOf(60000), contractAddress, data);
//
//                EthSendTransaction ethSendTransaction = web3j.ethSendTransaction(transaction).sendAsync().get();
//                txHash = ethSendTransaction.getTransactionHash();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return txHash;
//    }
//
//    /**
//     * 计算合约地址
//     *
//     * @param address
//     * @param nonce
//     * @return
//     */
//    private  String calculateContractAddress(String address, long nonce) {
////样例 https://ropsten.etherscan.io/tx/0x728a95b02beec3de9fb09ede00ca8ca6939bad2ad26c702a8392074dc04844c7
//        byte[] addressAsBytes = Numeric.hexStringToByteArray(address);
//
//        byte[] calculatedAddressAsBytes =
//                Hash.sha3(RlpEncoder.encode(
//                        new RlpList(
//                                RlpString.create(addressAsBytes),
//                                RlpString.create((nonce)))));
//
//        calculatedAddressAsBytes = Arrays.copyOfRange(calculatedAddressAsBytes,
//                12, calculatedAddressAsBytes.length);
//        String calculatedAddressAsHex = Numeric.toHexString(calculatedAddressAsBytes);
//        return calculatedAddressAsHex;
//    }

}
