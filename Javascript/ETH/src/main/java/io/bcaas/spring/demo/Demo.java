package io.bcaas.spring.demo;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

public class Demo {

	// 生成钱包方式二（没有钱包json文件）
	public static JSONObject process(String seed) {

		JSONObject processJson = new JSONObject();
		try {
			ECKeyPair ecKeyPair = Keys.createEcKeyPair();
			BigInteger privateKeyInDec = ecKeyPair.getPrivateKey();
			String sPrivatekeyInHex = privateKeyInDec.toString(16);

			BigInteger publicKeyInDec = ecKeyPair.getPublicKey();
			String sPublickeyInHex = publicKeyInDec.toString(16);

			WalletFile aWallet = Wallet.createLight(seed, ecKeyPair);
			String sAddress = aWallet.getAddress();

			processJson.put("address", "0x" + sAddress);
			processJson.put("privatekey", sPrivatekeyInHex);

			System.out.println("sAddress==" + sAddress);
			System.out.println("sPrivatekeyInHex==" + sPrivatekeyInHex);
			System.out.println("sPublickeyInHex==" + sPublickeyInHex);
			/**
			 * sAddress==cd5d3760de1cfcd71acb7ab66c997998d19c7eeb
			 * sPrivatekeyInHex==c149ef71022986f7ee422f43a248de12a85eb85258de7351afab600ba6865596
			 * sPublickeyInHex==21fb78ad973712ab5f896aaa1b843fe2dbad831f5d156d4a5ffd0028b455a92d82457cf171d92e152a4c89ebfbd410ce274d3f683c20bf5ad56688ed0f5e343e
			 */
		} catch (Exception exception) {
			//
		}
		return processJson;
	}

	/*************** 创建钱包（生成钱包json文件） ***************/
	public void creatAccount() throws NoSuchAlgorithmException, NoSuchProviderException,
			InvalidAlgorithmParameterException, CipherException, IOException {
		String walletFileName0 = "";// 文件名
		String walletFilePath0 = "/Users/yimi/Desktop/myWallet";
		// 钱包文件保持路径，请替换位自己的某文件夹路径

		walletFileName0 = WalletUtils.generateNewWalletFile("123456", new File(walletFilePath0), false);
		// WalletUtils.generateFullNewWalletFile("password1",new File(walleFilePath1));
		// WalletUtils.generateLightNewWalletFile("password2",new File(walleFilePath2));
		System.out.println("walletName: " + walletFileName0);
	}

	/************** 加载钱包文件 ****************/
	private void loadWallet() throws IOException, CipherException {
		String walleFilePath = "/Users/yimi/Desktop/myWallet/UTC--2018-11-29T05-42-41.351000000Z--e4d869165c4251f4b65c891774131072ed1a2af8.json";
		String passWord = "123456";
		Credentials credentials = WalletUtils.loadCredentials(passWord, walleFilePath);

		String address = credentials.getAddress();
		BigInteger publicKey = credentials.getEcKeyPair().getPublicKey();
		BigInteger privateKey = credentials.getEcKeyPair().getPrivateKey();

		System.out.println("address=" + address);
		System.out.println("public key=" + publicKey);
		System.out.println("private key=" + privateKey);

	}

	/******* 连接以太坊客户端 **************/
	public void conectETHclient() throws IOException {
		// 连接方式1：使用infura 提供的客户端
		Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io"));
		// 连接方式2：使用本地客户端
		// Web3j web3j = Web3j.build(new HttpService("127.0.0.1:8545"));
		// 测试是否连接成功
		String web3ClientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
		System.out.println("web3ClientVersion:----------------" + web3ClientVersion);
	}

	/*********** 查询指定地址的余额 ***********/
	public void getBlanceOf() throws IOException {
		Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io"));
		if (web3j == null) return;
		EthGetBalance balance = web3j.ethGetBalance("0x5836cc7b00696fd24e33f01c85f50371d87e9fd0", DefaultBlockParameterName.LATEST).send();
		System.out.println("balance="+balance);
		// 格式转化 wei-ether
		String blanceETH = Convert.fromWei(new BigDecimal(balance.getBalance()), Convert.Unit.ETHER).toPlainString()
				.concat("ether");
		System.out.println(blanceETH);
	}

