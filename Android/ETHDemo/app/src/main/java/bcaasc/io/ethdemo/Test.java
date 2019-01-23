package bcaasc.io.ethdemo;

import org.spongycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.spongycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.spongycastle.util.encoders.Hex;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Arrays;

import bcaasc.io.ethdemo.constants.Constants;

import static bcaasc.io.ethdemo.tool.SecureRandomTools.secureRandom;

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

//        try {
//            createBip39Account();
//        } catch (CipherException | IOException e) {
//            e.printStackTrace();
//        }
//

//        createAccount2();
//        try {
//            loadWallet();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (CipherException e) {
//            e.printStackTrace();
//        }

//        System.out.println("hex private key=" + Hex.toHexString(new BigInteger(101233875057005438239658919013501011727368307284946832848498204629504449734998).toByteArray()));


//        try {
//            new Test().connectETHClient();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        createKeyPair();
//        System.out.println("Fee:" + Convert.fromWei(String.valueOf(new BigInteger("6000000000").multiply(BigInteger.valueOf(21000))), Convert.Unit.ETHER));
//        System.out.println("---------");
//        System.out.println(Numeric.toBigInt("dfd057c031940800a306fb895fccc4659a063aee0a37526bcb784119ddd26956"));
//        String privateKey = "7ba2c387f7f35b6e97f2bc34fe7785a51b89939fbaa525f830e912bbc2aa6dee";
//        System.out.println(privateKey.length());

//        //"7ba2c387f7f35b6e97f2bc34fe7785a51b89939fbaa525f830e912bbc2aa6dee"
//        Credentials credentials = Credentials.create(privateKeyConvert);
//        System.out.println(credentials.getAddress());
//        System.out.println(credentials.getEcKeyPair().getPublicKey());
//        System.out.println(credentials.getEcKeyPair().getPrivateKey());
    }

    /**
     * 直接创建钱包
     */
    private static void createAccountDirectly() {
        try {

            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            System.out.println("PrivateKey:" + ecKeyPair.getPrivateKey());
            System.out.println("PublicKey:" + ecKeyPair.getPublicKey());
            System.out.println("Address:" + Keys.getAddress(ecKeyPair));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createKeyPair() {
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "CS");
            ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
            try {
                keyPairGenerator.initialize(ecGenParameterSpec, secureRandom());
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            BCECPrivateKey privateKey = (BCECPrivateKey) keyPair.getPrivate();
            BCECPublicKey publicKey = (BCECPublicKey) keyPair.getPublic();

            BigInteger privateKeyValue = privateKey.getD();

            // Ethereum does not use encoded public keys like bitcoin - see
            // https://en.bitcoin.it/wiki/Elliptic_Curve_Digital_Signature_Algorithm for details
            // Additionally, as the first bit is a constant prefix (0x04) we ignore this value
            byte[] publicKeyBytes = publicKey.getQ().getEncoded(false);
            BigInteger publicKeyValue =
                    new BigInteger(1, Arrays.copyOfRange(publicKeyBytes, 1, publicKeyBytes.length));
            ECKeyPair ecKeyPair = new ECKeyPair(privateKeyValue, publicKeyValue);
            System.out.println("PrivateKey:" + ecKeyPair.getPrivateKey());
            System.out.println("PublicKey:" + ecKeyPair.getPublicKey());
            System.out.println("Address:" + Keys.getAddress(ecKeyPair));
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }

    }

    /**
     * 因为生成出来的钱包私钥是BigInteger的格式，而BigInteger又是10进制。所以这里要将其转换成16进制的字符串；与iOS统一，
     * 也与我们自己的钱包或者BTC的钱包私钥格式统一
     */
    private static void bigIntegerConvertString() {
        /*method 1:*/
        String privateKey = "93404604165627305237506758501941880052707940482279758688425570060514417761703";
//
        byte[] array = new BigInteger(privateKey).toByteArray();
        if (array[0] == 0) {
            byte[] tmp = new byte[array.length - 1];
            System.arraycopy(array, 1, tmp, 0, tmp.length);
            array = tmp;
        }
        String privateKeyConvert = Hex.toHexString(array);
        System.out.println("privateKeyConvert:" + privateKeyConvert);
        /*method 2:*/
        try {
            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            //将当前10进制的私钥转换为16进制的格式，这样与iOS同步
            System.out.println(ecKeyPair.getPrivateKey().toString(16));
//            BigInteger privateKey = BigInteger.parseBigInteger(93404604165627305237506758501941880052707940482279758688425570060514417761703);
            //93404604165627305237506758501941880052707940482279758688425570060514417761703;
            System.out.println("getPublicKey:" + ecKeyPair.getPublicKey());
            try {
//                System.out.println("address:" + Wallet.createStandard("12345678", ecKeyPair).getAddress());
                System.out.println("address:" + Keys.getAddress(ecKeyPair));
                System.out.println("address:" + Keys.getAddress(privateKey));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*************创建一个钱包文件**************/
    private static void createAccount() throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidAlgorithmParameterException,
            CipherException, IOException {
        String walletFileName = "";//文件名
//        String walletFilePath = "/Users/catherine.brainwilliam/AndroidOBTExample/eth_wallet_keystore";
        String walletFilePath = "/Users/pengyiqiao/Desktop/Works/OrangeBlock/git_repository/ThirdpartySample/ETH/Android/ETHDemo/";
        //钱包文件保持路径，请替换位自己的某文件夹路径

        walletFileName = WalletUtils.generateLightNewWalletFile(Constants.password, new File(walletFilePath));
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
//        String walletFilePath = "/Users/pengyiqiao/Desktop/Works/OrangeBlock/git_repository/ThirdpartySample/ETH/Android/ETHDemo/UTC--2018-11-15T11-31-38.809--07757733653a6670a4f7b8d30704378cb4cf89b2.json";
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
