package io.bcaas.spring.frontcontroller;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import io.bcaas.spring.controller.Constants;

/**
 * 進入點, 初始化
 * 
 * @since 2018/07/01
 * 
 * @author Costa Peng
 * 
 * @version 1.0.0
 * 
 */

@Controller
public class RootController {

	@PostConstruct
	public void init() {
		// Initial ETH
		connectWeb3j();
	}

	/**
	 * 連上Web3j
	 * 
	 * @return Web3j
	 */
	public static Web3j connectWeb3j() {
		Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/7c4ebc9898924f0aa003ab19df9c36eb"));
		String web3ClientVersion = "";
		try {
			web3ClientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
			if (!StringUtils.isEmpty(web3ClientVersion)) {
				Constants.LOGGER_INFO.info("---[ETHTool]--- Connect to web3j, version: " + web3ClientVersion);
				return web3j;
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return null;
	}

}