	// 简单交易(使用系统自定义矿工费用)
	public static void transto() throws Exception {
		// 1加载钱包文件方式
		Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io"));
		String walleFilePath = "/Users/yimi/Desktop/myWallet/UTC--2018-11-29T05-42-41.351000000Z--e4d869165c4251f4b65c891774131072ed1a2af8.json";
		String passWord = "123456";
		Credentials credentials = WalletUtils.loadCredentials(passWord, walleFilePath);
		if (web3j == null)
			return;
		if (credentials == null)
			return;
		// 开始eth到指定地址
		String address_to = "0x5836cc7b00696fd24e33f01c85f50371d87e9fd0";
		try {
			TransactionReceipt send = Transfer
					.sendFunds(web3j, credentials, address_to, new BigDecimal("0.000002"), Convert.Unit.ETHER).send();// BigDecimal.ONE
			System.out.println("Transaction complete:");
			System.out.println("trans hash=" + send.getTransactionHash());
			System.out.println("from :" + send.getFrom());
			System.out.println("to:" + send.getTo());
			System.out.println("gas used=" + send.getGasUsed());
			System.out.println("status: " + send.getStatus());
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}

	}

	// 带签名的交易（可自定义矿工费用）
	public static void transTo3(Web3j web3j, Credentials credentials, BigInteger GAS_PRICE, BigInteger GAS_LIMIT,
			String fromAddress, String toAddress, String value) throws Exception {
		// 设置需要的矿工费
		// BigInteger GAS_PRICE = BigInteger.valueOf(22_000_000_000L);//22Gwei
		// BigInteger GAS_LIMIT = BigInteger.valueOf(21000L);
		// Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io"));
		// 转账人账户地址
		// String fromAddress = "0xe4d869165c4251f4b65c891774131072ed1a2af8";
		// 接受人账户地址
		// String toAddress = "0x5836cc7b00696fd24e33f01c85f50371d87e9fd0";
		// 交易方式
		// 1私钥的形式

		// 2加载钱包文件的形式
		// Credentials credentials = WalletUtils.loadCredentials("123456",
		// "/Users/yimi/Desktop/myWallet/UTC--2018-11-29T05-42-41.351000000Z--e4d869165c4251f4b65c891774131072ed1a2af8.json");

		// getNonce
		EthGetTransactionCount ethGetTransactionCount = web3j
				.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
		BigInteger nonce = ethGetTransactionCount.getTransactionCount();

		// 创建交易
		BigInteger value_BigInteger = Convert.toWei(value, Convert.Unit.ETHER).toBigInteger();
		RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, GAS_PRICE, GAS_LIMIT, toAddress,
				value_BigInteger);

		// 签名Transaction，这里要对交易做签名
		byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
		String hexValue = Numeric.toHexString(signedMessage);

