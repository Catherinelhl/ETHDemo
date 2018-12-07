package io.bcaas.spring.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

/***
 * 
 * @author yimi
 *
 */
public class EthController {

	/**
	 * 创建钱包
	 * 
	 * @param password
	 *            密码
	 * @param walletFilePath
	 *            钱包路径
	 * @param useFullScrypt
	 *            是否轻量级钱包
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
	 * @param password
	 *            钱包密码
	 * @param walleFilePath
	 *            钱包路径
	 * @throws Exception
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
	 * @throws IOException
	 */
	public void conectETHclient() {
		Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io"));
	}

	/**
	 * 查询指定地址的余额
	 * 
	 * @param web3j
	 *            Web3j
	 * @param address
	 *            查询余额的钱包地址
	 * @throws IOException
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
	 * 发送交易
	 * 
	 * @param web3j
	 *            Web3j
	 * @param walleFilePath
	 *            钱包地址
	 * @param password
	 *            钱包密码
	 * @param address_to
	 *            发送交易的地址
	 * @param value
	 *            发送交易的币数目
	 * @param unit
	 *            发送交易的币单位
	 * @throws Exception
	 */
	public Object transTo(Web3j web3j, String walleFilePath, String password, String address_to, String value,
			Unit unit) throws Exception {

		Credentials credentials = WalletUtils.loadCredentials(password, walleFilePath);
		if (web3j == null) {
			return null;
		}
		if (credentials == null) {
			return null;
		}
		// 开始发送交易到指定地址
		TransactionReceipt send = Transfer.sendFunds(web3j, credentials, address_to, new BigDecimal(value), unit)
				.send();
		System.out.println("Transaction complete:");
		System.out.println("trans hash=" + send.getTransactionHash());
		System.out.println("from :" + send.getFrom());
		System.out.println("to:" + send.getTo());
		System.out.println("gas used=" + send.getGasUsed());
		System.out.println("status: " + send.getStatus());
		return send.getTransactionHash();
	}

	/**
	 * 获取ETH的gasPrice
	 * 
	 * @param web3j
	 * @return
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
	 * @param address
	 *            钱包地址
	 * @return
	 */
	public static String getAllTransactions(String address) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		String url = "https://api.etherscan.io/api?module=account&action=txlist&address=" + address;
		HttpGet request = new HttpGet(url);
		try {
			HttpResponse response = client.execute(request);
			String result = EntityUtils.toString(response.getEntity());
			System.out.println("交易记录--------------------:" + result);
			return result;
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
		return null;
	}

}
