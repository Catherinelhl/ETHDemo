package io.bcaas.spring.frontcontroller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;
import io.bcaas.spring.controller.EthController;

@Controller
@RequestMapping(value = "/eth/")
public class EthFrontController {

	// 进入主页面
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String eth(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			ModelMap modelMap) {

		// Get parameters & Set parameters

		return "jsp/eth";
	}

	// 生成钱包
	@RequestMapping(value = "/creatWallet", method = RequestMethod.POST)
	@ResponseBody
	public Object creatWallet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

		String walleFilePath = httpServletRequest.getParameter("createWalletFile");
		String password = httpServletRequest.getParameter("addressPassword");

		Object data = null;
		Object json = "";
		try {
			EthController ethController = new EthController();
			data = ethController.creatWallet(password, walleFilePath, false);
			json = "{" + "\"walletFileName\": \"" + data + "\"" + "}";
			System.out.println("/creatWallet--data==" + data);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return json;
	}

	// 加载钱包(根据钱包文件获取钱包地址)
	@RequestMapping(value = "/loadWallet", method = RequestMethod.POST)
	@ResponseBody
	public Object loadWallet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

		String walleFilePath = httpServletRequest.getParameter("selectWalletFile");
		String password = httpServletRequest.getParameter("addressPassword");

		Object json = "";
		try {
			EthController ethController = new EthController();
			Object data = ethController.loadWallet(password, walleFilePath);
			json = "{" + "\"Walletaddress\": \"" + data + "\"" + "}";
			System.out.println("/loadWallet---data:" + json);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return json;
	}

	// 查询余额
	@RequestMapping(value = "/getBlance", method = RequestMethod.POST)
	@ResponseBody
	public Object getBlance(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

		Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/a53a28a37ec943f6aa4ba5bbf8e1d24c"));
		String address = httpServletRequest.getParameter("address");

		EthController ethController = new EthController();
		Object data = null;

		try {
			data = ethController.getBlance(web3j, address);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return data;
	}

	// 交易
	@RequestMapping(value = "/transTo", method = RequestMethod.POST)
	@ResponseBody
	public Object transTo(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

		Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/a53a28a37ec943f6aa4ba5bbf8e1d24c"));

		String walleFilePath = httpServletRequest.getParameter("selectWalletFile");
		String password = httpServletRequest.getParameter("addressPassword");
		String address_to = httpServletRequest.getParameter("toAddressInput");
		String value = httpServletRequest.getParameter("sendMoneyInput");
		Unit unit = Convert.Unit.ETHER;
		EthController ethController = new EthController();
		Object data = null;
		Object json = null;
		try {
			data = ethController.transTo(web3j, walleFilePath, password, address_to, value, unit);
			json = "{" + "\"transactionHash\": \"" + data + "\"" + "}";
			System.out.println("/transTo--json==" + json);

		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return json;
	}

	// 获取getGasPrice手续费
	@RequestMapping(value = "/getGasPrice", method = RequestMethod.POST)
	@ResponseBody
	public Object getGasPrice(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

		Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/a53a28a37ec943f6aa4ba5bbf8e1d24c"));

		EthController ethController = new EthController();
		Object data = null;
		try {
			data = ethController.getGasPrice(web3j);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return data;
	}

	// 获取账号的交易记录
	@RequestMapping(value = "/getAllTransactions", method = RequestMethod.POST)
	@ResponseBody
	public Object getAllTransactions(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

		EthController ethController = new EthController();
		Object data = null;
		// 获取需要查询的交易钱包账号
		String address = httpServletRequest.getParameter("address");
		try {
			data = ethController.getAllTransactions(address);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return data;
	}

	/**
	 * 测试web3js前端实现测试
	 * 
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/eth2/", method = RequestMethod.GET)
	public String eth2(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			ModelMap modelMap) {

		// Get parameters & Set parameters

		return "jsp/eth2";
	}

}
