//
//  RootViewController.swift
//  BTC_Sample
//
//  Created by Dong on 2018/11/13.
//  Copyright © 2018 orangeblock.com. All rights reserved.
//

import UIKit
import SwiftyJSON
import Alamofire
import Toast_Swift
import web3swift
import BigInt

class RootViewController: UIViewController {
    @IBOutlet weak var myAddressLabel: UILabel!
    @IBOutlet weak var reciveAddressLabel: UILabel!
    
    @IBOutlet weak var reciveAddressTextField: UITextField!
    @IBOutlet weak var balanceLabel: UILabel!
    @IBOutlet weak var sendAmountTextField: UITextField!
    @IBOutlet weak var jsonDataTextView: UITextView!
    
    @IBOutlet weak var symbolLabel: UILabel!
    @IBOutlet weak var feesLabel: UILabel!
    
    private var balance:Decimal = 0

    private let web3Main = Web3.init(infura: infura, accessToken: "v3/c716564ad9c346c895e36bae02ea5c8c")

    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.view.backgroundColor = .white
        
        myAddressLabel.text = "我的地址：" + myAddress
        symbolLabel.text = currencySymbol
        feesLabel.text = "手续费：\(fees) " + currencySymbol
        
        myAddressLabel.adjustsFontSizeToFitWidth = true
        feesLabel.adjustsFontSizeToFitWidth = true
        
        if let keystoreManager = EthereumTool.getKeystoreManager(by: myPrivateKey) {
            web3Main.addKeystoreManager(keystoreManager)
        }
        getBalnce()
        getGasPrice()
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    @IBAction func getBalanceButtonAction(_ sender: UIButton) {
        getBalnce()
        getGasPrice()
    }
    
    @IBAction func sendTxButtonAction(_ sender: UIButton) {
        
        createAndSendTx()
    }
    
    @IBAction func getTxRecordButtonAction(_ sender: Any) {
        
        getTxRecord()
    }
    
    @IBAction func qrCodeButtonAction(_ sender: Any) {
        let qrCodeVc = UINavigationController(rootViewController: QRCodeViewController.init(text: myAddress))
        self.present(qrCodeVc, animated: true, completion: nil)
    }
    @IBAction func scanButtonAction(_ sender: UIButton) {
        
        let scanVc = ScanViewController()
        scanVc.delegate = self
        
        self.present(UINavigationController(rootViewController: scanVc), animated: true, completion: nil)
    }
    
    
    
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
        
        MyLog("发送交易")
        let toAddress = Address(reciveAddress)
        let amountWei = BigUInt((amount * rate).intValue)
        var options = Web3Options()
        options.from = Address(myAddress)
        options.to = toAddress
        options.gasLimit = BigUInt(21000)
        options.gasPrice = BigUInt(gasPrice.intValue)
        options.value = amountWei
        
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
 
}


extension RootViewController : ScanViewControllerDelegate {
    func didReciveScanResult(_ result: String) {
        
        reciveAddressTextField.text = result
    }
}


extension UIView {
    func showToast(_ message:String?) {
        self.makeToast(message,position:.center)
    }
}
