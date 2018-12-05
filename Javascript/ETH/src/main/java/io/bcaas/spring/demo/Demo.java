package io.bcaas.spring.demo;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

public class Demo {
	
	/***************创建钱包***************/
	public void creatAccount() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CipherException, IOException {
	    String walletFileName0="";//文件名
	    String walletFilePath0="/Users/yimi/Desktop/myWallet";
	    //钱包文件保持路径，请替换位自己的某文件夹路径

	    walletFileName0 = WalletUtils.generateNewWalletFile("123456", new File(walletFilePath0), false);
	    //WalletUtils.generateFullNewWalletFile("password1",new File(walleFilePath1));
	    //WalletUtils.generateLightNewWalletFile("password2",new File(walleFilePath2));
	    System.out.println("walletName: "+walletFileName0);
	}

	/**************加载钱包文件****************/
	private void loadWallet() throws IOException, CipherException {
	    String walleFilePath="/Users/yimi/Desktop/myWallet/UTC--2018-11-29T05-42-41.351000000Z--e4d869165c4251f4b65c891774131072ed1a2af8.json";
	    String passWord="123456";
	    Credentials credentials = WalletUtils.loadCredentials(passWord, walleFilePath);
	    String address = credentials.getAddress();
	    BigInteger publicKey = credentials.getEcKeyPair().getPublicKey();
	    BigInteger privateKey = credentials.getEcKeyPair().getPrivateKey();

	    System.out.println("address="+address);
	   System.out.println("public key="+publicKey);
	   System.out.println("private key="+privateKey);

	}
	
	/*******连接以太坊客户端**************/
	public void conectETHclient() throws IOException {
	    //连接方式1：使用infura 提供的客户端
		Web3j  web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/v3/a53a28a37ec943f6aa4ba5bbf8e1d24c"));
	    //连接方式2：使用本地客户端
	 //   Web3j web3j = Web3j.build(new HttpService("127.0.0.1:8545"));
	    //测试是否连接成功
	    String web3ClientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
	    System.out.println("web3ClientVersion:----------------"+web3ClientVersion);
	}

	/***********查询指定地址的余额***********/
	public void getBlanceOf() throws IOException {
		Web3j  web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/a53a28a37ec943f6aa4ba5bbf8e1d24c"));
	//	Web3j web3j = Web3j.build(new HttpService());
		if (web3j == null) return;
	    String address = "0xe4d869165c4251f4b65c891774131072ed1a2af8";//等待查询余额的地址
	    //第二个参数：区块的参数，建议选最新区块
	    EthGetBalance balance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
	    //格式转化 wei-ether
	    String blanceETH = Convert.fromWei(new BigDecimal(balance.getBalance()), Convert.Unit.ETHER).toPlainString().concat("ether");
	    System.out.println(blanceETH);
	    
	}

	
	//交易
	public void transto() throws Exception {
//		Web3j  web3 = Web3j.build(new HttpService("https://rinkeby.infura.io/a53a28a37ec943f6aa4ba5bbf8e1d24c"));
	
//		Credentials credentials = WalletUtils.loadCredentials("123456", "/Users/yimi/Desktop/myWallet/UTC--2018-11-29T05-42-41.351000000Z--e4d869165c4251f4b65c891774131072ed1a2af8.json");
//
//		// get the next available nonce
//		EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(
//		             "0xe4d869165c4251f4b65c891774131072ed1a2af8", DefaultBlockParameterName.LATEST).sendAsync().get();
//		BigInteger nonce = ethGetTransactionCount.getTransactionCount();
//
//		// create our transaction
//		RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(
//		             nonce,new BigInteger(String.valueOf(1000000000)),new BigInteger(String.valueOf(21000)), "0x5836cc7b00696fd24e33f01c85f50371d87e9fd0",new BigInteger(String.valueOf(11000)));
//
//		// sign & send our transaction
//		byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
//		String hexValue = Hex.toHexString(signedMessage);
//		// FROM here you can get the tx hash.
//		EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).send();
//		
		
		Web3j  web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/a53a28a37ec943f6aa4ba5bbf8e1d24c"));
		  String walleFilePath="/Users/yimi/Desktop/myWallet/UTC--2018-11-29T05-42-41.351000000Z--e4d869165c4251f4b65c891774131072ed1a2af8.json";
		    String passWord="123456";
		    Credentials credentials = WalletUtils.loadCredentials(passWord, walleFilePath);
		// Credentials credentials = Credentials.create("48720043169805750595012787903631596162466927398976498574556887910912704215634");
        if (web3j == null) return;
        if (credentials == null) return;
        //开始发送0.01 =eth到指定地址
        String address_to = "0x5836cc7b00696fd24e33f01c85f50371d87e9fd0";
        TransactionReceipt send = Transfer.sendFunds(web3j, credentials, address_to,new BigDecimal("0.0002"), Convert.Unit.ETHER).send();//BigDecimal.ONE
        System.out.println("Transaction complete:");
        System.out.println("trans hash=" + send.getTransactionHash());
        System.out.println("from :" + send.getFrom());
        System.out.println("to:" + send.getTo());
        System.out.println("gas used=" + send.getGasUsed());
        System.out.println("status: " + send.getStatus());
    }


	
	//测试交易
	/**
	 * 为了和Infura节点交易，你需要去创建并且在你发送之前进行离线签名交易，
	 * Infura节点无法看到你的加密的以太坊密钥文件，这需要通过你的私人Geth/Parity私有的管理命令来解锁。
	 * @throws IOException
	 * @throws CipherException 
	 * @throws Exception 
	 * @throws  
	 */
	public void testTx() throws Exception {
		Web3j  web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/a53a28a37ec943f6aa4ba5bbf8e1d24c"));

		Credentials credentials = WalletUtils.loadCredentials("123456", "/Users/yimi/Desktop/myWallet/UTC--2018-11-29T05-42-41.351000000Z--e4d869165c4251f4b65c891774131072ed1a2af8.json");
		
		// get the next available nonce
		EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
		             "0xe4d869165c4251f4b65c891774131072ed1a2af8", DefaultBlockParameterName.LATEST).sendAsync().get();
		BigInteger nonce = ethGetTransactionCount.getTransactionCount();

