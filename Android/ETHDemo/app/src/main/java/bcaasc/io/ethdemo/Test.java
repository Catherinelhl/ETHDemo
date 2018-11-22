package bcaasc.io.ethdemo;

import bcaasc.io.ethdemo.constants.Constants;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * @author catherine.brainwilliam
 * @since 2018/11/15
 */
public class Test {

    private Web3j web3j;

    private static String TAG = Test.class.getSimpleName();

    public static void main(String[] args) {
//        try {
//            createAccount();
//        } catch (NoSuchAlgorithmException
//                | NoSuchProviderException
//                | CipherException
//                | InvalidAlgorithmParameterException
//                | IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            createBip39Account();
//        } catch (CipherException | IOException e) {
//            e.printStackTrace();
//        }

        try {
            loadWallet();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        }

//        try {
//            new Test().connectETHClient();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    /*************创建一个钱包文件**************/
    private static void createAccount() throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidAlgorithmParameterException,
            CipherException, IOException {
        String walletFileName = "";//文件名
        String walletFilePath = "/Users/catherine.brainwilliam/AndroidOBTExample/eth_wallet_keystore";
        //钱包文件保持路径，请替换位自己的某文件夹路径

        walletFileName = WalletUtils.generateNewWalletFile(Constants.password, new File(walletFilePath), true);
        //WalletUtils.generateFullNewWalletFile("password1",new File(walleFilePath1));
        //WalletUtils.generateLightNewWalletFile("password2",new File(walleFilePath2));
        System.out.println("walletName: " + walletFileName);
    }

    private static void createBip39Account() throws CipherException, IOException {
        String walletFilePath = "/Users/catherine.brainwilliam/AndroidOBTExample/eth_wallet_keystore";
        //钱包文件保持路径，请替换位自己的某文件夹路径
        Bip39Wallet bip39Wallet = WalletUtils.generateBip39Wallet(Constants.password, new File(walletFilePath));
        System.out.println(bip39Wallet.getFilename());
        System.out.println(bip39Wallet.getMnemonic());
    }

    /********加载钱包文件**********/
    private static void loadWallet() throws IOException, CipherException {
        String walletFilePath = "/Users/catherine.brainwilliam/AndroidOBTExample/eth_wallet_keystore/UTC--2018-11-15T11-31-38.809--07757733653a6670a4f7b8d30704378cb4cf89b2.json";
        Credentials credentials = WalletUtils.loadCredentials(Constants.password, walletFilePath);
        String address = credentials.getAddress();
        BigInteger publicKey = credentials.getEcKeyPair().getPublicKey();
        BigInteger privateKey = credentials.getEcKeyPair().getPrivateKey();

        System.out.println("address=" + address);
        System.out.println("public key=" + publicKey);
        System.out.println("private key=" + privateKey);

    }


    /*******连接以太坊客户端**************/
    private void connectETHClient() throws IOException {
        //连接方式1：使用infura 提供的客户端
        web3j = Web3jFactory.build(new HttpService("https://rinkeby.infura.io/v3/02d45666497a41f9b9dce844f03b4457"));// TODO: 2018/4/10 token更改为自己的
        //连接方式2：使用本地客户端
        //web3j = Web3j.build(new HttpService("127.0.0.1:7545"));
        //测试是否连接成功
        String web3ClientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
        System.out.println("version:" + web3ClientVersion);
        /***********查询指定地址的余额***********/
        String address = "0x07757733653a6670a4f7b8d30704378cb4cf89b2";//等待查询余额的地址
        //第二个参数：区块的参数，建议选最新区块
        EthGetBalance balance = web3j.ethGetBalance(address, DefaultBlockParameterName.EARLIEST).send();
        //格式转化 wei-ether
        String balanceETH = Convert.fromWei(balance.getBalance().toString(), Convert.Unit.ETHER).toPlainString().concat(" ether");
        System.out.println(balanceETH);
    }

}
