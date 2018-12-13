package io.bcaas.spring.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ChainId;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import org.web3j.utils.Convert.Unit;
import com.fasterxml.jackson.databind.ObjectMapper;


/***
 * 
 * @author yimi
 *
 */
public class EthController {

	/**
	 * 创建钱包
	 * 
	 * @param password 密码
	 *            
	 * @param walletFilePath 钱包路径
	 *            
	 * @param useFullScrypt 是否轻量级钱包
	 *            
	 */
	public Object creatWallet(String password, String walletFilePath, Boolean useFullScrypt) {
		String walletFileName = "";
		try {
			walletFileName = WalletUtils.generateNewWalletFile(password, new File(walletFilePath), false);
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
		return walletFileName;
	}

	/**
	 * 加载钱包文件
	 * 
	 * @param password  钱包密码
	 *           
	 * @param walleFilePath  钱包路径
	 *           
	 */
	public Object loadWallet(String password, String walleFilePath) {
		String address = "";
		try {
			Credentials credentials = WalletUtils.loadCredentials(password, walleFilePath);
			address = credentials.getAddress();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return address;
	}

	/**
	 * 连接以太坊客户端
	 * 
	 */
	public void conectETHclient() {
		Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io"));
	}

	/**
	 * 查询指定地址的余额
	 * 
	 * @param web3j  Web3j对象
	 *           
	 * @param address 查询余额的钱包地址
	 *            
	 */
	public Object getBlance(Web3j web3j, String address) {
		if (web3j == null) {
			return null;
		}
		EthGetBalance balance = null;
		try {
			balance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 格式转化 wei-ether
		String blanceETH = Convert.fromWei(new BigDecimal(balance.getBalance()), Convert.Unit.ETHER).toPlainString();
		System.out.println(blanceETH);
		return blanceETH;

	}

	/**
	 * 获取ETH的gasPrice
	 * 
	 * @param web3j Web3j对象
	 * @return 余额
	 */
	public Object getGasPrice(Web3j web3j) {
		String ethGasPrice = "";
		try {
			ethGasPrice = Convert
					.fromWei(new BigDecimal(web3j.ethGasPrice().send().getGasPrice().toString()), Convert.Unit.ETHER)
					.toPlainString();
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
		return ethGasPrice;
	}

	/**
	 * 获取指定账号的交易记录
	 * 
	 * @param address  钱包地址
	 *           
	 * @return 交易记录
	 */
	public static String getAllTransactions(String address) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		String url = "https://api.etherscan.io/api?module=account&action=txlist&sort=desc&address=" + address;
		HttpGet request = new HttpGet(url);
		try {
			HttpResponse response = client.execute(request);
			String result = EntityUtils.toString(response.getEntity());
			System.out.println("交易记录--------------------:" + result);
			return result;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}

	
	/**
	 * 发送交易(使用系统自定义矿工费用)
	 * 
	 * @param web3j  Web3j
	 *           
	 * @param walleFilePath 钱包地址
	 *            
	 * @param password 钱包密码
	 *            
	 * @param address_to 发送交易的地址
	 *            
	 * @param value 发送交易的币数目
	 *            
	 * @param unit 发送交易的币单位
	 *            
	 */
	public Object transTo(Web3j web3j, String walleFilePath, String password, String toAddress, String value, Unit unit){

		Credentials credentials = null;
		try {
			credentials = WalletUtils.loadCredentials(password, walleFilePath);
		} catch (Exception exception) {
			exception.printStackTrace();
		} 
		if (web3j == null) {
			return null;
		}
		if (credentials == null) {
			return null;
		}
		// 开始发送交易到指定地址
		TransactionReceipt send = null;
		try {
			send = Transfer.sendFunds(web3j, credentials, toAddress, new BigDecimal(value), unit).send();
		} catch (Exception exception) {
			// TODO Auto-generated catch block
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

	/**
	 * （可自定义矿工费用）不带签名
	 * 
	 * @param web3j  Web3j对象
	 *           
	 * @param credentials  Credentials对象
	 *           
	 * @param GAS_PRICE 自定义的燃料费用
	 *            
	 * @param GAS_LIMIT 自定义燃料最高限制
	 *            
	 * @param toAddress 接受交易者钱包地址
	 *            
	 * @param value  发送交易ether金额
	 *           
	 * @return String TxHash 交易号
	 */
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

	/**
	 * （可自定义矿工费用）带签名的交易
	 * 
	 * @param web3j  Web3j对象
	 *           
	 * @param credentials  Credentials对象
	 *           
	 * @param GAS_PRICE 自定义的燃料费用
	 *            
	 * @param GAS_LIMIT 自定义燃料最高限制
	 *            
	 * @param fromAddress 发送交易者钱包地址
	 *            
	 * @param toAddress 接受交易者钱包地址
	 *            
	 * @param value 发送交易ether金额
	 *            
	 * @return String TxHash 交易号
	 */
	public static Object transTo3(Web3j web3j, Credentials credentials, BigInteger GAS_PRICE, BigInteger GAS_LIMIT,
			String fromAddress, String toAddress, String value) {

		// getNonce
		EthGetTransactionCount ethGetTransactionCount = null;
		try {
			ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST)
					.sendAsync().get();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		BigInteger nonce = ethGetTransactionCount.getTransactionCount();

		// 创建交易
		BigInteger value_BigInteger = Convert.toWei(value, Convert.Unit.ETHER).toBigInteger();
		RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, GAS_PRICE, GAS_LIMIT, toAddress,
				value_BigInteger);

		// 签名Transaction，这里要对交易做签名
		byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
		String hexValue = Numeric.toHexString(signedMessage);

		// 发送交易
		EthSendTransaction ethSendTransaction = null;
		try {
			ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		String transactionHash = ethSendTransaction.getTransactionHash();
		System.out.println("result=" + ethSendTransaction.getResult());
		System.out.println("TxHash=" + transactionHash);
		if (ethSendTransaction.getError() != null) {
			System.out.println("errorMessage=" + ethSendTransaction.getError().getMessage());
			System.out.println("errorCode=" + ethSendTransaction.getError().getCode());
		}
		return transactionHash;
	}

	
	//以下为项目中暂时没有用到的另一种方案
	/**
	 * 创建钱包（不生成file文件，生成json文件形式）
	 *
	 * @param password 密码
	 *            
	 */
	public static void createWallet(String password) {
		WalletFile walletFile = null;
		ECKeyPair ecKeyPair = null;
		try {
			ecKeyPair = Keys.createEcKeyPair();
			walletFile = Wallet.createStandard(password, ecKeyPair);
			System.out.println("address = " +"0x"+ walletFile.getAddress());
			ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
			String jsonStr = objectMapper.writeValueAsString(walletFile);
			System.out.println("keystore json file " + jsonStr);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * 解密keystore 得到私钥及公钥
	 *
	 * @param keystore 钱包生成的json文件
	 * @param password 创建钱包时的密码
	 */
	public static String decryptWallet(String keystore, String password) {
		String privateKey = null;
		String publicKey = null;
		ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
		try {
			WalletFile walletFile = objectMapper.readValue(keystore, WalletFile.class);
			ECKeyPair ecKeyPair = null;
			ecKeyPair = Wallet.decrypt(password, walletFile);
			privateKey = ecKeyPair.getPrivateKey().toString(16);
			System.out.println("privateKey = " + privateKey);
			publicKey = ecKeyPair.getPublicKey().toString(16);
			System.out.println("publicKey = " + publicKey);
		} catch (CipherException e) {
			if ("Invalid password provided".equals(e.getMessage())) {
				System.out.println("密码错误");
			}
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return privateKey;
	}

	/***
	 * 利用私钥发送交易
	 * @param web3j Web3j对象
	 * @param GAS_PRICE 燃料价格
	 * @param GAS_LIMIT 燃料最大限度
	 * @param fromAddress 发送交易者钱包地址
	 * @param toAddress 接受交易者钱包地址
	 * @param value 交易的eth金额
	 * @param privateKey 发送者私钥
	 */
	public static void transToByPrivateKey(Web3j web3j, BigInteger GAS_PRICE, BigInteger GAS_LIMIT,
			String fromAddress, String toAddress, String value,String privateKey) {
		BigInteger nonce = null;
		EthGetTransactionCount ethGetTransactionCount = null;
		try {
			ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).send();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (ethGetTransactionCount == null)
			return;
		nonce = ethGetTransactionCount.getTransactionCount();
		BigInteger valueEther = Convert.toWei(new BigDecimal(value), Convert.Unit.ETHER).toBigInteger();
		String data = "";
		byte chainId = ChainId.MAINNET;//主网络
		String signedData;
		try {
			signedData = signTransaction(nonce, GAS_PRICE, GAS_LIMIT, toAddress, valueEther, data, chainId, privateKey);
			if (signedData != null) {
				EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedData).send();
				System.out.println("txHash=" + ethSendTransaction.getTransactionHash());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 利用私钥签名交易
	 */
	public static String signTransaction(BigInteger nonce, BigInteger GAS_PRICE, BigInteger GAS_LIMIT, String to,
			BigInteger value, String data, byte chainId, String privateKey) throws IOException {
		byte[] signedMessage;
		RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, GAS_PRICE, GAS_LIMIT, to, value, data);

		if (privateKey.startsWith("0x")) {
			privateKey = privateKey.substring(2);
		}
		ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
		Credentials credentials = Credentials.create(ecKeyPair);

		if (chainId > ChainId.NONE) {
			signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
		} else {
			signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
		}

		String hexValue = Numeric.toHexString(signedMessage);
		return hexValue;
	}

	/**根据交易的哈希值获取交易信息(方式一)
	 * @param web3j Web3j对象
	 * @param txHash 传入查询的哈希值
	 * @return EthTransaction对象
	 */
	public static EthTransaction getTransactionByHash(Web3j web3j, String txHash) {
		EthTransaction ethTransaction = null;
		try {
			ethTransaction = web3j.ethGetTransactionByHash(txHash).send();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return ethTransaction;
	}

	/**根据交易的哈希值获取交易信息(方式二)
	 * @param web3j Web3j对象
	 * @param txHash 传入查询的哈希值
	 * @return EthGetTransactionReceipt对象
	 */
	public static EthGetTransactionReceipt getTransactionByHash2(Web3j web3j, String txHash) {
		EthGetTransactionReceipt ethGetTransactionReceipt = null;
		try {
			ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(txHash).send();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return ethGetTransactionReceipt;
	}
		
	/**根据交易的哈希值获取交易信息(方式三)
	 * @param txHash 传入查询的哈希值
	 * @return json结果
	 */
	public static String getTransactionByHash3(String txHash) {
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
	
}
