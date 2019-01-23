//
//  RootViewController.swift
//  BTC_Sample
//
//  Created by Dong on 2018/11/13.
//  Copyright © 2018 orangeblock.com. All rights reserved.
//

import UIKit
import RxSwift
import RxCocoa
import SwiftyJSON
import Alamofire
import Toast_Swift
import web3swift
import BigInt

class RootViewController: UIViewController {
    @IBOutlet weak var myAddressTextFiled: UITextField!
    @IBOutlet weak var reciveAddressLabel: UILabel!
    
    @IBOutlet weak var reciveAddressTextField: UITextField!
    @IBOutlet weak var balanceLabel: UILabel!
    @IBOutlet weak var sendAmountTextField: UITextField!
    @IBOutlet weak var jsonDataTextView: UITextView!
    
    @IBOutlet weak var symbolLabel: UILabel!
    @IBOutlet weak var feesLabel: UILabel!
//    @IBOutlet weak var txHashTextField: UITextField!
    
    private var balance:Decimal = 0

    /// 初始化 web3Main
    private var web3Main = Web3.init(infura: infura, accessToken: accessToken)

    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.view.backgroundColor = .white
        
//        myAddressLabel.text = "我的地址：" + myAddress
        symbolLabel.text = currencySymbol
        feesLabel.text = "手续费：\(fees) " + currencySymbol
        
//        myAddressLabel.adjustsFontSizeToFitWidth = true
        feesLabel.adjustsFontSizeToFitWidth = true
        
        // 向Web3对象中添加keystore数据（用于私钥签名交易）
        if let keystoreManager = EthereumTool.getKeystoreManager(by: myPrivateKey) {
            web3Main.addKeystoreManager(keystoreManager)
        }
        // 获取余额 api返回数据单位为 wei （1 ETH = 10^18 wei）
        getBalnce()
        getGasPrice()
        