		// create our transaction
		RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(
		             nonce,new BigInteger(String.valueOf(100)),new BigInteger(String.valueOf(21000)), "0x5836cc7b00696fd24e33f01c85f50371d87e9fd0",new BigInteger(String.valueOf(11000)));
		// sign & send our transaction
		byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
		String hexValue = Numeric.toHexString(signedMessage);//Hex.
		// FROM here you can get the tx hash.
		EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
		
		System.out.println("------------------"+hexValue);
		
	}
	
	
	////////////////////////////////////////交易前判断会用到这三个方法
	public static BigInteger getNonce(Web3j web3j, String addr) {
		 try {
		     EthGetTransactionCount getNonce = web3j.ethGetTransactionCount(addr,DefaultBlockParameterName.PENDING).send();
		      if (getNonce == null){
		                throw new RuntimeException("net error");
		            }
		            return getNonce.getTransactionCount();
		        } catch (IOException e) {
		            throw new RuntimeException("net error");
		        }
		    }
	
	public static BigDecimal getBalance(Web3j web3j, String address) {
        try {
            EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            return Convert.fromWei(new BigDecimal(ethGetBalance.getBalance()),Convert.Unit.ETHER);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

	public static BigInteger getTransactionGasLimit(Web3j web3j, Transaction transaction) {
        try {
            EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();
            if (ethEstimateGas.hasError()){
                throw new RuntimeException(ethEstimateGas.getError().getMessage());
            }
            return ethEstimateGas.getAmountUsed();
        } catch (IOException e) {
            throw new RuntimeException("net error");
        }
    }

	//利用上面三个方法进行转账判断
	public static void transferETH(Web3j web3j, String fromAddr, String privateKey, String toAddr, BigDecimal amount, String data,BigInteger gasPrice){
        // 获得nonce
        BigInteger nonce = getNonce(web3j, fromAddr);
        // value 转换
        BigInteger value = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();

        // 构建交易
        Transaction transaction = Transaction.createEtherTransaction(fromAddr, nonce, gasPrice, null, toAddr, value);
        // 计算gasLimit
        BigInteger gasLimit = getTransactionGasLimit(web3j, transaction);

        System.out.println("gasLimit:-----"+gasLimit);
        // 查询调用者余额，检测余额是否充足
        BigDecimal ethBalance = getBalance(web3j, fromAddr);
        BigDecimal balance = Convert.toWei(ethBalance, Convert.Unit.ETHER);
        System.out.println("balance-------"+balance);

        // balance < amount + gasLimit ??
        if (balance.compareTo(amount.add(new BigDecimal(gasLimit.toString()))) < 0) {
           // throw new RuntimeException("余额不足，请核实");
        	System.out.println("------------余额不足，请核实---------------");
        }

     //   return signAndSend(web3j, nonce, gasPrice, gasLimit, toAddr, value, data, chainId, privateKey);
    }


	
public static void main(String[] args) throws Exception {
	Demo demo=new Demo();
//	demo.conectETHclient();
//	demo.getBlanceOf();
//	demo.testTx();
	
//	demo.transto();
	
	
	Web3j  web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/a53a28a37ec943f6aa4ba5bbf8e1d24c"));
    String fromAddr ="0xe4d869165c4251f4b65c891774131072ed1a2af8";
    String privateKey="48720043169805750595012787903631596162466927398976498574556887910912704215634";
    String toAddr="0x5836cc7b00696fd24e33f01c85f50371d87e9fd0";
    BigDecimal amount = new BigDecimal("100");
    String data = "";
    BigInteger gasPrice = new BigInteger("100");
	
  //  demo.transferETH(web3j, fromAddr, privateKey, toAddr, amount, data, gasPrice);
    
    demo.loadWallet();
    
    
}
}
