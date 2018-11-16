package bcaasc.io.ethdemo.presenter;

import bcaasc.io.ethdemo.constants.Constants;
import bcaasc.io.ethdemo.contract.MainContract;
import bcaasc.io.ethdemo.tool.LogTool;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * @author catherine.brainwilliam
 * @since 2018/11/15
 */
public class MainPresenterImp implements MainContract.Presenter {

    private String TAG = MainPresenterImp.class.getSimpleName();

    private Web3j web3j;
    private Credentials credentials;
    private String emptyAddress;


    private MainContract.View view;

    public MainPresenterImp(MainContract.View view) {
        this.view = view;
    }

    /*************创建一个钱包文件**************/

    @Override
    public void createAccount() {
        String walletFileName0 = "";//文件名
        String walletFilePath0 = "/Users/catherine.brainwilliam/AndroidOBTExample/eth_wallet_keystore";
        //钱包文件保持路径，请替换位自己的某文件夹路径

        try {
            walletFileName0 = WalletUtils.generateNewWalletFile("123456", new File(walletFilePath0), false);
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
        System.out.println("walletName: " + walletFileName0);
    }

    /********加载钱包文件**********/

    @Override
    public void loadWallet(File file) {
        String keystore = "{\"address\":\"07757733653a6670a4f7b8d30704378cb4cf89b2\",\"id\":\"428e5938-f0b3-41be-9957-de7c014581fb\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"275e9e1c3ad350b5ac9b85d1cb826ea1a786817ee58bc096016f1f91e8be6d81\",\"cipherparams\":{\"iv\":\"cd8c8e0089f105a2575699be756b9a01\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":4096,\"p\":6,\"r\":8,\"salt\":\"6dc98ae0c90c575529417ac932f6a91edaa2f7bdb0a7827edec7843803b953be\"},\"mac\":\"1b2062c587a2ba1444dc574d1f8d2c65c40ef36ad45f8cda851cf142e79620ac\"}}";

        writeKeyStoreToFile(keystore, file);
        String walletFilePath = "/Users/catherine.brainwilliam/AndroidOBTExample/eth_wallet_keystore/UTC--2018-11-15T11-31-38.809--07757733653a6670a4f7b8d30704378cb4cf89b2.json";
        String passWord = "123456";
        LogTool.d(TAG, file);
        ///storage/emulated/0/Android/data/bcaasc.io.ethdemo/files/bcaas/

        try {
            credentials = WalletUtils.loadCredentials(passWord, file);
        } catch (IOException | CipherException e) {
            e.printStackTrace();
            LogTool.e(TAG, e.getMessage());
        }
        String address = credentials.getAddress();
        BigInteger publicKey = credentials.getEcKeyPair().getPublicKey();
        BigInteger privateKey = credentials.getEcKeyPair().getPrivateKey();

        LogTool.d(TAG, "address=" + address);
        LogTool.d(TAG, "public key=" + publicKey);
        LogTool.d(TAG, "private key=" + privateKey);

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


    /*******连接以太坊客户端**************/

    @Override
    public void connectETHClient() {
        LogTool.d(TAG, "connectETHClient");
        //连接方式1：使用infura 提供的客户端
        web3j = Web3jFactory.build(new HttpService("https://mainnet.infura.io/v3/02d45666497a41f9b9dce844f03b4457"));
        //连接方式2：使用本地客户端
        //web3j = Web3j.build(new HttpService("127.0.0.1:7545"));
        //测试是否连接成功
        String web3ClientVersion = null;
        try {
            web3ClientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
        } catch (IOException e) {
            e.printStackTrace();
            LogTool.e(TAG, e.getMessage());
        }
        LogTool.d(TAG, "version=" + web3ClientVersion);
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
        EthGetBalance balance = null;
        try {
            balance = web3j.ethGetBalance(Constants.address, DefaultBlockParameterName.LATEST).send();
        } catch (IOException e) {
            e.printStackTrace();
            LogTool.e(TAG, e.getMessage());
        }
        //格式转化 wei-ether
        String balanceETH = Convert.fromWei(balance.getBalance().toString(), Convert.Unit.ETHER).toPlainString().concat(" ether");
        view.success("Balance：" + balanceETH);
    }

    @Override
    public void getTXList() {

    }

    /****************交易*****************/
    /**
     * 作为一个钱包，除了保存账户资产外，最重要的就是转账或交易了，利用web3j可以便捷的实现eth的转移。
     * 核心方法需要提供4个参数：
     * <p>
     * web3j实体
     * Credentials 源账户
     * address 转出地址
     * value 数量
     * uint 单位
     * 等待片刻后，会返回转账结果
     *
     * @throws Exception
     */
    @Override
    public void publishTX(String gas, String addressTo, String amountString) {
        if (web3j == null) return;
        if (credentials == null) {
            return;
        }
        LogTool.d(TAG, "address:" + credentials.getAddress());
        LogTool.d(TAG, "privateKey:" + credentials.getEcKeyPair().getPrivateKey());
        LogTool.d(TAG, "publicKey:" + credentials.getEcKeyPair().getPublicKey());
        //开始发送0.01 =eth到指定地址
        TransactionReceipt send = null;
        BigDecimal amount = new BigDecimal(amountString);

        try {
            send = Transfer.sendFunds(web3j, credentials, addressTo, amount, Convert.Unit.ETHER).send();
        } catch (Exception e) {
            e.printStackTrace();
            LogTool.e(TAG, e.getMessage());
        }

        if (send == null) {
            view.failure("failure");
        } else {
            LogTool.d(TAG, "Transaction complete:");
            LogTool.d(TAG, "trans hash=" + send.getTransactionHash());
            LogTool.d(TAG, "from :" + send.getFrom());
            LogTool.d(TAG, "to:" + send.getTo());
            LogTool.d(TAG, "gas used=" + send.getGasUsed());
            LogTool.d(TAG, "status: " + send.getStatus());
            view.success(send.getTransactionHash());
        }
    }

    @Override
    public void checkTXInfo() {

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