        setNavigationItem()
    }
    
    private func setNavigationItem() {
        self.title = "BTC测试网络"
        let switchButton = UISwitch()
        switchButton.isOn = true
        self.navigationItem.rightBarButtonItem = UIBarButtonItem(customView: switchButton)
        
        _ = switchButton.rx.value.subscribe(onNext: {[weak self] (value) in
            if value {
                coinType = .ethTest
                self?.title = "ETH测试网络"
            }else {
                coinType = .ethMain
                self?.title = "ETH主网络"
            }
            self?.web3Main = Web3.init(infura: infura, accessToken: accessToken)
            myAddress = ""
            myPrivateKey = ""
//            myPublicKey = ""
            
            self?.myAddressTextFiled.text = myAddress
            self?.balanceLabel.text = "0" + " " + currencySymbol
            self?.symbolLabel.text = currencySymbol
            self?.feesLabel.text = "手续费：\(fees) " + currencySymbol
            self?.jsonDataTextView.text = ""
            self?.getBalnce()
            self?.getGasPrice()
        })
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    @IBAction func getBalanceButtonAction(_ sender: UIButton) {
        getBalnce()
        getGasPrice()
//        EthereumTool.generateETHKey()
    }
    
    @IBAction func sendTxButtonAction(_ sender: UIButton) {
        
        createAndSendTx()
    }
    
    @IBAction func getTxRecordButtonAction(_ sender: Any) {
        
        getTxRecord()
    }
    
    // MARK: 扫描私钥按钮点击事件
    @IBAction func scanPrivateKeyButtonAction(_ sender: UIButton) {
        let scanVc = ScanViewController()
        scanVc.resultBlock = { result in
            if let ethKeyStore = EthereumTool.generateETHKeyBy(result) {
                myAddress = ethKeyStore.address ?? ""
                myPrivateKey = ethKeyStore.privateKey ?? ""
                self.myAddressTextFiled.text = ethKeyStore.address
                // 向Web3对象中添加keystore数据（用于私钥签名交易）
                if let keystoreManager = EthereumTool.getKeystoreManager(by: myPrivateKey) {
                    self.web3Main.addKeystoreManager(keystoreManager)
                }
                self.getBalnce()
                self.getGasPrice()
            }
        }
        
        self.navigationController?.pushViewController(scanVc, animated: true)
        
    }
    
    @IBAction func qrCodeButtonAction(_ sender: Any) {
        let qrCodeVc = QRCodeViewController.init(text: myAddress)
        self.navigationController?.pushViewController(qrCodeVc, animated: true)
    }
    @IBAction func scanButtonAction(_ sender: UIButton) {
        
        let scanVc = ScanViewController()
        scanVc.resultBlock = { result in
            self.reciveAddressTextField.text = result
        }
        
        self.navigationController?.pushViewController(scanVc, animated: true)
    }
    
//    @IBAction func getTxDetailButtonAction(_ sender: UIButton) {
//
//    }
    
    
    // MARK: - Api request
    // MARK: 获取余额
    private func getBalnce() {
        
        web3Main.eth.getBalancePromise(address: myAddress).done {[weak self] (balance) in
            MyLog("balance:\(balance.description)")
            let balance = NSDecimalNumber.init(string: balance.description).decimalValue
            self?.balance = balance
            self?.balanceLabel.text = "\(balance / rate)" + " " + currencySymbol
        }.catch { (aError) in
            MyLog(aError)
        }
        
        
    }
    // MARK: 获取gas_price
    private func getGasPrice() {
        
        web3Main.eth.getGasPricePromise().done {[weak self] (price) in
            MyLog("gesPrice:\(price.description)")
            gasPrice = NSDecimalNumber(string: price.description).decimalValue
            self?.feesLabel.text = "手续费：\(fees) " + currencySymbol
        }.catch { (aError) in
            MyLog(aError)
        }
    }
    
    // MARK: 创建&发送交易
    private func createAndSendTx() {
        let reciveAddress = reciveAddressTextField.text!.trimmingCharacters(in: .whitespacesAndNewlines)
        guard reciveAddress != "" else {
            self.view.showToast("收款地址不能为空")
            return
        }
        guard let amount = Decimal(string:sendAmountTextField.text!),amount > 0 else {
            self.view.showToast("发送金额有误")
            return
        }
        
        guard amount + fees <= balance else {
            self.view.showToast("余额不足")
            return
        }
        
        guard reciveAddress.isAddress else {
            self.view.makeToast("收款地址格式有误")
            return
        }
        // 设置交易参数
        MyLog("发送交易")
        let toAddress = Address(reciveAddress)
        let amountWei = BigUInt((amount * rate).intValue)
        var options = Web3Options()
        // 发送地址
        options.from = Address(myAddress)
        // 接收地址
        options.to = toAddress
        // gasLimit
        options.gasLimit = BigUInt(21000)
        // gas_price
        options.gasPrice = BigUInt(gasPrice.intValue)
        // 发送金额
        options.value = amountWei
        
        // 发送交易
        do {
            try web3Main.eth.sendETH(to: toAddress, amount: amountWei, options: options).sendPromise(password: EthereumTool.password).done {[weak self] (sendingResult) in
                MyLog(sendingResult.transaction.description)
                MyLog(sendingResult.hash)
                self?.jsonDataTextView.text = sendingResult.transaction.description + "\n" + sendingResult.hash
                self?.getBalnce()
            }.catch { (aError) in
                MyLog(aError)
            }
        }catch let aError{
            MyLog(aError)
        }
   
        
    }
 
    
    // MARK: 获取交易记录
    private func getTxRecord() {
        
        ApiManagerProvider.request(.getTxRecord(address: myAddress)) { (result) in
            switch result {
            case .success(let response):
                do{
                    let value = try response.mapJSON()
                    self.jsonDataTextView.text = "\(value)"
                }catch let aError {
                    MyLog(aError)
                }
            case .failure(let aError):
                MyLog(aError)
            }
        }
        
    }
    
//    // MARK: 通过交易hash获取交易详情
//    private func getTxDetail() {
//        let txHash = txHashTextField.text!.trimmingCharacters(in: .whitespacesAndNewlines)
//        guard txHash != "" else {
//            self.view.showToast("交易hash值不能为空")
//            return
//        }
//
//        web3Main.eth.getTransactionDetailsPromise(txHash).done { (txDetail) in
//
//        }.catch { (aError) in
//            MyLog(aError)
//        }
//
//    }
 
}


extension UIView {
    func showToast(_ message:String?) {
        self.makeToast(message,position:.center)
    }
}