		// 发送交易
		EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
		String transactionHash = ethSendTransaction.getTransactionHash();
		System.out.println("RawResponse=" + ethSendTransaction.getRawResponse());
		if (ethSendTransaction.getError() != null) {
			System.out.println("errorMessage=" + ethSendTransaction.getError().getMessage());
			System.out.println("errorCode=" + ethSendTransaction.getError().getCode());
		}
		System.out.println("TxHash=" + transactionHash);
	}

	// 不带签名（可自定义矿工费用2）
	public static Object transTo2(Web3j web3j, Credentials credentials, BigInteger GAS_PRICE, BigInteger GAS_LIMIT,
			String toAddress, BigDecimal value) {
		TransactionManager transactionManager = new RawTransactionManager(web3j, credentials);
		Transfer transfer = new Transfer(web3j, transactionManager);
		TransactionReceipt send = null;
		try {
			send = transfer.sendFunds(toAddress, value, Convert.Unit.ETHER, GAS_PRICE, GAS_LIMIT).send();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		System.out.println("Transaction complete:");
		System.out.println("trans hash=" + send.getTransactionHash());
		System.out.println("from :" + send.getFrom());
		System.out.println("to:" + send.getTo());
		System.out.println("gas used=" + send.getGasUsed());
		System.out.println("status: " + send.getStatus());
		return send.getTransactionHash();
	}

	// 对于失败的交易信息查询不到，根据API查询交易信息
	public static String getTransactionByHash(String txHash) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		String url = "https://api.etherscan.io/api?module=transaction&action=gettxreceiptstatus&txhash=" + txHash;
		HttpGet request = new HttpGet(url);
		try {
			HttpResponse response = client.execute(request);
			String result = EntityUtils.toString(response.getEntity());
			System.out.println("交易信息--------------------:" + result);
			return result;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}

	public static BigInteger getNonce(Web3j web3j, String addr) {
		try {
			EthGetTransactionCount getNonce = web3j.ethGetTransactionCount(addr, DefaultBlockParameterName.PENDING)
					.send();

			if (getNonce == null) {
				throw new RuntimeException("net error");
			}
			return getNonce.getTransactionCount();
		} catch (IOException e) {
			throw new RuntimeException("net error");
		}
	}

	public static BigInteger getTransactionGasLimit(Web3j web3j, Transaction transaction) {
		try {
			EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();
			if (ethEstimateGas.hasError()) {
				throw new RuntimeException(ethEstimateGas.getError().getMessage());
			}
			return ethEstimateGas.getAmountUsed();
		} catch (IOException e) {
			throw new RuntimeException("net error");
		}
	}

	public static void main(String[] args) throws Exception {
		Demo demo = new Demo();
		// 调用带签名的交易（加载钱包文件的方式）
		Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io"));
		 Credentials credentials = WalletUtils.loadCredentials("123456",
		 "/Users/yimi/Desktop/myWallet/UTC--2018-11-29T05-42-41.351000000Z--e4d869165c4251f4b65c891774131072ed1a2af8.json");
		 BigInteger GAS_PRICE = BigInteger.valueOf(2_000_000_000L);// 2Gwei
		 BigInteger GAS_LIMIT = BigInteger.valueOf(22000L);
		 String fromAddress = "0xe4d869165c4251f4b65c891774131072ed1a2af8";
		 String toAddress = "0x5836cc7b00696fd24e33f01c85f50371d87e9fd0";
		 String value = "0.000000000000033";
		 //demo.transTo3(web3j, credentials, GAS_PRICE, GAS_LIMIT, fromAddress,toAddress, value);
		// demo.transTo2(web3j, credentials, GAS_PRICE, GAS_LIMIT, toAddress, new BigDecimal("0.000000001"));

		/** 根据交易哈希值查询交易状态 **/
	//	 GetTransactionByHash返回的gas是传过去的值 blocknumber有可能为空（正在进行的交易，blocknumber为null）
	//	 GetTransactionReceipt 返回的gas是真实交易消耗的 blocknumber不为空(正在进行的交易查询不到)
		 EthTransaction a = web3j.ethGetTransactionByHash("0x9216bf5a69664be357ab0ff53a8c21cb138589f983510937e439bfc89ab2b803").send();
		 System.out.println(a.getTransaction());
		 System.out.println(a.getResult());
		 System.out.println(a.getError());
		 EthGetTransactionReceipt b = web3j.ethGetTransactionReceipt("0x9216bf5a69664be357ab0ff53a8c21cb138589f983510937e439bfc89ab2b803").send();
		 System.out.println(b.getResult());
		 System.out.println(b.getError());
		 System.out.println(b.getResult());

		// 对于失败的交易记录，不能查询到交易信息。现在采用接API的方式：
		 demo.getTransactionByHash("0x9216bf5a69664be357ab0ff53a8c21cb138589f983510937e439bfc89ab2b803");
		// 错误交易或没有被确认的交易
		// {"status":"1","message":"OK","result":{"status":""}}
		// 成功的交易
		// {"status":"1","message":"OK","result":{"status":"1"}}

		//测试用私钥发送交易
		// BigInteger privateKeyInBT = new
		// BigInteger("48720043169805750595012787903631596162466927398976498574556887910912704215634",
		// 16);
		// ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);
		// BigInteger privateKey = new
		// BigInteger("4732634301386064809375711616131879978825255988073844903514572124860663802539217019239286274283719724363771215996631683197188825846647125772392298911916762",
		// 16);
		// BigInteger publicKey = new
		// BigInteger("48720043169805750595012787903631596162466927398976498574556887910912704215634",
		// 16);
		// ECKeyPair pair = new ECKeyPair(privateKey, publicKey);
		// pair.sign("".getBytes());
		// Credentials credential = Credentials.create(pair);
//		Credentials credential = Credentials.create(
//				"4732634301386064809375711616131879978825255988073844903514572124860663802539217019239286274283719724363771215996631683197188825846647125772392298911916762");
//
//		BigInteger GAS_PRICE = BigInteger.valueOf(2_000_000_000L);// 2Gwei
//		BigInteger GAS_LIMIT = BigInteger.valueOf(22000L);
//		TransactionManager transactionManager = new RawTransactionManager(web3j, credential);
//		Transfer transfer = new Transfer(web3j, transactionManager);
//		TransactionReceipt send = null;
//		try {
//			send = transfer.sendFunds("0x5836cc7b00696fd24e33f01c85f50371d87e9fd0", new BigDecimal("0.0000000001"),
//					Convert.Unit.ETHER, GAS_PRICE, GAS_LIMIT).send();
//		} catch (Exception exception) {
//			exception.printStackTrace();
//		}
		
	}

}
